//package org.example.testUI;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.*;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.example.test.FrequencyCounter;
//import org.example.test.SpellChecker;
//import org.example.test.VocabularyBuilder;
//import org.example.test.WordCompletion;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class SmartwatchRecommendationSystem {
//    // Existing private fields remain the same
//    private Workbook appleWorkbook;
//    private Workbook gshockWorkbook;
//    private Workbook garminWorkbook;
//    private Workbook noiseWorkbook;
//    private Workbook workbook;
//    private VocabularyBuilder vocabularyBuilder;
//    private SpellChecker spellChecker;
//    private WordCompletion wordCompletion;
//    private FrequencyCounter frequencyCounter;
//    private Set<String> watchNames;
//
//
//    // Modify recommendSmartwatch to accept a list to store recommendations
//    public void recommendSmartwatch(Set<String> selectedFeatures, List<String> recommendations) {
//        recommendations.clear(); // Clear existing recommendations
//
//        recommendations.addAll(checkWorkbook(appleWorkbook, selectedFeatures));
//        recommendations.addAll(checkWorkbook(gshockWorkbook, selectedFeatures));
//        recommendations.addAll(checkWorkbook(garminWorkbook, selectedFeatures));
//        recommendations.addAll(checkWorkbook(noiseWorkbook, selectedFeatures));
//    }
//
//    // Modify display methods to write to a StringBuilder instead of System.out
//    public void displayWatchDetails(String modelName, StringBuilder details) {
//        Sheet sheet = appleWorkbook.getSheet(modelName);
//        if (sheet == null) {
//            details.append("Model not found.");
//            return;
//        }
//
//        details.append("Details for ").append(modelName).append(":\n");
//        details.append("+--------------------------+--------------------------------------------------+\n");
//        details.append(String.format("| %-24s | %-48s |\n", "Feature", "Details"));
//        details.append("+--------------------------+--------------------------------------------------+\n");
//
//        String currentHeading = "";
//        for (Row row : sheet) {
//            Cell headingCell = row.getCell(0);
//            Cell detailCell = row.getCell(1);
//
//            if (headingCell != null) {
//                String heading = headingCell.getStringCellValue().trim();
//                if (!heading.isEmpty() && !heading.equals(currentHeading)) {
//                    details.append("+--------------------------+--------------------------------------------------+\n");
//                    details.append(String.format("| %-73s |\n", heading));
//                    details.append("+--------------------------+--------------------------------------------------+\n");
//                    currentHeading = heading;
//                }
//            }
//
//            // Similar logic to console version, but appending to StringBuilder
//            if (detailCell != null) {
//                String detail = detailCell.getStringCellValue().trim();
//                if (!detail.isEmpty()) {
//                    String[] words = detail.split("\\s+");
//                    StringBuilder line = new StringBuilder();
//                    for (String word : words) {
//                        if (line.length() + word.length() > 48) {
//                            details.append(String.format("| %-24s | %-48s |\n",
//                                    line.length() == 0 ? headingCell.getStringCellValue().trim() : "",
//                                    line.toString().trim()));
//                            line = new StringBuilder();
//                        }
//                        line.append(word).append(" ");
//                    }
//                    if (line.length() > 0) {
//                        details.append(String.format("| %-24s | %-48s |\n",
//                                line.length() == 0 ? headingCell.getStringCellValue().trim() : "",
//                                line.toString().trim()));
//                    }
//                }
//            }
//        }
//        details.append("+--------------------------+--------------------------------------------------+\n");
//    }
//
//    // Similar modifications for other display methods
//    public void displayGShockWatchDetails(String modelName, StringBuilder details) {
//        Sheet sheet = gshockWorkbook.getSheet(modelName);
//        if (sheet == null) {
//            details.append("Model not found.");
//            return;
//        }
//
//        // Similar implementation to above, but for G-Shock watches
//        details.append("Details for ").append(modelName).append(":\n");
//        // ... (rest of the method follows the same pattern as displayWatchDetails)
//    }
//
//    public void displayGarminWatchDetails(String modelName, StringBuilder details) {
//        Sheet sheet = garminWorkbook.getSheet(modelName);
//        if (sheet == null) {
//            details.append("Model not found.");
//            return;
//        }
//
//        // Similar implementation for Garmin watches
//        details.append("Details for ").append(modelName).append(":\n");
//        // ... (rest of the method follows the same pattern as displayWatchDetails)
//    }
//
//    public void displayNoiseWatchDetails(String modelName, StringBuilder details) {
//        Sheet sheet = noiseWorkbook.getSheet(modelName);
//        if (sheet == null) {
//            details.append("Model not found.");
//            return;
//        }
//
//        // Similar implementation for Noise watches
//        details.append("Details for ").append(modelName).append(":\n");
//        // ... (rest of the method follows the same pattern as displayWatchDetails)
//    }
//
//    // Additional method to get search frequencies for the Swing app
//    public Map<String, Integer> getSearchFrequencies() {
//        return frequencyCounter.getSearchFrequencies();
//    }
//
//    // Other existing methods remain the same
//}
