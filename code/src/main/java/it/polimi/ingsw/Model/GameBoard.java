package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import it.polimi.ingsw.Exceptions.NoPawnInCloudException;

import javax.swing.text.html.Option;
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
        this.playerBoards = new ArrayList<>();
        this.teachers = new EnumMap<>(PawnColour.class);
        this.turnOrder = new TurnOrder();
        this.playerTeams = new HashMap<>(); // creates team associations based on number of players
        this.coinReserve = 20-nop; //hp: we assume 20 as amount of available coins just like the real game.

        for (int i = 0; i < nop; i++) {
            this.playerBoards.add(new PlayerBoard(i+1, nop, playerNicknames[i], this.studentBag));
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
        clouds = new ArrayList<>(nop);
        //2 players: 2 cloud tiles - 3 players: 3 cloud tiles: 4 players: 4 cloud tiles
        for(int i=1; i<= nop; i++){
            clouds.add(new Cloud(i));

            try {
                clouds.get(i).fill((ArrayList<PawnColour>) studentBag.multiple_extraction(nop == 3 ? 4 : 3));
            } catch (NoPawnInCloudException e) { System.out.println(e.getMessage()); }
        }

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

    public PlayerBoard getPlayerBoardById(int id) {
        return playerBoards.stream()
                .filter(p -> p.getId() == id)
                .findAny()
                .orElseThrow(() -> new InvalidInputException());
    }

    public PlayerBoard getPlayerBoardByNickname(String nickname) {
        return playerBoards.stream()
                .filter(p -> p.getNickname().equals(nickname))
                .findAny()
                .orElseThrow(() -> new InvalidInputException());
    }

    public Optional<Integer> influencerOf(IslandGroup ig) {
        Map<PawnColour, Integer> sc = ig.getStudentCount();
        Map<Integer, Integer> ic = new HashMap<>(); // maps the team with the influence count

        for (Map.Entry<PawnColour, Integer> e : sc.entrySet()) {
            PawnColour colour = e.getKey();
            int count = e.getValue();
            ic.merge(this.playerTeams.get(this.teachers.get(colour)), count, Integer::sum);
        }
        if (ig.getDenyTowerInfluence() == false) {
            ig.getTowerColour()
                    .ifPresent(towerColour -> ic.merge(towerColour.getTeamId(), ig.getTowerCount(), Integer::sum));
        }

        List<Map.Entry<Integer, Integer>> tbi = ic.entrySet().stream() // tbi is team by influence
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toCollection(ArrayList::new));

        ig.setDenyTowerInfluence(false);
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
        if(!mnp.getNoEntry().isPresent()) {
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
        }else{
            NoEntryTile noEntryTile = mnp.getNoEntry().get();
            mnp.setNoEntry(Optional.empty());
            //todo send back the noEntryTile to the card05
            }
        }
    }
