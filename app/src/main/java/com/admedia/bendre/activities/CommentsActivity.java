package com.admedia.bendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.admedia.bendre.R;
import com.admedia.bendre.model.Post;
import com.admedia.bendre.util.MessageUtil;

import static com.admedia.bendre.activities.PostDetailsActivity.SELECTED_POST;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Post selectedPost = (Post) getIntent().getSerializableExtra(SELECTED_POST);

        FloatingActionButton fabComment = findViewById(R.id.fab_new_comment);
        fabComment.setOnClickListener(view -> {
            MessageUtil.getInstance().ToastMessage(getApplicationContext(), "Bientôt disponible");
            // get prompts.xml view
//            LayoutInflater li = LayoutInflater.from(getApplicationContext());
//            View promptsView = li.inflate(R.layout.new_comment, null);
//
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
//
//            // set prompts.xml to alert dialog builder
//            alertDialogBuilder.setView(promptsView);
//
//            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
//
//            // set dialog message
//            alertDialogBuilder
//                    .setCancelable(false)
//                    .setPositiveButton("OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // get user input and set it to result
//                                    // edit text
////                                        result.setText(userInput.getText());
//                                }
//                            })
//                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
//
//            // create alert dialog
//            AlertDialog alertDialog = alertDialogBuilder.create();
//
//            // show it
//            alertDialog.show();
        });
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fabClose = findViewById(R.id.fab_close);
        fabClose.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PostDetailsActivity.class);
            intent.putExtra(SELECTED_POST, selectedPost);
            startActivity(intent);
        });
    }

}
