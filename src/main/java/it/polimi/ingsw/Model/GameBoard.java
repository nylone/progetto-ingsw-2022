package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TeamID;
import it.polimi.ingsw.Model.Enums.TowerColour;
import it.polimi.ingsw.RemoteView.Lobby;
import it.polimi.ingsw.RemoteView.Messages.Events.Internal.ModelUpdateEvent;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_PLAYERBOARDS;

public class GameBoard implements Serializable {

    @Serial
    private static final long serialVersionUID = 101L; // convention: 1 for model, (01 -> 99) for objects
    private final IslandField islandField;
    private final GameMode gameMode;
    private final StudentBag studentBag;
    private final List<PlayerBoard> playerBoards;
    private final Map<PawnColour, PlayerBoard> teachers;
    private final TeamMapper teamMap;
    private final TurnOrder turnOrder;
    private final EffectTracker effects;
    private final List<Cloud> clouds;
    private final List<CharacterCard> characterCards;
    private transient Optional<Lobby> subscribedListener;
    private int coinReserve;

    public GameBoard(
            IslandField islandField,
            GameMode gameMode,
            StudentBag studentBag,
            List<PlayerBoard> playerBoards,
            Map<PawnColour, PlayerBoard> teachers,
            TeamMapper teamMap,
            TurnOrder turnOrder,
            EffectTracker effects,
            List<Cloud> clouds,
            List<CharacterCard> characterCards,
            int coinReserve,
            int coinPerPlayerBoard
    ) {
        this.islandField = islandField;
        this.gameMode = gameMode;
        this.studentBag = studentBag;
        this.playerBoards = playerBoards;
        this.teachers = teachers;
        this.teamMap = teamMap;
        this.turnOrder = turnOrder;
        this.effects = effects;
        this.clouds = clouds;
        this.characterCards = characterCards;
        this.coinReserve = coinReserve - coinPerPlayerBoard * playerBoards.size();
        this.subscribedListener = Optional.empty();
    }

    public GameBoard(GameMode gameMode, String... playerNicknames) {
        final int nop = playerNicknames.length;
        this.islandField = new IslandField();
        this.gameMode = gameMode;
        this.studentBag = new StudentBag(24);
        this.playerBoards = new ArrayList<>();
        this.teachers = new EnumMap<>(PawnColour.class);
        this.coinReserve = 20 - nop; // hp: we assume 20 as amount of available coins just like the real game.

        for (int i = 0; i < nop; i++) {
            this.playerBoards.add(new PlayerBoard(i + 1, nop, playerNicknames[i], this.studentBag));
        } // add generate player based on nickname and store it
        this.turnOrder = new TurnOrder(playerBoards.toArray(new PlayerBoard[0]));
        this.teamMap = new TeamMapper(this.playerBoards);
        if (this.gameMode.equals(GameMode.ADVANCED)) {
            this.characterCards = CharacterDeckGenerator.generateCardSet(this);
            this.coinReserve = 20 - nop;
        } else {
            this.characterCards = null;
        }
        this.effects = new EffectTracker();
        this.clouds = new ArrayList<>(nop);
        //2 players: 2 cloud tiles - 3 players: 3 cloud tiles: 4 players: 4 cloud tiles
        for (int i = 0; i < nop; i++) {
            clouds.add(new Cloud(i));
        }
        refillClouds();
        subscribedListener = Optional.empty();
    }

