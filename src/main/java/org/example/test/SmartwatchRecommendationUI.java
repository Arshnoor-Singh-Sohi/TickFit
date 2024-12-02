package org.example.test;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartwatchRecommendationUI extends JFrame {
    private Workbook appleWorkbook;
    private Workbook gshockWorkbook;
    private Workbook garminWorkbook;
    private Workbook noiseWorkbook;
    private JTable table;
    private VocabularyBuilder vocabularyBuilder;
    private SpellChecker spellChecker;
    private WordCompletion wordCompletion;
    private FrequencyCounter frequencyCounter;

    public SmartwatchRecommendationUI(String appleFilePath, String gshockFilePath, String garminFilePath, String noiseFilePath) throws IOException {
        FileInputStream appleFileInputStream = new FileInputStream(appleFilePath);
        this.appleWorkbook = new XSSFWorkbook(appleFileInputStream);

        FileInputStream gshockFileInputStream = new FileInputStream(gshockFilePath);
        this.gshockWorkbook = new XSSFWorkbook(gshockFileInputStream);

        FileInputStream garminFileInputStream = new FileInputStream(garminFilePath);
        this.garminWorkbook = new XSSFWorkbook(garminFileInputStream);

        FileInputStream noiseFileInputStream = new FileInputStream(noiseFilePath);
        this.noiseWorkbook = new XSSFWorkbook(noiseFileInputStream);

        this.vocabularyBuilder = new VocabularyBuilder(appleWorkbook, gshockWorkbook);
        this.spellChecker = new SpellChecker(vocabularyBuilder.getVocabulary());
        this.wordCompletion = new WordCompletion(vocabularyBuilder.getVocabulary());
        this.frequencyCounter = new FrequencyCounter();

        initUI();
    }

    private void initUI() {
        setTitle("Smartwatch Recommendation System");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Add title
        JLabel titleLabel = new JLabel("Smartwatch Recommendation System", SwingConstants.CENTER);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Feature selection
        JPanel featurePanel = new JPanel(new GridLayout(0, 1));
        String[] features = {
                "OLED Always-on Retina display", "OLED Retina display", "ECG",
                "Up to 18 hours", "Up to 36 hours", "Up to 72 hours in Low Power Mode",
                "High and low heart rate notifications", "Blood Oxygen app", "Sleep Tracking", "GPS",
                "Swimproof", "Fast charging", "Cycle Tracking", "Bluetooth", "Offline maps",
                "aluminum", "titanium", "Mindfulness app with state of mind tracking",
                "Water-resistant", "Apple Pay", "Mineral Glass", "Shock Resistant", "REALTIME STAMINA", "Do not disturb mode"
        };
        JCheckBox[] featureCheckboxes = new JCheckBox[features.length];

        for (int i = 0; i < features.length; i++) {
            featureCheckboxes[i] = new JCheckBox(features[i]);
            featurePanel.add(featureCheckboxes[i]);
        }
        panel.add(featurePanel, BorderLayout.WEST);

        // Table
        table = new JTable();
        table.setModel(new DefaultTableModel(new Object[]{"Model Name", "Price"}, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton recommendButton = new JButton("Recommend");
        JButton detailsButton = new JButton("Details");
        JButton priceButton = new JButton("Filter by Price");

        recommendButton.addActionListener(e -> {
            Set<String> selectedFeatures = new HashSet<>();
            for (JCheckBox box : featureCheckboxes) {
                if (box.isSelected()) selectedFeatures.add(box.getText());
            }
            displayRecommendations(selectedFeatures);
        });

        detailsButton.addActionListener(e -> {
            String watchType = JOptionPane.showInputDialog(this, "Enter watch type (Apple, Garmin, GShock, Noise):");
            if (watchType == null || watchType.trim().isEmpty()) {
                return;
            }

            String watchName = JOptionPane.showInputDialog(this, "Enter the watch name:");
            if (watchName == null || watchName.trim().isEmpty()) {
                return;
            }

            displayDetails(watchName.trim(), watchType.trim());
        });
        ;

        priceButton.addActionListener(e -> {
            // Create a panel with radio buttons for price range selection
            JRadioButton range1 = new JRadioButton("$0 - $100");
            JRadioButton range2 = new JRadioButton("$100 - $200");
            JRadioButton range3 = new JRadioButton("$200 - $500");
            JRadioButton range4 = new JRadioButton("$500+");

            // Group the radio buttons to allow only one selection
            ButtonGroup group = new ButtonGroup();
            group.add(range1);
            group.add(range2);
            group.add(range3);
            group.add(range4);

            JPanel panelPrice = new JPanel(new GridLayout(0, 1));
            panelPrice.add(new JLabel("Select a price range:"));
            panelPrice.add(range1);
            panelPrice.add(range2);
            panelPrice.add(range3);
            panelPrice.add(range4);

            int result = JOptionPane.showConfirmDialog(
                    this, panelPrice, "Price Range Selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                double minPrice = 0, maxPrice = Double.MAX_VALUE;

                if (range1.isSelected()) {
                    minPrice = 0;
                    maxPrice = 100;
                } else if (range2.isSelected()) {
                    minPrice = 100;
                    maxPrice = 200;
                } else if (range3.isSelected()) {
                    minPrice = 200;
                    maxPrice = 500;
                } else if (range4.isSelected()) {
                    minPrice = 500;
                    maxPrice = Double.MAX_VALUE;
                } else {
                    JOptionPane.showMessageDialog(this, "No price range selected.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Display watches in the selected price range
                displayPriceFilteredWatches(minPrice, maxPrice);
            }
        });



        buttonPanel.add(recommendButton);
        buttonPanel.add(detailsButton);
        buttonPanel.add(priceButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void displayRecommendations(Set<String> selectedFeatures) {
        List<String> recommendations = new ArrayList<>();
        recommendations.addAll(checkWorkbook(appleWorkbook, selectedFeatures));
        recommendations.addAll(checkWorkbook(gshockWorkbook, selectedFeatures));
        recommendations.addAll(checkWorkbook(garminWorkbook, selectedFeatures));
        recommendations.addAll(checkWorkbook(noiseWorkbook, selectedFeatures));

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Recommended Watches"}, 0);
        for (String watch : recommendations) {
            model.addRow(new Object[]{watch});
        }

        if (recommendations.isEmpty()) {
            model.addRow(new Object[]{"No matching watches found."});
        }
        table.setModel(model);
    }

    private void displayDetails(String watchName, String watchType) {
        Sheet sheet = null;

        // Determine the appropriate workbook based on the watch type
        switch (watchType.toLowerCase()) {
            case "apple":
                sheet = appleWorkbook.getSheet(watchName);
                break;
            case "garmin":
                sheet = garminWorkbook.getSheet(watchName);
                break;
            case "gshock":
                sheet = gshockWorkbook.getSheet(watchName);
                break;
            case "noise":
                sheet = noiseWorkbook.getSheet(watchName);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid watch type.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        // If no sheet is found, notify the user
        if (sheet == null) {
            JOptionPane.showMessageDialog(this, "Watch not found in " + watchType + " records.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a table model to display the details
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Feature", "Detail"}, 0);

        String currentHeading = "";

        for (Row row : sheet) {
            Cell headingCell = row.getCell(0);
            Cell detailCell = row.getCell(1);

            if (headingCell != null) {
                String heading = headingCell.getStringCellValue().trim();
                if (!heading.isEmpty() && !heading.equals(currentHeading)) {
                    // Add a heading row
                    model.addRow(new Object[]{heading, ""});
                    currentHeading = heading;
                }
            }

            if (detailCell != null) {
                String detail = detailCell.getStringCellValue().trim();
                if (!detail.isEmpty()) {
                    // Add the detail row
                    model.addRow(new Object[]{"", detail});
                }
            }
        }

        // Update the table to display the details
        table.setModel(model);
    }


    private void displayPriceFilteredWatches(double minPrice, double maxPrice) {
        List<String> filteredWatches = new ArrayList<>();

        // Check watches in all workbooks for the selected price range
        filteredWatches.addAll(getWatchesByPrice(appleWorkbook, minPrice, maxPrice));
        filteredWatches.addAll(getWatchesByPrice(gshockWorkbook, minPrice, maxPrice));
        filteredWatches.addAll(getWatchesByPrice(garminWorkbook, minPrice, maxPrice));
        filteredWatches.addAll(getWatchesByPrice(noiseWorkbook, minPrice, maxPrice));

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Filtered Watches"}, 0);
        for (String watch : filteredWatches) {
            model.addRow(new Object[]{watch});
        }

        if (filteredWatches.isEmpty()) {
            model.addRow(new Object[]{"No watches found in the selected price range."});
        }
        table.setModel(model);
    }


    private List<String> checkWorkbook(Workbook workbook, Set<String> selectedFeatures) {
        List<String> matches = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (matchesFeatures(sheet, selectedFeatures)) {
                matches.add(sheet.getSheetName());
            }
        }
        return matches;
    }

    private boolean matchesFeatures(Sheet sheet, Set<String> selectedFeatures) {
        for (String feature : selectedFeatures) {
            boolean found = false;
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().contains(feature)) {
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            if (!found) return false;
        }
        return true;
    }

    private List<String> getWatchesByPrice(Workbook workbook, double minPrice, double maxPrice) {
        List<String> watches = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            double price = extractPrice(sheet);
            if (price >= minPrice && price <= maxPrice) {
                watches.add(sheet.getSheetName());
            }
        }
        return watches;
    }

    private double extractPrice(Sheet sheet) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING) {
                    String cellValue = cell.getStringCellValue().trim();
                    // Check for "$679" or "From $679" format
                    Matcher matcher = Pattern.compile("\\$\\d+(\\.\\d{2})?").matcher(cellValue);
                    if (matcher.find()) {
                        return Double.parseDouble(matcher.group().substring(1));
                    }
                    // Check for "$60" format
                    if (cellValue.startsWith("$")) {
                        try {
                            return Double.parseDouble(cellValue.substring(1));
                        } catch (NumberFormatException e) {
                            // Continue to next cell
                        }
                    }
                    // Check for "60" format (without dollar sign)
                    try {
                        return Double.parseDouble(cellValue);
                    } catch (NumberFormatException e) {
                        // Continue to next cell
                    }
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    // Handle numeric cell types
                    return cell.getNumericCellValue();
                }
            }
        }
        return -1; // Price not found
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new SmartwatchRecommendationUI(
                        "AppleWatchComparison.xlsx",
                        "GShockSmartwatchDetails.xlsx",
                        "garmin_models.xlsx",
                        "noise.xlsx"
                ).setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
