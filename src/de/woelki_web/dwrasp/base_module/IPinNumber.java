/* CHANGELOG
 * 2019-01-02   DW  Creation
 * 2019-01-06   DW  Added maps for MCP23X17 port expanders
 */
package de.woelki_web.dwrasp.base_module;

import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.gpio.extension.mcp.MCP23S17Pin;
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
    
    /**
     * Pin map for Raspberry Pi.
     */
    public final static Map<String,Pin> RASPI_PIN = Collections.unmodifiableMap(new HashMap<String,Pin>()
    {
        {
            put("0", RaspiPin.GPIO_00);  put("1", RaspiPin.GPIO_01);
            put("2", RaspiPin.GPIO_02);  put("3", RaspiPin.GPIO_03);
            put("4", RaspiPin.GPIO_04);  put("5", RaspiPin.GPIO_05);
            put("6", RaspiPin.GPIO_06);  put("7", RaspiPin.GPIO_07);
            put("8", RaspiPin.GPIO_08);  put("9", RaspiPin.GPIO_09);
            put("10", RaspiPin.GPIO_10); put("11", RaspiPin.GPIO_11);
            put("12", RaspiPin.GPIO_12); put("13", RaspiPin.GPIO_13);
            put("14", RaspiPin.GPIO_14); put("15", RaspiPin.GPIO_15);
            put("16", RaspiPin.GPIO_16); put("17", RaspiPin.GPIO_17);
            put("18", RaspiPin.GPIO_18); put("19", RaspiPin.GPIO_19);
            put("20", RaspiPin.GPIO_20); put("21", RaspiPin.GPIO_21);
            put("22", RaspiPin.GPIO_22); put("23", RaspiPin.GPIO_23);
            put("24", RaspiPin.GPIO_24); put("25", RaspiPin.GPIO_25);
            put("26", RaspiPin.GPIO_26); put("27", RaspiPin.GPIO_27);
            put("28", RaspiPin.GPIO_28); put("29", RaspiPin.GPIO_29);
            put("30", RaspiPin.GPIO_30); put("31", RaspiPin.GPIO_31);
        }
    });
    
    /**
     * Pin map for port expander <i>MCP23017</i> using I2C interface.
     */
    public final static Map<String,Pin> MCP23017_PIN = Collections.unmodifiableMap(new HashMap<String,Pin>()
    {
        {
            put("A0", MCP23017Pin.GPIO_A0); put("B0", MCP23017Pin.GPIO_B0);
            put("A1", MCP23017Pin.GPIO_A1); put("B1", MCP23017Pin.GPIO_B1);
            put("A2", MCP23017Pin.GPIO_A2); put("B2", MCP23017Pin.GPIO_B2);
            put("A3", MCP23017Pin.GPIO_A3); put("B3", MCP23017Pin.GPIO_B3);
            put("A4", MCP23017Pin.GPIO_A4); put("B4", MCP23017Pin.GPIO_B4);
            put("A5", MCP23017Pin.GPIO_A5); put("B5", MCP23017Pin.GPIO_B5);
            put("A6", MCP23017Pin.GPIO_A6); put("B6", MCP23017Pin.GPIO_B6);
            put("A7", MCP23017Pin.GPIO_A7); put("B7", MCP23017Pin.GPIO_B7);
            put("A00", MCP23017Pin.GPIO_A0); put("B00", MCP23017Pin.GPIO_B0);
            put("A01", MCP23017Pin.GPIO_A1); put("B01", MCP23017Pin.GPIO_B1);
            put("A02", MCP23017Pin.GPIO_A2); put("B02", MCP23017Pin.GPIO_B2);
            put("A03", MCP23017Pin.GPIO_A3); put("B03", MCP23017Pin.GPIO_B3);
            put("A04", MCP23017Pin.GPIO_A4); put("B04", MCP23017Pin.GPIO_B4);
            put("A05", MCP23017Pin.GPIO_A5); put("B05", MCP23017Pin.GPIO_B5);
            put("A06", MCP23017Pin.GPIO_A6); put("B06", MCP23017Pin.GPIO_B6);
            put("A07", MCP23017Pin.GPIO_A7); put("B07", MCP23017Pin.GPIO_B7);
        }
    });
    
    /**
     * Pin map for port expander <i>MCP23S17</i> using SPI interface.
     */
    public final static Map<String,Pin> MCP23S17_PIN = Collections.unmodifiableMap(new HashMap<String,Pin>()
    {
        {
            put("A0", MCP23S17Pin.GPIO_A0); put("B0", MCP23S17Pin.GPIO_B0);
            put("A1", MCP23S17Pin.GPIO_A1); put("B1", MCP23S17Pin.GPIO_B1);
            put("A2", MCP23S17Pin.GPIO_A2); put("B2", MCP23S17Pin.GPIO_B2);
            put("A3", MCP23S17Pin.GPIO_A3); put("B3", MCP23S17Pin.GPIO_B3);
            put("A4", MCP23S17Pin.GPIO_A4); put("B4", MCP23S17Pin.GPIO_B4);
            put("A5", MCP23S17Pin.GPIO_A5); put("B5", MCP23S17Pin.GPIO_B5);
            put("A6", MCP23S17Pin.GPIO_A6); put("B6", MCP23S17Pin.GPIO_B6);
            put("A7", MCP23S17Pin.GPIO_A7); put("B7", MCP23S17Pin.GPIO_B7);
            put("A00", MCP23S17Pin.GPIO_A0); put("B00", MCP23S17Pin.GPIO_B0);
            put("A01", MCP23S17Pin.GPIO_A1); put("B01", MCP23S17Pin.GPIO_B1);
            put("A02", MCP23S17Pin.GPIO_A2); put("B02", MCP23S17Pin.GPIO_B2);
            put("A03", MCP23S17Pin.GPIO_A3); put("B03", MCP23S17Pin.GPIO_B3);
            put("A04", MCP23S17Pin.GPIO_A4); put("B04", MCP23S17Pin.GPIO_B4);
            put("A05", MCP23S17Pin.GPIO_A5); put("B05", MCP23S17Pin.GPIO_B5);
            put("A06", MCP23S17Pin.GPIO_A6); put("B06", MCP23S17Pin.GPIO_B6);
            put("A07", MCP23S17Pin.GPIO_A7); put("B07", MCP23S17Pin.GPIO_B7);
        }
    });
    
    
    
}
