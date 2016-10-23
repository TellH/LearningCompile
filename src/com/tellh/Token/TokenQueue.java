package com.tellh.Token;

import java.util.ArrayList;

/**
 * Created by tlh on 2016/10/23.
 */
public class TokenQueue {
    private ArrayList<Token> queue = new ArrayList<>();
    private int head = 0;

    public void add(Token token) {
        queue.add(token);
    }

    public Token poll() {
        return queue.get(head);
    }

    public Token remove() {
        return queue.get(head++);
    }

    public Token peek(int i) {
        return queue.get(head + i);
    }

    public boolean isEmpty() {
        return head == queue.size();
    }

}
