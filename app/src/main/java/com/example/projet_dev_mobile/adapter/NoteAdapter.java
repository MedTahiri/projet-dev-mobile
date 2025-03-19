package com.example.projet_dev_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projet_dev_mobile.R;
import com.example.projet_dev_mobile.module.Note;

import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private List<Note> noteList;

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        TextView matiere = convertView.findViewById(R.id.Matiere);
        TextView score = convertView.findViewById(R.id.Score);
        ImageView status = convertView.findViewById(R.id.Status);

        Note note = noteList.get(position);
        matiere.setText(note.matiere);
        score.setText(note.score);

        if (note.status){
            status.setImageResource(R.drawable.baseline_done_24);
        }else {
            status.setImageResource(R.drawable.baseline_clear_24);
        }

        return convertView;
    }
}
