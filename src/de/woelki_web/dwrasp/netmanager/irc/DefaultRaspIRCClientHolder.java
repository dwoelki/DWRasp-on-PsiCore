/* CHANGELOG
 * 2019-01-01   DW  Creation as extension of RaspIRCClientHolder
 * 
 */

package de.woelki_web.dwrasp.netmanager.irc;

import de.tu_berlin.ilr.ipsm.netmanager.irc.IRCClient;
import de.tu_berlin.ilr.ipsm.netmanager.irc.IRCClientHolder;
import de.tu_berlin.ilr.ipsm.settings.PreSettings;

final public class DefaultRaspIRCClientHolder extends IRCClientHolder {
    
    public DefaultRaspIRCClientHolder() {
        super();
    }
    
    /**
     * Returns just a fresh instance of
     * {@link de.tu_berlin.ilr.ipsm.netmanager.irc.IPSMCoreIRCClient}.
     * 
     * @param serverName URI to the IRC server
     * @param serverPort port to be used for IRC server connection
     * @return <b>IRCClient</b> new generated object from type
     * {@link de.tu_berlin.ilr.ipsm.netmanager.irc.IPSMCoreIRCClient}
     */
    @Override
    protected IRCClient generateClient(String serverName, int serverPort) {
        final IRCClient _client = new DefaultRaspIRCClient(serverName, serverPort);
        RaspIRCClient.ACTIVE_CLIENT = (DefaultRaspIRCClient)_client;
        return _client;
    }
        
}