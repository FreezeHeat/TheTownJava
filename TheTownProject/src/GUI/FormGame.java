package GUI;

import Code.User;
import Code.Citizen;
import Code.Commands;
import Code.Healer;
import Code.Killer;
import Code.Roles;
import Code.Snitch;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Utils.JTextFieldLimit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import resources.LocalizationUtil;
import static resources.LocalizationUtil.localizedResourceBundle;
import static Code.ClientConnection.client;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * This class is the main game screen of <code>TheTownProject</code>, 
 * this is where the game is played
 *@author Ben Gilad and Asaf Yeshayahu
 *@version %I%
 *@see GUI.FormGame
 *@since 1.0
 */

public class FormGame extends javax.swing.JFrame {

     /**
     * boolean variable that sets whether a player voted or not
     */
    boolean voted;
    
    /**
     * The last user you(the current user) applied an action on
     */
    User lastActionUser;
    
     /**
     * {@code Arraylist} that holds all users in game
     * @see Code.User
     */
    ArrayList<User> users;
    
     /**
     * variable that holds a user
     * @see Code.User
     */
    User user;
    
     /**
     * variable that holds users' role
     * @see Code.Roles
     */
    Roles role;

    /**
     * A game {@code Thread}, used to receive information from the server
     * for the game logic
     */
    Game game = new Game();
    
     /**
     * timer for scheduled threads
     */
    Timer secondsTimer;
    
    /**
     * Seconds for the timer
     */
    int seconds = 0;
    
    /**
     * Used for global chat, it is used for color-coded messages
     */
    HTMLDocument globalChat;
    
    /**
     * Used in tandem with {@code globalChat} variable
     * @see #globalChat
     */
    HTMLEditorKit globalEditor;
    
     /**
     * boolean variable for checking if cycle is in day mode
     */
    Boolean day;

//    ClientOutputThread outputThread;
    /**
     * Creates new empty form <code>formGame</code>
     */
    
    /**
     * {@code JLabel ArrayList} that holds the images for any specific {@code Role}
     * @see Roles
     */
    ArrayList<JLabel> imageLabels = new ArrayList();
    
    /**
     * {@code JButton ArrayList} that holds the buttons for any specific {@code Role}
     * @see Roles
     */
    ArrayList<JButton> buttons = new ArrayList();
    
    /**
     * {@code JLabel ArrayList} that holds the labels for each player name
     * @see Roles
     */
    ArrayList<JLabel> playerLabels = new ArrayList();
    
    public FormGame() {
    }

    /**
     * Creates new empty form <code>formGame</code> the game screen
     *
     * @param user The logged in user
     */
    public FormGame(User user) {
        initComponents();
        updateCaptions();
        Border border = MainGamePanel.getBorder();
        Border margin = new EmptyBorder(0, 25, 0, 0);
        MainGamePanel.setBorder(new CompoundBorder(border, margin));
        this.user = user;
        Color c = new Color(255,204,204);
        Container con = this.getContentPane();
        con.setBackground(c);
        
        //FULL SCREEN MODE
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.addWindowListener(new FormGame.myWindowListener());

        // the user's input into the chat field
        this.txtChatField.setDocument(new JTextFieldLimit(100));

        //set to JTextArea to auto-scroll
        DefaultCaret caretGlobal = (DefaultCaret) txtGlobalChat.getCaret();
        DefaultCaret caretKillers = (DefaultCaret) txtKillersChat.getCaret();
        caretGlobal.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        caretKillers.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        //set txtGlobalChat so HTML tags can be added
        globalChat = (HTMLDocument) txtGlobalChat.getDocument();
        globalEditor = (HTMLEditorKit) txtGlobalChat.getEditorKit();

        //initiate arrays of players information
        this.buttons.add(btnPlayer1);
        this.buttons.add(btnPlayer2);
        this.buttons.add(btnPlayer3);
        this.buttons.add(btnPlayer4);
        btnPlayer5.setVisible(false);
        
        this.playerLabels.add(p1);
        this.playerLabels.add(p2);
        this.playerLabels.add(p3);
        this.playerLabels.add(p4);
        p5.setVisible(false);
        
        this.imageLabels.add(p1Lab);
        this.imageLabels.add(p2Lab);
        this.imageLabels.add(p3Lab);
        this.imageLabels.add(p4Lab);
        p5Lab.setVisible(false);
        
        
        //hide action buttons and set labels to blank
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setVisible(false);
            playerLabels.get(i).setText("");
        }

