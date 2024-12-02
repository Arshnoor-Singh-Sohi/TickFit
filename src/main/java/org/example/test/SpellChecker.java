package org.example.test;
import java.util.*;

public class SpellChecker {
    private Set<String> watchNames;

    public SpellChecker(Set<String> watchNames) {
        this.watchNames = watchNames;
    }

    public List<String> suggestCorrections(String input) {
        List<String> suggestions = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;

        for (String name : watchNames) {
            int distance = calculateEditDistance(input.toLowerCase(), name.toLowerCase());
            if (distance < minDistance) {
                suggestions.clear(); // Clear previous suggestions
                suggestions.add(name);
                minDistance = distance;
            } else if (distance == minDistance) {
                suggestions.add(name);
            }
        }

        // Limit the number of suggestions
        return suggestions.size() > 5 ? suggestions.subList(0, 5) : suggestions;
    }

    private int calculateEditDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    dp[i][j] = j; // If first string is empty
                } else if (j == 0) {
                    dp[i][j] = i; // If second string is empty
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // Characters match
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1])); // Insert, Remove, Replace
                }
            }
        }
        return dp[len1][len2];
    }
}