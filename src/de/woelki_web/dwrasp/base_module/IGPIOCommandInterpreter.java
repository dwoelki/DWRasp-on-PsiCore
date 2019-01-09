/* CHANGELOG
 * 2019-01-07   DW  Creation
 */
package de.woelki_web.dwrasp.base_module;

/**
 * Interface-description:<br>
 * 
 * @author doom@ls-clan.net
 */
public interface IGPIOCommandInterpreter {
    
    public String getImplName();
    public boolean interpreteMsg(String _msg);
}
