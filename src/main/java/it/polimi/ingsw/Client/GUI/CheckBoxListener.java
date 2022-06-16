package it.polimi.ingsw.Client.GUI;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CheckBoxListener implements ItemListener{
        private final int maxCheckBoxesSelectable;

        private int selectionCounter = 0;

        private final JCheckBox[] checkBoxes;
        public CheckBoxListener(int limit, JCheckBox[] checkBoxes){
            this.maxCheckBoxesSelectable = limit;
            this.checkBoxes = checkBoxes;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox source = (JCheckBox) e.getSource();

            if (source.isSelected()) {
                selectionCounter++;
                // check for max selections:
                if (selectionCounter == maxCheckBoxesSelectable)
                    for (JCheckBox box: checkBoxes)
                        if (!box.isSelected())
                            box.setEnabled(false);
            }
            else {
                selectionCounter--;
                // check for less than max selections:
                if (selectionCounter < maxCheckBoxesSelectable)
                    for (JCheckBox box: checkBoxes)
                        box.setEnabled(true);
            }
        }
    }
