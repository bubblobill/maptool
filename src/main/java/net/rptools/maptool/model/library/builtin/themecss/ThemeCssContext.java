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
package net.rptools.maptool.model.library.builtin.themecss;

import java.awt.Color;
import javax.swing.UIManager;

/** Context for the theme CSS. This is used to provide the CSS with the values it needs to render */
public class ThemeCssContext {

  /** The font size to use for the theme. */
  private final String fontSize;

  /** The font family to use for the theme. */
  private final String fontFamily;

  /** The background color to use for the theme. */
  private final String backgroundColor;

  /** The foreground color to use for the theme. */
  private final String foregroundColor;

  /** The disabled foreground color to use for the theme. */
  private final String foregroundColorDisabled;

  /** The background color to use for the panel. */
  private final String panelBackgroundColor;

  /** The foreground color to use for the panel. */
  private final String panelForegroundColor;

  /** The button CSS context. */
  private final ButtonCssContext button;

  /** The checkbox CSS context. */
  private final CheckBoxCssContext checkBox;

  /** The theme color CSS context. */
  private final ColorCssContext themeColor;

  /** The combo box CSS context. */
  private final ComboBoxCssContext comboBox;

  /** The component CSS context. */
  private final ComponentCssContext component;

  /** The meter progress bar CSS context. */
  private final MeterProgressBarCssContext meterProgressBar;

  /** The preferences CSS context. */
  private final PreferencesCssContext preferences;

  /** The progress bar CSS context. */
  private final ProgressBarCssContext progressBar;

  /** The scroll bar CSS context. */
  private final ScrollBarCssContext scrollBar;

  /** The slider CSS context. */
  private final SliderCssContext slider;

  /** The text area CSS context. */
  private final TextAreaCssContext textArea;

  /** The text input CSS context. */
  private final TextInputCssContext textInput;

  /** The theme header CSS context. */
  private final ThemeHeader themeHeader;

  /** Creates a new instance of the theme CSS context. */
  public ThemeCssContext() {
    var uiDef = UIManager.getDefaults();
    backgroundColor = formatColor(uiDef.getColor("Label.background"));
    foregroundColor = formatColor(uiDef.getColor("Label.foreground"));
    foregroundColorDisabled = formatColor(uiDef.getColor("Label.disabledForeground"));
    panelForegroundColor = formatColor(uiDef.getColor("Panel.foreground"));
    panelBackgroundColor = formatColor(uiDef.getColor("Panel.background"));
    var font = uiDef.getFont("Label.font");
    fontFamily = font.getFamily();
    fontSize = font.getSize() + "px";

    // special css contexts
    preferences = new PreferencesCssContext(ThemeCssContext::formatColor);
    themeColor = new ColorCssContext(uiDef, ThemeCssContext::formatColor);
    themeHeader = new ThemeHeader(uiDef);

    // swing control css contexts
    button = new ButtonCssContext(uiDef, ThemeCssContext::formatColor);
    checkBox = new CheckBoxCssContext(uiDef, ThemeCssContext::formatColor);
    comboBox = new ComboBoxCssContext(uiDef, ThemeCssContext::formatColor);
    component = new ComponentCssContext(uiDef, ThemeCssContext::formatColor);
    meterProgressBar = new MeterProgressBarCssContext(uiDef, ThemeCssContext::formatColor);
    progressBar = new ProgressBarCssContext(uiDef, ThemeCssContext::formatColor);
    scrollBar = new ScrollBarCssContext(uiDef, ThemeCssContext::formatColor);
    slider = new SliderCssContext(uiDef, ThemeCssContext::formatColor);
    textArea = new TextAreaCssContext(uiDef, ThemeCssContext::formatColor);
    textInput = new TextInputCssContext(uiDef, ThemeCssContext::formatColor);
  }

  /**
   * Formats the specified color into a CSS color string.
   *
   * @param color The color to format.
   * @return The formatted color.
   */
  static String formatColor(Color color) {
    return String.format(
        "rgba(%d, %d, %d, %.02f)",
        color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255.0);
  }

  /**
   * Gets the font size.
   *
   * @return The font size.
   */
  public String getFontSize() {
    return fontSize;
  }

  /**
   * Gets the font family.
   *
   * @return The font family.
   */
  public String getFontFamily() {
    return fontFamily;
  }

  /**
   * Gets the background color.
   *
   * @return The background color.
   */
  public String getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * Gets the foreground color.
   *
   * @return The foreground color.
   */
  public String getForegroundColor() {
    return foregroundColor;
  }

  /**
   * Gets the disabled foreground color CSS.
   *
   * @return The disabled foreground color CSS.
   */
  public String getForegroundColorDisabled() {
    return foregroundColorDisabled;
  }

  /**
   * Gets the panel background color.
   *
   * @return The panel background color.
   */
  public String getPanelBackgroundColor() {
    return panelBackgroundColor;
  }

  /** Gets the panel foreground color. */
  public String getPanelForegroundColor() {
    return panelForegroundColor;
  }

  /**
   * Gets the button CSS context.
   *
   * @return The button CSS context.
   */
  public ButtonCssContext getButton() {
    return button;
  }

  /**
   * Gets the check box CSS context.
   *
   * @return The check box CSS context.
   */
  public CheckBoxCssContext getCheckBox() {
    return checkBox;
  }

  /**
   * Gets the theme color CSS context.
   *
   * @return The theme color CSS context.
   */
  public ColorCssContext getThemeColor() {
    return themeColor;
  }

  /**
   * Gets the combo box CSS context.
   *
   * @return The combo box CSS context.
   */
  public ComboBoxCssContext getComboBox() {
    return comboBox;
  }

  /**
   * Gets the component CSS context.
   *
   * @return The component CSS context.
   */
  public ComponentCssContext getComponent() {
    return component;
  }

  /**
   * Gets the meter progress bar CSS context.
   *
   * @return The meter progress bar CSS context.
   */
  public MeterProgressBarCssContext getMeterProgressBar() {
    return meterProgressBar;
  }

  /**
   * Gets the preferences CSS context.
   *
   * @return The preferences CSS context.
   */
  public PreferencesCssContext getPreferences() {
    return preferences;
  }

  /**
   * Gets the progress bar CSS context.
   *
   * @return The progress bar CSS context.
   */
  public ProgressBarCssContext getProgressBar() {
    return progressBar;
  }

  /**
   * Gets the scroll bar CSS context.
   *
   * @return The scroll bar CSS context.
   */
  public ScrollBarCssContext getScrollBar() {
    return scrollBar;
  }

  /**
   * Gets the slider CSS context.
   *
   * @return The slider CSS context.
   */
  public SliderCssContext getSlider() {
    return slider;
  }

  /**
   * Gets the text area CSS context.
   *
   * @return The text area CSS context.
   */
  public TextAreaCssContext getTextArea() {
    return textArea;
  }

  /**
   * Gets the text input CSS context.
   *
   * @return The text input CSS context.
   */
  public TextInputCssContext getTextInput() {
    return textInput;
  }

  /**
   * Gets the theme header CSS context.
   *
   * @return The theme header CSS context.
   */
  public ThemeHeader getThemeHeader() {
    return themeHeader;
  }
}
