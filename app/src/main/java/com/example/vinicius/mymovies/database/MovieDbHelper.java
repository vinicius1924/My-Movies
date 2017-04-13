package com.example.vinicius.mymovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vinicius on 17/03/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper
{
	/* Se o banco de dados for mudado então esse número deve ser incrementado */
	private static final int DATABASE_VERSION = 1;

	/* Nome do banco de dados */
	static final String DATABASE_NAME = "movie.db";

	public MovieDbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
				  MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				  MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
				  MovieContract.MovieEntry.COLUMN_YEAR + " TEXT, " +
				  MovieContract.MovieEntry.COLUMN_RELEASED + " TEXT, " +
				  MovieContract.MovieEntry.COLUMN_RUNTIME + " TEXT, " +
				  MovieContract.MovieEntry.COLUMN_GENRE + " TEXT, " +
				  MovieContract.MovieEntry.COLUMN_DIRECTOR + " TEXT, " +
				  MovieContract.MovieEntry.COLUMN_ACTORS + " TEXT, " +
				  MovieContract.MovieEntry.COLUMN_PLOT + " TEXT, " +
				  MovieContract.MovieEntry.COLUMN_ORIGINAL_POSTER_PATH + " TEXT, " +
				  MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT" +
				  " );";

		sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);

		onCreate(sqLiteDatabase);
	}
}
