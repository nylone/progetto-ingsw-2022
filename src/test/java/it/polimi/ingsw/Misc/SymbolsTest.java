package it.polimi.ingsw.Misc;

import it.polimi.ingsw.Model.Enums.PawnColour;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SymbolsTest {
    // creates a list with all the ANSI colour codes
    List<String> colourCodes = List.of(Symbols.GREEN, Symbols.RED, Symbols.YELLOW, Symbols.PINK, Symbols.BLUE);
    // creates a list with all the ANSI colour codes for the background
    List<String> backgroundColourCodes = List.of(Symbols.BACKGROUND_GREEN, Symbols.BACKGROUND_RED,
            Symbols.BACKGROUND_YELLOW, Symbols.BACKGROUND_PINK, Symbols.BACKGROUND_BLUE);

    @Test
    public void testFormatOfStudentUIComponent() {
        // iterates over both the PawnColour enumeration and colourCodes list
        for (int i = 0; i < PawnColour.values().length; i++) {
            // act
            // generates the student UI component
            String actual = Symbols.colorizeStudent(PawnColour.values()[i], "message");

            // assert
            // checks that the component has the correct colour and message
            assertTrue(actual.contains(colourCodes.get(i)));
            assertTrue(actual.contains("message"));
        }
    }

    @Test
    public void testFormatOfStudentBackgroundUIComponent() {
        // iterates over both the PawnColour enumeration and background colourCodes list
        for (int i = 0; i < PawnColour.values().length; i++) {
            // act
            // generates the student UI component with background
            String actual = Symbols.colorizeBackgroundStudent(PawnColour.values()[i], "message");

            // assert
            // checks that the component has the correct colour and message
            assertTrue(actual.contains(backgroundColourCodes.get(i)));
            assertTrue(actual.contains("message"));
        }
    }

    @Test
    public void removeStylingFromUIComponent() {
        // arrange
        String initial = "Simple String";
        // added some ANSI codes to initial String
        String formatted = Symbols.BLUE + initial + Symbols.RESET;

        // act
        // removes previously added ANSI codes
        String unformatted = Symbols.stripFromANSICodes(formatted);

        // assert
        // checks that the unformatted String doesn't have the ANSI codes anymore
        assertEquals(initial, unformatted);
        assertEquals(initial.length(), unformatted.length());
    }
}
