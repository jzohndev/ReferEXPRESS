package com.quickenloans.referexpress.misc.old;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**

 * TypeFaceProvider - holds a static HashTable of typeface objects
 */
public class TypeFaceProvider {

	private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<String, Typeface>(4);

	public static Typeface getTypeFace(Context context, String fileName) {
		Typeface tempTypeface = sTypeFaces.get(fileName);

		if (tempTypeface == null) {
			tempTypeface = Typeface.createFromAsset(context.getAssets(), fileName);
			sTypeFaces.put(fileName, tempTypeface);
		}

		return tempTypeface;
	}

}