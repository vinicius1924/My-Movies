package com.example.vinicius.mymovies.server;

import android.content.Context;
import android.net.Uri;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.example.vinicius.mymovies.RequestQueueSingleton;
import com.example.vinicius.mymovies.interfaces.IApiServices;

import java.lang.reflect.Type;

/**
 * Created by vinicius on 16/04/16.
 */
public class ApiServices<T> implements IApiServices<T>
{
	@Override
	public void GetMovie(Response.Listener<T> successResponseRequestListener,
								Response.ErrorListener errorResponseRequestListener, Class<T> clazz, Type type,
								Context context, String requestTag, String name)
	{
		final String QUERY_PARAM = "t";
		final String TYPE_PARAM = "type";

		Uri builtUri = Uri.parse("http://www.omdbapi.com/?").buildUpon()
				  .appendQueryParameter(QUERY_PARAM, name)
				  .appendQueryParameter(TYPE_PARAM, "movie")
				  .build();

		GsonRequest<T> myReq;

		if(clazz != null)
		{
			myReq = new GsonRequest<T>(Request.Method.GET, builtUri.toString(), clazz, null, null, null,
					  successResponseRequestListener, errorResponseRequestListener);
		}
		else
		{
			myReq = new GsonRequest<T>(Request.Method.GET, builtUri.toString(), null, type, null, null,
					  successResponseRequestListener, errorResponseRequestListener);
		}

		int socketTimeout = 10000;
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				  DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		myReq.setRetryPolicy(policy);
		myReq.setTag(requestTag);

		RequestQueueSingleton.getInstance(context).addToRequestQueue(myReq);
	}
}
