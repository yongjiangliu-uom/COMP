import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * ActionListener to handling Action of User Interface
 *
 * @author Zhicong Jiang zjiang34@sheffield.ac.uk>
 */
public class ActionListenerHandler implements ActionListener {
    private JTable leftTable;
    private JTextArea documentTextArea;
    private JTextArea infoTextArea;
    private JTextArea hashTableTextArea;
    private JTextArea statisticTextArea;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private HorizontalBarChart barChart;

    private int lineSize;
    private int totalWords;
    private JComboBox<String> comboBox;

    private int numGrams = 1;
    private MyHashTable uniHashTable;
    private MyHashTable biHashTable;
    private MyHashTable triHashTable;
    private MyHashTable fourHashTable;
    private File selectedFile;

    private DefaultTableModel defaultModel;
    LikelySequenceGenerator sequenceGenerator;


    public ActionListenerHandler(JTable leftTable, JTextArea documentTextArea,
                                 JTextArea infoTextArea, JTextArea hashTableTextArea,
                                 JComboBox<String> comboBox, JTextArea inputTextArea,
                                 JTextArea outputTextArea, HorizontalBarChart barChart,
                                 JTextArea statisticTextArea) {
        this.leftTable = leftTable;
        this.documentTextArea = documentTextArea;
        this.infoTextArea = infoTextArea;
        this.hashTableTextArea = hashTableTextArea;
        this.comboBox = comboBox;
        this.inputTextArea = inputTextArea;
        this.outputTextArea = outputTextArea;
        this.barChart = barChart;
        this.statisticTextArea = statisticTextArea;

        TableModel model = leftTable.getModel();
        defaultModel = (DefaultTableModel) model;
    }

    /** Method triggered when an action is performed */
    @Override
    public void actionPerformed(ActionEvent e) {

        /* Check the action command and perform corresponding actions */
        if (e.getActionCommand().equalsIgnoreCase("New File")) {
            selectedFile = filePicker();
            if (selectedFile!=null){
                uniHashTable = generateHashTable(selectedFile, 1, true);
                biHashTable = generateHashTable(selectedFile, 2, false);
                triHashTable = generateHashTable(selectedFile, 3, false);
                fourHashTable = generateHashTable(selectedFile, 4, false);
                sequenceGenerator = new LikelySequenceGenerator(uniHashTable,biHashTable,triHashTable,fourHashTable);

                /* Update GUI, default 1-grams */
                updateOrAddWord(sequenceGenerator.getFrequencyMap(1), defaultModel);
                hashTableTextArea.setText(uniHashTable.toString());
                generateStatistic(uniHashTable);
            }
            comboBox.setSelectedItem("1");
        }

        if (e.getSource() == comboBox) {
            numGrams = Integer.parseInt((String) comboBox.getSelectedItem());
            reset();

            MyHashTable hashtable;
            /* Select hashtable by selected n-grams */
            if (numGrams == 2) {
                hashtable = biHashTable;
            } else if (numGrams == 3) {
                hashtable = triHashTable;
            }else if (numGrams == 4) {
                hashtable = fourHashTable;
            } else {
                hashtable = uniHashTable;
            }

            if (selectedFile!=null){
                /* Update GUI */
                updateOrAddWord(sequenceGenerator.getFrequencyMap(numGrams), defaultModel);
                hashTableTextArea.setText(hashtable.toString());
                generateStatistic(hashtable);
            }
        }

        if (e.getActionCommand().equalsIgnoreCase("Go!")) {
            System.out.println(numGrams);
            outputTextArea.setText("");
            String inputStr = inputTextArea.getText().toLowerCase().trim(); // get input from user

            outputTextArea.append(inputStr);
            outputTextArea.append(" ");

            int wordCount = 20; // Set the number of words to predict
            for (int i = 0; i < wordCount; i++) {
                String predictedWord = sequenceGenerator.predictNextWord(inputStr,numGrams);
                outputTextArea.append(predictedWord);
                if (i == wordCount-1){
                    outputTextArea.append(";");
                }
                if (predictedWord.equals(";")) {
                    break;
                }
                outputTextArea.append(" ");
                /*  Add the predicted word to the input string to make the prediction for the next word */
                inputStr += " " + predictedWord;
            }
        }

    }

    /**
     * Calculate LinkedListLength by hashTable
     * @param hashTable
     * @return List<Integer>: List of length
     */
    private List<Integer> calculateLinkedListLength(MyHashTable hashTable){
        List<Integer> linkedListLength = new ArrayList<>();
        for (int i = 0; i < hashTable.getHashTable().length; i++) {
            int length = 0;
            MyLinkedObject currentNode = hashTable.getHashTable()[i];
            while (currentNode != null) {
                length++;
                currentNode = currentNode.getNext();
            }
            linkedListLength.add(length); // linkedList List of length to prepare for later statistics
        }
        return linkedListLength;
    }


