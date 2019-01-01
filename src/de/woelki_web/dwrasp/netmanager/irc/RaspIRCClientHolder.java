/* CHANGELOG
 * 2019-01-01   DW  Creation as extension of IRCClientHolder
 * 
 */

package de.woelki_web.dwrasp.netmanager.irc;

import de.tu_berlin.ilr.ipsm.netmanager.irc.IRCClientHolder;
import de.tu_berlin.ilr.ipsm.settings.GlobalSettings;
import de.tu_berlin.ilr.ipsm.settings.PreSettings;
import de.tu_berlin.ilr.ipsm.util.LookupInstantiator;
import de.tu_berlin.ilr.ipsm.util.UtilBox;

abstract public class RaspIRCClientHolder extends IRCClientHolder {
    
    public RaspIRCClientHolder() {
        serverName      =   PreSettings.IPSMircServer;
        serverPort      =   PreSettings.IPSMircPort;
        channel         =   PreSettings.IPSMircChannel;
        channelKey      =   PreSettings.IPSMircChannelKey;
        nickName        =   PreSettings.IPSMircNickName;
        userName        =   "DWRasp-IRCClient";   //PreSettings.IPSMircUserName;
        realName        =   PreSettings.IPSMircUserName;
    }
    
    synchronized public static boolean startIRCclient() {
        boolean first = true;
        if (IRCT!=null) {
            if (IRCT.isAlive()) {
                return false;
            }
            else {
                first = false;
            }
        }
        if (first) {
            UtilBox.addConsole("IRC-Monitor");
        }
        final RaspIRCClientHolder _holderObject;
        if (GlobalSettings.DEFAULT_SERVICES.get(RaspIRCClientHolder.class.getName())!=null)
            _holderObject = (RaspIRCClientHolder)LookupInstantiator.instantiateViaLookup(RaspIRCClientHolder.class, GlobalSettings.DEFAULT_SERVICES.get(RaspIRCClientHolder.class.getName()));
        else
            _holderObject = (RaspIRCClientHolder)LookupInstantiator.instantiateViaLookup(RaspIRCClientHolder.class);
        
        IRCT = _holderObject;
        IRCT.setDaemon(true);
        IRCT.start();
        return true;
    }
    
        
}