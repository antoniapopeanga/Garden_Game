package fr.ubx.poo.ugarden.go.decor.nest;

import fr.ubx.poo.ugarden.game.Game;
import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.bonus.Insecticide;
import fr.ubx.poo.ugarden.go.decor.Decor;
import fr.ubx.poo.ugarden.go.decor.ground.Grass;
import fr.ubx.poo.ugarden.go.personage.Hornet;
import fr.ubx.poo.ugarden.engine.Timer;

import java.util.Random;


public class Nest extends CompositeDecor {


    private final Timer timer;
    private final Position position;
    Hornet hornets[];
    Insecticide insecticides[];
    Game game;

    public Nest(Position position, Game game, Decor decor) {
        super(position, decor);
        this.position = position;
        this.game=game;
        hornets = new Hornet[100];
        insecticides=new Insecticide[100];
        this.timer = new Timer(10); // 10 seconds
        this.timer.start(); // Start the timer
    }

    public void updateNest(long now) {
        timer.update(now);
        if (!timer.isRunning()) {
            spawnHornetInsecticide();
            timer.start(); // Restart the timer
        }
    }
    private void spawnHornetInsecticide() {
        // Create a new hornet with the position of the nest and add it to the game
        Hornet hornet = new Hornet( position,game);
        // We spawn an Insecticide on a random empty position on the map for every hornet created
        Position randomPos=getRandomPosition();
        Decor decor=game.world().getGrid().get(randomPos);
        while(!(decor instanceof Grass)) {
            randomPos = getRandomPosition();
            decor = game.world().getGrid().get(randomPos);
        }
        Insecticide insecticide=new Insecticide(randomPos,decor);
        decor.setBonus(insecticide);
        addHornet(hornet);
        addInsecticide(insecticide);

    }
    public void addHornet(Hornet hornet) {
        for (int i = 0; i < hornets.length; i++) {
            if (hornets[i] == null) {
                hornets[i] = hornet;
                break;
            }
        }
    }

    public void addInsecticide(Insecticide insecticide) {
        for (int i = 0; i < insecticides.length; i++) {
            if (insecticides[i] == null) {
                insecticides[i] = insecticide;
                break;
            }
        }
    }

    // Method to remove a hornet from the array
    public void removeHornet(Hornet hornet) {
        for (int i = 0; i < hornets.length; i++) {
            if (hornets[i] == hornet) {
                hornets[i] = null;
                break;
            }
        }
    }

    public void removeInsecticide(Insecticide insecticide) {
        for (int i = 0; i < insecticides.length; i++) {
            if (insecticides[i] == insecticide) {
                insecticides[i] = null;
                break;
            }
        }
    }

    public Hornet[] getHornets() {
        return hornets;
    }
    public  Insecticide[] getInsecticides(){return insecticides;}
    public Timer getTimer() {
        return timer;
    }

    private Position getRandomPosition() {
        Random random = new Random();
        int x = random.nextInt(game.world().getGrid().width());
        int y = random.nextInt(game.world().getGrid().height());
        return new Position(game.world().currentLevel(), x, y);
    }

}
