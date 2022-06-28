package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Model.Enums.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_PLAYERBOARDS;

/**
 * As the name suggests, this class is the game's Model. The Model's role is to keep track of the game's state and
 * provide the tools to modify it. This class is not able to detect changes to the underlying data. If you wish for that
 * functionality look at {@link ModelWrapper}
 */
public class Model implements Serializable {
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
    private int coinReserve;

    /**
     * Constructs a DEBUG ONLY version of the model, to be used for testing purposes.
     *
     * @param islandField        provide a reference to an external {@link IslandField}
     * @param gameMode           select the game mode
     * @param studentBag         provide a reference to an external {@link StudentBag}
     * @param playerBoards       provide references to external {@link PlayerBoard}s
     * @param teachers           provide a map from a {@link PawnColour} to one of the external {@link PlayerBoard}s
     * @param teamMap            provide a reference to an external {@link TeamMapper}
     * @param turnOrder          provide a reference to an external {@link TurnOrder}
     * @param effects            provide a reference to an external {@link EffectTracker}
     * @param clouds             provide references to external {@link Cloud}s
     * @param characterCards     provide references to external {@link CharacterCard}s
     * @param coinReserve        select the amount of coins left in the bank
     * @param coinPerPlayerBoard this number times the amount of players will be subtracted from coinReserve
     */
    public Model(
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
    }

