package fr.ubx.poo.td2;

public class Robot {
    final double cost;
    String name;
    Position position;
    double energy;


    public Robot(String name, Position position, double energy, double cost) {
        this.name = name;
        this.position = position;
        this.energy = energy;
        this.cost = cost;
    }

    // TODO
    // Robot's range of action
    int range() { return 0; }

    // TODO
    // Manhattan distance between the robot and the target
    int distance(Position target) {
        return 0;
    }

    // TODO
    // Can the robot move to the target position?
    boolean canMove(Position target) {
        return false;
    }

    // TODO
    // Actions to perform when the robot moves to the target: update the robot's coordinates, remaining energy, etc.
    void move(Position target) { }

    // TODO
    // Calculate the path between the robot and the target to be reached
    Position[] getPathTo(Position target) {
        return null;
    }


}
