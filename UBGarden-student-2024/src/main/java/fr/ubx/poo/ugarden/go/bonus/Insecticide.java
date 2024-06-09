package fr.ubx.poo.ugarden.go.bonus;

import fr.ubx.poo.ugarden.engine.Timer;
import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.decor.Decor;
import fr.ubx.poo.ugarden.go.personage.Gardener;

public class Insecticide extends Bonus{
    Timer timer;
    public Insecticide(Position position, Decor decor) {
        super(position, decor);
        this.timer=new Timer(10);
        this.timer.start();
    }
    public void update(long now)
    {
        timer.update(now);
        if (!timer.isRunning()) {
            remove();
            timer.start();
        }

    }
}
