package fr.ubx.poo.ugarden.view;

import fr.ubx.poo.ugarden.game.Direction;
import fr.ubx.poo.ugarden.go.GameObject;
import fr.ubx.poo.ugarden.go.personage.Hornet;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteHornet extends Sprite {

    public SpriteHornet(Pane layer, Hornet hornet) {
        super(layer, null, hornet);
        updateImage();
    }

    @Override
    public void updateImage() {
        Hornet hornet = (Hornet) getGameObject();
        if (hornet != null) {
            Direction direction = hornet.getDirection();
            Image image = getImage(direction);
            setImage(image);
        }

    }

    public Image getImage(Direction direction) {
        return ImageResourceFactory.getInstance().getHornet(direction);
    }
}
