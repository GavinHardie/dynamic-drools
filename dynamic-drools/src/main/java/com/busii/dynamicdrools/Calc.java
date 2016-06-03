package com.busii.dynamicdrools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calc {

    private static final Pattern OUT_BRACKET_PATTERN = Pattern.compile("([^(]+)([(].*|$)");
    private static final Pattern IN_BRACKET_PATTERN = Pattern.compile("([(][^)]*[)]).*");

    public Object cast(String toCast) {
        try {
            return Integer.parseInt(toCast);
        } catch (NumberFormatException e) { 
            System.out.println(String.format("Cast [%s] into 0", toCast));
            return 0;
        }
    }
    
    public Object rangeLookup(Object value, String table) {
        // table is of the form a(10)b(20)c(30)d(40)e
        String result = rangeLookup(value.toString(), value.getClass(), tokenize(table));
        return cast(result);
    }

    public Object valueLookup(Object value, String table) {
        // table is of the form 10(a)20(b)30(c)
        String result = valueLookup(value.toString(), value.getClass(), tokenize(table));
        return cast(result);
    }
    
    private List<String> tokenize(String s) {
        
        List<String> result = new ArrayList<>();

        // optional out bracket
        Matcher firstMatcher = OUT_BRACKET_PATTERN.matcher(s);
        if (firstMatcher.matches()) {
            String token = firstMatcher.group(1);
            result.add(token);
            s = s.substring(token.length());
        }

        boolean flipflop = true;
        boolean keepGoing = true;
        while (keepGoing) {
            Matcher b = flipflop ? IN_BRACKET_PATTERN.matcher(s) : OUT_BRACKET_PATTERN.matcher(s);
            if (b.matches()) {
                String token = b.group(1);
                result.add(token);
                s = s.substring(token.length());
            } else {
                keepGoing = false;
            }
            flipflop = !flipflop;
        }
        return result;
    }

    private String valueLookup(String value, Class clazz, List<String> table) {
        boolean found = false;
        for(String token : table) {
            if (token.startsWith("(") && token.endsWith(")")) {
                found = compare(value,token,clazz) == 0;
            } else {
                if (found) {
                    return token.substring(1, token.length() - 1);
                }
            }
        }
        return "";
    }
    
    private String rangeLookup(String value, Class clazz, List<String> table) {
        
        String result = "";
        for(String token : table) {
            if (token.startsWith("(") && token.endsWith(")")) {
                token = token.substring(1, token.length() - 1);
                if (compare(value,token,clazz) < 0) {
                    return result;
                } else {
                    result = "";
                }
            } else {
                result = token;
            }
        }
        return result;
    }

    private int compare(String paramVal, String tokenValue, Class clazz) {

        String clazzname = clazz.getName();
        if (clazzname.equals(String.class.getName())) {
            return paramVal.compareTo(tokenValue);
        }
        if (clazzname.equals(Integer.class.getName())) {
            Integer i1 = Integer.valueOf(paramVal);
            Integer i2 = Integer.valueOf(tokenValue);
            return i1.compareTo(i2);
        }
        if (clazzname.equals(Double.class.getName())) {
            Double d1 = Double.valueOf(paramVal);
            Double d2 = Double.valueOf(tokenValue);
            return d1.compareTo(d2);
        }
        if (clazzname.equals(Float.class.getName())) {
            Float f1 = Float.valueOf(paramVal);
            Float f2 = Float.valueOf(tokenValue);
            return f1.compareTo(f2);
        }
        throw new RuntimeException(String.format("[%s] not supported in Calc.compare()", clazzname));
    }
}
