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

import java.awt.*;
import java.util.function.Function;
import javax.swing.*;

/**
 * A context for the range input. This is used to extract and provide the color values handlebars
 * for the themed css.
 */
public class SliderCssContext {

  /** The background color of the slider. */
  private final String backgroundColor;

  /** The thumb color of the slider. */
  private final String thumbColor;

  /** The thumb color of the slider with focus. */
  private final String focusColor;

  /** The track color of the slider. */
  private final String trackColor;

  /** The thumb color of the slider when disabled. */
  private final String disabledThumbColor;

  /** The track color of the slider when disabled. */
  private final String disabledTrackColor;

  /** The thumb color of the slider when hovered. */
  private final String hoverThumbColor;

  /** The border color of the slider when pressed. */
  private final String pressedThumbColor;

  /**
   * Creates a new instance of the slider css context.
   *
   * @param uiDef The UI defaults to use to extract the values.
   * @param formatColor The function to use to format the color.
   */
  public SliderCssContext(UIDefaults uiDef, Function<Color, String> formatColor) {
    backgroundColor = formatColor.apply(uiDef.getColor("Slider.background"));
    thumbColor = formatColor.apply(uiDef.getColor("Slider.thumbColor"));
    trackColor = formatColor.apply(uiDef.getColor("Slider.trackColor"));
    focusColor = formatColor.apply(uiDef.getColor("Slider.focus"));
    disabledThumbColor = formatColor.apply(uiDef.getColor("Slider.disabledThumbColor"));
    disabledTrackColor = formatColor.apply(uiDef.getColor("Slider.disabledTrackColor"));
    hoverThumbColor = formatColor.apply(uiDef.getColor("Slider.hoverThumbColor"));
    pressedThumbColor = formatColor.apply(uiDef.getColor("Slider.pressedThumbColor"));
  }

  /**
   * Returns the background color of the slider.
   *
   * @return The background color of the slider.
   */
  public String getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * Returns the thumb color of the slider.
   *
   * @return The thumb color of the slider.
   */
  public String getThumbColor() {
    return thumbColor;
  }

  /**
   * Returns the track color of the slider.
   *
   * @return The track color of the slider.
   */
  public String getTrackColor() {
    return trackColor;
  }

  /**
   * Returns the track color of the slider with focus.
   *
   * @return The track color of the slider with focus.
   */
  public String getFocusColor() {
    return focusColor;
  }

  /**
   * Returns the thumb color of the slider when disabled.
   *
   * @return The thumb color of the slider when disabled.
   */
  public String getDisabledThumbColor() {
    return disabledThumbColor;
  }

  /**
   * Returns the track color of the slider when disabled.
   *
   * @return The track color of the slider when disabled.
   */
  public String getDisabledTrackColor() {
    return disabledTrackColor;
  }

  /**
   * Returns the thumb color of the slider when hovered.
   *
   * @return The thumb color of the slider when hovered.
   */
  public String getHoverThumbColor() {
    return hoverThumbColor;
  }

  /**
   * Returns the thumb color of the slider when pressed.
   *
   * @return The thumb color of the slider when pressed.
   */
  public String getPressedThumbColor() {
    return pressedThumbColor;
  }
}
