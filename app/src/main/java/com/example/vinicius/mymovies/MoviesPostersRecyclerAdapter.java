package com.example.vinicius.mymovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinicius.mymovies.database.MovieContract;
import com.example.vinicius.mymovies.models.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by vinicius on 05/04/17.
 */

public class MoviesPostersRecyclerAdapter extends CursorRecyclerAdapter<MoviesPostersRecyclerAdapter.CustomViewHolder>
{
	private Context context;
	private MoviesListItemClickListener mOnClickListener;

	public MoviesPostersRecyclerAdapter(Context context, Cursor c, int flags, MoviesListItemClickListener mOnClickListener)
	{
		super(context, c, flags);

		this.context = context;
		this.mOnClickListener = mOnClickListener;
	}

	public interface MoviesListItemClickListener
	{
		void onFavoriteListItemClick(Movie movie, ImageView thumbnailImage);
	}

	@Override
	public void bindViewHolder(final CustomViewHolder customViewHolder, Context context, Cursor cursor)
	{
		Movie movie = new Movie();

		movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
		movie.setYear(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_YEAR)));
		movie.setReleased(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASED)));
		movie.setRuntime(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RUNTIME)));
		movie.setGenre(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENRE)));
		movie.setDirector(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DIRECTOR)));
		movie.setActors(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ACTORS)));
		movie.setPlot(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT)));
		movie.setOriginalPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_POSTER_PATH)));
		movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));

		customViewHolder.bindMovie(movie);

		String originalPosterPath = movie.getOriginalPosterPath();
		String posterPath = movie.getPosterPath();

		if(posterPath != null)
		{
			Picasso.with(context).load(new File(posterPath))./*placeholder(R.drawable.image_placeholder).*/
			into(customViewHolder.thumbnailImage);
		}
		else
		{
//			if(originalPosterPath != null)
//			{
//				Picasso.with(context).load(originalPosterPath)./*placeholder(R.drawable.image_placeholder).*/into(customViewHolder.thumbnailImage);
//			}
//			else
//			{
				customViewHolder.thumbnailImage.setImageResource(R.drawable.image_placeholder);
//			}
		}

		customViewHolder.movieName.setText(movie.getTitle() + " (" + movie.getYear() + ")");

		ViewCompat.setTransitionName(customViewHolder.thumbnailImage, movie.getTitle());


//		String poster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
//
//		customViewHolder.bindMovie(movie);
//
//		Picasso picasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
//			@Override
//			public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//				Log.e("PICASSO ERROR", exception.toString());
//				String posterPath = String.valueOf(uri);
//
//				if(posterPath.startsWith("file://"))
//				{
//					posterPath = posterPath.substring(posterPath.indexOf("/data"));
//				}
//				picasso.load(new File(posterPath)).placeholder(R.drawable.image_placeholder).into(customViewHolder.thumbnailImage);
//			}
//		}).build();
//
//		if(poster != null)
//			picasso.load(new File(poster)).placeholder(R.drawable.image_placeholder).into(customViewHolder.thumbnailImage);
	}

	@Override
	public CustomViewHolder createViewHolder(Context context, ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_recycler_view_layout, parent, false);

		CustomViewHolder viewHolder = new CustomViewHolder(view);

		viewHolder.thumbnailImage.setImageResource(R.drawable.image_placeholder);

		return viewHolder;
	}

	class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		protected ImageView thumbnailImage;
		protected TextView movieName;
		protected Movie movie;

		public CustomViewHolder(View itemView)
		{
			super(itemView);

			itemView.setOnClickListener(this);

			this.thumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail);
			this.movieName = (TextView) itemView.findViewById(R.id.movieName);
		}

		public void bindMovie(Movie movie) {
			this.movie = movie;
		}

		@Override
		public void onClick(View view)
		{
			mOnClickListener.onFavoriteListItemClick(movie, thumbnailImage);
		}
	}
}
