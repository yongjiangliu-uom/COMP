# COM6516 Statistical Language Model

## Overview
This project is a Java-based implementation of a Statistical Language Model, developed for the COM6516 module. It demonstrates the fundamental concepts of Natural Language Processing (NLP) by building N-gram models (Unigrams, Bigrams, Trigrams) from scratch.

Unlike standard applications, this project implements custom data structures (**Hash Table** and **Linked List**) without relying on the Java Collections Framework, providing a deep dive into algorithmic efficiency and data structure design.

## 🚀 How to Run

This application is a standalone Java Swing application. No external libraries are required.

### Prerequisites
* Java Development Kit (JDK) 17 or higher.

### Compilation & Execution
Navigate to the `code` directory in your terminal and run the following commands:

```bash
# 1. Compile all Java files
javac *.java

# 2. Run the main application
java MyLanguageModel
⚠️ Important Note: Performance WarningFirst Letter Hash FunctionThe application includes a "First Letter Hash" option to demonstrate the impact of poor hash functions.Behavior: When loading news.txt with "First Letter Hash" selected, the application may appear to freeze or become unresponsive for 1-2 minutes.Reason: This is normal and expected behavior. The algorithm intentionally clusters thousands of words starting with the same letter (e.g., 't', 's') into single linked lists. Inserting thousands of items into a sorted linked list has a time complexity of $O(N^2)$, causing significant processing delay.Action: Please wait patiently until the status bar updates to "Loaded". Do not force close the application.Contrast this with the Polynomial Hash, which loads instantly due to efficient $O(1)$ distribution.✨ Key Features1. Custom Data StructuresMyHashTable: A custom implementation of a hash table handling collision via chaining.MyLinkedObject: A custom linked list node that stores word frequencies.Design Note: This class uses an Iterative approach (loops) instead of Recursion. This was a deliberate design choice to prevent StackOverflowError when handling the extreme chain lengths (depth > 4000) produced by the First Letter Hash.2. Hash Function StrategyUsers can switch strategies at runtime to compare efficiency:Polynomial Hash: Uses a rolling hash algorithm ($h = 31h + c$) to distribute words uniformly.First Letter Hash: Uses the first character ($h = char[0] \% size$), serving as a case study for collision clustering.3. Visualization (HistogramPanel)A custom-painted Swing component that visualizes the internal state of the Hash Table.Displays the distribution of collision chain lengths, allowing visual verification of the "Avalanche Effect" (or lack thereof).4. N-Gram PredictionBigram & Trigram Models: The system builds statistical models to predict the next word in a sequence based on conditional probabilities.GUI: Users can input a starting phrase (e.g., "it is") and generate a sequence of up to 20 likely following words.📂 Project StructureMyLanguageModel.java: Main entry point and GUI controller.MyHashTable.java: Core data structure managing buckets.MyLinkedObject.java: Node structure for collision chains.MyHashFunction.java (Abstract): Base class for hashing strategies.PolynomialHashFunction.java: Efficient implementation.FirstLetterHashFunction.java: Inefficient implementation for comparison.HistogramPanel.java: Custom component for drawing statistical graphs.AuthorStudent Name: Yongjiang LiuModule: COM6516 Object Oriented Programming