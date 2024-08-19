package com.peter.foward;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements   GestureOverlayView.OnGesturePerformedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.action_fragment_a:
                FragmentA fragmentA = new FragmentA();//activity
                fragmentTransaction.replace(R.id.fragmentContainer, fragmentA);
                fragmentTransaction.commit();
                return true;

            case R.id.action_fragment_b:
                FragmentB fragmentB = new FragmentB();//people
                fragmentTransaction.replace(R.id.fragmentContainer, fragmentB);
                fragmentTransaction.commit();
                return true;
            case R.id.action_fragment_c:
                FragmentB fragmentC = new FragmentC();//money
                fragmentTransaction.replace(R.id.fragmentContainer, fragmentC);
                fragmentTransaction.commit();
                return true;
            // Handle other menu items here

            default:
                return false;
        }
    }
    private BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list1);

        tableListLayout = findViewById(R.id.tableListLayout);
        databaseHelper = new DatabaseHelper(this);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        // Load initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new FragmentA())
                    .commit();
        }


        GestureOverlayView gestureOverlayView = findViewById(R.id.gestureOverlayView);

        gestureOverlayView.setFadeOffset(0);  // Disable fade out
        //gestureOverlayView.setAlpha(0f);//make invisible

        gestureOverlayView.setGestureStrokeWidth(1f);
        GestureLibrary gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        gestureLibrary.load();

        gestureOverlayView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
                if (!predictions.isEmpty()) {
                    Prediction prediction = predictions.get(0);
                    if (prediction.score > 1.0) {
                        String gestureName = prediction.name;
                        if ("s".equalsIgnoreCase(gestureName)) {
                            g=g+1;
                            if(g>2){
                                g=0;
                                Toast.makeText(MainActivity.this, "G ", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, TablesActivity.class);
                                startActivity(intent);
                            }
                        }

                    }
                }
            }
        });

    }
    private DatabaseHelper databaseHelper;
    private LinearLayout tableListLayout;
    private GestureLibrary gestureLibrary;
    int g=0;
    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);

        // If there's at least one prediction and the highest score prediction is greater than a certain threshold, consider it
        if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
            String gestureName = predictions.get(0).name;
            if ("s".equalsIgnoreCase(gestureName)) {
                Toast.makeText(this, "s drawn", Toast.LENGTH_SHORT).show();
            }
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
