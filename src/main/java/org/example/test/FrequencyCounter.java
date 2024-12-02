package org.example.test;

import java.util.HashMap;
import java.util.Map;

public class FrequencyCounter {
    private Map<String, Integer> wordFrequency;
    private Map<String, Integer> searchFrequency;

    public FrequencyCounter() {
        this.wordFrequency = new HashMap<>();
        this.searchFrequency = new HashMap<>();
    }

    public void addWordOccurrence(String word) {
        wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
    }

    public void addSearchQuery(String query) {
        searchFrequency.put(query, searchFrequency.getOrDefault(query, 0) + 1);
    }

    public int getWordFrequency(String word) {
        return wordFrequency.getOrDefault(word, 0);
    }

    public int getSearchFrequency(String query) {
        return searchFrequency.getOrDefault(query, 0);
    }

    public Map<String, Integer> getSearchFrequencies() {
        return new HashMap<>(searchFrequency);
    }
}