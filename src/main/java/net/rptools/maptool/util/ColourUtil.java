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
package net.rptools.maptool.util;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;

public class ColourUtil {
  public static final double AAA_STANDARD_TEXT_CONTRAST = 7; // WCAG standard contrast ratio
  public static final double AA_STANDARD_TEXT_CONTRAST = 4.5; // WCAG standard contrast ratio
  public static final double MINIMUM_STANDARD_TEXT_CONTRAST = 3; // WCAG standard contrast ratio

  /**
   * <a
   * href="https://conceptviz.app/blog/scientific-color-palette-for-research-papers-and-posters">Okabe-Ito
   * Palette</a>
   */
  public static final List<Color> COLOURBLIND_FRIENDLY_PALETTE_LIGHT =
      List.of(
          new Color(0x000000),
          new Color(0xE69F00),
          new Color(0x56B4E9),
          new Color(0x009E73),
          new Color(0xF0E442),
          new Color(0x0072B2),
          new Color(0xD55E00),
          new Color(0xCC79A7));

  /**
   * <a
   * href="https://conceptviz.app/blog/scientific-color-palette-for-research-papers-and-posters">Okabe-Ito
   * Palette</a> with White substituted for Black
   */
  public static final List<Color> COLOURBLIND_FRIENDLY_PALETTE_DARK =
      COLOURBLIND_FRIENDLY_PALETTE_LIGHT.stream()
          .map(c -> c.equals(Color.BLACK) ? Color.WHITE : c)
          .toList();

  /**
   * <a
   * href="https://conceptviz.app/blog/scientific-color-palette-for-research-papers-and-posters">Okabe-Ito
   * Palette</a> contrast adjusted for text readability against White.
   */
  public static final List<Color> COLOURBLIND_FRIENDLY_TEXT_LIGHT_PALETTE =
      List.of(
          new Color(0x000000),
          new Color(0x996A00),
          new Color(0x3B7DA1),
          new Color(0x008662),
          new Color(0x7D7719),
          new Color(0x0072B2),
          new Color(0xBC5300),
          new Color(0xFE7DC4));

  /**
   * <a
   * href="https://conceptviz.app/blog/scientific-color-palette-for-research-papers-and-posters">Okabe-Ito
   * Palette</a> contrast adjusted for text readability against Black.
   */
  public static final List<Color> COLOURBLIND_FRIENDLY_TEXT_DARK_PALETTE =
      List.of(
          new Color(0xFFFFFF),
          new Color(0x2B6CFF),
          new Color(0xB36031),
          new Color(0xFF618C),
          new Color(0x666DD9),
          new Color(0xFF8D4D),
          new Color(0x2AA1FF),
          new Color(0x338658));

  /**
   * Calculates contrast between two colours according to the <a
   * href="https://www.w3.org/WAI/WCAG22/quickref/?showtechniques=143#contrast-minimum">WCAG 2.2
   * Standard</a>.
   *
   * <dl>
   *   <dt>Minimum Standard
   *   <dd>3:1
   *   <dt>AA Standard
   *   <dd>
   *       <ol>
   *         <li>Text < 18 point if not bold, <u><b>or</b></u> < 14 point if bold. <b>4.5:1</b>
   *         <li>Text >= 18 point if not bold, <u><b>or</b></u> >= 14 point if bold. <b>3:1</b>
   *       </ol>
   *   <dt>AAA Standard
   *   <dd>
   *       <ol>
   *         <li>Text < 18 point if not bold, <u><b>or</b></u> < 14 point if bold. <b>7:1</b>
   *         <li>Text >= 18 point if not bold, <u><b>or</b></u> >= 14 point if bold. <b>4.5:1</b>
   *       </ol>
   * </dl>
   *
   * @param color1 first comparison {@link Color}
   * @param color2 second comparison {@link Color}
   * @return the perceptual contrast ratio
   */
  public static double contrastRatio(Color color1, Color color2) {
    double luminance1 = relativeLuminance(color1);
    double luminance2 = relativeLuminance(color2);
    return luminance1 > luminance2
        ? (luminance1 + 0.05) / (luminance2 + 0.05)
        : (luminance2 + 0.05) / (luminance1 + 0.05);
  }

