/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Code.User;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import resources.LocalizationUtil;
import static resources.LocalizationUtil.localizedResourceBundle;
import static Code.ClientConnection.client;
import Code.Commands;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the poppup screen of <code>Top10</code>
 * it includes the top10 list of players
 *@author Ben Gilad and Asaf Yeshayahu
 *@version %I%
 *@see GUI.FormGame
 *@since 1.0
 */
public class FormTop extends javax.swing.JFrame {
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

    /**
     * Creates a new <b>form</b> <i>FormOptions</i>
     */
    public FormTop(){
        localizedResourceBundle = ResourceBundle.getBundle(
                "resources.Bundle", new Locale("en_US"));
        this.setLocationRelativeTo(null); // center window
        initComponents();
        updateCaptions();
        this.setTableValues(); // set the user's values inside the table
        this.setVisible(true);

    }
        /**
     * Creates a new <b>form</b> <i>FormOptions</i>
     * @param parent the parent form
     */
    public FormTop(JFrame parent){

        initComponents();
        updateCaptions();
        Color c = new Color(255,204,0);
        Container con = this.getContentPane();
        con.setBackground(c);
                 
        this.setVisible(true);

    }
        /**
     * This method changes the <i>localization</i> and updates the 
     * text to different <i>language</i>
     */
   public void updateCaptions(){
        LocalizationUtil.localizedResourceBundle = ResourceBundle.getBundle
        ("resources.Bundle");
        
        // Set table for top10 list
         tabStats.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null}
                },
                new String[]{
                    localizedResourceBundle.getString("formTop.statsPlace"),
                    localizedResourceBundle.getString("formTop.statsName"),
                    localizedResourceBundle.getString("formTop.statsWins"),
                    localizedResourceBundle.getString("formTop.statsLost"),
                    localizedResourceBundle.getString("formTop.statsKills"),
                    localizedResourceBundle.getString("formTop.statsHeals")
                }));
     //   btnTop.setText(localizedResourceBundle.getString("FormOptions.labVolume"));
     //     labAbusiveLanguage.setText(localizedResourceBundle.getString("FormOptions.labAbusiveLanguage"));
      //                  this.setTitle(localizedResourceBundle.getString("FormOptions.title"));
                        
    }
//    /**
 //    * Updates the the parent and this form with the locale change
 //    */
//    public void btnRadioUpdateLocale(){
//        String selectedLanguage = btnGrpLanguage.getSelection().getActionCommand();
//        localizedResourceBundle = ResourceBundle.getBundle(
//                "resources.Bundle", new Locale(selectedLanguage));
//        
//        this.setTitle(localizedResourceBundle.getString("FormLogin.title"));
//        labVolume.setText(localizedResourceBundle.getString(
//                "FormOptions.labVolume.text"));
//        labAbusiveLanguage.setText(localizedResourceBundle.getString(
//                "FormOptions.labAbusiveLanguage.text"));
//        labLanguage.setText(localizedResourceBundle.getString(
//                "FormOptions.labLanguage.text"));
//        radioEnglish.setText(localizedResourceBundle.getString(
//                "FormOptions.radioEnglish.text"));
//        radioHebrew.setText(localizedResourceBundle.getString(
//                "FormOptions.radioHebrew.text"));
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGrpLanguage = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabStats = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        labTop = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Options");
        setLocationByPlatform(true);
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        tabStats.setBackground(new java.awt.Color(0, 204, 204));
        tabStats.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        tabStats.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Place", "Name", "Wins", "Loses", "Kills", "Heals"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabStats.setToolTipText("");
        tabStats.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabStats.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tabStats);

        labTop.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labTop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labTop.setText("Top 10 Players");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 804, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(labTop, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 54, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(labTop, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

            /**
     * sets values to table
     */
    private void setTableValues() {
        
        try {
            ArrayList<User> users =new ArrayList<User>();
            DefaultTableModel model = (DefaultTableModel) this.tabStats.getModel();
            
            // get top10 list from the server
            client.out.writeObject(Commands.TOP10);
            users = (ArrayList<User>)client.in.readObject();
            
            for (int i = 0, count = 1; i < users.size(); i++, count++) {
                
                
                model.setValueAt(
                        count,
                        i,
                        0);
                
                model.setValueAt(
                        users.get(i).getUsername(),
                        i,
                        1);
                
                model.setValueAt(
                       users.get(i).getWon(), // text
                        i, // row
                        2); //column
                
                model.setValueAt(
                       users.get(i).getLost(), // text
                        i, // row
                        3); //column
                
                model.setValueAt(
                       users.get(i).getKills(),
                        i,
                        4);
                
                model.setValueAt(
                       users.get(i).getHeales(),
                        i,
                        5);
                
                // center all text inside the table
                for ( int j = 0; j < 6; j++) {
                    this.centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                    this.tabStats.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FormTop.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FormTop.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
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
            java.util.logging.Logger.getLogger(FormTop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FormTop().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGrpLanguage;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labTop;
    private javax.swing.JTable tabStats;
    // End of variables declaration//GEN-END:variables
}