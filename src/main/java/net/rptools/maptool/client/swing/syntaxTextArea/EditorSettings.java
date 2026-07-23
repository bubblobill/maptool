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

import static org.fife.ui.rsyntaxtextarea.TokenTypes.*;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.ColorFunctions;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import net.rptools.maptool.client.ui.theme.Images;
import net.rptools.maptool.client.ui.theme.RessourceManager;
import net.rptools.maptool.client.ui.theme.ThemeSupport;
import net.rptools.maptool.util.ColourUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rtextarea.*;

public class EditorSettings {
  public static final String BACKGROUND_PROPERTY = "TextArea.background";
  public static final String CARET_COLOR_PROPERTY = "TextArea.caretForeground";
  public static final String FONT_PROPERTY = "TextArea.font";
  public static final String FOREGROUND_PROPERTY = "TextArea.foreground";
  public static final String MARGIN_PROPERTY = "TextArea.margin";
  public static final String SELECTION_BACKGROUND_PROPERTY = "TextArea.selectionBackground";
  public static final String SELECTION_FOREGROUND_PROPERTY = "TextArea.selectionForeground";
  public static final String HIGHLIGHT_COLOR_PROPERTY = "TextField.highlight";
  public static final String HYPERLINK_COLOR_PROPERTY = "Actions.Blue";

  private static final ImageIcon BOOKMARK;
  private static final EditorSettings DEFAULTS;
  private static final List<Color> BACKGROUNDS = new ArrayList<>();
  private static final List<Color> FOREGROUNDS;

  //  private static Font font = UIManager.getFont(FONT_PROPERTY);
  private static Font font = FlatUIUtils.nonUIResource(UIManager.getFont("monospaced.font"));
  private static Font gutterLineNumberFont = font.deriveFont(font.getSize2D() - 2.8f);
  private static SyntaxScheme syntaxScheme = new SyntaxScheme(font);

  private boolean animateBracketMatching = false;
  private boolean antiAliasingEnabled = true;
  private boolean autoIndentEnabled = true;
  private boolean bracketMatchingEnabled = true;
  private boolean clearWhitespaceLines = true;
  private boolean closeCurlyBraces = true;
  private boolean closeMarkupTags = true;
  private boolean codeFoldingEnabled = true;
  private boolean eolMarkersVisible = false;
  private boolean enableBookmarking = true;
  private boolean enableLineNumbers = true;
  private boolean fadeCurrentLineHighlight = true;
  private boolean fractionalFontMetricsEnabled = true;
  private boolean highlightCurrentLine = true;
  private boolean highlightSecondaryLanguages = true;
  private boolean hyperlinksEnabled = false;
  private boolean insertPairedCharacters = false;
  private boolean lineWrap = true;
  private boolean marginLineEnabled = false;
  private boolean markAllOnOccurrenceSearches = true;
  private boolean markOccurrences = true;
  private boolean paintMarkOccurrencesBorder = true;
  private boolean paintMatchedBracketPair = true;
  private boolean paintTabLines = false;
  private boolean showMatchedBracketPopup = true;
  private boolean tabsEmulated = false;
  private boolean useFocusableTips = true;
  private boolean useSelectedTextColor = false;
  private boolean whitespaceVisible = false;
  private boolean wrapStyleWord = true;

  private int marginLinePosition = 180;
  private int markOccurrencesDelay = 1000;
  private int rhsCorrection = 1;
  private int tabSize = 4;
  private int textMode = RSyntaxTextArea.INSERT_MODE;

  private Insets margin = UIManager.getDefaults().getInsets(MARGIN_PROPERTY);

  private Color background = UIManager.getDefaults().getColor(BACKGROUND_PROPERTY);
  private Color foreground = UIManager.getDefaults().getColor(FOREGROUND_PROPERTY);
  private Color hyperlinkColor = UIManager.getDefaults().getColor(HYPERLINK_COLOR_PROPERTY);
  private Color selectionBackground =
      UIManager.getDefaults().getColor(SELECTION_BACKGROUND_PROPERTY);
  private Color selectionForeground =
      UIManager.getDefaults().getColor(SELECTION_FOREGROUND_PROPERTY);
  private Color caretColor = UIManager.getDefaults().getColor(CARET_COLOR_PROPERTY);
  private Color currentLineHighlightColor =
      ColorFunctions.fade(UIManager.getDefaults().getColor(HIGHLIGHT_COLOR_PROPERTY), 0.07f);

