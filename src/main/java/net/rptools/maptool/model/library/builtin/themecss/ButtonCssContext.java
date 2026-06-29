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
import java.util.Objects;
import java.util.function.Function;
import javax.swing.UIDefaults;
import net.rptools.maptool.client.ui.theme.ThemeSupport;

/**
 * Class that extracts and represents the information passed to handlebars for building the themed
 * CSS.
 */
public class ButtonCssContext {

  /** The button foreground color. */
  private final String foregroundColor;

  /** Starting background color for the button background. */
  private final String startBackgroundColor;

  /** Ending background color for the button background. */
  private final String endBackgroundColor;

  /** The foreground color of the button when it is pressed. */
  private final String pressedForegroundColor;

  /** The background color of the button when it is pressed. */
  private final String pressedBackgroundColor;

  /** The background color of the button when it is disabled. */
  private final String disabledBackgroundColor;

  /** The foreground color of the button when it is disabled. */
  private final String disabledForegroundColor;

  /** The size of the button border when it is disabled. */
  private final String disabledBorderSize;

  /** The border color when the button is disabled. */
  private final String disabledBorderColor;

  /** The foreground color of the button when the mouse pointer is hovering over it. */
  private final String hoverForegroundColor;

  /** The background color of the button when the mouse pointer is hovering over it. */
  private final String hoverBackgroundColor;

  /** The border color of the button when the mouse pointer is hovering over it. */
  private final String hoverBorderColor;

  /** The border color of the button when it has focus. */
  private final String focusedForegroundColor;

  /** The border color of the button when it has focus. */
  private final String focusedBackgroundColor;

  /** The border color of the button when it has focus. */
  private final String focusedBorderColor;

  /** The border width of the button. */
  private final String borderWidth;

  /** The shadow width of the button. */
  private final String shadowWidth;

  /** The shadow color of the button. */
  private final String shadowColor;

  /** The shadow color of the button. */
  private final String startBorderColor;

  /** The shadow color of the button. */
  private final String endBorderColor;

  /** Whether to show a button shadow color. */
  private final int showShadow;

  /**
   * Creates a new <code>ButtonCssContext</code>
   *
   * @param uiDef the {@link UIDefaults} to extract information from.
   * @param formatColor the function to use to format the color.
   */
  public ButtonCssContext(UIDefaults uiDef, Function<Color, String> formatColor) {
    foregroundColor = formatColor.apply(uiDef.getColor("Button.foreground"));
    startBackgroundColor = formatColor.apply(uiDef.getColor("Button.startBackground"));
    endBackgroundColor = formatColor.apply(uiDef.getColor("Button.endBackground"));
    var pressedForeground = uiDef.getColor("Button.pressedForeground");
    pressedForegroundColor = pressedForeground == null ? "" : formatColor.apply(pressedForeground);
    var pressedBackground = uiDef.getColor("Button.pressedBackground");
    pressedBackgroundColor = pressedBackground == null ? "" : formatColor.apply(pressedBackground);
    disabledBackgroundColor = formatColor.apply(uiDef.getColor("Button.disabledBackground"));
    disabledForegroundColor = formatColor.apply(uiDef.getColor("Button.disabledForeground"));
    disabledBorderSize = uiDef.getInt("Button.disabledBorderSize") + "px";
    disabledBorderColor = formatColor.apply(uiDef.getColor("Button.disabledBorderColor"));
    var hoverForeground = uiDef.getColor("Button.hoverForeground");
    hoverForegroundColor = hoverForeground == null ? "" : formatColor.apply(hoverForeground);
    hoverBackgroundColor = formatColor.apply(uiDef.getColor("Button.hoverBackground"));
    hoverBorderColor = formatColor.apply(uiDef.getColor("Button.hoverBorderColor"));
    var focusedForeground = uiDef.getColor("Button.focusedForeground");
    focusedForegroundColor = focusedForeground == null ? "" : formatColor.apply(focusedForeground);
    var focusedBackground = uiDef.getColor("Button.focusedBackground");
    focusedBackgroundColor = focusedBackground == null ? "" : formatColor.apply(focusedBackground);
    focusedBorderColor = formatColor.apply(uiDef.getColor("Button.focusedBorderColor"));
    borderWidth = uiDef.getInt("Button.borderWidth") + "px";
    var shadowClr = uiDef.getColor("Button.shadowColor");
    shadowColor = shadowClr == null ? "" : formatColor.apply(shadowClr);
    shadowWidth = uiDef.getInt("Button.shadowWidth") + "px";
    startBorderColor = formatColor.apply(uiDef.getColor("Button.startBorderColor"));
    endBorderColor = formatColor.apply(uiDef.getColor("Button.endBorderColor"));
    showShadow =
        uiDef.getBoolean("button.showShadow") || Objects.equals(ThemeSupport.getThemeName(), "Aah")
            ? 1
            : 0;
  }

