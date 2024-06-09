/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ugarden.engine;

import fr.ubx.poo.ugarden.game.Direction;
import fr.ubx.poo.ugarden.game.Game;
import fr.ubx.poo.ugarden.game.Position;
import fr.ubx.poo.ugarden.go.GameObject;
import fr.ubx.poo.ugarden.go.bonus.Hedgehog;
import fr.ubx.poo.ugarden.go.bonus.Insecticide;
import fr.ubx.poo.ugarden.go.decor.Decor;
import fr.ubx.poo.ugarden.go.decor.door.DoorClosed;
import fr.ubx.poo.ugarden.go.decor.door.DoorOpened;
import fr.ubx.poo.ugarden.go.decor.ground.Grass;
import fr.ubx.poo.ugarden.go.decor.nest.CompositeDecor;
import fr.ubx.poo.ugarden.go.decor.nest.Nest;
import fr.ubx.poo.ugarden.go.personage.Gardener;
import fr.ubx.poo.ugarden.go.personage.Hornet;
import fr.ubx.poo.ugarden.view.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final Game game;
    private final Gardener gardener;
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Stage stage;
    private final Pane layer = new Pane();
    private StatusBar statusBar;
    private Input input;
    private Nest nest;
    private int currentLevel;


    public GameEngine(Game game, final Stage stage) {
        this.stage = stage;
        this.game = game;
        this.gardener = game.getGardener();
        initialize();
        buildAndSetGameLoop();
    }

    private void initialize() {
        Group root = new Group();

        int height = game.world().getGrid().height();
        int width = game.world().getGrid().width();
        int sceneWidth = width * ImageResource.size;
        int sceneHeight = height * ImageResource.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.hide();
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight);

        // Create sprites
         currentLevel = game.world().currentLevel();

        for (var decor : game.world().getGrid().values()) {
            sprites.add(SpriteFactory.create(layer, decor));
            decor.setModified(true);
            var bonus = decor.getBonus();
            if (bonus != null) {
                sprites.add(SpriteFactory.create(layer, bonus));
                bonus.setModified(true);
            } else if (decor.getNest() != null) {
                // Found the nest decor
                nest = new Nest(decor.getPosition(),game, decor);
                sprites.add(SpriteFactory.create(layer, nest));
            }
        }

        sprites.add(new SpriteGardener(layer, gardener));
    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                checkLevel();

                // Check keyboard actions
                processInput();

                // Do actions
                update(now);
                checkCollision();

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }


    private void checkLevel() {
        if (game.isSwitchLevelRequested()) {
            // Find the new level to switch to
            int nextLevel = game.world().currentLevel() + 1;

            // Clear all existing sprites from the current level
            cleanupSprites();

            // Change the current level to the new level
            game.world().setCurrentLevel(nextLevel);

            //reset the switch level request
            game.clearSwitchLevel();

            // Iterate through all the decors on the grid
            for (var decor : game.world().getGrid().values()){
                // Check if the decor is an opened door
                if (decor instanceof DoorOpened) {
                    // Save the position of the opened door
                    Position doorPosition = decor.getPosition();
                    //closing the door
                    DoorClosed doorClosed=new DoorClosed(doorPosition);
                    sprites.add(SpriteFactory.create(layer, doorClosed));
                    gardener.setPosition(doorPosition);
                }
            }

            // Initialize the game again with the updated level
            initialize();
        }
    }

    private void checkCollision() {
        // Check a collision between a hornet and the gardener
        Position gardenerPos = game.getGardener().getPosition();

        for(int i=0;i<nest.getHornets().length;i++)
        {
            if(nest.getHornets()[i]!=null) {
                //we check to see if the hornet and the gardener have the same position on the grid
                if (nest.getHornets()[i].getPosition().x() == gardenerPos.x() && nest.getHornets()[i].getPosition().y() == gardenerPos.y()) {
                    //if the gardener does not have any insecticides he loses energy points
                    if(gardener.getNumInsecticides()==0)
                        gardener.consumeEnergy(20);
                    else {
                        gardener.setNumInsecticides(gardener.getNumInsecticides()-1);
                    }
                    //we remove the hornet from the array and we mark it as deleted
                    nest.getHornets()[i].remove();
                    nest.removeHornet(nest.getHornets()[i]);

                    cleanupSprites();

                }
            }
        }
    }
    private void processInput() {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            gardener.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            gardener.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            gardener.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            gardener.requestMove(Direction.UP);
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput();
            }
        }.start();
    }

    private void update(long now) {
        game.world().getGrid().values().forEach(decor -> decor.update(now));

        gardener.update(now);

        //if the energy of the gardener gets to 0 the game is lost
        if (game.getGardener().getEnergy() == 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }

        //victory if the gardener finds the Hedgehog
        Position gardenerCurentPos=game.getGardener().getPosition();
        Decor object = game.world().getGrid().get(gardenerCurentPos);
        if (object.getBonus() instanceof Hedgehog) {
            gardener.take((Hedgehog)object.getBonus());
            gameLoop.stop();
            showMessage("Victoire!", Color.GREEN);
        }

        nest.updateNest(now);

         //we update the hornets and insecticides arrays
        for(int i=0;i<nest.getHornets().length;i++)
        {

            if(nest.getHornets()[i]!=null) {
                nest.getHornets()[i].update(now);
                SpriteHornet spriteHornet = new SpriteHornet(layer, nest.getHornets()[i]);
                sprites.add(spriteHornet);
            }
        }

        for(int i=0;i<nest.getInsecticides().length;i++)
        {

            if(nest.getInsecticides()[i]!=null) {
                nest.getInsecticides()[i].update(now);
                sprites.add(SpriteFactory.create(layer, nest.getInsecticides()[i]));

            }
        }

        //if the door is closed and the gardener has a key(walkableBy function returns true)
        //then the door opens
        if(object instanceof DoorClosed &&object.walkableBy(game.getGardener()))
        {
            DoorOpened doorOpened=new DoorOpened(gardenerCurentPos);
            sprites.add(SpriteFactory.create(layer, doorOpened));
            game.requestSwitchLevel(currentLevel);
            checkLevel();
        }

    }

    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
    }

    private void render() {
        sprites.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }
}
