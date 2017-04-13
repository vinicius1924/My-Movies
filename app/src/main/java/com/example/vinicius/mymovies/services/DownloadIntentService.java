package com.example.vinicius.mymovies.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.vinicius.mymovies.database.MovieContract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadIntentService extends IntentService
{
	private final String DOWNLOADINTENTSERVICETAG = getClass().getSimpleName();
	// TODO: Rename actions, choose action names that describe tasks that this
	// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
	private static final String ACTION_DOWNLOAD = "com.example.vinicius.mymovies.services.action.DOWNLOAD";
	private static final String ACTION_DELETE = "com.example.vinicius.mymovies.services.action.DELETE";

	// TODO: Rename parameters
	private static final String EXTRA_URL = "com.example.vinicius.mymovies.services.extra.URL";
	private static final String EXTRA_FILE_NAME = "com.example.vinicius.mymovies.services.extra.FILENAME";
	private static final String EXTRA_ID = "com.example.vinicius.mymovies.services.extra.ID";

	public DownloadIntentService()
	{
		super("DownloadIntentService");
	}

	/**
	 * Starts this service to perform action Download with the given parameters. If
	 * the service is already performing a task this action will be queued.
	 *
	 * @see IntentService
	 */
	// TODO: Customize helper method
	public static void startActionDownload(Context context, String url, String fileName, String id)
	{
		Intent intent = new Intent(context, DownloadIntentService.class);
		intent.setAction(ACTION_DOWNLOAD);
		intent.putExtra(EXTRA_URL, url);
		intent.putExtra(EXTRA_FILE_NAME, fileName);
		intent.putExtra(EXTRA_ID, id);
		context.startService(intent);
	}

	/**
	 * Starts this service to perform action Delete with the given parameters. If
	 * the service is already performing a task this action will be queued.
	 *
	 * @see IntentService
	 */
	// TODO: Customize helper method
	public static void startActionDelete(Context context, String fileName)
	{
		Intent intent = new Intent(context, DownloadIntentService.class);
		intent.setAction(ACTION_DELETE);
		intent.putExtra(EXTRA_FILE_NAME, fileName);
		context.startService(intent);
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		if(intent != null)
		{
			final String action = intent.getAction();
			if(ACTION_DOWNLOAD.equals(action))
			{
				final String url = intent.getStringExtra(EXTRA_URL);
				final String fileName = intent.getStringExtra(EXTRA_FILE_NAME);
				final String id = intent.getStringExtra(EXTRA_ID);
				handleActionDownload(url, fileName, id);
			}
			else if(ACTION_DELETE.equals(action))
			{
				final String fileName = intent.getStringExtra(EXTRA_FILE_NAME);
				handleActionDelete(fileName);
			}
		}
	}

	/**
	 * Handle action Download in the provided background thread with the provided
	 * parameters.
	 */
	private void handleActionDownload(String url, String fileName, String id)
	{
		InputStream inputStream = null;
		FileOutputStream outputStream = null;

		try
		{
			URL downloadUrl = new URL(url);

			inputStream = downloadUrl.openConnection().getInputStream();
			outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

			int read;
			byte[] data = new byte[1024];

			while((read = inputStream.read(data)) != -1)
			{
				outputStream.write(data, 0, read);
			}

			ContentResolver resolver = getContentResolver();
			ContentValues movieValues = new ContentValues();

			movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, getFilesDir() + "/" + fileName);

			resolver.update(MovieContract.MovieEntry.buildMovieUri(Long.valueOf(id)), movieValues, null, null);
		}
		catch(MalformedURLException e)
		{
			Log.e(DOWNLOADINTENTSERVICETAG, e.toString());
		}
		catch(FileNotFoundException e)
		{
			Log.e(DOWNLOADINTENTSERVICETAG, e.toString());
		}
		catch(IOException e)
		{
			Log.e(DOWNLOADINTENTSERVICETAG, e.toString());
		}
		finally
		{
			if (outputStream != null)
			{
				try
				{
					outputStream.close();
				}
				catch(IOException e)
				{
					Log.e(DOWNLOADINTENTSERVICETAG, e.toString());
				}
			}

			if(inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch(IOException e)
				{
					Log.e(DOWNLOADINTENTSERVICETAG, e.toString());
				}
			}
		}
	}

	/**
	 * Handle action Delete in the provided background thread with the provided
	 * parameters.
	 */
	private void handleActionDelete(String fileName)
	{
		File file = new File(fileName);

		if(!file.delete())
		{
			Log.e(DOWNLOADINTENTSERVICETAG, "File " + fileName + " is not deleted");
		}
	}
}
