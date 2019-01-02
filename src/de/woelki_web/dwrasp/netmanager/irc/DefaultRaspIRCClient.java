/* CHANGELOG
 * 2019-01-01   DW  Creation and implementation
 */
package de.woelki_web.dwrasp.netmanager.irc;

/**
 * Class-description:<br>
 * 
 * @author dominik@woelki-web.de
 */
public class DefaultRaspIRCClient extends RaspIRCClient {

    public DefaultRaspIRCClient(String server, int port) {
        super(server, port, "de.woelki_web.dwrasp.netmanager.irc.DefaultRaspIRCInterpreter");
    }

    @Override
    protected String getInterpreterImplementation() {
        return "de.woelki_web.dwrasp.netmanager.irc.DefaultRaspIRCInterpreter";
    }
    
}
