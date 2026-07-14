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
package net.rptools.maptool.client.ui.token.dialog.edit;

import com.jidesoft.combobox.MultilineStringExComboBox;
import com.jidesoft.combobox.PopupPanel;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.basic.BasicExComboBoxUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.TokenProperty;
import net.rptools.maptool.model.VariableType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import static com.jidesoft.converter.ConverterContext.DEFAULT_CONTEXT;

public class TokenPropertiesEditorPanel extends PropertyPane {
    private static final Logger log = LogManager.getLogger();

    private final EditTokenDialog editTokenDialog;
    private final WordWrapCellRenderer wordWrapCellRenderer = new WordWrapCellRenderer();
    private final NumberCellRenderer numberCellRenderer = new NumberCellRenderer();
    private final NumberCellEditor<?> numberCellEditor = new NumberCellEditor<>();
    private Token token;
    private PropertyTableSearchable searchable;

    public TokenPropertiesEditorPanel(EditTokenDialog editTokenDialog) {
        super(new PropertyTable() {
            @Override
            public String getToolTipText(MouseEvent event) {
                String text = super.getToolTipText(event);
                return text != null && text.length() > 100 ? text.substring(0, 100) + " ..." : text;
            }
        }, 1);

        this.editTokenDialog = editTokenDialog;

        setShowDescription(false);

        searchable = new PropertyTableSearchable(getPropertyTable());

        getPropertyTable().setModel(new TokenPropertyTableModel());
        getPropertyTable().setFillsViewportHeight(true);
        getPropertyTable().setName("propertiesTable");


        /* wrap button and functionality */
        JPanel buttonsAndPropertyTable = new JPanel();
        buttonsAndPropertyTable.setLayout(new BorderLayout());
        JCheckBox wrapToggle = new JCheckBox(I18N.getString("EditTokenDialog.msg.wrap"));
        wrapToggle.addActionListener(
                e -> {
                    wordWrapCellRenderer.setLineWrap(wrapToggle.isSelected());
                    getPropertyTable().repaint();
                });
        buttonsAndPropertyTable.add(wrapToggle, BorderLayout.PAGE_END);


        buttonsAndPropertyTable.add(this, BorderLayout.CENTER);
        setMinimumSize(new Dimension(300, 200));
        setPreferredSize(new Dimension(-1, -1));
        setVisible(true);
    }

    public void reset(Token token) {
    }


    /**
     * Updates the property table.
     *
     * @param propertyType the property type of the token (unused).
     */
    protected void updatePropertiesTable(@Nullable Token token, final String propertyType) {
        EventQueue.invokeLater(
                () -> {
                    PropertyTable pp = getPropertyTable();
                    List<TokenProperty> propertyTypeProperties = MapTool.getCampaign().getTokenPropertyList(propertyType);
                    pp.setModel(
                            new TokenPropertyTableModel(token, propertyType, propertyTypeProperties, wordWrapCellRenderer, numberCellEditor, numberCellRenderer));
                    pp.expandAll();
                });
    }

