package com.example.tara.oughttado;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText editText;
    private ListView listView;
    private ToDoDatabase mToDoDb;
    final Context context = this;
    private String task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        mToDoDb = ToDoDatabase.getInstance(getApplicationContext());

        // elements of activity
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        // get data and append it to the list
        Cursor data = mToDoDb.getData();

        ArrayList<String> listData = new ArrayList<>();
        // iterate through rows with moveToNext
        while(data.moveToNext()) {

            // get value from data at column 1, titles
            listData.add(data.getString(1));
        }
        // create list adapter and set adapter
        ListAdapter mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                task = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + task);

                Cursor data = mToDoDb.getItemID(task);
                int itemID = -1;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);
                }
                if (itemID > -1) {
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set title
                    alertDialogBuilder.setTitle("Delete");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click Yes to delete this task!")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, delete and close
                                    // current activity
                                    mToDoDb.delete(task);
                                    populateListView();
                                    toastMsg(task + " deleted!");
                                }
                            })
                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
                else {
                    toastMsg("No ID associated with that name");
                }
            }
        });
    }

    public void ClickedAdd(View view) {
        String newEntry = editText.getText().toString();

        // Check if null
        if (editText.length() > 0) {
            AddData(newEntry);

            // return to empty
            editText.setText("");
            populateListView();
        } else {
            toastMsg("Please write a possible task along the line");
        }
    }

    public void AddData(String newEntry) {
        boolean insertData = mToDoDb.addData(newEntry);
        if (insertData) {
            toastMsg("Data Successfully Inserted");
        } else {
            toastMsg("Oops! Something's wrong");
        }
    }


    /*
    Customizable toast method
     */
    private void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}