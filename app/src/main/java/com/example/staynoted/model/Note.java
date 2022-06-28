package com.example.staynoted.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Note implements Serializable {
    @Exclude
    private String id;
    private long date;
    private String noteText, noteTitle, noteColor;
    private ArrayList<String> imgUrls;

    public Note() {

    }

    public Note(long date, String noteText, String noteTitle, String noteColor, ArrayList<String> imgUrls) {
        this.date = date;
        this.noteText = noteText;
        this.noteTitle = noteTitle;
        this.noteColor = noteColor;
        this.imgUrls = imgUrls;
    }

    public ArrayList<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(ArrayList<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
    }
}
