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
import java.util.function.Function;
import javax.swing.UIDefaults;
import javax.swing.plaf.InsetsUIResource;

/**
 * A context for the scroll bar CSS. This is used to extract and provide the scroll bar CSS values.
 */
public class ScrollBarCssContext {

  /** The scroll bar width. */
  private final String width;

  /** The scroll bar background color. */
  private final String backgroundColor;

  /** The scroll bar foreground color. */
  private final String foregroundColor;

  /** The scroll bar button hover color. */
  private final String hoverButtonBackgroundColor;

  /** The scroll bar thumb hover color. */
  private final String hoverThumbColor;

  /** The scroll bar track hover color. */
  private final String hoverTrackColor;

  /** The scroll bar thumb pressed color. */
  private final String pressedThumbColor;

  /** The scroll bar show buttons flag. */
  private final int showButtons;

  /** The scroll bar thumb highlight color. */
  private final String thumbHighlightColor;

  /** The scroll bar thumb arc. */
  private final int thumbArc;

  /** The scroll bar thumb border color. */
  private final String thumbBorderColor;

  /** The scroll bar thumb shadow color. */
  private final String thumbShadowColor;

  /** The scroll bar thumb dark shadow color. */
  private final String thumbDarkShadowColor;

  /** The scroll bar thumb color. */
  private final String thumbColor;

  /** The scroll bar thumb top inset. */
  private final int thumbInsetsTop;

  /** The scroll bar thumb right inset. */
  private final int thumbInsetsRight;

  /** The scroll bar thumb bottom inset. */
  private final int thumbInsetsBottom;

  /** The scroll bar thumb left inset. */
  private final int thumbInsetsLeft;

  /** The scroll bar track color. */
  private final String trackColor;

  /** The scroll bar track top inset. */
  private final int trackInsetsTop;

  /** The scroll bar track right inset. */
  private final int trackInsetsRight;

  /** The scroll bar track bottom inset. */
  private final int trackInsetsBottom;

  /** The scroll bar track left inset. */
  private final int trackInsetsLeft;

  /** Whether scroll bar button has track insets. */
  private final int hasTrackInsets;

  /** The scroll bar button arrow color. */
  private final String buttonArrowColor;

  /**
   * Creates a new instance of the theme CSS context.
   *
   * @param uiDef the UI defaults to use.
   */
  public ScrollBarCssContext(UIDefaults uiDef, Function<Color, String> formatColor) {
    width = uiDef.get("ScrollBar.width") == null ? "12px" : uiDef.getInt("ScrollBar.width") + "px";
    backgroundColor = formatColor.apply(uiDef.getColor("ScrollBar.background"));
    foregroundColor = formatColor.apply(uiDef.getColor("ScrollBar.foreground"));
    hoverButtonBackgroundColor =
        formatColor.apply(uiDef.getColor("ScrollBar.hoverButtonBackground"));
    hoverThumbColor = formatColor.apply(uiDef.getColor("ScrollBar.hoverThumbColor"));
    hoverTrackColor = formatColor.apply(uiDef.getColor("ScrollBar.hoverTrackColor"));
    pressedThumbColor = formatColor.apply(uiDef.getColor("ScrollBar.pressedThumbColor"));
    showButtons = uiDef.getBoolean("ScrollBar.showButtons") ? 1 : 0;
    thumbHighlightColor = formatColor.apply(uiDef.getColor("ScrollBar.thumbHighlight"));
    thumbArc = uiDef.getInt("ScrollBar.thumbArc");
    thumbShadowColor = formatColor.apply(uiDef.getColor("ScrollBar.thumbShadow"));
    var dscol = uiDef.getColor("ScrollBar.thumbDarkShadow");
    thumbDarkShadowColor = dscol != null ? formatColor.apply(dscol) : thumbShadowColor;
    var tcol = uiDef.getColor("ScrollBar.thumbBorderColor");
    thumbBorderColor = tcol != null ? formatColor.apply(tcol) : thumbShadowColor;
    thumbColor = formatColor.apply(uiDef.getColor("ScrollBar.thumb"));
    InsetsUIResource thumbInsets = (InsetsUIResource) uiDef.get("ScrollBar.thumbInsets");
    thumbInsetsTop = thumbInsets == null ? 2 : thumbInsets.top;
    thumbInsetsRight = thumbInsets == null ? 2 : thumbInsets.right;
    thumbInsetsBottom = thumbInsets == null ? 2 : thumbInsets.bottom;
    thumbInsetsLeft = thumbInsets == null ? 2 : thumbInsets.left;
    trackColor = formatColor.apply(uiDef.getColor("ScrollBar.track"));
    InsetsUIResource trackInsets = (InsetsUIResource) uiDef.get("ScrollBar.trackInsets");
    trackInsetsTop = trackInsets == null ? 0 : trackInsets.top;
    trackInsetsRight = trackInsets == null ? 0 : trackInsets.right;
    trackInsetsBottom = trackInsets == null ? 0 : trackInsets.bottom;
    trackInsetsLeft = trackInsets == null ? 0 : trackInsets.left;
    hasTrackInsets = trackInsets == null ? 0 : 1;
    buttonArrowColor = formatColor.apply(uiDef.getColor("ScrollBar.buttonArrowColor"));
  }

