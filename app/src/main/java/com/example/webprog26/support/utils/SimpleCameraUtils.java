package com.example.webprog26.support.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.webprog26.support.constants.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by webprog26 on 16.03.2016.
 */
public class SimpleCameraUtils {

    private static final String TAG = "SimpleCameraUtils";

    private static SimpleCameraUtils instance;

    public static SimpleCameraUtils newInstance(){
        if(instance == null){
            instance = new SimpleCameraUtils();
        }
        return instance;
    }

    private SimpleCameraUtils() {}

    public Uri getOutPutMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        if(Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED){
            Log.e(TAG, "Environment.getExternalStorageState(): " + Environment.getExternalStorageState());
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), TAG);

        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.e(TAG, "Failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        if(type == Constants.MEDIA_TYPE_IMAGE){
           mediaFile = new File(mediaStorageDir.getPath() + File.separator + "SUPPORT_IMG_" + timeStamp + ".jpg");
        }
        /*
        place for MEDIA_TYPE == VIDEO
         */
        else{
           return null;
        }
    return mediaFile;
    }

    public Bitmap cropAndScale(Activity activity, Uri fileUri, int scale){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        String filePath = fileUri.getPath();
        BitmapFactory.decodeFile(filePath, options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        String imageType = options.outMimeType;
        Log.i(TAG, "imageType: " + imageType);
        Log.i(TAG, "filePath: " + filePath);

        Bitmap source = null;
        try{
            source = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), fileUri);
        } catch (IOException ioe){
            Log.e(TAG, "IOException: ", ioe);
            return source;
        }

        int factor = source.getHeight() <= source.getWidth() ? source.getHeight(): source.getWidth();
        int longer = source.getHeight() >= source.getWidth() ? source.getHeight(): source.getWidth();
        int x = source.getHeight() >= source.getWidth() ?0:(longer-factor)/2;
        int y = source.getHeight() <= source.getWidth() ?0:(longer-factor)/2;
        source = Bitmap.createBitmap(source, x, y, factor, factor);
        source = Bitmap.createScaledBitmap(source, scale, scale, false);
        return source;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth){
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromFile(Uri fileUri, int reqWidth, int reqHeight){
        String filePath = fileUri.getPath();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        Log.i(TAG, "decoding::: " + filePath);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }



}