  /**
   * Returns the foreground color of the button.
   *
   * @return the foreground color of the button.
   */
  public String getForegroundColor() {
    return foregroundColor;
  }

  /**
   * Returns the starting background color of the button.
   *
   * @return the starting background color of the button.
   */
  public String getStartBackgroundColor() {
    return startBackgroundColor;
  }

  /**
   * Returns the ending background color of the button.
   *
   * @return the ending background color of the button.
   */
  public String getEndBackgroundColor() {
    return endBackgroundColor;
  }

  /**
   * Returns the foreground color of the button when it is pressed.
   *
   * @return the foreground color of the button when it is pressed.
   */
  public String getPressedForegroundColor() {
    return pressedForegroundColor;
  }

  /**
   * Returns the background color of the button when it is pressed.
   *
   * @return the background color of the button when it is pressed.
   */
  public String getPressedBackgroundColor() {
    return pressedBackgroundColor;
  }

  /**
   * Returns the background color of the button when it is disabled.
   *
   * @return the background color of the button when it is disabled.
   */
  public String getDisabledBackgroundColor() {
    return disabledBackgroundColor;
  }

  /**
   * Returns the foreground color of the button when it is disabled.
   *
   * @return the foreground color of the button when it is disabled.
   */
  public String getDisabledForegroundColor() {
    return disabledForegroundColor;
  }

  /**
   * Returns the size of the border when the button is disabled.
   *
   * @return the size of the border when the button is disabled.
   */
  public String getDisabledBorderSize() {
    return disabledBorderSize;
  }

  /**
   * Returns the color of the border when the button is disabled.
   *
   * @return the color of the border when the button is disabled.
   */
  public String getDisabledBorderColor() {
    return disabledBorderColor;
  }

  /**
   * Returns the background color of the button when the mouse pointer is hovering over it.
   *
   * @return the background color of the button when the mouse pointer is hovering over it.
   */
  public String getHoverBackgroundColor() {
    return hoverBackgroundColor;
  }

  /**
   * Returns the foreground color of the button when the mouse pointer is hovering over it.
   *
   * @return the foreground color of the button when the mouse pointer is hovering over it.
   */
  public String getHoverForegroundColor() {
    return hoverForegroundColor;
  }

  /**
   * Returns the border color of the button when the mouse pointer is hovering over it.
   *
   * @return the border color of the button when the mouse pointer is hovering over it.
   */
  public String getHoverBorderColor() {
    return hoverBorderColor;
  }

  /**
   * Returns the foreground color of the button when it has focus.
   *
   * @return the foreground color of the button when it has focus.
   */
  public String getFocusedForegroundColor() {
    return focusedForegroundColor;
  }

  /**
   * Returns the background color of the button when it has focus.
   *
   * @return the background color of the button when it has focus.
   */
  public String getFocusedBackgroundColor() {
    return focusedBackgroundColor;
  }

  /**
   * Returns the border color of the button when it has focus.
   *
   * @return the border color of the button when it has focus.
   */
  public String getFocusedBorderColor() {
    return focusedBorderColor;
  }

  /**
   * Returns the border width of the button.
   *
   * @return the border width of the button.
   */
  public String getBorderWidth() {
    return borderWidth;
  }

  /**
   * Returns the shadow width of the button.
   *
   * @return the shadow width of the button.
   */
  public String getShadowWidth() {
    return shadowWidth;
  }

  /**
   * Returns the shadow color of the button.
   *
   * @return the shadow color of the button.
   */
  public String getShadowColor() {
    return shadowColor;
  }

  /**
   * Returns the start border color of the button.
   *
   * @return the start border color of the button.
   */
  public String getStartBorderColor() {
    return startBorderColor;
  }

  /**
   * Returns the end border color of the button.
   *
   * @return the end border color of the button.
   */
  public String getEndBorderColor() {
    return endBorderColor;
  }

  /**
   * Returns whether to show a button shadow.
   *
   * @return whether to show a button shadow.
   */
  public int getShowShadow() {
    return showShadow;
  }
}
