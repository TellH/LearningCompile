package com.tellh.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by tlh on 2016/10/22.
 */
public class KeywordToken extends Token {
    private String value;

    private static final List<String> pattern = Arrays.asList("VAR", "BEGIN", "END", "IF", "THEN",
            "CALL", "WHILE", "DO", "READ", "WRITE", "CONST", "PROCEDURE");


    public static String getPattern() {
        return String.join("|", pattern);
    }

    public KeywordToken(int lineNum, String value) {
        super(lineNum);
        this.value = value;
    }

    @Override
    protected Type getType() {
        return Type.Keyword;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeywordToken{" +
                "value='" + value + '\'' +
                "at line: " + lineNumber +
                '}';
    }
}
