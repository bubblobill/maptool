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

import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.helper.LogHelper;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.github.jknack.handlebars.helper.ext.AssignHelper;
import com.github.jknack.handlebars.helper.ext.IncludeHelper;
import com.github.jknack.handlebars.helper.ext.NumberHelper;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlebarsHelpers {
  static Handlebars registerHelpers(Handlebars handlebars) {
    StringHelpers.register(handlebars);
    Arrays.stream(ConditionalHelpers.values()).forEach(h -> handlebars.registerHelper(h.name(), h));
    handlebars.registerHelper("json", Jackson2Helper.INSTANCE);
    NumberHelper.register(handlebars);
    handlebars.registerHelper(AssignHelper.NAME, AssignHelper.INSTANCE);
    handlebars.registerHelper(IncludeHelper.NAME, IncludeHelper.INSTANCE);
    handlebars.registerHelper(MarkdownHelper.NAME, MarkdownHelper.INSTANCE);
    handlebars.registerHelper(Base64EncodeHelper.NAME, Base64EncodeHelper.INSTANCE);
    Arrays.stream(HandlebarsHelpers.MathsHelpers.values())
        .forEach(h -> handlebars.registerHelper(h.name(), h));
    return handlebars;
  }

  static class Base64EncodeHelper implements Helper<Object> {
    /** A singleton instance of this helper. */
    public static final Helper<Object> INSTANCE = new Base64EncodeHelper();

    /** The helper's name. */
    public static final String NAME = "base64Encode";

    /**
     * Turns the textual form of the value into a base64-encoded string. For example:
     *
     * <pre>
     * &lt;script type="application/json;base64" id="jsonProperty"&gt;
     *   {{ base64Encode properties[0].value }}
     * &lt;/script&gt;
     * &lt;script type="application/javascript"&gt;
     * const jsonProperty = JSON.parse(atob(document.getElementById("jsonProperty").innerText));
     * &lt;/script&gt;
     * </pre>
     */
    @Override
    public Object apply(final Object context, final Options options) {
      if(context == null || context instanceof String s && s.isBlank()){
        return "";
      } else {
        byte[] message = context.toString().getBytes(StandardCharsets.UTF_8);
        return new Handlebars.SafeString(Base64.getUrlEncoder().encodeToString(message));
      }
    }
  }

  public enum MathsHelpers implements Helper<Object> {
    add {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          List<BigDecimal> numbers = numbers(a, options);
          BigDecimal result = numbers.removeLast();
          while (!numbers.isEmpty()) {
            result = result.add(numbers.removeLast());
          }
          return String.valueOf(result);
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },
    subtract {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          List<BigDecimal> numbers = numbers(a, options);
          BigDecimal result = numbers.removeLast();
          while (!numbers.isEmpty()) {
            result = result.subtract(numbers.removeLast());
          }
          return String.valueOf(result);
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },
    multiply {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          List<BigDecimal> numbers = numbers(a, options).reversed();
          BigDecimal result = numbers.removeLast();
          while (!numbers.isEmpty()) {
            result = result.multiply(numbers.removeLast());
          }
          return String.valueOf(result);
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },
    divide {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          List<BigDecimal> numbers = numbers(a, options).reversed();
          BigDecimal result = numbers.removeLast();
          while (!numbers.isEmpty()) {
            result = result.divide(numbers.removeLast(), RoundingMode.HALF_EVEN);
          }
          return String.valueOf(result);
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },
    max {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          List<BigDecimal> numbers = numbers(a, options);
          BigDecimal result = numbers.removeLast();
          while (!numbers.isEmpty()) {
            result = result.max(numbers.removeLast());
          }
          return String.valueOf(result);
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },
    min {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          List<BigDecimal> numbers = numbers(a, options);
          BigDecimal result = numbers.removeLast();
          while (!numbers.isEmpty()) {
            result = result.min(numbers.removeLast());
          }
          return String.valueOf(result);
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },
    mod {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          List<BigDecimal> numbers = numbers(a, options);
          BigDecimal result = numbers.removeLast();
          while (!numbers.isEmpty()) {
            result = result.remainder(numbers.removeLast());
          }
          return String.valueOf(result);
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },
    div {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          List<BigDecimal> numbers = numbers(a, options);
          BigDecimal result = numbers.removeLast();
          while (!numbers.isEmpty()) {
            result = result.divideToIntegralValue(numbers.removeLast());
          }
          return String.valueOf(result);
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },
    pow {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          List<BigDecimal> numbers = numbers(a, options);
          BigDecimal result = numbers.removeLast();
          while (!numbers.isEmpty()) {
            result = result.pow(numbers.removeLast().intValue());
          }
          return String.valueOf(result);
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },
    abs {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          return new BigDecimal(a.toString()).abs().toString();
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },
    sqrt {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        try {
          return new BigDecimal(a.toString()).sqrt(MathContext.DECIMAL32).toString();
        } catch (Exception ignored) {
          return "NaN";
        }
      }
    },

    tau {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        return String.valueOf(Math.TAU);
      }
    },
    pi {
      @Override
      public Object apply(final Object a, final Options options) throws IOException {
        return String.valueOf(Math.PI);
      }
    },
    ;

    List<BigDecimal> numbers(Object a, final Options options) {
      int count = options.params.length + 1;
      List<BigDecimal> values = new ArrayList<>();
      try {
        values.add(new BigDecimal(a.toString()));
        for (int i = 0; i < count; i++) {
          values.add(new BigDecimal((double) options.param(i)));
        }

      } catch (NumberFormatException ignored) {
      }
      return values;
    }
  }

  public static class HBLogger extends LogHelper {
    private static final Logger log = LoggerFactory.getLogger(Handlebars.class);

    /** A singleton instance of this helper. */
    public static final Helper<Object> INSTANCE = new HBLogger();

    /** The helper's name. */
    public static final String NAME = "log";

    @Override
    public Object apply(Object context, Options options) throws IOException {
      StringBuilder sb = new StringBuilder();
      String level = options.hash("level", "info");
      TagType tagType = options.tagType;
      if (tagType.inline()) {
        sb.append(context);
        for (int i = 0; i < options.params.length; i++) {
          sb.append(" ").append((Object) options.param(i));
        }
      } else {
        sb.append(options.fn());
      }
      System.out.println("Handlebars(" + level + "): " + sb.toString().trim());
      switch (level) {
        case "error":
          log.error(sb.toString().trim());
          break;
        case "debug":
          log.debug(sb.toString().trim());
          break;
        case "warn":
          log.warn(sb.toString().trim());
          break;
        case "trace":
          log.trace(sb.toString().trim());
          break;
        default:
          log.info(sb.toString().trim());
      }
      return null;
    }
  }

  public static class MarkdownHelper implements Helper<Object> {
    private static final MutableDataSet MD_OPTIONS = new MutableDataSet();
    private static final Parser PARSER = Parser.builder(MD_OPTIONS).build();
    private static final HtmlRenderer HTML_RENDERER = HtmlRenderer.builder(MD_OPTIONS).build();

    /** A singleton version of {@link MarkdownHelper}. */
    public static final Helper<Object> INSTANCE = new MarkdownHelper();

    /** The helper's name. */
    public static final String NAME = "markdown";

    @Override
    public Object apply(final Object context, final Options options) throws IOException {
      if (options.isFalsy(context)) {
        return "";
      }
      String markdown = context.toString();
      Node document = PARSER.parse(markdown);
      return new Handlebars.SafeString(HTML_RENDERER.render(document));
    }
  }
}
