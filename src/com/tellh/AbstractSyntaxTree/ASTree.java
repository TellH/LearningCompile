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

    @Override
    public Iterator<ASTree> iterator() {
        return children();
    }

}