  private Color marginLineColor = new Color(0x394448);
  private Color markAllHighlightColor = new Color(0x6b8189);
  private Color markOccurrencesColor = new Color(0x5b7179);
  private Color matchedBracketBGColor = new Color(0x6b8189);
  private Color[] secondaryLanguageBackgrounds =
      new Color[] {new Color(0x333344), new Color(0x223322), new Color(0x332222)};
  private Color gutterBorderColor = new Color(0x81969A);
  private Color gutterLineNumberColor = new Color(0x81969A);
  private Color gutterCurrentLineNumberColor = new Color(0xa9b7c6);
  private Color gutterFoldForeground = new Color(0x6A8088);
  private Color gutterArmedFoldForeground = new Color(0x3399ff);

  private Color gutterActiveLineRangeColor = new Color(0x3399ff);
  private Color matchedBracketBorderColor = RSyntaxTextArea.getDefaultBracketMatchBorderColor();
  private Color tabLineColor = new Color(-8355712);

  static {
    BOOKMARK = new ImageIcon(RessourceManager.getImage(Images.MAPTOOL_LOGO_MINI));

    boolean isDark = ThemeSupport.isDark();
    BACKGROUNDS.addAll(
        isDark
            ? ColourUtil.COLOURBLIND_FRIENDLY_PALETTE_LIGHT
            : ColourUtil.COLOURBLIND_FRIENDLY_PALETTE_DARK);
    BACKGROUNDS.replaceAll(color -> ColorFunctions.fade(color, 0.07f));
    FOREGROUNDS =
        isDark
            ? ColourUtil.COLOURBLIND_FRIENDLY_TEXT_DARK_PALETTE
            : ColourUtil.COLOURBLIND_FRIENDLY_TEXT_LIGHT_PALETTE;

    Color black = FOREGROUNDS.getFirst();
    Color orange = FOREGROUNDS.get(1);
    Color lightBlue = FOREGROUNDS.get(2);
    Color green = FOREGROUNDS.get(3);
    Color yellow = FOREGROUNDS.get(4);
    Color darkBlue = FOREGROUNDS.get(5);
    Color red = FOREGROUNDS.get(6);
    Color pink = FOREGROUNDS.get(7);

    Color bgBlack = BACKGROUNDS.getFirst();
    Color bgOrange = BACKGROUNDS.get(1);
    Color bgLightBlue = BACKGROUNDS.get(2);
    Color bgGreen = BACKGROUNDS.get(3);
    Color bgYellow = BACKGROUNDS.get(4);
    Color bgDarkBlue = BACKGROUNDS.get(5);
    Color bgRed = BACKGROUNDS.get(6);
    Color bgPink = BACKGROUNDS.get(7);

    for (int i = 0; i < SyntaxScheme.DEFAULT_NUM_TOKEN_TYPES; i++) {
      Style style = null;
      switch (i) {
        case NULL -> style = new Style(black);
        // Variable
        case IDENTIFIER -> style = new Style(red);
        // ROLL_OPTION
        case RESERVED_WORD -> style = new Style(darkBlue, null, font.deriveFont(Font.BOLD));
        // EVENT_NAME
        case RESERVED_WORD_2 -> style = new Style(green, null, font.deriveFont(Font.BOLD));
        // UDF
        case ANNOTATION -> style = new Style(red, null, font.deriveFont(Font.BOLD));
        // SPECIAL_VARIABLES
        case DATA_TYPE -> style = new Style(yellow, null, font.deriveFont(Font.BOLD));
        case OPERATOR -> style = new Style(lightBlue, null, font.deriveFont(Font.BOLD));
        // PROPERTY_NAME
        case VARIABLE -> style = new Style(lightBlue, null, font.deriveFont(Font.ITALIC));

        case FUNCTION -> style = new Style(orange, null, font.deriveFont(Font.BOLD));
        // values
        case LITERAL_BOOLEAN,
                LITERAL_NUMBER_DECIMAL_INT,
                LITERAL_NUMBER_FLOAT,
                LITERAL_NUMBER_HEXADECIMAL,
                LITERAL_STRING_DOUBLE_QUOTE,
                LITERAL_CHAR,
                LITERAL_BACKQUOTE ->
            style = new Style(green);
        // Brackets
        case SEPARATOR -> style = new Style(orange, bgLightBlue, font.deriveFont(Font.BOLD));

        case COMMENT_DOCUMENTATION -> style = new Style(Color.MAGENTA, Color.CYAN);
        case COMMENT_EOL -> style = new Style(Color.MAGENTA, Color.YELLOW);
        case COMMENT_MULTILINE -> style = new Style(Color.MAGENTA, Color.RED);
        case COMMENT_KEYWORD -> style = new Style(Color.RED, Color.PINK);
        case COMMENT_MARKUP -> style = new Style(Color.MAGENTA, Color.black);

        case MARKUP_TAG_DELIMITER ->
            style = new Style(Color.ORANGE, bgYellow, font.deriveFont(Font.ITALIC));
        case MARKUP_TAG_NAME -> style = new Style(Color.CYAN, bgYellow);
        case MARKUP_TAG_ATTRIBUTE ->
            style = new Style(Color.CYAN, bgYellow, font.deriveFont(Font.ITALIC));
        case MARKUP_TAG_ATTRIBUTE_VALUE ->
            style = new Style(Color.CYAN, bgYellow, font.deriveFont(Font.ITALIC));
        case MARKUP_COMMENT ->
            style = new Style(Color.CYAN, bgYellow, font.deriveFont(Font.ITALIC));
        case MARKUP_DTD -> style = new Style(Color.CYAN, bgYellow, font.deriveFont(Font.ITALIC));
        case MARKUP_PROCESSING_INSTRUCTION ->
            style = new Style(Color.CYAN, bgYellow, font.deriveFont(Font.ITALIC));
        case MARKUP_CDATA ->
            style = new Style(Color.CYAN, Color.BLUE, font.deriveFont(Font.ITALIC));
        case MARKUP_CDATA_DELIMITER ->
            style = new Style(Color.CYAN, Color.black, font.deriveFont(Font.BOLD));
        case MARKUP_ENTITY_REFERENCE ->
            style = new Style(Color.CYAN, Color.RED, font.deriveFont(Font.ITALIC));

        case PREPROCESSOR -> style = new Style(red, bgBlack, font.deriveFont(Font.ITALIC));
        case REGEX -> style = new Style(red, bgOrange, font.deriveFont(Font.BOLD));

        case WHITESPACE -> style = new Style(null);
        case ERROR_IDENTIFIER, ERROR_NUMBER_FORMAT, ERROR_STRING_DOUBLE, ERROR_CHAR ->
            style = new Style(red, bgPink, font.deriveFont(Font.BOLD));
      }
      syntaxScheme.setStyle(i, style);
    }
    DEFAULTS = new EditorSettings();
  }

