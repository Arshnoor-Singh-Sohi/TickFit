package org.example.test;

import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord;

    public TrieNode() {}
}

public class WordCompletion {
    private TrieNode root;

    public WordCompletion(Set<String> vocabulary) {
        root = new TrieNode();
        for (String word : vocabulary) {
            insert(word);
        }
    }

    private void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEndOfWord = true;
    }

    public List<String> complete(String prefix) {
        List<String> completions = new ArrayList<>();
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return completions;
        }
        findCompletions(node, prefix, completions);
        return completions;
    }

    private void findCompletions(TrieNode node, String prefix, List<String> completions) {
        if (node.isEndOfWord) completions.add(prefix);
        for (char c : node.children.keySet()) {
            findCompletions(node.children.get(c), prefix + c, completions);
        }
    }
}