    /**
     * Call File Picker in the system to let user choose a .txt file
     * @return File
     */
    private File filePicker() {
        JFileChooser fileChooser = new JFileChooser();

        /* Create a file filter that allows only txt files to be selected */
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                /* Allows to select directories or files ending in.txt */
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Text Files (*.txt)";
            }

        };

        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            documentTextArea.setText("");
            lineSize = 0;
            reset();
            return selectedFile;
        } else {
            System.out.println("Not Picking Anything");
            return null;
        }
    }

    /**
     * Iterate file and extract word and store in hashTable
     * @param selectedFile User Selected File
     * @param n number of grams needed to store in the hashTable
     * @param isWrite flag to decide if need to write to GUI or not
     * @return MyHashTable
     */
    private MyHashTable generateHashTable(File selectedFile, int n, Boolean isWrite){
        try {
            // Read File
            List<String> lines = Files.readAllLines(Paths.get(selectedFile.getAbsolutePath()));
            int lineSize = Math.min(lines.size(), 3000); // max 3000 lines
            this.lineSize = lineSize;
            MyHashTable hashTable = new MyHashTable(lineSize, HashEnum.SHA1);
            String[] lastWord = null; // store last word in the line

            // Iterate file and insert into hashtable
            for (int i = 0; i < lineSize; i++) {
                if (isWrite){documentTextArea.append(lines.get(i) + "\n");}
                if (n==1){ // 1-grams
                    hashTable.insert(lines.get(i));
                }else{ // n-grams
                    String[] result = hashTable.insertNGrams(lastWord, lines.get(i), n);
                    lastWord = result;
                }
            }
            return hashTable;

        } catch (IOException e) {
            // Handling exceptions that may occur while reading files
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Make Statistic for hashTable
     * @param hashTable
     */
    private void generateStatistic(MyHashTable hashTable){
        statisticTextArea.setText("");
        List<Integer> linkedListLength = calculateLinkedListLength(hashTable); // Length of each linked list in the hash table
        /* calculate Average */
        double sum = 0;
        for (int length : linkedListLength) {
            sum += length;
        }
        double average = sum / linkedListLength.size();

        /* calculate Standard Deviation */
        double variance = 0;
        for (int length : linkedListLength) {
            variance += Math.pow(length - average, 2);
        }
        double standardDeviation = Math.sqrt(variance / linkedListLength.size());



        /* Visual list length distribution */
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int length : linkedListLength) {
            countMap.put(length, countMap.getOrDefault(length, 0) + 1);
        }
        barChart.setData(countMap);

        statisticTextArea.append("\nAverage Length: " + average + "\n");
        statisticTextArea.append("Standard Deviation: " + standardDeviation + "\n");

        infoTextArea.setText("Total lines of document: " + lineSize + "; Total number of words: " +
                totalWords + ";  Total number of different words: " + defaultModel.getRowCount() + ";");
    }

    /**
     * Reset GUI
     */
    private void reset(){
        statisticTextArea.setText("");
        statisticTextArea.append("\nAverage Length: " + 0.0 + "\n");
        statisticTextArea.append("Standard Deviation: " + 0.0 + "\n");
        hashTableTextArea.setText("");

        /* Clear table data */
        TableModel model = leftTable.getModel();
        DefaultTableModel defaultModel = (DefaultTableModel) model;
        defaultModel.setRowCount(0);
        totalWords = 0;
        infoTextArea.setText("Total lines of document: 0; Total number of words: 0; " +
                "Total number of different words: 0;");

    }

    /**
     * Visulise data to Table GUI
     * @param WordFrequency
     * @param defaultModel
     */
    public void updateOrAddWord(Map<String,Integer> WordFrequency,DefaultTableModel defaultModel) {
        defaultModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : WordFrequency.entrySet()) {
            String word = entry.getKey();
            int wordCount = entry.getValue();

            boolean wordExists = false;
            for (int row = 0; row < defaultModel.getRowCount(); row++) {
                String rowData = defaultModel.getValueAt(row, 0).toString();
                if (rowData.equals(word)) {
                    int count = (int) defaultModel.getValueAt(row, 1); // get current count
                    defaultModel.setValueAt(count + 1, row, 1); // update count
                    totalWords++;
                    wordExists = true;
                    break;
                }
            }

            if (!wordExists) {
                defaultModel.addRow(new Object[]{word, wordCount}); // if not exit, add new row
                totalWords = totalWords + wordCount;
            }
        }


    }


}
