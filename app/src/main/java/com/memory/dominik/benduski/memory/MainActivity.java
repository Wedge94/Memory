package com.memory.dominik.benduski.memory;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private EditText numberOfPhotos;
    private static final int minPhotos = 4;
    private static final int maxPhotos = 10;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static int photosNumber;
    private static int photosTaked;
    private static FeedReaderDbHelper mReaderDbHelper;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button newGameButton = (Button) findViewById(R.id.newGame);
        mReaderDbHelper = new FeedReaderDbHelper(this);
        newGameButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                numberOfPhotos = createEditTextView("Ilosc zdjec (min. " + minPhotos + " max. " + maxPhotos + "): ");
                newGameButton.setVisibility(View.GONE);
                commitOnEditorActionListener();
            }
        });
    }

    private void commitOnEditorActionListener()
    {
        numberOfPhotos.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
            {
                photosNumber = Integer.parseInt(textView.getText().toString());
                photosTaked = mReaderDbHelper.getData().size();
                if(photosNumber >= minPhotos && photosNumber <= maxPhotos)
                {
                    if(photosNumber <= photosTaked)
                    {
                        startGame();
                    }
                    else
                    {
                        for(int j = photosTaked; j < photosNumber; j++)
                        {
                            dispatchTakePictureIntent();
                        }
                    }
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
        LinearLayout myLayout = (LinearLayout)findViewById(R.id.layoutForThings);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLayout.addView(editText, lp);
        return editText;
    }

    private ImageView createImageView(String uriPath)
    {
        ImageView imageView = new ImageView(this);
        imageView.setImageURI(Uri.parse(uriPath));
        imageView.setMaxHeight(20);
        imageView.setMaxWidth(20);
        LinearLayout myLayout = (LinearLayout) findViewById(R.id.layoutForThings);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20,20);
        myLayout.addView(imageView, lp);
        return imageView;
    }

    private void toastMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    /*protected void onStop()
    {
        this.deleteDatabase(mReaderDbHelper.DATABASE_NAME);
        super.onStop();
    }*/

    /*private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            if(photosNumber <= photosTaked)
            {
                startGame();
            }
        }
    }

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
        return image;
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            // Create the File where the photo should go
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException ex)
            {
                toastMessage("Blad");
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                String mCurrentPhotoPath = photoFile.getAbsolutePath();
                mReaderDbHelper.insertData(mCurrentPhotoPath);
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.memory.dominik.benduski.memory",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                photosTaked++;
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void startGame()
    {
        numberOfPhotos.setVisibility(View.GONE);
        List photosPaths = mReaderDbHelper.getData();
        toastMessage(Integer.toString(photosPaths.size()));
        for(int i = 0; i < photosNumber; i++)
        {
            createImageView(photosPaths.get(i).toString());
        }
    }
}