  /**
   * Comparing two colours, changes one colour trying to meet the standard
   *
   * @param referenceColour fixed {@link Color} we are trying to contrast against
   * @param adjustableColour {@link Color} to alter
   * @param standard Nullable. The target contrast standard to meet. Defaults to {@link
   *     #AAA_STANDARD_TEXT_CONTRAST}
   * @return a colour that meets the standard or the best of Black/White
   */
  public static Color findAcceptableContrast(
      final Color referenceColour, Color adjustableColour, @Nullable Double standard) {
    if (standard == null) {
      standard = AAA_STANDARD_TEXT_CONTRAST;
    }
    standard = Math.clamp(standard, 1, 21);
    if (contrastRatio(referenceColour, adjustableColour) >= standard) {
      return adjustableColour;
    }
    Color AAColour = null;
    Color minColour = null;
    Color tempColour;
    Color boundaryColor = contrast(adjustableColour); // the extremis end colour
    float[] directionComponents = boundaryColor.getComponents(null);
    float mean = (directionComponents[0] + directionComponents[1] + directionComponents[2]) / 3;
    float[] tempComponents = adjustableColour.getComponents(null);

    while (!Arrays.equals(directionComponents, tempComponents)) {
      for (int i = 0; i < 3; i++) {
        boolean increase = directionComponents[i] > tempComponents[i];
        float delta = Math.clamp(Math.abs((mean - tempComponents[i])) * 0.04f, 0.01f, 1f);
        tempComponents[i] = Math.clamp(tempComponents[i] + (increase ? delta : -delta), 0, 1);
      }
      tempColour = new Color(tempComponents[0], tempComponents[1], tempComponents[2]);
      double contrast = contrastRatio(referenceColour, tempColour);
      if (contrast >= standard) {
        return tempColour;
      } else if (contrast >= AA_STANDARD_TEXT_CONTRAST) {
        AAColour = tempColour;
      } else if (contrast >= MINIMUM_STANDARD_TEXT_CONTRAST) {
        minColour = tempColour;
      }
    }
    // fallback, return the highest standard met
    return Objects.requireNonNullElse(
        AAColour, Objects.requireNonNullElse(minColour, boundaryColor));
  }

  /**
   * Calculates the relative luminance of a colour according to the <a
   * href="https://www.w3.org/WAI/WCAG22/quickref/?showtechniques=143#contrast-minimum">WCAG 2.2
   * Standard</a>.
   *
   * @param color {@link Color} to perform calculation on.
   * @return the perceived luminance of the colour
   */
  public static double relativeLuminance(Color color) {
    float[] comp1 = new float[3];
    color.getColorComponents(comp1);
    // gamma correction
    for (int i = 0; i < 3; i++) {
      comp1[i] =
          (float)
              (comp1[i] <= 0.04045 ? comp1[i] / 12.92 : Math.pow((comp1[i] + 0.055) / 1.055, 2.4));
    }
    // calculate perceptual luminance
    return 0.2126 * comp1[0] + 0.7152 * comp1[1] + 0.0722 * comp1[2];
  }

  /**
   * For a given {@link Color}, determine whether black or white is best as a contrasting color and
   * return that color.
   *
   * @param c The color to contrast.
   * @return A black or white {@link Color}.
   * @see <a
   *     href="https://stackoverflow.com/questions/946544/good-text-foreground-color-for-a-given-background-color">https://stackoverflow.com/questions/946544/good-text-foreground-color-for-a-given-background-color</a>
   */
  public static Color contrast(Color c) {
    if (c == null) {
      return null;
    }
    return relativeLuminance(c) > 0.179 ? Color.BLACK : Color.WHITE;
  }
}
