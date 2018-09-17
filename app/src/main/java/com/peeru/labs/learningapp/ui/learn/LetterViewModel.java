package com.peeru.labs.learningapp.ui.learn;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.peeru.labs.learningapp.data.WordDatabase;
import com.peeru.labs.learningapp.data.dao.LetterDao;
import com.peeru.labs.learningapp.data.dao.WordDao;
import com.peeru.labs.learningapp.data.model.Letter;
import com.peeru.labs.learningapp.data.model.Word;

import java.util.List;

/**
 * Created by Priyank Jain on 15-09-2018.
 */
public class LetterViewModel extends AndroidViewModel {
    private LetterDao letterDao;
    private LiveData<List<Letter>> lettersLiveData;
    private WordDao wordDao;
    private LiveData<List<Word>> wordLiveData;

    public LetterViewModel(@NonNull Application application) {
        super(application);
        letterDao = WordDatabase.getDatabase(application).letterDao();
        lettersLiveData = letterDao.getAllLetters();
        wordDao = WordDatabase.getDatabase(application).wordDao();
        wordLiveData = wordDao.getAllWords();
    }

    public LiveData<List<Letter>> getLetterList() {
        return lettersLiveData;
    }

    public LiveData<List<Word>> getWordList() {
        return wordLiveData;
    }
    public void deleteAll() {
        letterDao.deleteAll();
    }
}