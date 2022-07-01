package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Server.Lobby;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class ControllerTest {

    @Test
    public void creteGame() throws InputValidationException {
        //Model model = new Model(GameMode.ADVANCED, "ale", "teo");
        Lobby lobby = new Lobby(UUID.randomUUID(), true, 2, "ale");
        Controller controller = Controller.createGame(GameMode.ADVANCED, OptionalValue.of(lobby), "ale", "teo");
        assertTrue(controller != null);
    }

    @Test(expected = GenericInputValidationException.class)
    public void creteGameWithPlayersAmountNotValid() throws InputValidationException {
        Lobby lobby = new Lobby(UUID.randomUUID(), true, 2, "ale");
        Controller controller = Controller.createGame(GameMode.ADVANCED, OptionalValue.of(lobby), "ale");
    }
}