        game.start();
    }

    //GUI UPDATES 
    
    /**
     * This method updates <b>GUI</b> in this class
     */
    public void updateGuiKill() {
        URL loadURL = FormGame.class.getResource("kill.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);
        statusPic.setIcon(imageIcon);
    }
    
    /**
     * This method updates <b>GUI </b>in this class
     */
    public void updateGuiVote() {
        URL loadURL = FormGame.class.getResource("voteyes.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);
        statusPic.setIcon(imageIcon);
    }
    
    /**
     * This method updates <b>GUI </b>in this class
     */
    public void updateGuiDetect() {
        URL loadURL = FormGame.class.getResource("detect.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);
        statusPic.setIcon(imageIcon);
    }
    
    /**
     * This method updates <b>GUI</b> in this class
     */
    public void updateGuiVoteNo() {
        URL loadURL = FormGame.class.getResource("voteno.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);
        statusPic.setIcon(imageIcon);
    }
    
    /**
     * This method updates <b>GUI</b> in this class
     */
    public void updateGuiHeal() {
        URL loadURL = FormGame.class.getResource("heal.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);
        statusPic.setIcon(imageIcon);
    }
    
    /**
     * This method updates<b> GUI </b>in this class
     */
    private void updateGuiHanged() {
        URL loadURL = FormGame.class.getResource("hang.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);
        statusPic.setIcon(imageIcon);
    }
    
    /**
     * This method updates <b>GUI</b> in this class
     */
    public void updateGuiDay() {
        URL loadURL = FormGame.class.getResource("sun.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);
        statusPic.setIcon(imageIcon);
    }
    
    /**
     * This method updates<b>GUI</b> in this class
     */
    public void updateGuiNight() {
        URL loadURL = FormGame.class.getResource("moon.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);
        statusPic.setIcon(imageIcon);
    }
    
    /**
     * This method updates buttons for clients' <b>GUI</b>
     */
    public void updateBtnsKill() {
        URL loadURL = FormGame.class.getResource("kill.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);

        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).IsAlive()){
                imageLabels.get(i).setIcon(imageIcon);
            }
        }
    }
    
    /**
     * This method updates buttons for clients' <b>GUI</b>
     */
    public void updateBtnsHeal() {
        URL loadURL = FormGame.class.getResource("heal.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);

        for (int i = 0; i < users.size(); i++) {
           if(users.get(i).IsAlive()){
                imageLabels.get(i).setIcon(imageIcon);
            }
        }
    }
    /**
     * This method updates buttons for clients' <b>GUI</b>
     */
    public void updateBtnsVoteYes() {
        URL loadURL = FormGame.class.getResource("voteyes.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);

        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).IsAlive()){
               imageLabels.get(i).setIcon(imageIcon);
            }
        }
    }

//    public void updateBtnsVoteNo() {
//        JLabel[] labels = new JLabel[]{p1Lab, p2Lab, p3Lab, p4Lab, p5Lab};
//
//        URL loadURL = FormGame.class.getResource("voteno.jpeg");
//        ImageIcon imageIcon = new ImageIcon(loadURL);
//
//        for (int i = 0; i < labels.length - 1; i++) {
//            labels[i].setIcon(imageIcon);
//        }
//    }
    /**
     * This method updates button labels for clients' <b>GUI</b>
     */
    public void updateBtnsDetect() {
        URL loadURL = FormGame.class.getResource("detect.jpeg");
        ImageIcon imageIcon = new ImageIcon(loadURL);

        for (int i = 0; i < users.size(); i++) {
           if(users.get(i).IsAlive()){
               imageLabels.get(i).setIcon(imageIcon);
            }
        }
    }
    /**
     * This method updates button labels for clients' <b>GUI</b>
     */
    public void updateBtnsDead() {
        URL loadURL = FormGame.class.getResource("blood.png");
        ImageIcon imageIcon = new ImageIcon(loadURL);

        for (int i = 0; i < imageLabels.size(); i++) {
            imageLabels.get(i).setIcon(imageIcon);
        }
    }
    
    /**
     * This method updates button lables and buttons for citizens' <b>GUI</b>
     */
    public void updateBtnsCitizen(){
        int i;
        for (i = 0; i < users.size(); i++) {
            if(users.get(i).IsAlive()){
                imageLabels.get(i).setIcon(null);
            }
            buttons.get(i).setVisible(false);
        }
    }
    
    /**
     * This method updates button labels for clients' <b>GUI</b>
     */
    public void clearBtns() {
        for (int i = 0; i < imageLabels.size(); i++) {
            imageLabels.get(i).setIcon(null);
        }
    }
    /**
     * This method updates text label for clients' <b>GUI</b>
     * <p>Updates the <b>phase</b></p>
     */
    public void setPhaseBegin() {
        phaseInfo.setText(localizedResourceBundle.getString("FormGame.phaseBegin"));
    }
    /**
     * This method updates text label for clients' <b>GUI</b>
     * <p>Updates the <b>phase</b></p>
     */
    public void setPhaseDay() {
        phaseInfo.setText(localizedResourceBundle.getString("FormGame.phaseDay"));
    }
    /**
     * This method updates text label for clients' <b>GUI</b>
     * <p>Updates the <b>phase</b></p>
     */
    public void setPhaseNight() {
        phaseInfo.setText(localizedResourceBundle.getString("FormGame.phaseNight"));
    }

    /**
     * This method changes the <i>localization</i> and updates the 
     * text to different <i>language</i>
     * @see resources.LocalizationUtil
     */
    public void updateCaptions() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LocalizationUtil.localizedResourceBundle = ResourceBundle.getBundle("resources.Bundle");
                tabGlobal.setName(localizedResourceBundle.getString("FormGame.tabGlobal"));
                tabKillers.setName(localizedResourceBundle.getString("FormGame.tabKillers"));
                setTitle(localizedResourceBundle.getString("FormGame.title"));
                btnExit.setText(localizedResourceBundle.getString("FormGame.btnExit"));
                btnDisconnect.setText(localizedResourceBundle.getString("FormGame.btnDisconnect"));
                btnOptions.setText(localizedResourceBundle.getString("FormGame.btnOptions"));
                btnSend.setText(localizedResourceBundle.getString("FormGame.btnSend"));
                chatTabs.setTitleAt(
                        0,
                        localizedResourceBundle.getString("FormGame.tabGlobal"));
                chatTabs.setTitleAt(
                        1,
                        localizedResourceBundle.getString("FormGame.tabKillers"));
                sidebarTab.setTitleAt(
                        0,
                        localizedResourceBundle.getString("FormGame.roleTab"));
                sidebarTab.setTitleAt(
                        1,
                        localizedResourceBundle.getString("FormGame.statsTab"));
            }
        });
    }

    /**
     * <b>GUI method </b> sets clients' <i>labels</i> 
     * @see Code.User
     * @param list list of users in-game
     */
    //You get the list of players from the server, including you,
    //and you must set the names in the gui
    //you also need to match the buttons you your class
    public void setPlayerNames(List<User> list) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int i;
                
                //only if the current user in the list is this user
                //and if currently before checking, this user is alive
                //NOTE: if user isn't alive, there's no need to update
                user = list.get(list.indexOf(user));
                
                //remove the user from the list
                //since GUI updates only other players' labels
                list.remove(user);
                
                //alert the user if he's dead
                if (role != Roles.DEAD && user.IsAlive() == false) {
                    JOptionPane.showMessageDialog(
                            null,
                            localizedResourceBundle.getString(
                                    "FormGame.sendDead"
                            )
                    );
                    //set role to dead and enable chat (DEAD CHAT)
                    role = Roles.DEAD;
                    txtChatField.setEditable(true);
                    btnSend.setEnabled(true);
                }
                
                // update player's role
                setPlayerRole();
                
                //iterate through the list and array of labels and add the names
                //also get the role of the current player
                for (i = 0; i < list.size();) {

                    //if the player is dead, remove his/her avatar
                    if (list.get(i).IsAlive() == false) {
                        list.remove(i);
                        playerLabels.get(i).setText("");
                        playerLabels.get(i).setVisible(false);
                        buttons.get(i).setVisible(false);
                        imageLabels.get(i).setIcon(null);
                        continue;
                    }
                    playerLabels.get(i).setText(list.get(i).getUsername());
                    playerLabels.get(i).setVisible(true);
                    i++;
                }
                
                // set the buttons and labels that aren't in use
                while(i < buttons.size()){
                    imageLabels.get(i).setIcon(null);
                    buttons.get(i).setVisible(false);
                    playerLabels.get(i).setVisible(false);
                    playerLabels.get(i).setText("");
                    i++;
                }
            }
        });
    }
