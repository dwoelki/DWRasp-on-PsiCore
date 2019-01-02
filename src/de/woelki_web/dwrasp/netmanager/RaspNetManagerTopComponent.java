/* CHANGELOG
 * 2019-01-02   DW  Creation
 * 2019-01-02   DW  First, rudimentary implementation
 */
package de.woelki_web.dwrasp.netmanager;

import de.tu_berlin.ilr.ipsm.gui.output.ConsoleWriterTopComponent;
import de.tu_berlin.ilr.ipsm.netmanager.Client;
import de.tu_berlin.ilr.ipsm.netmanager.Host;
import de.tu_berlin.ilr.ipsm.netmanager.NetManagerOutput;
import de.tu_berlin.ilr.ipsm.outputs.IConsoleOutput;
import de.tu_berlin.ilr.ipsm.settings.PreSettings;
import de.tu_berlin.ilr.ipsm.util.ConsoleTab;
import de.tu_berlin.ilr.ipsm.util.StringManager;
import de.tu_berlin.ilr.ipsm.util.TextFileManager;
import de.tu_berlin.ilr.ipsm.util.UtilBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//de.woelki_web.dwrasp.netmanager//RaspNetManager//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "RaspNetManagerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "de.woelki_web.dwrasp.netmanager.RaspNetManagerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_RaspNetManagerAction",
        preferredID = "RaspNetManagerTopComponent"
)
@Messages({
    "CTL_RaspNetManagerAction=RaspNetManager",
    "CTL_RaspNetManagerTopComponent=RaspNetManager Window",
    "HINT_RaspNetManagerTopComponent=This is a RaspNetManager window"
})
public final class RaspNetManagerTopComponent extends TopComponent {

    private         boolean         lock;
    private         Thread          NMST;
    
    public RaspNetManagerTopComponent() {
        initComponents();
        setName(Bundle.CTL_RaspNetManagerTopComponent());
        setToolTipText(Bundle.HINT_RaspNetManagerTopComponent());

        lock = false;
        initListeners();
        try {
            try {
                de.tu_berlin.ilr.ipsm.util.SettingsLoader.loadPreSettingsFromFile();
            }
            catch(Exception ex1) {
                String chosenFile ="locate_settings.dat";
                if (new java.io.File(chosenFile).exists()) {
                    final String[] _location = new TextFileManager(chosenFile, "lined").GetLinedContent();
                    chosenFile = _location[0];
                }
                else {
                    final JFileChooser fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fc.setDialogTitle("Settings file not found! Please select!");
                    int fcReturnValue = fc.showOpenDialog(null);
                    chosenFile = fc.getSelectedFile().toString();
                    chosenFile = new java.io.File(chosenFile).getAbsolutePath();
                    if (fcReturnValue == JFileChooser.APPROVE_OPTION) {
                        //everything ok, since file chosen
                    }
                    else {
                        UtilBox.Console.errln("Presettings not properly loaded!");
                        UtilBox.ErrLog.outln("Presettings not properly loaded!");
                    }
                }
                PreSettings.LoadPreSettingsFile = StringManager.FileParser(chosenFile);
                de.tu_berlin.ilr.ipsm.util.SettingsLoader.loadPreSettingsFromFile();
                TextFileManager.writeLinedFile("locate_settings.dat", new String[] {chosenFile}, true);
            }
            initNetManagerServer();
        }
            catch(RuntimeException ex2) {
            UtilBox.ErrLog.errStackTrace(ex2,10);
        }
    }
    
    private void initNetManagerServer() {
        NMST = new Thread() {
            @Override
                public void run() {
                    try {
                        sleep(5000);
                    } catch (InterruptedException ex) { /*nothing*/ }
                    UtilBox.addConsole(NetManagerOutput.DEFAULT_TAB_NAME);
                    IConsoleOutput _serverConsoleTab = (ConsoleTab)UtilBox.getConsole(NetManagerOutput.DEFAULT_TAB_NAME);
                    if (_serverConsoleTab instanceof ConsoleTab)
                        java.awt.EventQueue.invokeLater(
                            new Runnable() {
                                public void run() {
                                    ConsoleWriterTopComponent.findInstance().addTab((ConsoleTab)_serverConsoleTab);
                                }
                            }
                        );
                    if (Host.RUN_HOST)
                        Host.startHost();
                }
        };
        NMST.start();
        try {
            NMST.join(5000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private void initListeners() {
        ActionListener l_connectClient = new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!lock) {
                    try {
                        if (Client.actualClient==null)
                            Client.actualClient = new Client();
                        Client.actualClient.startClient();
                    } catch(Exception ex) {
                        lock = false;
                    }
                }
            }
        };
        this.jButtonConnectClient.addActionListener(l_connectClient);
        
        ActionListener l_disconnectClient = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!lock) {
                    try {
                        Client.actualClient.closeClient();
                    } catch(Exception ex) {
                        lock = false;
                    }
                }
            }
        };
        this.jButtonDisconnectClient.addActionListener(l_disconnectClient);
        
        ActionListener l_connectHost = new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!lock) {
                    try {
                        Host.startHost();
                    } catch(Exception ex) {
                        lock = false;
                    }
                }
            }
        };
        this.jButtonConnectHost.addActionListener(l_connectHost);
        
        ActionListener l_disconnectHost = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!lock) {
                    try {
                        Host.stopHost();
                    } catch(Exception ex) {
                        lock = false;
                    }
                }
            }
        };
        this.jButtonDisconnectHost.addActionListener(l_disconnectHost);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonConnectClient = new javax.swing.JButton();
        jButtonDisconnectClient = new javax.swing.JButton();
        jLabelClient = new javax.swing.JLabel();
        jButtonConnectHost = new javax.swing.JButton();
        jButtonDisconnectHost = new javax.swing.JButton();
        jLabelHost = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jButtonConnectClient, org.openide.util.NbBundle.getMessage(RaspNetManagerTopComponent.class, "RaspNetManagerTopComponent.jButtonConnectClient.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonDisconnectClient, org.openide.util.NbBundle.getMessage(RaspNetManagerTopComponent.class, "RaspNetManagerTopComponent.jButtonDisconnectClient.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelClient, org.openide.util.NbBundle.getMessage(RaspNetManagerTopComponent.class, "RaspNetManagerTopComponent.jLabelClient.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonConnectHost, org.openide.util.NbBundle.getMessage(RaspNetManagerTopComponent.class, "RaspNetManagerTopComponent.jButtonConnectHost.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonDisconnectHost, org.openide.util.NbBundle.getMessage(RaspNetManagerTopComponent.class, "RaspNetManagerTopComponent.jButtonDisconnectHost.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelHost, org.openide.util.NbBundle.getMessage(RaspNetManagerTopComponent.class, "RaspNetManagerTopComponent.jLabelHost.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelClient)
                    .addComponent(jButtonConnectClient, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDisconnectClient)
                    .addComponent(jLabelHost)
                    .addComponent(jButtonConnectHost, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDisconnectHost))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelClient)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonConnectClient)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDisconnectClient)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelHost)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonConnectHost)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDisconnectHost)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonConnectClient;
    private javax.swing.JButton jButtonConnectHost;
    private javax.swing.JButton jButtonDisconnectClient;
    private javax.swing.JButton jButtonDisconnectHost;
    private javax.swing.JLabel jLabelClient;
    private javax.swing.JLabel jLabelHost;
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
