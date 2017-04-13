package com.example.vinicius.mymovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.vinicius.mymovies.DTO.MovieDTO;
import com.example.vinicius.mymovies.database.MovieContract;
import com.example.vinicius.mymovies.server.ApiServices;
import com.example.vinicius.mymovies.services.DownloadIntentService;
import com.example.vinicius.mymovies.utils.NetworkUtils;
import com.example.vinicius.mymovies.utils.VolleyUtils;

public class NewMovieActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher
{
	private TextInputEditText movieName;
	private TextView noMovieNameError;
	private Toolbar toolbar;
	private Button buttonSearch;
	private CoordinatorLayout coordinatorLayout;
	private Snackbar snackbar;
	private boolean requestsCanceled = false;
	private ProgressBar progressBar;
	private Toast toast;
	private String lastMovieResearched;

	private final String NEWMOVIEACTIVITYTAG = getClass().getSimpleName();

	final Response.Listener<MovieDTO> successResponseRequestListener = new Response.Listener<MovieDTO>()
	{
		@Override
		public void onResponse(MovieDTO movieDTO)
		{
			enableButtonSearch();

			if(movieDTO.getTitle() == null)
			{
				showToast(getResources().getString(R.string.movie_not_found));
			}
			else
			{
				if(checkIfMovieIsAlreadySaved(movieDTO.getTitle()))
				{
					showToast(getResources().getString(R.string.movie_inserted));
				}
				else
				{
					insertMovie(movieDTO);
				}
			}
		}
	};

	final Response.ErrorListener errorResponseRequestListener = new Response.ErrorListener()
	{
		@Override
		public void onErrorResponse(VolleyError error)
		{
			enableButtonSearch();

			if(error.getLocalizedMessage() != null && error.getLocalizedMessage().toLowerCase().startsWith("movie not found"))
			{
				showToast(error.getLocalizedMessage());
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_movie);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			getWindow().setEnterTransition(TransitionInflater.from(this).
					  inflateTransition(R.transition.main_actvity_window_exit_transition).setDuration(375));

			getWindow().setExitTransition(TransitionInflater.from(this).
					  inflateTransition(R.transition.main_actvity_window_exit_transition).setDuration(195));
		}

		movieName = (TextInputEditText) findViewById(R.id.movieName);
		noMovieNameError = (TextView) findViewById(R.id.noMovieNameError);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		buttonSearch = (Button) findViewById(R.id.buttonSearch);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		buttonSearch.setOnClickListener(this);
		enableButtonSearch();

		/*
		 * Faz com que uma toolbar seja a actionbar da activity
	    */
		setSupportActionBar(toolbar);

		/* Remove o nome do app da toolbar */
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		/* Mostra o back button no toolbar */
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		movieName.addTextChangedListener(this);

		if(requestsCanceled)
		{
			findMovie(lastMovieResearched);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		int numberOfRequests = 0;

		try
		{
			numberOfRequests = VolleyUtils.getNumberOfRequestsInQueue(getApplicationContext());
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}

		if(numberOfRequests > 0)
		{
				/*
				 * Cancela todas as requisições que foram feitas usando a tag NEWMOVIEACTIVITYTAG.
				 * Isso garante que o listener da resposta da requisição não será chamado.
				 * Nesse caso o listener é successResponseRequestListener
				 */
			RequestQueueSingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll(NEWMOVIEACTIVITYTAG);
			requestsCanceled = true;
			outState.putBoolean("requestsCanceled", requestsCanceled);
			outState.putString("lastMovieResearched", lastMovieResearched);
		}
		else
		{
			requestsCanceled = false;
			outState.putBoolean("requestsCanceled", requestsCanceled);
		}

		super.onSaveInstanceState(outState);
	}

	/* Método chamado antes de onStart() */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);

