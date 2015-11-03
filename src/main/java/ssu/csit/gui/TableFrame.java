package ssu.csit.gui;
import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class TableFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;

    /**
     * Create the frame.
     * 
     * @param data
     */
    public TableFrame(Vector<Vector<String>> data) {
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Path");
        columnNames.add("State");

        table = new JTable(data, columnNames);
        table.setEnabled(false);
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
    }

}
