package fr.ubx.poo.ugarden.go.decor.nest;

import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.GameObject;
import fr.ubx.poo.ugarden.go.decor.Decor;
import fr.ubx.poo.ugarden.go.personage.Hornet;

public abstract class CompositeDecor extends GameObject {
    private final Decor decor;

    public CompositeDecor(Position position, Decor decor) {
        super(position);
        this.decor = decor;
    }

}
