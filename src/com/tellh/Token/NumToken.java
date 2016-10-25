package com.tellh.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by tlh on 2016/10/22.
 */
public class NumToken extends Token {
    private float value;
    private static final List<String> pattern = Arrays.asList("[0-9]+\\.?[0-9]+", "[0-9]+");


    public static String getPattern() {
        return String.join("|", pattern);
    }

    public NumToken(int lineNumber, float value) {
        super(lineNumber);
        this.value = value;
    }

    @Override
    protected Type getType() {
        return Type.NumberConstants;
    }

    @Override
    public String getValue() {
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return "NumToken{" +
                " value= " + value +
                " at line: " + lineNumber +
                '}';
    }
}
