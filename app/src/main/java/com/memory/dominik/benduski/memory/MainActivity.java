package com.memory.dominik.benduski.memory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private EditText numberOfPhotos;
    private final int minPhotos = 4;
    private final int maxPhotos = 10;
    FeedReaderDbHelper mReaderDbHelper;


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
                numberOfPhotos = createEditTextView("Wpisz ilosc zdjec do zrobienia:");
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
                    mReaderDbHelper.addData(Integer.toString(number));
                    List l = mReaderDbHelper.getData();

                    for(int j = 0; j < l.size(); j++)
                    {
                        toastMessage(l.get(j).toString());
                    }
                }
                else
                {
                    toastMessage("Chuju kurwa zla liczba");
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

    protected void onDestroy()
    {
        this.deleteDatabase("FeedReader.db");
        super.onDestroy();
    }
}
