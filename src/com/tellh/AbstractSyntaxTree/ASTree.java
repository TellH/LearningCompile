package com.tellh.AbstractSyntaxTree;


import java.util.Iterator;

/**
 * Created by tlh on 2016/10/24.
 */
public abstract class ASTree implements Iterable<ASTree> {
    public abstract ASTree child(int i);

    public abstract int numChildren();

    public abstract Iterator<ASTree> children();

    public abstract String location();

    public abstract String name();

    public abstract String val();

    public abstract void setVal(String val);

    @Override
    public Iterator<ASTree> iterator() {
        return children();
    }

}
