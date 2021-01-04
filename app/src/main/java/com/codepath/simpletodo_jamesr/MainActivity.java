package com.codepath.simpletodo_jamesr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

//Source_code for application:
public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btn_add;
    EditText et_Item;
    RecyclerView rv_Items;
    ItemsAdapter items_Adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        et_Item = findViewById(R.id.et_item);
        rv_Items = findViewById(R.id.rv_items);


        loadItems();
        //Mock Data:
        //items.add("Buy milk");
        //items.add("Go to the gym");
        //items.add("Have fun!");

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter
                items_Adapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position" + position);
                // Create the new activity
                Intent intent_1 = new Intent(MainActivity.this, EditActivity.class);
                // Pass the data being edited
                intent_1.putExtra(KEY_ITEM_TEXT, items.get(position));
                intent_1.putExtra(KEY_ITEM_POSITION, position);
                // Display the activity
                startActivityForResult(intent_1, EDIT_TEXT_CODE);
            }
        };
        items_Adapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rv_Items.setAdapter(items_Adapter);
        rv_Items.setLayoutManager(new LinearLayoutManager(this));

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = et_Item.getText().toString();
                // Add item to the model
                items.add(todoItem);
                // Notify adapter that an item is inserting
                items_Adapter.notifyItemInserted(items.size() - 1);
                et_Item.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // handle result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update the model at the right position w/ new item text
            items.set(position, itemText);
            // notify the adapter
            items_Adapter.notifyItemChanged(position);
            // persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
       return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of the data file
    private void loadItems() {

        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // This function saves items by writing them into the data file..
    // Called when adding/removing items
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}
    /** Recycler View: need to work w/:
        RecyclerView.Adapter - to handle data collection and bind to view
        LayoutManager - Helps in positioning items
        ItemAnimator - Helps w/ animating items for common operations
            such as 'Addition' or 'Removal' of Item
        //--> intent to open url (at a browser), or intent to open up a camero
        //-> intents core part of android system...
        //- Intent i = new Intent("context", "destination");
     */

