package com.aps;

import com.aps.ui.MainJFrame;
import java.awt.Insets;
import java.util.logging.Level;
import lombok.extern.java.Log;

@Log
public class App {

    public static void main(final String[] args) {
        log.log(Level.INFO, "Iniciando aplicação");
//        new DownloadService().downloadFiles();
        startUI();
    }

    private static void startUI() {
        final MainJFrame mainJFrame = new MainJFrame();
        final Insets insets = mainJFrame.getInsets();
        mainJFrame.setSize(mainJFrame.getWidth() + insets.left + insets.right,
            mainJFrame.getHeight() + insets.top + insets.bottom);
        mainJFrame.setVisible(true);

    }
}
