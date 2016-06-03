package com.busii.utils;

import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

public class DomainBase {
    
    private KnowledgeBase kbase;
    
    public DomainBase(String filename) {

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newFileResource(filename), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            throw new RuntimeException("Errors: " + kbuilder.getErrors());
        }
        this.kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
    }
    
    public DomainSession getModel() {
        return new DomainSession(kbase.newKieSession());
    }
    
}
