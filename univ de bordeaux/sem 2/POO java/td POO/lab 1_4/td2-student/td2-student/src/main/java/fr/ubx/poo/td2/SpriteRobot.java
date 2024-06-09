package fr.ubx.poo.td2;

import javafx.animation.PathTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class SpriteRobot {
    private Robot robot;
    private ImageView img;

    public SpriteRobot(Robot robot) {
        this.robot = robot;
        img = new ImageView(ImageResource.imageRobot);
        updateLocation(robot.position);
    }

    private void updateLocation(Position position) {
        img.setX(position.x * ImageResource.size);
        img.setY(position.y * ImageResource.size);
    }

    public ImageView getImg() {
        return img;
    }

    public void animateMove(Position target) {
        // Make the path movement
        Position[] positionPath = robot.getPathTo(target);

        if (positionPath == null) {
            updateLocation(target);
            robot.move(target);
        } else {
            Path path = new Path();

            path.getElements().add(new MoveTo(robot.position.x * ImageResource.size + ImageResource.size / 2,
                    robot.position.y * ImageResource.size + ImageResource.size / 2));
            for (Position pos : positionPath) {
                path.getElements().add(new LineTo(pos.x * ImageResource.size + ImageResource.size / 2, pos.y * ImageResource.size + ImageResource.size / 2));
            }

            PathTransition ptr = new PathTransition();
            ptr.setDuration(Duration.millis(300 * robot.distance(target)));
            ptr.setPath(path);
            ptr.setNode(getImg());

            ptr.setOnFinished(e -> {
                robot.move(target);
            });
            ptr.play();

        }
    }
}
