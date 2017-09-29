package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID id;
    private String title;
    private Date date;
    private boolean solved;
    private String suspect;

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID uuid) {
        id = uuid;
        date = new Date();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }

    @Override
    public String toString() {
        return "Crime{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", solved=" + solved +
                ", suspect='" + suspect + '\'' +
                '}';
    }
}
