package fr.ubx.poo.ugarden.go.decor.door;

import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.personage.Gardener;

public class DoorClosed extends Door{

    public DoorClosed(Position position) {
        super(position);
    }
    @Override
    public boolean walkableBy(Gardener gardener) {
        if(gardener.getNumKeys()>0) {
            return true;

        }
        return false;
    }
}
