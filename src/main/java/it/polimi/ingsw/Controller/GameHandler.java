package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.Input.GenericInputValidationException;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Utils;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.TeamID;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The GameHandler object is the Controller of the whole game. <br>
 * The Controller should be the only entity able to modify the model.
 */
public class GameHandler {
    private final List<PlayerAction> history;
    private final ByteArrayOutputStream backup;
    private GameBoard model;
    private Optional<Integer> winner;

    /**
     * Generates a new instance of Game. This is the default method to call to create a game.
     *
     * @param gameMode the game mode the players are going to use
     * @param players  a list of maximum 4, minimum 2 strings containing the nicknames of the players
     */
    public GameHandler(GameMode gameMode, String... players) throws InputValidationException {
        if (players.length > 1 && players.length <= 4) {
            this.history = new ArrayList<>(6);
            this.backup = new ByteArrayOutputStream();
            this.model = new GameBoard(gameMode, players);
            this.winner = Optional.empty();
        } else {
            throw new GenericInputValidationException("Players", "The number of players must be 2, 3 or 4.\n" +
                    "Players received: " + players.length);
        }
    }

    /**
     * Generates a new instance of Game. This is the debug method to call to create a game, since the internal attributes
     * are set to the parameters. <br>
     * <b>Note:</b> this method should be called <b>ONLY</b> by test code.
     *
     * @param game    an instance of GameBoard
     * @param history an instance to a list of PlayerAction, is used by the controller to check the flow of the game
     */
    GameHandler(GameBoard game, List<PlayerAction> history) {
        this.history = history;
        this.backup = new ByteArrayOutputStream();
        this.model = game;
        this.winner = Optional.empty();
    }

    /**
     * A thread safe execution request. Actions are passed in, validated and executed without risk of deadlocks or
     * undefined behaviours.
     *
     * @param action the action to be validated and (if validation succeeds) to be executed.
     * @throws InputValidationException thrown when validation fails, carries information about the error. If thrown,
     *                                  the model is guaranteed to not have been modified.
     */
    public synchronized void executeAction(PlayerAction action) throws InputValidationException {
        action.safeExecute(getHistory(), model);
        setWinner(action);
        if (action.getClass() == EndTurnOfActionPhase.class || action.getClass() == PlayAssistantCard.class) {
            commitGameState();
            this.history.clear();
            return;
        }
        history.add(action);
    }

    /**
     * @return an immutable copy of the list of player actions.<br>
     * <b>Note:</b> the single actions are immutable by default, so do not get cloned
     */
    private List<PlayerAction> getHistory() {
        return List.copyOf(history);
    }

    /**
     * Commits the current game state as the backup state for the controller. Useful when handling disconnections.
     */
    private void commitGameState() {
        try {
            this.backup.reset();
            ObjectOutputStream serModel = new ObjectOutputStream(this.backup);
            serModel.writeObject(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkNoTowerLeft() {
        PlayerBoard currentPlayer = model.getMutableTurnOrder().getMutableCurrentPlayer();
        return model.getTeamMap().getMutableTowerStorage(currentPlayer).getTowerCount() == 0;
    }

    private boolean check3IslandsLeft() {
        return model.getMutableIslandField().getMutableGroups().size() == 3;
    }

    private boolean isEndOfRound(PlayerAction action) {
        int currentPlayer = action.getPlayerBoardId();
        int lastPlayer = model.getMutableTurnOrder().getCurrentTurnOrder()
                .get(model.getMutablePlayerBoards().size() - 1).getId();
        return action.getClass() == EndTurnOfActionPhase.class && currentPlayer == lastPlayer;
    }

    private boolean checkUsedAllCards() {
        return model.getMutableTurnOrder().getMutableCurrentPlayer()
                .getMutableAssistantCards().stream()
                .allMatch(card -> card.getUsed());
    }

    private boolean checkEmptyBag() {
        return model.getMutableStudentBag().getSize() == 0;
    }

    /**
     * This method calculates the winner based on the least amount of towers left in the tower storage or
     * alternatively based on the greatest number of teachers controlled.
     * If a winner is not found, a random player is selected.
     *
     * @returnthe winning team.
     */
    private int calculateWinner() {
        Map<TeamID, Integer> towersLeft = new HashMap<>();
        for (PlayerBoard p : model.getMutablePlayerBoards()) {
            towersLeft.put(model.getTeamMap().getTeamID(p), model.getTeamMap().getMutableTowerStorage(p).getTowerCount());
        }
        List<Map.Entry<TeamID, Integer>> topTeams = towersLeft.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toCollection(ArrayList::new))
                .subList(0, 2);
        if (topTeams.get(0).getValue() == topTeams.get(1).getValue()) { // same number of towers
            // verify number of teachers
            Map<TeamID, Integer> teachersByTeam = new HashMap<>();
            for (PlayerBoard p : model.getMutablePlayerBoards()) {
                teachersByTeam.merge(model.getTeamMap().getTeamID(p),
                        model.getOwnTeachers(p).size(), Integer::sum);
            }
            for (Map.Entry e : teachersByTeam.entrySet()) {
                if (topTeams.stream().noneMatch(t -> t.getKey().equals(e.getKey()))) {
                    teachersByTeam.remove(e.getKey()); // should remove player if he has a teammate
                }
            }
            List<Map.Entry<TeamID, Integer>> topTeamsByTeacher = teachersByTeam.entrySet().stream()
                    .sorted(Comparator.comparingInt(Map.Entry::getValue))
                    .collect(Collectors.toCollection(ArrayList::new));
            switch (topTeamsByTeacher.size()) {
                case 2:
                    if (topTeamsByTeacher.get(0).getValue() == topTeamsByTeacher.get(1).getValue()) {
                        return Utils.random(topTeamsByTeacher).getKey().getTeamID();
                    }
                    return topTeamsByTeacher.get(0).getValue() > topTeamsByTeacher.get(1).getValue() ?
                            topTeamsByTeacher.get(1).getKey().getTeamID() :
                            topTeamsByTeacher.get(0).getKey().getTeamID();
                case 1:
                    return topTeamsByTeacher.get(0).getKey().getTeamID();
            }
        }
        return topTeams.get(0).getValue() > topTeams.get(1).getValue() ?
                topTeams.get(1).getKey().getTeamID() :
                topTeams.get(0).getKey().getTeamID();
    }

    public Optional<Integer> getWinner() {
        return winner;
    }

    private void setWinner(PlayerAction action) {
        PlayerBoard currentPlayer = model.getMutableTurnOrder().getMutableCurrentPlayer();
        int currentTeam = model.getTeamMap().getTeamID(currentPlayer).getTeamID();
        if (action.getClass() == MoveMotherNature.class) {
            if (checkNoTowerLeft() || check3IslandsLeft()) {
                winner = Optional.of(currentTeam);
            }
        } else if (isEndOfRound(action)) {
            if (checkUsedAllCards() || checkEmptyBag()) {
                winner = Optional.of(calculateWinner());
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
        writer.writeObject(model);
        return out.toByteArray();
    }

    /**
     * When called, signals the GameHandler that a player has disconnected.
     * This event is not treated as an action since the game state might have to be fetched from a backup.
     *
     * @param nickname the nickname of the disconnected player
     */
    protected void handleDisconnection(String nickname) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(this.backup.toByteArray());
            ObjectInputStream serModel = new ObjectInputStream(stream);
            this.model = (GameBoard) serModel.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
