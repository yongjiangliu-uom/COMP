/*
 * Author : Manu Kenchappa Junjanna
 * Email : mkenchappajunjanna1@sheffield.ac.uk
 * Created on Sun Dec 10 2023
 */

package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import constants.Constants;
import constants.Constants.HashFunctionType;
import hash_table.MyHashTable;
import hash_table.NGramAndProbabilityCalculation;
import util.FileReaderUtil;

public class Frame {
    private JFrame frame;
    private Container container;
    private WordAndCountTablePanel hashTablePanel;
    private InputReadFilePanel inputReadFilePanel;
    private JPanel initalButtonPanel;
    private NGramAndProbabilityCalculation nGram;
    private HashFunctionType hashFunctionType;
    private String selectedFilePath = null;
    private int hashTableSize = Constants.HASH_TABLE_SIZE;

    public Frame() {
        // write a code to create a Jframe with full screen
        frame = new JFrame();
        // by default use simple hash function
        hashFunctionType = HashFunctionType.SIMPLE_HASH_FUNCTION;
        container = frame.getContentPane();
    }

    public void renderFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(new Dimension(500 * 2,frame.getHeight()));
        frame.setVisible(true);
        renderFilePicker();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cleanUp();
            }
        });
    }

    private void renderFilePicker() {
        initalButtonPanel = new JPanel();
        initalButtonPanel.setLayout(new BoxLayout(initalButtonPanel, BoxLayout.Y_AXIS));

        JButton pickFileButton = new JButton("Pick File");
        pickFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pickFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                File defaultDirectory = new File("./");
                fileChooser.setCurrentDirectory(defaultDirectory);
                int result = fileChooser.showOpenDialog(container);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    onFilePicked(selectedFile.getAbsolutePath());
                }
            }
        });

        JButton pickNewsButton = new JButton("Pick News");
        pickNewsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pickNewsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFilePicked("./news.txt");
            }
        });

        container.setLayout(new BorderLayout());
        initalButtonPanel.add(pickFileButton);
        initalButtonPanel.add(pickNewsButton);
        container.add(initalButtonPanel, BorderLayout.CENTER);
        container.repaint();
        container.revalidate();
    }

    public void toggleMyHashFunction() {
        if (selectedFilePath != null) {
            if (hashFunctionType == HashFunctionType.SIMPLE_HASH_FUNCTION) {
                hashFunctionType = HashFunctionType.POLYNOMIAL_HASH_FUNCTION;
            } else {
                hashFunctionType = HashFunctionType.SIMPLE_HASH_FUNCTION;
            }
            onFilePicked(selectedFilePath);
        }
    }

    public void onFilePicked(String filePath) {
        selectedFilePath = filePath;
        renderLoader();
        StringBuilder wordsInStringBuilder = FileReaderUtil.readFile(filePath, Constants.MAX_CHAR_LIMIT);
        String[] words = wordsInStringBuilder.toString().split("\\s+|\\n");
        List<String> correctWords = new ArrayList<>();
        List<String> missProcessedWords = new ArrayList<>();

        for (String word : words) {
            if (word.matches(".*[^a-z'.].*")) {
                missProcessedWords.add(word);
            } else {
                correctWords.add(word);
            }
        }

        // initalise a instance of nGramProbabilityCalculation, which is later used for
        // probability calculation
        String[] data = correctWords.toArray(new String[0]);
        nGram = new NGramAndProbabilityCalculation(data, hashFunctionType, hashTableSize);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate to show that the data is being processed using the new hash function
                Thread.sleep(1000);
                return null;
            }

            @Override
            protected void done() {
                if (nGram != null) {
                    // render the UI for unigram
                    container.removeAll();
                    renderTopComponent();
                    renderLeftComponent(String.join(" ", correctWords), filePath, nGram.getUniGramHashTable(),
                            missProcessedWords.toArray(new String[0]));
                    renderRightComponent(nGram.getUniGramHashTable());
                    container.repaint();
                    container.revalidate();
                }
            }
        };
        worker.execute();

    }

    private void renderLeftComponent(String data, String filePath,
            MyHashTable uniGramHashTable, String[] misProcessedWords) {

        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BorderLayout());
        inputReadFilePanel = new InputReadFilePanel(data, filePath, misProcessedWords);
        hashTablePanel = new WordAndCountTablePanel(uniGramHashTable);
        ProbabilityCalculationPanel probabilityCalculationLayer = new ProbabilityCalculationPanel(uniGramHashTable,
                nGram);

        JPanel horizontalPanel = new JPanel();
        horizontalPanel.add(inputReadFilePanel);
        horizontalPanel.add(hashTablePanel);

        // Add components to the main panel
        verticalPanel.add(horizontalPanel, BorderLayout.NORTH); // Top components
        verticalPanel.add(probabilityCalculationLayer, BorderLayout.SOUTH); // Bottom component

        container.add(verticalPanel);
    }

    private void renderTopComponent() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("\"" +
                hashFunctionType.getDisplayName() + "\" is used to calculate the below results");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton toggleHashFunctionButton = new JButton("Change Hash Function");
        toggleHashFunctionButton.setHorizontalAlignment(SwingConstants.CENTER);
        toggleHashFunctionButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        toggleHashFunctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMyHashFunction();
            }
        });
        topPanel.add(titleLabel);
        topPanel.add(toggleHashFunctionButton);
        container.add(topPanel, BorderLayout.NORTH);
    }

    private void renderLoader() {
        JLabel progressBarText = new JLabel();
        progressBarText.setText("Processing '" + selectedFilePath + "' using " + hashFunctionType.getDisplayName()
                + " and Hash Table Size " + hashTableSize + ".\nPlease wait...");
        progressBarText.setHorizontalAlignment(SwingConstants.CENTER);
        progressBarText.setFont(new Font("Arial", Font.BOLD, 26));
        container.removeAll();
        container.add(progressBarText, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();
    }

    // ...

    private void renderRightComponent(MyHashTable uniGramHashTable) {
        StatisticsValuePanel barGraph = new StatisticsValuePanel(uniGramHashTable);
        container.add(barGraph, BorderLayout.EAST);

        // Create the panel with text field and button
        JPanel textFieldButtonPanel = new JPanel();
        textFieldButtonPanel.setLayout(new BoxLayout(textFieldButtonPanel, BoxLayout.X_AXIS));

        JTextField textField = new JTextField(10);
        textField.setToolTipText("Hash Table Size (Press Enter to update)");
        textField.setMaximumSize(new Dimension(100, 30));
        JButton button = new JButton("Update Hash Table Size");
        textFieldButtonPanel.add(new JLabel("Current Table Size:" + " "));
        textField.setText(hashTableSize + "");
        textFieldButtonPanel.add(textField);
        textFieldButtonPanel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                try {
                    int newSize = Integer.parseInt(text);
                    if (newSize > 0) {
                        hashTableSize = newSize;
                        onFilePicked(selectedFilePath);
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input");
                }
            }
        });

        // Align the panel vertically to the barGraph panel
        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Change Hash Table Size");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        verticalPanel.add(titleLabel);
        verticalPanel.add(textFieldButtonPanel);
        verticalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        verticalPanel.add(barGraph);
        container.add(verticalPanel, BorderLayout.EAST);
    }

    public void cleanUp() {
        frame.dispose();
        if (hashTablePanel != null) {
            hashTablePanel.cleanUp();
        }
        if (inputReadFilePanel != null) {
            inputReadFilePanel.cleanUp();
        }
        // Release references to objects
        inputReadFilePanel = null;
        hashTablePanel = null;
    }
}
