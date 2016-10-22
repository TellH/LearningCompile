package com.tellh.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by tlh on 2016/10/22.
 */
public class DelimiterToken extends Token {
    private String value;
    private static final List<String> pattern = Collections.singletonList("[.;,()]");

    public static String getPattern() {
        return String.join("|", pattern);
    }

    public DelimiterToken(int lineNumber, String value) {
        super(lineNumber);
        this.value = value;
    }

    @Override
    protected Type getType() {
        return Type.Delimiter;
    }

    @Override
    protected String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DelimiterToken{" +
                "value='" + value + '\'' +
                "at line: " + lineNumber +
                '}';
    }
}