		requestsCanceled = savedInstanceState.getBoolean("requestsCanceled");
		lastMovieResearched = savedInstanceState.getString("lastMovieResearched");
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		movieName.removeTextChangedListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			/* Este é o id do back button */
			case android.R.id.home:
				onBackPressed();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.buttonSearch:
				lastMovieResearched = movieName.getText().toString();
				findMovie(movieName.getText().toString());
				break;
			default:
				break;
		}
	}

	private void findMovie(String movieName)
	{
		if(movieName.isEmpty())
		{
			showMovieNameError(getResources().getString(R.string.type_movie_name));
		}
		else
		{
			hideKeyboard();

			if(NetworkUtils.isOnline(getApplicationContext()))
			{
				//						Type type = new TypeToken<MovieDTO>()
				//						{
				//						}.getType();

				if(movieName.length() > 0)
				{
					disableButtonSearch();

					ApiServices<MovieDTO> apiServices = new ApiServices<>();
					apiServices.GetMovie(successResponseRequestListener, errorResponseRequestListener, MovieDTO.class,
							  null, getApplicationContext(), NEWMOVIEACTIVITYTAG, movieName);
				}
			}
			else
			{
				snackbar = Snackbar.make(coordinatorLayout, getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.retry), new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						snackbar.dismiss();

						buttonSearch.performClick();
					}
				});

				snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
				snackbar.show();
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{

	}

	@Override
	public void afterTextChanged(Editable editable)
	{
		if(movieName.getText().hashCode() == editable.hashCode())
		{
			if(movieName.getText().toString().length() > 0)
			{
				showMovieNameError("");
			}
			else
			{
				showMovieNameError(getResources().getString(R.string.type_movie_name));
			}
		}
	}

	public void showToast(String message)
	{
		if (toast != null)
		{
			toast.cancel();
		}

		toast = Toast.makeText(NewMovieActivity.this, message, Toast.LENGTH_SHORT);
		toast.show();
	}

	public void showMovieNameError(String message)
	{
		if(message.equals(""))
		{
			noMovieNameError.setText(message);
			noMovieNameError.setVisibility(View.INVISIBLE);
		}
		else
		{
			noMovieNameError.setText(message);
			noMovieNameError.setVisibility(View.VISIBLE);
		}
	}

	public boolean checkIfMovieIsAlreadySaved(String movieTitle)
	{
		ContentResolver resolver = getContentResolver();

		String[] mProjection = {MovieContract.MovieEntry._ID};
		String mSelectionClause = MovieContract.MovieEntry.COLUMN_TITLE + " = ? ";
		String[] mSelectionArgs = {movieTitle};

		Cursor cursor = resolver.query(MovieContract.MovieEntry.CONTENT_URI, mProjection, mSelectionClause,
				  mSelectionArgs, null);

		boolean hasElement = cursor.moveToFirst();

		cursor.close();

		return hasElement;
	}

	public void insertMovie(MovieDTO movieDTO)
	{
		ContentResolver resolver = getContentResolver();
		ContentValues movieValues = new ContentValues();

		movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieDTO.getTitle());
		movieValues.put(MovieContract.MovieEntry.COLUMN_YEAR, movieDTO.getYear());
		movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASED, movieDTO.getReleased());
		movieValues.put(MovieContract.MovieEntry.COLUMN_RUNTIME, movieDTO.getRuntime());
		movieValues.put(MovieContract.MovieEntry.COLUMN_GENRE, movieDTO.getGenre());
		movieValues.put(MovieContract.MovieEntry.COLUMN_DIRECTOR, movieDTO.getDirector());
		movieValues.put(MovieContract.MovieEntry.COLUMN_ACTORS, movieDTO.getActors());
		movieValues.put(MovieContract.MovieEntry.COLUMN_PLOT, movieDTO.getPlot());


		String poster = movieDTO.getPoster(getResources().getBoolean(R.bool.smallestWidth600), getResources().getBoolean(R.bool.smallestWidth720));

		if(poster != null)
		{
			movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_POSTER_PATH, poster);
		}

		Uri insertedUri = resolver.insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);

		if(insertedUri != null)
		{
			showToast(getResources().getString(R.string.movie_inserted_with_success));
		}

		if(poster != null)
		{
			String id = insertedUri.toString().substring(insertedUri.toString().lastIndexOf("/") + 1);

			DownloadIntentService.startActionDownload(NewMovieActivity.this, poster, poster.substring(poster.lastIndexOf("/") + 1), id);
		}
	}

	private void enableButtonSearch()
	{
		buttonSearch.setEnabled(true);
		hideProgressBar();
	}

	private void disableButtonSearch()
	{
		buttonSearch.setEnabled(false);
		showProgressBar();
	}

	private void hideKeyboard() {
		View view = getCurrentFocus();
		if (view != null) {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
					  hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private void showProgressBar()
	{
		progressBar.setVisibility(View.VISIBLE);
	}

	private void hideProgressBar()
	{
		progressBar.setVisibility(View.INVISIBLE);
	}
}
