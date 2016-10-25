package com.tellh.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by tlh on 2016/10/22.
 */
public class IdToken extends Token {
    private static final List<String> pattern = Collections.singletonList("[A-Z_a-z][A-Z_a-z0-9]*");
    private String value;

    public IdToken(int lineNumber, String value) {
        super(lineNumber);
        this.value = value;
    }

    public static String getPattern() {
        return String.join("|", pattern);
    }


    @Override
    protected Type getType() {
        return Type.Identifier;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IdToken{" +
                "value='" + value + '\'' +
                "at line: " + lineNumber +
                '}';
    }
}
