package com.tellh.AbstractSyntaxTree;

import java.util.Iterator;
import java.util.List;

/**
 * Created by tlh on 2016/10/24.
 */
public class ASTList extends ASTree {
    protected List<ASTree> children;
    private String name;

    public ASTList(List<ASTree> children) {
        this.children = children;
    }


    @Override
    public ASTree child(int i) {
        return children.get(i);
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
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "ASTList{" +
                "children=" + children +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }
}
