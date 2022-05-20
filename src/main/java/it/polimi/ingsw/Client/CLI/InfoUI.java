package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.GameBoard;

public class InfoUI {

    public static String draw(GameBoard ctx, String caller) {
        String info = "";
        if(caller.equals(ctx.getMutableTurnOrder().getMutableCurrentPlayer().getNickname()))
            info = info + ctx.getMutableTurnOrder().getMutableCurrentPlayer().getNickname() + " is your turn!\n";
        else
            info = info + "It's "+ctx.getMutableTurnOrder().getMutableCurrentPlayer().getNickname() + "'s Turn\n";
        if (ctx.getMutableTurnOrder().getCurrentTurnOrder()
                .indexOf(ctx.getMutableTurnOrder().getMutableCurrentPlayer()) <
                ctx.getMutableTurnOrder().getCurrentTurnOrder().size() - 1) {
            String nextPlayer = ctx.getMutableTurnOrder().getCurrentTurnOrder().get(ctx.getMutableTurnOrder()
                    .getCurrentTurnOrder().indexOf(ctx.getMutableTurnOrder().getMutableCurrentPlayer()) + 1).getNickname();
            info = info + "Next player: " + nextPlayer + "\n";
        }
        else info = info + "Next player: " + ctx.getMutableTurnOrder().getCurrentTurnOrder().get(0).getNickname() + "\n";
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