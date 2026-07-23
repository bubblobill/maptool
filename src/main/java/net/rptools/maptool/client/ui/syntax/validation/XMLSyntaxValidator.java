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

import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

public class XMLSyntaxValidator implements RSyntaxValidator {
  @Override
  public ValidationResult validate(String text) {
    if (text == null || text.isBlank()) {
      return ValidationResult.valid("XML Empty");
    }

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      // secure processing
      factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

      // disable doctype (i.e. file reads)
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

      // prevent parser accessing external resources
      factory.setFeature("http://xml.org/sax/features/external-general-entities", false);

      // prevent parser from loading and expanding external DTD fragments
      factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

      factory.setExpandEntityReferences(false);
      factory.setXIncludeAware(false);
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.parse(new InputSource(new StringReader(text)));

      return ValidationResult.valid("XML Valid");

    } catch (SAXParseException e) {
      return ValidationResult.error(
          "XML Error at line "
              + e.getLineNumber()
              + ", column "
              + e.getColumnNumber()
              + ": "
              + e.getMessage());

    } catch (Exception e) {
      return ValidationResult.error("XML Error: " + e.getMessage());
    }
  }
}
