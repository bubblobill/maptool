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
package net.rptools.maptool.model;

import com.google.gson.annotations.JsonAdapter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import net.rptools.maptool.client.functions.json.StrPropTypeAdapter;

@JsonAdapter(StrPropTypeAdapter.class)
public class StringPropList {
  private String delimiter = ";";
  private final Map<String, String> stringPropertiesMap = new HashMap<>();
  private static final BiFunction<String, String, String[]> splitEntries = String::split;

  private void parse(String... strings) {
    if (strings.length == 1 && !strings[0].contains("=")) {
      delimiter = strings[0];
      return;
    } else if (strings.length == 1 && strings[0].contains("=")) {
      strings = splitEntries.apply(strings[0], delimiter);
    } else if (strings.length == 2 && !strings[1].contains("=")) {
      delimiter = strings[0];
      strings = splitEntries.apply(strings[1], delimiter);
    } else if (strings.length % 2 == 1) {
      delimiter = strings[0];
      strings = Arrays.copyOfRange(strings, 1, strings.length - 1);
    }
    for (int i = 0; i < strings.length; i++) {
      String string = strings[i];
      if (string.contains("=")) {
        String[] pair = string.split("=");
        if (pair.length > 1) {
          stringPropertiesMap.put(pair[0], pair[1]);
        } else {
          stringPropertiesMap.put(pair[0], "");
        }
      } else {
        if (i < strings.length - 1) {
          stringPropertiesMap.put(strings[i], strings[i + 1]);
        } else {
          stringPropertiesMap.put(strings[i], "");
        }
      }
    }
  }

  public Map<String, String> getStringPropertiesMap() {
    return stringPropertiesMap;
  }

  public StringPropList() {}

  public StringPropList(String string) {
    parse(string);
  }

  public StringPropList(String... strings) {
    parse(strings);
  }

  public StringPropList(Map<String, String> stringProperties) {
    this.stringPropertiesMap.putAll(stringProperties);
  }

  @Override
  public String toString() {
    return String.join(
        delimiter,
        stringPropertiesMap.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .toList());
  }
}
