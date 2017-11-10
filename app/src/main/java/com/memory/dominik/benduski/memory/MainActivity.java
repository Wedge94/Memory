package com.memory.dominik.benduski.memory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    private EditText numberOfPhotos;
    private static final int minPhotos = 4;
    private static final int maxPhotos = 10;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    FeedReaderDbHelper mReaderDbHelper;
    ImageView img;
    String mCurrentPhotoPath;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button newGameButton = (Button) findViewById(R.id.newGame);
        img = (ImageView)findViewById(R.id.imageView1);
        mReaderDbHelper = new FeedReaderDbHelper(this);
        newGameButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                numberOfPhotos = createEditTextView("Wpisz ilosc zdjec do zrobienia (min. " + minPhotos + " max. " + maxPhotos + "): ");
                newGameButton.setVisibility(View.GONE);
                commitOnEditorActionListener(mReaderDbHelper);
            }
        });
    }

    private void commitOnEditorActionListener(final FeedReaderDbHelper mReaderDbHelper)
    {
        numberOfPhotos.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
            {
                int number = Integer.parseInt(textView.getText().toString());
                if(number >= minPhotos && number <= maxPhotos)
                {
                    dispatchTakePictureIntent();

                }
                else
                {
                    toastMessage("Ilosc zdjec musi byc pomiedzy " + minPhotos + ", a " + maxPhotos + "!");
                }
                return false;
            }
        });
    }

    private EditText createEditTextView(String text)
    {
        EditText editText = new EditText(this);
        editText.setInputType(2);
        editText.setHint(text);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        RelativeLayout myLayout = (RelativeLayout)findViewById(R.id.layoutForThings);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        myLayout.addView(editText, lp);
        return editText;
    }

    private void toastMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    protected void onStop()
    {
        this.deleteDatabase(mReaderDbHelper.DATABASE_NAME);
        super.onStop();
    }

    /*private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }*/

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
        }
    }*/

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        toastMessage(mCurrentPhotoPath);
        toastMessage(mCurrentPhotoPath);
        toastMessage(mCurrentPhotoPath);
        toastMessage(mCurrentPhotoPath);
        toastMessage(mCurrentPhotoPath);
        toastMessage(mCurrentPhotoPath);
        toastMessage(mCurrentPhotoPath);
        toastMessage(mCurrentPhotoPath);
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                toastMessage("Blad");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.memory.dominik.benduski.memory",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
