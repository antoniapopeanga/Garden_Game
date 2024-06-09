package fr.ubx.poo.ugarden.engine;

import fr.ubx.poo.ugarden.game.Game;
import fr.ubx.poo.ugarden.view.ImageResource;
import fr.ubx.poo.ugarden.view.ImageResourceFactory;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class StatusBar {
    public static final int height = 55;
    private final Text keys = new Text();
    private final HBox level = new HBox();

    private final Text energy = new Text();

    private final Text diseaseLevel = new Text();

    private final Text insecticideNumber = new Text();
    private int gameLevel = 1;
    private final DropShadow ds = new DropShadow();

    public StatusBar(Group root, int sceneWidth, int sceneHeight) {
        // Status bar
        level.getStyleClass().add("level");
        updateLevelDisplay();

        ds.setRadius(5.0);
        ds.setOffsetX(3.0);
        ds.setOffsetY(3.0);
        ds.setColor(Color.color(0.5f, 0.5f, 0.5f));

        HBox status = new HBox();
        status.getStyleClass().add("status");
        HBox insecticideStatus = statusGroup(ImageResourceFactory.getInstance().get(ImageResource.INSECTICIDE), insecticideNumber);
        HBox keysStatus = statusGroup(ImageResourceFactory.getInstance().get(ImageResource.KEY), keys);
        HBox energyStatus = statusGroup(ImageResourceFactory.getInstance().get(ImageResource.ENERGY), energy);
        HBox diseaseLevelStatus = statusGroup(ImageResourceFactory.getInstance().get(ImageResource.POISONED_APPLE), diseaseLevel);

        status.setSpacing(40.0);
        status.getChildren().addAll(diseaseLevelStatus, insecticideStatus, keysStatus, energyStatus);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(level, status);
        hBox.getStyleClass().add("statusBar");
        hBox.relocate(0, sceneHeight);
        hBox.setPrefSize(sceneWidth, height);
        root.getChildren().add(hBox);
    }

    private HBox statusGroup(Image kind, Text number) {
        HBox group = new HBox();
        ImageView img = new ImageView(kind);
        group.setSpacing(4);
        number.setEffect(ds);
        number.setCache(true);
        number.setFill(Color.BLACK);
        number.getStyleClass().add("number");
        group.getChildren().addAll(img, number);
        return group;
    }

    private void updateLevelDisplay() {
        level.getChildren().clear();
        level.getChildren().add(new ImageView(ImageResourceFactory.getInstance().getDigit(gameLevel)));
    }

    public void update(Game game) {
        Platform.runLater(() -> {
            // Update the bonuses
            insecticideNumber.setText(Integer.toString(game.getGardener().getNumInsecticides()));
            diseaseLevel.setText("x" + Integer.toString(game.getGardener().getDiseaseLevel()));
            keys.setText(Integer.toString(game.getGardener().getNumKeys()));
            energy.setText(Integer.toString(game.getGardener().getEnergy()));

            // Update the level
            int newLevel = game.world().currentLevel();
            if (newLevel != gameLevel) {
                gameLevel = newLevel;
                updateLevelDisplay();
            }
        });
    }
}
