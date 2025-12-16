package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InputReadFilePanel extends JPanel {
    private JTextArea textArea;
    private JScrollPane scrollPane;

    public InputReadFilePanel(String data, String filePath, String[] misProcessedWords) {
        setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        topPanel.add(new JLabel("File: " + filePath));
        // add the misprocessed words if any to the top panel and make top panel
        // vertically
        if (misProcessedWords.length > 0) {
            scrollPane.setPreferredSize(new Dimension(500, 490));
            JLabel misProcessedWordsLabel = new JLabel();
            misProcessedWordsLabel.setText("There are over " + misProcessedWords.length
                    + " misprocessed words, which are not considered for processing. ");
            // add a border and red background to the label
            misProcessedWordsLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10));
            misProcessedWordsLabel.setOpaque(true);
            misProcessedWordsLabel.setBackground(java.awt.Color.RED);
            topPanel.add(misProcessedWordsLabel);
        }
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load file content on initialization
        textArea.setText(data.toString());
    }

    public void cleanUp() {
        textArea = null;
        scrollPane = null;
    }

}
