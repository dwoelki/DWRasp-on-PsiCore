/* CHANGELOG
 * 2019-01-01   DW  Created as dummy
 */

package de.woelki_web.dwrasp.netmanager;

import de.tu_berlin.ilr.ipsm.util.UtilBox;
import de.tu_berlin.ilr.ipsm.util.netmanager.IClientManager;

/**
 * Class-description:<br>
 * 
 * 
 *
 * @author dominik@woelki-web.de
 */
public class RaspClientManager implements IClientManager {

    @Override
    public void connectToHost() {
        //nothing yet
        UtilBox.Console.outln("info: feature IPSM_NM not yet implemented.");
        return;
    }

    @Override
    public void disconnectFromHost() {
        //nothing yet
        return;
    }

    @Override
    public boolean isConnectedWithHost() {
        //nothing yet
        UtilBox.Console.outln("info: feature IPSM_NM not yet implemented.");
        return false;
    }

    @Override
    public boolean submitRequestToHost(Object _requestObject) {
        //nothing yet
        UtilBox.Console.outln("info: feature IPSM_NM not yet implemented, hence no request submission possible.");
        return false;
    }
    
}
