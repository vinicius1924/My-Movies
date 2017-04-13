package com.example.vinicius.mymovies;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinicius.mymovies.database.MovieContract;
import com.example.vinicius.mymovies.models.Movie;

public class MoviesListFragment extends Fragment implements MoviesPostersRecyclerAdapter.MoviesListItemClickListener,
		  LoaderManager.LoaderCallbacks<Cursor>
{
	private RecyclerView moviesPostersRecyclerView;
	private MoviesPostersRecyclerAdapter moviesPostersRecyclerAdapter;
	private TextView noMoviesTextView;
	private FloatingActionButton fab;

	private OnFragmentInteractionListener mListener;

	public MoviesListFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			setExitTransition(TransitionInflater.from(getContext()).
					  inflateTransition(android.R.transition.slide_right).setDuration(195));

			setEnterTransition(TransitionInflater.from(getContext()).
					  inflateTransition(android.R.transition.slide_right).setDuration(375));

			setSharedElementReturnTransition(TransitionInflater.from(getContext()).
					  inflateTransition(R.transition.change_image_transform));
		}
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_movies_list, container, false);

		fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent i = new Intent(getContext(), NewMovieActivity.class);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
				{
					startActivity(i, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
				}
				else
				{
					startActivity(i);
				}
			}
		});

		moviesPostersRecyclerView = (RecyclerView) rootView.findViewById(R.id.moviesPostersRecyclerView);
		noMoviesTextView = (TextView) rootView.findViewById(R.id.noMoviesTextView);

		//moviesPostersRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns()));
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		moviesPostersRecyclerView.setLayoutManager(layoutManager);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(moviesPostersRecyclerView.getContext(),
				  layoutManager.getOrientation());
		moviesPostersRecyclerView.addItemDecoration(dividerItemDecoration);

		moviesPostersRecyclerView.setHasFixedSize(true);

		moviesPostersRecyclerAdapter = new MoviesPostersRecyclerAdapter(getActivity().getApplicationContext(), null, 0, this);
		moviesPostersRecyclerView.setAdapter(moviesPostersRecyclerAdapter);

		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		getLoaderManager().initLoader(0, null, this);

		super.onActivityCreated(savedInstanceState);
	}

//	private int numberOfColumns() {
//		DisplayMetrics displayMetrics = new DisplayMetrics();
//		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		// Esse valor deve ser ajustado de acordo com a largura do poster que será mostrado
//
//		int widthDivider = 185;
//
//		if(getResources().getBoolean(R.bool.smallestWidth600) || getResources().getBoolean(R.bool.smallestWidth720))
//		{
//			widthDivider = 340;
//		}
//
//		if(getResources().getBoolean(R.bool.smallestWidth720))
//		{
//			widthDivider = 480;
//		}
//
//		int width = displayMetrics.widthPixels;
//		int nColumns = width / widthDivider;
//
//		if (nColumns < 2)
//			return 2;
//
//		return nColumns;
//	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if(context instanceof OnFragmentInteractionListener)
		{
			mListener = (OnFragmentInteractionListener) context;
		}
		else
		{
			throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onFavoriteListItemClick(Movie movie, ImageView thumbnailImage)
	{
		if(mListener != null)
		{
			mListener.onItemSelected(movie, thumbnailImage);
		}
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		return new android.support.v4.content.CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI,
				  null,
				  null,
				  null,
				  null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data)
	{
		if(data == null || !data.moveToFirst())
		{
			noMoviesTextView.setVisibility(View.VISIBLE);
		}
		else
		{
			noMoviesTextView.setVisibility(View.INVISIBLE);
		}

		moviesPostersRecyclerAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		moviesPostersRecyclerAdapter.swapCursor(null);
	}

	public interface OnFragmentInteractionListener
	{
		void onItemSelected(Movie movie, ImageView thumbnailImage);
	}
}
