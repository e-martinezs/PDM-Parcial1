package com.example.parcial1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

public class ImageHandler {

    public static void loadImage(Context context, ImageView imageView, Uri uri) {
        if (uri == null){
            uri = Contact.defaultUri;
        }
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            int scale = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, scale, true);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), scaledBitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);
        } catch (Exception e) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_person);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);
        }
    }
}