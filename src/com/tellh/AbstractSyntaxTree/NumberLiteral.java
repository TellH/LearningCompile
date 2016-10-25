package com.tellh.AbstractSyntaxTree;

import com.tellh.Token.NumToken;
import com.tellh.Token.Token;

/**
 * Created by tlh on 2016/10/24.
 */
public class NumberLiteral extends ASTLeaf {
    public NumberLiteral(Token token) {
        super(token);
    }

    public int value() {
        return Integer.parseInt(token().getValue());
    }

}
