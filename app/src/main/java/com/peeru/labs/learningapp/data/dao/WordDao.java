package com.peeru.labs.learningapp.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.peeru.labs.learningapp.data.model.Word;

import java.util.List;

/**
 * Created by Priyank Jain on 15-09-2018.
 */
@Dao
public interface WordDao {
    @Query("SELECT * FROM word WHERE title = :word LIMIT 1")
    Word findWord(String word);

    @Query("SELECT * FROM word WHERE letterId = :letterId")
    LiveData<List<Word>> findWordByLetterId(int letterId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word... words);


    @Query("DELETE FROM word")
    void deleteAll();

    @Query("SELECT * FROM word ORDER BY title ASC")
    LiveData<List<Word>> getAllWords();
}