    /**
     * Construct a Model object
     *
     * @param gameMode        selects the {@link GameMode}
     * @param playerNicknames a vararg list of {@link String} representing each player. The first nickname (at index 0) will
     *                        receive ID = 0, the second ID = 1, and so on and so forth.
     */
    public Model(GameMode gameMode, String... playerNicknames) {
        final int nop = playerNicknames.length;
        this.islandField = new IslandField();
        this.gameMode = gameMode;
        this.studentBag = new StudentBag(24);
        this.playerBoards = new ArrayList<>();
        this.teachers = new EnumMap<>(PawnColour.class);
        this.coinReserve = 20 - nop; // hp: we assume 20 as amount of available coins just like the real game.

        for (int i = 0; i < nop; i++) {
            this.playerBoards.add(new PlayerBoard(i, nop, playerNicknames[i], this.studentBag));
        } // add generate player based on nickname and store it
        this.turnOrder = new TurnOrder(playerBoards);
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
        try {
            refillClouds();
        } catch (FullContainerException e) {
            Logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Refill the cloud tiles
     *
     * @throws FullContainerException if one or more cloud tiles were found full while trying to fill them up
     */
    public void refillClouds() throws FullContainerException {
        for (Cloud cloud : this.clouds) {
            cloud.fill(studentBag.multipleExtraction(this.playerBoards.size() == 3 ? 4 : 3));
        }
    }

    /**
     * Serializes the game model to a new object.
     *
     * @return a copy of the GameBoard object or null if the Serialization was not possible <br>
     * <b>Note:</b> once called, all changes to the original GameBoard object won't be reflected in the instance returned
     * by this method
     */
    public Model copy() {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(getSerializedModel());
            ObjectInputStream reader = new ObjectInputStream(stream);
            return (Model) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // never executed under normal circumstances
    }

    /**
     * Serializes the game model to a new de-serializable byte array.
     *
     * @return a copy of the GameBoard object. <br>
     * <b>Note:</b> once called, all changes to the original GameBoard object won't be reflected in the instance returned
     * by this method
     */
    private byte[] getSerializedModel() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream writer = new ObjectOutputStream(out);
        writer.writeObject(this);
        return out.toByteArray();
    }

    /**
     * @return the amount of coins left in the bank, not yet collected by players
     */
    public int getCoinReserve() {
        return coinReserve;
    }

    /**
     * @return an Unmodifiable {@link List} of the {@link CharacterCard}s currently in the game
     */
    public List<CharacterCard> getCharacterCards() {
        return List.copyOf(this.characterCards);
    }

    /**
     * @return the {@link GameMode} this Model is running
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * @return an Unmodifiable {@link Map} from {@link PawnColour} to {@link PlayerBoard}, mapping a teacher's colour to
     * its owner. If the teacher has no owner, the mapping will be null.
     */
    public Map<PawnColour, PlayerBoard> getTeachers() {
        return Map.copyOf(this.teachers);
    }

    /**
     * Accessing a {@link PlayerBoard} can only be done through its ID since the nicknames can be non-unique
     *
     * @param id the ID of the {@link PlayerBoard} to fetch for
     * @return the fetched {@link PlayerBoard}
     * @throws InvalidContainerIndexException if no board can be found matching the given ID
     */
    public PlayerBoard getMutablePlayerBoard(int id) throws InvalidContainerIndexException {
        return playerBoards.stream()
                .filter(p -> p.getId() == id)
                .findAny()
                .orElseThrow(() -> new InvalidContainerIndexException(CONTAINER_NAME_PLAYERBOARDS));
    }

    /**
     * @return a reference to the {@link EffectTracker}
     */
    public EffectTracker getMutableEffects() {
        return effects;
    }

    /**
     * @return a non-empty {@link SerializableOptional} containing the winners of the game if {@link #isGameOver()} returns true,
     * else an empty {@link SerializableOptional}
     */
    public SerializableOptional<List<PlayerBoard>> getWinners() {
        if (!isGameOver()) {
            return SerializableOptional.empty();
        }

        PlayerBoard currentPlayer = this.getMutableTurnOrder().getMutableCurrentPlayer();

        // immediate win for 3 islands left
        if (this.getMutableIslandField().getMutableGroups().size() == 3) {
            return SerializableOptional.of(this.getMutablePlayerBoardsByTeamID(this.getTeamMapper().getTeamID(currentPlayer)));
        }

        // calculate best players depending on tower storage.
        List<PlayerBoard> sortByTowerAndTeacherCount = this.getMutablePlayerBoards().stream()
                .sorted((e1, e2) -> {
                    int countE1 = this.getTeamMapper().getMutableTowerStorage(e1).getTowerCount();
                    int countE2 = this.getTeamMapper().getMutableTowerStorage(e2).getTowerCount();
                    if (countE1 != countE2) return countE1 - countE2;
                    else {
                        return -this.getOwnTeamTeacherCount(e1) + this.getOwnTeamTeacherCount(e2);
                    }
                })
                .toList();
        PlayerBoard firstPlayer = sortByTowerAndTeacherCount.get(0);
        int firstPlayerTeamTeacherCount = this.getOwnTeamTeacherCount(firstPlayer);
        int firstPlayerTowerCount = this.getTeamMapper().getMutableTowerStorage(firstPlayer).getTowerCount();
        List<PlayerBoard> winners = sortByTowerAndTeacherCount.stream()
                .takeWhile(e -> this.getTeamMapper().getMutableTowerStorage(e).getTowerCount() == firstPlayerTowerCount)
                .takeWhile(e -> this.getOwnTeamTeacherCount(e) == firstPlayerTeamTeacherCount)
                .toList();

        return SerializableOptional.of(winners);
    }

    /**
     * Checks to see if the game is over, this function should be called at the end of each complete set of changes to the
     * Model.
     *
     * @return true if the game is over, else false.
     */
    public boolean isGameOver() {
        // Check if current player has no towers left
        PlayerBoard currentPlayer = this.getMutableTurnOrder().getMutableCurrentPlayer();
        boolean noTowersLeft = this.getTeamMapper().getMutableTowerStorage(currentPlayer).getTowerCount() == 0;
        // Check if only three island groups remain
        boolean threeIslandsLeft = this.getMutableIslandField().getMutableGroups().size() <= 3;
        // Check if all assistant cards have been used
        boolean allCardsUsed = this.getMutableTurnOrder().getMutableCurrentPlayer()
                .getMutableAssistantCards().stream()
                .allMatch(AssistantCard::getUsed);
        // Check if bag is empty
        boolean emptyBag = this.getMutableStudentBag().isEmpty();
        boolean noViableCloudTiles = this.getClouds().stream().allMatch(cloud -> cloud.getContents().size() == 0);

        return noTowersLeft ||
                threeIslandsLeft ||
                this.getMutableTurnOrder().getGamePhase() == GamePhase.SETUP &&
                        (allCardsUsed || (emptyBag && noViableCloudTiles));
    }

    /**
     * @return a reference to the {@link TurnOrder}
     */
    public TurnOrder getMutableTurnOrder() {
        return turnOrder;
    }

    /**
     * @return a reference to the {@link Island}
     */
    public IslandField getMutableIslandField() {
        return islandField;
    }

    /**
     * In case there are 4 players, teams are enabled.
     *
     * @param teamID the ID of the team you wish to fetch for
     * @return an Unmodifiable {@link List} of {@link PlayerBoard}/s that are part of the specified Team
     */
    public List<PlayerBoard> getMutablePlayerBoardsByTeamID(TeamID teamID) {
        return this.getMutablePlayerBoards().stream()
                .filter(player -> teamID.equals(this.getTeamMapper().getTeamID(player)))
                .toList();
    }

    /**
     * @return a reference to the {@link TeamMapper}
     */
    public TeamMapper getTeamMapper() {
        return teamMap;
    }

    /**
     * @return an Unmodifiable {@link List} of the {@link PlayerBoard}s (which can be modified)
     */
    public List<PlayerBoard> getMutablePlayerBoards() {
        return List.copyOf(playerBoards);
    }

    /**
     * @param pb the {@link PlayerBoard} you wish to search the owned teachers of
     * @return the amount of teachers linked to such player
     */
    private int getOwnTeamTeacherCount(PlayerBoard pb) {
        return this.getOwnTeamTeacherCount(this.getTeamMapper().getTeamID(pb));
    }

    /**
     * @return a reference to the {@link StudentBag}
     */
    public StudentBag getMutableStudentBag() {
        return studentBag;
    }

    /**
     * @return an Unmodifiable {@link List} of the {@link Cloud}s (which can be modified)
     */
    public List<Cloud> getClouds() {
        return List.copyOf(this.clouds);
    }

    /**
     * @param teamID the {@link TeamID} you wish to search the owned teachers of
     * @return the amount of teachers linked to such team
     */
    private int getOwnTeamTeacherCount(TeamID teamID) {
        return this.getMutablePlayerBoardsByTeamID(teamID).stream()
                .map(this::getOwnTeachers)
                .mapToInt(List::size)
                .sum();
    }

    /**
     * @param pb the {@link PlayerBoard} you wish to search the owned teachers of
     * @return an Unmodifiable {@link List} of the {@link PawnColour}s representing the teachers owned by pb
     */
    public List<PawnColour> getOwnTeachers(PlayerBoard pb) {
        return this.teachers.entrySet().stream()
                .filter(e -> e.getValue().equals(pb))
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Mother nature can move to an island and, once moved there must act out its power
     *
     * @param steps the steps mother nature will take (can be both positive or negative or zero)
     */
    public void moveAndActMotherNature(int steps) {
        this.islandField.moveMotherNature(steps);
        actMotherNaturePower(this.islandField.getMutableMotherNaturePosition());
    }

    /**
     * Acts out mother nature's power, unifying {@link IslandGroup}s adjacent to mnp
     *
     * @param mnp an {@link IslandGroup} representing Mother nature's position.
     */
    public void actMotherNaturePower(IslandGroup mnp) {
        if (mnp.getMutableNoEntryTiles().isEmpty()) {
            SerializableOptional<TeamID> optInfluencer = getInfluencerOf(mnp);
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

    /**
     * @param ig the {@link IslandGroup} to find the influencer of
     * @return the {@link TeamID} that holds influence over ig, wrapped in a {@link SerializableOptional}
     */
    public SerializableOptional<TeamID> getInfluencerOf(IslandGroup ig) {
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
                return SerializableOptional.empty();
            case 1:
                return SerializableOptional.of(tbi.get(0).getKey());
            default: {
                if (tbi.get(0).getValue() > tbi.get(1).getValue())
                    return SerializableOptional.of(tbi.get(0).getKey());
                else return ig.getTowerColour().flatMap(tc -> SerializableOptional.of(tc.getTeamID()));
            }
        }
    }

    /**
     * Adds a student to the dining room of a player.
     *
     * @param student the {@link PawnColour} of the student you wish to add
     * @param pb      the {@link PlayerBoard} you wish to add student to
     * @throws FullContainerException if the dining room has no more free space to fit the student
     */
    public void addStudentToDiningRoom(PawnColour student, PlayerBoard pb) throws FullContainerException {
        boolean shouldGiveCoin = pb.unsafeAddStudentToDiningRoom(student);
        if (shouldGiveCoin && this.coinReserve > 0) {
            this.coinReserve -= 1;
            pb.addCoin();
        }
        // trigger calculation of new teacher placements
        PlayerBoard owner = this.teachers.get(student);
        if (
                owner == null ||
                        owner.getDiningRoomCount(student) < pb.getDiningRoomCount(student) ||
                        (
                                this.effects.isAlternativeTeacherAssignmentEnabled() &&
                                        owner.getDiningRoomCount(student) == pb.getDiningRoomCount(student)
                        )
        ) {
            this.setTeacher(student, pb);
        }
    }

    /**
     * Given a teacher, assigns it to a player
     *
     * @param teacher the {@link PawnColour} of the teacher you wish to select
     * @param player  the {@link PlayerBoard} you wish to link teacher to
     */
    protected void setTeacher(PawnColour teacher, PlayerBoard player) {
        if (player != null) {
            teachers.put(teacher, player);
        } else {
            teachers.remove(teacher);
        }
    }

    /**
     * Removes a student to the dining room of a player.
     *
     * @param student the {@link PawnColour} of the student you wish to remove
     * @param pb      the {@link PlayerBoard} you wish to remove student from
     * @throws EmptyContainerException if the dining room has no more students to remove
     */
    public void removeStudentFromDiningRoom(PawnColour student, PlayerBoard pb) throws EmptyContainerException {
        pb.unsafeRemoveStudentFromDiningRoom(student);

        // trigger calculation of new teacher placements
        PlayerBoard owner = this.teachers.get(student);
        // if we were the owner of teacher, we need to find the correct teacher owner
        if (owner != null && owner.equals(pb)) {
            for (PlayerBoard player : this.playerBoards) {
                if (player.getDiningRoomCount(student) > owner.getDiningRoomCount(student)) owner = player;
            }
            if (owner.getDiningRoomCount(student) == 0) {
                owner = null;
            }
            this.setTeacher(student, owner);
        }
    }

    /**
     * @param amount the amount of currency to bring back into the bank
     */
    public void addCoinToReserve(int amount) {
        this.coinReserve += amount;
    }
}
