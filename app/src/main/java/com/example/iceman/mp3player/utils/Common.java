package com.example.iceman.mp3player.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.iceman.mp3player.R;

/**
 * Created by IceMan on 12/10/2016.
 */

public class Common {
    public static Bitmap getDefaultBg(Context mContext) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_wallpaper);
        return bitmap;
    };
}
