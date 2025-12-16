package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DistributionBarPanel extends JPanel {

    private Map<Integer, Integer> distributionData;

    public DistributionBarPanel(Map<Integer, Integer> distributionData) {
        this.distributionData = distributionData;
        int maxKey = 0;
        for (int key : distributionData.keySet()) {
            if (key > maxKey) {
                maxKey = key;
            }
        }
        System.out.println("maxKey = " + maxKey);
        int panelWidth = (maxKey + 1) * 40 + 20;
        setPreferredSize(new Dimension(panelWidth, 300));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int barWidth = 20;
        int xOffset = 20;
        int yOffset = getHeight() - 50;

        // find the max frequency
        int maxFrequency = 0;
        for (Map.Entry<Integer, Integer> entry : distributionData.entrySet()) {
            int frequency = entry.getValue();
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
            }
        }

        for (Map.Entry<Integer, Integer> entry : distributionData.entrySet()) {
            int index = entry.getKey();
            int frequency = entry.getValue();

            int barHeight = frequency * (getHeight()) / maxFrequency;
            g.setColor(Color.blue);
            g.fillRect(xOffset, yOffset - barHeight, barWidth, barHeight);

            g.setColor(Color.RED);
            g.drawString("i=" + index, xOffset, getHeight() - 20);

            xOffset += barWidth + 20;
        }
    }
}
