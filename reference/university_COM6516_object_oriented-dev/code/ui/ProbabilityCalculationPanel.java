package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import hash_table.MyHashTable;
import hash_table.NGramAndProbabilityCalculation;

/**
 * BottomLayer
 */
public class ProbabilityCalculationPanel extends JPanel {
    NGramAndProbabilityCalculation nGramProbabilityCalculation;

    public ProbabilityCalculationPanel(MyHashTable hashTable, NGramAndProbabilityCalculation nGramProbabilityCalculation) {
        this.nGramProbabilityCalculation = nGramProbabilityCalculation;

        JPanel probabilityPanel = new JPanel();
        probabilityPanel.setLayout(new BoxLayout(probabilityPanel, BoxLayout.Y_AXIS));
        probabilityPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        EmptyBorder emptyBorder = new EmptyBorder(10, 10, 10, 10);
        LineBorder lineBorder = new LineBorder(Color.BLACK);
        CompoundBorder compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        probabilityPanel.setBorder(compoundBorder);

        JLabel titleLabel = new JLabel("Word Prediction ( Probability Calculation)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        probabilityPanel.add(titleLabel);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel biagramLabel = new JLabel("Input Inital words ");
        JTextField inputTextField = new JTextField(10);
        JButton calculateUsingBigram = new JButton("Calculate using Bigram");
        JButton calculateUsingTrigram = new JButton("Calculate using Trigram");
        JButton clearButton = new JButton("Clear");

        inputPanel.add(biagramLabel);
        inputPanel.add(inputTextField);
        inputPanel.add(calculateUsingBigram);
        inputPanel.add(calculateUsingTrigram);
        inputPanel.add(clearButton);

        probabilityPanel.add(inputPanel);

        JTextArea biagramOutput = new JTextArea(3, 15);
        biagramOutput.setEditable(false);
        biagramOutput.setWrapStyleWord(true);
        biagramOutput.setLineWrap(true);
        biagramOutput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        biagramOutput.setBackground(Color.LIGHT_GRAY);

        JTextArea trigramOutput = new JTextArea(3, 15);
        trigramOutput.setEditable(false);
        trigramOutput.setWrapStyleWord(true);
        trigramOutput.setLineWrap(true);
        trigramOutput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        trigramOutput.setBackground(Color.LIGHT_GRAY);

        probabilityPanel.add(biagramOutput);
        probabilityPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        probabilityPanel.add(trigramOutput);

        probabilityPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextField.setText("");
                biagramOutput.setText("");
                biagramOutput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                biagramOutput.setBackground(Color.LIGHT_GRAY);
                trigramOutput.setText("");
                trigramOutput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                trigramOutput.setBackground(Color.LIGHT_GRAY);
            }
        });
        calculateUsingBigram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputTextField.getText();
                try {
                    biagramOutput.setText(nGramProbabilityCalculation.get20MostFrequentWordsUsingBigram(input));
                    biagramOutput.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                    biagramOutput.setBackground(new Color(220, 255, 220));
                } catch (IllegalArgumentException exception) {
                    biagramOutput.setBorder(BorderFactory.createLineBorder(Color.RED));
                    biagramOutput.setBackground(new Color(255, 220, 220));
                    biagramOutput.setText(exception.getMessage());
                } catch (Exception ex) {
                    biagramOutput.setBorder(BorderFactory.createLineBorder(Color.RED));
                    biagramOutput.setBackground(new Color(255, 220, 220));
                    biagramOutput.setText("Please input valid words");
                }
            }
        });

        calculateUsingTrigram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputTextField.getText();
                try {
                    trigramOutput.setText(nGramProbabilityCalculation.get20MostFrequentWordsUsingTrigram(input));
                    trigramOutput.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                    trigramOutput.setBackground(new Color(220, 255, 220));
                } catch (IllegalArgumentException exception) {
                    trigramOutput.setBorder(BorderFactory.createLineBorder(Color.RED));
                    trigramOutput.setBackground(new Color(255, 220, 220));
                    trigramOutput.setText(exception.getMessage());
                } catch (Exception ex) {
                    trigramOutput.setBorder(BorderFactory.createLineBorder(Color.RED));
                    trigramOutput.setBackground(new Color(255, 220, 220));
                    trigramOutput.setText(ex.getMessage());
                }
            }
        });

        add(probabilityPanel);
    }

}