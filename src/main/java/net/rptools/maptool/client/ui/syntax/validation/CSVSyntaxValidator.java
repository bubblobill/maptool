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
    //                        "CSV Valid: " + rowCount + " data rows, " + expectedColumns + "
    // columns");
    //            }
    //        } catch (Exception e) {
    //            return ValidationResult.error("CSV Error: " + e.getMessage());
    //        }
  }
}
