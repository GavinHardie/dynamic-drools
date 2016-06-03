package com.busii.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.kie.api.definition.type.FactType;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieSession;

public class DomainSession {

    private static final Pattern CLASSNAME_PATTERN = Pattern.compile("(.*)\\.(\\w+)");
    private static final Pattern ENUM_PATTERN = Pattern.compile("(.*)\\.(\\w+)\\.(\\w+)");

    final private KieSession ksession;

    /**
     * Compile a DRL file and setup a KieSession
     * @param filename 
     */
     DomainSession(KieSession ksession) {
        this.ksession = ksession;

        // Add some debugging listeners
        ksession.addEventListener(new DebugAgendaEventListener());
        ksession.addEventListener(new DebugRuleRuntimeEventListener());
    }

    public void setGlobal(String name, Object object) {
        ksession.setGlobal(name, object);
    }
     
    /**
     * Load dynamic objects (where the structure is defined in the DRL file)
     * into the session.
     * 
     * @param fullname
     * @param objectProperties 
     */
    public void loadObject(String fullname, Map<String,Object> objectProperties) {
        
        Matcher matcher = CLASSNAME_PATTERN.matcher(fullname);
        if (!matcher.matches()) 
            throw new RuntimeException(String.format("[%s] failed to parse out as an enum value", fullname));
        String packageName = matcher.group(1);
        String className = matcher.group(2);
        
        try {
            FactType factType = ksession.getKieBase().getFactType(packageName, className);
            Object newInstance = factType.newInstance();
            factType.setFromMap(newInstance, objectProperties);
            ksession.insert(newInstance);
        } catch (InstantiationException | IllegalAccessException e ) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a dynamic enum value defined in the DRL file
     * @param fullname
     * @return 
     */
    public Object getEnum(String fullname) {
        Matcher matcher = ENUM_PATTERN.matcher(fullname);
        if (!matcher.matches()) 
            throw new RuntimeException(String.format("[%s] failed to parse out as an enum value", fullname));
        String packageName = matcher.group(1);
        String className = matcher.group(2);
        String valueName = matcher.group(3);
        FactType factType = ksession.getKieBase().getFactType(packageName, className);
        Class clazz = factType.getFactClass();
        Object[] enumConstants = clazz.getEnumConstants();
        for(Object enumConstant : enumConstants) {
            if (enumConstant.toString().equals(valueName)) {
                return enumConstant;
            }
        }
        return null;
    }

    public void execute() {
        ksession.fireAllRules();
    }
    
    public List<Map<String,Object>> getObjects(String fullname) {
        Matcher matcher = CLASSNAME_PATTERN.matcher(fullname);
        if (!matcher.matches()) 
            throw new RuntimeException(String.format("[%s] failed to parse out as an enum value", fullname));
        String packageName = matcher.group(1);
        String className = matcher.group(2);
        FactType factType = ksession.getKieBase().getFactType(packageName, className);
        
        List<Map<String,Object>> result = new ArrayList<>();
        for(Object object : ksession.getObjects(new ClassObjectFilter(factType.getFactClass()))) {
            Map<String,Object> objectAsMap = factType.getAsMap(object);
            result.add(objectAsMap);
        }
        return result;
    }
    
    public void close() {
        ksession.dispose();
    }

}