  public static EditorSettings getDefaults() {
    return DEFAULTS;
  }

  public void apply(SyntaxTextArea sta) {
    RSyntaxTextArea rsta = sta.getEditor();

    rsta.setAnimateBracketMatching(animateBracketMatching);

    rsta.setAntiAliasingEnabled(antiAliasingEnabled);
    rsta.setAutoIndentEnabled(autoIndentEnabled);
    rsta.setBracketMatchingEnabled(bracketMatchingEnabled);
    rsta.setBackground(background);
    rsta.setCaretColor(caretColor);
    rsta.setCloseMarkupTags(closeMarkupTags);
    rsta.setCodeFoldingEnabled(codeFoldingEnabled);
    rsta.setCloseCurlyBraces(closeCurlyBraces);
    rsta.setClearWhitespaceLinesEnabled(clearWhitespaceLines);
    rsta.setCurrentLineHighlightColor(currentLineHighlightColor);
    rsta.setEOLMarkersVisible(eolMarkersVisible);
    rsta.setFractionalFontMetricsEnabled(fractionalFontMetricsEnabled);
    rsta.setFadeCurrentLineHighlight(fadeCurrentLineHighlight);
    rsta.setForeground(foreground);
    rsta.setFont(font);
    rsta.setHighlightCurrentLine(highlightCurrentLine);
    rsta.setHighlightSecondaryLanguages(highlightSecondaryLanguages);

    rsta.setHyperlinksEnabled(hyperlinksEnabled);
    rsta.setHyperlinkForeground(hyperlinkColor);
    rsta.setInsertPairedCharacters(insertPairedCharacters);
    rsta.setLineWrap(lineWrap);
    rsta.setMarkAllOnOccurrenceSearches(markAllOnOccurrenceSearches);
    rsta.setMarkOccurrences(markOccurrences);
    rsta.setMarkOccurrencesColor(markOccurrencesColor);
    rsta.setMatchedBracketBGColor(matchedBracketBGColor);
    rsta.setMatchedBracketBorderColor(matchedBracketBorderColor);
    rsta.setMarkAllHighlightColor(markAllHighlightColor);
    rsta.setMarkOccurrencesDelay(markOccurrencesDelay);
    rsta.setMarginLineColor(marginLineColor);
    rsta.setMarginLineEnabled(marginLineEnabled);
    rsta.setMarginLinePosition(marginLinePosition);
    rsta.setMargin(margin);
    rsta.setPaintTabLines(paintTabLines);
    rsta.setPaintMarkOccurrencesBorder(paintMarkOccurrencesBorder);
    rsta.setPaintMatchedBracketPair(paintMatchedBracketPair);
    rsta.setRightHandSideCorrection(rhsCorrection);
    rsta.setShowMatchedBracketPopup(showMatchedBracketPopup);
    rsta.setSecondaryLanguageBackground(1, secondaryLanguageBackgrounds[0]);
    rsta.setSecondaryLanguageBackground(2, secondaryLanguageBackgrounds[1]);
    rsta.setSecondaryLanguageBackground(3, secondaryLanguageBackgrounds[2]);
    rsta.setSelectedTextColor(selectionForeground);
    rsta.setSelectionColor(selectionBackground);
    rsta.setTabLineColor(tabLineColor);
    rsta.setTabsEmulated(tabsEmulated);
    rsta.setTabSize(tabSize);
    rsta.setTextMode(textMode);
    rsta.setUseFocusableTips(useFocusableTips);
    rsta.setUseSelectedTextColor(useSelectedTextColor);
    rsta.setWrapStyleWord(wrapStyleWord);
    rsta.setWhitespaceVisible(whitespaceVisible);

    if (syntaxScheme != null) {
      rsta.setSyntaxScheme(syntaxScheme);
    }

    RTextScrollPane sp = sta.getScrollPane();
    sp.setLineNumbersEnabled(enableLineNumbers);

    Gutter gutter = sp.getGutter();
    if (enableBookmarking) {
      gutter.setBookmarkingEnabled(enableBookmarking);
      gutter.setSpacingBetweenLineNumbersAndFoldIndicator(3);
      gutter.setBookmarkIcon(BOOKMARK);
    }

    gutter.setLineNumberFont(gutterLineNumberFont);
    gutter.setLineNumberColor(gutterLineNumberColor);
    gutter.setCurrentLineNumberColor(gutterCurrentLineNumberColor);
    gutter.setActiveLineRangeColor(gutterActiveLineRangeColor);
    gutter.setBorderColor(gutterBorderColor);
    if (codeFoldingEnabled) {
      gutter.setArmedFoldBackground(background);
      gutter.setFoldIndicatorArmedForeground(gutterArmedFoldForeground);
      gutter.setFoldBackground(background);
      gutter.setFoldIndicatorForeground(gutterFoldForeground);
      gutter.setFoldIndicatorStyle(FoldIndicatorStyle.CLASSIC);
      gutter.setExpandedFoldRenderStrategy(ExpandedFoldRenderStrategy.ALWAYS);
    }
    rsta.revalidate();
  }
}
