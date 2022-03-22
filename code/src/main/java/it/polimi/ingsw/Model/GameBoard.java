package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class GameBoard {

    private final IslandField islands;
    private final GameMode gameMode;
    private final StudentBag studentBag;
    private final ArrayList<PlayerBoard> playerBoards;
    private final Map<PawnColour, PlayerBoard> teachers;
    private final ArrayList<PlayerBoard> turnOrder;
    private final Map<TowerColour, Integer> freeTowers;
    private CharacterCard[] characterCards;
    private int coinReserve;
    private ArrayList<Cloud> clouds;
    private GamePhase gamePhase;

    public GameBoard(GameMode gameMode, int numOfPlayers) {
        //todo
        this.islands = new IslandField(this);
        this.gameMode = gameMode;
        this.studentBag = new StudentBag(24);
        this.playerBoards = new ArrayList<>();
        this.teachers = new EnumMap<>(PawnColour.class);
        this.turnOrder = new ArrayList<>();
        this.freeTowers = new EnumMap<>(TowerColour.class);
        if (this.gameMode.equals(GameMode.ADVANCED)) {
            this.characterCards = CharacterDeckGenerator.generateCardSet();
            this.coinReserve = 20 - numOfPlayers;
        }
        this.clouds = new ArrayList<>();
        for (int i = 0; i < (numOfPlayers == 3 ? 3 : 2 ); i++) {
            this.clouds.add(new Cloud(i));
        }
        this.gamePhase = GamePhase.SETUP;
    }

    public StudentBag getStudentBag() {
        return studentBag;
    }
}