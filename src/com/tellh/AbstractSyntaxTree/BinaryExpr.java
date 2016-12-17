package com.tellh.AbstractSyntaxTree;


import java.util.List;

import static com.tellh.IfElseStatementTranslator.AssignmentCode.order;

/**
 * Created by tlh on 2016/10/24.
 */
public class BinaryExpr extends ASTList {
    public BinaryExpr(List<ASTree> children) {
        super(children);
        if (!operator().equals("="))
            setVal("T" + order);
//        else {
//            System.out.println("(" + operator() + " " + " " + "   " + right().val() + " " + left().val() + ")");
//        }
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
