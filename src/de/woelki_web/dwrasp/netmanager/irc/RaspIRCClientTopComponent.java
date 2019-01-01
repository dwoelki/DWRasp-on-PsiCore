/* CHANGELOG
 * 2019-01-01   DW  Created as copy from IPSM_NetManagerGUI's IRC top component
 * 2019-01-01   DW  Fit to basic requirements of DWRaspBase IRC communications
 */

package de.woelki_web.dwrasp.netmanager.irc;

import de.tu_berlin.ilr.ipsm.netmanager.irc.IRCClient;
import de.tu_berlin.ilr.ipsm.outputs.IConsoleOutput;
import de.tu_berlin.ilr.ipsm.settings.GlobalSettings;
import de.tu_berlin.ilr.ipsm.settings.PreSettings;
import de.tu_berlin.ilr.ipsm.util.ConsoleTab;
import de.tu_berlin.ilr.ipsm.util.LookupInstantiator;
import de.tu_berlin.ilr.ipsm.util.UtilBox;
import de.tu_berlin.ilr.ipsm.util.netmanager.IClientManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Class-description:<br>
 * This is the top component window of the default IRC-client.
 * 
 * @author ILR TU Berlin , dominik.woelki@ilr.tu-berlin.de
 */
@ConvertAsProperties(
        dtd = "-//de.woelki_web.dwrasp.netmanager.irc//RaspIRCClientTopComponent//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "RaspIRC-Client",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "de.woelki_web.dwrasp.netmanager.irc.RaspIRCClientTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_RaspIRCClientTopComponent",
        preferredID = "RaspIRC-Client"
)
@Messages({
    "CTL_RaspIRCClientTopComponentAction=RaspIRC-Client",
    "CTL_RaspIRCClientTopComponent=RaspIRC-Client",
    "HINT_RaspIRCClientTopComponent=Monitors the default RaspIRC-Client"
})
public final class RaspIRCClientTopComponent extends TopComponent {

    final public static     String          IRC_CONSOLE_NAME  =  "IRC-Monitor";
    final private           List<String>    RECENT_MSGS;
    private                 IConsoleOutput  console;
    private                 Thread          firstIRC;
    private                 int             index = -1;
    
    
    public RaspIRCClientTopComponent() {
        initComponents();
        initUpdateComponents();
        initListeners();
        initToolTips();
        try {
            initIRC();
        } catch(Exception ex) { /*nothing*/ }
        RECENT_MSGS = new java.util.ArrayList<>();
        setName(Bundle.CTL_RaspIRCClientTopComponent());
        setToolTipText(Bundle.HINT_RaspIRCClientTopComponent());
    }
    
    private void initUpdateComponents() {
        //add console
        UtilBox.addConsole(IRC_CONSOLE_NAME);
        console = UtilBox.getConsole(IRC_CONSOLE_NAME);
        if (console instanceof ConsoleTab) {
            this.jTabbedPaneChat.add(IRC_CONSOLE_NAME,(ConsoleTab)console);
            this.jTabbedPaneChat.repaint();
            ((ConsoleTab)console).setAutoScrollDown(true);
        }
        //clean nicknames combo box
        this.jComboBoxNicknames.removeAllItems();
        this.jComboBoxNicknames.addItem("Click to refresh!");
        this.jComboBoxNicknames.setSelectedIndex(0);
    }
    
