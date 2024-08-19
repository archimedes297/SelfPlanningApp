package com.peter.foward;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.N)
public class TablesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private LinearLayout tableListLayout;
    int g=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);

        tableListLayout = findViewById(R.id.tableListLayout);
        databaseHelper = new DatabaseHelper(this);

        List<String> tableNames = getTableNamesFromDatabase();
        for (String tableName : tableNames) {
            addTableDataToLayout(tableName);
        }



    }

    private List<String> getTableNamesFromDatabase() {
        List<String> tableNames = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        while (cursor.moveToNext()) {
            String tableName = cursor.getString(cursor.getColumnIndex("name"));
            tableNames.add(tableName);
        }

        cursor.close();
        return tableNames;
    }


    private void addTableDataToLayout(String tableName) {
        // Create and add the title
        TextView tableTitle = new TextView(this);
        tableTitle.setText(tableName);
        tableTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Set a bigger font size for the title
        tableTitle.setGravity(Gravity.CENTER); // Center the text in the title
        tableListLayout.addView(tableTitle);

        HorizontalScrollView scrollView = new HorizontalScrollView(this);
        TableLayout tableLayout = new TableLayout(this);

        // Fetch column names from the database
        List<String> columnNames = getColumnNamesForTable(tableName);

        // Create header row with column names
        TableRow headerRow = new TableRow(this);
        for (String columnName : columnNames) {
            TextView columnHeader = new TextView(this);
            columnHeader.setText(columnName);
            headerRow.addView(columnHeader);
        }
        tableLayout.addView(headerRow);

        // Fetch data from the database
        Cursor cursor = getDataForTable(tableName);
        while (cursor.moveToNext()) {
            TableRow dataRow = new TableRow(this);
            for (int i = 0; i < columnNames.size(); i++) {
                TextView dataCell = new TextView(this);
                dataCell.setText(cursor.getString(i));
                dataRow.addView(dataCell);
            }
            tableLayout.addView(dataRow);
        }
        cursor.close();

        scrollView.addView(tableLayout);
        tableListLayout.addView(scrollView);
    }

    private List<String> getColumnNamesForTable(String tableName) {
        List<String> columnNames = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);

        while (cursor.moveToNext()) {
            String columnName = cursor.getString(cursor.getColumnIndex("name"));
            columnNames.add(columnName);
        }

        cursor.close();
        return columnNames;
    }


    private Cursor getDataForTable(String tableName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        return db.query(tableName, null, null, null, null, null, null);
    }

}

