package com.busii.dynamicdrools;

import com.busii.utils.DomainBase;
import com.busii.utils.DomainSession;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    
    public static final void main(final String[] args) {
        
        // Load the rule file
        DomainBase domainBase = new DomainBase("DynamicWorld.drl");

        // Create a session and load data into it
        DomainSession domainSession = domainBase.getModel();

        // Put a calc engine into session
        domainSession.setGlobal("calc", new Calc());
        
        // Put a data object into the session
        Map<String,Object> messageProperties = new HashMap<>();
        messageProperties.put("name", "MrTrue");
        messageProperties.put("a", 10);
        messageProperties.put("b", 10);
        messageProperties.put("c", 10);
        messageProperties.put("indicator", Boolean.TRUE );
        domainSession.loadObject("com.busii.dynamicdrools.Person", messageProperties);

        messageProperties = new HashMap<>();
        messageProperties.put("name", "MrFalse");
        messageProperties.put("a", 10);
        messageProperties.put("b", 10);
        messageProperties.put("c", 10);
        messageProperties.put("indicator", Boolean.FALSE );
        domainSession.loadObject("com.busii.dynamicdrools.Person", messageProperties);
        
        
        // Execute rules
        domainSession.execute();
        
        LOG.debug("------------------------------------");
        
        // Examine objects in the session
        for(Map<String,Object> person : domainSession.getObjects("com.busii.dynamicdrools.Person")) {
            LOG.debug("---");
            for(Map.Entry<String, Object> entry : person.entrySet()) {
                LOG.debug(String.format("%s=%s", entry.getKey(), String.valueOf(entry.getValue())));
            }
        }
    }

}
