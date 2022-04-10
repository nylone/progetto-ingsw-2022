package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.InvalidInputException;
import it.polimi.ingsw.Exceptions.NoPawnInCloudException;
import it.polimi.ingsw.Model.Enums.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class GameBoard implements Serializable {

    private final IslandField islandField;
    private final GameMode gameMode;
    private final StudentBag studentBag;
    private final List<PlayerBoard> playerBoards;
    private final Map<PawnColour, PlayerBoard> teachers;
    private final TeamMapper teamMap;
    private final TurnOrder turnOrder;
    private List<CharacterCard> characterCards;
    private int coinReserve;
    @Serial
    private static final long serialVersionUID = 101L; // convention: 1 for model, (01 -> 99) for objects
    private final GamePhase gamePhase;
    private boolean increasedInfluenceFlag;
    private Optional<PawnColour> denyPawnColourInfluence = Optional.empty();
    private final List<Cloud> clouds;


    public GameBoard(GameMode gameMode, String... playerNicknames) {
        final int nop = playerNicknames.length;
        this.islandField = new IslandField();
        this.gameMode = gameMode;
        this.studentBag = new StudentBag(24);
        this.playerBoards = new ArrayList<>();
        this.teachers = new EnumMap<>(PawnColour.class);
        this.turnOrder = new TurnOrder();
        this.coinReserve = 20-nop; // hp: we assume 20 as amount of available coins just like the real game.
        this.increasedInfluenceFlag = false;

        for (int i = 0; i < nop; i++) {
            this.playerBoards.add(new PlayerBoard(i+1, nop, playerNicknames[i], this.studentBag));
        } // add generate player based on nickname and store it
        this.teamMap = new TeamMapper(this.playerBoards);
        if (this.gameMode.equals(GameMode.ADVANCED)) {
            this.characterCards = CharacterDeckGenerator.generateCardSet(this);
            this.coinReserve = 20 - nop;
        }
        clouds = new ArrayList<>(nop);
        //2 players: 2 cloud tiles - 3 players: 3 cloud tiles: 4 players: 4 cloud tiles
        for(int i = 0; i <= nop; i++){
            clouds.add(new Cloud(i));

            try {
                clouds.get(i).fill((ArrayList<PawnColour>) studentBag.multiple_extraction(nop == 3 ? 4 : 3));
            } catch (NoPawnInCloudException e) { System.out.println(e.getMessage()); }
        }

        this.gamePhase = GamePhase.SETUP;
    }

    public List<Cloud> getClouds() {
        return this.clouds;
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
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

    public boolean getIncreasedInfluenceFlag() {
        return increasedInfluenceFlag;
    }

    public StudentBag getStudentBag() {
        return studentBag;
    }

    public TurnOrder getTurnOrder() {
        return turnOrder;
    }

    public Optional<PawnColour> getDenyPawnColourInfluence() {
        return denyPawnColourInfluence;
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


    public void setTeacher(PawnColour teacher, PlayerBoard player) {
        teachers.put(teacher, player);
    }

    public void setIncreasedInfluenceFlag(boolean increasedInfluenceFlag) {
        this.increasedInfluenceFlag = increasedInfluenceFlag;
    }

    public void setDenyPawnColourInfluence(Optional<PawnColour> denyPawnColourInfluence) {
        this.denyPawnColourInfluence = denyPawnColourInfluence;
    }

    // returns the team that holds influence over a particular islandgroup
    public Optional<TeamID> influencerOf(IslandGroup ig) {
        // todo aumentare di 2 il conteggio del'influenza quando IncreasedInfluenceFlag è true (effetto carta 8)
        Map<PawnColour, Integer> sc = ig.getStudentCount();
        Map<TeamID, Integer> ic = new HashMap<>(); // maps the team with the influence count

        for (Map.Entry<PawnColour, Integer> e : sc.entrySet()) {
            PawnColour colour = e.getKey();
            if (teachers.get(colour) == null) { continue; } //
            int count = e.getValue();
            if (denyPawnColourInfluence.isPresent()) {
                if (colour == denyPawnColourInfluence.get()) {
                    continue;
                }
            }
            ic.merge(this.teamMap.getTeamID(this.teachers.get(colour)), count, Integer::sum);
        }

        if (!ig.getDenyTowerInfluence()) {
            ig.getTowerColour()
                    .ifPresent(tc -> ic.merge(tc.getTeamID(), ig.getTowerCount(), Integer::sum));
        }

        List<Map.Entry<TeamID, Integer>> tbi = ic.entrySet().stream() // tbi is team by influence
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(tbi);

        ig.setDenyTowerInfluence(false);
        switch (tbi.size()) {
            case 0:
                return Optional.empty();
            case 1:
                return Optional.of(tbi.get(0).getKey());
            default: {
                if (tbi.get(0).getValue() > tbi.get(1).getValue())
                    return Optional.of(tbi.get(0).getKey());
                else return ig.getTowerColour().flatMap(tc -> Optional.of(tc.getTeamID()));
            }
        }
    }

    public void moveMotherNature(int steps) {
        this.islandField.moveMotherNature(steps);
        actMotherNaturePower(this.islandField.getMotherNaturePosition());
    }

    public void actMotherNaturePower(IslandGroup mnp) {
        if(mnp.getNoEntry().isEmpty()) {
            Optional<TeamID> optInfluencer = influencerOf(mnp);
            if (optInfluencer.isPresent()) {
                TeamID newInfluencer = optInfluencer.get();
                if (
                        mnp.getTowerColour().isEmpty() ||
                                mnp.getTowerColour().get() != TowerColour.fromTeamId(newInfluencer)
                ) {
                    mnp.swapTower(this.teamMap.getTowerStorage(newInfluencer));
                }
            }
            this.islandField.joinGroups();
        }else{
            NoEntryTile noEntryTile = mnp.getNoEntry().get();
            noEntryTile.goHome(mnp);
        }
    }

    public TeamMapper getTeamMap() {
        return teamMap;
    }
}
