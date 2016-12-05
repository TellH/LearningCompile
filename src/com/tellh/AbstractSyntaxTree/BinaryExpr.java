package com.tellh.AbstractSyntaxTree;

import com.tellh.ExprParser;

import java.util.List;

/**
 * Created by tlh on 2016/10/24.
 */
public class BinaryExpr extends ASTList {
    public BinaryExpr(List<ASTree> children) {
        super(children);
        if (!operator().equals("="))
            setVal("T" + ExprParser.generateQuadruple(left(), operator(), right()));
        else {
            System.out.println("(" + operator() + " " + " " + "   " + right().val() + " " + left().val() + ")");
        }
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
