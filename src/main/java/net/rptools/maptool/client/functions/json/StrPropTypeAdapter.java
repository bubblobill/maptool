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

import com.google.gson.TypeAdapter;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.rptools.maptool.model.StringPropList;

public class StrPropTypeAdapter extends TypeAdapter<String> {
  StringPropList spl;

  @Override
  public void write(JsonWriter out, String value) throws IOException {
    spl = new StringPropList(value);
    if (spl.getStringPropertiesMap().isEmpty()) {
      out.nullValue();
      return;
    }

    out.beginObject();
    for (Map.Entry<String, String> entry : spl.getStringPropertiesMap().entrySet()) {
      out.name(entry.getKey());
      out.value(entry.getValue());
    }
    out.endObject();
  }

  @Override
  public String read(JsonReader in) throws IOException {
    JsonToken peek = in.peek();
    if (peek == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    Map<String, String> map = new HashMap<>();

    if (peek == JsonToken.BEGIN_OBJECT) {
      in.beginObject();
      while (in.hasNext()) {
        JsonReaderInternalAccess.INSTANCE.promoteNameToValue(in);
        map.put(in.nextName(), in.nextString());
      }
      in.endObject();
    }
    return new StringPropList(map).toString();
  }
}
