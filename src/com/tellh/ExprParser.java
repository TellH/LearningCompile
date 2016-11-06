package com.tellh;


import com.tellh.AbstractSyntaxTree.*;
import com.tellh.Token.IdToken;
import com.tellh.Token.NumToken;
import com.tellh.Token.Token;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * 赋值表达式文法：
 * statement=> target=expression
 * target=> Identifier
 * expression=> term|term+expression|term-expression
 * term=> factor|factor*term|factor/term
 * factor=> Number|(expression)
 * <p>
 * Identifier是标识符，Number是数字
 * 递归子程序法
 */

public class ExprParser {
    private Lexer lexer;

    public ExprParser(Lexer p) {
        lexer = p;
    }

    public ASTree statement() {
        ASTree left = target();
        ASTLeaf op = new ASTLeaf(token(":="));
        left = new BinaryExpr(Arrays.asList(left, op, expression()));
        ((BinaryExpr)left).setName("statement");
        return left;
    }

    public ASTree expression() throws ParseException {
        ASTree left = term();
        while (isToken("+") || isToken("-")) {
            Token token = lexer.read();
            System.out.println("在第" + token.getLineNumber() + "行 读入：" + token.getValue());
            ASTLeaf op = new ASTLeaf(token);
            ASTree right = term();
            left = new BinaryExpr(Arrays.asList(left, op, right));
            ((BinaryExpr)left).setName("expression");
        }
        return left;
    }

    public ASTree target() {
        Token tar = lexer.read();
        System.out.println("在第" + tar.getLineNumber() + "行 读入：" + tar.getValue());
        if (tar instanceof IdToken) {
            return new Identifier(tar);
        } else
            throw new ParseException(tar);
    }

    public ASTree term() throws ParseException {
        ASTree left = factor();
        while (isToken("*") || isToken("/")) {
            ASTLeaf op = new ASTLeaf(lexer.read());
            ASTree right = factor();
            left = new BinaryExpr(Arrays.asList(left, op, right));
            ((BinaryExpr)left).setName("term");
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

    private Token token(String name) throws ParseException {
        Token t = lexer.read();
        if (!name.equals(t.getValue()))
            throw new ParseException(t);
        System.out.println("在第" + t.getLineNumber() + "行 读入：" + t.getValue());
        return t;
    }

    private boolean isToken(String name) throws ParseException {
        Token t = lexer.peek(0);
        if (!name.equals(t.getValue()))
            return false;
        System.out.println("在第" + t.getLineNumber() + "行 匹配：" + t.getValue());
        return true;
    }

    private static void printTree(ASTree root, int blk) {
        for (int i = 0; i < blk; i++) System.out.print("\t");//缩进
        System.out.println(String.format("|—<%s>", root.name()));//打印"|—<id>"形式
        for (int i = 0; i < root.numChildren(); ++i) {
            printTree(root.child(i), blk + 1);//打印子树，累加缩进次数
        }
    }

    public static void main(String[] args) throws ParseException {
        InputStream in = Lexer.class.getResourceAsStream("test.txt");
        Lexer lexer = new Lexer(new InputStreamReader(in));
        ExprParser p = new ExprParser(lexer);
        ASTree t = p.statement();
        printTree(t, 0);
    }

}