/* CHANGELOG
 * 2019-01-06   DW  Creation
 * 2019-01-06   DW  Test loading pin configuration from XML with port expander
 *                  and turn on/off
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
public class Test_PinInitializationFromXML {
    
    public static void main(String[] args) {
        System.out.println("-->>");
        
        QuickGPIO TEST = new QuickGPIO();
        
        TEST.loadPinsFromXML("/home/pi/dev/DWRaspBase/pin_test.xml");
        
        for (int i=0; i<7; i++) {
            try {
                pause(i);
                System.out.println("PINS high state");
                for (String k : TEST.getPinNames()) {
                    TEST.setPinState(k, true);
                }
                pause(i);
                System.out.println("PINS low state");
                for (String k : TEST.getPinNames()) {
                    TEST.setPinState(k, false);
                }
            }
            catch(Exception ex) {
                System.out.println("\t"+ex.getClass().getName());
                System.out.println("\t"+ex.getMessage());
            }
        }
        
        
        System.out.println("<<--");
    }
    
    private static void pause(int i) {
        UtilBox.delay((i+1)*300);
    }
    
}
