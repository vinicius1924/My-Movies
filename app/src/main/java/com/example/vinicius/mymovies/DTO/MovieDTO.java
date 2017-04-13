package com.example.vinicius.mymovies.DTO;

/**
 * Created by vinicius on 12/03/17.
 */

public class MovieDTO
{
	private String Title;
	private String Year;
	private String Released;
	private String Runtime;
	private String Genre;
	private String Director;
	private String Actors;
	private String Plot;
	private String Poster;

	public MovieDTO()
	{
	}

	public String getPoster(boolean smallestWidth600, boolean smallestWidth720)
	{
		if(!Poster.equals("N/A"))
		{
			if(smallestWidth600)
			{
				String posterWithoutSize = Poster.substring(0, Poster.lastIndexOf(".") - 3);
				String posterExtension = Poster.substring(Poster.lastIndexOf("."), Poster.length());

				return posterWithoutSize + "340" + posterExtension;
			}

			if(smallestWidth720)
			{
				String posterWithoutSize = Poster.substring(0, Poster.lastIndexOf(".") - 3);
				String posterExtension = Poster.substring(Poster.lastIndexOf("."), Poster.length());

				return posterWithoutSize + "340" + posterExtension;
			}

			String posterWithoutSize = Poster.substring(0, Poster.lastIndexOf(".") - 3);
			String posterExtension = Poster.substring(Poster.lastIndexOf("."), Poster.length());

			return posterWithoutSize + "180" + posterExtension;
		}

		return null;
	}

	public void setPoster(String posterPath)
	{
		this.Poster = posterPath;
	}

	public String getTitle()
	{
		return Title;
	}

	public void setTitle(String title)
	{
		Title = title;
	}

	public String getYear()
	{
		return Year;
	}

	public void setYear(String year)
	{
		Year = year;
	}

	public String getReleased()
	{
		return Released;
	}

	public void setReleased(String released)
	{
		Released = released;
	}

	public String getRuntime()
	{
		return Runtime;
	}

	public void setRuntime(String runtime)
	{
		Runtime = runtime;
	}

	public String getGenre()
	{
		return Genre;
	}

	public void setGenre(String genre)
	{
		Genre = genre;
	}

	public String getDirector()
	{
		return Director;
	}

	public void setDirector(String director)
	{
		Director = director;
	}

	public String getActors()
	{
		return Actors;
	}

	public void setActors(String actors)
	{
		Actors = actors;
	}

	public String getPlot()
	{
		return Plot;
	}

	public void setPlot(String plot)
	{
		Plot = plot;
	}
}
