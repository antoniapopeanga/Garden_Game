package fr.ubx.poo.td2;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class View {
    private int width;
    private int height;
    private Pane pane;
    private Scene scene;

    public View(int width, int height) {
        this.pane = new Pane();
        this.scene = new Scene(pane);
        scene.setFill(Color.rgb(241,199,128));
        this.width = width;
        this.height = height;
        drawLines();
    }

    private void drawLines() {
        for (int i = 1; i < width; i++) {
            int x = ImageResource.size*i;
            Line line = new Line(x, 0, x, ImageResource.size*height);
            line.setStroke(Color.GRAY);
            line.getStrokeDashArray().addAll(2d, 5d);
            pane.getChildren().add(line);
        }
        for (int j = 1; j < height; j++) {
            int y = ImageResource.size*j;
            Line line = new Line(0, y, ImageResource.size*width, y);
            line.getStrokeDashArray().addAll(2d, 5d);
            line.setStroke(Color.GRAY);
            pane.getChildren().add(line);
        }
    }

    /* Returns the position of the mouse in the grid */
    public Position getPosition(MouseEvent e) {
        int x = (int) e.getX() / ImageResource.size;
        int y = (int) e.getY() / ImageResource.size;
        return new Position(x, y);
    }

    public Pane getPane() {
        return pane;
    }
}
