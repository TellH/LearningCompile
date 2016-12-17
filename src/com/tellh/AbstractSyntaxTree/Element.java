package com.tellh.AbstractSyntaxTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlh on 2016/10/24.
 */
public class Element extends ASTList {
    private Integer codeBegin;
    private List<Integer> trueChain;
    private List<Integer> falseChain;

    public Element(List<ASTree> children) {
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

    @Override
    public String val() {
        return left().val() + operator() + right().val();
    }

    public int getCodeBegin() {
        return codeBegin;
    }

    public void setCodeBegin(Integer codeBegin) {
        this.codeBegin = codeBegin;
    }

    public List<Integer> getTrueChain() {
        return trueChain;
    }

    public void setTrueChain(List<Integer> trueChain) {
        this.trueChain = new ArrayList<>();
        this.trueChain.addAll(trueChain);
    }

    public List<Integer> getFalseChain() {
        return falseChain;
    }

    public void setFalseChain(List<Integer> falseChain) {
        this.falseChain = new ArrayList<>();
        this.falseChain.addAll(falseChain);
    }


}
