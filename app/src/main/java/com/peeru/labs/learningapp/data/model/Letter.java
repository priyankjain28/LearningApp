package com.peeru.labs.learningapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Priyank Jain on 15-09-2018.
 */
@Entity(tableName = "letter",
        indices = {@Index(value = "start_word", unique = true)})
public class Letter {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lid")
    public int id;

    @ColumnInfo(name = "start_word")
    @NonNull
    public String startWord;

    @Ignore
    public int score;

    public Letter(@NonNull String startWord) {
        this.startWord = startWord;
    }

    @Override
    public String toString() {
        return "Letter{" +
                "id=" + id +
                ", startWord='" + startWord + '\'' +
                ", score=" + score +
                '}';
    }
}
