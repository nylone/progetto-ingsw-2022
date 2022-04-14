package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enums.PawnColour;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EffectTracker {
    private Optional<PawnColour> deniedPawnColour;
    private boolean denyPawnColourIsActive;
    private Optional<IslandGroup> deniedTowerGroup;
    private boolean increasedInfluenceIsActive;
    private boolean noEntryIsActive;
    private Map<IslandGroup, NoEntryTile> noEntryIsland;

    public EffectTracker() {
        deniedPawnColour = Optional.empty();
        denyPawnColourIsActive = false;
        deniedTowerGroup = Optional.empty();
        increasedInfluenceIsActive = false;
        noEntryIsActive = false;
        noEntryIsland = new HashMap<>();
    }

    public boolean isCard04Active() {
        return noEntryIsActive;
    }

    public void setByCard04(boolean noEntryIsActive) {
        this.noEntryIsActive = noEntryIsActive;
    }

    public boolean isCard09Active() {
        return denyPawnColourIsActive;
    }

    public Optional<PawnColour> getDeniedPawnColour() {
        return deniedPawnColour;
    }

    public void setByCard09(boolean denyPawnColourIsActive, PawnColour colour) {
        if (!denyPawnColourIsActive) {
            deniedPawnColour = Optional.empty();
        }
        this.denyPawnColourIsActive = denyPawnColourIsActive;
        this.deniedPawnColour = Optional.of(colour);
    }

    public boolean isCard08Active() {
        return increasedInfluenceIsActive;
    }

    public void setByCard08(boolean increasedInfluenceIsActive) {
        this.increasedInfluenceIsActive = increasedInfluenceIsActive;
    }

    public boolean islandGroupHasTowerDenied(IslandGroup ig) {
        if (deniedTowerGroup.isPresent()) {
            return deniedTowerGroup.get().equals(ig);
        }
        else return false;
    }

    public void denyIslandGroup(IslandGroup ig) {
        deniedTowerGroup = Optional.of(ig);
    }

    public boolean islandGroupHasDenyTile(IslandGroup ig) {
        return noEntryIsland.containsKey(ig);
    }

    public void removeTile(IslandGroup ig) {
        noEntryIsland.get(ig).goHome();
        noEntryIsland.remove(ig);
    }

    public void setByCard05(IslandGroup ig, NoEntryTile tile) {
        noEntryIsland.put(ig, tile);
    }

}