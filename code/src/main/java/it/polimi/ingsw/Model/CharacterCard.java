package it.polimi.ingsw.Model;

import java.io.Serializable;

abstract class CharacterCard implements Serializable {
    private int id;
    //GameBoard context;
    private Character character;
    private int cost;
    private int timeUsed;

}
