package com.example.projet_dev_mobile.module;

public class Note {
    public int id;
    public int student_id;
    public String matiere;
    public String score;
    public boolean status;

    public Note(int id, int student_id, String matiere, String score, boolean status) {
        this.id = id;
        this.student_id = student_id;
        this.matiere = matiere;
        this.score = score;
        this.status = status;
    }

    public Note() {
    }
}
