package com.peeru.labs.learningapp.ui.learn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peeru.labs.learningapp.R;
import com.peeru.labs.learningapp.Teaching;
import com.peeru.labs.learningapp.data.model.Letter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Priyank Jain on 15-09-2018.
 */
public class LetterListAdapter extends RecyclerView.Adapter<LetterListAdapter.LetterViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Letter> letterList;
    HashMap<Integer, ArrayList<String>> wordList = new HashMap<Integer, ArrayList<String>>();
    private Context context;
    int counter;
    public LetterListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setLetterList(List<Letter> letterList,HashMap<Integer, ArrayList<String>> wordList) {
        this.letterList = letterList;
        this.wordList = wordList;
        counter = 1;
        notifyDataSetChanged();
    }

    @Override
    public LetterListAdapter.LetterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = layoutInflater.inflate(R.layout.layout_letter, parent, false);

        return new LetterListAdapter.LetterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LetterListAdapter.LetterViewHolder holder, int position) {
        if (letterList == null) {
            return;
        }

        final Letter letter = letterList.get(position);

        if (letter != null) {
            holder.letterText.setText(letter.startWord);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Teaching.class);
                    intent.putExtra("WordList", wordList.get(letter.id));
                    context.startActivity(intent);


                }
            });
            holder.module.setText("Module "+(counter++));
            if(wordList.get(letter.id) != null) {
                for (int i = 0; i < wordList.get(letter.id).size(); i++) {
                    View line = new View(context);
                    LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(100, 10);
                    lineParams.setMargins(0, 0, 30, 0);
                    line.setBackgroundColor(0xFFECECEC);
                    line.setLayoutParams(lineParams);
                    holder.linearLayout.addView(line);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (letterList == null) {
            return 0;
        } else {
            return letterList.size();
        }
    }

    static class LetterViewHolder extends RecyclerView.ViewHolder {
        private TextView letterText;
        private LinearLayout linearLayout;
        private TextView module;

        public LetterViewHolder(View itemView) {
            super(itemView);
            module = itemView.findViewById(R.id.module);
            letterText = itemView.findViewById(R.id.letter_text);
            linearLayout = itemView.findViewById(R.id.linearlayout_word);
        }
    }
}
