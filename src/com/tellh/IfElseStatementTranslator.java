package com.tellh;

import com.tellh.AbstractSyntaxTree.*;
import com.tellh.Token.IdToken;
import com.tellh.Token.NumToken;
import com.tellh.Token.OperatorToken;
import com.tellh.Token.Token;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.tellh.IfElseStatementTranslator.AssignmentCode.order;

/**
 * Created by tlh on 2016/12/17.
 * Grammar(S):
 * A->i=E;
 * E->T|T+E|T-E|T || E
 * T->F|F*T|F/T|F && T
 * F->n|(E)|i rop i|i
 * S->if E {S}
 * S->if E {S} else {S}
 * S->AS
 * S->A
 */
public class IfElseStatementTranslator {
    private Lexer lexer;
    private int nextStat;
    private List<Code> result;

    public IfElseStatementTranslator(Lexer lexer) {
        this.lexer = lexer;
        result = new ArrayList<>();
    }

    public void S() {
        if (isToken("if")) {
            readToken("if");
            Element e = (Element) E();
            readToken("{");
            backPatch(e.getTrueChain(), nextStat);
            S();
            readToken("}");
            backPatch(e.getFalseChain(), nextStat);
            if (isToken("else")) {
                readToken("else");
                readToken("{");
                S();
                readToken("}");
            }
        } else {
            while (isId())
                A();
        }
    }


    public ASTree A() {
        ASTree left = id();
        ASTLeaf op = new ASTLeaf(readToken("="));
        left = new BinaryExpr(Arrays.asList(left, op, E()));
        readToken(";");
        ((BinaryExpr) left).setName("statement");
        addCode((BinaryExpr) left);
        return left;
    }

    public ASTree E() throws ParseException {
        ASTree left = T();
        while (isToken("+") || isToken("-") || isToken("||")) {
            Token token = lexer.read();
            ASTLeaf op = new ASTLeaf(token);
            ASTree right = T();
            if (op.val().equals("||")) {
                Element leftElem = (Element) left;
                Element rightElem = (Element) right;
                Element elem = new Element(Arrays.asList(left, op, right));
                backPatch(leftElem.getFalseChain(), rightElem.getCodeBegin());
                elem.setCodeBegin(leftElem.getCodeBegin());
                elem.setTrueChain(merge(leftElem.getTrueChain(), rightElem.getTrueChain()));
                elem.setFalseChain(rightElem.getFalseChain());
                return elem;
            }
            left = new BinaryExpr(Arrays.asList(left, op, right));
            ((BinaryExpr) left).setName("expression");
            addCode((BinaryExpr) left);
        }
        return left;
    }

    public ASTree T() throws ParseException {
        ASTree left = F();
        while (isToken("*") || isToken("/") || isToken("&&")) {
            ASTLeaf op = new ASTLeaf(lexer.read());
            ASTree right = F();
            if (op.val().equals("&&")) {
                Element leftElem = (Element) left;
                Element rightElem = (Element) right;
                Element elem = new Element(Arrays.asList(left, op, right));
                backPatch(leftElem.getTrueChain(), rightElem.getCodeBegin());
                elem.setCodeBegin(leftElem.getCodeBegin());
                elem.setTrueChain(rightElem.getTrueChain());
                elem.setFalseChain(merge(leftElem.getFalseChain(), rightElem.getFalseChain()));
                return elem;
            }
            left = new BinaryExpr(Arrays.asList(left, op, right));
            ((BinaryExpr) left).setName("term");
            addCode((BinaryExpr) left);
        }
        return left;
    }

    public ASTree F() throws ParseException {
        if (isToken("(")) {
            readToken("(");
            ASTree e = E();
            readToken(")");
            return e;
        } else if (isId()) {
            ASTree id1 = id();
            if (isOp()) {
                Element element = new Element(Arrays.asList(id1, op(), id()));
                element.setTrueChain(Collections.singletonList(nextStat));
                element.setCodeBegin(nextStat);
                element.setFalseChain(Collections.singletonList(nextStat + 1));
                IFStatementCode ifStatementCode = new IFStatementCode(element.val(), result);
                GotoStatementCode gotoStatementCode = new GotoStatementCode(result);
                addCode(ifStatementCode);
                addCode(gotoStatementCode);
                return element;
            }
            return id1;
        } else {
            Token t = lexer.read();
            if (t instanceof NumToken) {
                NumberLiteral n = new NumberLiteral(t);
                return n;
            } else
                throw new ParseException(t);
        }
    }


