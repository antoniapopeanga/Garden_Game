package fr.ubx.poo.ugarden.go.decor.door;

import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.Walkable;
import fr.ubx.poo.ugarden.go.decor.Decor;

public abstract class Door extends Decor implements Walkable {

    public Door(Position position) {
        super(position);
    }
}
