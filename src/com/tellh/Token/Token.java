package com.tellh.Token;

import java.util.List;

/**
 * Created by tlh on 2016/10/22.
 */
public abstract class Token {
    protected enum Type {
        Identifier,
        NumberConstants,
        StringConstants,
        Keyword,
        Operator,
        Delimiter,
        EOF,
        EOL
    }

    public static final Token EOF = new Token(-1) {
        @Override
        protected Type getType() {
            return Type.EOF;
        }

        @Override
        public String getValue() {
            return null;
        }
    };

    public static class EOL extends Token {

        public EOL(int lineNumber) {
            super(lineNumber);
        }

        @Override
        protected Type getType() {
            return Type.EOL;
        }

        @Override
        public String getValue() {
            return "\n";
        }

        @Override
        public String toString() {
            return "\n";
        }
    }

    //    public static final String EOL = "\\n";
    protected int lineNumber;

    public Token(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }


    protected abstract Type getType();

    public abstract String getValue();
//    protected boolean belongTo(String word);
}
