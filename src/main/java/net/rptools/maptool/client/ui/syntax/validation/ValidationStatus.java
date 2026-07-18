package net.rptools.maptool.client.ui.syntax.validation;

import javax.swing.*;
import java.awt.*;

public enum ValidationStatus {
    VALID((Color) UIManager.get("Actions.Green")),
    WARNING((Color) UIManager.get("Actions.Yellow")),
    ERROR((Color) UIManager.get("Actions.Red"));

    private final Color color;

    ValidationStatus(Color color) {
        this.color = color;
    }

    public Color color() {
        return color;
    }
}
