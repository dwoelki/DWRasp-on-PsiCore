/* CHANGELOG
 * 2019-01-07   DW  Creation
 * 2019-01-07   DW  Settings loader
 */
package de.woelki_web.dwrasp.base_module;

import de.tu_berlin.ilr.ipsm.outputs.ITreeNode;
import de.tu_berlin.ilr.ipsm.settings.PreSettings;
import de.tu_berlin.ilr.ipsm.util.DOMXMLTree1;
import de.tu_berlin.ilr.ipsm.util.LookupInstantiator;
import de.tu_berlin.ilr.ipsm.util.StringManager;
import de.tu_berlin.ilr.ipsm.util.UtilBox;
import java.util.HashMap;

/**
 * Class-description:<br>
 * 
 * @author doom@ls-clan.net
 */
public class ConfigurationLoader {
    
    final private static HashMap<String, IGPIOCommandInterpreter>    SERVICES = new HashMap<>();
    
    public static void loadConfigurations() {
        if (QuickGPIO.GLOBAL_INST==null)
            QuickGPIO.GLOBAL_INST = new QuickGPIO();
        
        final DOMXMLTree1 _settings = new DOMXMLTree1(StringManager.PathFileMaker(PreSettings.LoadPreSettingsFile));
        final ITreeNode   _raspnode = _settings.getNode("DWRasp");
        final ITreeNode   _services = _raspnode.getNode("Services");
        final ITreeNode   _files    = _raspnode.getNode("Config");
        for (String k : _services.getElements()) {
            boolean _registered = ConfigurationLoader.addService(k);
            if (_registered)
                UtilBox.Console.outln("Service registered:\t"+k);
            else
                UtilBox.Console.errln("Error! Service could not be registered:\t"+k);
        }
        try {
            for (String k : _files.getElements()) {
                boolean _configLoaded = QuickGPIO.GLOBAL_INST.loadPinsFromXML(_files.getValueAt(k));
                if (_configLoaded)
                    UtilBox.Console.outln("Pin configuration loaded from:\t"+k);
                else
                    UtilBox.Console.errln("Error! Pin configuration could not be loaded from:\t"+k);
            }
        } catch(RuntimeException ex) {
            UtilBox.Console.errln("No GPIO configuration loaded!");
        }
    }
    
    
    public static IGPIOCommandInterpreter getService(String _implName) {
        if (SERVICES.containsKey(_implName))
            return SERVICES.get(_implName);
        else
            return null;
    }
    
    private static boolean addService(String _implName) {
        Object _byImplName = null;
        Object[] _allByImplName = LookupInstantiator.instantiateAllViaLookupSilent(IGPIOCommandInterpreter.class);
        for (Object o : _allByImplName) {
            if (o instanceof IGPIOCommandInterpreter) {
                if ( ((IGPIOCommandInterpreter)o).getImplName().equals(_implName) ) {
                    _byImplName = o;
                }
            }
        }
        if (_byImplName==null) {
            Object _byClassName = LookupInstantiator.instantiateViaLookup(IGPIOCommandInterpreter.class, _implName);
            if (_byClassName!=null) {
                if (_byClassName instanceof IGPIOCommandInterpreter) {
                    final IGPIOCommandInterpreter _service = (IGPIOCommandInterpreter)_byClassName;
                    SERVICES.put(_service.getImplName(), _service);
                    return true;
                }
            }
        }
        else {
            final IGPIOCommandInterpreter _service = (IGPIOCommandInterpreter)_byImplName;
            SERVICES.put(_service.getImplName(), _service);
            return true;
        }
        return false;
    }
    
    
}
