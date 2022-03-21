package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class GameBoard {

    private final IslandField islands = new IslandField(this);
    private ArrayList<CharacterCard> characterCards;
    private int coinReserve;
    private ArrayList<Cloud> clouds;
    private final GameMode gameMode;
    private GamePhase gamePhase;
    private final ArrayList<PawnColour> studentBag = new ArrayList<>();
    private final ArrayList<PlayerBoard> playerBoards = new ArrayList<>();
    private final Map<PawnColour, PlayerBoard> teachers = new EnumMap<>(PawnColour.class);
    private final ArrayList<PlayerBoard> turnOrder = new ArrayList<>();
    private int turnPosition;
    private final Map<TowerColour, Integer> freeTowers = new EnumMap<>(TowerColour.class);

    public GameBoard(GameMode gameMode) {
        //todo
        this.gameMode = gameMode;
    }

    public ArrayList<PawnColour> getStudentBag() {
        return studentBag;

    }
}
