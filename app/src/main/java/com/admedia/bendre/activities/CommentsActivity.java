package com.admedia.bendre.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.admedia.bendre.R;

import java.util.Objects;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            View promptsView = li.inflate(R.layout.new_comment, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());

            // set prompts.xml to alert dialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // get user input and set it to result
                                    // edit text
//                                        result.setText(userInput.getText());
                                }
                            })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

}
