package com.sun.bingo.util.UrlParse;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface DowloadImageCallback {

	/**
	 * 
	 * @param imageView
	 *            ImageView to receive the bitmap.
	 * @param loadedBitmap
	 *            Bitmap downloaded from url.
	 * @param url
	 *            Image url.
	 */
	void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url);

}
