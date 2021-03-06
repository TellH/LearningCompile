package com.tellh.AbstractSyntaxTree;

import com.tellh.Token.Token;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tlh on 2016/10/24.
 */
public class ASTLeaf extends ASTree {
    protected Token token;
    protected String val;

    @Override
    public void setVal(String val) {
        this.val = val;
    }

    private static List empty = Collections.EMPTY_LIST;

    public ASTLeaf(Token token) {
        this.token = token;
    }

    @Override
    public ASTree child(int i) {
        throw new IndexOutOfBoundsException("Leaf Node!");
    }

    @Override
    public int numChildren() {
        return 0;
    }

    @Override
    public Iterator<ASTree> children() {
        return (Iterator<ASTree>) empty.iterator();
    }

    @Override
    public String location() {
        return "At line " + token.getLineNumber();
    }

    @Override
    public String name() {
        return token().getValue();
    }

    @Override
    public String val() {
        if (val == null)
            val = token.getValue();
        return val;
    }

    public Token token() {
        return token;
    }

    @Override
    public String toString() {
        return "ASTLeaf{" +
                "token=" + token +
                '}';
    }
}