    public void refillClouds() {
        for (Cloud cloud : this.clouds) {
            try {
                cloud.fill((ArrayList<PawnColour>) studentBag.multipleExtraction(this.playerBoards.size() == 3 ? 4 : 3));
            } catch (FullContainerException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void notifyLobby() {
        this.subscribedListener.ifPresent(e -> e.notifyPlayers(new ModelUpdateEvent(this.getModelCopy().getUserModel())));
    }

    /**
     * Sanitizes the object removing any information leaks for the user.
     *
     * @return a sanitized instance of the GameBoard object. <br>
     * <b>Note:</b> once called, all changes to the original GameBoard object will be reflected in the instance returned
     * by this method
     */
    public GameBoard getUserModel() {
        return new GameBoard(
                this.islandField,
                this.gameMode,
                null, // studentbag is removed
                this.playerBoards,
                this.teachers,
                this.teamMap,
                this.turnOrder,
                this.effects,
                this.clouds,
                this.characterCards,
                this.coinReserve,
                0 // coins per playerboard is not to be used right now
        );
    }

    /**
     * Serializes the game model to a new object.
     *
     * @return a copy of the GameBoard object. <br>
     * <b>Note:</b> once called, all changes to the original GameBoard object won't be reflected in the instance returned
     * by this method
     */
    public GameBoard getModelCopy() {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(getSerializedModel());
            ObjectInputStream reader = new ObjectInputStream(stream);
            return (GameBoard) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // never executed
    }

    /**
     * Serializes the game model to a new de-serializable byte array.
     *
     * @return a copy of the GameBoard object. <br>
     * <b>Note:</b> once called, all changes to the original GameBoard object won't be reflected in the instance returned
     * by this method
     */
    public byte[] getSerializedModel() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream writer = new ObjectOutputStream(out);
        writer.writeObject(this);
        return out.toByteArray();
    }

    public void subscribeLobby(Lobby lobby) {
        this.subscribedListener = Optional.of(lobby);
    }

    public int getCoinReserve() {
        return coinReserve;
    }

    // IMMUTABLE GETTERS //
    public List<Cloud> getClouds() {
        return List.copyOf(this.clouds);
    }

    public List<CharacterCard> getCharacterCards() {
        return List.copyOf(this.characterCards);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Map<PawnColour, PlayerBoard> getTeachers() {
        return Map.copyOf(this.teachers);
    }

    public List<PawnColour> getOwnTeachers(PlayerBoard p) {
        return this.teachers.entrySet().stream()
                .filter(e -> e.getValue().equals(p))
                .map(Map.Entry::getKey)
                .toList();
    }

    public TeamMapper getTeamMap() {
        return teamMap;
    }

    // MUTABLE GETTERS //
    public List<PlayerBoard> getMutablePlayerBoards() {
        return List.copyOf(playerBoards);
    }

    public PlayerBoard getMutablePlayerBoardById(int id) throws InvalidContainerIndexException {
        return playerBoards.stream()
                .filter(p -> p.getId() == id)
                .findAny()
                .orElseThrow(() -> new InvalidContainerIndexException(CONTAINER_NAME_PLAYERBOARDS));
    }

    public PlayerBoard getMutablePlayerBoardByNickname(String nickname) throws InvalidContainerIndexException {
        return playerBoards.stream()
                .filter(p -> p.getNickname().equals(nickname))
                .findAny()
                .orElseThrow(() -> new InvalidContainerIndexException(CONTAINER_NAME_PLAYERBOARDS));
    }

    public EffectTracker getMutableEffects() {
        return effects;
    }

    public IslandField getMutableIslandField() {
        return islandField;
    }

    public StudentBag getMutableStudentBag() {
        return studentBag;
    }

    public TurnOrder getMutableTurnOrder() {
        return turnOrder;
    }

    public void moveAndActMotherNature(int steps) {
        this.islandField.moveMotherNature(steps);
        actMotherNaturePower(this.islandField.getMutableMotherNaturePosition());
    }

    public void actMotherNaturePower(IslandGroup mnp) {
        if (mnp.getMutableNoEntryTiles().isEmpty()) {
            Optional<TeamID> optInfluencer = getInfluencerOf(mnp);
            if (optInfluencer.isPresent()) {
                TeamID newInfluencer = optInfluencer.get();
                if (
                        mnp.getTowerColour().isEmpty() ||
                                mnp.getTowerColour().get() != TowerColour.fromTeamId(newInfluencer)
                ) {
                    mnp.swapTower(this.teamMap.getMutableTowerStorage(newInfluencer));
                }
            }
            this.islandField.joinGroups();
        } else {
            mnp.resetNoEntry();
        }
    }

    // returns the team that holds influence over a particular islandgroup
    public Optional<TeamID> getInfluencerOf(IslandGroup ig) {
        Map<PawnColour, Integer> sc = ig.getStudentCount();
        Map<TeamID, Integer> ic = new HashMap<>(); // maps the team with the influence count

        for (Map.Entry<PawnColour, Integer> e : sc.entrySet()) {
            PawnColour colour = e.getKey();
            if (teachers.get(colour) == null) {
                continue;
            } //
            int count = e.getValue();
            if (effects.isPawnColourDenied()) {
                if (colour == effects.getDeniedPawnColour().get()) {
                    continue;
                }
            }
            ic.merge(this.teamMap.getTeamID(this.teachers.get(colour)), count, Integer::sum);
        }

        if (!effects.isTowerInfluenceDenied()) {
            ig.getTowerColour()
                    .ifPresent(tc -> ic.merge(tc.getTeamID(), ig.getTowerCount(), Integer::sum));
        }

        if (effects.isInfluenceIncreased()) {
            TeamID currentTeam = this.teamMap.getTeamID(turnOrder.getMutableCurrentPlayer());
            ic.merge(currentTeam, 2, Integer::sum);
        }
        List<Map.Entry<TeamID, Integer>> tbi = ic.entrySet().stream() // tbi is team by influence
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(tbi);

        this.effects.reset();

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


    public void addStudentToDiningRoom(PawnColour colour, PlayerBoard me) throws FullContainerException {
        boolean shouldGiveCoin = me.addStudentToDiningRoom(colour);
        if (shouldGiveCoin && this.coinReserve > 0) {
            this.coinReserve -= 1;
            me.addCoin();
        }
        // trigger calculation of new teacher placements
        PlayerBoard owner = this.teachers.get(colour);
        if (owner == null ||
                owner.getDiningRoomCount(colour) < me.getDiningRoomCount(colour) ||
                (this.effects.isAlternativeTeacherAssignmentEnabled() &&
                        owner.getDiningRoomCount(colour) == me.getDiningRoomCount(colour))
        ) {
            this.setTeacher(colour, me);
        }
    }

    public void setTeacher(PawnColour teacher, PlayerBoard player) {
        teachers.put(teacher, player);
    }

    public void addCoinToReserve(int amount) {
        this.coinReserve += amount;
    }
}
