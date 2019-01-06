/* CHANGELOG
 * 2019-01-02   DW  Creation
 * 2019-01-02   DW  fs
 */
package de.woelki_web.dwrasp.base_module;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.*;
import com.pi4j.io.gpio.event.*;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.spi.SpiChannel;
import de.tu_berlin.ilr.ipsm.exceptions.InitException;
import de.tu_berlin.ilr.ipsm.outputs.ITreeNode;
import de.tu_berlin.ilr.ipsm.util.DOMXMLTree1;
import de.tu_berlin.ilr.ipsm.util.UtilBox;
import java.util.HashMap;
import static de.woelki_web.dwrasp.base_module.IPinNumber.RASPI_PIN;
import java.io.IOException;
import java.util.List;
import org.openide.util.Exceptions;

/**
 * Class-description:<br>
 * 
 * 
 * @author dominik@woelki-web.de
 */
public class QuickGPIO {
    
    final   private     GpioController                      INST;
    final   private     HashMap<String, GpioProvider>       CHIP_INST;
    final   private     HashMap<String, GpioPinDigital>     PINS;
    
    
    public QuickGPIO() {
        INST        =   GpioFactory.getInstance();
        CHIP_INST   =   new HashMap<>();
        PINS        =   new HashMap<>();
    }
    
    public List<String> getPinNames() {
        final List<String> _list = new java.util.ArrayList<>();
        for (String k : PINS.keySet())
            _list.add(k);
        return _list;
    }
    
    public boolean registerChip(String _id, String _type) {
        try {
            final GpioProvider _chip;
            if (_type.equalsIgnoreCase("MCP23017")) {
                try {
                    _chip = new MCP23017GpioProvider(I2CFactory.getInstance(I2CFactory.getBusIds()[0]), 0x20);
                } catch (IOException ex) {
                    throw ex;
                } catch (I2CFactory.UnsupportedBusNumberException ex) {
                    throw ex;
                }
            }
            else if (_type.equalsIgnoreCase("MCP23S17")) {
                _chip = new MCP23S17GpioProvider(MCP23S17GpioProvider.DEFAULT_ADDRESS, SpiChannel.CS0);
            }
            else {
                throw new de.tu_berlin.ilr.ipsm.exceptions.InitException("Chip type \""+_type+"\" not supported!");
            }
            CHIP_INST.put(_id, _chip);
            return true;
        }
        catch(Exception ex) {
            UtilBox.ErrLog.errStackTrace(ex,10);
            return false;
        }
    }
    
    public boolean initPin(String _id, String _number, boolean _isOutput) {
        return initPin(_id, _number, _isOutput, false);
    }
    
    public boolean initPin(String _id, String _number, boolean _isOutput, boolean _initialHigh) {
        try {
            final GpioPinDigital  _pin;
            if (_isOutput)
                _pin = provideOutPin(_number);
            else
                _pin = provideInPin(_number);
            PINS.put(_id, _pin);
            if (_pin instanceof GpioPinDigitalOutput)
                ((GpioPinDigitalOutput)_pin).setState(_initialHigh);
            return true;
        }
        catch(NullPointerException en) {
            UtilBox.Console.errln("Number "+_number+" is no valid pin!");
            return false;
        }
        catch(Exception ex) {
            UtilBox.Console.errln("Pin("+_number+") initialization failed!");
            UtilBox.ErrLog.errStackTrace(ex);
            return false;
        }
    }
    
    public boolean initPin(String _id, String _chip, String _number, boolean _isOutput) {
        return initPin(_id, _chip, _number, _isOutput, false);
    }
    
    public boolean initPin(String _id, String _chip, String _number, boolean _isOutput, boolean _initialHigh) {
        try {
            final GpioPinDigital  _pin;
            if (_isOutput)
                _pin = provideOutPin(_chip, _number);
            else
                _pin = provideInPin(_chip, _number);
            PINS.put(_id, _pin);
            if (_pin instanceof GpioPinDigitalOutput)
                ((GpioPinDigitalOutput)_pin).setState(_initialHigh);
            return true;
        }
        catch(NullPointerException en) {
            UtilBox.Console.errln("Number "+_number+" is no valid pin!");
            return false;
        }
        catch(Exception ex) {
            UtilBox.Console.errln("Pin("+_number+") initialization failed!");
            UtilBox.ErrLog.errStackTrace(ex);
            return false;
        }
    }
    
