/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.client.functions.json;

import com.google.gson.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class JsonHtmlFunctions {

  JsonHtmlFunctions(JsonMTSTypeConversion converter) {
    typeConversion = converter;
  }

  private static final Logger log = LogManager.getLogger(JsonHtmlFunctions.class);

  /** Class used for conversion between Json and MTS types. */
  private final JsonMTSTypeConversion typeConversion;

  /** SortMethod enumeration */
  private enum SortMethod {
    NONE,
    REVERSE,
    ASCENDING,
    DESCENDING,
  }

  /** Track the current depth in the json hierarchy during the conversion to html table(s) */
  private Integer jsonPathDepth;

  // region MTS function user options & defaults
  /** Return a specific order of object keys (if present) to start with */
  private JsonArray optionLeadObjectKeys;

  /** Return a specific order of object keys (if present) to end with */
  private JsonArray optionRearObjectKeys;

  /** Return object keys in a sorted order (for keys not defined as lead and rear object keys */
  private SortMethod optionSortObjectKeys;

  /** Sets any html attributes on the root table */
  private JsonObject optionAttributes;

  /** Adds a caption element to the root table */
  private String optionCaption;

  /** whether to output html tables with a detail/summary wrapper */
  private boolean optionCollapsible;

  /** whether a detail/summary wrapper is open by default */
  private Integer optionCollapsibleOpenDepth;

  /** whether to output the json path as html title attributes */
  private boolean optionTitles;

  /** whether to pivot json objects within a json array */
  private boolean optionArrayOfObjects;

  /** whether to pivot json objects within a json object */
  private boolean optionObjectOfObjects;

  /**
   * whether json values starting with "assetid://" should returned as an html {@code <img>} element
   */
  private boolean optionAssetImage;

  /**
   * Whether json values containing certain html characters in should be escaped.
   *
   * <p>See {@link JsonHtmlFunctions#escapeHtmlEntities(String)} for list html characters
   */
  private boolean optionEscapeHtml;

  /**
   * Whether to sanitize the html returned from jsonToHtmlTable.
   *
   * <p>See {@link JsonHtmlFunctions#sanitizeHtml(String)} for the safelist.
   */
  private boolean optionSanitizeHtml;

  /** whether json values should be converted to input elements for use in a form */
  private boolean optionInput;

  // endregion

  /**
   * Entry method to convert whatever json into nested html tables. First it determines any
   * conversion settings from either the options provided or from defaults and second it then calls
   * the main recursive method {@link #tabularizeJsonElement(JsonElement, String)}.
   *
   * @param jsonElement the json to convert
   * @param options any conversion or html options
   * @return an html table or table of tables
   */
  public String jsonToHtmlTable(JsonElement jsonElement, JsonObject options) {

    // set variables from provided options in a JsonObject
    processJsonToHtmlTableOptions(options);

    // region create the root html table and process the json
    jsonPathDepth = 0;
    StringBuilder html = new StringBuilder();

    // add the root table
    html.append("<table");

    // if provided add any table attributes
    for (String key : optionAttributes.keySet()) {
      html.append(" ")
          .append(escapeHtmlEntities(key))
          .append("=")
          .append(optionAttributes.getAsJsonPrimitive(key));
    }
    html.append(">");

    // if provided add a table caption
    if (!optionCaption.isEmpty()) {
      html.append("<caption>").append(optionCaption).append("</caption>");
    }

    // add the converted json
    html.append("<tr><td>");
    html.append(tabularizeJsonElement(jsonElement, ""));
    html.append("</td></tr>");
    html.append("</table>");

    if (optionSanitizeHtml) {
      return sanitizeHtml(html.toString());
    } else {
      return html.toString();
    }
  }

  /**
   * Set the options as provided to the MTScript function, or set to default values
   *
   * @param options JsonObject containing the option keys (lowercase) and values
   */
  private void processJsonToHtmlTableOptions(JsonObject options) {

    // region non-boolean options
    optionAttributes =
        options.has("attributes") ? options.getAsJsonObject("attributes") : new JsonObject();
    optionCaption = options.has("caption") ? options.get("caption").getAsString() : "";
    optionCollapsibleOpenDepth =
        options.has("collapsibleopendepth")
            ? Integer.parseInt(options.get("collapsibleopendepth").getAsString())
            : -1;
    optionLeadObjectKeys =
        options.has("leadobjectkeys") ? options.getAsJsonArray("leadobjectkeys") : new JsonArray();
    optionRearObjectKeys =
        options.has("rearobjectkeys") ? options.getAsJsonArray("rearobjectkeys") : new JsonArray();
    optionSortObjectKeys = SortMethod.NONE;
    if (options.has("sortobjectkeys")) {
      String optionSortObjects = options.get("sortobjectkeys").getAsString().toLowerCase();
      if (optionSortObjects.startsWith("a")) {
        optionSortObjectKeys = SortMethod.ASCENDING;
      } else if (optionSortObjects.startsWith("d")) {
        optionSortObjectKeys = SortMethod.DESCENDING;
      } else if (optionSortObjects.startsWith("r")) {
        optionSortObjectKeys = SortMethod.REVERSE;
      } else if (optionSortObjects.startsWith("n")) {
        optionSortObjectKeys = SortMethod.NONE;
      }
    }
    // endregion

    // region boolean options which default to true
    optionTitles =
        !options.has("titles") || Boolean.parseBoolean(options.get("titles").getAsString());
    optionCollapsible =
        !options.has("collapsible")
            || Boolean.parseBoolean(options.get("collapsible").getAsString());
    optionArrayOfObjects =
        !options.has("arrayofobjects")
            || Boolean.parseBoolean(options.get("arrayofobjects").getAsString());
    optionObjectOfObjects =
        !options.has("objectofobjects")
            || Boolean.parseBoolean(options.get("objectofobjects").getAsString());
    optionAssetImage =
        !options.has("assetimage") || Boolean.parseBoolean(options.get("assetimage").getAsString());
    optionEscapeHtml =
        !options.has("escapehtml") || Boolean.parseBoolean(options.get("escapehtml").getAsString());
    optionSanitizeHtml =
        !options.has("sanitizehtml")
            || Boolean.parseBoolean(options.get("sanitizehtml").getAsString());
    // endregion

    // region boolean options which default to false
    optionInput = options.has("input") && Boolean.parseBoolean(options.get("input").getAsString());
    // endregion

  }

  /**
   * Convert json into nested html tables, by recursively looping through the json elements and
   * building the html string for the objects, array, and values. It assesses what type of {@link
   * JsonElement} is at the current json path location, then will either output the value stored at
   * the specific json path, or if another json element recursively call another method to convert
   * the json type found into a table with constituent rows and cells.
   *
   * <ul>
   *   <li>{@link #tabularizeJsonArray(JsonArray, String)}
   *   <li>{@link #tabularizeJsonArrayOfObjects(JsonArray, String)}
   *   <li>{@link #tabularizeJsonObject(JsonObject, String)}
   *   <li>{@link #tabularizeJsonObjectOfObjects(JsonObject, String)}
   *
   * @param jsonElement the json
   * @param jsonPath the current json path which is being processed
   * @return the html
   */
  private String tabularizeJsonElement(JsonElement jsonElement, String jsonPath) {

    StringBuilder html = new StringBuilder();

    switch (jsonElement) {
      case JsonArray ja -> {
        // pivot if we have to
        if (optionArrayOfObjects && isArrayOfObjects(ja)) {
          html.append(tabularizeJsonArrayOfObjects(ja, jsonPath));
        } else {
          html.append(tabularizeJsonArray(ja, jsonPath));
        }
      }
      case JsonObject jo -> {
        // pivot if we have to
        if (optionObjectOfObjects && isObjectOfObjects(jo)) {
          html.append(tabularizeJsonObjectOfObjects(jo, jsonPath));
        } else {
          html.append(tabularizeJsonObject(jo, jsonPath));
        }
      }
      case JsonNull jn -> {
        // provide some indication of the JsonNull value
        html.append("<span")
            .append(htmlAttr("class", "json-null"))
            .append(">")
            .append(jn.toString())
            .append("</span>");
      }
      case null -> {
        // should not get here, but just in case...
        // provide some indication of the JsonElement being Null
        html.append("<span").append(htmlAttr("class", "json-element-null")).append("></span>");
      }
      default -> {
        String jsonValue = typeConversion.jsonToScriptString(jsonElement);
        if (optionInput) {
          html.append("<input")
              .append(htmlAttr("type", "text"))
              .append(htmlAttr("value", jsonValue))
              .append(htmlAttr("data-json-path", jsonPath))
              .append(">");
        } else {
          if (optionEscapeHtml) {
            html.append(escapeHtmlEntities(jsonValue));
          } else if (optionAssetImage && jsonValue.trim().toLowerCase().matches("^asset://.*")) {
            html.append("<img")
                .append(htmlAttr("class", "asset"))
                .append(htmlAttr("src", jsonValue))
                .append(">");
          } else {
            html.append(jsonValue);
          }
        }
      }
    }

    return html.toString();
  }

  /**
   * Convert a json array into an html table.
   *
   * @param jsonArray the json array
   * @param jsonPath the current json path which is being processed
   * @return html
   */
  private String tabularizeJsonArray(JsonArray jsonArray, String jsonPath) {

    jsonPathDepth++;
    StringBuilder html = new StringBuilder();

    if (optionCollapsible) {
      html.append(
          generateHtmlSummaryDetails(
              jsonArray.isEmpty(), "json-array", String.format("Array [%s]", jsonArray.size())));
    }

    // add a table to hold the array data
    html.append("<table").append(htmlAttr("class", "json-array")).append(">");

    // loop through each item in the array
    for (int i = 0; i < jsonArray.size(); i++) {
      String currentJsonPath = String.format("%s[%s]", jsonPath, i);

      // add a row for each item
      html.append("<tr>");
      // add a row header

      html.append("<th")
          .append(htmlAttrStandard("json-array-header", currentJsonPath, null, i))
          .append(">")
          .append(i)
          .append("</th>");

      // add the contents
      html.append("<td")
          .append(htmlAttrStandard("json-array", currentJsonPath, null, i))
          .append(">")
          .append(tabularizeJsonElement(jsonArray.get(i), currentJsonPath))
          .append("</td>");
      html.append("</tr>");
    }
    html.append("</table>");
    if (optionCollapsible) {
      html.append("</details>");
    }
    jsonPathDepth--;
    return html.toString();
  }

  /**
   * Pivots a json array of json objects into an html table with the array as rows and child objects
   * as columns.
   *
   * @param jsonArray the json array of json objects.
   * @param jsonPath the json path to this json array
   * @return an html table
   */
  private String tabularizeJsonArrayOfObjects(JsonArray jsonArray, String jsonPath) {

    jsonPathDepth++;
    StringBuilder html = new StringBuilder();

    // compile a set of unique keys across all objects in the array - these will be the table column
    // headers.  If the objects within the array have a vastly different structure, then displaying
    // a pivoted array of objects may actually not be not appropriate.
    HashSet<String> jsonKeys = new HashSet<>();
    int joKeysMin = 0;
    int joKeysMax = 0;
    for (int i = 0; i < jsonArray.size(); i++) {
      if (jsonArray.get(i) instanceof JsonObject jo) {
        jsonKeys.addAll(jo.keySet());
        if (i == 0) {
          joKeysMin = jo.size();
          joKeysMax = jo.size();
        } else {
          joKeysMin = Math.min(joKeysMin, jo.size());
          joKeysMax = Math.max(joKeysMax, jo.size());
        }
      }
    }
    LinkedHashSet<String> orderedObjectKeys = orderKeys(jsonKeys);
    if (optionCollapsible) {
      String summaryText =
          (joKeysMin == joKeysMax && joKeysMin == jsonKeys.size())
              ? String.format("Array [%s] of Objects {%s}", jsonArray.size(), jsonKeys.size())
              : String.format(
                  "Array [%s] of Objects {%s (%s-%s)}",
                  jsonArray.size(), jsonKeys.size(), joKeysMin, joKeysMax);
      html.append(
          generateHtmlSummaryDetails(jsonArray.isEmpty(), "json-array-of-objects", summaryText));
    }

    // add the table
    html.append("<table").append(htmlAttr("class", "json-array-of-objects")).append(">");

    // add the table header row for the array index and for each unique object key

    html.append("<tr>");

    // array index column header
    String jsonPathArrayHeader = String.format("%s[%s]", jsonPath, "*");
    html.append("<th")
        .append(htmlAttrStandard("json-array-header", jsonPathArrayHeader, null, null))
        .append(">")
        .append("#")
        .append("</th>");

    // object key column headers
    for (String key : orderedObjectKeys) {
      String jsonPathObjectHeader = String.format("%s['%s']", jsonPathArrayHeader, key);
      html.append("<th")
          .append(htmlAttrStandard("json-object-header", jsonPathObjectHeader, key, null))
          .append(">")
          .append(optionEscapeHtml ? escapeHtmlEntities(key) : key)
          .append("</th>");
    }
    html.append("</tr>");
    jsonPathDepth++;

    // add a row for every item in the array, adding a column for the array index and for the
    // content of each object key
    for (int i = 0; i < jsonArray.size(); i++) {

      html.append("<tr>");
      // array index row header
      String jsonPathArray = String.format("%s[%s]", jsonPath, i);
      html.append("<th")
          .append(htmlAttrStandard("json-array-header", jsonPathArray, null, "i"))
          .append(">")
          .append(i)
          .append("</th>");

      // get the json object for this array item
      JsonObject jo = (JsonObject) jsonArray.get(i);

      // loop through each key in the list of object keys
      for (String key : orderedObjectKeys) {
        String jsonPathObject = String.format("%s['%s']", jsonPathArray, key);
        if (jo.get(key) != null) {
          html.append("<td")
              .append(htmlAttrStandard("json-object", jsonPathObject, key, null))
              .append(">")
              .append(tabularizeJsonElement(jo.get(key), jsonPathObject))
              .append("</td>");
        } else {

          // some objects may not have a key in the compiled list of keys from all objects
          html.append("<td")
              .append(htmlAttrStandard("json-path-leaf-missing", jsonPathObject, key, null))
              .append(">")
              .append("</td>");
        }
      }
      html.append("</tr>");
    }
    jsonPathDepth--;
    html.append("</table>");
    if (optionCollapsible) {
      html.append("</details>");
    }
    jsonPathDepth--;
    return html.toString();
  }

  /**
   * Convert a json object into an html table.
   *
   * @param jsonObject the json object
   * @param jsonPath the current json path which is being processed
   * @return html
   */
  private String tabularizeJsonObject(JsonObject jsonObject, String jsonPath) {

    jsonPathDepth++;
    StringBuilder html = new StringBuilder();

    if (optionCollapsible) {
      html.append(
          generateHtmlSummaryDetails(
              jsonObject.isEmpty(),
              "json-object",
              String.format("Object {%s}", jsonObject.size())));
    }

    // add a table to hold the object data
    html.append("<table").append(htmlAttr("class", "json-object")).append(">");
    LinkedHashSet<String> orderedObjectKeys = orderKeys(jsonObject.keySet());

    // loop through each key in the list of object keys
    for (String key : orderedObjectKeys) {
      String currentJsonPath = String.format("%s['%s']", jsonPath, key);

      // add a row for each object key
      html.append("<tr>");

      // add a row header
      html.append("<th")
          .append(htmlAttrStandard("json-object-header", currentJsonPath, key, null))
          .append(">")
          .append(optionEscapeHtml ? escapeHtmlEntities(key) : key)
          .append("</th>");

      // add the contents
      html.append("<td")
          .append(htmlAttrStandard("json-object", currentJsonPath, key, null))
          .append(">")
          .append(tabularizeJsonElement(jsonObject.get(key), currentJsonPath))
          .append("</td>");
      html.append("</tr>");
    }
    html.append("</table>");
    if (optionCollapsible) {
      html.append("</details>");
    }
    jsonPathDepth--;
    return html.toString();
  }

  /**
   * Pivots a json object of json objects into an html table with the object as rows and child
   * objects as columns.
   *
   * @param jsonObject the json object of json objects.
   * @param jsonPath the json path to this json array
   * @return an html table
   */
  private String tabularizeJsonObjectOfObjects(JsonObject jsonObject, String jsonPath) {
    jsonPathDepth++;
    StringBuilder html = new StringBuilder();

    // compile a set of unique keys across all objects in the array - these will be the table column
    // headers.  If the objects within the array have a vastly different structure, then maybe
    // pivoting into an array of objects is not appropriate.
    HashSet<String> jsonKeys = new HashSet<>();
    for (String key : jsonObject.keySet()) {
      if (jsonObject.get(key) instanceof JsonObject jo) {
        jsonKeys.addAll(jo.keySet());
      }
    }
    LinkedHashSet<String> orderedObjectKeys = orderKeys(jsonKeys);
    if (optionCollapsible) {
      html.append(
          generateHtmlSummaryDetails(
              jsonObject.isEmpty(),
              "json-object-of-objects",
              String.format("Object {%s} of Objects {%s}", jsonObject.size(), jsonKeys.size())));
    }

    // add the table
    html.append("<table").append(htmlAttr("class", "json-object-of-objects")).append(">");

    // add the table header row for the parent object and for each unique child object key
    html.append("<tr>");

    // parent object column header
    String jsonPathParentObjectHeader = String.format("%s[%s]", jsonPath, "*");
    html.append("<th")
        .append(htmlAttrStandard("json-object-header", jsonPathParentObjectHeader, "*", null))
        .append(">")
        .append("*")
        .append("</th>");

    // child object key column headers
    for (String key : orderedObjectKeys) {
      String jsonPathChildObjectHeader = String.format("%s['%s']", jsonPathParentObjectHeader, key);
      html.append("<th")
          .append(htmlAttrStandard("json-object-header", jsonPathChildObjectHeader, key, null))
          .append(">")
          .append(optionEscapeHtml ? escapeHtmlEntities(key) : key)
          .append("</th>");
    }
    html.append("</tr>");
    jsonPathDepth++;

    // add a row for every key in the parent object
    // add a column for the parent key and for the content of each object key
    for (String key : jsonObject.keySet()) {
      html.append("<tr>");

      // object key row header
      String jsonPathParentObject = String.format("%s['%s']", jsonPath, key);
      html.append("<th")
          .append(htmlAttrStandard("json-object-header", jsonPathParentObject, key, null))
          .append(">")
          .append(optionEscapeHtml ? escapeHtmlEntities(key) : key)
          .append("</th>");

      // get the child json object for this object key
      JsonObject jo = (JsonObject) jsonObject.get(key);

      // loop through each key in the list of object keys
      for (String childKey : orderedObjectKeys) {
        String jsonPathChildObject = String.format("%s['%s']", jsonPathParentObject, childKey);
        if (jo.get(childKey) != null) {
          html.append("<td")
              .append(htmlAttrStandard("json-object", jsonPathChildObject, childKey, null))
              .append(">")
              .append(tabularizeJsonElement(jo.get(childKey), jsonPathChildObject))
              .append("</td>");
        } else {

          // some objects may not have a key in the compiled list of keys from all objects
          html.append("<td")
              .append(
                  htmlAttrStandard("json-path-leaf-missing", jsonPathChildObject, childKey, null))
              .append(">")
              .append("</td>");
        }
      }
      html.append("</tr>");
    }
    jsonPathDepth--;
    html.append("</table>");
    if (optionCollapsible) {
      html.append("</details>");
    }
    jsonPathDepth--;
    return html.toString();
  }

  /**
   * Standardize html attribute additions for given data type as they require a leading space
   *
   * @param attribute the html attribute
   * @param value the html attribute's value
   * @return a formatted string representing the html attribute and value
   */
  private String htmlAttr(String attribute, Integer value) {
    return String.format(" %s=%s", attribute, value);
  }

  private String htmlAttr(String attribute, String value) {
    return String.format(" %s=\"%s\"", attribute, value);
  }

  private String htmlAttr(String attribute, Object value) {
    return String.format(" %s=\"%s\"", attribute, value.toString());
  }

  /**
   * Standardizes html element opening tag attributes for jsonToHtml
   *
   * @param classAttr the class name
   * @param jsonPath the json path
   * @param jsonKey the json key
   * @param jsonIndex the json index
   * @return a formatted string representing the html attributes and values
   */
  private String htmlAttrStandard(
      String classAttr, String jsonPath, String jsonKey, Object jsonIndex) {
    return (classAttr != null ? htmlAttr("class", classAttr) : "")
        + (optionTitles && jsonPath != null ? htmlAttr("title", jsonPath) : "")
        + (jsonPath != null ? htmlAttr("data-json-path", jsonPath) : "")
        + (jsonKey != null ? htmlAttr("data-json-key", jsonKey) : "")
        + (jsonIndex != null ? htmlAttr("data-json-index", jsonIndex) : "");
  }

  /**
   * Standardizes the creation of html collapsibles using {@code <details>} and {@code <summary>}
   * html elements (N.B. supported in HTML5).
   *
   * @param isEmpty whether there is any detail
   * @param classAttr the class name attribute
   * @param summaryText the summary text
   * @return the opening details element and entire summary element
   */
  private String generateHtmlSummaryDetails(boolean isEmpty, String classAttr, String summaryText) {

    StringBuilder html = new StringBuilder();

    html.append("<details").append(htmlAttr("data-json-path-depth", jsonPathDepth));
    if (!isEmpty && jsonPathDepth <= optionCollapsibleOpenDepth
        || optionCollapsibleOpenDepth == -1) {
      html.append(" open");
    }
    html.append(">");
    html.append("<summary")
        .append(htmlAttr("class", classAttr))
        .append(">")
        .append(summaryText)
        .append("</summary>");

    return html.toString();
  }

  /**
   * Replace specific characters (e.g. <code>&<>\'</code>) with html entity escaped versions.
   *
   * @param string the string which may contain characters to be escaped
   * @return the html with specific characters escaped
   */
  private String escapeHtmlEntities(String string) {
    return string
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&apos;");
  }

  /**
   * Remove any html tags and attributes not defined on the safelist below.
   *
   * @param html the html to sanitize
   * @return the sanitized html
   */
  private String sanitizeHtml(String html) {
    Safelist safelist =
        Safelist.basic()
            .addTags("table", "caption", "th", "tr", "td", "input", "details", "summary", "img")
            .addAttributes(
                ":all",
                "id",
                "class",
                "title",
                "data-json-path",
                "data-json-key",
                "data-json-index",
                "data-json-path-depth")
            .addAttributes("details", "open")
            .addAttributes("img", "src")
            .addAttributes("input", "type", "value")
            .addProtocols("img", "src", "asset");
    return Jsoup.clean(html, safelist);
  }

  /**
   * Check if a json array only contains json objects.
   *
   * @param jsonArray the json array to check
   * @return true if the json array just contains json objects, otherwise false
   */
  private boolean isArrayOfObjects(JsonArray jsonArray) {
    boolean check = true;
    if (jsonArray.isEmpty()) {
      check = false;
    } else {
      for (int i = 0; i < jsonArray.size(); i++) {
        if (!jsonArray.get(i).isJsonObject()) {
          check = false;
          break;
        }
      }
    }
    return check;
  }

  /**
   * Check if a json object only contains other json objects.
   *
   * @param jsonObject the json object to check
   * @return true if the json object just contains other json object, otherwise false
   */
  private boolean isObjectOfObjects(JsonObject jsonObject) {
    boolean check = true;
    if (jsonObject.isEmpty()) {
      check = false;
    } else {
      for (String key : jsonObject.keySet()) {
        if (!jsonObject.get(key).isJsonObject()) {
          check = false;
          break;
        }
      }
    }
    return check;
  }

  /**
   * Order a set of json object keys, using optional lead/rear object keys to position them at the
   * start/end, and an optional sort method for all keys in between .
   *
   * @param jsonKeys the keys
   * @return a ordered list of keys
   */
  private LinkedHashSet<String> orderKeys(Set<String> jsonKeys) {

    LinkedHashSet<String> orderedObjectKeys = new LinkedHashSet<>();

    // add lead object keys to the set
    if (!optionLeadObjectKeys.isEmpty()) {
      for (int i = 0; i < optionLeadObjectKeys.size(); i++) {
        String leadObjectKey = optionLeadObjectKeys.get(i).getAsString();
        if (jsonKeys.contains(leadObjectKey)) {
          orderedObjectKeys.add(leadObjectKey);
        }
      }
    }

    // sort all object keys by the sort method
    List<String> sortedObjectKeys;
    if (optionSortObjectKeys.equals(SortMethod.ASCENDING)) {
      sortedObjectKeys = jsonKeys.stream().sorted().toList();
    } else if (optionSortObjectKeys.equals(SortMethod.DESCENDING)) {
      sortedObjectKeys = jsonKeys.stream().sorted().toList().reversed();
    } else if (optionSortObjectKeys.equals(SortMethod.REVERSE)) {
      sortedObjectKeys = jsonKeys.stream().toList().reversed();
    } else {
      sortedObjectKeys = jsonKeys.stream().toList();
    }

    // add sorted object keys to the set
    orderedObjectKeys.addAll(sortedObjectKeys);

    // add rear object keys to the set
    if (!optionRearObjectKeys.isEmpty()) {
      for (int i = 0; i < optionRearObjectKeys.size(); i++) {
        String rearObjectKey = optionRearObjectKeys.get(i).getAsString();
        if (jsonKeys.contains(rearObjectKey)) {
          orderedObjectKeys.remove(rearObjectKey);
          orderedObjectKeys.add(rearObjectKey);
        }
      }
    }
    return orderedObjectKeys;
  }
}
