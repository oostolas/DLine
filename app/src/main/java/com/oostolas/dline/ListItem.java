package com.oostolas.dline;

import java.util.Date;

public class ListItem {
    public int id;
    public Date date;
    public String name;

    public ListItem(int id, Date date, String name){
        this.id = id;
        this.date = date;
        this.name = name;
    }

    private static String timeFormatMain(long time) {
        return "" +
                (time / 86400000) + ":" +
                (time % 86400000 / 3600000) + ":" +
                (time % 3600000 / 60000) + ":";
    }

    static String timeFormat(long time) {
        return timeFormatMain(time) + (time % 60000 / 10);
    }

    static String timeFormat(Date date) {
        long time = date.getTime();
        if(time < 0) time *= -1;
        return timeFormatMain(time) + (time % 60000 / 1000);
    }

}
