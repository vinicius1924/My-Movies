package com.example.vinicius.mymovies;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.vinicius.mymovies.models.Movie;

public class DetailActivity extends AppCompatActivity
{
	private Movie movie;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		if (savedInstanceState == null) {
			Bundle bundle = getIntent().getExtras();
			movie = bundle.getParcelable(Movie.PARCELABLE_KEY);

			Bundle arguments = new Bundle();
			arguments.putParcelable(Movie.PARCELABLE_KEY, movie);

			DetailFragment fragment = new DetailFragment();
			fragment.setArguments(arguments);

			getSupportFragmentManager().beginTransaction()
					  .add(R.id.movieDetailContainer, fragment)
					  .commit();
		}
	}
}
