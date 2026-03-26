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
package net.rptools.maptool.client.functions.exceptions;

import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.rptools.maptool.language.I18N;
import net.rptools.parser.ParserException;
import org.apache.commons.lang3.tuple.Pair;

public class ParserExceptionBuilder {
  private Throwable throwable = null;
  private List<Pair<String, Object>> messageParams;
  private static ParserExceptionBuilder instance;
  private String msgKey = null;

  private ParserExceptionBuilder() {}

  private void checkInitialised() {
    if (instance == null) {
      start();
    }
  }

  public ParserException build() {
    if (throwable != null) {
      return new ParserException(throwable);
    } else if (msgKey != null) {
      return new ParserException(I18N.getMessage(msgKey, messageParams));
    } else {
      return new ParserException(I18N.getMessage("macro.function.general.unknownError"));
    }
  }

  public static ParserExceptionBuilder start() {
    return start(null);
  }

  public static ParserExceptionBuilder start(String i18nKey) {
    instance = new ParserExceptionBuilder();
    instance.messageParams = new ArrayList<>();
    instance.throwable = null;
    instance.msgKey = i18nKey;
    return instance;
  }

  public ParserExceptionBuilder forThrowable(final Throwable cause) {
    checkInitialised();
    throwable = cause;
    return this;
  }

  public ParserExceptionBuilder i18nKey(final String i18nKey) {
    checkInitialised();
    instance.msgKey = i18nKey;
    return this;
  }

  public ParserExceptionBuilder functionName(final String functionName) {
    checkInitialised();
    return namedValue("functionName", functionName);
  }

  public ParserExceptionBuilder parameterIndex(final int parameterIndex) {
    checkInitialised();
    return namedValue("parameterIndex", parameterIndex);
  }

  public ParserExceptionBuilder options(String options) {
    checkInitialised();
    return namedValue("options", options);
  }

  public ParserExceptionBuilder results(String results) {
    checkInitialised();
    return namedValue("results", results);
  }

  public ParserExceptionBuilder parameterValue(Object parameterValue) {
    return namedValue("parameterValue", parameterValue);
  }

  public ParserExceptionBuilder namedValue(final String name, Object value) {
    checkInitialised();
    if (value instanceof JsonElement je) {
      value = je.toString();
    } else if (value instanceof List<?> list) {
      value = Arrays.deepToString(list.toArray());
    } else if (value instanceof Object[] array) {
      value = Arrays.deepToString(array);
    }
    messageParams.add(Pair.of(name, value));
    return this;
  }
}
