package net.rptools.maptool.client.ui.syntax.validation;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