    public ASTree id() {
        Token t = lexer.read();
        if (t instanceof IdToken) {
            return new Identifier(t);
        } else
            throw new ParseException(t);
    }

    public ASTree op() {
        Token t = lexer.read();
        if (t instanceof OperatorToken) {
            return new ASTLeaf(t);
        } else
            throw new ParseException(t);
    }

    private Token readToken(String name) throws ParseException {
        Token t = lexer.read();
        if (!name.equals(t.getValue()))
            throw new ParseException(t);
        return t;
    }

    private boolean isToken(String name) throws ParseException {
        Token t = lexer.peek(0);
        if (!name.equals(t.getValue()))
            return false;
        return true;
    }

    private boolean isId() throws ParseException {
        Token t = lexer.peek(0);
        if (t instanceof IdToken) {
            return true;
        } else
            return false;
    }

    private boolean isOp() throws ParseException {
        Token t = lexer.peek(0);
        if (t instanceof OperatorToken) {
            return true;
        } else
            return false;
    }

    public static abstract class Code {
        protected int lineNumber;

        public int getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public abstract String print();
    }

    public static class AssignmentCode extends Code {
        public static int order;
        private final String print;

        public AssignmentCode(String print) {
            this.print = print;

        }

        @Override
        public String print() {
//            if (!binaryExpr.operator().equals("="))
//                return "(" + getLineNumber() + ") " + binaryExpr.operator() + " " + binaryExpr.left().val() + " " + binaryExpr.right().val() + " " + "T" + order++;
//            else {
//                return "(" + getLineNumber() + ") " + binaryExpr.operator() + " " + " " + "   " + binaryExpr.right().val() + " " + binaryExpr.left().val();
//            }
            return "(" + getLineNumber() + ") " + print;
        }
    }

    public static class GotoStatementCode extends Code {
        protected int target;
        protected List<Code> result;

        public GotoStatementCode(List<Code> result) {
            this.result = result;
        }

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        @Override
        public String print() {
            return "(" + getLineNumber() + ") " + "goto " + result.get(target).getLineNumber();
        }
    }

    public static class IFStatementCode extends GotoStatementCode {
        private String boolStatement;

        public IFStatementCode(String boolStatement, List<Code> result) {
            super(result);
            this.boolStatement = boolStatement;
        }

        @Override
        public String print() {
            return "(" + getLineNumber() + ") " + "if " + boolStatement + " goto " + result.get(target).getLineNumber();
        }
    }

    public List<Integer> merge(List<Integer> chain1, List<Integer> chain2) {
        List<Integer> chain = new ArrayList<>();
        for (Integer c : chain1) {
            if (chain.contains(c))
                continue;
            chain.add(c);
        }
        for (Integer c : chain2) {
            if (chain.contains(c))
                continue;
            chain.add(c);
        }
        return chain;
    }

    public void backPatch(List<Integer> chain, int codeBegin) {
        for (Integer code : chain) {
            ((GotoStatementCode) result.get(code)).setTarget(codeBegin);
        }
    }

    public void addCode(Code code) {
        result.add(code);
        code.setLineNumber(nextStat);
        nextStat++;
    }

    public void addCode(BinaryExpr binaryExpr) {
        if (!binaryExpr.operator().equals("="))
            addCode(new AssignmentCode(
                    binaryExpr.operator() + " " + binaryExpr.left().val() + " " + binaryExpr.right().val() + " " + "T" + order++));
        else {
            addCode(new AssignmentCode(
                    binaryExpr.operator() + " " + " " + "   " + binaryExpr.right().val() + " " + binaryExpr.left().val()));
        }
    }

    public static void main(String[] args) {
        InputStream in = Lexer.class.getResourceAsStream("IF-ELSE-Test.txt");
        Lexer lexer = new Lexer(new InputStreamReader(in));
        IfElseStatementTranslator p = new IfElseStatementTranslator(lexer);
        p.S();
        for (Code code : p.result) {
            System.out.println(code.print());
        }
    }
}