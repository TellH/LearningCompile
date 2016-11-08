package com.tellh;

import com.tellh.Token.IdToken;
import com.tellh.Token.NumToken;
import com.tellh.Token.Token;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.*;

/**
 * Created by tlh on 2016/11/6.
 */
public class SimpleFirstParser {
    private HashMap<String, String> grammarMap;
    private List<Token> analyseStack;
    private HashMap<Pair, Integer> relativeMatrix;
    private Lexer tokenSteam;
    private int stackPointer;

    public SimpleFirstParser(HashMap<String, String> grammarMap, Lexer tokenSteam, HashMap<Pair, Integer> relativeMatrix) {
        this.grammarMap = grammarMap;
        this.tokenSteam = tokenSteam;
        this.relativeMatrix = relativeMatrix;
        analyseStack = new ArrayList<>();
        push(new ComposeToken(-1, "#"));

        Integer cmp;
        while (tokenSteam.hasNext()) {
            while ((cmp = top().getValue().equals("#") ?
                    -1 : relativeMatrix.get(new Pair(top(), tokenSteam.peek(0)))) != null && cmp != 1) {
                push(tokenSteam.read());
            }
            if (cmp == null)
                throw new ParseException("出错");
            StringBuilder sb = new StringBuilder();
            int back = stackPointer;
            while (true) {
                Token right = pop();
                Token left = top();
                if ((cmp = relativeMatrix.get(new Pair(left, right))) != null && cmp == -1) {
//                    pop();
                    break;
                }
            }
            for (int i = stackPointer; i < back; i++) {
                sb.append(analyseStack.get(i).getValue());
            }
            push(new ComposeToken(1, grammarMap.get(sb.toString())));
        }
        if (!analyseStack.get(0).getValue().equals("#") || !analyseStack.get(1).getValue().equals("statement"))
            throw new ParseException("出错了");
    }

    private void push(Token t) {
        try {
            analyseStack.add(stackPointer++, new TokenWrapper(t));
        } catch (Exception e) {
            analyseStack.set(stackPointer++, new TokenWrapper(t));
        }
    }

    private Token top() {
        return analyseStack.get(stackPointer - 1);
    }

    private Token pop() {
        return analyseStack.get(--stackPointer);
    }

    static class Pair {
        private String leftToken;
        private String rightToken;

        public Pair(String leftToken, String rightToken) {
            this.leftToken = leftToken;
            this.rightToken = rightToken;
        }

        public Pair(Token leftToken, Token rightToken) {
            this.leftToken = new TokenWrapper(leftToken).getValue();
            this.rightToken = new TokenWrapper(rightToken).getValue();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;

            Pair pair = (Pair) o;

            if (leftToken != null ? !leftToken.equals(pair.leftToken) : pair.leftToken != null) return false;
            return rightToken != null ? rightToken.equals(pair.rightToken) : pair.rightToken == null;

        }

        @Override
        public int hashCode() {
            int result = leftToken != null ? leftToken.hashCode() : 0;
            result = 31 * result + (rightToken != null ? rightToken.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "{" +
                    "leftToken='" + leftToken + '\'' +
                    ", rightToken='" + rightToken + '\'' +
                    '}';
        }
    }

    private static class ComposeToken extends Token {

        private final String value;

        public ComposeToken(int lineNumber, String value) {
            super(lineNumber);
            this.value = value;
        }

