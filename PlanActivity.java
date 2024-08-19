package com.peter.foward;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    EditText startingDateEditText;
    EditText finishingDateEditText;
    AutoCompleteTextView nameEditText;
    AutoCompleteTextView category;
    AutoCompleteTextView subcategory;
    AutoCompleteTextView possibleProfitEditText;
    AutoCompleteTextView possibleProfitEditText1;
    AutoCompleteTextView procedureEditText;
    AutoCompleteTextView initialCapitalEditText;
    AutoCompleteTextView otherResourcesEditText;
    int check=0;
    long primaryKey=-1;
    SparseBooleanArray previousSelectedItems=null;
    SparseBooleanArray previousSelectedItems1=null;
    List<Person> peopleLis;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        dbHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //define items in the activity
        nameEditText = findViewById(R.id.nameEditText);
        startingDateEditText = findViewById(R.id.startingDateEditText);
        finishingDateEditText= findViewById(R.id.finishDateEditText);
        possibleProfitEditText = findViewById(R.id.possibleProfitEditText);
        possibleProfitEditText1 = findViewById(R.id.benefits);
        procedureEditText = findViewById(R.id.procedureEditText);
        initialCapitalEditText = findViewById(R.id.initialCapitalEditText);
        otherResourcesEditText = findViewById(R.id.otherResourcesEditText);
        possibleProfitEditText = findViewById(R.id.possibleProfitEditText);
        category=findViewById(R.id.category);
        subcategory=findViewById(R.id.subcategory);
        View invertedVImageView1 = findViewById(R.id.invertedVImageView1);
        View revealButton1 = findViewById(R.id.revealButton1);
        View invertedVImageView = findViewById(R.id.invertedVImageView);
        View revealButton = findViewById(R.id.revealButton);
        // Set an InputFilter to allow only integer values
        InputFilter integerInputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder filtered = new StringBuilder();

                // Iterate through the input and keep only digits
                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);
                    if (Character.isDigit(currentChar)) {
                        filtered.append(currentChar);
                    }
                }

                // Return the filtered input
                return filtered.toString();
            }
        };

        possibleProfitEditText.setFilters(new InputFilter[]{integerInputFilter});
        initialCapitalEditText.setFilters(new InputFilter[]{integerInputFilter});

        //add suggestions to the user entries from the database
        List<List<String>> suggestions = getAllTableDataColumn(db, "plan");
        List<List<String>> suggestions1 = getAllTableDataColumn(db, "activity_template");
        //List<List<String>> joinedLists = rightJoinLists(suggestions, suggestions1, 2, 1);

        setupAutoCompleteTextView(nameEditText, suggestions1.get(2).stream().distinct().collect(Collectors.toList()));
        setupAutoCompleteTextView(possibleProfitEditText, suggestions.get(6).stream().distinct().collect(Collectors.toList()));
        setupAutoCompleteTextView(initialCapitalEditText, suggestions.get(12).stream().distinct().collect(Collectors.toList()));
        setupAutoCompleteTextView(procedureEditText, suggestions.get(14).stream().distinct().collect(Collectors.toList()));
        setupAutoCompleteTextView(otherResourcesEditText, suggestions.get(13).stream().distinct().collect(Collectors.toList()));
        setupAutoCompleteTextView(category, suggestions1.get(3).stream().distinct().collect(Collectors.toList()));
        setupAutoCompleteTextView(subcategory, suggestions1.get(4).stream().distinct().collect(Collectors.toList()));
        setupAutoCompleteTextView(possibleProfitEditText1, suggestions.get(9).stream().distinct().collect(Collectors.toList()));


        invertedVImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealButton.setVisibility(View.VISIBLE);
                invertedVImageView.setVisibility(View.GONE);

            }
        });

        revealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setVisibility(View.VISIBLE);
                if (check==0)invertedVImageView1.setVisibility(View.VISIBLE);
                check=1;

            }
        });
        invertedVImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealButton1.setVisibility(View.VISIBLE);
                invertedVImageView1.setVisibility(View.GONE);

            }
        });
        revealButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subcategory.setVisibility(View.VISIBLE);

            }
        });

        //call date picker when start date set is called
        startingDateEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickerDialog(startingDateEditText);
                    return true; // Consume the touch event
                }
                return false;
            }
        });
        finishingDateEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickerDialog(finishingDateEditText);
                    return true; // Consume the touch event
                }
                return false;
            }
        });
        procedureEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    dbHelper.showAddProcedureDialog(PlanActivity.this, primaryKey, new DialogCallback() {
                        @Override
                        public void onDialogComplete(long result) {
                            primaryKey=result;
                            procedureEditText.setText(dbHelper.fetchProcedure(primaryKey).getName());
                        }
                    });
                    return true; // Consume the touch event
                }
                return false;
            }
        });
        Button peopleButton = findViewById(R.id.people);

        Button businessButton = findViewById(R.id.businesses);
        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Query the database to retrieve the list of people
                List<Person> peopleList = new ArrayList<>();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + "people", null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String nickname = cursor.getString(cursor.getColumnIndex("type"));

                        Person person = new Person(id, name, nickname);
                        peopleList.add(person);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();

                // Restore previously selected items (if any)

                if (previousSelectedItems == null) {
                    previousSelectedItems = new SparseBooleanArray();
                }

                PeopleAdapter adapter = new PeopleAdapter(PlanActivity.this, peopleList, previousSelectedItems);

                // Create a dialog with a ListView using the custom adapter
                AlertDialog.Builder builder = new AlertDialog.Builder(PlanActivity.this);
                builder.setTitle("Select People");
                builder.setAdapter(adapter, null);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle selected items here
                        SparseBooleanArray selectedItems = adapter.getSelectedItems();
                        previousSelectedItems = selectedItems; // Save selected items for the next dialog
                        for (int i = 0; i < selectedItems.size(); i++) {
                            int position = selectedItems.keyAt(i);
                            if (selectedItems.get(position)) {
                                int selectedId = adapter.getItem(position).getId();
                                associatedPeople.add(Long.valueOf(selectedId));
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();
                ListView listView = dialog.getListView();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Person clickedPerson = adapter.getItem(position);
                        // Create an Intent to start the PersonActivity
                        Intent intent = new Intent(PlanActivity.this, PersonActivity.class);

                        // Pass the primary key to the PersonActivity using extras
                        intent.putExtra("PERSON_ID", clickedPerson.getId());

                        // Start the PersonActivity
                        startActivity(intent);
                    }
                });
                dialog.show();
                peopleLis=peopleList;
            }
        });

        businessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Query the database to retrieve the list of people
                List<Person> peopleList = new ArrayList<>();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + "business", null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("plan_id"));
                        String nickname = cursor.getString(cursor.getColumnIndex("goodsandservices"));

                        Person person = new Person(id, name, nickname);
                        peopleList.add(person);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();

                // Restore previously selected items (if any)

                if (previousSelectedItems1 == null) {
                    previousSelectedItems1 = new SparseBooleanArray();
                }

                PeopleAdapter adapter = new PeopleAdapter(PlanActivity.this, peopleList, previousSelectedItems1);

                // Create a dialog with a ListView using the custom adapter
                AlertDialog.Builder builder = new AlertDialog.Builder(PlanActivity.this);
                builder.setTitle("Select People");
                builder.setAdapter(adapter, null);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle selected items here
                        SparseBooleanArray selectedItems = adapter.getSelectedItems();
                        previousSelectedItems1 = selectedItems; // Save selected items for the next dialog
                        for (int i = 0; i < selectedItems.size(); i++) {
                            int position = selectedItems.keyAt(i);
                            if (selectedItems.get(position)) {
                                int selectedId = adapter.getItem(position).getId();
                                associatedBusiness.add(Long.valueOf(selectedId));
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();
                ListView listView = dialog.getListView();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Person clickedPerson = adapter.getItem(position);
                        // Create an Intent to start the PersonActivity
                        Intent intent = new Intent(PlanActivity.this, BusinessActivity.class);

                        // Pass the primary key to the PersonActivity using extras
                        intent.putExtra("Business_ID", clickedPerson.getId());


                        // Start the PersonActivity
                        startActivity(intent);
                    }
                });
                dialog.show();
                peopleLis=peopleList;
                db.close();
            }
        });
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                String name = nameEditText.getText().toString();
                String categoryy = category.getText().toString();
                String sub_category = subcategory.getText().toString();
                String possibleProfitNonMonetary = possibleProfitEditText1.getText().toString();
                String otherResources = otherResourcesEditText.getText().toString();
                Date startingDateStr=new Date();
                Date finishingDateStr=new Date();
                try {
                    startingDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startingDateEditText.getText().toString());
                    finishingDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(finishingDateEditText.getText().toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                float possibleProfit = 0.0f; // Default value of zero
                float initialCapital = 0.0f;//
                try {
                        possibleProfit = Float.parseFloat( possibleProfitEditText.getText().toString());
                        initialCapital=Float.parseFloat(initialCapitalEditText.getText().toString());

                } catch (NumberFormatException e) {
                        // Handle invalid float input if necessary
                        e.printStackTrace();
                    }
                Long procedureId = primaryKey;
                Log.d("startingDateStr", startingDateEditText.getText().toString());
                long result = insert_plan(db, name,categoryy,sub_category, startingDateStr,finishingDateStr, possibleProfit, possibleProfitNonMonetary,
                        procedureId.toString(),initialCapital, otherResources, associatedPeople,associatedBusiness);

                if (result != -1) {
                    Toast.makeText(PlanActivity.this, "Business plan saved successfully", Toast.LENGTH_SHORT).show();
                    // Clear input fields or perform other actions
                } else {
                    Toast.makeText(PlanActivity.this, "Failed to save business plan", Toast.LENGTH_SHORT).show();
                }

            }
        });
        db.close();
    }



    List<Long> associatedPeople = new ArrayList<>();
    List<Long> associatedBusiness = new ArrayList<>();
    public List<List<String>> getAllTableDataColumn(SQLiteDatabase db, String tableName) {
        List<List<String>> allData = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        if (cursor != null) {
            // Get column names and add them to the first list
            List<String> columnNames = Arrays.asList(cursor.getColumnNames());
            allData.add(columnNames);

            int columnCount = cursor.getColumnCount();

            // Iterate through each column
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                List<String> columnData = new ArrayList<>();

                // Iterate through each row in the column
                while (cursor.moveToNext()) {
                    String value = cursor.getString(columnIndex);
                    columnData.add(value);
                }

                // Reset the cursor position after processing each column
                cursor.moveToFirst();

                // Add the column data to the allData list
                allData.add(columnData);
            }

            cursor.close();
        }

        return allData;
    }
    public List<List<String>> getAllTableDataRaw(SQLiteDatabase db, String tableName) {
        List<List<String>> allData = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        if (cursor != null) {
            // Add column names as the first list
            List<String> columnNames = new ArrayList<>();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                columnNames.add(cursor.getColumnName(i));
            }
            allData.add(columnNames);

            // Add row data
            while (cursor.moveToNext()) {
                List<String> columns = new ArrayList<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    columns.add(cursor.getString(i));
                }
                allData.add(columns);
            }

            cursor.close();
        }

        return allData;
    }
    public List<List<String>> rightJoinLists(List<List<String>> list1, List<List<String>> list2, int index1, int index2) {
        List<List<String>> joinedLists = list1;
        List<String> checker1 = list1.get(index1);
        List<String> checker2 = list2.get(index2);

        list1.size();
        List <Integer> index = new ArrayList<>();
        for (int i = 1; i < checker1.size(); i++){
            index.add(checker2.indexOf(checker1.get(i)));
            Log.d("Join",index.toString());
        }
        for (int i = 1; i < list2.size(); i++) {
            List<String> current = list2.get(i);
            List<String> New=new ArrayList<>();
            New.add("empty");
            for (int j = 1; j < index.size(); j++){
                if(index.get(j)!=-1)New.add(current.get(index.get(j)));
            }
            joinedLists.add(New);
        }


        return joinedLists;
    }



    private void setupAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView, List<String> suggestions) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        autoCompleteTextView.setAdapter(adapter);
    }

    // Method to show DatePickerDialog
    private void showDatePickerDialog(EditText startingDateEdit) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        showTimePickerDialog(calendar.getTime(),startingDateEdit);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Method to show TimePickerDialog
    private void showTimePickerDialog(Date selectedDate,EditText startingDateEdit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, R.style.TimePickerDialogTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String formattedDateTime = dateFormat.format(calendar.getTime());
                        startingDateEdit.setText(formattedDateTime);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public long insertBusinessPlan(SQLiteDatabase db, String name, Date startingDate, float possibleProfit, String procedure,
                                   float initialCapital, String otherResources, List<Long> associatedPeople) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("startingdate", dateFormat.format(startingDate));
        values.put("possibleprofit", possibleProfit);
        values.put("procedure", procedure);
        values.put("initialcapital", initialCapital);
        values.put("otherresources", otherResources);
        values.put("associated_people_ids", TextUtils.join(",", associatedPeople));
        return db.insert("plan", null, values);
    }

    private long insert_plan(SQLiteDatabase db, String name, String categoryy, String sub_category,
                             Date startingDateStr, Date finishingDateStr, float possibleProfit,
                             String possibleProfitNonMonetary, String Procedure, float initialCapital,
                             String otherResources, List<Long> associatedPeople, List<Long> associatedBusiness) {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        values1.put("name", name);
        values1.put("category", categoryy);
        values1.put("subcategory", sub_category);
        long p;

        String[] projection = { "id" };
        String selection = "name = ? AND category = ? AND subcategory = ?";
        String[] selectionArgs = { String.valueOf(name), String.valueOf(categoryy), String.valueOf(sub_category) };
        Cursor cursor = db.query("activity_template", projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            p= cursor.getLong(cursor.getColumnIndex("id"));
        } else {
            p = db.insert("activity_template", null, values1);
        }

        cursor.close();



        values.put("dateplannedstart", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startingDateStr));
        values.put("dateplannedend", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(finishingDateStr));
        values.put("anticipatedreturns", possibleProfit);
        values.put("anticipatedbenefits", possibleProfitNonMonetary);
        values.put("associatedpeople_ids", TextUtils.join(",", associatedPeople));
        values.put("associatedbusiness_ids", TextUtils.join(",", associatedBusiness));
        values.put("cost", initialCapital);
        values.put("otherresources",otherResources);
        values.put("procedure", Procedure);
        values.put("address", p);
        if(p!=-1){
            p=db.insert("plan", null, values);
        }
        return p;
    }
   }



