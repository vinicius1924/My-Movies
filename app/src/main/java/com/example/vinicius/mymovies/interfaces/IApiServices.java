package com.example.vinicius.mymovies.interfaces;

import android.content.Context;

import com.android.volley.Response;

import java.lang.reflect.Type;

/**
 * Created by vinicius on 16/04/16.
 */
public interface IApiServices<T>
{
	void GetMovie(Response.Listener<T> successResponseRequestListener, Response.ErrorListener errorResponseRequestListener,
					  Class<T> clazz, Type type, Context context, String requestTag, String name);
}
