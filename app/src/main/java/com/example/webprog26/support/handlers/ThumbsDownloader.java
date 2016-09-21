package com.example.webprog26.support.handlers;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.webprog26.support.interfaces.ThumbsDownloadListener;
import com.example.webprog26.support.utils.SimpleCameraUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by webprog26 on 20.03.2016.
 */
public class ThumbsDownloader<Token> extends HandlerThread {
    private static final String TAG = "ThumbsDownloader";

    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mRequestHandler;
    private Handler mResponseHandler;
    private ThumbsDownloadListener<Token> mThumbsDownloadListener;
    private ConcurrentMap<Token, String> mRequestMap = new ConcurrentHashMap<Token, String>();

    public void setThumbsDownloadListener(ThumbsDownloadListener listener) {
        mThumbsDownloadListener = listener;
    }

    public ThumbsDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public void queueThumbnail(Token target, String fileUri){
        Log.i(TAG, "Got an URI: " + fileUri);
        if(fileUri == null){
            mRequestMap.remove(target);
        } else{
            mRequestMap.put(target, fileUri);

            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
            .sendToTarget();
        }
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mRequestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == MESSAGE_DOWNLOAD){
                    Token target = (Token) msg.obj;
                    Log.i(TAG, "Got a request for URI: " + mRequestMap.get(target));
                    handleResults(target);
                }
            }
        };
    }

    private void handleResults(final Token target){
            final String fileUri = mRequestMap.get(target);
            if(fileUri == null) return;

            final Bitmap bitmap = SimpleCameraUtils.newInstance().decodeSampledBitmapFromFile(Uri.parse(fileUri), 80, 80);
            Log.i(TAG, "Bitmap created");

        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mRequestMap.get(target) != fileUri) return;
                mRequestMap.remove(target);
                mThumbsDownloadListener.onThumbNailDownloaded(target, bitmap);
            }
        });
    }

    public void clearQueue(){
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }
}
