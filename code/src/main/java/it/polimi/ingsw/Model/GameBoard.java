package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class GameBoard implements Serializable {

    private final IslandField islandField;
    private final GameMode gameMode;
    private final StudentBag studentBag;
    private final List<PlayerBoard> playerBoards;
    private final Map<PawnColour, PlayerBoard> teachers;
    private final Map<PlayerBoard, Integer> playerTeams;
    private final Map<Integer, TowerStorage> towerStorageTeams;
    private final TurnOrder turnOrder;
    private List<CharacterCard> characterCards;
    private int coinReserve;
    private List<Cloud> clouds;
    private final GamePhase gamePhase;

    // todo
    public GameBoard(GameMode gameMode, String... playerNicknames) {
        final int nop = playerNicknames.length;
        this.islandField = new IslandField();
        this.gameMode = gameMode;
        this.studentBag = new StudentBag(24);
        this.playerBoards = new ArrayList<>(); //todo create arraylists
        this.teachers = new EnumMap<>(PawnColour.class);
        this.turnOrder = new TurnOrder();
        this.playerTeams = new HashMap<>(); // creates team associations based on number of players
        for (int i = 0; i < nop; i++) {
            this.playerTeams.put(this.playerBoards.get(i), i % (nop == 4 ? 2 : nop));
        } // note: for 4 players the first team is always made up by the first 2 nicknames
        this.towerStorageTeams = new HashMap<>(); // creates tower storage associations based on number of players
        for (int i = 0; i < (nop == 4 ? 2 : nop); i++) {
            this.towerStorageTeams.put(i, new TowerStorage(TowerColour.fromTeamId(i), nop == 3 ? 6 : 8));
        } // note: for 4 players the first team is always made up by the first 2 nicknames
        if (this.gameMode.equals(GameMode.ADVANCED)) {
            this.characterCards = CharacterDeckGenerator.generateCardSet();
            this.coinReserve = 20 - nop;
        }
        // todo add clouds
        // todo create playerBoards
        this.gamePhase = GamePhase.SETUP;
    }

    public StudentBag getStudentBag() {
        return studentBag;
    }

    public IslandField getIslandField() {
        return islandField;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public List<PlayerBoard> getPlayerBoards() {
        return playerBoards;
    }

    public Map<PawnColour, PlayerBoard> getTeachers() {
        return teachers;
    }

    public List<PawnColour> getOwnTeachers(PlayerBoard p) {
        return this.teachers.entrySet().stream()
                .filter(e -> e.getValue().equals(p))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public TurnOrder getTurnOrder() {
        return turnOrder;
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    public Optional<Integer> influencerOf(IslandGroup ig) {
        Map<PawnColour, Integer> sc = ig.getStudentCount();
        Map<Integer, Integer> ic = new HashMap<>(); // maps the team with the influence count
        for (Map.Entry<PawnColour, Integer> e : sc.entrySet()) {
            PawnColour colour = e.getKey();
            int count = e.getValue();
            ic.merge(this.playerTeams.get(this.teachers.get(colour)), count, Integer::sum);
        }
        ig.getTowerColour()
                .ifPresent(towerColour -> ic.merge(towerColour.getTeamId(), ig.getTowerCount(), Integer::sum));
        List<Map.Entry<Integer, Integer>> tbi = ic.entrySet().stream() // tbi is team by infulence
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toCollection(ArrayList::new));

        switch (tbi.size()) {
            case 0:
                return Optional.empty();
            case 1:
                return Optional.of(tbi.get(0).getKey());
            default: {
                if (tbi.get(0).getValue() > tbi.get(1).getValue())
                    return Optional.of(tbi.get(0).getKey());
                else return Optional.empty();
            }
        }
    }

    public void moveMotherNature(int steps) {
        this.islandField.moveMotherNature(steps);
        IslandGroup mnp = this.islandField.getMotherNaturePosition();
        Optional<Integer> optInfluencer = influencerOf(mnp);
        if (optInfluencer.isPresent()) {
            int newInfluencer = optInfluencer.get();
            if (
                    !mnp.getTowerColour().isPresent() ||
                            mnp.getTowerColour().get() != TowerColour.fromTeamId(newInfluencer)
            ) {
                mnp.swapTower(this.towerStorageTeams.get(newInfluencer));
            }
        }
        this.islandField.joinGroups();
    }
}
