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
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.io.AbstractTemplateLoader;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.rptools.maptool.model.library.Library;
import net.rptools.maptool.model.library.LibraryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class to apply a Handlebars template given a bean.
 *
 * @param <T> The type of the bean to apply the template to.
 */
public class HandlebarsUtil<T> {
  private static final HighConcurrencyTemplateCache HIGH_CONCURRENCY_TEMPLATE_CACHE =
      new HighConcurrencyTemplateCache();

  /**
   * Use this to get an instance of Handlebars instead of creating one separately.
   *
   * <p>Specify a TemplateLoader if the default ClassPathTemplateLoader is not required.
   *
   * <p>Using this ensures the instance returned has a consistent setup with all helpers registered.
   *
   * @param loader The TemplateLoader to use. If null, handlebars defaults to
   *     ClassPathTemplateLoader.
   * @return A HandleBars instance with appropriate settings and registered helpers
   */
  static Handlebars getHandlebarsInstance(@Nullable TemplateLoader loader) {
    Handlebars handlebars =
        new Handlebars()
            .with(HIGH_CONCURRENCY_TEMPLATE_CACHE)
            .preEvaluatePartialBlocks(false)
            .parentScopeResolution(false)
            .setCharset(StandardCharsets.UTF_8);
    if (loader != null) {
      handlebars.with(loader);
    }
    return HandlebarsHelpers.registerHelpers(handlebars);
  }

  public static boolean isAssetFileHandlebars(String filename) {
    if (filename == null) {
      return false;
    }
    return filename.toLowerCase().endsWith(".hbs");
  }

  /** The compiled template. */
  private final Template template;

  /** Logging class instance. */
  private static final Logger log = LogManager.getLogger(HandlebarsUtil.class);

  /** Handlebars partial template source that uses Add-On files */
  private static record LibraryTemplateSource(@Nonnull Library library, @Nonnull String filename)
      implements TemplateSource {
    @Override
    public long lastModified() {
      // No modification time is available.
      return -1;
    }

    @Override
    @Nonnull
    public String content(@Nonnull final Charset charset) throws IOException {
      try {
        // The library API requires a URL even if it only uses the path.
        var url = new URI("lib", library.getNamespace().join(), filename, null).toURL();
        try (var is = library.read(url).join()) {
          return new String(is.readAllBytes(), charset);
        }
      } catch (URISyntaxException e) {
        throw new AssertionError("lib URL of namespace and filename should be valid", e);
      }
    }
  }

  /** Handlebars partial template loader that uses Add-On Library URIs */
  private static class LibraryTemplateLoader extends AbstractTemplateLoader {
    /** Path to template being resolved, relative paths are resolved relative to its parent. */
    @Nonnull final URI current;

    @Nonnull final Library library;

    private LibraryTemplateLoader(@Nonnull String current, @Nonnull Library library) {
      if (!current.startsWith("/")) {
        current = "/" + current;
      }
      this.current = URI.create(current);
      this.library = library;
      setPrefix(TemplateLoader.DEFAULT_PREFIX);
      setSuffix(TemplateLoader.DEFAULT_SUFFIX);
    }

    /** Resolve possibly relative uri to a new location relative to current rooted below prefix */
    @Override
    @Nonnull
    public String resolve(@Nonnull final String path) {
      var location = current.resolve(path).normalize().toString();
      if (location.startsWith("/")) {
        location = location.substring(1);
      }
      return getPrefix() + location + getSuffix();
    }

    @Override
    @Nonnull
    public LibraryTemplateSource sourceAt(@Nonnull final String location) {
      return new LibraryTemplateSource(library, resolve(location));
    }
  }

  /**
   * Creates a new instance of the utility class.
   *
   * @param stringTemplate The template to compile.
   * @param loader The template loader for loading included partial templates
   * @throws IOException If there is an error compiling the template.
   */
  private HandlebarsUtil(String stringTemplate, TemplateLoader loader) throws IOException {
    Handlebars handlebars = getHandlebarsInstance(loader);
    try {
      template = handlebars.compileInline(stringTemplate);
    } catch (IOException e) {
      log.error("Handlebars Error: {}", e.getMessage());
      throw e;
    }
  }

  /**
   * Creates a new instance of the utility class.
   *
   * @param stringTemplate The template to compile.
   * @param entry The lib:// URL of the template to load partial templates relative to
   * @throws IOException If there is an error compiling the template.
   */
  public HandlebarsUtil(String stringTemplate, URL entry) throws IOException {
    this(
        stringTemplate,
        new LibraryTemplateLoader(
            entry.getPath(),
            // Template is defined by AddOn so library should always be present.
            new LibraryManager().getLibrary(entry).join().orElseThrow()));
  }

  /**
   * Creates a new instance of the utility class.
   *
   * @param stringTemplate The template to compile.
   * @throws IOException If there is an error compiling the template.
   */
  public HandlebarsUtil(String stringTemplate) throws IOException {
    this(stringTemplate, new ClassPathTemplateLoader());
  }

  /**
   * Applies the template to the given bean.
   *
   * @param bean The bean to apply the template to.
   * @return The result of applying the template to the bean.
   * @throws IOException If there is an error applying the template.
   */
  public String apply(T bean) throws IOException {
    try {
      var context = Context.newBuilder(bean).resolver(JavaBeanValueResolver.INSTANCE).build();
      return template.apply(context);
    } catch (IOException e) {
      log.error("Handlebars Error: {}", e.getMessage());
      throw e;
    }
  }
}
