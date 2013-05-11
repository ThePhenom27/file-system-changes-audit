package ssu.csit;

import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.DIRECTORIES_ONLY;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import ssu.csit.fileservice.HashDirectory;
import ssu.csit.gui.TableFrame;

public class Application extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField pathField;
    private JLabel lblEnterPathTo;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    Application dialog = new Application();
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the dialog.
     */
    public Application() {
        setBounds(100, 100, 388, 167);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        {
            lblEnterPathTo = new JLabel("Enter path to the directory you want to get changes for:");
        }
        {
            pathField = new JTextField();
            pathField.setColumns(10);
        }

        JButton openBtn = new JButton("Browse...");
        openBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(DIRECTORIES_ONLY);
                int chooseState = fileChooser.showOpenDialog(Application.this);

                if (chooseState == APPROVE_OPTION) {
                    pathField.setText(fileChooser.getSelectedFile().getPath());
                }
            }

        });
        GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
        gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
                gl_contentPanel
                        .createSequentialGroup()
                        .addGap(21)
                        .addGroup(
                                gl_contentPanel
                                        .createParallelGroup(Alignment.LEADING)
                                        .addGroup(
                                                gl_contentPanel
                                                        .createSequentialGroup()
                                                        .addComponent(pathField, GroupLayout.PREFERRED_SIZE, 203,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(ComponentPlacement.RELATED)
                                                        .addComponent(openBtn)).addComponent(lblEnterPathTo))
                        .addContainerGap(53, Short.MAX_VALUE)));
        gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
                gl_contentPanel
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblEnterPathTo, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(
                                gl_contentPanel
                                        .createParallelGroup(Alignment.BASELINE)
                                        .addComponent(pathField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE).addComponent(openBtn)).addGap(156)));
        contentPanel.setLayout(gl_contentPanel);
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String path = pathField.getText();
                if (path.equals("")) {
                    JOptionPane.showMessageDialog(Application.this, "Please enter a path.", "",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                File file = new File(path);
                TableFrame frame;
                try {
                    frame = new TableFrame(new HashDirectory(file).getFileSystemChanges());
                    frame.setVisible(true);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(Application.this, String.format("No such directory: '%s'", file),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }

        });
        buttonPane.add(cancelButton);

    }
}
