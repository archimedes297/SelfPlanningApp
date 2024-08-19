package com.peter.foward;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;



public class ActivityDialogFragment extends DialogFragment {
    private DialogDismissListener listener;

    public void setDialogDismissListener(DialogDismissListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDialogDismissed();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(requireContext());
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogStyle);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_start, container, false);
        started=false;
        // Initialize UI elements here, e.g., nameEditText, startingDateEditText, etc.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //define items in the activity
        nameEditText = view.findViewById(R.id.nameEditText);
        startingDateEditText = view.findViewById(R.id.startingDateEditText);
        finishingDateEditText= view.findViewById(R.id.finishDateEditText);
        possibleProfitEditText = view.findViewById(R.id.possibleProfitEditText);
        possibleProfitEditText1 = view.findViewById(R.id.benefits);
        procedureEditText = view.findViewById(R.id.procedureEditText);
        initialCapitalEditText = view.findViewById(R.id.initialCapitalEditText);
        otherResourcesEditText = view.findViewById(R.id.otherResourcesEditText);
        category=view.findViewById(R.id.category);
        subcategory=view.findViewById(R.id.subcategory);
        View invertedVImageView1 = view.findViewById(R.id.invertedVImageView1);
        View revealButton1 = view.findViewById(R.id.revealButton1);
        View invertedVImageView = view.findViewById(R.id.invertedVImageView);
        View revealButton = view.findViewById(R.id.revealButton);
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
                    //primaryKey=Integer.parseInt("1");
                    dbHelper.showAddProcedureDialog(getContext(), primaryKey, new DialogCallback() {
                        @Override
                        public void onDialogComplete(long result) {
                            Log.d("AprimaryKey", String.valueOf(primaryKey));
                            primaryKey=result;
                            procedureEditText.setText(dbHelper.fetchProcedure(primaryKey).getName());
                        }
                    });
                    return true; // Consume the touch event
                }
                return false;
            }
        });
        Button peopleButton = view.findViewById(R.id.people);

        Button businessButton = view.findViewById(R.id.businesses);
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

                PeopleAdapter adapter = new PeopleAdapter(getContext(), peopleList, previousSelectedItems);

                // Create a dialog with a ListView using the custom adapter
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                associatedPeople.add(Long.valueOf(selectedId-1));
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
                        Intent intent = new Intent(getContext(), PersonActivity.class);

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

                PeopleAdapter adapter1 = new PeopleAdapter(getContext(), peopleList, previousSelectedItems1);

                // Create a dialog with a ListView using the custom adapter
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select People");
                builder.setAdapter(adapter1, null);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle selected items here
                        SparseBooleanArray selectedItems = adapter1.getSelectedItems();
                        previousSelectedItems1 = selectedItems; // Save selected items for the next dialog
                        for (int i = 0; i < selectedItems.size(); i++) {
                            int position = selectedItems.keyAt(i);
                            if (selectedItems.get(position)) {
                                int selectedId = adapter1.getItem(position).getId();
                                associatedBusiness.add(Long.valueOf(selectedId-1));
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

                        Person clickedPerson = adapter1.getItem(position);
                        // Create an Intent to start the PersonActivity
                        Intent intent = new Intent(getContext(), BusinessActivity.class);

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
        // Query the database to retrieve the list of people
        List<Activity> activityList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + "completed_activities", null);
        Cursor cursor1;
        Cursor cursor2;
        /*
        if (cursor.moveToFirst()) {
            do {
                int id_completed=cursor.getInt(cursor.getColumnIndex("id"));;
                Date DateStarted= null;
                Date DateCompleted=null;
                Float Returns= Float.valueOf(cursor.getInt(cursor.getColumnIndex("returns")));;
                String Benefits=cursor.getString(cursor.getColumnIndex("benefits"));;
                String AssociatedPeople=cursor.getString(cursor.getColumnIndex("associatedpeople_ids"));
                String AssociatedBusiness=cursor.getString(cursor.getColumnIndex("associatedbusiness_id"));
                Float Cost=Float.valueOf(cursor.getInt(cursor.getColumnIndex("cost")));;;
                String OtherResources=cursor.getString(cursor.getColumnIndex("otherresources"));;


                String[] planId=new String[] {cursor.getString(cursor.getColumnIndex("planId"))};
                cursor1 = db.rawQuery("SELECT * FROM " + "plan WHERE id = ?", planId);
                cursor1.moveToFirst();
                String Procedure=cursor1.getString(cursor1.getColumnIndex("procedure"));
                Long idPlan= Long.valueOf(cursor1.getInt(cursor1.getColumnIndex("id")));
                Date DatePlannedStart=null;
                Date DatePlannedEnd=null;
                try {
                    DateCompleted=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(cursor.getString(cursor.getColumnIndex("datecompleted")));
                    DateStarted = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(cursor.getString(cursor.getColumnIndex("datestarted")));
                    DatePlannedStart= new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(cursor1.getString(cursor1.getColumnIndex("dateplannedstart")));
                    DatePlannedEnd= new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(cursor1.getString(cursor1.getColumnIndex("dateplannedend")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Float AnticipatedReturns= Float.valueOf(cursor1.getInt(cursor1.getColumnIndex("anticipatedreturns")));
                String AnticipatedBenefits=cursor1.getString(cursor1.getColumnIndex("anticipatedbenefits"));
                Float AnticipatedCost=Float.valueOf(cursor1.getInt(cursor1.getColumnIndex("cost")));;

                String[] templateId=new String[] {cursor.getString(cursor.getColumnIndex("address"))};
                cursor2 = db.rawQuery("SELECT * FROM " + "activity_template WHERE id = ?", templateId);
                cursor2.moveToFirst();
                Log.d("Ids", cursor.getString(cursor.getColumnIndex("address")));
                long id=Long.valueOf(cursor2.getInt(cursor2.getColumnIndex("id")));;
                String name=cursor2.getString(cursor2.getColumnIndex("name"));
                String category=cursor2.getString(cursor2.getColumnIndex("category"));
                String subcategory=cursor2.getString(cursor2.getColumnIndex("subcategory"));
                String SuccessIndexList=cursor2.getString(cursor2.getColumnIndex("successindexlist"));
                Float AvgTimeTaken=cursor2.getFloat(cursor2.getColumnIndex("avgtimetaken"));
                Float TimeChange=cursor2.getFloat(cursor2.getColumnIndex("timechange"));
                Float AvgReturnPerMonth=cursor2.getFloat(cursor2.getColumnIndex("avgreturnpermonth"));;


                Activity activity = new Activity(id_completed,  DateStarted,  DateCompleted,  Returns,
                        Benefits,  AssociatedPeople,  AssociatedBusiness,  Cost,
                        OtherResources,  Procedure,  idPlan,  DatePlannedStart,
                        DatePlannedEnd,AnticipatedCost,  AnticipatedReturns,  AnticipatedBenefits,
                        id,  name,  category,  subcategory,  AvgReturnPerMonth,
                        SuccessIndexList,  AvgTimeTaken,  TimeChange);
                activityList.add(activity);
                cursor1.close();
                cursor2.close();
            } while (cursor.moveToNext());
        }
        cursor.close();*/

        int iteration=0;
        Cursor cursor3 = db.rawQuery("SELECT * FROM plan WHERE completed IS NULL", null);

        if (cursor3.moveToFirst()) {
            do {
                int id_completed=cursor3.getInt(cursor3.getColumnIndex("id"));;
                Date DateStarted= null;
                Date DateCompleted=null;
                Float Returns=0.0f;
                String Benefits="No Benefits Yet";
                String AssociatedPeople=cursor3.getString(cursor3.getColumnIndex("associatedpeople_ids"));
                String AssociatedBusiness=cursor3.getString(cursor3.getColumnIndex("associatedbusiness_ids"));
                Float Cost=0.0f;
                String OtherResources="Not Yet Known";


                cursor1 = db.rawQuery("SELECT * FROM " + "plan WHERE id = ?", new String[] {String.valueOf(id_completed)});
                cursor1.moveToFirst();
                String Procedure=cursor1.getString(cursor1.getColumnIndex("procedure"));
                Long idPlan= Long.valueOf(cursor1.getInt(cursor1.getColumnIndex("id")));
                Date DatePlannedStart=null;
                Date DatePlannedEnd=null;
                try {
                    DateCompleted=new Date();
                    DateStarted = new Date();
                    DatePlannedStart= new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(cursor1.getString(cursor1.getColumnIndex("dateplannedstart")));
                    DatePlannedEnd= new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(cursor1.getString(cursor1.getColumnIndex("dateplannedend")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Float AnticipatedReturns= Float.valueOf(cursor1.getInt(cursor1.getColumnIndex("anticipatedreturns")));
                String AnticipatedBenefits=cursor1.getString(cursor1.getColumnIndex("anticipatedbenefits"));
                Float AnticipatedCost=Float.valueOf(cursor1.getInt(cursor1.getColumnIndex("cost")));;

                String[] templateId=new String[] {cursor3.getString(cursor3.getColumnIndex("address"))};
                cursor2 = db.rawQuery("SELECT * FROM " + "activity_template WHERE id = ?", templateId);
                cursor2.moveToFirst();

                Log.d("Ids", String.valueOf(iteration)+" : "+cursor3.getString(cursor3.getColumnIndex("address")));
                iteration=iteration+1;
                long id=Long.valueOf(cursor2.getInt(cursor2.getColumnIndex("id")));;
                String name=cursor2.getString(cursor2.getColumnIndex("name"));
                String category=cursor2.getString(cursor2.getColumnIndex("category"));
                String subcategory=cursor2.getString(cursor2.getColumnIndex("subcategory"));
                String SuccessIndexList=cursor2.getString(cursor2.getColumnIndex("successindexlist"));
                Float AvgTimeTaken=cursor2.getFloat(cursor2.getColumnIndex("avgtimetaken"));
                Float TimeChange=cursor2.getFloat(cursor2.getColumnIndex("timechange"));
                Float AvgReturnPerMonth=cursor2.getFloat(cursor2.getColumnIndex("avgreturnpermonth"));;


                Activity activity = new Activity(-1,  DateStarted,  DateCompleted,  Returns,
                        Benefits,  AssociatedPeople,  AssociatedBusiness,  Cost,
                        OtherResources,  Procedure,  idPlan,  DatePlannedStart,
                        DatePlannedEnd,AnticipatedCost,  AnticipatedReturns,  AnticipatedBenefits,
                        id,  name,  category,  subcategory,  AvgReturnPerMonth,
                        SuccessIndexList,  AvgTimeTaken,  TimeChange);
                activityList.add(activity);
                cursor1.close();
                cursor2.close();
            } while (cursor3.moveToNext());
        }
        cursor3.close();

        //get plan
        //activityList.get(1).getDatePlannedStart();
        Activity closeActivity = findClosestActivity(activityList);
        Switch switch1 = view.findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked & closeActivity!=null) {
                    {
                        nameEditText.setText(closeActivity.getName());
                        startingDateEditText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(closeActivity.getDatePlannedStart()));
                        finishingDateEditText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(closeActivity.getDatePlannedEnd()));
                        possibleProfitEditText.setText(closeActivity.getAnticipatedReturns().toString());
                        possibleProfitEditText1 .setText(closeActivity.getAnticipatedBenefits());
                        procedureEditText .setText(closeActivity.getProcedure());
                        primaryKey= Long.parseLong(closeActivity.getProcedure());
                        initialCapitalEditText .setText(closeActivity.getAnticipatedCost().toString());
                        otherResourcesEditText .setText(closeActivity.getAnticipatedBenefits());
                        category.setText(closeActivity.getCategory());
                        subcategory.setText(closeActivity.getSubcategory());

                        previousSelectedItems=sba(closeActivity.getAssociatedPeople());
                        previousSelectedItems1=sba(closeActivity.getAssociatedBusiness());


                    }

                } else {

                }
            }
        });


        //

        Button saveButto = view.findViewById(R.id.saveButton);
        saveButto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ractivity=closeActivity;
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
                String associatedPeoples=TextUtils.join(",", associatedPeople);
                if(associatedPeoples.equals(""))associatedPeoples=Ractivity.getAssociatedPeople();
                String associatedBusinesss=TextUtils.join(",", associatedBusiness);
                if(associatedBusinesss.equals(""))associatedBusinesss=Ractivity.getAssociatedBusiness();

                long result = insert_plan(db, name,categoryy,sub_category, startingDateStr,finishingDateStr, possibleProfit, possibleProfitNonMonetary,
                        procedureId.toString(),initialCapital, otherResources, associatedPeoples,associatedBusinesss,Ractivity.getIdPlan());
                Ractivity.setId_completed((int) result);
                if (result != -1) {
                    Toast.makeText(getContext(), "Business plan saved successfully", Toast.LENGTH_SHORT).show();
                    // Clear input fields or perform other actions
                } else {
                    Toast.makeText(getContext(), "Failed to save business plan", Toast.LENGTH_SHORT).show();
                }
                started=true;
                dismiss();

            }
        });
        db.close();
        // Set up listeners and logic for UI elements



        // Other UI setup and logic specific to the dialog
        // Set new width and height for the view (in pixels)

        return view;
    }

    private SparseBooleanArray sba(String str) {
        if(str.equals(""))return new SparseBooleanArray();
        // create a new SparseBooleanArray
        SparseBooleanArray sba = new SparseBooleanArray();

        // split the string by comma and convert each substring into an int
        String[] arr = str.split(","); // arr is ["1", "4", "7"]

        // loop through the array and put each int as a key and true as a value into the SparseBooleanArray
        for (String s : arr) {
            int key = Integer.parseInt(s); // convert the string to an int
            sba.put(key, true); // put the key and true into the SparseBooleanArray
        }
        return sba;
    }

    public static Activity findClosestActivity(List<Activity> activityList) {
        if(activityList==null)return null;
        Activity closestActivity = null;
        Date currentDate = new Date(); // Get the current date and time

        // Initialize a variable to store the time difference
        long closestTimeDifference = Long.MAX_VALUE;

        for (Activity activity : activityList) if(activity.getDatePlannedStart()!=null){
            Date plannedStartDate = activity.getDatePlannedStart();

            // Calculate the time difference between the current date and planned start date
            long timeDifference = Math.abs(plannedStartDate.getTime() - currentDate.getTime());

            // Check if this activity has a closer planned start date
            if (timeDifference < closestTimeDifference) {
                closestTimeDifference = timeDifference;
                closestActivity = activity;
            }
        }

        return closestActivity;
    }

    // Method to order activities by planned start date (closest to furthest)
    public static void orderActivitiesByPlannedStartDate(List<Activity> activityList) {
        Collections.sort(activityList, new Comparator<Activity>() {
            @Override
            public int compare(Activity activity1, Activity activity2) {
                Date date1 = activity1.getDatePlannedStart();
                Date date2 = activity2.getDatePlannedStart();
                return date1.compareTo(date2);
            }
        });
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        super.onResume();
    }

    private DatabaseHelper dbHelper;
    public boolean started=false;
    public Activity Ractivity;
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions);
        autoCompleteTextView.setAdapter(adapter);
    }

    // Method to show DatePickerDialog
    private void showDatePickerDialog(EditText startingDateEdit) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), R.style.DatePickerDialogTheme,
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
    private void showTimePickerDialog(Date selectedDate, EditText startingDateEdit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(), R.style.TimePickerDialogTheme,
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
                             String otherResources, String associatedPeople, String associatedBusiness, Long idPlan) {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        values1.put("name", String.valueOf(startingDateStr));
        values1.put("category", String.valueOf(startingDateStr));
        values1.put("subcategory", String.valueOf(startingDateStr));
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



        values.put("datestarted", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        //values.put("datecompleted", String.valueOf(finishingDateStr));
        //values.put("returns", possibleProfit);
        //values.put("benefits", possibleProfitNonMonetary);
        values.put("associatedpeople_ids",  associatedPeople);
        values.put("associatedbusiness_id",  associatedBusiness);
        values.put("cost", initialCapital);
        values.put("otherresources",otherResources);
        values.put("procedure", Procedure);
        values.put("address", p);
        values.put("planId", idPlan);
        if(p!=-1){
            p=db.insert("completed_activities", null, values);
            ContentValues values2=new ContentValues();
            values2.put("completed",p);
            db.update("plan",values2,"id=?",new String[]{String.valueOf(Ractivity.getIdPlan())});
        }
        return p;
    }
}
