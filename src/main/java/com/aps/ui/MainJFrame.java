package com.aps.ui;

import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainJFrame extends JFrame {

    public MainJFrame() throws HeadlessException {
        setName("AppFileDownloader");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("File Downloader");
        setContentPane(new MainJPanel());
        pack();
    }
}
