package it.polimi.ingsw.Client.GUI;

import java.awt.*;

public class CircleLayout implements LayoutManager {

    /**
     * For compatibility with LayoutManager interface
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * For compatibility with LayoutManager interface
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Returns this CircleLayout's preferred size based on its Container
     *
     * @param target This CircleLayout's target container
     * @return The preferred size
     */

    public Dimension preferredLayoutSize(Container target) {
        return target.getSize();
    }

    /**
     * Returns this CircleLayout's minimum size based on its Container
     *
     * @param target This CircleLayout's target container
     * @return The minimum size
     */
    public Dimension minimumLayoutSize(Container target) {
        return target.getSize();
    }

    /**
     * Arranges the parent's Component objects in a Circle.
     */
    public void layoutContainer(Container parent) {
        int x, y, w, h, s, c;
        int n = parent.getComponentCount();
        double parentWidth = parent.getSize().width;
        double parentHeight = parent.getSize().height;
        Insets insets = parent.getInsets();
        int centerX = ((int) (parentWidth - (insets.left + insets.right)) / 2) - 100;
        int centerY = ((int) (parentHeight - (insets.top + insets.bottom)) / 2) - 75;

        Component comp;
        Dimension compPS;
        if (n == 1) {
            comp = parent.getComponent(0);
            x = centerX;
            y = centerY;
            compPS = comp.getPreferredSize();
            w = compPS.width;
            h = compPS.height;
            comp.setBounds(x, y, w, h);
        } else {
            double r = (Math.min(parentWidth - (insets.left + insets.right), parentHeight
                    - (insets.top + insets.bottom))) / 2;
            r *= 0.75; // Multiply by .75 to account for extreme right and bottom
            // Components
            for (int i = 0; i < n; i++) {
                comp = parent.getComponent(i);
                compPS = comp.getPreferredSize();
                c = (int) (r * Math.cos(2 * i * Math.PI / n));
                s = (int) (r * Math.sin(2 * i * Math.PI / n));
                x = c + centerX;
                y = s + centerY;

                w = compPS.width;
                h = compPS.height;

                comp.setBounds(x, y, w, h);
            }
        }

    }

    /**
     * Returns a String representation of this CircleLayout.
     *
     * @return A String that represents this CircleLayout
     */
    public String toString() {
        return this.getClass().getName();
    }

}
