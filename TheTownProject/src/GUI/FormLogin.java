package GUI;

import Code.User;
import Code.Commands;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import resources.LocalizationUtil;
import static resources.LocalizationUtil.localizedResourceBundle;
import static Code.ClientConnection.client;
import java.awt.Container;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 * This class is the login screen of <code>TheTownProject</code>, 
 * this is where you login or register.
 *@author Ben Gilad and Asaf Yeshayahu
 *@version %I%
 *@see GUI.FormGame
 *@since 1.0
 */
public class FormLogin extends javax.swing.JFrame {

    /**
     * Creates new <code>FormLogin</code> the login screen
     */
    public FormLogin() {
        initComponents();
       
                Color c = new Color(255,204,204);
                Container con = this.getContentPane();
                con.setBackground(c);

        this.setLocationRelativeTo(null); // center window
        this.pack();
        this.addWindowListener(new myWindowListener());
        this.setVisible(true);
        txtValidUsername.setText("");
        txtValidPassword.setText("");
    }

    /**
     * Starts the login process to the server
     */
    public void login() {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        boolean valid = true;
                        User user = null;

                        //connect to the socket
                        connect();

                        //if failed to connect
                        if (client.online == false) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    localizedResourceBundle.getString("allForms.error"));
                            return;
                        }

                        //reset labels
                        txtValidPassword.setText("");
                        txtValidUsername.setText("");

                        if (txtUsername.getText().isEmpty() == true) {
                            txtValidUsername.setForeground(Color.red);
                            txtValidUsername.setText(
                                    localizedResourceBundle.getString(
                                            "formLogin.btnLoginActionPerformed.usernameInvalid"));
                            valid = false;
                        }

                        if (txtPassword.getText().isEmpty() == true) {
                            txtValidPassword.setForeground(Color.red);
                            txtValidPassword.setText(
                                    localizedResourceBundle.getString(
                                            "formLogin.btnLoginActionPerformed.passwordInvalid"));
                            valid = false;
                            return;
                        }

                        user = new User(txtUsername.getText(), txtPassword.getText());

                        //send details to the server
                        try {
                            client.out.writeObject(Commands.LOGIN);
                            client.out.writeObject(user);
                            client.out.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                    null,
                                    localizedResourceBundle.getString("allForms.error"));
                        }
                        //check if the user is valid or not, and open a form if so
                        checkUser(user);
                    }
                });

    }

    /**
     * This method changes the <i>localization</i> and updates the 
     * text to different <i>language</i>
     * @param lang Language to be set
     * @see resources.LocalizationUtil
     */
    public void updateCaptions(String lang) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                LocalizationUtil.localizedResourceBundle = ResourceBundle.getBundle(
                        "resources.Bundle", new Locale(lang));

                labLang.setText(localizedResourceBundle.getString(
                        "FormLogin.labLang.text"));
                btnHeb.setText(localizedResourceBundle.getString(
                        "FormLogin.btnHeb.text"));
                btnEng.setText(localizedResourceBundle.getString(
                        "FormLogin.btnEng.text"));
                setTitle(localizedResourceBundle.getString(
                        "FormLogin.title"));
                btnRegister.setText(localizedResourceBundle.getString(
                        "FormLogin.btnRegister.label"));
                btnLogin.setText(localizedResourceBundle.getString(
                        "FormLogin.btnLogin.label"));
                labUsername.setText(localizedResourceBundle.getString(
                        "FormLogin.labUsername.text"));
                labPassword.setText(localizedResourceBundle.getString(
                        "FormLogin.labPassword.text"));
                btnOptions.setText(localizedResourceBundle.getString(
                        "FormLogin.btnOptions.text"));
                btnExit.setText(localizedResourceBundle.getString(
                        "FormLogin.btnExit.text"));
                txtValidPassword.setText(localizedResourceBundle.getString(
                        "FormLogin.txtValidPassword.text"));
                txtValidUsername.setText(localizedResourceBundle.getString(
                        "FormLogin.txtValidUsername.text"));

            }
        });
    }

    /**
     * Sends a query to the server and checks the user for these cases:
     * <ul>
     * <li> If already connected </li>
     * <li> If details are wrong (password / user) </li>
     * <li> If there was an error (Server side) </li>
     * </ul>
     * @param user The {@code User} to be checked
     * @see Code.User
     */
    public void checkUser(User user) {
        try {

            //calls for a method inside BasicClientTest,
            //that returns an User with all the information
            Commands result = (Commands) client.in.readObject();

            FormLobby f1;

            switch (result) {
                case ALREADY_CONNECTED:
                    JOptionPane.showMessageDialog(
                            null,
                            localizedResourceBundle.getString(
                                    "formLogin.btnLoginActionPerformed.userAlreadyConnected"));
                    break;
                case CONNECTION_ERROR:
                    JOptionPane.showMessageDialog(
                            null,
                            localizedResourceBundle.getString("allForms.error"));
                    break;
                case WRONG_DETAILS:
                    JOptionPane.showMessageDialog(
                            null,
                            localizedResourceBundle.getString(
                                    "formLogin.btnLoginActionPerformed.wrongDetails"));
                    break;
                case LOGIN:
                    user = (User) client.in.readObject();
                    this.setVisible(false);
                    f1 = new FormLobby(user);
                    return;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    localizedResourceBundle.getString("allForms.error"));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Starts the socket connection
     */
    public void connect() {
        //start the connection
        try {
            client.startConnection();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    localizedResourceBundle.getString("allForms.error"));
            client.online = false;
        }
    }

    /**
     * <i>Dialog</i> pop-up used for exiting the game
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
                System.exit(0);
            }
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

        labPassword = new javax.swing.JLabel();
        labUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        txtValidUsername = new javax.swing.JLabel();
        txtValidPassword = new javax.swing.JLabel();
        labLogo = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();
        btnOptions = new javax.swing.JButton();
        btnEng = new javax.swing.JButton();
        btnHeb = new javax.swing.JButton();
        labLang = new javax.swing.JLabel();
        lblLang = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Login");
        setBackground(new java.awt.Color(255, 204, 204));
        setLocation(new java.awt.Point(400, 300));
        setPreferredSize(new java.awt.Dimension(733, 550));
        setResizable(false);
        setSize(new java.awt.Dimension(733, 414));

        labPassword.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labPassword.setForeground(new java.awt.Color(255, 51, 51));
        labPassword.setText("Password:");
        labPassword.setName("labPassword"); // NOI18N

        labUsername.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labUsername.setForeground(new java.awt.Color(255, 51, 51));
        labUsername.setText("Username:");
        labUsername.setName("labUsername"); // NOI18N

        txtUsername.setToolTipText("");
        txtUsername.setName("txtUsername"); // NOI18N
        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsernameKeyPressed(evt);
            }
        });

        txtPassword.setName("password"); // NOI18N
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });

        btnLogin.setBackground(new java.awt.Color(0, 204, 51));
        btnLogin.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnLogin.setLabel("Login");
        btnLogin.setName("btnLogin"); // NOI18N
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnRegister.setBackground(new java.awt.Color(0, 204, 51));
        btnRegister.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnRegister.setLabel("Register");
        btnRegister.setName("btnSignUp"); // NOI18N
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        txtValidUsername.setText("valid");

        txtValidPassword.setText("valid");

        labLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Login.png"))); // NOI18N

        btnExit.setBackground(new java.awt.Color(255, 0, 0));
        btnExit.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnExit.setText("Exit Game");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnOptions.setBackground(new java.awt.Color(51, 153, 255));
        btnOptions.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnOptions.setText("Options");
        btnOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionsActionPerformed(evt);
            }
        });

        btnEng.setText("English");
        btnEng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEngActionPerformed(evt);
            }
        });

        btnHeb.setText("Hebrew");
        btnHeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHebActionPerformed(evt);
            }
        });

        labLang.setText("Language");

        lblLang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/languageIcon.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEng)
                                    .addComponent(btnHeb)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(labLang))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblLang)))
                        .addGap(219, 219, 219)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOptions, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(221, 221, 221)
                                .addComponent(labLogo))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(334, 334, 334)
                                .addComponent(labPassword)))
                        .addGap(7, 7, 7)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtValidUsername, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtValidPassword, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap(299, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
            .addGroup(layout.createSequentialGroup()
                .addGap(332, 332, 332)
                .addComponent(labUsername)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labLogo)
                .addGap(16, 16, 16)
                .addComponent(labUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtValidUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(txtValidPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(lblLang)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labLang)
                        .addGap(7, 7, 7)
                        .addComponent(btnEng)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHeb))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    /**
     * <i>Action</i> when clicked <b>button</b> for login procedure
     * @param evt Click event
     * @see login()
     */
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        login();
    }//GEN-LAST:event_btnLoginActionPerformed

    /**
     * <i>Action</i> when clicked <b>button</b> for register procedure
     * @param evt Click event
     */
    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        // TODO add your handling code here:
        FormRegister form = new FormRegister(this);
        form.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnRegisterActionPerformed

    /**
     * <i>Action</i> when clicked <b>button</b> before exiting game
     * @param evt Click event
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
     * <i>Action</i> Logging in using the <b>'Enter'</b> Key
     * @param evt User pressed <b>'Enter'</b> Key
     */
    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        // TODO add your handling code here:

        //if user entered "ENTER"
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            login();
        }
    }//GEN-LAST:event_txtPasswordKeyPressed

    /**
     * <i>Action</i> Logging in using the <b>'Enter'</b> Key
     * @param evt User pressed <b>'Enter'</b> Key
     */
    private void txtUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyPressed
        // TODO add your handling code here:

        //if user entered "ENTER"
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            login();
        }
    }//GEN-LAST:event_txtUsernameKeyPressed

    /**
     * <i>Action</i>open <b>options</b> when clicked <b>button</b>
     * @param evt Click event
     */
    private void btnOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOptionsActionPerformed
        // TODO add your handling code here:

        FormOptions form = new FormOptions(this);
        form.setVisible(true);
        btnRegister.setText(localizedResourceBundle.getString(
                "FormLogin.btnRegister.label"));
        btnLogin.setText(localizedResourceBundle.getString(
                "FormLogin.btnLogin.label"));
        labUsername.setText(localizedResourceBundle.getString(
                "FormLogin.labUsername.text"));
        labPassword.setText(localizedResourceBundle.getString(
                "FormLogin.labPassword.text"));
        btnOptions.setText(localizedResourceBundle.getString(
                "FormLogin.btnOptions.text"));
        this.setTitle(localizedResourceBundle.getString(
                "FormLogin.title"));
        btnExit.setText(localizedResourceBundle.getString(
                "FormLogin.btnExit.text"));
        txtValidPassword.setText(localizedResourceBundle.getString(
                "FormLogin.txtValidPassword.text"));
        txtValidUsername.setText(localizedResourceBundle.getString(
                "FormLogin.txtValidUsername.text"));

    }//GEN-LAST:event_btnOptionsActionPerformed

    /**
     * <i>Action</i> clicked <b>Hebrew</b> to change language <b>button</b>
     * @param evt Click event
     */
    private void btnHebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHebActionPerformed
        Locale.setDefault(new Locale("iw"));
        updateCaptions("iw");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHebActionPerformed

    /**
     * <i>Action</i> clicked <b>English</b> to change language <b>button</b>
     * @param evt Click event
     */
    private void btnEngActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEngActionPerformed
        Locale.setDefault(new Locale("en", "US"));
        updateCaptions("en_US");
    }//GEN-LAST:event_btnEngActionPerformed

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
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                FormLogin login = new FormLogin();

                login.setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEng;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHeb;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnOptions;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel labLang;
    private javax.swing.JLabel labLogo;
    private javax.swing.JLabel labPassword;
    private javax.swing.JLabel labUsername;
    private javax.swing.JLabel lblLang;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JLabel txtValidPassword;
    private javax.swing.JLabel txtValidUsername;
    // End of variables declaration//GEN-END:variables
}