    protected static class TokenPropertyTableModel
            extends PropertyTableModel<TokenPropertyTableModel.TableTokenProperty>
            implements NavigableModel {
        final String gm = String.format(" (%s)", I18N.getText("permissionsScope.displayName.gm"));
        private final java.util.List<TokenProperty> propertyList;
        private final Map<String, String> propertyMap;

        public TokenPropertyTableModel() {
            propertyList = java.util.List.of();
            propertyMap = Map.of();
        }

        public TokenPropertyTableModel(
                @Nullable Token model,
                String propertyTypeName,
                List<TokenProperty> propertyList,
                WordWrapCellRenderer wordWrapCellRenderer,
                NumberCellEditor<?> numberCellEditor,
                NumberCellRenderer numberCellRenderer) {
            this.propertyList = propertyList;

            Set<String> typePropertyNames = propertyList.stream().map(TokenProperty::getName).collect(Collectors.toSet());
            Set<String> otherPropertyNames = Set.of();
            if (model != null) {
                otherPropertyNames = model.getPropertyNamesRaw();
                otherPropertyNames = otherPropertyNames.stream().filter(name -> !typePropertyNames.stream().toList().contains(name)).collect(Collectors.toSet());
            }

            this.propertyMap = new HashMap<>();

            ArrayList<TableTokenProperty> gridProperties = new ArrayList<>();
            for(TokenProperty tp : propertyList){
                String value = null;
                if(model != null){
                    value = (String) model.getProperty(tp.getName());
                }
                this.propertyMap.put(tp.getName(), value == null && tp.hasDefaultValue() ? tp.getDefaultValue() : value);

                TableTokenProperty gridProperty = new TableTokenProperty(tp, propertyTypeName);
                if(tp.getVariableType().equals(VariableType.NUMBER)){
                    gridProperty.setTableCellRenderer(numberCellRenderer);
                    gridProperty.setCellEditor(numberCellEditor);
                } else {
                    gridProperty.setTableCellRenderer(wordWrapCellRenderer);
                    gridProperty.setCellEditor(new MTMultilineStringCellEditor());
                }
                gridProperties.add(gridProperty);
            }
            for (String propName : otherPropertyNames) {
                this.propertyMap.put(propName, (String) model.getProperty(propName));

                TableTokenProperty gridProperty = new TableTokenProperty(propName, propertyTypeName, gm);
                gridProperty.setTableCellRenderer(wordWrapCellRenderer);
                gridProperty.setCellEditor(new MTMultilineStringCellEditor());
                gridProperties.add(gridProperty);
            }

            setOriginalProperties(gridProperties);
        }

        public void applyTo(Token token) {
            for (TokenProperty property : propertyList) {
                String value = propertyMap.get(property.getName());
                if (property.getDefaultValue() != null && property.getDefaultValue().equals(value)) {
                    token.setProperty(property.getName(), null); // Clear original value
                    continue;
                }
                token.setProperty(property.getName(), value);
            }
        }

        @Override
        public boolean isNavigableAt(int rowIndex, int columnIndex) {
            /* make the property name column non-navigable so that tab takes you directly to the next property value cell. */
            return (columnIndex != 0);
        }

        @Override
        public boolean isNavigationOn() {
            return true;
        }

        class TableTokenProperty extends Property {
            public TableTokenProperty(TokenProperty tokenProperty, String propertyType) {
                this(tokenProperty.getName(),
                        tokenProperty.getDisplayName(),
                        tokenProperty.getVariableType() == null ? VariableType.UNDEFINED.getClass() : tokenProperty.getVariableType().getKlass(),
                        tokenProperty.isPlayerEditable() ? propertyType : propertyType + gm,
                        DEFAULT_CONTEXT,
                        null);
            }

            public TableTokenProperty(String propertyName, String propertyType, String category) {
                this(propertyName, propertyName, String.class, propertyType, DEFAULT_CONTEXT, null);
            }

            public TableTokenProperty(String name, String displayName, Class<?> klass, String category, ConverterContext converterContext, List<Property> children) {
                super(name, "", klass, category, converterContext, children);
                setDisplayName(displayName);
            }

            @Override
            public Object getValue() {
                return propertyMap.get(getName());
            }

            @Override
            public void setValue(Object value) {
                propertyMap.put(getName(), (String) value);
            }

            @Override
            public boolean hasValue() {
                return propertyMap.get(getName()) != null;
            }
        }
    }

    /* needed to change the popup for properties */
    private static class MTMultilineStringExComboBox extends MultilineStringExComboBox {

        final ResourceBundle a = ResourceBundle.getBundle("com.jidesoft.combobox.combobox");

        public ResourceBundle getResourceBundle(Locale paramLocale) {
            return ResourceBundle.getBundle("com.jidesoft.combobox.combobox", paramLocale);
        }

        public PopupPanel createPopupComponent() {
            MTMultilineStringPopupPanel pp =
                    new MTMultilineStringPopupPanel(
                            getResourceBundle(Locale.getDefault()).getString("ComboBox.multilineStringTitle"));
            return pp;
        }
    }

    /* the cell editor for property popups */
    private static class MTMultilineStringCellEditor extends MultilineStringCellEditor {

