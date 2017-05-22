package com.studios146.de.stundenplan24;

/**
 * Created by FM on 03.05.2017.
 *
 */

class Lesson {
    final long id;
    final String klasse;
    final String stunde;
    final String fach;
    final String lehrer;
    final String raum;
    final String info;

    /**
     * @param id ID
     * @param klasse Klasse
     * @param stunde Stunde
     * @param fach Fach
     * @param lehrer Lehrer
     * @param raum Raum
     * @param info Info
     */
    Lesson(long id, String klasse, String stunde, String fach,
           String lehrer, String raum, String info) {
        this.id = id;
        this.klasse = klasse;
        this.stunde = stunde;
        this.fach = fach;
        this.lehrer = lehrer;
        this.raum = raum;
        this.info = info;
    }

    @Override
    public String toString(){
        return this.id+","+this.klasse+","+this.stunde+","+this.fach+","+this.lehrer+","+
                this.raum+","+this.info;
    }
}
