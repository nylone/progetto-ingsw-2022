package it.polimi.ingsw.Client.GUI;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Listener responsible for restricting the maximum number of checkboxes that can be selected
 */
public class CheckBoxListener implements ItemListener {
    /**
     * limit of selectable checkboxes
     */
    private final int maxCheckBoxesSelectable;
    /**
     * "listened" checkboxes
     */
    private final JCheckBox[] checkBoxes;
    /**
     * Selected checkboxes
     */
    private int selectionCounter = 0;

    /**
     * Create a new checkBoxListener
     * @param limit maximum number of selectable checkBoxes
     * @param checkBoxes checkboxes that will be listened by this listener
     */
    public CheckBoxListener(int limit, JCheckBox[] checkBoxes) {
        this.maxCheckBoxesSelectable = limit;
        this.checkBoxes = checkBoxes;
    }

    /**
     * As soon as one checkbox has been selected, verify that selected checkBoxes are less than maximum
     * @param e the event to be processed
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox source = (JCheckBox) e.getSource();
        if (source.isSelected()) {
            selectionCounter++;
            // check for max selections:
            if (selectionCounter == maxCheckBoxesSelectable)
                for (JCheckBox box : checkBoxes)
                    if (!box.isSelected())
                        box.setEnabled(false);
        } else {
            selectionCounter--;
            // check for less than max selections:
            if (selectionCounter < maxCheckBoxesSelectable)
                for (JCheckBox box : checkBoxes)
                    box.setEnabled(true);
        }
    }
}
