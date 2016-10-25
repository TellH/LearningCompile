package com.tellh;

import com.tellh.AbstractSyntaxTree.*;
import com.tellh.Token.IdToken;
import com.tellh.Token.NumToken;
import com.tellh.Token.OperatorToken;
import com.tellh.Token.Token;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class OpPrecedenceParser {
    private Lexer lexer;
    protected HashMap<String, Precedence> operators;

    public static class Precedence {
        int value;
        boolean leftAssoc; // left associative

        public Precedence(int v, boolean a) {
            value = v;
            leftAssoc = a;
        }
    }

    public OpPrecedenceParser(Lexer p) {
        lexer = p;
        operators = new HashMap<>();
        operators.put("<", new Precedence(1, true));
        operators.put(">", new Precedence(1, true));
        operators.put("+", new Precedence(2, true));
        operators.put("-", new Precedence(2, true));
        operators.put("*", new Precedence(3, true));
        operators.put("/", new Precedence(3, true));
        operators.put("^", new Precedence(4, false));
    }

    public ASTree statement() {
        ASTree right = target();
        ASTLeaf op = new ASTLeaf(token(":="));
        right = new BinaryExpr(Arrays.asList(right, op, expression()));
        return right;
    }

    public ASTree expression() throws ParseException {
        ASTree right = factor();
        Precedence next;
        while ((next = nextOperator()) != null)
            right = doShift(right, next.value);
        return right;
    }

    private ASTree doShift(ASTree left, int prec) throws ParseException {
        ASTLeaf op = new ASTLeaf(lexer.read());
        ASTree right = factor();
        Precedence next;
        while ((next = nextOperator()) != null && rightIsExpr(prec, next))
            right = doShift(right, next.value);

        return new BinaryExpr(Arrays.asList(left, op, right));
    }

    private Precedence nextOperator() throws ParseException {
        Token t = lexer.peek(0);
        if (t instanceof OperatorToken)
            return operators.get(t.getValue());
        else
            return null;
    }

    private static boolean rightIsExpr(int prec, Precedence nextPrec) {
        if (nextPrec.leftAssoc)
            return prec < nextPrec.value;
        else
            return prec <= nextPrec.value;
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

    public ASTree target() {
        Token tar = lexer.read();
        if (tar instanceof IdToken) {
            return new Identifier(tar);
        } else
            throw new ParseException(tar);
    }

    Token token(String name) throws ParseException {
        Token t = lexer.read();
        if (!name.equals(t.getValue()))
            throw new ParseException(t);
        return t;
    }

    boolean isToken(String name) throws ParseException {
        Token t = lexer.peek(0);
        return name.equals(t.getValue());
    }

    public static void main(String[] args) throws ParseException {
        InputStream in = Lexer.class.getResourceAsStream("testOp.txt");
        Lexer lexer = new Lexer(new InputStreamReader(in));
        OpPrecedenceParser p = new OpPrecedenceParser(lexer);
//        ASTree t = p.expression();
        ASTree t = p.statement();
        System.out.println("=> " + t);
    }
}
