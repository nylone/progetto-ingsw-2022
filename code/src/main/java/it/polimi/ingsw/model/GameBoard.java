package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Map;

public class GameBoard {
    private IslandField islands;
    private ArrayList<CharacterCard> characterCards;
    private int coinReserve;
    private ArrayList<Cloud> clouds;
    //private GameMode gameMode;
    //private GamePhase gamePhase;
    private ArrayList<PawnColour> studentBag;
    private ArrayList<PlayerBoard> playerBoards;
    private Map<PawnColour, PlayerBoard> teachers;
    private ArrayList<PlayerBoard> turnOrder;
    private int turnPosition;
    private Map<TowerColour, Integer> freeTowers;
}
