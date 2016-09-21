package com.example.webprog26.support.interfaces;

import android.graphics.Bitmap;

/**
 * Created by webprog26 on 20.03.2016.
 */
public interface ThumbsDownloadListener<Token> {

    public void onThumbNailDownloaded(Token target, Bitmap thumbnail);
}
