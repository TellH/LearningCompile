package com.tellh;

import com.tellh.Token.Token;

/**
 * Created by tlh on 2016/10/25.
 */
public class ParseException extends RuntimeException {
    public ParseException(Token t) {
        super("Token " + t.getValue() + " can't appear hear at line " + t.getLineNumber());
    }

    public ParseException(String message) {
        super(message);
    }
}
