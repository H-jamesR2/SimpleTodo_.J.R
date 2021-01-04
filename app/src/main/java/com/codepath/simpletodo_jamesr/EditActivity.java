package com.codepath.simpletodo_jamesr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText et_Item;
    Button btn_Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        et_Item = findViewById(R.id.et_item);
        btn_Save = findViewById(R.id.btn_save);

        getSupportActionBar().setTitle("Edit item");

        et_Item.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // when the user is done editing, they click the save button
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent which will contain the results
                Intent intent_fin = new Intent();

                // Pass the data (results of editing)
                intent_fin.putExtra(MainActivity.KEY_ITEM_TEXT, et_Item.getText().toString());
                intent_fin.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                // Set the result of the intent
                setResult(RESULT_OK, intent_fin);
                // Finish activity, close the screen and go back
                finish();
            }
        });
    }
}