  /** Gets the scroll bar width. */
  public String getWidth() {
    return width;
  }

  /** Gets the scroll background color. */
  public String getBackgroundColor() {
    return backgroundColor;
  }

  /** Gets the scroll foreground color. */
  public String getForegroundColor() {
    return foregroundColor;
  }

  /** Gets the scroll button hover color. */
  public String getHoverButtonBackgroundColor() {
    return hoverButtonBackgroundColor;
  }

  /** Gets the scroll thumb hover color. */
  public String getHoverThumbColor() {
    return hoverThumbColor;
  }

  /** Gets the scroll track hover color. */
  public String getHoverTrackColor() {
    return hoverTrackColor;
  }

  /** Gets the scroll bar thumb pressed color. */
  public String getPressedThumbColor() {
    return pressedThumbColor;
  }

  /** Gets the scroll bar show buttons. */
  public int getShowButtons() {
    return showButtons;
  }

  /** Gets the scroll bar thumb highlight color. */
  public String getThumbHighlightColor() {
    return thumbHighlightColor;
  }

  /** Gets the scroll bar thumb arc. */
  public int getThumbArc() {
    return thumbArc;
  }

  /** Gets the scroll bar thumb border color. */
  public String getThumbBorderColor() {
    return thumbBorderColor;
  }

  /** Gets the scroll bar thumb shadow color. */
  public String getThumbShadowColor() {
    return thumbShadowColor;
  }

  /** Gets the scroll bar thumb dark shadow color. */
  public String getThumbDarkShadowColor() {
    return thumbDarkShadowColor;
  }

  /** Gets the scroll bar thumb color. */
  public String getThumbColor() {
    return thumbColor;
  }

  /** Gets the scroll bar thumb insets left. */
  public int getThumbInsetsLeft() {
    return thumbInsetsLeft;
  }

  /** Gets the scroll bar thumb insets top. */
  public int getThumbInsetsTop() {
    return thumbInsetsTop;
  }

  /** Gets the scroll bar thumb insets right. */
  public int getThumbInsetsRight() {
    return thumbInsetsRight;
  }

  /** Gets the scroll bar thumb insets bottom. */
  public int getThumbInsetsBottom() {
    return thumbInsetsBottom;
  }

  /** Gets the scroll bar track color. */
  public String getTrackColor() {
    return trackColor;
  }

  /** Gets the scroll bar track insets left. */
  public int getTrackInsetsLeft() {
    return trackInsetsLeft;
  }

  /** Gets the scroll bar track insets top. */
  public int getTrackInsetsTop() {
    return trackInsetsTop;
  }

  /** Gets the scroll bar track insets right. */
  public int getTrackInsetsRight() {
    return trackInsetsRight;
  }

  /** Gets the scroll bar track insets bottom. */
  public int getTrackInsetsBottom() {
    return trackInsetsBottom;
  }

  /** Gets whether the scroll bar has track insets. */
  public int getHasTrackInsets() {
    return hasTrackInsets;
  }

  /** Gets the scroll bar button arrow color. */
  public String getButtonArrowColor() {
    return buttonArrowColor;
  }
}
