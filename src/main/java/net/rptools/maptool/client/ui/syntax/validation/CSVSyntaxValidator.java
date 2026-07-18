package net.rptools.maptool.client.ui.syntax.validation;

import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;

import java.util.List;

public class CSVSyntaxValidator implements RSyntaxValidator {
    @Override
    public ValidationResult validate(String text) {
        return null;

//        try (CSVParser parser =
//                     CSVParser.parse(
//                             text,
//                             CSVFormat.DEFAULT
//                                     .builder()
//                                     .setHeader()
//                                     .setAllowMissingColumnNames(false)
//                                     .setSkipHeaderRecord(false)
//                                     .setIgnoreEmptyLines(true)
//                                     .build())) {
//
//            int expectedColumns = -1;
//            long rowCount = 0;
//
//            for (CSVRecord record : parser) {
//                rowCount = record.getRecordNumber();
//
//                if (expectedColumns == -1) {
//                    expectedColumns = record.size();
//                } else if (record.size() != expectedColumns) {
//                    return ValidationResult.error(
//                            "Row "
//                                    + record.getRecordNumber()
//                                    + " has "
//                                    + record.size()
//                                    + " columns, expected "
//                                    + expectedColumns);
//                }
//            }
//
//            if (expectedColumns < 0) {
//                return ValidationResult.valid("CSV Empty");
//            } else {
//                return ValidationResult.valid(
//                        "CSV Valid: " + rowCount + " data rows, " + expectedColumns + " columns");
//            }
//        } catch (Exception e) {
//            return ValidationResult.error("CSV Error: " + e.getMessage());
//        }
    }
}
