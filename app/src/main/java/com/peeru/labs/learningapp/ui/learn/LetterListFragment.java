package com.peeru.labs.learningapp.ui.learn;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peeru.labs.learningapp.R;
import com.peeru.labs.learningapp.data.model.Letter;
import com.peeru.labs.learningapp.data.model.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Priyank Jain on 15-09-2018.
 */
public class LetterListFragment extends Fragment {
    private LetterListAdapter letterListAdapter;
    private LetterViewModel letterViewModel;

    private Context context;

    public static LetterListFragment newInstance() {
        return new LetterListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        letterListAdapter = new LetterListAdapter(context);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_letters);
        recyclerView.setAdapter(letterListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2, GridLayoutManager.VERTICAL, false));
        return view;
    }

    private void initData() {
        letterViewModel = ViewModelProviders.of(this).get(LetterViewModel.class);

        letterViewModel.getLetterList().observe(this, new Observer<List<Letter>>() {
            @Override
            public void onChanged(@Nullable List<Letter> letters) {
               initWordData(letters);
            }
        });
    }

    private void initWordData(final List<Letter> letters) {
        letterViewModel.getWordList().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> words) {
                filterWordSetAdapter(letters,words);
            }
        });
    }

    private void filterWordSetAdapter(List<Letter> letters, List<Word> words) {
        HashMap<Integer, ArrayList<String>> wordList = new HashMap<Integer, ArrayList<String>>();
        for(Word w : words){
            if (!wordList.containsKey(w.letterId)) {
                ArrayList<String> wordAdded = new ArrayList<String>();
                wordAdded.add(w.title);
                wordList.put(w.letterId, wordAdded);
            } else {
                wordList.get(w.letterId).add(w.title);
            }
        }
        letterListAdapter.setLetterList(letters,wordList);
        letterListAdapter.notifyDataSetChanged();
    }

    public void removeData() {
        if (letterViewModel != null) {
            letterViewModel.deleteAll();
        }
    }
}
