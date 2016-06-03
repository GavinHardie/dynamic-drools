package com.busii.utils;

import org.kie.api.runtime.ObjectFilter;

public class ClassObjectFilter implements ObjectFilter {

    private final Class myClass;

    public ClassObjectFilter(Class clazz) {
        this.myClass = clazz;
    }

    @Override
    public boolean accept(Object o) {
        return myClass.isInstance(o);
    }
}
