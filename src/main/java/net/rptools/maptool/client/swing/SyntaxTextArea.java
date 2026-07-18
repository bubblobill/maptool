package net.rptools.maptool.client.swing;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import net.rptools.maptool.client.AppConstants;
import net.rptools.maptool.client.AppPreferences;
import net.rptools.maptool.client.ui.syntax.MapToolScriptAutoComplete;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVRecord;
import net.rptools.maptool.client.ui.syntax.Syntax;
import net.rptools.maptool.client.ui.syntax.validation.SyntaxValidator;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rsyntaxtextarea.folding.CurlyFoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rtextarea.RTextScrollPane;

public class SyntaxTextArea extends JPanel {
    private final RSyntaxTextArea editor;
    private final JLabel statusLabel;
    private final SyntaxValidator syntaxValidator;
    private Syntax syntax;

    public SyntaxTextArea(int rows, int cols, String specSyntax) {
        this.editor = new RSyntaxTextArea(rows, cols);
        this.statusLabel = new JLabel();

        this.syntax = Syntax.lookup(specSyntax);
        this.syntaxValidator = SyntaxValidator.createValidator(this);

        if (syntax == Syntax.MAPTOOL_SCRIPT) {
            // load MapTool Script's custom syntax (before we configure the editor)
            AbstractTokenMakerFactory atmf =
                    (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
            atmf.putMapping(
                    Syntax.MAPTOOL_SCRIPT.rSyntaxStyle(),
                    "net.rptools.maptool.client.ui.syntax.MapToolScriptSyntax");
            FoldParserManager.get()
                    .addFoldParserMapping(Syntax.MAPTOOL_SCRIPT.rSyntaxStyle(), new CurlyFoldParser());
        }

        // configure the editor
        editor.setSyntaxEditingStyle(syntax.rSyntaxStyle());
        editor.setCodeFoldingEnabled(true);
        editor.setEditable(true);
        editor.setInsertPairedCharacters(false);
        editor.setLineWrap(true);
        editor.setTabSize(4);
        editor.setWrapStyleWord(true);
        editor.setUseFocusableTips(false);

        if (syntax == Syntax.MAPTOOL_SCRIPT) {
            // add autocomplete
            CompletionProvider provider = new MapToolScriptAutoComplete().get();
            AutoCompletion ac = new AutoCompletion(provider);
            ac.setAutoCompleteEnabled(true);
            ac.setAutoActivationEnabled(true);
            ac.setAutoActivationDelay(500);
            ac.setShowDescWindow(true);
            ac.setAutoCompleteSingleChoices(false);
            ac.install(editor);
        }

        // try and apply MapTool's macro editor theme
        loadTheme(editor);

        // customise the popup menu
        EditorPopupMenuBuilder.install(editor, syntax);

        // bung the editor into a scroll pane (so we get line numbers and scrolling)
        RTextScrollPane scrollPane = new RTextScrollPane(editor);

        // we line wrap so a horizontal scrollbar is not required, vertical always
        // n.b. AS_NEEDED policy options caused Swing layout jitter so is avoided
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // set caret position
        SwingUtilities.invokeLater(() -> editor.setCaretPosition(0));

        // plop everything in the panel
        setLayout(new MigLayout("fill", "[grow]", "[grow]"));
        add(scrollPane, "grow, push, wrap");
        add(statusLabel);

        // check if what has been provided is valid (and update the status label)
        syntaxValidator.validate();
    }

    public RSyntaxTextArea getEditor() {
        return editor;
    }

    public Syntax getSyntax() {
        return syntax;
    }

    public void setSyntax(Syntax syntax){
        this.syntax = syntax;
    }
    public JLabel getStatusLabel(){
        return statusLabel;
    }
    public void setValidationTask(Runnable task) {
         syntaxValidator.setValidationTask(task);
    }

    public void restartValidationTimer() {
        syntaxValidator.restartValidationTimer();
    }

    public void loadTheme(RSyntaxTextArea editor) {
        Path themePath =
                AppConstants.THEMES_DIR
                        .toPath()
                        .resolve(AppPreferences.defaultMacroEditorTheme.get() + ".xml");
        try (InputStream in = Files.newInputStream(themePath)) {
            Theme.load(in).apply(editor);
        } catch (IOException e) {
            System.err.println("Unable to load theme: " + themePath + " " + e);
        }
    }
    
    /** */
    private static final class EditorPopupMenuBuilder {
        private EditorPopupMenuBuilder() {}

        /**
         * @param editor
         * @param syntax
         */
        public static void install(RSyntaxTextArea editor, Syntax syntax) {

            JPopupMenu popupMenu = editor.getPopupMenu();

            // region 1. insert at the top of the pop-up menu
            JMenuItem disabledTitle = new JMenuItem(syntax.toString() + " Editor");
            disabledTitle.setEnabled(false);
            popupMenu.insert(disabledTitle, 0);
            popupMenu.insert(new JPopupMenu.Separator(), 1);
            // endregion

            // region 2. add at the bottom of the pop-up menu
            popupMenu.addSeparator();
            // special JSON editor pop-up menu items
            if (syntax == Syntax.JSON) {
                JMenuItem miPrettify = new JMenuItem("JSON Prettify");
                miPrettify.addActionListener(e -> JsonFormatter.prettify(editor));
                popupMenu.add(miPrettify);
                JMenuItem miMinify = new JMenuItem("JSON Minify");
                miMinify.addActionListener(e -> JsonFormatter.minify(editor));
                popupMenu.add(miMinify);
            }
            // endregion
        }
    }

    private static final class JsonFormatter {

        private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
        private static final Gson MINIFY_GSON = new Gson();

        private JsonFormatter() {}

        public static void prettify(RSyntaxTextArea editor) {
            reformat(editor, PRETTY_GSON);
        }

        public static void minify(RSyntaxTextArea editor) {
            reformat(editor, MINIFY_GSON);
        }

        private static void reformat(RSyntaxTextArea editor, Gson gson) {

            String original = editor.getText();
            if (original.isBlank()) {
                return;
            }

            try {
                JsonElement json = JsonParser.parseString(original);
                String formatted = gson.toJson(json);
                if (Objects.equals(original, formatted)) {
                    return;
                }
                int caret = editor.getCaretPosition();
                editor.beginAtomicEdit();
                try {
                    editor.setText(formatted);
                } finally {
                    editor.endAtomicEdit();
                    editor.setCaretPosition(Math.min(caret, formatted.length()));
                }
            } catch (JsonSyntaxException ignored) {
            }
        }
    }

    /** Use for programmatic updates */
    public void validateNow() {
        syntaxValidator.validateNow();
    }
}
