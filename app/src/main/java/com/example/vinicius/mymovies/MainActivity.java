package com.example.vinicius.mymovies;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.vinicius.mymovies.models.Movie;

public class MainActivity extends AppCompatActivity implements MoviesListFragment.OnFragmentInteractionListener
{
	private boolean twoPane;
	private static final String DETAILFRAGMENT_TAG = "DFTAG";
	private Fragment moviesListFragment;
	private String lastMovieShowed = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		moviesListFragment = new MoviesListFragment();

		if(savedInstanceState == null)
			getSupportFragmentManager().beginTransaction().add(R.id.moviesListContainer, moviesListFragment).commit();

		if(findViewById(R.id.movieDetailContainer) != null)
		{
			// A view de detalhes estará presente apenas em telas grandes no format landscape
			// (res/layout-sw600dp-land). Se esta view estiver presente, entao a activity estará no modo twoPane
			twoPane = true;
			// No modo com dois painéis, mostra a view de detalhes nesta activity
			// adicionando ou substituindo o DetailFragment usando um fragment transaction
			if(savedInstanceState == null)
			{
				getSupportFragmentManager().beginTransaction().replace(R.id.movieDetailContainer, new DetailFragment(),
						  DETAILFRAGMENT_TAG).commit();
			}
		}
		else
		{
			twoPane = false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if(id == android.R.id.home)
		{
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putString("lastMovieShowed", lastMovieShowed);

		super.onSaveInstanceState(outState);
	}

	/* Método chamado antes de onStart() */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);

		lastMovieShowed = savedInstanceState.getString("lastMovieShowed");
	}

	@Override
	public void onItemSelected(Movie movie, ImageView thumbnailImage)
	{
		if(twoPane)
		{
			// Quando estiver em tablets em modo landscape com dois painéis, mostra a view de detalhes nesta activity
			// adicionando ou substituindo o DetailFragment usando um fragment transaction
			Bundle args = new Bundle();
			args.putParcelable(Movie.PARCELABLE_KEY, movie);
			args.putString(DetailFragment.TRANSITION_NAME, ViewCompat.getTransitionName(thumbnailImage));

			DetailFragment fragment = new DetailFragment();
			fragment.setArguments(args);

			if(lastMovieShowed == null || !lastMovieShowed.equals(movie.getTitle()))
			{
				lastMovieShowed = movie.getTitle();

				getSupportFragmentManager().beginTransaction().replace(R.id.movieDetailContainer, fragment, DETAILFRAGMENT_TAG)
						  .addSharedElement(thumbnailImage, ViewCompat.getTransitionName(thumbnailImage))
						  .commit();
			}
		}
		else
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			{
				Bundle arguments = new Bundle();
				arguments.putParcelable(Movie.PARCELABLE_KEY, movie);
				arguments.putString(DetailFragment.TRANSITION_NAME, ViewCompat.getTransitionName(thumbnailImage));

				Fragment detailFragment = new DetailFragment();
				detailFragment.setArguments(arguments);

				getSupportFragmentManager().beginTransaction()
						  .replace(R.id.moviesListContainer, detailFragment)
						  .addSharedElement(thumbnailImage, ViewCompat.getTransitionName(thumbnailImage))
						  .addToBackStack(DETAILFRAGMENT_TAG)
						  .commit();
			}
			else
			{
				Bundle arguments = new Bundle();
				arguments.putParcelable(Movie.PARCELABLE_KEY, movie);

				Fragment detailFragment = new DetailFragment();
				detailFragment.setArguments(arguments);

				getSupportFragmentManager().beginTransaction()
						  .replace(R.id.moviesListContainer, detailFragment)
						  .addToBackStack(DETAILFRAGMENT_TAG)
						  .commit();
			}
		}
	}
}
