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

import de.tu_berlin.ilr.ipsm.netmanager.irc.InterpretingIRCClient;

abstract public class RaspIRCClient extends InterpretingIRCClient {
    
    final   static  String  INTERPRETER_IMPL = "de.tu_berlin.ilr.ipsm.core.remote.IRCInterpreter";
    static  RaspIRCClient   ACTIVE_CLIENT;
    
    RaspIRCClient(String server, int port) {
        super(server, port, INTERPRETER_IMPL);
    }
    
    abstract protected String getInterpreterImplementation();
    
    @Override
    public void ignite() {
        loadPermissions();
        join(channel);
        msg("Hello, I am an IRC bot running DWRasp");
        msg("My name is "+nickName);
        msg("Call me by sending a message with my name "+nickName+" and a command to the group chat or a private message");
        msg("You may ask me for help whenever you need to by sending a message with the message \""+nickName+" help\"");
    }
    
    @Override
    protected boolean helpMessage(final String _caller, String _helpSpecifier) {
        privmsg(_caller, interprete(_caller, "help"));
        return true;
    }

}