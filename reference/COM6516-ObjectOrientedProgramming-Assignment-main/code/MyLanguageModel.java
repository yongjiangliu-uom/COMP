import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Comparator;


public class MyLanguageModel extends JFrame {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);

    public static void main(String[] args) {
        MyLanguageModel myLanguageModel = new MyLanguageModel("MyLanguageModel");
    }


    public MyLanguageModel(String textForTitleBar){
        super(textForTitleBar);

        // Panel
        JPanel functionP = new JPanel(new BorderLayout());
        functionP.setPreferredSize(new Dimension(WIDTH, HEIGHT / 6));
        functionP.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        JPanel infoP = new JPanel();
        infoP.setPreferredSize(new Dimension(WIDTH, functionP.getPreferredSize().height / 6));
        JPanel buttonP = new JPanel();
        buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.Y_AXIS));

        JPanel statisticPanel = new JPanel(new BorderLayout());

        JSplitPane contentP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane leftPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane operateP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane optionP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Statistic
        int[] charData = {0}; // Replace this with your data
        String[] charLabels = {"Length"}; // Replace this with corresponding labels
        HorizontalBarChart barChart = new HorizontalBarChart(charData, charLabels);


        JScrollPane tablePanel = new JScrollPane();
        JScrollPane chartPanel = new JScrollPane();
        JScrollPane documentPanel = new JScrollPane();
        JScrollPane hashTablePanel = new JScrollPane();
        JScrollPane inputP = new JScrollPane();
        JScrollPane outputP = new JScrollPane();

        // add panel to splitPanel
        leftPanel.setLeftComponent(tablePanel);
        leftPanel.setRightComponent(statisticPanel);
        rightPanel.setLeftComponent(documentPanel);
        rightPanel.setRightComponent(hashTablePanel);
        contentP.setLeftComponent(leftPanel);
        contentP.setRightComponent(rightPanel);
        operateP.setLeftComponent(optionP);
        operateP.setRightComponent(outputP);
        optionP.setLeftComponent(buttonP);
        optionP.setRightComponent(inputP);

        this.add(contentP, BorderLayout.CENTER);
        this.add(functionP, BorderLayout.SOUTH);

        functionP.add(infoP, BorderLayout.NORTH);
        functionP.add(operateP, BorderLayout.CENTER);

        // Table
        Object[][] data = {};
        Object[] columns = {"Word", "Counts"};

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置所有单元格不可编辑
            }
        };

        JTable leftTable = new JTable(tableModel);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        leftTable.setRowSorter(sorter);

        // set sorter rule
        sorter.setSortable(1, true); // 允许指定列进行排序
        sorter.setComparator(1, Comparator.naturalOrder()); // 使用自然顺序比较器按照字母顺序排序
        sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.DESCENDING))); // 按升序排序

        sorter.setSortable(0, true); // 允许指定列进行排序
        sorter.setComparator(0, Comparator.naturalOrder()); // 使用自然顺序比较器按照字母顺序排序
        sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING))); // 按升序排序

        tablePanel.setViewportView(leftTable);

        // set font
        JTableHeader tableHeader = leftTable.getTableHeader();
        Font headerFont = tableHeader.getFont();
        Font newHeaderFont = headerFont.deriveFont(Font.BOLD, 16);
        tableHeader.setFont(newHeaderFont);
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        // set alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        leftTable.setDefaultRenderer(Object.class, centerRenderer);



        // Document Panel
        JTextArea documentTextArea = new JTextArea();
        documentTextArea.setEditable(false);
        documentTextArea.setLineWrap(true); // Enable wrap
        documentTextArea.setWrapStyleWord(true); // Line breaks occur at word boundaries
        documentPanel.setViewportView(documentTextArea);

        // HashTable Panel
        JTextArea hashTableTextArea = new JTextArea();
        hashTableTextArea.setEditable(false);
        hashTableTextArea.setLineWrap(true);
        hashTableTextArea.setWrapStyleWord(true);
        hashTablePanel.setViewportView(hashTableTextArea);

        // Info Panel
        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setText("Total lines of document: 0; Total number of words: 0;  Total number of different words: 0");
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setPreferredSize(infoP.getPreferredSize());
        infoP.add(infoTextArea);

        // Statistic Panel
        JTextArea statisticTextArea = new JTextArea();
        statisticTextArea.append("\nAverage Length: " + 0.0 + "\n");
        statisticTextArea.append("Standard Deviation: " + 0.0 + "\n");
        statisticTextArea.setEditable(false);
        statisticTextArea.setLineWrap(true);
        statisticTextArea.setWrapStyleWord(true);
        statisticPanel.add(statisticTextArea,BorderLayout.SOUTH);
        statisticPanel.add(chartPanel,BorderLayout.CENTER);
        chartPanel.setViewportView(barChart);

        // Input Panel
        JTextArea inputTextArea = new JTextArea();
        inputP.setViewportView(inputTextArea);

        // Output Panel
        JTextArea outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputP.setViewportView(outputTextArea);

        JLabel ngrams = new JLabel("n-grams: ");

        String[] options = {"1", "2", "3", "4"};
        JComboBox<String> comboBox = new JComboBox<>(options);

        ActionListenerHandler actionListenerHandler = new ActionListenerHandler(leftTable, documentTextArea,
                infoTextArea, hashTableTextArea, comboBox, inputTextArea, outputTextArea, barChart,statisticTextArea);

        JButton newFileBtn = new JButton("New File");
        newFileBtn.addActionListener(actionListenerHandler);


        JButton goBtn = new JButton("Go!");
        goBtn.addActionListener(actionListenerHandler);
        comboBox.addActionListener(actionListenerHandler);

        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboBox.getPreferredSize().height));
        goBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, goBtn.getPreferredSize().height));

        buttonP.add(newFileBtn);
        buttonP.add(ngrams);
        buttonP.add(comboBox);
        buttonP.add(goBtn);



        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        this.getContentPane().setPreferredSize(dimension);
        this.getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10)); // 为根面板添加填充
        this.pack();
        this.setVisible(true);
        this.setMinimumSize(dimension);

        leftPanel.setDividerLocation(0.5);
        leftPanel.setEnabled(false);

        rightPanel.setDividerLocation(0.5);
        rightPanel.setEnabled(false);

        contentP.setDividerLocation(0.6);
        contentP.setEnabled(false);

        operateP.setDividerLocation(0.5);
        operateP.setEnabled(false);

        optionP.setDividerLocation(0.8);
        optionP.setEnabled(false);

    }
}
