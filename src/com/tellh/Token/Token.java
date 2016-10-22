package com.tellh.Token;

import java.util.List;

/**
 * Created by tlh on 2016/10/22.
 */
public abstract class Token {
    enum Type {
        Identifier,
        NumberConstants,
        StringConstants,
        Keyword,
        Operator,
        Delimiter,
        EOF
    }

    public static final Token EOF = new Token(-1) {
        @Override
        protected Type getType() {
            return Type.EOF;
        }

        @Override
        protected String getValue() {
            return null;
        }
    };

    public static final String EOL = "\\n";
    protected int lineNumber;

    public Token(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }


    protected abstract Type getType();

    protected abstract String getValue();
//    protected boolean belongTo(String word);
}
