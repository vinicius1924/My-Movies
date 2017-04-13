package com.example.vinicius.mymovies;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinicius.mymovies.models.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment
{
	private Movie movie;
	private TextView movieTitle;
	private TextView movieGenre;
	private TextView movieRuntime;
	private TextView moviePlot;
	private ImageView moviePoster;
	private TextView movieReleaseDate;
	private TextView movieReleaseDateLabel;
	private TextView movieDirector;
	private TextView movieDirectorLabel;
	private TextView movieActors;
	private TextView movieActorsLabel;
	private Toolbar toolbar;

	public static final String TRANSITION_NAME = "TRANSITION_NAME";


	public DetailFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		postponeEnterTransition();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setSharedElementEnterTransition(TransitionInflater.from(getContext()).
					  inflateTransition(R.transition.change_image_transform));

			if(!getResources().getBoolean(R.bool.smallestWidth600) && !getResources().getBoolean(R.bool.smallestWidth720))
			{
				setEnterTransition(TransitionInflater.from(getContext()).
						  inflateTransition(android.R.transition.explode).setDuration(375));

				setExitTransition(TransitionInflater.from(getContext()).
						  inflateTransition(android.R.transition.explode).setDuration(195));
			}
			else
			{
				setEnterTransition(TransitionInflater.from(getContext()).
						  inflateTransition(android.R.transition.slide_right).setDuration(375));

				setExitTransition(TransitionInflater.from(getContext()).
						  inflateTransition(android.R.transition.slide_left).setDuration(195));
			}
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

		movieTitle = (TextView) rootView.findViewById(R.id.movieTitle);
		movieGenre = (TextView) rootView.findViewById(R.id.movieGenre);
		movieRuntime = (TextView) rootView.findViewById(R.id.movieRuntime);
		moviePlot = (TextView) rootView.findViewById(R.id.moviePlot);
		moviePoster = (ImageView) rootView.findViewById(R.id.moviePoster);
		movieReleaseDate = (TextView) rootView.findViewById(R.id.movieReleaseDate);
		movieReleaseDateLabel = (TextView) rootView.findViewById(R.id.movieReleaseDateLabel);
		movieDirector = (TextView) rootView.findViewById(R.id.movieDirector);
		movieDirectorLabel = (TextView) rootView.findViewById(R.id.movieDirectorLabel);
		movieActors = (TextView) rootView.findViewById(R.id.movieActors);
		movieActorsLabel = (TextView) rootView.findViewById(R.id.movieActorsLabel);

		toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

		if(!getResources().getBoolean(R.bool.smallestWidth600) && !getResources().getBoolean(R.bool.smallestWidth720))
		{
			((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

			((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

			((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}


		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		Bundle arguments = getArguments();

		if (arguments != null) {
			movie = arguments.getParcelable(Movie.PARCELABLE_KEY);

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			{
				moviePoster.setTransitionName(arguments.getString(TRANSITION_NAME));
			}

			movieTitle.setText(movie.getTitle() + " (" + movie.getYear() + ")");
			movieGenre.setText(movie.getGenre().replace(",", " |"));
			movieRuntime.setText(movie.getRuntime());
			moviePlot.setText(movie.getPlot());

			if(movie.getPosterPath() != null)
			{
				Picasso.with(getActivity()).load(new File(movie.getPosterPath())).placeholder(R.drawable.image_placeholder)
						  .into(moviePoster, new Callback()
						  {
							  @Override
							  public void onSuccess()
							  {
								  startPostponedEnterTransition();
							  }

							  @Override
							  public void onError()
							  {
								  startPostponedEnterTransition();
							  }
						  });
			}
			else
			{
				Picasso.with(getActivity()).load(movie.getOriginalPosterPath()).placeholder(R.drawable.image_placeholder)
						  .into(moviePoster, new Callback()
							{
								@Override
								public void onSuccess()
								{
									startPostponedEnterTransition();
								}

								@Override
								public void onError()
								{
									startPostponedEnterTransition();
								}
							});
			}

			movieReleaseDateLabel.setVisibility(View.VISIBLE);
			movieDirectorLabel.setVisibility(View.VISIBLE);
			movieActorsLabel.setVisibility(View.VISIBLE);

			movieReleaseDate.setText(movie.getReleased());
			movieDirector.setText(movie.getDirector());
			movieActors.setText(movie.getActors());
		}
		else
		{
			movieReleaseDateLabel.setVisibility(View.INVISIBLE);
			movieDirectorLabel.setVisibility(View.INVISIBLE);
			movieActorsLabel.setVisibility(View.INVISIBLE);
		}
	}
}
