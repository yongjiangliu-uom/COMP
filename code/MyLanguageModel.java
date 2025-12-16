import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MyLanguageModel extends JFrame {
    
    // GUI Components
    private JTextArea txtInput;
    private JTextArea txtOutput;
    private JTable vocabularyTable;
    private DefaultTableModel tableModel;
    private JLabel lblStatus;
    private JTextArea txtStats; 
    private HistogramPanel histogramPanel; 
    private JComboBox<String> cmbHashFunction; 

    // Data Structures
    private MyHashTable unigramTable;
    private MyHashTable bigramTable;
    private MyHashTable trigramTable;
    
    private int totalWordCount = 0;

    public MyLanguageModel() {
        super("COM6516 Language Model - Improved Version");
        initGUI();
    }

    private void initGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1100, 750);
        this.setLayout(new BorderLayout());

        // --- Top Panel: Controls ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnLoad = new JButton("Load news.txt");
        
        JLabel lblHash = new JLabel("Hash Function:");
        String[] hashOptions = {"Polynomial Hash", "First Letter Hash"};
        cmbHashFunction = new JComboBox<>(hashOptions);
        
        JButton btnSortAlpha = new JButton("Sort by Word");
        JButton btnSortCount = new JButton("Sort by Count");
        
        topPanel.add(btnLoad);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(lblHash);
        topPanel.add(cmbHashFunction);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnSortAlpha);
        topPanel.add(btnSortCount);
        
        this.add(topPanel, BorderLayout.NORTH);

        // --- Center Area ---
        String[] columns = {"Word/Gram", "Count"};
        tableModel = new DefaultTableModel(columns, 0);
        vocabularyTable = new JTable(tableModel);
        JScrollPane scrollTable = new JScrollPane(vocabularyTable);
        
        JPanel statsContainer = new JPanel(new BorderLayout());
        
        txtStats = new JTextArea(4, 25);
        txtStats.setEditable(false);
        txtStats.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollStats = new JScrollPane(txtStats);
        scrollStats.setBorder(BorderFactory.createTitledBorder("Mathematical Stats"));
        
        histogramPanel = new HistogramPanel();
        histogramPanel.setBorder(BorderFactory.createTitledBorder("Collision Distribution (Graph)"));
        
        statsContainer.add(histogramPanel, BorderLayout.CENTER);
        statsContainer.add(scrollStats, BorderLayout.SOUTH);
        statsContainer.setPreferredSize(new Dimension(400, 300));

        JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTable, statsContainer);
        leftSplit.setDividerLocation(350);

        txtInput = new JTextArea(3, 30);
        txtInput.setLineWrap(true);
        txtInput.setBorder(BorderFactory.createTitledBorder("Input (Start words)"));
        txtInput.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        txtOutput = new JTextArea(10, 30);
        txtOutput.setBorder(BorderFactory.createTitledBorder("Prediction Output"));
        txtOutput.setEditable(false);
        txtOutput.setLineWrap(true);
        txtOutput.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JPanel predictionPanel = new JPanel(new BorderLayout());
        JPanel inputWrapper = new JPanel(new BorderLayout());
        inputWrapper.add(new JScrollPane(txtInput), BorderLayout.CENTER);
        
        JPanel btnPredPanel = new JPanel(new FlowLayout());
        JButton btnPredictUni = new JButton("Predict (Bigram)");
        JButton btnPredictTri = new JButton("Predict (Trigram)");
        btnPredPanel.add(btnPredictUni);
        btnPredPanel.add(btnPredictTri);
        inputWrapper.add(btnPredPanel, BorderLayout.SOUTH);
        
        predictionPanel.add(inputWrapper, BorderLayout.NORTH);
        predictionPanel.add(new JScrollPane(txtOutput), BorderLayout.CENTER);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, predictionPanel);
        mainSplit.setDividerLocation(450);
        this.add(mainSplit, BorderLayout.CENTER);

        lblStatus = new JLabel(" Ready to load file.");
        lblStatus.setBorder(BorderFactory.createEtchedBorder());
        this.add(lblStatus, BorderLayout.SOUTH);

        // --- Logic ---
        btnLoad.addActionListener(e -> loadDocument());
        btnSortAlpha.addActionListener(e -> updateTable(true));
        btnSortCount.addActionListener(e -> updateTable(false));
        btnPredictUni.addActionListener(e -> predictNextWords(2));
        btnPredictTri.addActionListener(e -> predictNextWords(3));
    }

    private void loadDocument() {
        JFileChooser fileChooser = new JFileChooser(".");
        int retval = fileChooser.showOpenDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            processFile(file);
        }
    }

    private void processFile(File file) {
        try {
            int tableSize = 5000; 
            MyHashFunction hf;
            
            String selectedHash = (String) cmbHashFunction.getSelectedItem();
            if ("First Letter Hash".equals(selectedHash)) {
                hf = new FirstLetterHashFunction(tableSize);
            } else {
                hf = new PolynomialHashFunction(tableSize);
            }
            
            unigramTable = new MyHashTable(tableSize, hf);
            bigramTable = new MyHashTable(tableSize, hf);
            trigramTable = new MyHashTable(tableSize, hf);
            
            totalWordCount = 0;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String prevWord = null;
            String prevPrevWord = null;
            
            // Regex for valid words: only a-z, dot, apostrophe
            Pattern validPattern = Pattern.compile("^[a-z\\.\\']+$");
            boolean warningShown = false;

            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.isEmpty()) continue;

                // Split by whitespace
                String[] tokens = line.split("\\s+");
                
                for (String token : tokens) {
                    // Task 4: Check for invalid characters
                    Matcher m = validPattern.matcher(token);
                    if (!m.matches()) {
                        if (!warningShown) {
                            JOptionPane.showMessageDialog(this, 
                                "Warning: Mis-processed item found: \"" + token + "\"\n" + 
                                "Only 'a-z', '.', and ''' are allowed.\n" +
                                "This item will be skipped (and subsequent errors suppressed).",
                                "Invalid Input Detected", 
                                JOptionPane.WARNING_MESSAGE);
                            warningShown = true;
                        }
                        continue; // Skip invalid word
                    }

                    // Valid word processing
                    unigramTable.insert(token);
                    totalWordCount++;
                    
                    if (prevWord != null) {
                        bigramTable.insert(prevWord + " " + token);
                        if (prevPrevWord != null) {
                            trigramTable.insert(prevPrevWord + " " + prevWord + " " + token);
                        }
                    }
                    prevPrevWord = prevWord;
                    prevWord = token;
                }
            }
            br.close();
            
            lblStatus.setText(" Loaded: " + file.getName() + " | Words: " + totalWordCount + " | Algo: " + selectedHash);
            updateTable(true);
            calculateStatistics();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void calculateStatistics() {
        if (unigramTable == null) return;
        
        MyLinkedObject[] table = unigramTable.getTable();
        List<Integer> lengths = new ArrayList<>();
        Map<Integer, Integer> lengthDist = new TreeMap<>(); 
        
        long sum = 0;
        int nonEmptySlots = 0;
        
        for (MyLinkedObject node : table) {
            int len = 0;
            MyLinkedObject curr = node;
            while (curr != null) {
                len++;
                curr = curr.getNext();
            }
            lengths.add(len);
            if (len > 0) nonEmptySlots++;
            sum += len;
            
            lengthDist.put(len, lengthDist.getOrDefault(len, 0) + 1);
        }
        
        histogramPanel.setDistribution(lengthDist);
        
        double avg = (double) sum / table.length; 
        double varianceSum = 0;
        for (int len : lengths) {
            varianceSum += Math.pow(len - avg, 2);
        }
        double stdDev = Math.sqrt(varianceSum / table.length);
        
        txtStats.setText(String.format(
            " Hash Table Analysis:\n" +
            " --------------------\n" +
            " Table Size (m) : %d\n" +
            " Used Slots     : %d\n" +
            " Load Factor    : %.2f%%\n" +
            " Avg List Len   : %.4f\n" +
            " Std Deviation  : %.4f\n", 
            table.length, nonEmptySlots, (double)nonEmptySlots/table.length*100, avg, stdDev));
    }

    private void updateTable(boolean sortByAlpha) {
        if (unigramTable == null) return;
        tableModel.setRowCount(0);
        List<MyLinkedObject> allWords = new ArrayList<>();
        for (MyLinkedObject node : unigramTable.getTable()) {
            MyLinkedObject curr = node;
            while (curr != null) {
                allWords.add(curr);
                curr = curr.getNext();
            }
        }
        if (sortByAlpha) {
            allWords.sort(Comparator.comparing(MyLinkedObject::getWord));
        } else {
            allWords.sort((a, b) -> {
                int cmp = Integer.compare(b.getCount(), a.getCount());
                if (cmp == 0) return a.getWord().compareTo(b.getWord());
                return cmp;
            });
        }
        for (MyLinkedObject obj : allWords) {
            tableModel.addRow(new Object[]{obj.getWord(), obj.getCount()});
        }
    }

    private void predictNextWords(int n) {
        String input = txtInput.getText().toLowerCase().trim();
        if (input.isEmpty()) return;
        StringBuilder result = new StringBuilder();
        result.append(input);
        String currentSeq = input;
        
        for (int i = 0; i < 20; i++) {
            String nextWord = findMostLikelyNext(currentSeq, n);
            if (nextWord == null) break;
            result.append(" ").append(nextWord);
            String[] parts = (currentSeq + " " + nextWord).split("\\s+");
            if (n == 3 && parts.length >= 2) {
                currentSeq = parts[parts.length-2] + " " + parts[parts.length-1];
            } else {
                currentSeq = nextWord;
            }
        }
        txtOutput.setText(result.toString());
    }

    private String findMostLikelyNext(String context, int n) {
        String bestWord = null;
        double maxCount = -1.0;
        String[] words = context.split("\\s+");
        String prefix;
        if (n == 3) {
            if (words.length < 2) return null;
            prefix = words[words.length-2] + " " + words[words.length-1];
        } else {
            if (words.length < 1) return null;
            prefix = words[words.length-1];
        }
        MyHashTable targetTable = (n == 3) ? trigramTable : bigramTable;
        if (targetTable == null) return null;

        // Iterate through all slots - simplistic but meets requirement (Task 5 "crude approach")
        for (MyLinkedObject node : targetTable.getTable()) {
            MyLinkedObject curr = node;
            while (curr != null) {
                String key = curr.getWord();
                // Check if key is "prefix + word"
                if (key.startsWith(prefix + " ")) {
                    // Extract the next word (suffix)
                    // Be careful: "prefix word" -> length of prefix + 1
                    if (key.length() > prefix.length() + 1) {
                        String suffix = key.substring(prefix.length() + 1);
                        // Ensure it's just one word (simplification)
                        if (!suffix.contains(" ")) {
                            if (curr.getCount() > maxCount) {
                                maxCount = curr.getCount();
                                bestWord = suffix;
                            }
                        }
                    }
                }
                curr = curr.getNext();
            }
        }
        return bestWord;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new MyLanguageModel().setVisible(true));
    }
}