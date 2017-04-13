package com.example.vinicius.mymovies.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by vinicius on 07/04/17.
 */

public class Movie implements Parcelable, Cloneable
{
	private String title;
	private String year;
	private String released;
	private String runtime;
	private String genre;
	private String director;
	private String actors;
	private String plot;
	private String originalPosterPath;
	private String posterPath;

	public static final String PARCELABLE_KEY = "movie";


	public Movie()
	{
	}

	public Movie(Parcel in)
	{
		readFromParcel(in);
	}

	private void readFromParcel(Parcel in)
	{
		title = in.readString();
		year = in.readString();
		released = in.readString();
		runtime = in.readString();
		genre = in.readString();
		director = in.readString();
		actors = in.readString();
		plot = in.readString();
		originalPosterPath = in.readString();
		posterPath = in.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeString(title);
		parcel.writeString(year);
		parcel.writeString(released);
		parcel.writeString(runtime);
		parcel.writeString(genre);
		parcel.writeString(director);
		parcel.writeString(actors);
		parcel.writeString(plot);
		parcel.writeString(originalPosterPath);
		parcel.writeString(posterPath);
	}

	public String getOriginalPosterPath()
	{
		return originalPosterPath;
	}

	public void setOriginalPosterPath(String originalPosterPath)
	{
		this.originalPosterPath = originalPosterPath;
	}

	public String getPosterPath()
	{
		return posterPath;
	}

	public void setPosterPath(String posterPath)
	{
		this.posterPath = posterPath;
	}

	public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
	{
		public Movie createFromParcel(Parcel in)
		{
			return new Movie(in);
		}

		public Movie[] newArray(int size)
		{
			return new Movie[size];
		}
	};

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getYear()
	{
		return year;
	}

	public void setYear(String year)
	{
		this.year = year;
	}

	public String getReleased()
	{
		return released;
	}

	public void setReleased(String released)
	{
		this.released = released;
	}

	public String getRuntime()
	{
		return runtime;
	}

	public void setRuntime(String runtime)
	{
		this.runtime = runtime;
	}

	public String getGenre()
	{
		return genre;
	}

	public void setGenre(String genre)
	{
		this.genre = genre;
	}

	public String getDirector()
	{
		return director;
	}

	public void setDirector(String director)
	{
		this.director = director;
	}

	public String getActors()
	{
		return actors;
	}

	public void setActors(String actors)
	{
		this.actors = actors;
	}

	public String getPlot()
	{
		return plot;
	}

	public void setPlot(String plot)
	{
		this.plot = plot;
	}

	public Movie clone()
	{
		Movie clone = null;

		try
		{
			clone = (Movie) super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			Log.e(this.getClass().toString(), this.getClass().toString() + " does not implement Cloneable");
		}

		return clone;
	}
}
