package com.memory.dominik.benduski.memory;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * Created by domin on 18.11.2017.
 */

public class MemoryActivity extends AppCompatActivity
{

    FeedReaderDbHelper mReaderDbHelper;
    TableLayout myLayout;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_layout);
        mReaderDbHelper = new FeedReaderDbHelper(this);
        myLayout = (TableLayout) findViewById(R.id.tableLayout);
        int numberOfPhotos = Math.round(getIntent().getIntExtra("number", -1) / 2);
        toastMessage(Integer.toString(numberOfPhotos));
        for(int i = 0; i < numberOfPhotos; i++)
        {
            int index = 0;
            TableRow tableRow = createTableRow(myLayout);
            for(int j = 0; j < 4; j++)
            {
                createImageView(mReaderDbHelper.getData().get(index).toString(), tableRow);
                index++;
            }
        }
    }

    private TableRow createTableRow(TableLayout myLayout)
    {
        TableRow newRow = new TableRow(this);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        myLayout.addView(newRow, lp);
        return newRow;
    }

    private ImageView createImageView(String uriPath, TableRow tableRow)
    {
        ImageView imageView = new ImageView(this);
        imageView.setImageURI(Uri.parse(uriPath));
        tableRow.addView(imageView);
        return imageView;
    }

    private void toastMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
