package com.steven.utils;

import com.sun.javafx.tk.Toolkit;
import javafx.scene.text.Font;

public class FontUtils {
    public static int getPixLength(String target, Font font) {
        float width = Toolkit.getToolkit().getFontLoader().computeStringWidth(target, font);

        return (int) width;
    }
}
