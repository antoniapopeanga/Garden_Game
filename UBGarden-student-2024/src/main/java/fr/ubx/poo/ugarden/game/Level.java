package fr.ubx.poo.ugarden.game;

import fr.ubx.poo.ugarden.go.bonus.Apple;
import fr.ubx.poo.ugarden.go.bonus.Hedgehog;
import fr.ubx.poo.ugarden.go.bonus.Key;
import fr.ubx.poo.ugarden.go.bonus.PoisonedApple;
import fr.ubx.poo.ugarden.go.decor.*;
import fr.ubx.poo.ugarden.go.decor.door.DoorClosed;
import fr.ubx.poo.ugarden.go.decor.door.DoorOpened;
import fr.ubx.poo.ugarden.go.decor.ground.Carrots;
import fr.ubx.poo.ugarden.go.decor.ground.Grass;
import fr.ubx.poo.ugarden.go.decor.ground.Land;
import fr.ubx.poo.ugarden.go.decor.ground.Flowers;
import fr.ubx.poo.ugarden.go.decor.nest.Nest;
import fr.ubx.poo.ugarden.go.decor.Tree;
import fr.ubx.poo.ugarden.launcher.MapEntity;
import fr.ubx.poo.ugarden.launcher.MapLevel;

import java.util.Collection;
import java.util.HashMap;

public class Level implements Map {



    private final int level;
    private final int width;

    private final int height;


    private final java.util.Map<Position, Decor> decors = new HashMap<>();

    public Level(Game game, int level, MapLevel entities) {
        this.level = level;
        this.width = entities.width();
        this.height = entities.height();

        //filling the Hash map
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Position position = new Position(level, i, j);
                MapEntity mapEntity = entities.get(i, j);
                switch (mapEntity) {
                    case Grass:
                        decors.put(position, new Grass(position));
                        break;
                    case Tree:
                        decors.put(position, new Tree(position));
                        break;
                    case Key: {
                        Decor grass = new Grass(position);
                        grass.setBonus(new Key(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case Apple:{
                        Decor grass = new Grass(position);
                        grass.setBonus(new Apple(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case PoisonedApple:
                    {
                        Decor grass = new Grass(position);
                        grass.setBonus(new PoisonedApple(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case DoorNextClosed:
                    {
                        decors.put(position, new DoorClosed(position));
                        break;
                    }
                    case DoorPrevOpened:
                    {
                        decors.put(position, new DoorOpened(position));
                        break;
                    }

                    case Land:
                    {
                        decors.put(position, new Land(position));
                        break;
                    }
                    case Carrots:
                    {
                        decors.put(position, new Carrots(position));
                        break;
                    }
                    case Flowers:
                    {
                        decors.put(position, new Flowers(position));
                        break;
                    }
                    case Nest:   {
                        Decor grass = new Grass(position);
                        grass.setBonus(null);
                        grass.setNest(new Nest(position,game,grass));
                        decors.put(position, grass);
                        break;
                    }

                    case Hedgehog:
                    {
                        Decor grass = new Grass(position);
                        grass.setBonus(new Hedgehog(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    default:
                        throw new RuntimeException("EntityCode " + mapEntity.name() + " not processed");
                }
            }
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    public Decor get(Position position) {
        return decors.get(position);
    }

    @Override
    public void remove(Position position) {
        decors.remove(position);
    }

    public Collection<Decor> values() {
        return decors.values();
    }


    @Override
    public boolean inside(Position position) {
        return true;
    }

    @Override
    public void set(Position position, Decor decor) {
        if (!inside(position))
            throw new IllegalArgumentException("Illegal Position");
        if (decor != null)
            decors.put(position, decor);
    }


}
