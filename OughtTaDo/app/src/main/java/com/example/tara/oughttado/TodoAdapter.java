package com.example.tara.oughttado;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ResourceCursorAdapter;

/**
 * Created by Tara on 24/11/2017.
 */

public class TodoAdapter extends ResourceCursorAdapter{
    public TodoAdapter(Context context, Cursor cursor){
        super(context, R.layout.rowlayout, cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("taart", "cake");
        int completed = cursor.getInt(cursor.getColumnIndex("completed"));
        String title = cursor.getString(cursor.getColumnIndex("title"));
//        int ID = cursor.getInt(cursor.getColumnIndex("_id"));

        CheckedTextView row = view.findViewById(R.id.funnycheckbox);
        row.setText(title);
//        row.setId(ID);

        if (completed == 1) {
            row.setChecked(true);
        } else {
            row.setChecked(false);
        }
    }
}
