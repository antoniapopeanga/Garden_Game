package fr.ubx.poo.ugarden.go;

import fr.ubx.poo.ugarden.go.decor.Decor;
import fr.ubx.poo.ugarden.go.decor.Tree;
import fr.ubx.poo.ugarden.go.decor.ground.Flowers;

public interface WalkVisitor {
    default boolean canWalkOn(Decor decor) {
        return true;
    }

    default boolean canWalkOn(Tree tree) {
        return false;
    }

}
