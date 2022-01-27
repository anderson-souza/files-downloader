package com.aps;

import lombok.extern.java.Log;

@Log
public class App {

    public static void main(String[] args) {
        new DownloadService().baixarArquivos();
    }
}
