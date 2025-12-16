package ui;
/*
 * Author : Manu Kenchappa Junjanna
 * Email : mkenchappajunjanna1@sheffield.ac.uk
 * Created on Mon Dec 11 2023
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Comparator;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import hash_table.MyHashTable;

public class WordAndCountTablePanel extends JPanel {
    private JTable jTable;
    private JScrollPane scrollPane;
    // deault sorting is based on alphabetical order
    private boolean isSortedByAlphabetical = true;

    public WordAndCountTablePanel(MyHashTable table) {
        setLayout(new BorderLayout());
        TreeMap<String, Integer> map = table.getAllWordAndItsCount();
        // render the unique and total word counts in the table
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("| Total words: " + table.getTotalWordCount() + " ||"));
        topPanel.add(new JLabel("|| Total Unique Words: " + table.getUniqueWordCount() + " |"));
        JButton sortButton = new JButton("Change Sort order");
        sortButton.addActionListener(e -> {
            if (isSortedByAlphabetical) {
                renderByFrequency(map);
                isSortedByAlphabetical = false;
            } else {
                renderAlphabetical(map);
                isSortedByAlphabetical = true;
            }
            repaint();
            revalidate();
        });
        topPanel.add(sortButton);
        add(topPanel, BorderLayout.NORTH);

        // by deafault render the words in alphabetical order
        renderAlphabetical(map);

    }

    private void renderAlphabetical(TreeMap<String, Integer> map) {
        // remove the previous table if any
        if (jTable != null) {
            remove(scrollPane);
        }
        String[] columnNames = { "Word(Sorted Alphabetically)", "Frequency" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (String word : map.keySet()) {
            int count = map.get(word);
            model.addRow(new Object[] { word, count });
        }
        jTable = new JTable(model);
        jTable.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(jTable);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void renderByFrequency(TreeMap<String, Integer> map) {
        // remove the previous table if any
        if (jTable != null) {
            remove(scrollPane);
        }
        // sorting based on frequency
        TreeMap<String, Integer> frequencyOrderMap = new TreeMap<>(
                Comparator.<String, Integer>comparing(map::get).reversed().thenComparing(Comparator.naturalOrder()));
        frequencyOrderMap.putAll(map);
        String[] columnNames = { "Word", "Frequency(Sorted By Frequency)" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (String word : frequencyOrderMap.keySet()) {
            int count = frequencyOrderMap.get(word);
            model.addRow(new Object[] { word, count });
        }
        jTable = new JTable(model);
        jTable.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(jTable);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void cleanUp() {
        jTable = null;
    }

}
