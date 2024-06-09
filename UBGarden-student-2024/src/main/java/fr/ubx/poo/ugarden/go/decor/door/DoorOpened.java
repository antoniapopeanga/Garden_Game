package fr.ubx.poo.ugarden.go.decor.door;

import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.personage.Gardener;

public class DoorOpened extends Door{
    public DoorOpened(Position position) {
        super(position);
    }
    @Override
    public boolean walkableBy(Gardener gardener) {
        return true;
    }
}
