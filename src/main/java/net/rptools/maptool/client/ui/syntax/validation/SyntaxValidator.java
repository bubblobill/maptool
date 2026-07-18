package net.rptools.maptool.client.ui.syntax.validation;

import net.rptools.maptool.client.swing.SyntaxTextArea;
import net.rptools.maptool.client.ui.syntax.Syntax;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;

public class SyntaxValidator {
    private RSyntaxTextArea editor = null;
    private JLabel statusLabel = null;
    private Timer validationTimer = null;

    private Runnable validationTask;
    private RSyntaxValidator validator = null;

    /**
     * Use {@link  #createValidator}
     */
    private SyntaxValidator() {
    }

    public static SyntaxValidator createValidator(RSyntaxTextArea rSyntaxTextArea) {
        SyntaxValidator sv = new SyntaxValidator();
        sv.setEditor(rSyntaxTextArea);
        sv.setSyntax(Syntax.lookup(rSyntaxTextArea.getSyntaxEditingStyle()));
        return sv;
    }

    public static SyntaxValidator createValidator(SyntaxTextArea syntaxTextArea) {
        SyntaxValidator sv = new SyntaxValidator();
        sv.setEditor(syntaxTextArea.getEditor());
        sv.setStatusLabel(syntaxTextArea.getStatusLabel());
        sv.setSyntax(syntaxTextArea.getSyntax());
        return sv;
    }

    public void setEditor(RSyntaxTextArea editor) {
        this.editor = editor;
    }

    public void setSyntax(Syntax syntax) {
        editor.setSyntaxEditingStyle(syntax.rSyntaxStyle());
        if(syntax.supportsValidation()) {
            this.validator = switch (syntax) {
                case CSV -> new CSVSyntaxValidator();
                case HTML -> new HTMLSyntaxValidator();
                case JAVASCRIPT -> new JavascriptSyntaxValidator();
                case JSON -> new JSONSyntaxValidator();
                case XML -> new XMLSyntaxValidator();
                default -> null;
            };
            if(this.validator != null) {
                createValidationTimer();
            }
        }
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setValidationTask(Runnable task) {
        validationTask = task;
    }

    private void createValidationTimer() {
        validationTimer =
                new Timer(
                        500,
                        e -> {
                            if (validationTask != null) {
                                validationTask.run();
                            }
                        });
        validationTimer.setRepeats(false);
    }

    public void restartValidationTimer() {
        if(validationTimer != null) {
            validationTimer.restart();
        }
    }

    public void updateLabel(ValidationResult result) {
        if(statusLabel != null) {
            if (result == null) {
                statusLabel.setText("");
                return;
            }
            statusLabel.setText(result.message());
            statusLabel.setForeground(result.status().color());
        }
    }

    /** Use for programmatic updates */
    public void validateNow() {
        if (validationTask != null) {
            validationTask.run();
        } else {
            validate();
        }
    }

    public ValidationResult validate() {
        ValidationResult vr = null;
        if(this.validator != null) {
            vr = validator.validate(editor.getText());
            updateLabel(vr);
        }
        return vr;
    }
}