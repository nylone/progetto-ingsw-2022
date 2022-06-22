package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Exceptions.Container.FullContainerException;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Constants.CONTAINER_NAME_PLAYERBOARDS;

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
        refillClouds();
    }

    public void refillClouds() {
        for (Cloud cloud : this.clouds) {
            try {
                cloud.fill(studentBag.multipleExtraction(this.playerBoards.size() == 3 ? 4 : 3));
            } catch (FullContainerException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Serializes the game model to a new object.
     *
     * @return a copy of the GameBoard object. <br>
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
        return null; // never executed
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

    public int getCoinReserve() {
        return coinReserve;
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

    public Optional<List<PlayerBoard>> getWinners() {
        if (!isGameOver()) {
            return Optional.empty();
        }

        PlayerBoard currentPlayer = this.getMutableTurnOrder().getMutableCurrentPlayer();

        // immediate win for 3 islands left
        if (this.getMutableIslandField().getMutableGroups().size() == 3) {
            return Optional.of(this.getMutablePlayerBoardsByTeamID(this.getTeamMapper().getTeamID(currentPlayer)));
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

        return Optional.of(winners);
    }

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

    public TurnOrder getMutableTurnOrder() {
        return turnOrder;
    }

    public IslandField getMutableIslandField() {
        return islandField;
    }

    public List<PlayerBoard> getMutablePlayerBoardsByTeamID(TeamID teamID) {
        return this.getMutablePlayerBoards().stream()
                .filter(player -> teamID.equals(this.getTeamMapper().getTeamID(player)))
                .toList();
    }

    public TeamMapper getTeamMapper() {
        return teamMap;
    }

    public List<PlayerBoard> getMutablePlayerBoards() {
        return List.copyOf(playerBoards);
    }

    private int getOwnTeamTeacherCount(PlayerBoard pb) {
        return this.getOwnTeamTeacherCount(this.getTeamMapper().getTeamID(pb));
    }

    public StudentBag getMutableStudentBag() {
        return studentBag;
    }

    public List<Cloud> getClouds() {
        return List.copyOf(this.clouds);
    }

    private int getOwnTeamTeacherCount(TeamID teamID) {
        return this.getMutablePlayerBoardsByTeamID(teamID).stream()
                .map(this::getOwnTeachers)
                .mapToInt(List::size)
                .sum();
    }

    public List<PawnColour> getOwnTeachers(PlayerBoard p) {
        return this.teachers.entrySet().stream()
                .filter(e -> e.getValue().equals(p))
                .map(Map.Entry::getKey)
                .toList();
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
        boolean shouldGiveCoin = me.unsafeAddStudentToDiningRoom(colour);
        if (shouldGiveCoin && this.coinReserve > 0) {
            this.coinReserve -= 1;
            me.addCoin();
        }
        // trigger calculation of new teacher placements
        PlayerBoard owner = this.teachers.get(colour);
        if (
                owner == null ||
                        owner.getDiningRoomCount(colour) < me.getDiningRoomCount(colour) ||
                        (
                                this.effects.isAlternativeTeacherAssignmentEnabled() &&
                                        owner.getDiningRoomCount(colour) == me.getDiningRoomCount(colour)
                        )
        ) {
            this.setTeacher(colour, me);
        }
    }

    protected void setTeacher(PawnColour teacher, PlayerBoard player) {
        if (player != null) {
            teachers.put(teacher, player);
        } else {
            teachers.remove(teacher);
        }
    }

    public void removeStudentFromDiningRoom(PawnColour colour, PlayerBoard me) throws EmptyContainerException {
        me.unsafeRemoveStudentFromDiningRoom(colour);

        // trigger calculation of new teacher placements
        PlayerBoard owner = this.teachers.get(colour);
        // if we were the owner of teacher, we need to find the correct teacher owner
        if (owner != null && owner.equals(me)) {
            for (PlayerBoard player : this.playerBoards) {
                if (player.getDiningRoomCount(colour) > owner.getDiningRoomCount(colour)) owner = player;
            }
            if (owner.getDiningRoomCount(colour) == 0) {
                owner = null;
            }
            this.setTeacher(colour, owner);
        }
    }

    public void addCoinToReserve(int amount) {
        this.coinReserve += amount;
    }
}
