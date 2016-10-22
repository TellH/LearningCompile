package com.tellh.Token;

import java.util.Collections;
import java.util.List;

/**
 * Created by tlh on 2016/10/22.
 */
public class StringToken extends Token {
    private String value;
    //匹配与\"或\\或\n或除"以外任意一个字符匹配
    private static final List<String> pattern = Collections.singletonList("\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\"");


    public StringToken(int lineNumber, String value) {
        super(lineNumber);
        this.value = value;
    }

    public static String getPattern() {
        return String.join("|", pattern);
    }


    @Override
    protected Type getType() {
        return Type.StringConstants;
    }

    @Override
    protected String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StringToken{" +
                "value='" + value + '\'' +
                "at line: " + lineNumber +
                '}';
    }
}
