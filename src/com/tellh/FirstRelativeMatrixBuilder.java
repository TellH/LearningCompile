package com.tellh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by tlh on 2016/11/8.
 */
public class FirstRelativeMatrixBuilder {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private HashMap<String, Set<String>> head;
    private HashMap<String, Set<String>> last;
    private HashMap<String, Set<String>> grammar;

    public HashMap<String, Set<String>> getGrammar() {
        return grammar;
    }

    public FirstRelativeMatrixBuilder(InputStream in) {
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
        //保存文法的产生式和归类所有的终结符和非终结符。
        nonTerminals = new HashSet<>();
        terminals = new HashSet<>();
        try {
            init(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        head = new HashMap<>();
        last = new HashMap<>();
        findHeadSet(grammar, head);
        findLastSet(grammar, last);

    }

    private void init(LineNumberReader reader) throws IOException {
        grammar = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] strings = line.split("->");
            nonTerminals.add(strings[0]);
            String right = strings[1];
            String[] products = right.split("\\|");
            for (int i = 0; i < products.length; i++) {
                String product = products[i];
                putGrammar(grammar, strings[0], product);
                for (int j = 0; j < product.length(); j++) {
                    char ch = product.charAt(j);
                    if (isNonTerminal(ch))
                        nonTerminals.add(String.valueOf(ch));
                    else terminals.add(String.valueOf(ch));
                }
            }
        }
    }


    private void putGrammar(HashMap<String, Set<String>> grammar, String nonTerminal, String product) {
        if (!grammar.containsKey(nonTerminal))
            grammar.put(nonTerminal, new HashSet<>());
        grammar.get(nonTerminal).add(product);
    }

    private Set<String> findHeadSet(HashMap<String, Set<String>> grammar, HashMap<String, Set<String>> head, String nonTerminal) {
        Set<String> products = grammar.get(nonTerminal);
        Set<String> headSet = head.get(nonTerminal);
        if (headSet == null) {
            headSet = new HashSet<>();
            head.put(nonTerminal, headSet);
        } else if (!headSet.isEmpty())
            return headSet;
        for (String product : products) {
            char first = product.charAt(0);
            if (first != nonTerminal.charAt(0) && first > 'A' && first < 'Z')
                headSet.addAll(findHeadSet(grammar, head, String.valueOf(first)));
            headSet.add(String.valueOf(first));
        }
        return headSet;
    }

    private void findHeadSet(HashMap<String, Set<String>> grammar, HashMap<String, Set<String>> head) {
        putTerminalIntoHeadOrLastSet(head);
        for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
            String nonTerminal = entry.getKey();
            findHeadSet(grammar, head, nonTerminal);
        }
    }

    private void putTerminalIntoHeadOrLastSet(HashMap<String, Set<String>> head) {
        for (String terminal : terminals) {
            HashSet<String> value = new HashSet<>();
            value.add(terminal);
            head.put(terminal, value);
        }
    }

    private void findLastSet(HashMap<String, Set<String>> grammar, HashMap<String, Set<String>> last) {
        for (Map.Entry<String, Set<String>> entry : grammar.entrySet()) {
            String nonTerminal = entry.getKey();
            findLastSet(grammar, last, nonTerminal);
        }
    }

    private Set<String> findLastSet(HashMap<String, Set<String>> grammar, HashMap<String, Set<String>> last, String nonTerminal) {
        Set<String> products = grammar.get(nonTerminal);
        Set<String> lastSet = last.get(nonTerminal);
        if (lastSet == null) {
            lastSet = new HashSet<>();
            last.put(nonTerminal, lastSet);
        } else if (!lastSet.isEmpty())
            return lastSet;
        for (String product : products) {
            char end = product.charAt(product.length() - 1);
            if (end != nonTerminal.charAt(0) && isNonTerminal(end))
                lastSet.addAll(findLastSet(grammar, last, String.valueOf(end)));
            lastSet.add(String.valueOf(end));
        }
        return lastSet;
    }

    private boolean isNonTerminal(char end) {
        return end > 'A' && end < 'Z';
    }

    private boolean isNonTerminal(String end) {
        return end.charAt(0) > 'A' && end.charAt(0) < 'Z';
    }

    public HashMap<SimpleFirstParser.Pair, Integer> build() {
        HashMap<SimpleFirstParser.Pair, Integer> relativeMatrix = new HashMap<>();
        for (Set<String> products : grammar.values()) {
            for (String product : products) {
                for (int i = 0; i < product.length() - 1; i++) {
                    String left = String.valueOf(product.charAt(i));
                    String right = String.valueOf(product.charAt(i + 1));
                    //先加入直接相邻关系
                    relativeMatrix.put(new SimpleFirstParser.Pair(left, right), 0);
                    //左边非终结符的last集大于右边的符号
                    if (isNonTerminal(left)) {
                        Set<String> lasts = this.last.get(left);
                        for (String end : lasts) {
                            relativeMatrix.put(new SimpleFirstParser.Pair(end, right), 1);
                        }
                    }
                    //右边非终结符的head集小于左边的符号
                    if (isNonTerminal(right)) {
                        Set<String> firsts = this.head.get(right);
                        for (String first : firsts) {
                            relativeMatrix.put(new SimpleFirstParser.Pair(left, first), -1);
                        }
                    }
                }
            }
        }

        return relativeMatrix;
    }

    public static void main(String[] args) {
        InputStream in = FirstRelativeMatrixBuilder.class.getResourceAsStream("grammar.txt");
        FirstRelativeMatrixBuilder builder = new FirstRelativeMatrixBuilder(in);
        builder.build();
    }
}
