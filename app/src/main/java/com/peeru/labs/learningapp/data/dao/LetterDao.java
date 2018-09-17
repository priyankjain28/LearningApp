package com.peeru.labs.learningapp.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.peeru.labs.learningapp.data.model.Letter;

import java.util.List;

/**
 * Created by Priyank Jain on 15-09-2018.
 */

@Dao
public interface LetterDao {
    @Query("SELECT * FROM letter WHERE lid = :id LIMIT 1")
    Letter findLetterById(int id);

    @Query("SELECT * FROM letter WHERE start_word = :startWord LIMIT 1")
    Letter findLetterByWord(String startWord);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Letter letter);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Letter... letters);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Letter letter);

    @Query("DELETE FROM letter")
    void deleteAll();

    @Query("SELECT * FROM letter ORDER BY start_word ASC")
    LiveData<List<Letter>> getAllLetters();
}
