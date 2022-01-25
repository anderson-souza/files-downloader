package com.aps.utils;

public final class ThreadsUtils {

    private ThreadsUtils() {

    }

    public static int retornarNumeroThreadsDisponivel() {
        return Runtime.getRuntime().availableProcessors();
    }

}
