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
package net.rptools.maptool.client.ui.syntax.validation;

import java.util.List;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;

public class HTMLSyntaxValidator implements RSyntaxValidator {
  @Override
  public ValidationResult validate(String text) {
    if (text == null || text.isBlank()) {
      return ValidationResult.valid("HTML Empty");
    }

    try {
      Parser parser = Parser.htmlParser();
      parser.setTrackErrors(100);
      parser.parseInput(text, "");
      List<ParseError> errors = parser.getErrors();
      if (errors.isEmpty()) {
        return ValidationResult.valid("HTML Valid");
      }
      ParseError first = errors.getFirst();
      return ValidationResult.warning(
          "HTML Parsed with " + errors.size() + " warning(s). First: " + first.getErrorMessage());
    } catch (Exception e) {
      return ValidationResult.error("HTML Error: " + e.getMessage());
    }
  }
}
