package com.studios146.de.stundenplan24;

/**
 * Created by FM on 03.05.2017.
 *
 */

class Lesson {
    long id;
    String klasse;
    String tag;
    String stunde;
    String fach;
    String lehrer;
    String raum;
    String info;
    Boolean substituted;

    /**
     * @param id ID
     * @param klasse Klasse
     * @param tag Tag
     * @param stunde Stunde
     * @param fach Fach
     * @param lehrer Lehrer
     * @param raum Raum
     * @param info Info
     * @param substituted Boolean, that returns, whether lesson is substituted
     */
    Lesson(long id, String klasse, String tag, String stunde, String fach,
           String lehrer, String raum, String info, Boolean substituted) {
        this.id = id;
        this.klasse = klasse;
        this.tag = tag;
        this.stunde = stunde;
        this.fach = fach;
        this.lehrer = lehrer;
        this.raum = raum;
        this.info = info;
        this.substituted = substituted;
    }

    @Override
    public String toString(){
        return this.id+","+this.klasse+","+this.tag +","+this.stunde+","+this.fach+","+this.lehrer+","+
                this.raum+","+this.info+","+this.substituted;
    }
}
