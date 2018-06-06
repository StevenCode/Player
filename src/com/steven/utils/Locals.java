package com.steven.utils;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;

public class Locals {
    public static double getLeftScreen(Node node) {
        Parent parent = node.getParent();
        Bounds childBounnds = node.getBoundsInParent();
        Bounds parentBounds = parent.localToScene(parent.getBoundsInLocal());
        return childBounnds.getMinX() + parentBounds.getMinX() + parent.getScene().getX() + parent.getScene()
                .getWindow()
                .getX();
    }

    public static double balance(double source, double left, double right) {
        source = Math.min(right, source);
        source = Math.max(source, left);

        return source;
    }

    public static double getTopScreen(Node node){
        Parent parent = node.getParent();
        Bounds childBounds = node.getBoundsInParent();
        Bounds parentBounds = parent.localToScene(parent.getBoundsInLocal());

        return childBounds.getMinY() + parentBounds.getMinY() + parent.getScene().getY()
                + parent.getScene().getWindow().getY();
    }


    public static double getRightScreen(Node node){
        Parent parent = node.getParent();
        Bounds childBounnds = node.getBoundsInParent();
        Bounds parentBounds = parent.localToScene(parent.getBoundsInLocal());

        return childBounnds.getMaxX() + parentBounds.getMinX() + parent.getScene().getX()
                + parent.getScene().getWindow().getX();
    }
}
