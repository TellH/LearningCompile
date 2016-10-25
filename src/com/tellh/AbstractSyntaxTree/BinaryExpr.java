package com.tellh.AbstractSyntaxTree;

import java.util.List;

/**
 * Created by tlh on 2016/10/24.
 */
public class BinaryExpr extends ASTList {
    public BinaryExpr(List<ASTree> children) {
        super(children);
    }

    public ASTree left() {
        return child(0);
    }

    public String operator() {
        return ((ASTLeaf) child(1)).token().getValue();
    }

    public ASTree right() {
        return child(2);
    }
}
