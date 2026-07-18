package net.rptools.maptool.client.ui.syntax.validation;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;

import java.util.List;

public class JavascriptSyntaxValidator implements RSyntaxValidator {
    @Override
    public ValidationResult validate(String text) {
        try (Context context = Context.create()) {
            context.eval("js", text);
            return ValidationResult.valid("JavaScript Valid");
        } catch (PolyglotException e) {
            return ValidationResult.error("JavaScript Error: " + e.getMessage());
        }
    }
}
