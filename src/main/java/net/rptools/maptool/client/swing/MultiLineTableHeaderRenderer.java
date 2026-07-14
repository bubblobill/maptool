package net.rptools.maptool.client.swing;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MultiLineTableHeaderRenderer implements TableCellRenderer {
    private final Color fg, bg;

    public MultiLineTableHeaderRenderer() {
        bg = UIManager.getDefaults().getColor("TableHeader.background");
        fg = UIManager.getDefaults().getColor("TableHeader.foreground");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new JPanel();
        panel.setBackground(bg);
        panel.setForeground(fg);
        LookAndFeel.installBorder(panel, "TableHeader.cellBorder");

        BoxLayout box = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(box);

        String[] heading = ((String)value).split(" ");
        for(String word: heading){
            JLabel label = new JLabel(word, null, SwingConstants.CENTER);
            label.setBackground(bg);
        label.setForeground(fg);
            label.setOpaque(false);
            label.setAlignmentX(0.5f);
            panel.add(label);
        }
        panel.invalidate();

        return panel;
    }
}