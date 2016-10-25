package com.tellh.AbstractSyntaxTree;

import com.tellh.Token.Token;

/**
 * Created by tlh on 2016/10/24.
 */
public class Identifier extends ASTLeaf {
    public Identifier(Token token) {
        super(token);
    }

    public String name() {
        return token().getValue();
    }
}
