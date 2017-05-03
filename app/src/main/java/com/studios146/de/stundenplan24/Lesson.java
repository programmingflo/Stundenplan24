package com.studios146.de.stundenplan24;

/**
 * Created by FM on 03.05.2017.
 *
 */

class Lesson {
    private final String klasse;
    private final String stunde;
    private final String fach;
    private final String lehrer;
    private final String raum;
    private final String info;

    /**
     *
     * @param klasse Klasse
     * @param stunde Stunde
     * @param fach Fach
     * @param lehrer Lehrer
     * @param raum Raum
     * @param info Info
     */
    Lesson(String klasse, String stunde, String fach, String lehrer, String raum, String info) {
        this.klasse = klasse;
        this.stunde = stunde;
        this.fach = fach;
        this.lehrer = lehrer;
        this.raum = raum;
        this.info = info;
    }
}
