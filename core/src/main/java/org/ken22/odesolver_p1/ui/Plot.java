import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * The Plot class is a JPanel that generates a plot from given time points and state vectors.
 * It displays the plot with linear scales on both axes, labels, and grid lines.
 */
public class Plot extends JPanel {

    private final ArrayList<Double> time;
    private final ArrayList<ArrayList<Double>> stateVectors;

    /**
     * Constructs a Plot with the specified time points and state vectors.
     *
     * @param time         An ArrayList of Double values representing the time points.
     * @param stateVectors An ArrayList of ArrayLists of Double values representing the state vectors.
     */
    public Plot(ArrayList<Double> time, ArrayList<ArrayList<Double>> stateVectors) {
        this.time = time;
        this.stateVectors = stateVectors;
    }

    /**
     * Paints the plot on the panel.
     *
     * @param g The Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // Draw axes
        g2.drawLine(50, height - 50, width - 50, height - 50); // x-axis
        g2.drawLine(50, height - 50, 50, 50); // y-axis

        // Draw labels
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("Time (t)", width / 2 - 30, height - 20);
        AffineTransform orig = g2.getTransform();
        g2.rotate(-Math.PI / 2);
        g2.drawString("Solution (y)", -height / 2 - 30, 20);
        g2.setTransform(orig);

        // Draw title
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Plot", width / 2 - 30, 30);

        // Get the plot boundaries
        double minX = getMin(time);
        double maxX = getMax(time);
        double minY = getMinY();
        double maxY = getMaxY();

        // Draw tick marks and labels
        drawTicks(g2, minX, maxX, height - 50, 50, width - 50, true);  // x-axis
        drawTicks(g2, minY, maxY, 50, height - 50, 50, false); // y-axis

        // Plot each set of points with red line
        g2.setColor(Color.RED);
        for (ArrayList<Double> stateVector : stateVectors) {
            for (int i = 0; i < time.size() - 1; i++) {
                double x1 = time.get(i);
                double y1 = stateVector.get(i);
                double x2 = time.get(i + 1);
                double y2 = stateVector.get(i + 1);

                int x1Screen = (int) (50 + ((x1 - minX) / (maxX - minX)) * (width - 100));
                int y1Screen = (int) (height - 50 - ((y1 - minY) / (maxY - minY)) * (height - 100));
                int x2Screen = (int) (50 + ((x2 - minX) / (maxX - minX)) * (width - 100));
                int y2Screen = (int) (height - 50 - ((y2 - minY) / (maxY - minY)) * (height - 100));

                g2.draw(new Line2D.Double(x1Screen, y1Screen, x2Screen, y2Screen));
            }
        }

        // Set dashed stroke for grid lines
        float[] dashPattern = { 5, 5 };
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);
        g2.setStroke(dashed);
        g2.setColor(Color.LIGHT_GRAY);

        // Draw vertical dashed lines
        for (int i = 0; i < time.size(); i++) {
            double x = time.get(i);
            int xScreen = (int) (50 + ((x - minX) / (maxX - minX)) * (width - 100));
            g2.draw(new Line2D.Double(xScreen, height - 50, xScreen, 50));
        }

        // Draw horizontal dashed lines
        for (double y = minY; y <= maxY; y += (maxY - minY) / 10) {
            int yScreen = (int) (height - 50 - ((y - minY) / (maxY - minY)) * (height - 100));
            g2.draw(new Line2D.Double(50, yScreen, width - 50, yScreen));
        }
    }

    /**
     * Draws tick marks and labels on the axes.
     *
     * @param g2       The Graphics2D object used for drawing.
     * @param minVal   The minimum value.
     * @param maxVal   The maximum value.
     * @param axisPos  The position of the axis.
     * @param start    The starting position for the tick marks.
     * @param end      The ending position for the tick marks.
     * @param isXAxis  A boolean indicating whether to draw ticks on the x-axis (true) or y-axis (false).
     */
    private void drawTicks(Graphics2D g2, double minVal, double maxVal, int axisPos, int start, int end, boolean isXAxis) {
        int tickSize = 5; // Increased tick size for better visibility
        Font originalFont = g2.getFont();
        Font smallFont = new Font("Arial", Font.PLAIN, 10); // Smaller font size for tick labels
        g2.setFont(smallFont);

        int numTicks = 10;
        for (int i = 0; i <= numTicks; i++) {
            double value = minVal + (i * (maxVal - minVal) / numTicks);
            int pos = (int) (start + ((value - minVal) / (maxVal - minVal)) * (end - start));
            String label = String.format("%.2f", value);
            if (isXAxis) {
                g2.drawLine(pos, axisPos, pos, axisPos - tickSize);
                g2.drawString(label, pos - 10, axisPos + 15); // Adjusted position for x-axis labels
            } else {
                g2.drawLine(axisPos, pos, axisPos + tickSize, pos);
                AffineTransform orig = g2.getTransform();
                g2.rotate(-Math.PI / 2);
                g2.drawString(label, -pos - 15, axisPos - 10); // Adjusted position for y-axis labels
                g2.setTransform(orig);
            }
        }

        g2.setFont(originalFont); // Restore original font
    }

    /**
     * Gets the minimum value from a list of doubles.
     *
     * @param list The list of doubles.
     * @return The minimum value.
     */
    private double getMin(ArrayList<Double> list) {
        double minVal = Double.MAX_VALUE;
        for (double val : list) {
            minVal = Math.min(minVal, val);
        }
        return minVal;
    }

    /**
     * Gets the minimum y-value from the state vectors.
     *
     * @return The minimum y-value.
     */
    private double getMinY() {
        double minY = Double.MAX_VALUE;
        for (ArrayList<Double> stateVector : stateVectors) {
            for (double y : stateVector) {
                minY = Math.min(minY, y);
            }
        }
        return minY;
    }

    /**
     * Gets the maximum value from a list of doubles.
     *
     * @param list The list of doubles.
     * @return The maximum value.
     */
    private double getMax(ArrayList<Double> list) {
        double maxVal = Double.MIN_VALUE;
        for (double val : list) {
            maxVal = Math.max(maxVal, val);
        }
        return maxVal;
    }

    /**
     * Gets the maximum y-value from the state vectors.
     *
     * @return The maximum y-value.
     */
    private double getMaxY() {
        double maxY = Double.MIN_VALUE;
        for (ArrayList<Double> stateVector : stateVectors) {
            for (double y : stateVector) {
                maxY = Math.max(maxY, y);
            }
        }
        return maxY;
    }
}
