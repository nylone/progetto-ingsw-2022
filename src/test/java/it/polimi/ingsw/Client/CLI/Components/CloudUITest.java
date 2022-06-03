package it.polimi.ingsw.Client.CLI.Components;

import it.polimi.ingsw.Client.CLI.CloudUI;
import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Model;
import org.junit.Test;

public class CloudUITest {

    @Test
    public void shouldDrawClouds() throws EmptyContainerException {
        Model gb = new Model(GameMode.SIMPLE, "ale", "teo");
        gb.getClouds().get(0).extractContents();
        System.out.println(CloudUI.draw(gb));
    }
}