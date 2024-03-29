package com.example.plane.utils;

public final class IntentKey {

    public IntentKey() {
    }

    public static IntentKey getInstance() {
        return SingletonInternalClassHolder.instance;
    }

    private static class SingletonInternalClassHolder {
        private static final IntentKey instance = new IntentKey();
    }

    //
    public final String username = "username";
    public final String index = "index";
    public final String appwidget = "appwidget";

}
