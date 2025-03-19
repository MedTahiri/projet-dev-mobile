package com.example.projet_dev_mobile.module;

public class createNote {
    public int student_id;
    public String matiere;
    public String score;
    public boolean status;

    public createNote(int student_id, String matiere, String score, boolean status) {
        this.student_id = student_id;
        this.matiere = matiere;
        this.score = score;
        this.status = status;
    }

    public createNote() {
    }
}