    public void setPinState(String _name, boolean _state) {
        if (PINS.get(_name) instanceof GpioPinDigitalOutput)
            ((GpioPinDigitalOutput)PINS.get(_name)).setState(_state);
    }
    
    private GpioPinDigitalInput provideInPin(String i) throws NullPointerException {
        final GpioPinDigitalInput _pin = INST.provisionDigitalInputPin(RASPI_PIN.get(i) );
        if (_pin==null)
            throw new NullPointerException();
        return _pin;
    }
    
    private GpioPinDigitalOutput provideOutPin(String i) throws NullPointerException {
        final GpioPinDigitalOutput _pin = INST.provisionDigitalOutputPin(IPinNumber.RASPI_PIN.get(i) );
        if (_pin==null)
            throw new NullPointerException();
        return _pin;
    }
    
    private GpioPinDigitalInput provideInPin(String chip, String i) throws NullPointerException {
        final GpioPinDigitalInput _pin;
        if (CHIP_INST.containsKey(chip)) {
            if (CHIP_INST.get(chip) instanceof MCP23017GpioProvider) {
                _pin = INST.provisionDigitalInputPin(CHIP_INST.get(chip), IPinNumber.MCP23017_PIN.get(i));
            }
            else if (CHIP_INST.get(chip) instanceof MCP23S17GpioProvider) {
                _pin = INST.provisionDigitalInputPin(CHIP_INST.get(chip), IPinNumber.MCP23S17_PIN.get(i));
            }
            else {
                _pin = null;
            }
        }
        else if (chip.toLowerCase().contains("rasp")) {
            return provideInPin(i);
        }
        else {
            _pin = null;
        }
        if (_pin==null)
            throw new NullPointerException();
        return _pin;
    }
    
    private GpioPinDigitalOutput provideOutPin(String chip, String i) throws NullPointerException {
        final GpioPinDigitalOutput _pin;
        if (CHIP_INST.containsKey(chip)) {
            if (CHIP_INST.get(chip) instanceof MCP23017GpioProvider) {
                _pin = INST.provisionDigitalOutputPin(CHIP_INST.get(chip), IPinNumber.MCP23017_PIN.get(i));
            }
            else if (CHIP_INST.get(chip) instanceof MCP23S17GpioProvider) {
                _pin = INST.provisionDigitalOutputPin(CHIP_INST.get(chip), IPinNumber.MCP23S17_PIN.get(i));
            }
            else {
                _pin = null;
            }
        }
        else if (chip.toLowerCase().contains("rasp")) {
            return provideOutPin(i);
        }
        else {
            _pin = null;
        }
        if (_pin==null)
            throw new NullPointerException();
        return _pin;
    }
    
    public boolean loadPinsFromXML(String _filename) {
        final ITreeNode _root = new DOMXMLTree1(_filename).getMother();
        for (String k : _root.getElements()) {
            initPinFromXMLNode(_root, k, "rasp");
        }
        for (ITreeNode _chipNode : _root.getNodes()) {
            final String _chip = _chipNode.getID();
            if (!CHIP_INST.containsKey(_chip)) {
                this.registerChip(_chip, _chipNode.getSubID());
            }
            for (String k : _chipNode.getElements()) {
                initPinFromXMLNode(_chipNode, k, _chip);
            }
        }
        return true;
    }
    
    private void initPinFromXMLNode(ITreeNode n, String k, String chip) {
        String  pin     =   n.getValueAt(k);
        String  isOut   =   n.getAttributeValue(k, "mode");
        boolean mode;
        if ((isOut!=null) && (isOut.toLowerCase().contains("out"))) {
            mode = true;
        }
        else if ((isOut!=null) && (isOut.toLowerCase().contains("out"))) {
            mode = false;
        }
        else {
            throw new de.tu_berlin.ilr.ipsm.exceptions.InitException("Pin \""+k+"\" configuration in XML doesn't contain attribute \"mode\" with \"input\" or \"output\" as value.");
        }
        String  state   =   n.getAttributeValue(k, "init");
        if ((state!=null) && (state.equalsIgnoreCase("true"))) {
            initPin(k, chip, pin, mode, true);
        }
        else {
            initPin(k, chip, pin, mode, false);
        }
    }
    
    
}
