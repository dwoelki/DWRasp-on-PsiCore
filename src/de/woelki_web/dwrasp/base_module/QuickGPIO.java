/* CHANGELOG
 * 2019-01-02   DW  Creation
 * 2019-01-02   DW  fs
 */
package de.woelki_web.dwrasp.base_module;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.*;
import com.pi4j.io.gpio.event.*;
import static de.woelki_web.dwrasp.base_module.IPinNumber.PIN_NUMBER;
import de.tu_berlin.ilr.ipsm.util.UtilBox;
import java.util.HashMap;

/**
 * Class-description:<br>
 * 
 * 
 * @author dominik@woelki-web.de
 */
public class QuickGPIO {
    
    final   private     GpioController                      INST;
    final   private     HashMap<String, GpioPinDigital>     PINS;
    
    
    public QuickGPIO() {
        INST        =   GpioFactory.getInstance();
        PINS        =   new HashMap<>();
    }
    
    public boolean initPin(String _id, int _number, boolean _isOutput) {
        return initPin(_id, _number, _isOutput, false);
    }
    
    public boolean initPin(String _id, int _number, boolean _isOutput, boolean _initialHigh) {
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
    
    public void setPinState(String _name, boolean _state) {
        if (PINS.get(_name) instanceof GpioPinDigitalOutput)
            ((GpioPinDigitalOutput)PINS.get(_name)).setState(_state);
    }
    
    private GpioPinDigitalInput provideInPin(int i) throws NullPointerException {
        final GpioPinDigitalInput _pin = INST.provisionDigitalInputPin( PIN_NUMBER.get(i) );
        if (_pin==null)
            throw new NullPointerException();
        return _pin;
    }
    
    private GpioPinDigitalOutput provideOutPin(int i) throws NullPointerException {
        final GpioPinDigitalOutput _pin = INST.provisionDigitalOutputPin( PIN_NUMBER.get(i) );
        if (_pin==null)
            throw new NullPointerException();
        return _pin;
    }
    
    
}
