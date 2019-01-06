/* CHANGELOG
 * 2019-01-02   DW  Creation
 * 2019-01-02   DW  Test looping through pins and switching on/off as output
 */
package de.woelki_web.dwrasp.test_base_module;

import de.tu_berlin.ilr.ipsm.util.UtilBox;
import de.woelki_web.dwrasp.base_module.QuickGPIO;

/**
 * Test-class-description:<br>
 * 
 * 
 * @author dominik@woelki-web.de
 */
public class Test_BasicPinControl {
    
    public static void main(String[] args) {
        System.out.println("-->>");
        
        QuickGPIO TEST = new QuickGPIO();
        
        for (int i=0; i<32; i++) {
            try {
                pause();
                System.out.println("PIN "+i);
                TEST.initPin("pin"+i, String.valueOf(i), true);
                System.out.println("\tinitialized");
                TEST.setPinState("pin"+i, true);
                pause();
                TEST.setPinState("pin"+i, false);
            }
            catch(Exception ex) {
                System.out.println("\t"+ex.getClass().getName());
                System.out.println("\t"+ex.getMessage());
            }
        }
        
        
        System.out.println("<<--");
    }
    
    private static void pause() {
        UtilBox.delay(1500);
    }
    
}
