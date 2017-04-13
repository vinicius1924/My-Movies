package com.example.vinicius.mymovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by vinicius on 21/03/17.
 */

public final class NetworkUtils
{
	public static boolean isOnline(Context context)
	{
		ConnectivityManager cm =
				  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

		return isConnected;
	}
}