    private void initListeners() {
        //IRC control
        final ActionListener l_irc = new ActionListener() {
            boolean lock = false;
            Thread ircCT;
            @Override public void actionPerformed(ActionEvent e) {
                if (!lock) {
                    lock = true;
                    final boolean _buttonState = jButtonIRCClient.isSelected();
                    ircCT = new Thread() {
                        @Override public void run() {
                            try {
                                if (_buttonState)
                                    RaspIRCClientHolder.startIRCclient();
                                else
                                    RaspIRCClientHolder.stopIRCclient();
                                //new IRCClientStartStop().actionPerformed(e);
                            } catch(Exception ex) { /*nothing to handle here*/ }
                            try {
                                this.sleep(2000);
                            } catch (InterruptedException ex) { /*nothing*/ }
                        }
                    };
                    ircCT.setDaemon(true);
                    ircCT.start();
                    try {
                        ircCT.join(2000);
                    } catch (InterruptedException ex) { /*nothing*/ }
                    lock = false;
                }
            }
        };
        jButtonIRCClient.addActionListener(l_irc);
        final ActionListener l_ircDebug = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                de.tu_berlin.ilr.ipsm.netmanager.irc.IRCClient.DEBUG = jCheckBoxIRCDebug.isSelected();
            }
        };
        jCheckBoxIRCDebug.addActionListener(l_ircDebug);
        //Send button
        final ActionListener l_send = new ActionListener() {
            boolean lock = false;
            @Override public void actionPerformed(ActionEvent e) {
                if (!lock) {
                    lock = true;
                    final String message = jTextFieldMsg.getText();
                    try {
                        if (message.trim().length()==0) {
                            //nothing, since no message
                        }
                        if (message.length()>IRCClient.MAX_MSG_LENGTH) {
                            console.errln("Message too long! (maxChars="+IRCClient.MAX_MSG_LENGTH+")");
                        }
                        else {
                            String recipient = jComboBoxNicknames.getSelectedItem().toString();
                            if (recipient.equals("Click to refresh!"))
                                recipient = "CHANNEL";
                            final boolean sent = RaspIRCClientHolder.tunnelMessage(recipient, message);
                            if (!sent) {
                                console.errln("Message could not be send to "+recipient+"!");
                            }
                            else {
                                index = -1;
                                jTextFieldMsg.setText("");
                                final String _chosen = jComboBoxNicknames.getSelectedItem().toString();
                                if (_chosen!=null) {
                                    if ((_chosen.length()>0) && (!_chosen.equals("CHANNEL")) && (!_chosen.equals("Click to refresh!")))
                                        jTextFieldMsg.setText(_chosen+" ");
                                }
                                RECENT_MSGS.add(0, message);
                                if (RECENT_MSGS.size()>8)
                                    RECENT_MSGS.remove(8);
                            }
                        }
                    }
                    catch(Exception ex) {
                        console.errln("Message could not be send!");
                    }
                    lock = false;
                }
                else {
                    //currently locked
                }
            }
        };
        jButtonSendMsg.addActionListener(l_send);
        final KeyListener k_sendMsg = new KeyListener() {
            boolean lock  = false;
            @Override public void keyTyped(KeyEvent e) { /*nothing*/ }
            @Override public void keyPressed(KeyEvent e) {
                if (!lock) {
                    if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                        lock = true;
                        l_send.actionPerformed(null);
                        lock = false;
                    }
                    else if (e.getKeyCode()==KeyEvent.VK_UP) {
                        lock = true;
                        index++;
                        if (index>=RECENT_MSGS.size())
                            index = 0;
                        try {
                            jTextFieldMsg.setText(RECENT_MSGS.get(index));
                        } catch(RuntimeException e_index) { /*nothing*/ }
                        lock = false;
                    }
                    else if (e.getKeyCode()==KeyEvent.VK_DOWN) {
                        lock = true;
                        index--;
                        if (index<0)
                            index = RECENT_MSGS.size()-1;
                        try {
                            jTextFieldMsg.setText(RECENT_MSGS.get(index));
                        } catch(RuntimeException e_index) { /*nothing*/ }
                        lock = false;
                    }
                }
            }
            @Override public void keyReleased(KeyEvent e) { /*nothing*/ }
        };
        jTextFieldMsg.addKeyListener(k_sendMsg);
        
        //Nicknames selection
        final PopupMenuListener l_nickUpdate = new PopupMenuListener() {
            boolean lock = false;
            @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (!lock) {
                    lock = true;
                    try {
                        updateNicknames();
                    }
                    catch(Exception ex) {
                        //nothing
                    }
                    lock = false;
                }
            }

            @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { /*nothing*/ }
            
            @Override public void popupMenuCanceled(PopupMenuEvent e) { /*nothing*/ }
        };
        jComboBoxNicknames.addPopupMenuListener(l_nickUpdate);

    }
    
    private void initToolTips() {
        jTextFieldMsg.setToolTipText("Enter your message and send it by typing [ENTER]");
        jButtonSendMsg.setToolTipText("Sends the entered message");
        jComboBoxNicknames.setToolTipText("Select your message's recipient: One specific user or the entire channel");
        jButtonIRCClient.setToolTipText("Connects/disconnects the IRC client. This may take some seconds");
        jCheckBoxIRCDebug.setToolTipText("Debug mode prints the raw/unparsed incoming messages to the chat area");
    }
    
    private void initIRC() {
        final IClientManager _managerObject;
        if (GlobalSettings.DEFAULT_SERVICES.get(IClientManager.class.getName())!=null)
            _managerObject = (IClientManager)LookupInstantiator.instantiateViaLookup(IClientManager.class, GlobalSettings.DEFAULT_SERVICES.get(IClientManager.class.getName()));
        else
            _managerObject = (IClientManager)LookupInstantiator.instantiateViaLookup(IClientManager.class);
        firstIRC = new Thread() {
            @Override public void run() {
                try {
                    sleep(10000);
                } catch(Exception ex) { /*nothing*/ }
                try {
                    IRCClient.registerDefaultIPSMNMClientManager(_managerObject);
                } catch(Exception ex) { /*nothing*/ }
                if (PreSettings.IPSMircConnectOnStart)
                    try {
                        if (!jButtonIRCClient.isSelected()) {
                            jButtonIRCClient.setSelected( RaspIRCClientHolder.startIRCclient() );
                        }
                    } catch(Exception ex) { /*nothing*/ }
                }
            };
        firstIRC.setDaemon(true);
        firstIRC.start();
    }
    
    private void updateNicknames() {
        this.jComboBoxNicknames.removeAllItems();
        if (!RaspIRCClientHolder.isConnected())
            return;
        jComboBoxNicknames.addItem("CHANNEL");
        for (String k : RaspIRCClientHolder.getRecipients())
            jComboBoxNicknames.addItem(k);
        this.jComboBoxNicknames.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonIRCClient = new javax.swing.JToggleButton();
        jCheckBoxIRCDebug = new javax.swing.JCheckBox();
        jComboBoxNicknames = new javax.swing.JComboBox<>();
        jTextFieldMsg = new javax.swing.JTextField();
        jButtonSendMsg = new javax.swing.JButton();
        jLabelMsg = new javax.swing.JLabel();
        jLabelRecipent = new javax.swing.JLabel();
        jTabbedPaneChat = new javax.swing.JTabbedPane();

        org.openide.awt.Mnemonics.setLocalizedText(jButtonIRCClient, org.openide.util.NbBundle.getMessage(RaspIRCClientTopComponent.class, "RaspIRCClientTopComponent.jButtonIRCClient.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxIRCDebug, org.openide.util.NbBundle.getMessage(RaspIRCClientTopComponent.class, "RaspIRCClientTopComponent.jCheckBoxIRCDebug.text")); // NOI18N
        jCheckBoxIRCDebug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxIRCDebugActionPerformed(evt);
            }
        });

        jComboBoxNicknames.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextFieldMsg.setText(org.openide.util.NbBundle.getMessage(RaspIRCClientTopComponent.class, "RaspIRCClientTopComponent.jTextFieldMsg.text")); // NOI18N
        jTextFieldMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMsgActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonSendMsg, org.openide.util.NbBundle.getMessage(RaspIRCClientTopComponent.class, "RaspIRCClientTopComponent.jButtonSendMsg.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelMsg, org.openide.util.NbBundle.getMessage(RaspIRCClientTopComponent.class, "RaspIRCClientTopComponent.jLabelMsg.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelRecipent, org.openide.util.NbBundle.getMessage(RaspIRCClientTopComponent.class, "RaspIRCClientTopComponent.jLabelRecipent.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelRecipent)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxNicknames, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                        .addComponent(jButtonIRCClient)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxIRCDebug, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTabbedPaneChat)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelMsg)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldMsg)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonSendMsg)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneChat, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMsg)
                    .addComponent(jTextFieldMsg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSendMsg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRecipent)
                    .addComponent(jComboBoxNicknames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxIRCDebug)
                    .addComponent(jButtonIRCClient))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxIRCDebugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxIRCDebugActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxIRCDebugActionPerformed

    private void jTextFieldMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMsgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMsgActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton jButtonIRCClient;
    private javax.swing.JButton jButtonSendMsg;
    private javax.swing.JCheckBox jCheckBoxIRCDebug;
    private javax.swing.JComboBox<String> jComboBoxNicknames;
    private javax.swing.JLabel jLabelMsg;
    private javax.swing.JLabel jLabelRecipent;
    private javax.swing.JTabbedPane jTabbedPaneChat;
    private javax.swing.JTextField jTextFieldMsg;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
