package ssu.csit.gui;
import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TableFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;

    public TableFrame() {
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Path");
        columnNames.add("State");

        table = new JTable();
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        table.setModel(model);
        table.setEnabled(false);
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public TableFrame addRow(String ... rowData) {
        model.addRow(rowData);
        return this;
    }

}
