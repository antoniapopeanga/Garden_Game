package fr.ubx.poo.ugarden.go.personage;

import fr.ubx.poo.ugarden.engine.Timer;
import fr.ubx.poo.ugarden.game.Direction;
import fr.ubx.poo.ugarden.game.Game;
import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.GameObject;
import fr.ubx.poo.ugarden.go.Movable;
import fr.ubx.poo.ugarden.go.bonus.Insecticide;
import fr.ubx.poo.ugarden.go.decor.Decor;
import fr.ubx.poo.ugarden.go.decor.Tree;

public class Hornet extends GameObject implements Movable {
    private Direction direction; // The current direction of the hornet
    Position position; // The position of the hornet
    Game game; // The game instance
    Timer timer; // Timer to control the movement frequency of the hornet


    public Hornet(Position position, Game game) {
        super(position);
        this.position = position;
        this.game = game;
        // Initialize the direction randomly
        this.direction = Direction.random();
        // Initialize the timer for hornet movement frequency
        this.timer = new Timer(game.configuration().hornetMoveFrequency());
        this.timer.start(); // Start the timer
    }

    // Method to check if the hornet can move in a given direction
    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        int x = nextPos.x();
        int y = nextPos.y();
        // Check if the next position is within the bounds of the grid and not blocked by a tree
        return x >= 0 && x < game.world().getGrid().width() && y >= 0 && y < game.world().getGrid().height() && !(next instanceof Tree);
    }

    // Method to perform movement in a given direction
    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition(), 1);
        Decor next = game.world().getGrid().get(nextPos);
        setPosition(nextPos);
        // If the next position contains an insecticide, remove the hornet
        if (next.getBonus() instanceof Insecticide)
            remove();
    }

    // Method to update the state of the hornet
    public void update(long now) {
        // Update the timer
        timer.update(now);
        // If the timer is not running, perform movement
        if (!timer.isRunning()) {
            if (canMove(direction)) {
                doMove(direction);
            } else {
                // If the hornet cannot move in the current direction, change the direction randomly
                direction = Direction.random();
            }
            timer.start(); // Restart the timer
        }
    }

    // Getter method for the direction
    public Direction getDirection() {
        return direction;
    }
}
