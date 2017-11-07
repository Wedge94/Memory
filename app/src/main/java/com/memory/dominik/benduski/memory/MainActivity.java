package com.memory.dominik.benduski.memory;

import android.app.ActionBar;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private EditText numberOfPhotos;
    private final int minPhotos = 4;
    private final int maxPhotos = 10;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button newGameButton = (Button) findViewById(R.id.newGame);
        newGameButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                numberOfPhotos = createEditTextView("Wpisz ilosc zdjec do zrobienia:");
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
                toastMessage("TO ZYJE!");
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
}
