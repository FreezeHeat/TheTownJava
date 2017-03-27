package GUI;

import Code.User;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import resources.LocalizationUtil;
import static resources.LocalizationUtil.localizedResourceBundle;
import static Code.ClientConnection.client;
import Code.Commands;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the screen of <code>Lobby</code>, this is where the
 * player gets to after login. in here a couple of options:
 * <ul>
 * <li> Join the game </li>
 * <li> See player's statistics </li>
 * <li> Top10 list </li>
 * </ul>
 *@author Ben Gilad and Asaf Yeshayahu
 *@version %I%
 *@see GUI.FormGame
 *@since 1.0
 */
public class FormLobby extends javax.swing.JFrame {

    User user;
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

    /**
     * Creates new form formLobby
     */
    public FormLobby() {
    }

    /**
     * Creates a new <code>formLobby</code> that has information in it
     * @see Code.User
     * @param user The user that logged in
     */
    public FormLobby(User user) {
        initComponents();
        updateCaptions();
        this.setLocationRelativeTo(null); // center window
        this.user = user;
        Color c = new Color(255,204,204);
        Container con = this.getContentPane();
        con.setBackground(c);
        this.setTableValues(); // set the user's values inside the table
        this.labWelcome.setText(localizedResourceBundle.getString(
                "formLobby.formLobby.Wellcome") + " " + user.getUsername());
        this.pack();
        this.setVisible(true);
        this.addWindowListener(new myWindowListener());
    }
        /**
     * This method changes the <i>localization</i> and updates the 
     * text to different <i>language</i>
     */
    public void updateCaptions() {
        LocalizationUtil.localizedResourceBundle = ResourceBundle.getBundle("resources.Bundle");

        labWelcome.setText(localizedResourceBundle.getString("FormLobby.labWelcome"));
        btnLogout.setText(localizedResourceBundle.getString("FormLobby.btnLogout"));
        btnExit.setText(localizedResourceBundle.getString("FormLobby.btnExit"));
        btnJoin.setText(localizedResourceBundle.getString("FormLobby.btnJoin"));
        btnOptions.setText(localizedResourceBundle.getString("FormLobby.btnOptions"));
        this.setTitle(localizedResourceBundle.getString("FormLobby.title"));
        tabStats.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null}
                },
                new String[]{
                    localizedResourceBundle.getString("formLobby.statWonLost"),
                    localizedResourceBundle.getString("fomrLobby.statKills"),
                    localizedResourceBundle.getString("formLobby.statHeals")
                }));

    }
        /**
     * <i>Action</i> when clicked <b>button</b> before exiting game
     *  
     */
    public class myWindowListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent we) {
            // ask the user a question and get his input

            int result = JOptionPane.showConfirmDialog(
                    null, //parent
                    localizedResourceBundle.getString("allForms.exitDialog"), //text
                    localizedResourceBundle.getString("allForms.titleExitDialog"), //title
                    JOptionPane.YES_NO_OPTION, //display options
                    JOptionPane.QUESTION_MESSAGE); //icon(default questionmark)
            //null; usually for an image icon
            if (result == JOptionPane.YES_OPTION) {
                client.exit();
                dispose();
            }
        }
    }

    /**
     * Sets the <b>Table/Grid</b> values to the 
     * default with the <b>user's stats</b>
     */
    private void setTableValues() {

        DefaultTableModel model = (DefaultTableModel) this.tabStats.getModel();

        model.setValueAt(
                user.getWon() + " / " + user.getLost(), // text
                0, // row
                0); //column

        model.setValueAt(
                user.getKills(),
                0,
                1);

        model.setValueAt(
                user.getHeales(),
                0,
                2);

        // center all text inside the table
        for (int i = 0; i < 3; i++) {
            this.centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            this.tabStats.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labWelcome = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnJoin = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabStats = new javax.swing.JTable();
        btnOptions = new javax.swing.JButton();
        lblLobbyPic = new javax.swing.JLabel();
        lblJoin = new javax.swing.JLabel();
        lblSettings = new javax.swing.JLabel();
        btnTop = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Lobby");
        setLocation(new java.awt.Point(400, 300));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        setSize(new java.awt.Dimension(800, 600));

        labWelcome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labWelcome.setText("welcome");

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(255, 0, 0));
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("Exit Game");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnJoin.setBackground(new java.awt.Color(0, 204, 102));
        btnJoin.setText("Join Game");
        btnJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJoinActionPerformed(evt);
            }
        });

        tabStats.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        tabStats.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "WON / LOST", "KILLS", "HEALS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.Long.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabStats.setToolTipText("");
        tabStats.setShowVerticalLines(false);
        tabStats.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tabStats);
        if (tabStats.getColumnModel().getColumnCount() > 0) {
            tabStats.getColumnModel().getColumn(0).setResizable(false);
            tabStats.getColumnModel().getColumn(1).setResizable(false);
            tabStats.getColumnModel().getColumn(2).setResizable(false);
        }

        btnOptions.setBackground(new java.awt.Color(0, 153, 255));
        btnOptions.setText("Options");
        btnOptions.setToolTipText("");
        btnOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionsActionPerformed(evt);
            }
        });

        lblLobbyPic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/nightday2resized.jpg"))); // NOI18N

        lblJoin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/playResized.jpg"))); // NOI18N
        lblJoin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblJoinMouseClicked(evt);
            }
        });

        lblSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/settings.jpg"))); // NOI18N
        lblSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSettingsMouseClicked(evt);
            }
        });

        btnTop.setBackground(new java.awt.Color(255, 204, 102));
        btnTop.setText("Top 10 Players");
        btnTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(labWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(lblLobbyPic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 35, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJoin, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSettings, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnJoin, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                    .addComponent(btnOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(btnTop, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(83, 83, 83))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnExit, btnJoin, btnLogout});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLobbyPic)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblJoin)
                                    .addComponent(btnJoin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSettings)
                                    .addComponent(btnOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(btnTop, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnExit, btnJoin, btnLogout});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * <i>Action</i> when clicked <b>button</b> opens FormGame to initiate game
     * @see GUI.FormGame
     * @see Code.User
     * @param evt the event itself
     */
    private void btnJoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinActionPerformed
        try {
            
            //check if the user can join a game
            client.out.writeObject(Commands.CHECK_GAME);
            
            //if he can, open a new form game
            if(!(Boolean)client.in.readObject()){
                dispose();
                FormGame form = new FormGame(user);
            }
            else{
                JOptionPane.showMessageDialog(
                    null,
                    localizedResourceBundle.getString(
                            "formLobby.tooMuchPlayers"
                    )
                );
            }
        } catch (IOException ex) {
            Logger.getLogger(FormLobby.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FormLobby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnJoinActionPerformed

    /**
     * <i>Action</i> when clicked <b>button</b>, initiate logout process
     * @param evt the event itself
     */
    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        this.setVisible(false);
        this.dispose();
        FormLogin form = new FormLogin();
    }//GEN-LAST:event_btnLogoutActionPerformed

    /**
     * <i>Action</i> when clicked <b>button</b> opens FormGame to initiate game
     * @see GUI.FormGame
     * @see Code.User
     * @param evt the event itself
     */
    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        int result = JOptionPane.showConfirmDialog(
                null, //parent
                localizedResourceBundle.getString("allForms.exitDialog"), //text
                localizedResourceBundle.getString("allForms.titleExitDialog"), //title
                JOptionPane.YES_NO_OPTION, //display options
                JOptionPane.QUESTION_MESSAGE); //icon(default questionmark)
        //null; usually for an image icon
        if (result == JOptionPane.YES_OPTION) {
            client.exit();
            dispose();
            System.exit(0);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    /**
     * <i>Action</i> when clicked <b>button</b> opens FormOptions for options
     * @see GUI.FormOptions
     * @param evt the event itself
     */
    private void btnOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOptionsActionPerformed
        // TODO add your handling code here:
        FormOptions form = new FormOptions();
    }//GEN-LAST:event_btnOptionsActionPerformed

    private void lblJoinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblJoinMouseClicked

    }//GEN-LAST:event_lblJoinMouseClicked

    private void lblSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSettingsMouseClicked

    }//GEN-LAST:event_lblSettingsMouseClicked

    /**
     * <i>Action</i> when clicked <b>button</b> opens Top10 form, with top10 list
     * @see GUI.FormTop
     * @param evt the event itself
     */
    private void btnTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTopActionPerformed

        FormTop form = new FormTop();
    }//GEN-LAST:event_btnTopActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormLobby.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormLobby.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormLobby.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormLobby.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormLobby().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnJoin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnOptions;
    private javax.swing.JButton btnTop;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labWelcome;
    private javax.swing.JLabel lblJoin;
    private javax.swing.JLabel lblLobbyPic;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JTable tabStats;
    // End of variables declaration//GEN-END:variables
}
