package com.example.tara.oughttado;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
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
    private TodoAdapter todoAdapter;

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
        final Cursor data = mToDoDb.getData();
        Log.d("taart0", "cake");

        todoAdapter = new TodoAdapter(this, data);
        Log.d("taart2", "cake");

        listView.setAdapter(todoAdapter);

        listView.setLongClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                task = adapterView.getItemAtPosition(i).toString();
                CheckedTextView checkbox = view.findViewById(R.id.funnycheckbox);
                if (checkbox.isChecked()){
                    int newCheckbox = 0;
                    checkbox.setChecked(false);
                    mToDoDb.update(newCheckbox, l);

                    populateListView();
                    toastMsg("task is unchecked");
                } else {
                    int newCheckbox = 1;
                    checkbox.setChecked(true);
                    mToDoDb.update(newCheckbox, l);

                    populateListView();
                    toastMsg("task is checked");
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, final long l) {
                Log.d(TAG, "onItemLongClick: You Clicked on " + task);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                task = adapterView.getItemAtPosition(i).toString();

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click Yes to Delete task")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, delete and close
                                // current activity
                                mToDoDb.delete(l);
                                populateListView();
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
                return true;
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
        boolean insertData = mToDoDb.addData(newEntry, false);
        if (insertData) {
            toastMsg("Task added!");
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