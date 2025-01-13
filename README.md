# Smart Watch Recommendation System

## Overview

This project is a Java-based Smart Watch Recommendation System that helps users find the perfect smartwatch based on their preferences and budget. The system includes data from popular brands such as Apple, G-Shock, Garmin, and Noise, offering a wide range of options for users to explore.

## Features

- **Multi-brand Support**: Includes watches from Apple, G-Shock, Garmin, and Noise.
- **Price-based Filtering**: Allows users to set a minimum and maximum price range for recommendations.
- **Spell Check**: Implements an intelligent spell-checking feature to handle user input errors and suggest similar watch names.
- **Detailed Information**: Provides comprehensive details about each recommended watch, including specifications and features.
- **User-friendly Interface**: Offers an interactive command-line interface for easy navigation and selection.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache POI library for Excel file handling

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/Arshnoor-Singh-Sohi/Smart-Watch.git
   ```
2. Navigate to the project directory:
   ```
   cd Smart-Watch
   ```
3. Compile the Java files:
   ```
   javac *.java
   ```

### Usage

1. Run the main class:
   ```
   java SmartwatchRecommendationSystem
   ```
2. Follow the on-screen prompts to:
   - Enter your desired price range
   - Input watch names or partial names
   - Select from suggested watches
   - View detailed information about selected watches

## Project Structure

- `SmartwatchRecommendationSystem.java`: Main class that handles user interaction and recommendation logic.
- `SpellChecker.java`: Implements the spell-checking functionality using edit distance algorithm.
- `ExcelReader.java`: Manages reading watch data from Excel files.
- Excel files containing watch data for each brand (not included in the repository).

## Contributing

Contributions to improve the Smart Watch Recommendation System are welcome. Please follow these steps:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Acknowledgments

- Apache POI library for Excel file handling
