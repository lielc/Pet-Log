package com.example.lielco.petlog.Pet.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Liel on 06/02/2018.
 */

public class PetLocal {
    public static PetLocal petLocal;
    private final File imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    private PetLocal () {}

    public synchronized static PetLocal getInstance() {
        if (petLocal == null) {
            petLocal = new PetLocal();
        }

        return  petLocal;
    }

    public void saveImageToFile(Context context,Bitmap image, String imageFileName){
        try {
            if (!imagesDir.exists()){
                imagesDir.mkdir();
            }

            File imageFile = new File(imagesDir,imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG,100,out);
            out.close();

            addImageToGallery(context,imageFile);

        } catch (FileNotFoundException e) {
            Log.d("TAG","Could not find file. Exception: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG","Error saving image. Exception: " + e.getMessage());
        }
    }

    public Bitmap loadImageFromFile (String imageFileName) {
        Bitmap bitmap = null;
        try {
            File imageFile = new File(imagesDir, imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            Log.d("TAG","Could not find file. Exception: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG","Error getting image. Exception: " + e.getMessage());
        }

        return bitmap;
    }

    private void addImageToGallery(Context context, File imageFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