        protected MTMultilineStringExComboBox createMultilineStringComboBox() {
            MTMultilineStringExComboBox localMultilineStringExComboBox =
                    new MTMultilineStringExComboBox();
            localMultilineStringExComboBox.setEditable(true);
            localMultilineStringExComboBox.setUI(new BasicExComboBoxUI());
            return localMultilineStringExComboBox;
        }
    }

    /* cell renderer for properties table */
    protected static class WordWrapCellRenderer extends RSyntaxTextArea implements TableCellRenderer {

        WordWrapCellRenderer() {
            setLineWrap(false);
            setWrapStyleWord(true);
//
//            /* Set the color style via Theme */
//            try {
//                File themeFile =
//                        new File(
//                                AppConstants.THEMES_DIR, AppPreferences.defaultMacroEditorTheme.get() + ".xml");
//                Theme theme = Theme.load(new FileInputStream(themeFile));
//                theme.apply(this);
//
//                revalidate();
//            } catch (IOException e) {
//                log.error("Error while loading theme", e);
//            }
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value == null) {
                value = "";
            }
            setText(value.toString());
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
            if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, getPreferredSize().height);
            }
            return this;
        }
    }

    /* the property popup table */
    private static class MTMultilineStringPopupPanel extends PopupPanel {

        private RSyntaxTextArea j = createTextArea();

        public MTMultilineStringPopupPanel() {
            this("");
        }

        public MTMultilineStringPopupPanel(String paramString) {
            this.setResizable(true);
//            /* Set the color style via Theme */
//            try {
//                File themeFile =
//                        new File(
//                                AppConstants.THEMES_DIR, AppPreferences.defaultMacroEditorTheme.get() + ".xml");
//                Theme theme = Theme.load(new FileInputStream(themeFile));
//                theme.apply(j);
//
//                j.revalidate();
//            } catch (IOException e) {
//                log.error("Error while loading multiline property editor theme", e);
//            }
            JScrollPane localJScrollPane = new RTextScrollPane(j);
            localJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            localJScrollPane.setAutoscrolls(true);
            localJScrollPane.setPreferredSize(new Dimension(300, 200));
            setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
            setLayout(new BorderLayout());
            setTitle(paramString);
            add(localJScrollPane, "Center");
            setDefaultFocusComponent(j);
            j.setLineWrap(false);
            JCheckBox wrapToggle = new JCheckBox(I18N.getString("EditTokenDialog.msg.wrap"));
            wrapToggle.addActionListener(e -> j.setLineWrap(!j.getLineWrap()));

            DefaultComboBoxModel syntaxListModel = new DefaultComboBoxModel();
            syntaxListModel.addElement(SyntaxConstants.SYNTAX_STYLE_NONE);
            syntaxListModel.addElement(SyntaxConstants.SYNTAX_STYLE_JSON);
            syntaxListModel.addElement(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
            syntaxListModel.addElement(SyntaxConstants.SYNTAX_STYLE_HTML);
            syntaxListModel.addElement(SyntaxConstants.SYNTAX_STYLE_XML);
            JComboBox syntaxComboBox = new JComboBox(syntaxListModel);
            syntaxComboBox.addActionListener(
                    e -> j.setSyntaxEditingStyle(syntaxComboBox.getSelectedItem().toString()));

            add(syntaxComboBox, BorderLayout.BEFORE_FIRST_LINE);
            add(wrapToggle, BorderLayout.AFTER_LAST_LINE);
        }

        public Object getSelectedObject() {
            return j.getText();
        }

        public void setSelectedObject(Object paramObject) {
            if (paramObject != null) {
                j.setText(paramObject.toString());
            } else {
                j.setText("");
            }
        }

        protected RSyntaxTextArea createTextArea() {
            RSyntaxTextArea textArea = new RSyntaxTextArea();
            textArea.setUseFocusableTips(false);
            textArea.setAnimateBracketMatching(true);
            textArea.setBracketMatchingEnabled(true);
            textArea.setLineWrap(false);
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
            return textArea;
        }
    }
}
