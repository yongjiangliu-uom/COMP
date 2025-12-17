# COM6516 Statistical Language Model

> A Java-based Statistical Language Model built from scratch for academic demonstration of NLP fundamentals, data structures, and hashing strategies.

---

## 📌 Project Overview

This repository contains a **Java Swing application** implementing a **Statistical Language Model** for the **COM6516 Object Oriented Programming** module.

The project focuses on:

* Core **Natural Language Processing (NLP)** concepts
* Manual construction of **N-gram models** (Unigram, Bigram, Trigram)
* Implementation of **custom data structures** without using the Java Collections Framework
* Empirical comparison of **hash function strategies**

Unlike production-ready NLP libraries, this project is intentionally educational, prioritising **algorithmic transparency**, **performance analysis**, and **software design decisions**.

---

## ✨ Key Features

### 🔹 N-Gram Language Modelling

* Supports **Unigram**, **Bigram**, and **Trigram** models
* Computes conditional probabilities to predict the next word in a sequence
* Generates sequences of up to **20 words** from a user-provided seed phrase

### 🔹 Custom Data Structures

* **MyHashTable** – custom hash table using separate chaining
* **MyLinkedObject** – linked list node storing word-frequency pairs

> **Design Choice**
> All collision chains are handled iteratively (not recursively) to prevent `StackOverflowError` under extreme collision scenarios.

### 🔹 Hash Function Strategies

Users can dynamically switch between hashing strategies to observe performance differences:

* **Polynomial Hash Function**
  Efficient rolling hash:

  [ h = 31h + c ]

* **First Letter Hash Function**
  Deliberately inefficient strategy using the first character of each word, included for performance comparison and teaching purposes.

### 🔹 Hash Table Visualisation

* Custom Swing component (**HistogramPanel**)
* Visualises bucket distribution and collision chain lengths
* Demonstrates the *Avalanche Effect* (or lack thereof)

---

## ⚠️ Performance Notice

### First Letter Hash (Expected Slow Behaviour)

When loading large corpora (e.g. `news.txt`) with **First Letter Hash** enabled:

* The application may appear unresponsive for **1–2 minutes**
* This is **expected behaviour**, not a bug

**Explanation**:

* Thousands of words share the same initial letter
* Buckets degrade into very long sorted linked lists (often >4000 nodes)
* Insertions degrade to **O(N²)** time complexity

**User Action**:

* Please wait until the status bar displays **"Loaded"**
* Do not force-close the application

The **Polynomial Hash** completes loading almost instantly due to near **O(1)** distribution.

---

## 🚀 Getting Started

### Requirements

* **Java Development Kit (JDK) 17+**

### Build & Run

```bash
# Compile source files
javac *.java

# Launch application
java MyLanguageModel
```

No external dependencies or build tools are required.

---

## 📂 Project Structure

```
MyLanguageModel.java         # Application entry point & GUI controller
MyHashTable.java             # Custom hash table implementation
MyLinkedObject.java          # Linked list node for collision chains
MyHashFunction.java          # Abstract hash function interface
PolynomialHashFunction.java  # Efficient hashing strategy
FirstLetterHashFunction.java # Inefficient hash for comparison
HistogramPanel.java          # Hash table visualisation component
```

---

## 🎓 Academic Context

This project was developed as part of:

* **Module:** COM6516 – Object Oriented Programming
* **Institution:** University of Manchester

The repository is intended for **educational and demonstration purposes**.

---

## 👤 Author

**Yongjiang Liu**
BSc Undergraduate Student
University of Manchester

---

## 📄 License

This project is provided for **academic use only**. Reuse or redistribution should retain attribution to the original author.
