package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class GameBoard implements Serializable {

    private final IslandField islands;
    private final GameMode gameMode;
    private final StudentBag studentBag;
    private final List<PlayerBoard> playerBoards;
    private final Map<PawnColour, PlayerBoard> teachers;
    private final TurnOrder turnOrder;
    private List<CharacterCard> characterCards;
    private int coinReserve;
    private List<Cloud> clouds;
    private GamePhase gamePhase;

    public GameBoard(GameMode gameMode, String... playerNicknames) {
        final int numOfPlayers = playerNicknames.length;
        this.islands = new IslandField(this);
        this.gameMode = gameMode;
        this.studentBag = new StudentBag(24);
        this.playerBoards = new ArrayList<>(); //todo create arraylists
        this.teachers = new EnumMap<>(PawnColour.class);
        this.turnOrder = new TurnOrder();
        if (this.gameMode.equals(GameMode.ADVANCED)) {
            this.characterCards = CharacterDeckGenerator.generateCardSet();
            this.coinReserve = 20 - numOfPlayers;
        }
        // todo add clouds
        // todo create playerBoards
        this.gamePhase = GamePhase.SETUP;
    }

    public StudentBag getStudentBag() {
        return studentBag;
    }
}
