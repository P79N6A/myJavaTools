package com.commons.logMethodPerformance.src.main.java.com.blog.performance;

import java.io.Serializable;

/**
 */
public class Method implements Serializable {
    private static final long serialVersionUID = 5891644182247161881L;
    
    public String className;
    public String name;
    
    public Method(String className, String name) {
        this.className = className;
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Method)) return false;
        Method method = (Method)obj;
        return this.className.equals(method.getClassName()) &&
               this.name.equals(method.getName());
    }
    
    @Override
    public int hashCode() {
        return this.className.hashCode() ^ this.name.hashCode();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(className)
            .append(".")
            .append(name);
        return sb.toString();
    }
}
