/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ugarden.go.personage;

import fr.ubx.poo.ugarden.engine.Timer;
import fr.ubx.poo.ugarden.game.Direction;
import fr.ubx.poo.ugarden.game.Game;
import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.GameObject;
import fr.ubx.poo.ugarden.go.Movable;
import fr.ubx.poo.ugarden.go.TakeVisitor;
import fr.ubx.poo.ugarden.go.WalkVisitor;
import fr.ubx.poo.ugarden.go.bonus.*;
import fr.ubx.poo.ugarden.go.decor.Decor;
import fr.ubx.poo.ugarden.go.decor.door.DoorClosed;
import fr.ubx.poo.ugarden.go.decor.door.DoorOpened;
import fr.ubx.poo.ugarden.go.decor.ground.Carrots;
import fr.ubx.poo.ugarden.go.decor.ground.Grass;
import fr.ubx.poo.ugarden.go.decor.ground.Land;

public class Gardener extends GameObject implements Movable, TakeVisitor, WalkVisitor {

    private int energy; // The gardener's current energy level
    Timer moveTimer; // Timer to control the gardener's energy recovery
    private int energyRecoverDuration; // Duration for energy recovery
    private Timer sicknessTimer; // Timer to control the duration of sickness
    private Direction direction; // The direction in which the gardener is facing
    private boolean moveRequested = false; // Flag to indicate if a movement has been requested
    private int numInsecticides; // Number of insecticides the gardener possesses
    private int diseaseLevel; // Level of sickness the gardener has
    private int numKeys; // Number of keys the gardener possesses

    // Constructor
    public Gardener(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
        numInsecticides=0;
        this.energy = game.configuration().gardenerEnergy();
        this.energyRecoverDuration=game.configuration().energyRecoverDuration();
        this.diseaseLevel=1;
        this.numKeys=0;
        this.moveTimer = new Timer(energyRecoverDuration);
        this.moveTimer.start();
        this.sicknessTimer=new Timer(game.configuration().diseaseDuration());
        this.sicknessTimer.start();
    }

    // Method to handle taking a key
    @Override
    public void take(Key key) {
        numKeys++; // Increment the number of keys
        key.remove(); // Remove the key from the game world
    }

    // Method to handle taking an insecticide
    @Override
    public void take(Insecticide insecticide) {
        numInsecticides++; // Increment the number of insecticides
        insecticide.remove(); // Remove the insecticide from the game world
    }

    // Method to handle taking a poisoned apple
    @Override
    public void take(PoisonedApple poisonedApple) {
        diseaseLevel++; // Increment the sickness level
        poisonedApple.remove(); // Remove the poisoned apple from the game world
    }

    // Method to handle taking an apple
    @Override
    public void take(Apple apple) {
        diseaseLevel=1; // Reset the sickness level
        energy+=game.configuration().energyBoost(); // Increase energy with energy boost
        if(energy>=100)
            energy=100; // Ensure energy does not exceed maximum limit
        apple.remove(); // Remove the apple from the game world
    }

    // Method to handle taking a hedgehog
    @Override
    public void take(Hedgehog hedgehog) {
        hedgehog.remove(); // Remove the hedgehog from the game world
    }

    // Getter method for the number of insecticides
    public int getNumInsecticides(){ return this.numInsecticides;}

    // Setter method for the number of insecticides
    public void setNumInsecticides(int numInsecticides){this.numInsecticides=numInsecticides;}

    // Getter method for the sickness level
    public int getDiseaseLevel(){return this.diseaseLevel;}

    // Getter method for the energy level
    public int getEnergy() {
        return this.energy;
    }

    // Getter method for the number of keys
    public int getNumKeys(){return numKeys;}

    // Method to request a movement in a specific direction
    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    // Method to check if a movement is possible in a given direction
    @Override
    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        int x = nextPos.x();
        int y = nextPos.y();
        return x >= 0 && x < game.world().getGrid().width() && y >= 0 && y < game.world().getGrid().height()&& next.walkableBy(game.getGardener());
    }

    // Method to perform a movement in a given direction
    @Override
    public void doMove(Direction direction) {
        // Restart the timer
        moveTimer.start();
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        setPosition(nextPos);
        if (next != null)
            next.takenBy(this);
        Bonus bonus=next.getBonus();
        if(bonus!=null)
        {
            if(bonus instanceof Key)
                take((Key)bonus);
            if(bonus instanceof PoisonedApple)
                take((PoisonedApple) bonus);
            if(bonus instanceof Apple)
                take((Apple)bonus);
            if(bonus instanceof Insecticide)
                take((Insecticide) bonus);
        }
        // Consume energy based on the type of ground
        if(next instanceof Land)
            game.getGardener().consumeEnergy(2);
        if(next instanceof Carrots)
            game.getGardener().consumeEnergy(3);
        if(next instanceof Grass)
            game.getGardener().consumeEnergy(1);
    }

    // Method to update the gardener's state
    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
        // Check for energy recovery
        moveTimer.update(now);
        if (!moveTimer.isRunning()) {
            recoverEnergy(); // Recover energy if enough time has passed
            moveTimer.start(); // Reset the timer
        }
        // Check for sickness duration
        sicknessTimer.update(now);
        if(!sicknessTimer.isRunning())
        {
            diseaseLevel=1; // Reset sickness level
            sicknessTimer.start(); // Reset the timer
        }
    }

    // Method to handle taking damage
    public void hurt(int damage) {
    }

    // Overloaded method to handle taking damage
    public void hurt() {
        hurt(1);
    }

    // Getter method for the direction
    public Direction getDirection() {
        return direction;
    }

    // Method to consume energy
    public void consumeEnergy(int amount) {
        if (energy >= amount) {
            energy -= amount*diseaseLevel;
        } else {
            energy = 0;
        }
    }

    // Method to recover energy
    private void recoverEnergy() {
        if(energy<game.configuration().gardenerEnergy())
            energy++; // Recover 1 energy point
    }
}
