/* CHANGELOG
 * 2019-01-01   DW  Created as copy of IPSMCoreIRCClient
 * 2019-01-01   DW  Made the interpreter implementation String a method, which
 *                  can be overwritten by inheriting classes. Hence this class
 *                  is not final as its original template IPSMCoreIRCClient
 */

/*
 THIS CLASS AND ITS RELATIVES ARE NO PROPERTY OF ILR TU BERLIN.
 This class has been downloaded from https://gist.github.com/kaecy .
 All usage permissions are reserved to the original author.
 This class and its relatives must not be content of a commercial
 IPSM build.
 */

package de.woelki_web.dwrasp.netmanager.irc;

import de.tu_berlin.ilr.ipsm.globals.Constants;
import de.tu_berlin.ilr.ipsm.netmanager.irc.InterpretingIRCClient;

abstract public class RaspIRCClient extends InterpretingIRCClient {
    
    final   static  String  INTERPRETER_IMPL = "de.woelki_web.dwrasp.netmanager.irc.DefaultRaspIRCClient";
    static  RaspIRCClient   ACTIVE_CLIENT;
    
    RaspIRCClient(String server, int port) {
        super(server, port, INTERPRETER_IMPL);
    }
    
    RaspIRCClient(String server, int port, String interprImpl) {
        super(server, port, interprImpl);
    }
    
    abstract protected String getInterpreterImplementation();
    
    @Override
    public void ignite() {
        INDIVIDUAL_ATTENTION_TIME = 2*Constants.hour;
        GENERAL_ATTENTION_TIME    = 30*Constants.min;
        loadPermissions();
        join(channel);
        msg("Hello, I am an IRC bot running DWRasp");
        msg("My name is "+nickName);
        msg("Call me by sending a message with my name "+nickName+" and a command to the group chat or a private message");
        msg("You may ask me for help whenever you need to by sending a message with the message \""+nickName+" help\"");
        msg("-----.......");
        msg("My general attention time with \"!\" is "+GENERAL_ATTENTION_TIME+" seconds");
        msg("My individual attention time with \"!"+nickName+"\" is "+GENERAL_ATTENTION_TIME+" seconds");
    }
    
    @Override
    protected boolean helpMessage(final String _caller, String _helpSpecifier) {
        privmsg(_caller, interprete(_caller, "help"));
        return true;
    }

}