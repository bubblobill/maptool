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
package net.rptools.maptool.language;

import com.google.gson.JsonElement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractMessageBuilder {
  protected final Map<String, Object> messageParams;
  protected String msgKey;

  protected AbstractMessageBuilder(final String i18nKey) {
    this.msgKey = i18nKey;
    messageParams = new HashMap<>();
  }

  /** Persuade likely value types to something meaningful */
  protected Function<Object, String> stringify =
      value -> {
        if (value instanceof JsonElement je) {
          return je.toString();
        } else if (value instanceof List<?> list) {
          return Arrays.deepToString(list.toArray());
        } else if (value instanceof Object[] array) {
          return Arrays.deepToString(array);
        }
        return String.valueOf(value);
      };

  public AbstractMessageBuilder namedValue(final String name, final Object value) {
    messageParams.put(name, stringify.apply(value));
    return this;
  }

  public String build() {
    return I18N.getMessage(msgKey, messageParams);
  }
}
