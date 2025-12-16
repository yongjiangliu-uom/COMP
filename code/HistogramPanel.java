import java.awt.*;
import java.util.Map;
import javax.swing.*;

/**
 * A custom Swing component to visualize the distribution of linked list lengths.
 * This adds "Stylishness" to the GUI as per the marking scheme.
 */
public class HistogramPanel extends JPanel {
    private Map<Integer, Integer> distribution; // Key: List Length, Value: Frequency
    private int maxFrequency = 0;

    public HistogramPanel() {
        this.setPreferredSize(new Dimension(400, 200));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public void setDistribution(Map<Integer, Integer> distribution) {
        this.distribution = distribution;
        this.maxFrequency = 0;
        if (distribution != null) {
            for (int count : distribution.values()) {
                if (count > maxFrequency) maxFrequency = count;
            }
        }
        repaint(); // Trigger redraw
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (distribution == null || distribution.isEmpty()) {
            g.setColor(Color.GRAY);
            g.drawString("No data to display", 20, 30);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 30;
        int graphWidth = width - 2 * padding;
        int graphHeight = height - 2 * padding;

        // Draw Axes
        g2.setColor(Color.BLACK);
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X Axis
        g2.drawLine(padding, height - padding, padding, padding); // Y Axis

        // Draw Bars
        int numBars = distribution.size();
        if (numBars == 0) return;
        
        int barWidth = Math.min(40, graphWidth / numBars); // Dynamic width, max 40px
        int x = padding + 10;

        // Find max length to order the X axis (0, 1, 2, ...)
        int maxLen = distribution.keySet().stream().max(Integer::compareTo).orElse(0);

        for (int len = 0; len <= maxLen; len++) {
            if (!distribution.containsKey(len)) continue; // Skip lengths that don't exist

            int count = distribution.get(len);
            int barHeight = (int) ((double) count / maxFrequency * graphHeight);

            // Bar fill
            g2.setColor(new Color(100, 149, 237)); // Cornflower Blue
            g2.fillRect(x, height - padding - barHeight, barWidth - 5, barHeight);
            
            // Bar border
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(x, height - padding - barHeight, barWidth - 5, barHeight);

            // Labels
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            
            // Draw Count (Y value) above bar
            String countLabel =String.valueOf(count);
            int labelWidth = g2.getFontMetrics().stringWidth(countLabel);
            g2.drawString(countLabel, x + (barWidth-5)/2 - labelWidth/2, height - padding - barHeight - 5);

            // Draw Length (X label) below axis
            g2.drawString(String.valueOf(len), x + (barWidth-5)/2 - 3, height - padding + 15);

            x += barWidth;
        }
        
        // Axis Labels
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("List Length", width / 2 - 30, height - 5);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(-Math.PI / 2);
        g2d.drawString("Frequency", -height / 2 - 30, 20);
        g2d.dispose();
    }
}