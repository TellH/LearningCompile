package com.tellh.Token;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tlh on 2016/10/22.
 */
public class OperatorToken extends Token {
    private String value;
    private static final List<String> pattern = Arrays.asList("<=", "==", ">=", "&&",
            "\\|\\|", ":=", "<", ">", "\\+", "-", "\\*", "/", "#");


    public static String getPattern() {
        return String.join("|", pattern);
    }

    public OperatorToken(int lineNumber, String value) {
        super(lineNumber);
        this.value = value;
    }

    @Override
    protected Type getType() {
        return Type.Operator;
    }

    @Override
    protected String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "OperatorToken{" +
                "value='" + value + '\'' +
                "at line: " + lineNumber +
                '}';
    }
}