        @Override
        protected Token.Type getType() {
            return null;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private static class TokenWrapper extends Token {
        private Token token;

        public TokenWrapper(Token token) {
            super(token.getLineNumber());
            this.token = token;
        }

        @Override
        protected Type getType() {
            return null;
        }

        @Override
        public String getValue() {
            if (token instanceof IdToken)
                return "i";
            if (token instanceof NumToken)
                return "n";
            return token.getValue();
        }

        @Override
        public String toString() {
            return getValue();
        }
    }

    public static void main(String[] args) {
        InputStream in = Lexer.class.getResourceAsStream("test.txt");
        Lexer lexer = new Lexer(new InputStreamReader(in));
        in = FirstRelativeMatrixBuilder.class.getResourceAsStream("grammar.txt");
        FirstRelativeMatrixBuilder relativeMatrixBuilder = new FirstRelativeMatrixBuilder(in);
        final HashMap<Pair, Integer> relativeMatrix = relativeMatrixBuilder.build();
        SimpleFirstParser parser = new SimpleFirstParser(initGrammarMap(relativeMatrixBuilder.getGrammar()), lexer, relativeMatrix);
    }


    private static HashMap<String, String> initGrammarMap(HashMap<String, Set<String>> rawMap) {
        HashMap<String, String> grammarMap = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : rawMap.entrySet()) {
            String nonTerminal = entry.getKey();
            Set<String> products = entry.getValue();
            for (String product : products) {
                grammarMap.put(product, nonTerminal);
            }
        }
        return grammarMap;
    }

    private static void initRelativeMatrix(HashMap<Pair, Integer> relativeMatrix) {
        relativeMatrix.put(new Pair("expression", ")"), 0);
        relativeMatrix.put(new Pair("expression", "+"), 0);
        relativeMatrix.put(new Pair("expression", "-"), 0);
        relativeMatrix.put(new Pair("term", "+"), 0);
        relativeMatrix.put(new Pair("term", "-"), 0);
        relativeMatrix.put(new Pair("term", "*"), 0);
        relativeMatrix.put(new Pair("term", "/"), 0);
        relativeMatrix.put(new Pair("term", ")"), 1);
        relativeMatrix.put(new Pair("factor", "+"), 1);
        relativeMatrix.put(new Pair("factor", "-"), 1);
        relativeMatrix.put(new Pair("factor", ")"), 1);
        relativeMatrix.put(new Pair("NUMBER", "+"), 1);
        relativeMatrix.put(new Pair("NUMBER", "-"), 1);
        relativeMatrix.put(new Pair("NUMBER", "*"), 1);
        relativeMatrix.put(new Pair("NUMBER", "/"), 1);
        relativeMatrix.put(new Pair("NUMBER", ")"), 1);
        relativeMatrix.put(new Pair("ID", ":="), 0);
        relativeMatrix.put(new Pair("+", "term"), -1);
        relativeMatrix.put(new Pair("-", "term"), -1);
        relativeMatrix.put(new Pair("+", "factor"), -1);
        relativeMatrix.put(new Pair("-", "factor"), -1);
        relativeMatrix.put(new Pair("+", "NUMBER"), -1);
        relativeMatrix.put(new Pair("-", "NUMBER"), -1);
        relativeMatrix.put(new Pair("+", "("), -1);
        relativeMatrix.put(new Pair("-", "("), -1);
        relativeMatrix.put(new Pair("*", "term"), 0);
        relativeMatrix.put(new Pair("/", "term"), 0);
        relativeMatrix.put(new Pair("*", "factor"), -1);
        relativeMatrix.put(new Pair("/", "factor"), -1);
        relativeMatrix.put(new Pair("*", "NUMBER"), -1);
        relativeMatrix.put(new Pair("/", "NUMBER"), -1);
        relativeMatrix.put(new Pair("*", "("), -1);
        relativeMatrix.put(new Pair("/", "("), -1);
        relativeMatrix.put(new Pair(":=", "term"), -1);
        relativeMatrix.put(new Pair(":=", "factor"), -1);
        relativeMatrix.put(new Pair(":=", "NUMBER"), -1);
        relativeMatrix.put(new Pair(":=", "("), -1);
        relativeMatrix.put(new Pair("(", "expression"), 0);
        relativeMatrix.put(new Pair("(", "term"), -1);
        relativeMatrix.put(new Pair("(", "factor"), -1);
        relativeMatrix.put(new Pair("(", "NUMBER"), -1);
        relativeMatrix.put(new Pair("(", "+"), 1);
        relativeMatrix.put(new Pair("(", "-"), 1);
        relativeMatrix.put(new Pair("(", ")"), -1);
        relativeMatrix.put(new Pair(")", "*"), 1);
        relativeMatrix.put(new Pair(")", "/"), 1);
        relativeMatrix.put(new Pair(")", ")"), 1);
    }
}
