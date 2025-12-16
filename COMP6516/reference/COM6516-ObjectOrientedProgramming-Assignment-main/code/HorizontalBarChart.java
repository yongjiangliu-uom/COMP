import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorizontalBarChart extends JPanel {

    private int[] values;
    private String[] labels;
    private int barHeight = 20;
    private int minBarWidth = 5;
    private int padding = 2;

    public HorizontalBarChart(int[] values, String[] labels) {
        this.values = values;
        this.labels = labels;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();
        barHeight = Math.max(20, (height - padding * 2) / values.length);

        int maxValue = 0;
        for (int value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        FontMetrics fm = g2d.getFontMetrics();

        int maxLabelWidth = 0;
        for (String label : labels) {
            int labelWidth = fm.stringWidth(label);
            maxLabelWidth = Math.max(maxLabelWidth, labelWidth);
        }

        int xStart = maxLabelWidth + padding * 4;

        for (int i = 0; i < values.length; i++) {
            int y = i * barHeight + padding;
            int labelWidth = fm.stringWidth(labels[i]);
            int labelX = padding + maxLabelWidth - labelWidth;

            g2d.drawString(labels[i], labelX, y + barHeight / 2);

            int barWidth = (int) ((double) values[i] / maxValue * (width - 50 - xStart - padding - 10));
            barWidth = Math.max(barWidth, minBarWidth);

            int x = xStart;
            int barEndX = x + barWidth;
            if (barEndX > getWidth() - padding * 2) {
                barWidth = getWidth() - padding * 2 - x;
            }

            g2d.fillRect(x, y, barWidth, barHeight - 5);

            g2d.setColor(Color.BLACK);
            String labelValue = String.valueOf(values[i]);
            int labelValueWidth = fm.stringWidth(labelValue);
            g2d.drawString(labelValue, x + barWidth + padding, y + barHeight / 2 + 5);
        }

        g2d.dispose();
    }


    public void setData(Map<Integer, Integer> countMap) {
        /* Prepare new data and labels as lists */
        List<Integer> newDataList = new ArrayList<>();
        List<String> newLabelsList = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            int length = entry.getKey();
            int count = entry.getValue();

            newLabelsList.add(String.valueOf(length)); // Convert length to string and add it to the labels list
            newDataList.add(count); // Add the count to the data list
        }

        /* Convert lists to arrays */
        this.values = newDataList.stream().mapToInt(Integer::intValue).toArray();
        this.labels = newLabelsList.toArray(new String[0]);

        /* Calculate the new bar chart height and width */
        int newBarHeight = Math.max(20, (getHeight() - padding * 2) / newDataList.size());
        int newMaxValue = newDataList.stream().max(Integer::compare).orElse(0);

        barHeight = newBarHeight;

        /* update values and labels */
        this.values = newDataList.stream().mapToInt(Integer::intValue).toArray();
        this.labels = newLabelsList.toArray(new String[0]);

        /* Set the new preferred size to accommodate the larger size */
        int newWidth = (int) ((double) newMaxValue / newMaxValue * (getWidth() - 60)) + 2 * padding * 5;
        int newHeight = newDataList.size() * barHeight + 2 * padding;

        setPreferredSize(new Dimension(newWidth, newHeight));
        revalidate();
        repaint();
    }

}
