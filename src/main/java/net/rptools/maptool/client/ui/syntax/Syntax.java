package net.rptools.maptool.client.ui.syntax;

import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.DisplayNames;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import java.util.Set;

/**
 * Collection of text document types, their UI names, their {@link org.fife.ui.rsyntaxtextarea.RSyntaxDocument} type, and if validation is supported.
 */
public enum Syntax implements DisplayNames {
    NONE(Set.of("NONE"), SyntaxConstants.SYNTAX_STYLE_NONE, false),
    CSS(Set.of("CSS"), SyntaxConstants.SYNTAX_STYLE_CSS, false),
    CSV(Set.of("CSV"), SyntaxConstants.SYNTAX_STYLE_CSV, true),
    JAVASCRIPT(Set.of("JAVASCRIPT", "JS"), SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT, true),
    JSON(Set.of("JSON"), SyntaxConstants.SYNTAX_STYLE_JSON, true),
    HANDLEBARS(Set.of("HANDLEBARS", "HBS"), SyntaxConstants.SYNTAX_STYLE_HANDLEBARS, false),
    HTML(Set.of("HTML"), SyntaxConstants.SYNTAX_STYLE_HTML, true),
    MAPTOOL_SCRIPT(Set.of("MAPTOOLSCRIPT", "MTSCRIPT", "MTS"), "text/MapToolScript", false),
    MARKDOWN(Set.of("MARKDOWN", "MD"), SyntaxConstants.SYNTAX_STYLE_MARKDOWN, false),
    PROPERTIES(Set.of("PROPERTIES"), SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE, false),
    XML(Set.of("XML"), SyntaxConstants.SYNTAX_STYLE_XML, true);

    private final Set<String> aliases;
    private final String displayName;
    private final String shortName;
    private final String rSyntaxStyle;
    private final boolean supportsValidation;

    Syntax(Set<String> aliases, String rSyntaxStyle, boolean supportsValidation) {
        this.displayName = I18N.getText(rSyntaxStyle);
        this.shortName = I18N.getText(rSyntaxStyle + ".short");
        this.aliases = aliases;
        this.rSyntaxStyle = rSyntaxStyle;
        this.supportsValidation = supportsValidation;
    }

    public static Set<Syntax> getTokenNoteTypes() {
        return Set.of(NONE, HTML, MARKDOWN);
    }

    public String rSyntaxStyle() {
        return rSyntaxStyle;
    }

    public boolean supportsValidation() {
        return supportsValidation;
    }

    /**
     * Find Syntax from name, alias, etc.
     * @param lookup search string
     * @return Syntax
     */
    public static Syntax lookup(String lookup) {
        lookup = lookup.toUpperCase();
        for (Syntax syntax : values()) {
            if (syntax.aliases.contains(lookup)
                    || lookup.equalsIgnoreCase(syntax.name())
                    || lookup.equalsIgnoreCase(syntax.getShortName())
                    || lookup.equalsIgnoreCase(syntax.getShortName())
                    || lookup.equalsIgnoreCase(syntax.rSyntaxStyle())
            ) {
                return syntax;
            }
        }
        return NONE;
    }


//    /**
//     * @param text
//     * @return
//     */
//    public ValidationResult validate(String text) {
//        return switch (this) {
//            case CSV -> validateCsv(text);
//            case HTML -> validateHtml(text);
//            case JAVASCRIPT -> validateJavascript(text);
//            case JSON -> validateJson(text);
//            case XML -> validateXml(text);
//            default -> null;
//        };
//    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return !hasDisplayName() ? name() : displayName;
    }

    @Override
    public String getShortName() {
        return !hasShortName() ? getDisplayName() : shortName;
    }

    @Override
    public boolean hasShortName() {
        return shortName != null && !shortName.isBlank();
    }

    @Override
    public boolean hasDisplayName() {
        return displayName != null && !displayName.isBlank();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
