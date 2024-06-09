package fr.ubx.poo.ugarden.go;

import fr.ubx.poo.ugarden.go.bonus.*;

public interface TakeVisitor {

    // Key
    default void take(Key key) {
    }


    //Insecticide
    default void take(Insecticide insecticide)
    {

    }

    //Poisoned apple
    default void take(PoisonedApple poisonedApple)
    {

    }
    //Apple
   default void take(Apple apple){}
    default void take(Hedgehog hedgehog){}
}
