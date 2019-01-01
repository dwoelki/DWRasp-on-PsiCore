/* CHANGELOG
 * 2019-01-01   DW  Creation and implementation
 */
package de.woelki_web.dwrasp.netmanager.irc;

import de.tu_berlin.ilr.ipsm.process.IProcessArguments;

/**
 * Class-description:<br>
 * 
 * @author dominik@woelki-web.de
 */
public class DefaultRaspIRCClient extends RaspIRCClient implements IProcessArguments {

    public DefaultRaspIRCClient(String server, int port) {
        super(server, port);
    }

    @Override
    protected String getInterpreterImplementation() {
        return "de.woelki_web.dwrasp.netmanager.irc.DefaultRaspIRCClient";
    }

    @Override
    public Object execute(String[] args) {
        return "No one teached me how to answer your request -.-";
    }

    @Override
    public Object execute(String args) {
        return "No one teached me how to answer your request -.-";
    }
    
}
