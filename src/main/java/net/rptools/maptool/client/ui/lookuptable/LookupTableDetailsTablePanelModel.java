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
package net.rptools.maptool.client.ui.lookuptable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.LookupTable;

/** Define the table and columns for the LookupTable window details view */
public class LookupTableDetailsTablePanelModel extends AbstractTableModel {

  private List<LookupTable> tables = List.of();

  /** Defines the details view columns visible for GMs */
  private static final List<DetailsTableColumn> GM_COLUMNS =
      List.of(
          DetailsTableColumn.IMAGE,
          DetailsTableColumn.NAME,
          DetailsTableColumn.ROLL,
          DetailsTableColumn.COUNT_ENTRIES,
          DetailsTableColumn.COUNT_VALUES,
          DetailsTableColumn.COUNT_IMAGES,
          DetailsTableColumn.PLAYER_VISIBLE,
          DetailsTableColumn.ALLOW_LOOKUP,
          DetailsTableColumn.PICK_ONCE);

  /** Defines the details view columns visible for non-GMs */
  private static final List<DetailsTableColumn> PLAYER_COLUMNS =
      List.of(DetailsTableColumn.IMAGE, DetailsTableColumn.NAME);

  /**
   * Retries the set list of details view columns based on GM role or not
   *
   * @return the list of columns
   */
  List<DetailsTableColumn> getColumns() {
    return MapTool.getPlayer().isGM() ? GM_COLUMNS : PLAYER_COLUMNS;
  }

  /**
   * Defines the details table columns available for viewing {@link LookupTable} attributes in the
   * {@link LookupTableDetailsTablePanel}, specifying:
   *
   * <ul>
   *   <li>the column header label
   *   <li>the column header tooltip
   *   <li>the column class
   *   <li>the column alignment
   *   <li>the preferred column width
   */
  public enum DetailsTableColumn {
    IMAGE(
        I18N.getText("Label.image"),
        I18N.getText("Label.table.image"),
        LookupTable.class,
        SwingConstants.CENTER,
        60) {
      @Override
      Object getValue(LookupTable table) {
        return table;
      }
    },
    NAME(
        I18N.getText("Label.name"),
        I18N.getText("lookuptable.description"),
        String.class,
        SwingConstants.LEFT,
        300) {
      @Override
      Object getValue(LookupTable table) {
        return table.getName();
      }
    },
    ROLL(
        I18N.getText("Label.roll"),
        I18N.getText("lookuptable.description"),
        String.class,
        SwingConstants.CENTER,
        80) {
      @Override
      Object getValue(LookupTable table) {
        return table.getRoll();
      }
    },
    COUNT_ENTRIES(
        I18N.getText("LookupTablePanel.countEntries"),
        I18N.getText("LookupTablePanel.countEntries.tooltip"),
        Integer.class,
        SwingConstants.CENTER,
        80) {
      @Override
      Object getValue(LookupTable table) {
        return table.getEntryCount();
      }
    },
    COUNT_VALUES(
        I18N.getText("LookupTablePanel.countValues"),
        I18N.getText("LookupTablePanel.countValues.tooltip"),
        Integer.class,
        SwingConstants.CENTER,
        80) {
      @Override
      Object getValue(LookupTable table) {
        return table.getEntryValueCount();
      }
    },
    COUNT_IMAGES(
        I18N.getText("LookupTablePanel.countImages"),
        I18N.getText("LookupTablePanel.countImages.tooltip"),
        Integer.class,
        SwingConstants.CENTER,
        80) {
      @Override
      Object getValue(LookupTable table) {
        return table.getEntryImageCount();
      }
    },
    PLAYER_VISIBLE(
        I18N.getText("EditLookupTablePanel.showplayer"),
        I18N.getText("EditLookupTablePanel.tooltip.visible"),
        Boolean.class,
        SwingConstants.CENTER,
        40) {
      @Override
      Object getValue(LookupTable table) {
        return table.getVisible();
      }
    },
    ALLOW_LOOKUP(
        I18N.getText("EditLookupTablePanel.lookup"),
        I18N.getText("EditLookupTablePanel.tooltip.allowLookup"),
        Boolean.class,
        SwingConstants.CENTER,
        40) {
      @Override
      Object getValue(LookupTable table) {
        return table.getAllowLookup();
      }
    },
    PICK_ONCE(
        I18N.getText("EditLookupTablePanel.pickOnce"),
        I18N.getText("EditLookupTablePanel.tooltip.pickOnce"),
        Boolean.class,
        SwingConstants.CENTER,
        40) {
      @Override
      Object getValue(LookupTable table) {
        return table.getPickOnce();
      }
    };

    private final String title;
    private final String tooltip;
    private final Class<?> columnClass;
    private final int alignment;
    private final int preferredWidth;

    DetailsTableColumn(
        String title, String tooltip, Class<?> columnClass, int alignment, int preferredWidth) {
      this.title = title;
      this.tooltip = tooltip;
      this.columnClass = columnClass;
      this.alignment = alignment;
      this.preferredWidth = preferredWidth;
    }

    public String getTitle() {
      return title;
    }

    public String getTooltip() {
      return tooltip;
    }

    public Class<?> getColumnClass() {
      return columnClass;
    }

    public int getAlignment() {
      return alignment;
    }

    public int getPreferredWidth() {
      return preferredWidth;
    }

    abstract Object getValue(LookupTable table);
  }

  @Override
  public Class<?> getColumnClass(int column) {
    return getColumns().get(column).getColumnClass();
  }

  @Override
  public int getRowCount() {
    return tables.size();
  }

  @Override
  public int getColumnCount() {
    return getColumns().size();
  }

  @Override
  public Object getValueAt(int row, int column) {

    if (row < 0 || row >= tables.size()) {
      return null;
    }

    return getColumns().get(column).getValue(tables.get(row));
  }

  @Override
  public String getColumnName(int column) {
    return getColumns().get(column).getTitle();
  }

  /**
   * Retrieves the {@code LookupTable} at a specific row
   *
   * @param row the row in the details view table
   * @return the found {@code LookupTable}
   */
  public LookupTable getLookupTableAt(int row) {
    return tables.get(row);
  }

  /**
   * Refresh the data in the details view.
   *
   * <p>Use when a table property changes:
   *
   * <ul>
   *   <li>visible
   *   <li>allowLookup
   *   <li>pickOnce
   *   <li>roll
   *   <li>image
   *   <li>entries
   */
  public void refreshData() {
    tables = buildTables();
    fireTableDataChanged();
  }

  /**
   * Refresh the structure of the details view.
   *
   * <p>Use when the columns may change:
   *
   * <ul>
   *   <li>campaign loaded
   *   <li>GM/player status changes
   */
  public void refreshStructure() {
    tables = buildTables();
    fireTableStructureChanged();
  }

  /** Reset the data in the details view */
  public void reset() {
    tables = List.of(); // an empty list
    fireTableStructureChanged();
  }

  /**
   * Snapshot of authoritative data for table structure refreshes so we do not get index out ranges
   * (e.g. during a campaign load) if the number of {@code LookupTable}s needed to be displayed
   * changes)
   *
   * @return a list of {@code LookupTable}s
   */
  private List<LookupTable> buildTables() {
    Map<String, LookupTable> lookupTables = MapTool.getCampaign().getLookupTableMap();

    List<LookupTable> result = new ArrayList<>();

    if (MapTool.getPlayer().isGM()) {
      result.addAll(lookupTables.values());
    } else {
      lookupTables.values().stream().filter(LookupTable::getVisible).forEach(result::add);
    }

    result.sort(Comparator.comparing(LookupTable::getName));

    return result;
  }
}