/**
 * <b>GUI method </b> sets clients' <i>labels</i> 
 */
    private void getPlayerStatus() {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(user)) {
                user = users.get(i);
                return;
            }
        }
    }

    /**
     * <b>GUI method </b> sets clients' <i>labels</i> 
     *
     * @param category role category
     */
    public void setPlayerButtons(Roles category) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String action = "";
                
                switch (category) {
                    case CITIZEN:
                        updateBtnsCitizen();
                        return;
                    case KILLER:
                        updateBtnsKill();
                        action = localizedResourceBundle.getString("FormGame.kill");
                        break;
                    case HEALER:
                        updateBtnsHeal();
                        action = localizedResourceBundle.getString("FormGame.heal");
                        break;
                    case SNITCH:
                        updateBtnsDetect();
                        action = localizedResourceBundle.getString("FormGame.snitch");
                        break;
                    case VOTE:
                        clearBtns();
                        action = localizedResourceBundle.getString("FormGame.vote");
                        break;
                    default:
                        break;
                }
                
                // in case the player is dead
                if(user.IsAlive() == false){
                    updateBtnsDead();
                    action = localizedResourceBundle.getString("FormGame.dead");
                }

                //when there's no user specified in the avatar, do not update the button
                for (int i = 0; i < buttons.size(); i++) {
                    if ((!playerLabels.get(i).getText().equals("")) && user.IsAlive()) {
                        buttons.get(i).setText(action);
                        buttons.get(i).setVisible(true);
                    } //hide the button if there's no name there
                    else {
                        buttons.get(i).setVisible(false);
                    }
                }
            }
        });
    }

    /**
     *  sets clients' <i>role</i> 
     */
    public void setPlayerRole() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (user.IsAlive() == false) {
                    role = Roles.DEAD;
                } else if (user instanceof Citizen) {
                    role = Roles.CITIZEN;
                } else if (user instanceof Killer) {
                    role = Roles.KILLER;
                } else if (user instanceof Snitch) {
                    role = Roles.SNITCH;
                } else if (user instanceof Healer) {
                    role = Roles.HEALER;
                }

                updateRoleStats(role);
            }
        });
    }

    /**
     *<b>GUI method </b> sets clients' <i>role</i> 
     *
     * @param category the role's category
     */
    public void updateRoleStats(Roles category) {
        switch (category) {
            case CITIZEN:
                txtRole.setText(
                        user.getUsername()
                        + "\n"
                        + localizedResourceBundle.getString("FormGame.rCitizen")
                        + "\n"
                        + localizedResourceBundle.getString("FormGame.RoleCitizen"));
                return;
            case KILLER:
                txtRole.setText(
                        user.getUsername()
                        + "\n"
                        + localizedResourceBundle.getString("FormGame.rKiller")
                        + "\n"
                        + localizedResourceBundle.getString("FormGame.RoleKiller"));
                break;
            case HEALER:
                txtRole.setText(
                        user.getUsername()
                        + "\n"
                        + localizedResourceBundle.getString("FormGame.rHealer")
                        + "\n"
                        + localizedResourceBundle.getString("FormGame.RoleHealer"));
                break;
            case SNITCH:
                txtRole.setText(
                        user.getUsername()
                        + "\n"
                        + localizedResourceBundle.getString("FormGame.rSnitch")
                        + "\n"
                        + localizedResourceBundle.getString("FormGame.RoleSnitch"));
                break;
            case VOTE:
                break;
            case DEAD:
                txtRole.setText(
                        user.getUsername()
                        + "\n"
                        + localizedResourceBundle.getString("FormGame.dead"));
                break;
            default:
                break;
        }
    }

    /**
     * Method that intercepts <b>targets</b> of each type of 
     * client <i>(based on role)</i> and executes an <b>action</b>
     * 
     * @see Code.User
     * @param target client is targeting this target
     * @param category the role's category
     */
    public void playerAction(User target, Roles category) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                switch (category) {
                    case CITIZEN:
                        break;
                    case KILLER:
                        Killer k = (Killer) user;
                        k.kill(target);
                        break;
                    case HEALER:
                        Healer h = (Healer) user;
                        h.heal(target);
                        break;
                    case SNITCH:
                        Snitch s = (Snitch) user;
                        s.snitch(target);
                        break;
                    case VOTE:
                        user.vote(target);
                        
                        //set icon for the target and clear everyone else
                        if(voted == false || (!target.equals(lastActionUser)) ){
                            for (int i = 0; i < imageLabels.size(); i++) {
                                if (i != users.indexOf(target)) {
                                    imageLabels.get(i).setIcon(null);
                                } else {
                                    URL loadURL = FormGame.class.getResource("voteno.jpeg");
                                    ImageIcon imageIcon = new ImageIcon(loadURL);
                                    imageLabels.get(i).setIcon(imageIcon);
                                }
                            }
                            voted = true;
                            lastActionUser = target;
                        }
                        
                        // if it's the same target, recall action:
                        // set icons to null (remove icons)
                        else if(target.equals(lastActionUser)){
                            for (int i = 0; i < imageLabels.size(); i++) {
                                    imageLabels.get(i).setIcon(null);
                            }
                            
                            voted = false;
                            lastActionUser = null;
                        }
                        break;
                    case DEAD:
                        JOptionPane.showMessageDialog(
                                null,
                                localizedResourceBundle.getString("FormGame.sendDead"));
                    default:
                        break;
                }
            }
        });
    }

    /**
     * Method for updating <b>Chat</b> GUI
     */
    public void sendInput() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                //if role is killer and selected chat is killers
                if (role == Roles.KILLER && cbChat.getSelectedIndex() == 1) {
                    Killer k = (Killer) user;
                    k.sendMessageKiller(txtChatField.getText());
                } else if (role != Roles.DEAD) {
                    user.sendMessage(txtChatField.getText());
                } else {
                    user.sendMessageDead(txtChatField.getText());
                }

                //clear the text field for the next input
                txtChatField.setText("");
            }
        });
    }

    /**
     * <i>thread</i> for the input from the server
     */
    public class Game extends Thread {

        Commands command;
        String msg;

        @Override
        public void run() {
            try {

                //Tell the server that you are ready for the game
                user.joinGame();
                client.out.flush();
                boolean inGame = true;
                while (inGame) {

                    //game logic and protocol
                    command = (Commands) client.in.readObject();
                    switch (command) {
                        case SEND_MESSAGE:
                            msg = (String) client.in.readObject();

                            //add to textPane as HTML
                            try {
                                globalEditor.insertHTML(
                                        globalChat,
                                        globalChat.getLength(),
                                        "<font color='white'>"
                                        + msg
                                        + "</font><br>",
                                        0,
                                        0,
                                        null);
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case SEND_MESSAGE_KILLER:
                            msg = (String) client.in.readObject();
                            txtKillersChat.append(msg + "\n");
                            break;
                        case SEND_MESSAGE_DEAD:
                            msg = (String) client.in.readObject();
                            try {
                                globalEditor.insertHTML(
                                        globalChat,
                                        globalChat.getLength(),
                                        "<font color='red'>*DEAD*"
                                        + msg
                                        + "</font><br>",
                                        0,
                                        0,
                                        null);
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case SERVER_MESSAGE:
                            msg = (String) client.in.readObject();
                            try {
                                globalEditor.insertHTML(
                                        globalChat,
                                        globalChat.getLength(),
                                        "<font color='green'>"
                                        + msg
                                        + "</font><br>",
                                        0,
                                        0,
                                        null);
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case REFRESH_PLAYERS:
                            users = (ArrayList<User>) client.in.readObject();
                            setPlayerNames(users);
                            break;
                        case BEGIN:
                            p1.setToolTipText(users.get(0).toString());
                            p2.setToolTipText(users.get(1).toString());
                            p3.setToolTipText(users.get(2).toString());
                            p4.setToolTipText(users.get(3).toString());
                            
                            txtStats.append((user.toStringTwo()));
                            txtStats.append((users.get(0).toStringTwo()));
                            txtStats.append((users.get(1).toStringTwo()));
                            txtStats.append((users.get(2).toStringTwo()));
                            txtStats.append((users.get(3).toStringTwo()));
                            
                            setPhaseBegin();
                            updateGuiDay();
                            setTimer(35);
                            break;
                        case DAY:
                            voted= false;
                            updateGuiDay();
                            setPhaseDay();
                            if(role != Roles.DEAD){
                                setPlayerButtons(Roles.VOTE);
                                txtChatField.setEditable(true);
                                btnSend.setEnabled(true);
                            }else{
                                setPlayerButtons(Roles.DEAD);
                            }
                            setTimer(40);
                            day = true;
                            break;
                        case NIGHT:
                            updateGuiNight();
                            setPhaseNight();
                            
                            //dead people can talk in chat all the time
                            if(user.IsAlive() == true){
                                txtChatField.setEditable(false);
                                btnSend.setEnabled(false);
                                setPlayerButtons(role);
                            }else{
                                setPlayerButtons(Roles.DEAD);
                            }
                            setTimer(30);
                            day = false;
                            break;
                        case END:
                            Boolean citizenWin = (Boolean) client.in.readObject();
                            if (citizenWin) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        localizedResourceBundle.getString("FormGame.winCitizens"));
                            } else {
                                JOptionPane.showMessageDialog(
                                        null,
                                        localizedResourceBundle.getString("FormGame.winKillers"));
                            }
                            inGame = false;
                            gameFinished();
                            break;
                        case CONNECTION_ERROR:
                            JOptionPane.showMessageDialog(
                                        null,
                                        localizedResourceBundle.getString("FormGame.notEnoughPlayers"));
                            inGame = false;
                            client.exit();
                            FormLogin form = new FormLogin();
                            form.setVisible(true);
                            dispose();
                            break;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        localizedResourceBundle.getString("allForms.error"));
                client.exit();
                FormLogin form = new FormLogin();
                form.setVisible(true);
                dispose();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Set a <b>timer</b> to initiate a <i>TimerTask</i>, this is for the timer in-game
     *
     * @param seconds the seconds the timer will be set to
     */
    public void setTimer(int seconds) {
        this.seconds = seconds;
        if (secondsTimer != null && secondsTimer.isRunning() == true) {
            secondsTimer.stop();
        }
        secondsTimer = new Timer(1000, new SecondsTimer());
        secondsTimer.setInitialDelay(1000);
        secondsTimer.start();
    }

    /**
     * <p>When the game is finished, update the <b>user</b> (with the <i>new stats</i>) then open
     * the formLobby with the new info</p>
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void gameFinished() throws IOException, ClassNotFoundException {
        user = (User) client.in.readObject();
        client.exit();
        FormLogin form = new FormLogin();
        form.setVisible(true);
        dispose();
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
                dispose();
                client.exit();
                System.exit(0);
            }
        }
    }

    /**
     * Used with the timer in-game, it updates the <b>GUI</b>
     */
    class SecondsTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            timer.setText("" + --seconds);
            if (seconds <= 0) {
                secondsTimer.stop();
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

        Main = new javax.swing.JPanel();
        chatWindow = new javax.swing.JPanel();
        chatTabs = new javax.swing.JTabbedPane();
        tabGlobal = new javax.swing.JScrollPane();
        txtGlobalChat = new javax.swing.JTextPane();
        tabKillers = new javax.swing.JScrollPane();
        txtKillersChat = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        chatActionPanel = new javax.swing.JPanel();
        btnSend = new javax.swing.JButton();
        txtChatField = new javax.swing.JTextField();
        chatSelectorPanel = new javax.swing.JPanel();
        cbChat = new javax.swing.JComboBox();
        MainGamePanel = new javax.swing.JPanel();
        gameStatusPanel = new javax.swing.JPanel();
        phaseInfo = new javax.swing.JTextArea();
        statusPic = new javax.swing.JLabel();
        timer = new javax.swing.JLabel();
        playersPanel = new javax.swing.JPanel();
        p1p = new javax.swing.JPanel();
        p1 = new javax.swing.JLabel();
        btnPlayer1 = new javax.swing.JButton();
        p1Lab = new javax.swing.JLabel();
        p2p = new javax.swing.JPanel();
        p2 = new javax.swing.JLabel();
        btnPlayer2 = new javax.swing.JButton();
        p2Lab = new javax.swing.JLabel();
        p3p = new javax.swing.JPanel();
        p3 = new javax.swing.JLabel();
        btnPlayer3 = new javax.swing.JButton();
        p3Lab = new javax.swing.JLabel();
        p4p = new javax.swing.JPanel();
        p4 = new javax.swing.JLabel();
        btnPlayer4 = new javax.swing.JButton();
        p4Lab = new javax.swing.JLabel();
        p5p = new javax.swing.JPanel();
        p5 = new javax.swing.JLabel();
        btnPlayer5 = new javax.swing.JButton();
        p5Lab = new javax.swing.JLabel();
        sliderBar = new javax.swing.JPanel();
        sidebarTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        txtRole = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        txtStats = new javax.swing.JTextArea();
        options = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnOptions = new javax.swing.JButton();
        btnDisconnect = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("TheTownProject");
        setBackground(new java.awt.Color(0, 0, 0));
        setFocusCycleRoot(false);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(1024, 768));
        setSize(new java.awt.Dimension(1024, 768));

        Main.setLayout(new java.awt.BorderLayout());

        chatWindow.setPreferredSize(new java.awt.Dimension(1456, 220));
        chatWindow.setRequestFocusEnabled(false);
        chatWindow.setLayout(new java.awt.BorderLayout());

        chatTabs.setToolTipText("");
        chatTabs.setAutoscrolls(true);
        chatTabs.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        chatTabs.setInheritsPopupMenu(true);

        tabGlobal.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tabGlobal.setToolTipText("");
        tabGlobal.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tabGlobal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabGlobalMouseClicked(evt);
            }
        });

        txtGlobalChat.setEditable(false);
        txtGlobalChat.setBackground(new java.awt.Color(0, 0, 0));
        txtGlobalChat.setContentType("text/html"); // NOI18N
        txtGlobalChat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtGlobalChat.setToolTipText("");
        tabGlobal.setViewportView(txtGlobalChat);

        chatTabs.addTab("Global", tabGlobal);

        tabKillers.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tabKillers.setToolTipText("");
        tabKillers.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tabKillers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabKillersMouseClicked(evt);
            }
        });

        txtKillersChat.setColumns(30);
        txtKillersChat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtKillersChat.setLineWrap(true);
        txtKillersChat.setRows(5);
        txtKillersChat.setWrapStyleWord(true);
        tabKillers.setViewportView(txtKillersChat);

        chatTabs.addTab("Killers", tabKillers);

        chatWindow.add(chatTabs, java.awt.BorderLayout.CENTER);

        jPanel5.setLayout(new java.awt.BorderLayout());

        chatActionPanel.setLayout(new java.awt.BorderLayout());

        btnSend.setBackground(new java.awt.Color(0, 204, 102));
        btnSend.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });
        chatActionPanel.add(btnSend, java.awt.BorderLayout.LINE_END);

        txtChatField.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtChatField.setText("chatTxtField");
        txtChatField.setName(""); // NOI18N
        txtChatField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtChatFieldKeyPressed(evt);
            }
        });
        chatActionPanel.add(txtChatField, java.awt.BorderLayout.CENTER);

        jPanel5.add(chatActionPanel, java.awt.BorderLayout.CENTER);

        chatSelectorPanel.setLayout(new java.awt.BorderLayout());

        cbChat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Global", "Killers" }));
        cbChat.setToolTipText("");
        cbChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbChatActionPerformed(evt);
            }
        });
        chatSelectorPanel.add(cbChat, java.awt.BorderLayout.CENTER);

        jPanel5.add(chatSelectorPanel, java.awt.BorderLayout.WEST);

        chatWindow.add(jPanel5, java.awt.BorderLayout.SOUTH);

        Main.add(chatWindow, java.awt.BorderLayout.SOUTH);

        MainGamePanel.setLayout(new javax.swing.BoxLayout(MainGamePanel, javax.swing.BoxLayout.PAGE_AXIS));

        gameStatusPanel.setLayout(new java.awt.GridLayout(1, 0));

        phaseInfo.setEditable(false);
        phaseInfo.setBackground(new java.awt.Color(240, 240, 240));
        phaseInfo.setColumns(20);
        phaseInfo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        phaseInfo.setLineWrap(true);
        phaseInfo.setRows(6);
        phaseInfo.setWrapStyleWord(true);
        gameStatusPanel.add(phaseInfo);

        statusPic.setToolTipText("");
        statusPic.setPreferredSize(new java.awt.Dimension(100, 100));
        gameStatusPanel.add(statusPic);

        timer.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        gameStatusPanel.add(timer);

        MainGamePanel.add(gameStatusPanel);

        playersPanel.setLayout(new java.awt.GridLayout(1, 0));

        p1p.setLayout(new javax.swing.BoxLayout(p1p, javax.swing.BoxLayout.PAGE_AXIS));

        p1.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        p1.setForeground(new java.awt.Color(255, 0, 0));
        p1.setText("PlayerLab1");
        p1.setPreferredSize(new java.awt.Dimension(20, 20));
        p1p.add(p1);

        btnPlayer1.setText("btnPlayer5");
        btnPlayer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayer1ActionPerformed(evt);
            }
        });
        p1p.add(btnPlayer1);
        p1p.add(p1Lab);

        playersPanel.add(p1p);

        p2p.setLayout(new javax.swing.BoxLayout(p2p, javax.swing.BoxLayout.PAGE_AXIS));

        p2.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        p2.setForeground(new java.awt.Color(255, 0, 0));
        p2.setText("PlayerLab5");
        p2.setPreferredSize(new java.awt.Dimension(20, 20));
        p2p.add(p2);

        btnPlayer2.setText("btnPlayer5");
        btnPlayer2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayer2ActionPerformed(evt);
            }
        });
        p2p.add(btnPlayer2);
        p2p.add(p2Lab);

        playersPanel.add(p2p);

        p3p.setLayout(new javax.swing.BoxLayout(p3p, javax.swing.BoxLayout.PAGE_AXIS));

        p3.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        p3.setForeground(new java.awt.Color(255, 0, 0));
        p3.setText("PlayerLab5");
        p3.setPreferredSize(new java.awt.Dimension(20, 20));
        p3p.add(p3);

        btnPlayer3.setText("btnPlayer5");
        btnPlayer3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayer3ActionPerformed(evt);
            }
        });
        p3p.add(btnPlayer3);
        p3p.add(p3Lab);

        playersPanel.add(p3p);

        p4p.setLayout(new javax.swing.BoxLayout(p4p, javax.swing.BoxLayout.PAGE_AXIS));

        p4.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        p4.setForeground(new java.awt.Color(255, 0, 0));
        p4.setText("PlayerLab5");
        p4.setPreferredSize(new java.awt.Dimension(20, 20));
        p4p.add(p4);

        btnPlayer4.setText("btnPlayer5");
        btnPlayer4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayer4ActionPerformed(evt);
            }
        });
        p4p.add(btnPlayer4);
        p4p.add(p4Lab);

        playersPanel.add(p4p);

        p5p.setLayout(new javax.swing.BoxLayout(p5p, javax.swing.BoxLayout.PAGE_AXIS));

        p5.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        p5.setForeground(new java.awt.Color(255, 0, 0));
        p5.setText("PlayerLab5");
        p5.setPreferredSize(new java.awt.Dimension(20, 20));
        p5p.add(p5);

        btnPlayer5.setText("btnPlayer5");
        btnPlayer5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayer5ActionPerformed(evt);
            }
        });
        p5p.add(btnPlayer5);
        p5p.add(p5Lab);

        playersPanel.add(p5p);

        MainGamePanel.add(playersPanel);

        Main.add(MainGamePanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(Main, java.awt.BorderLayout.CENTER);

        sliderBar.setLayout(new java.awt.BorderLayout());

        sidebarTab.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        sidebarTab.setFocusable(false);
        sidebarTab.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        sidebarTab.setPreferredSize(new java.awt.Dimension(300, 201));

        txtRole.setEditable(false);
        txtRole.setColumns(20);
        txtRole.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtRole.setLineWrap(true);
        txtRole.setRows(5);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtRole, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtRole, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
        );

        sidebarTab.addTab("Role", jPanel1);

        txtStats.setEditable(false);
        txtStats.setColumns(20);
        txtStats.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtStats.setLineWrap(true);
        txtStats.setRows(5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 451, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(txtStats))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(txtStats, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE))
        );

        sidebarTab.addTab("Stats", jPanel2);

        sliderBar.add(sidebarTab, java.awt.BorderLayout.CENTER);

        btnExit.setBackground(new java.awt.Color(255, 0, 0));
        btnExit.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnOptions.setBackground(new java.awt.Color(0, 153, 255));
        btnOptions.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnOptions.setText("Options");
        btnOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionsActionPerformed(evt);
            }
        });

        btnDisconnect.setBackground(new java.awt.Color(255, 102, 102));
        btnDisconnect.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnDisconnect.setText("Disconnect");
        btnDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout optionsLayout = new javax.swing.GroupLayout(options);
        options.setLayout(optionsLayout);
        optionsLayout.setHorizontalGroup(
            optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsLayout.createSequentialGroup()
                .addContainerGap(178, Short.MAX_VALUE)
                .addGroup(optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDisconnect)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(125, 125, 125))
        );

        optionsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDisconnect, btnExit, btnOptions});

        optionsLayout.setVerticalGroup(
            optionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnOptions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDisconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        optionsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDisconnect, btnExit, btnOptions});

        sliderBar.add(options, java.awt.BorderLayout.SOUTH);

        getContentPane().add(sliderBar, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    /**
     * <i>Action</i> when clicked <b>button</b>
     * @param evt 
     */
    private void btnPlayer1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayer1ActionPerformed
        // TODO add your handling code here:
        playerAction(users.get(0), (day == false) ? role : Roles.VOTE);
    }//GEN-LAST:event_btnPlayer1ActionPerformed
    /**
     * <i>Action</i> when clicked <b>button</b>
     * @param evt Click event
     */
    private void btnPlayer2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayer2ActionPerformed
        // TODO add your handling code here:
        playerAction(users.get(1), (day == false) ? role : Roles.VOTE);
    }//GEN-LAST:event_btnPlayer2ActionPerformed
    /**
     * <i>Action</i> when clicked <b>button</b>
     * @param evt Click event 
     */
    private void btnPlayer3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayer3ActionPerformed
        // TODO add your handling code here:
        playerAction(users.get(2), (day == false) ? role : Roles.VOTE);
    }//GEN-LAST:event_btnPlayer3ActionPerformed
    /**
     * <i>Action</i> when clicked <b>button</b>
     * @param evt Click event
     */
    private void btnPlayer4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayer4ActionPerformed
        // TODO add your handling code here:
    playerAction(users.get(3), (day == false) ? role : Roles.VOTE);
    }//GEN-LAST:event_btnPlayer4ActionPerformed
    /**
     * <i>Action</i> when clicked <b>button</b>
     * @param evt Click event
     */
    private void btnPlayer5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayer5ActionPerformed
        // TODO add your handling code here:
        playerAction(users.get(4), (day == false) ? role : Roles.VOTE);
    }//GEN-LAST:event_btnPlayer5ActionPerformed
    /**
     * <i>Action</i>send message when clicked <b>button</b>
     * @param evt Click event
     */
    private void txtChatFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtChatFieldKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            sendInput();
        }
    }//GEN-LAST:event_txtChatFieldKeyPressed
    /**
     * <i>Action</i>send message when clicked <b>button</b>
     * @param evt Click event
     */
    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
        sendInput();
    }//GEN-LAST:event_btnSendActionPerformed
    /**
     * <i>Action</i> when clicked <b>button</b> before disconnecting from a game
     * @param evt Click event
     */
    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectActionPerformed
        // TODO add your handling code here:

        int result = JOptionPane.showConfirmDialog(
                null, //parent
                localizedResourceBundle.getString("allForms.exitDialog"), //text
                localizedResourceBundle.getString("allForms.titleExitDialog"), //title
                JOptionPane.YES_NO_OPTION, //display options
                JOptionPane.QUESTION_MESSAGE); //icon(default questionmark)
        //null; usually for an image icon
        if (result == JOptionPane.YES_OPTION) {
            client.exit();
        }
    }//GEN-LAST:event_btnDisconnectActionPerformed

        /**
     * <i>Action</i>open <b>options</b> when clicked <b>button</b>
     * @param evt Click event
     */
    private void btnOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOptionsActionPerformed
        FormOptions options = new FormOptions();
    }//GEN-LAST:event_btnOptionsActionPerformed

        /**
     * <i>Action</i> when clicked <b>button</b> before exiting game
     * @param evt Click event
     */
    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(
                null, //parent
                localizedResourceBundle.getString("allForms.exitDialog"), //text
                localizedResourceBundle.getString("allForms.titleExitDialog"), //title
                JOptionPane.YES_NO_OPTION, //display options
                JOptionPane.QUESTION_MESSAGE); //icon(default questionmark)
        //null; usually for an image icon
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            client.exit();
            System.exit(0);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    private void tabKillersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabKillersMouseClicked

    }//GEN-LAST:event_tabKillersMouseClicked

    private void tabGlobalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabGlobalMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_tabGlobalMouseClicked
    /**
     * <i>Action</i>send killers message when clicked <b>button</b>
     * @param evt Click event
     */
    private void cbChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbChatActionPerformed
        // TODO add your handling code here:
        if (cbChat.getSelectedIndex() == 1 && user instanceof Killer) {
            txtChatField.setEditable(true);
            btnSend.setEnabled(true);
        } //if the user isn't a killer and it's night
        else if (day == false) {
            txtChatField.setEditable(false);
            btnSend.setEnabled(false);
        }
    }//GEN-LAST:event_cbChatActionPerformed

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
            java.util.logging.Logger.getLogger(FormGame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormGame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormGame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormGame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormGame().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Main;
    private javax.swing.JPanel MainGamePanel;
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnOptions;
    private javax.swing.JButton btnPlayer1;
    private javax.swing.JButton btnPlayer2;
    private javax.swing.JButton btnPlayer3;
    private javax.swing.JButton btnPlayer4;
    private javax.swing.JButton btnPlayer5;
    private javax.swing.JButton btnSend;
    private javax.swing.JComboBox cbChat;
    private javax.swing.JPanel chatActionPanel;
    private javax.swing.JPanel chatSelectorPanel;
    private javax.swing.JTabbedPane chatTabs;
    private javax.swing.JPanel chatWindow;
    private javax.swing.JPanel gameStatusPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel options;
    private javax.swing.JLabel p1;
    private javax.swing.JLabel p1Lab;
    private javax.swing.JPanel p1p;
    private javax.swing.JLabel p2;
    private javax.swing.JLabel p2Lab;
    private javax.swing.JPanel p2p;
    private javax.swing.JLabel p3;
    private javax.swing.JLabel p3Lab;
    private javax.swing.JPanel p3p;
    private javax.swing.JLabel p4;
    private javax.swing.JLabel p4Lab;
    private javax.swing.JPanel p4p;
    private javax.swing.JLabel p5;
    private javax.swing.JLabel p5Lab;
    private javax.swing.JPanel p5p;
    private javax.swing.JTextArea phaseInfo;
    private javax.swing.JPanel playersPanel;
    private javax.swing.JTabbedPane sidebarTab;
    private javax.swing.JPanel sliderBar;
    private javax.swing.JLabel statusPic;
    private javax.swing.JScrollPane tabGlobal;
    private javax.swing.JScrollPane tabKillers;
    private javax.swing.JLabel timer;
    private javax.swing.JTextField txtChatField;
    private javax.swing.JTextPane txtGlobalChat;
    private javax.swing.JTextArea txtKillersChat;
    private javax.swing.JTextArea txtRole;
    private javax.swing.JTextArea txtStats;
    // End of variables declaration//GEN-END:variables
}
