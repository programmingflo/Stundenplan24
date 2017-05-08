package com.studios146.de.stundenplan24;

/**
 * Created by FM on 03.05.2017.
 *
 */

final class Lesson {
    public final String klasse;
    final String stunde;
    public final String fach;
    public final String lehrer;
    public final String raum;
    public final String info;

    Lesson(String klasse, String stunde, String fach, String lehrer, String raum, String info) {
        this.klasse = klasse;
        this.stunde = stunde;
        this.fach = fach;
        this.lehrer = lehrer;
        this.raum = raum;
        this.info = info;
    }
}
