package net.rptools.maptool.client.ui.syntax.validation;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONSyntaxValidator implements RSyntaxValidator {
    @Override
    public ValidationResult validate(String text) {
        try {
            JsonParser.parseString(text);
            return ValidationResult.valid("JSON Valid");
        } catch (JsonSyntaxException jse) {
            return ValidationResult.error("JSON Error: " + validateJsonErrorFormatter(jse));
        }
    }

    private static String validateJsonErrorFormatter(JsonSyntaxException jse) {

        // Regex pattern to extract line, column, and internal path coordinates
        Pattern LINE_COL_PATTERN = Pattern.compile("at line (\\d+) column (\\d+)(?: path (\\S+))?");

        String rawMessage = jse.getMessage();
        if (rawMessage == null || rawMessage.isEmpty()) {
            return "Invalid JSON syntax detected.";
        }

        // extract position tracking numbers if present
        String positionDetails = "";
        Matcher matcher = LINE_COL_PATTERN.matcher(rawMessage);
        if (matcher.find()) {
            String line = matcher.group(1);
            String column = matcher.group(2);
            positionDetails = " at line " + line + ", column " + column;
        }

        // clean up common structural errors
        String lowercaseMsg = rawMessage.toLowerCase();

        if (lowercaseMsg.contains("unterminated object")) {
            return "Missing a closing curly bracket '}'" + positionDetails + ".";
        }
        if (lowercaseMsg.contains("unterminated array")) {
            return "Missing a closing square bracket ']'" + positionDetails + ".";
        }
        if (lowercaseMsg.contains("expected name")) {
            return "Missing a property key name or missing a comma separator" + positionDetails + ".";
        }
        if (lowercaseMsg.contains("expected value") || lowercaseMsg.contains("unexpected char")) {
            return "Syntax error or unexpected characters found" + positionDetails + ".";
        }
        if (lowercaseMsg.contains("malformed json")) {
            return "The text contains unquoted p8structural strings or bad character encoding"
                    + positionDetails
                    + ".";
        }

        // fallback translation if regex metadata fails
        if (!positionDetails.isEmpty()) {
            return "Syntax error discovered" + positionDetails + ".";
        }

        // fallback to raw message if completely unrecognizable
        return rawMessage;
    }
}
