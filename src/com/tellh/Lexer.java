package com.tellh;

import com.tellh.Token.*;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    //\s*((//.*)|(VAR|BEGIN|END|IF|THEN|CALL|WHILE|DO|READ|WRITE|CONST|PROCEDURE)|([0-9]+)|(<=|==|>=|&&|\|\||:=|<|>|\+|-|\*|/)|([.;,()])|("(\\"|\\\\|\\n|[^"\\])*")|([A-Z_a-z][A-Z_a-z0-9]*))?
    private static String regexPat = "\\s*((//.*)|" +
            "(?<keyword>" + KeywordToken.getPattern() + ")|" +
            "(?<numConst>" + NumToken.getPattern() + ")|" +
            "(?<operator>" + OperatorToken.getPattern() + ")|" +
            "(?<delimiter>" + DelimiterToken.getPattern() + ")|" +
            "(?<strConst>" + StringToken.getPattern() + ")|" +
            "(?<identifier>" + IdToken.getPattern() + ")" +
            ")?";
    private Pattern pattern = Pattern.compile(regexPat);
    private TokenQueue queue = new TokenQueue();
    private LineNumberReader reader;

    public Lexer(Reader r) {
        reader = new LineNumberReader(r);
    }

    public Token read() {
        if (queue.isEmpty()) {
            boolean hasMore = readLine();
            if (!hasMore)
                return Token.EOF;
        }
        return queue.remove();
    }

    private boolean readLine() {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line == null)
            return false;

        int lineNum = reader.getLineNumber();
        Matcher matcher = pattern.matcher(line);
//        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        do {
            if (!(matcher.find())) {
                continue;
            }
            String keyword = matcher.group("keyword");
            if (!checkNull(keyword)) {
                queue.add(new KeywordToken(lineNum, keyword));
                continue;
            }
            String numConst = matcher.group("numConst");
            if (!checkNull(numConst)) {
                queue.add(new NumToken(lineNum, Integer.valueOf(numConst)));
                continue;
            }
            String operator = matcher.group("operator");
            if (!checkNull(operator)) {
                queue.add(new OperatorToken(lineNum, operator));
                continue;
            }
            String delimiter = matcher.group("delimiter");
            if (!checkNull(delimiter)) {
                queue.add(new DelimiterToken(lineNum, delimiter));
                continue;
            }
            String strConst = matcher.group("strConst");
            if (!checkNull(strConst)) {
                queue.add(new StringToken(lineNum, strConst));
                continue;
            }
            String identifier = matcher.group("identifier");
            if (!checkNull(identifier)) {
                queue.add(new IdToken(lineNum, identifier));
            }
        } while (matcher.end() < line.length());
        return !queue.isEmpty() || readLine();
    }

    public boolean checkNull(String s) {
        return s == null || s.isEmpty();
    }

    public static void main(String[] args) {
        try {
            InputStream in = Lexer.class.getResourceAsStream("source.txt");
            Lexer lexer = new Lexer(new InputStreamReader(in));
            for (Token token; (token = lexer.read()) != Token.EOF; )
                System.out.println(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
