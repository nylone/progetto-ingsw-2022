package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Model.StudentBag;
import it.polimi.ingsw.Model.TurnOrder;

import java.util.List;

/**
 * InfoUI is a graphical informative component (as a String data structure)
 * useful to print details about the {@link TurnOrder}
 * and the {@link StudentBag}
 */
public class InfoUI {

    /**
     * It will provide the players with a summary about the turn order, it will show the current and next players in
     * line and highlight if you are one of them.
     * <br>
     * It also shows the number of remaining coins if the game is in advanced mode.
     *
     * @param ctx reference to the model used to access {@link GameBoard#getMutableTurnOrder()}
     * @param caller the player to whom the information will be displayed. It is used to highlight if you are
     *               the current or next player
     * @return a visual description of useful information for the players: turns and coins
     */
    public static String draw(GameBoard ctx, String caller) {
        String info = "";
        PlayerBoard currentPlayer = ctx.getMutableTurnOrder().getMutableCurrentPlayer();
        List<PlayerBoard> turnOrder = ctx.getMutableTurnOrder().getCurrentTurnOrder();

        // Prints the current player
        if (caller.equals(currentPlayer.getNickname())) {
            // Highlight whether the current player is the person to which the console is shown
            info = info + currentPlayer.getNickname() + " is your turn!\n";
        }
        else
            info = info + "It's " + currentPlayer.getNickname() + "'s turn\n";

        // Prints the next player
        if (turnOrder.indexOf(currentPlayer) < turnOrder.size() - 1) {
            String nextPlayer = turnOrder.get(turnOrder.indexOf(currentPlayer) + 1).getNickname();
            if (caller.equals(nextPlayer)) {
                // Highlight whether the next player is the person to which the console is shown
                info = info + "Next player: you \n";
            } else info = info + "Next player: " + nextPlayer + "\n";
        } else {
            // This loops around the list of players to address the fact that the next player after the last one
            // is the first one in line
            String nextPlayer = turnOrder.get(0).getNickname(); // the first element in the list of players
            if (caller.equals(nextPlayer)) {
                info = info + "Next player: you \n";
            } else info = info + "Next player: " + nextPlayer + "\n";
        }
        // Prints the coins left if the game mode is advanced
        if (ctx.getGameMode().equals(GameMode.ADVANCED)) {
            switch (ctx.getCoinReserve()) {
                case 0 -> info = info + "There are no coins left";
                case 1 -> info = info + "There is 1 coin left";
                case default -> info = info + "There are " + ctx.getCoinReserve() + " coins left\n";
            }
        }
        return info;
    }
}