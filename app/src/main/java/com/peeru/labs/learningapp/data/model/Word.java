package com.peeru.labs.learningapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Priyank Jain on 15-09-2018.
 */
@Entity(tableName = "word",
        foreignKeys = @ForeignKey(entity = Letter.class,
                parentColumns = "lid",
                childColumns = "letterId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("title"), @Index("letterId")})
public class Word {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wid")
    public int id;

    @ColumnInfo(name = "title")
    @NonNull
    public String title;

    @ColumnInfo(name = "letterId")
    public int letterId;

    public Word(@NonNull String title, int letterId) {
        this.title = title;
        this.letterId = letterId;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + title + '\'' +
                ", letterId=" + letterId +
                '}';
    }
}