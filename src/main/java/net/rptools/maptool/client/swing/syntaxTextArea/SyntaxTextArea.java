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
package net.rptools.maptool.client.swing.syntaxTextArea;

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
import net.rptools.maptool.client.ui.syntax.Syntax;
import net.rptools.maptool.client.ui.syntax.validation.SyntaxValidator;
import org.fife.rsta.ac.css.CssLanguageSupport;
import org.fife.rsta.ac.html.HtmlLanguageSupport;
import org.fife.rsta.ac.js.JavaScriptLanguageSupport;
import org.fife.rsta.ac.xml.XmlLanguageSupport;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rsyntaxtextarea.folding.CurlyFoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rtextarea.RTextScrollPane;

public class SyntaxTextArea extends JPanel {
  private final RSyntaxTextArea editor;
  private final RTextScrollPane scrollPane;
  private final SyntaxValidator syntaxValidator;
  private JLabel statusLabel;
  private Syntax syntax;
  private static final FoldParserManager FOLD_PARSER_MANAGER = FoldParserManager.get();
  private static final AbstractTokenMakerFactory TMF =
      (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
  private static final AutoCompletion AC_MTS =
      new AutoCompletion(new MapToolScriptAutoComplete().get());

  static {
    TMF.putMapping(
        Syntax.MAPTOOL_SCRIPT.rSyntaxStyle(),
        "net.rptools.maptool.client.ui.syntax.MapToolScriptSyntax");
    TMF.putMapping(Syntax.CSS.rSyntaxStyle(), "org.fife.ui.rsyntaxtextarea.modes.CSSTokenMaker");
    TMF.putMapping(Syntax.CSV.rSyntaxStyle(), "org.fife.ui.rsyntaxtextarea.modes.CsvTokenMaker");
    TMF.putMapping(
        Syntax.HANDLEBARS.rSyntaxStyle(), "org.fife.ui.rsyntaxtextarea.modes.HandlebarsTokenMaker");
    TMF.putMapping(Syntax.HTML.rSyntaxStyle(), "org.fife.ui.rsyntaxtextarea.modes.HTMLTokenMaker");
    TMF.putMapping(
        Syntax.JAVASCRIPT.rSyntaxStyle(), "org.fife.ui.rsyntaxtextarea.modes.JavaScriptTokenMaker");
    TMF.putMapping(Syntax.JSON.rSyntaxStyle(), "org.fife.ui.rsyntaxtextarea.modes.JsonTokenMaker");
    TMF.putMapping(
        Syntax.MARKDOWN.rSyntaxStyle(), "org.fife.ui.rsyntaxtextarea.modes.MarkdownTokenMaker");
    TMF.putMapping(
        Syntax.NONE.rSyntaxStyle(), "org.fife.ui.rsyntaxtextarea.modes.PlainTextTokenMaker");
    TMF.putMapping(Syntax.XML.rSyntaxStyle(), "org.fife.ui.rsyntaxtextarea.modes.XMLTokenMaker");
    FOLD_PARSER_MANAGER.addFoldParserMapping(
        Syntax.MAPTOOL_SCRIPT.rSyntaxStyle(), new CurlyFoldParser());
    AC_MTS.setAutoCompleteEnabled(true);
    AC_MTS.setAutoActivationEnabled(true);
    AC_MTS.setAutoActivationDelay(500);
    AC_MTS.setShowDescWindow(true);
    AC_MTS.setAutoCompleteSingleChoices(false);
  }

  public SyntaxTextArea() {
    this(Syntax.NONE);
  }

  public SyntaxTextArea(Syntax syntax) {
    editor = new RSyntaxTextArea(syntax.rSyntaxStyle());
    // bung the editor into a scroll pane (so we get line numbers and scrolling)
    scrollPane = new RTextScrollPane(editor);
    // we line wrap so a horizontal scrollbar is not required, vertical always
    // n.b. AS_NEEDED policy options caused Swing layout jitter so is avoided
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    // set the Syntax
    setSyntax(syntax);

    // configure the editor
    EditorSettings.getDefaults().apply(this);

    // customise the popup menu
    EditorPopupMenuBuilder.install(editor, syntax);

    // set caret position
    SwingUtilities.invokeLater(() -> editor.setCaretPosition(0));

    // plop everything in the panel
    setLayout(new MigLayout("fill", "[grow]", "[grow]"));
    add(scrollPane, "grow, push, wrap");

    // check if what has been provided is valid (and update the status label)
    syntaxValidator = SyntaxValidator.createValidator(this);
    syntaxValidator.validate();
  }

  public SyntaxTextArea(int rows, int cols, String specSyntax) {
    this(rows, cols, Syntax.lookup(specSyntax));
  }

  public SyntaxTextArea(int rows, int cols, Syntax syntax) {
    this(syntax);
    editor.setRows(rows);
    editor.setColumns(cols);
    // create and include the status label
    this.statusLabel = new JLabel();
    add(statusLabel);
  }

  public RSyntaxTextArea getEditor() {
    return editor;
  }

  public Syntax getSyntax() {
    return syntax;
  }

  public void setSyntax(Syntax syntax) {
    if (this.syntax != null && this.syntax.equals(syntax)) {
      return;
    }
    this.syntax = syntax;

    switch (syntax) {
      case Syntax.MAPTOOL_SCRIPT -> AC_MTS.install(editor);
      case CSS -> new CssLanguageSupport().install(editor);
      case JAVASCRIPT -> new JavaScriptLanguageSupport().install(editor);
      case XML -> new XmlLanguageSupport().install(editor);
      case HTML -> new HtmlLanguageSupport().install(editor);
      case null, default -> new AutoCompletion(new DefaultCompletionProvider()).install(editor);
    }
    editor.revalidate();
    validateNow();
  }

  public JLabel getStatusLabel() {
    return statusLabel;
  }

  public void setValidationTask(Runnable task) {
    if (syntaxValidator != null) {
      syntaxValidator.setValidationTask(task);
    }
  }

  public void restartValidationTimer() {
    if (syntaxValidator != null) {
      syntaxValidator.restartValidationTimer();
    }
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

  /** Use for programmatic updates */
  public void validateNow() {
    if (syntaxValidator != null) {
      syntaxValidator.validateNow();
    }
  }

  public RTextScrollPane getScrollPane() {
    return scrollPane;
  }

  /** */
  private static final class EditorPopupMenuBuilder {
    private EditorPopupMenuBuilder() {}

    /**
     * @param editor editor to install to
     * @param syntax syntax language
     */
    public static void install(RSyntaxTextArea editor, Syntax syntax) {

      JPopupMenu popupMenu = editor.getPopupMenu();

      // region 1. insert at the top of the pop-up menu
      JMenuItem disabledTitle = new JMenuItem(syntax.toString() + " Editor");
      disabledTitle.setEnabled(false);
      popupMenu.insert(disabledTitle, 0);
      popupMenu.insert(new JPopupMenu.Separator(), 1);
      // endregion

      // special JSON editor pop-up menu items
      if (syntax == Syntax.JSON) {
        // region 2. add at the bottom of the pop-up menu
        popupMenu.addSeparator();
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
}
