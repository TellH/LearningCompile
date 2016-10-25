package com.tellh.AbstractSyntaxTree;

import java.util.Iterator;
import java.util.List;

/**
 * Created by tlh on 2016/10/24.
 */
public class ASTList extends ASTree {
    protected List<ASTree> children;

    public ASTList(List<ASTree> children) {
        this.children = children;
    }


    @Override
    public ASTree child(int i) {
        return null;
    }

    @Override
    public int numChildren() {
        return children.size();
    }

    @Override
    public Iterator<ASTree> children() {
        return children.iterator();
    }

    @Override
    public String location() {
        for (ASTree child : children) {
            String s = child.location();
            if (s != null)
                return s;
        }
        return null;
    }

    @Override
    public String toString() {
        return "ASTList{" +
                "children=" + children +
                '}';
    }
}
