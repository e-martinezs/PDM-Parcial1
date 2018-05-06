package com.example.parcial1.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.example.parcial1.R;
import com.example.parcial1.model.Contact;

public class ImageHandler {

    //Obtiene la imagen de la direccion uri y la coloca en un image view
    public static void loadImage(Context context, ImageView imageView, Uri uri) {
        //Si la uri es nula, la coloca como la imagen por defecto
        if (uri == null) {
            uri = Contact.defaultUri;
        }
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            //Disminuye el tamaño de la imagen para evitar errores
            int scale = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, scale, true);

            //Crea un bitmap con forma redonda
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), scaledBitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);
        } catch (Exception e) {

            //Si hay algun error coloca la imagen por defecto
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_person);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);
        }
    }
}