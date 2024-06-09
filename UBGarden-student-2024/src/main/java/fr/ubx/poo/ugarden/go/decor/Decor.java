package fr.ubx.poo.ugarden.go.decor;

import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.GameObject;
import fr.ubx.poo.ugarden.go.Takeable;
import fr.ubx.poo.ugarden.go.Walkable;
import fr.ubx.poo.ugarden.go.bonus.Bonus;
import fr.ubx.poo.ugarden.go.decor.nest.CompositeDecor;
import fr.ubx.poo.ugarden.go.decor.nest.Nest;
import fr.ubx.poo.ugarden.go.personage.Gardener;

public abstract class Decor extends GameObject implements Walkable, Takeable {

    private Bonus bonus;
    private CompositeDecor nest;

    public Decor(Position position) {
        super(position);
    }

    public Decor(Position position, Bonus bonus) {
        super(position);
        this.bonus = bonus;
    }
    public Decor(Position position,CompositeDecor nest) {
        super(position);
        this.nest = nest;
    }

    public Bonus getBonus() {
        return bonus;
    }
    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public void setNest(Nest nest) {
        this.nest= nest;
    }
    public CompositeDecor getNest() {
        return nest;
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return gardener.canWalkOn(this);
    }

    @Override
    public void update(long now) {
        super.update(now);
        if (bonus != null) bonus.update(now);
    }

}