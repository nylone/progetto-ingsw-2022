package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class GameBoard {

    private IslandField islands = new IslandField();
    private ArrayList<CharacterCard> characterCards;
    private int coinReserve;
    private ArrayList<Cloud> clouds;
    private GameMode gameMode;
    private GamePhase gamePhase;
    private static ArrayList<PawnColour> studentBag = new ArrayList<>();
    private ArrayList<PlayerBoard> playerBoards = new ArrayList<>();
    private Map<PawnColour, PlayerBoard> teachers = new EnumMap<>(PawnColour.class);
    private ArrayList<PlayerBoard> turnOrder = new ArrayList<>();
    private int turnPosition;
    private Map<TowerColour, Integer> freeTowers = new EnumMap<>(TowerColour.class);

    public GameBoard(GameMode gameMode){
        //todo
        this.gameMode = gameMode;
    }

    public static ArrayList<PawnColour> getStudentBag() {
        return studentBag;

    }
}
