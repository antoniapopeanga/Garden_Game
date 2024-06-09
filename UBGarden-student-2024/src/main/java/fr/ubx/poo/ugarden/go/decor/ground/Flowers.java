package fr.ubx.poo.ugarden.go.decor.ground;

import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.decor.Decor;
import fr.ubx.poo.ugarden.go.personage.Gardener;

public class Flowers extends Ground {


    public Flowers(Position position) {
        super(position);
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return false;
    }

    @Override
    public int energyConsumptionWalk() {
        return super.energyConsumptionWalk();
    }
}
