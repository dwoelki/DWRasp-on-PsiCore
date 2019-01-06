/* CHANGELOG
 * 2019-01-02   DW  Creation
 */
package de.woelki_web.dwrasp.base_module;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface-description:<br>
 * 
 * 
 * @author dominik@woelki-web.de
 */
public interface IPinNumber {
    
    public final static Map<Integer,Pin> PIN_NUMBER = Collections.unmodifiableMap(new HashMap<Integer,Pin>()
    {
        {
            put(1, RaspiPin.GPIO_01);   put(2, RaspiPin.GPIO_02);
            put(3, RaspiPin.GPIO_03);   put(4, RaspiPin.GPIO_04);
            put(5, RaspiPin.GPIO_05);   put(6, RaspiPin.GPIO_06);
            put(7, RaspiPin.GPIO_07);   put(8, RaspiPin.GPIO_08);
            put(9, RaspiPin.GPIO_09);   put(10, RaspiPin.GPIO_10);
            put(11, RaspiPin.GPIO_11);  put(12, RaspiPin.GPIO_12);
            put(13, RaspiPin.GPIO_13);  put(14, RaspiPin.GPIO_14);
            put(15, RaspiPin.GPIO_15);  put(16, RaspiPin.GPIO_16);
            put(17, RaspiPin.GPIO_17);  put(18, RaspiPin.GPIO_18);
            put(19, RaspiPin.GPIO_19);  put(20, RaspiPin.GPIO_20);
            put(21, RaspiPin.GPIO_21);  put(22, RaspiPin.GPIO_22);
            put(23, RaspiPin.GPIO_23);  put(24, RaspiPin.GPIO_24);
            put(25, RaspiPin.GPIO_25);  put(26, RaspiPin.GPIO_26);
            put(27, RaspiPin.GPIO_27);  put(28, RaspiPin.GPIO_28);
            put(29, RaspiPin.GPIO_29);  put(30, RaspiPin.GPIO_30);
            put(31, RaspiPin.GPIO_31);  put(0, RaspiPin.GPIO_00);
        }
    });
    
}
