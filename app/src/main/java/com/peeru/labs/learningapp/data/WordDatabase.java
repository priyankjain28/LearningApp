package com.peeru.labs.learningapp.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.peeru.labs.learningapp.data.dao.LetterDao;
import com.peeru.labs.learningapp.data.dao.WordDao;
import com.peeru.labs.learningapp.data.model.Letter;
import com.peeru.labs.learningapp.data.model.Word;

/**
 * Created by Priyank Jain on 15-09-2018.
 */
@Database(entities = {Word.class, Letter.class}, version = 1)
public abstract class WordDatabase extends RoomDatabase {
    private static WordDatabase INSTANCE;
    private static final String DB_NAME = "words.db";

    public static WordDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordDatabase.class) {
                if (INSTANCE == null) {
                    context.getApplicationContext().deleteDatabase(DB_NAME);
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordDatabase.class, DB_NAME)
                            .allowMainThreadQueries() // SHOULD NOT BE USED IN PRODUCTION !!!
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    new PopulateDbAsync(INSTANCE).execute();
                                }
                            })
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public void clearDb() {
        if (INSTANCE != null) {
            new PopulateDbAsync(INSTANCE).execute();
        }
    }

    public abstract WordDao wordDao();

    public abstract LetterDao letterDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final WordDao wordDao;
        private final LetterDao letterDao;

        public PopulateDbAsync(WordDatabase instance) {
            wordDao = instance.wordDao();
            letterDao = instance.letterDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAll();
            letterDao.deleteAll();

            Letter letterOne = new Letter("Sh/S");
            Letter letterTwo = new Letter("Th/T");
            Letter letterThree = new Letter("Ph/P");
            Letter letterFour = new Letter("Bi/B");
            Letter letterFive = new Letter("Ae/A");
            Letter letterSix = new Letter("Or/O");

            final int lIdOne = (int)letterDao.insert(letterOne);
            Word wordIOne = new Word("Sheep", lIdOne);
            Word wordITwo = new Word("Shape", lIdOne);
            Word wordIThree = new Word("Shoot", lIdOne);

            final int lIdTwo = (int) letterDao.insert(letterTwo);
            Word wordIIOne = new Word("Then", (int) lIdTwo);
            Word wordIITwo = new Word("Thanks", (int) lIdTwo);
            Word wordIIThree = new Word("Throw", (int) lIdTwo);


            final int lIdThree = (int) letterDao.insert(letterThree);
            Word wordIIIOne = new Word("Phone", (int) lIdThree);
            Word wordIIITwo = new Word("Photo", (int) lIdThree);
            Word wordIIIThree = new Word("Phrase", (int) lIdThree);

            final int lIdFour = (int) letterDao.insert(letterFour);
            Word wordIVOne = new Word("Bill", (int) lIdFour);
            Word wordIVTwo = new Word("Bike", (int) lIdFour);
            Word wordIVThree = new Word("Birth", (int) lIdFour);



            final int lIdFive = (int) letterDao.insert(letterFive);
            Word wordVOne = new Word("Air", (int) lIdFive);
            Word wordVTwo = new Word("Aim ", (int) lIdFive);
            Word wordVThree = new Word("Aid", (int) lIdFive);


            final int lIdSix = (int) letterDao.insert(letterSix);
            Word wordVIOne = new Word("Oreo", (int) lIdSix);
            Word wordVITwo = new Word("Oral", (int) lIdSix);
            Word wordVIThree = new Word("Origin", (int) lIdSix);

            wordDao.insert(wordIOne,wordITwo,wordIThree,wordIIOne,wordIITwo,wordIIThree,wordIIIOne,wordIIITwo,wordIIIThree,
                    wordIVOne,wordIVTwo,wordIVThree,wordVOne,wordVTwo,wordVThree,wordVIOne,wordVITwo,wordVIThree);

            return null;
        }
    }
}

