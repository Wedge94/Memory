package com.memory.dominik.benduski.memory;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by domin on 18.11.2017.
 */

public class MemoryActivity extends AppCompatActivity
{

    private FeedReaderDbHelper mReaderDbHelper;
    private TableLayout myLayout;
    private Map<String,Integer> mapOfImage;
    private int score;
    private TextView scoreView;
    private final int TIME = 2000;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_layout);
        mReaderDbHelper = new FeedReaderDbHelper(this);
        myLayout = (TableLayout) findViewById(R.id.tableLayout);
        int numberOfPhotos = Math.round(getIntent().getIntExtra("number", -1) / 2);
        toastMessage(Integer.toString(numberOfPhotos));
        createMapOfImage();
        score = 0;
        scoreView = (TextView) findViewById(R.id.scoreView);
        for(int i = 0; i < numberOfPhotos; i++)
        {
            TableRow tableRow = createTableRow(myLayout);
            for(int j = 0; j < 4; j++)
            {
                createImageView(mReaderDbHelper.getData().get(j).toString(), tableRow);
            }
        }
    }

    private void createMapOfImage()
    {
        mapOfImage = new HashMap<>();
        for(int i = 0; i < mReaderDbHelper.getData().size(); i++)
        {
            mapOfImage.put(mReaderDbHelper.getData().get(i).toString(), i);
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
        imageView.setImageResource(R.drawable.ic);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(75,75);
        lp.weight = 1;
        playInMemory(imageView, uriPath);
        tableRow.addView(imageView, lp);
        return imageView;
    }

    private void playInMemory(ImageView imageView, String uriPath)
    {
        final MemoryImageView miv = new MemoryImageView(imageView, uriPath, mapOfImage.get(uriPath));
        imageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(miv.getClick())
                {
                    toastMessage("Drugi click");
                    if(miv == miv.getMiv())
                    {
                        toastMessage("ten sam");
                    }
                    else
                    {
                        if(miv.getId() == miv.getMiv().getId())
                        {
                            stopOnClick();
                            miv.setOnImage();
                            Runnable mMyRunnable = new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    score += 2;
                                    scoreView.setText("Score: " + Integer.toString(score));
                                    miv.setInvisible();
                                    miv.getMiv().setInvisible();
                                    miv.setClick(false);
                                    startOnClick();
                                }
                            };
                            Handler myHandler = new Handler();
                            myHandler.postDelayed(mMyRunnable, TIME);

                        }
                        else
                        {

                            miv.setOnImage();
                            Runnable mMyRunnable = new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    score--;
                                    scoreView.setText("Score: " + Integer.toString(score));
                                    miv.setOnMark();
                                    miv.getMiv().setOnMark();
                                    miv.setClick(false);
                                    startOnClick();
                                }
                            };
                            Handler myHandler = new Handler();
                            myHandler.postDelayed(mMyRunnable, TIME);
                        }
                    }
                }
                else
                {
                    toastMessage("Pierwszy click");
                    miv.setClick(true);
                    miv.setMiv(miv);
                    miv.setOnImage();
                }
            }
        });
    }

    private void stopOnClick()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void startOnClick()
    {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void toastMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
