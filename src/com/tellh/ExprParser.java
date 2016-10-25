package com.tellh;


import com.tellh.AbstractSyntaxTree.ASTLeaf;
import com.tellh.AbstractSyntaxTree.ASTree;
import com.tellh.AbstractSyntaxTree.BinaryExpr;
import com.tellh.AbstractSyntaxTree.NumberLiteral;
import com.tellh.Token.IdToken;
import com.tellh.Token.NumToken;
import com.tellh.Token.Token;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ExprParser {
    private Lexer lexer;

    public ExprParser(Lexer p) {
        lexer = p;
    }

    public ASTree expression() throws ParseException {
        ASTree left = term();
        while (isToken("+") || isToken("-")) {
            ASTLeaf op = new ASTLeaf(lexer.read());
            ASTree right = term();
            left = new BinaryExpr(Arrays.asList(left, op, right));
        }
        return left;
    }

    public ASTree term() throws ParseException {
        ASTree left = factor();
        while (isToken("*") || isToken("/")) {
            ASTLeaf op = new ASTLeaf(lexer.read());
            ASTree right = factor();
            left = new BinaryExpr(Arrays.asList(left, op, right));
        }
        return left;
    }

    public ASTree factor() throws ParseException {
        if (isToken("(")) {
            token("(");
            ASTree e = expression();
            token(")");
            return e;
        } else {
            Token t = lexer.read();
            if (t instanceof NumToken) {
                NumberLiteral n = new NumberLiteral(t);
                return n;
            } else
                throw new ParseException(t);
        }
    }

    void token(String name) throws ParseException {
        Token t = lexer.read();
        if (!(t instanceof IdToken && name.equals(t.getValue())))
            throw new ParseException(t);
    }

    boolean isToken(String name) throws ParseException {
        Token t = lexer.peek(0);
        return t instanceof IdToken && name.equals(t.getValue());
    }

    public static void main(String[] args) throws ParseException {
        InputStream in = Lexer.class.getResourceAsStream("source.txt");
        Lexer lexer = new Lexer(new InputStreamReader(in));
        ExprParser p = new ExprParser(lexer);
        ASTree t = p.expression();
        System.out.println("=> " + t);
    }
}