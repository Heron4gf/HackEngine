package it.unicam.ids2026.hackhub;

public class HackHub {

    private static HackHub instance;

    private HackHub() {
        // using Singleton
    }

    public static HackHub getInstance() {
        if(instance == null) instance = new HackHub();
        return instance;
    }


}
