package net.rptools.maptool.client.ui.syntax.validation;

import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;

import java.util.List;

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
