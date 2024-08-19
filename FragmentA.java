package com.peter.foward;
import android.app.AlertDialog;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class FragmentA extends Fragment implements DialogDismissListener{

    private Context context;
    private DatabaseHelper databaseHelper;
    Button todayButton;
    TextView adviceTV;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        // Logo at top left corner
        // You can add an ImageView for the logo here

        // Plan Button
        Button planButton = view.findViewById(R.id.PlanButton);
        planButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Plan activity
                Intent intent = new Intent(context, PlanActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Add this flag
                startActivity(intent);
            }
        });

        // Today Button
        todayButton = view.findViewById(R.id.todayButton);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate and show the popup window
                showPopupWindow();
            }
        });

        //
        planSeekBar = view.findViewById(R.id.planSeekBar);
        startFinishButton = view.findViewById(R.id.startFinishButton);
        currentStepTextView = view.findViewById(R.id.currentStep);
        handler = new Handler();
        String planName = "Sample Plan";
        String[] steps = {"Step 1", "Step 2", "Step 3", "Step 4"};
        long startTime = System.currentTimeMillis();
        long finishTime = startTime + 6000; // Adding 1 minute for demonstration
        startFinishButton.setOnClickListener(v -> {
            if (!isStarted) {
                start(getChildFragmentManager());//pop(getContext());
            } else {
                finisher();
            }});

        //


        adviceTV=view.findViewById(R.id.advice);
        changeAdvice();
        TextView time=view.findViewById(R.id.timeHealth);
        TextView plan=view.findViewById(R.id.planHealth);
        calculateHealth(time,plan);

        activitiesPlot(view);

        return view;
    }


    private void calculateHealth(TextView time, TextView plan) {
        int timeHealth=0;
        SQLiteDatabase db=databaseHelper.getWritableDatabase();
        // Query to count the number of rows with non-null "completed" values
        String countQuery = "SELECT COUNT(*) FROM plan WHERE completed IS NOT NULL";

        Cursor cursor = db.rawQuery(countQuery, null);
        int totalCount = 0;
        if (cursor.moveToFirst()) {
            totalCount = cursor.getInt(0);
        }
        // Query to count the total number of rows in the table
        countQuery = "SELECT COUNT(*) FROM plan";
        cursor = db.rawQuery(countQuery, null);
        int totalRows = 0;
        if (cursor.moveToFirst()) {
            totalRows = cursor.getInt(0);
        }
        // Calculate the proportion
        double proportion = (double) totalCount / totalRows;
        cursor.close();
        db.close();
        time.setText("");
        plan.setText(String.valueOf(100*databaseHelper.getProportionOfCompletedWithinWeek(2)));

    }

    private void changeAdvice() {
        Random random = new Random();
        String [] advice= new String[]{
                "Do not suffer in your imagination, instead make it colorful and see you dreams come true",
                "What you have to work on most is yourself and everything else falls in line. Be king, see the good in people",
                "If you practice everyday for a long time efficiently and effectively, you are soon among the best and the value of your time exponentially grows",
                "Spend 4hrs with family, 8hrs working, 6hrs with sleeping, 4 hours bathing, eating and grooming, and 2hrs practicing a skill everyday to grow",
                "Delegation and leverage surpass effort, if you can hire someone to do it for you and still make a profit, then do not do it",
                "It is all people business. you have to learn people because your family is people",
                "Talk to people but more importantly listen to them. make them see you as a friend find make them happier and they will buy you",
                "The best sales man is the one who has learnt from every sales conversation and made the most sales conversations",
                "Health before Wealth. You forget this, you loose everything",
                "Just find more money",
                "Its first the Innovator then the imitator then the idiot",
                "Abandom all your ego, Selflessness and truthfulness will lead to harmony",
                "Slow money is better than no money but fast money is best",
                "For your family work harder, take more risks, hate poverty",
                "Do not need things. If you dare need them they'll hide so need nothing",
                "Alone is very good. Value your own company. It all depends on the thought you tolerate",
                "What is life? What are we? Why are we?",
                "Learn from every mistake. Your defeat is as dear as wisdom",
                "Ask why then ask how, when and where. The thinker is still thinking while the doer is almost done"
        };
        int randomIndex = random.nextInt(advice.length); // Generate a random index
        String randomAdvice = advice[randomIndex]; // Get a random advice from the array
        adviceTV.setText(randomAdvice); // Set the random advice to the TextView

        // Schedule the next advice change after 2 minutes (in milliseconds)
        handler.postDelayed(changeAdviceRunnable,  1000);
    }

    private Runnable changeAdviceRunnable= new Runnable() {
        @Override
        public void run() {
            changeAdvice(); // Change the advice text
        }
    };
    String monetaryReturns ;
    String otherBenefits;
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


    private void finisher() {
        Activity ractvity = activityDialogFragment.Ractivity;
        int id=ractvity.getId_completed();
        ContentValues values = new ContentValues();
        values.put("datecompleted", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));

        // Create a LayoutInflater to inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.input_dialog, null);

        // Initialize the input fields
        final EditText editTextMonetaryReturns = dialogView.findViewById(R.id.editTextMonetaryReturns);
        editTextMonetaryReturns.setFilters(new InputFilter[]{integerInputFilter});
        final EditText editTextOtherBenefits = dialogView.findViewById(R.id.editTextOtherBenefits);

        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Returns and Benefits");
        builder.setView(dialogView);

        // Set positive button action
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isStarted = false;
                startFinishButton.setImageResource(android.R.drawable.ic_media_play); // Switch to start icon
                // Retrieve the user input from the EditText fields
                handler1.removeCallbacks(runnable);
                monetaryReturns = editTextMonetaryReturns.getText().toString();
                otherBenefits = editTextOtherBenefits.getText().toString();

                // You can do something with the entered values here
                // For example, you can return them or perform further actions
                // Depending on your needs, you can store them in variables or pass them to another function

                // For now, let's just log the values
                Log.d("UserInput", "Monetary Returns: " + monetaryReturns);
                Log.d("UserInput", "Other Benefits: " + otherBenefits);
                values.put("returns", String.valueOf(monetaryReturns));
                values.put("benefits", otherBenefits);

                SQLiteDatabase db=databaseHelper.getWritableDatabase();
                db.update("completed_activities", values, "id = ?", new String[]{String.valueOf(id)});
            }
        });

        // Set negative button action
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dialog canceled by the user
                dialog.cancel();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    ActivityDialogFragment activityDialogFragment = new ActivityDialogFragment();

    private void start(FragmentManager fragmentManager) {
        activityDialogFragment.setDialogDismissListener(this);
        activityDialogFragment.show(fragmentManager, "plan_dialog");

        //Log.d("steps","null");
    }

    @Override
    public void onDialogDismissed() {
        Activity ractivity = activityDialogFragment.Ractivity;
        boolean started =activityDialogFragment.started;
        Log.d("Started",String.valueOf(started));
        if (started)playSeekBar(ractivity);
    }

    private void playSeekBar(Activity ractivity) {
        isStarted = true;
        startFinishButton.setImageResource(android.R.drawable.ic_media_pause); // Switch to stop icon
        Cursor cursor=databaseHelper.getReadableDatabase().query("Procedure",null,
                "id = ?", new String[]{ractivity.getProcedure()},null,null,null);
        if (cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            String[] columnNames = cursor.getColumnNames();

            for (String columnName : columnNames) {
                int columnIndex = cursor.getColumnIndex(columnName);
                if (columnIndex != -1) {
                    // Retrieve the value from the cursor and put it into ContentValues
                    switch (cursor.getType(columnIndex)) {
                        case Cursor.FIELD_TYPE_NULL:
                            values.putNull(columnName);
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            values.put(columnName, cursor.getInt(columnIndex));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            values.put(columnName, cursor.getFloat(columnIndex));
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            values.put(columnName, cursor.getString(columnIndex));
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            values.put(columnName, cursor.getBlob(columnIndex));
                            break;
                    }
                }
            }
            String steps=values.getAsString("Steps");
            Long period=ractivity.getDatePlannedEnd().getTime()-ractivity.getDatePlannedStart().getTime();
            player(period,steps,planSeekBar,currentStepTextView);
        }
        else{Log.d("steps","null");}

    }
    // Initialize a handler to schedule updates
    Handler handler1 = new Handler();
    Runnable runnable ;

    private void player(Long period, String steps, SeekBar yourSeekBar, TextView yourTextView) {
        // Split the steps string into an array
        String[] stepsArray = steps.split(",");

// Calculate individual step duration
        long stepDuration = period / stepsArray.length;

// Initialize a counter to keep track of the current step
        final int[] currentStep = {0};

        runnable = new Runnable() {
            @Override
            public void run() {
                if(period>0 & currentStep[0] < stepsArray.length) {
                    // Update the SeekBar
                    yourSeekBar.setProgress((int) ((currentStep[0]) * stepDuration * 100 / period));

                    // Update the TextView with the current step
                    yourTextView.setText(stepsArray[currentStep[0]]);

                    // Schedule the next update
                    currentStep[0]++;
                    handler.postDelayed(this, stepDuration);
                } else {
                    // All steps are completed
                    yourTextView.setText("You should be Done!");

                    // Reset the SeekBar
                    yourSeekBar.setProgress(100);
                }
            }
        };
// Start the updates
        handler1.post(runnable);
    }

    private void pop(Context context) {
        // Query the database to retrieve the list of people
        List<Activity> activityList = new ArrayList<>();
        SQLiteOpenHelper dbHelper=new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + "completed_activities", null);
        Cursor cursor1;
        Cursor cursor2;

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
                String Procedure=cursor.getString(cursor.getColumnIndex("procedure"));

                String[] planId=new String[] {cursor.getString(cursor.getColumnIndex("planId"))};
                cursor1 = db.rawQuery("SELECT * FROM " + "plan WHERE id = ?", planId);
                cursor1.moveToFirst();
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
        cursor.close();

        db.close();


        ActivityAdapter adapter = new ActivityAdapter(context, activityList, new SparseBooleanArray());

        // Create a dialog with a ListView using the custom adapter
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select People");
        builder.setAdapter(adapter, null);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle selected items here
                SparseBooleanArray selectedItems = adapter.getSelectedItems();
                for (int i = 0; i < selectedItems.size(); i++) {
                    int position = selectedItems.keyAt(i);
                    if (selectedItems.get(position)) {
                        int selectedId = (int) adapter.getItem(position).getId();
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

                Activity clickedActivity = adapter.getItem(position);
                // Create an Intent to start the PersonActivity
                Intent intent = new Intent(context, PersonActivity.class);

                // Pass the primary key to the PersonActivity using extras
                intent.putExtra("PERSON_ID", clickedActivity.getId());

                // Start the PersonActivity
                startActivity(intent);
            }
        });
        dialog.show();
    }


    private SeekBar planSeekBar;
    private ImageButton startFinishButton;
    private TextView currentStepTextView;
    private Handler handler;
    private Runnable updateRunnable;
    long currentTime;
    boolean isStarted=false;
    int stepIndex;



    private void showPopupWindow() {
        // Create the popup window layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_layout, null);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // Set up the popup window's content
        TableLayout tableLayout = popupView.findViewById(R.id.popupTableLayout);
        List<String> timeList = getTimeList(); // Replace with your logic to get time periods
        List<String> doneActivities = getDoneActivities(); // Replace with your logic to get done activities
        List<String> plannedActivities = getPlannedActivities(); // Replace with your logic to get planned activities

        for (int i = 0; i < timeList.size(); i++) {
            TableRow tableRow = new TableRow(context);

            TextView timeTextView = createCustomTextView(timeList.get(i));
            tableRow.addView(timeTextView);

            TextView doneTextView = createCustomTextView(doneActivities.get(i));
            if (doneActivities.get(i).equals(plannedActivities.get(i))) {
                tableRow.setBackgroundColor(Color.argb(100,0,0,200));
            } else {
                tableRow.setBackgroundColor(Color.RED);
            }
            tableRow.addView(doneTextView);

            TextView plannedTextView = createCustomTextView(plannedActivities.get(i));
            tableRow.addView(plannedTextView);

            tableLayout.addView(tableRow);
        }


        popupWindow.showAsDropDown(todayButton);
    }

    private TextView createCustomTextView(String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        // Padding
        int padding = 10;  // adjust this value as needed
        textView.setPadding(padding, padding, padding, padding);
        // Shadow
        float radius = 1.5f;  // adjust this value as needed
        int shadowColor = Color.BLACK;
        int dx = 2;   // adjust this value as needed
        int dy = 2;   // adjust this value as needed
        textView.setShadowLayer(radius, dx, dy, shadowColor);

        return textView;
    }

    private List<String> getTimeList() {
        List<String> timeList = new ArrayList<>();
        timeList.add("9:00 AM - 10:00 AM");
        timeList.add("10:30 AM - 11:30 AM");
        timeList.add("12:00 PM - 1:00 PM");
        // Add more time periods as needed
        return timeList;
    }

    private List<String> getDoneActivities() {
        List<String> doneActivities = new ArrayList<>();
        doneActivities.add("Meeting with Client");
        doneActivities.add("Lunch Break");
        doneActivities.add("Code Refactoring");
        // Add more done activities as needed
        return doneActivities;
    }

    private List<String> getPlannedActivities() {
        List<String> plannedActivities = new ArrayList<>();
        plannedActivities.add("Meeting with Team");
        plannedActivities.add("Design UI Mockups");
        plannedActivities.add("Code Refactoring");
        // Add more planned activities as needed
        return plannedActivities;
    }
    SQLiteDatabase database;

    public void plot1(View views) {
        database = databaseHelper.getReadableDatabase();
        ConstraintLayout view = views.findViewById(R.id.chart);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        List<String> tables = getTables();
        Map<String, Map<String, Integer>> allData = new HashMap<>();
        for (String table : tables) {
            Map<String, String> columns = getColumns(table);
            for (Map.Entry<String, String> entry : columns.entrySet()) {
                String columnName = entry.getKey();
                String columnType = entry.getValue();
                if ("TEXT".equalsIgnoreCase(columnType)) {
                    Cursor cursor = database.rawQuery("SELECT " + columnName + " FROM " + table, null);
                    if (cursor.moveToFirst()) {
                        Map<String, Integer> data = new HashMap<>();
                        do {
                            String value = cursor.getString(cursor.getColumnIndex(columnName));
                            data.put(value, data.getOrDefault(value, 0) + 1);
                        } while (cursor.moveToNext());
                        allData.put(columnName, data);
                    }
                    cursor.close();
                } else {
                    // Create other types of charts for numerical data
                    // ... (Setup other charts with data)
                    // Here you can create different types of charts like BarChart, LineChart based on column data
                }
            }
        }

        Spinner spinner = new Spinner(getContext());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>(allData.keySet()));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        PieChart pieChart = new PieChart(getContext());
        setupPieChart(pieChart, allData.get(spinner.getSelectedItem().toString()));
        pieChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setupPieChart(pieChart, allData.get(spinner.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        linearLayout.addView(spinner);
        linearLayout.addView(pieChart);
        view.addView(linearLayout);
    }
    public void plot(View views) {
        database = databaseHelper.getReadableDatabase();
        ConstraintLayout view = views.findViewById(R.id.chart);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        List<String> tables = getTables();
        Map<String, Map<String, Integer>> allData = new HashMap<>();
        for (String table : tables) {
            Map<String, String> columns = getColumns(table);
            for (Map.Entry<String, String> entry : columns.entrySet()) {
                String columnName = entry.getKey();
                String columnType = entry.getValue();
                if ("TEXT".equalsIgnoreCase(columnType)) {
                    Cursor cursor = database.rawQuery("SELECT " + columnName + " FROM " + table, null);
                    if (cursor.moveToFirst()) {
                        Map<String, Integer> data = new HashMap<>();
                        do {
                            String value = cursor.getString(cursor.getColumnIndex(columnName));
                            data.put(value, data.getOrDefault(value, 0) + 1);
                        } while (cursor.moveToNext());
                        allData.put(columnName, data);
                    }
                    cursor.close();
                } else {
                    // Create other types of charts for numerical data
                    Cursor cursor = database.rawQuery("SELECT " + columnName + " FROM " + table, null);
                    if (cursor.moveToFirst()) {
                        List<BarEntry> barEntries = new ArrayList<>();
                        List<Entry> lineEntries = new ArrayList<>();
                        int index = 0;
                        do {
                            float value = cursor.getFloat(cursor.getColumnIndex(columnName));
                            barEntries.add(new BarEntry(index, value));
                            lineEntries.add(new Entry(index, value));
                            index++;
                        } while (cursor.moveToNext());

                        // Setup BarChart with barEntries
                        BarDataSet barDataSet = new BarDataSet(barEntries, columnName);
                        //barDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                        BarData barData = new BarData(barDataSet);
                        BarChart barChart = new BarChart(getContext());
                        barChart.setData(barData);
                        barChart.getDescription().setEnabled(false); // Disable the description
                        barChart.invalidate(); // Refresh the chart

                        // Setup LineChart with lineEntries
                        LineDataSet lineDataSet = new LineDataSet(lineEntries, columnName);
                        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        LineData lineData = new LineData(lineDataSet);
                        LineChart lineChart = new LineChart(getContext());
                        lineChart.setData(lineData);
                        lineChart.getDescription().setEnabled(false); // Disable the description
                        lineChart.invalidate(); // Refresh the chart

                        barChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
                        lineChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));


                        // Add the charts to the linear layout
                        linearLayout.addView(barChart);
                        linearLayout.addView(lineChart);
                    }
                    cursor.close();
                }
            }
        }

        Spinner spinner = new Spinner(getContext());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>(allData.keySet()));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        PieChart pieChart = new PieChart(getContext());
        setupPieChart(pieChart, allData.get(spinner.getSelectedItem().toString()));
        pieChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setupPieChart(pieChart, allData.get(spinner.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                // Do nothing when nothing is selected
            }

        });

        linearLayout.addView(spinner);
        linearLayout.addView(pieChart);
        ScrollView scrollView = new ScrollView(getContext());;
        scrollView.addView(linearLayout);
        view.addView(scrollView);
    }

    private void activitiesPlot(View views) {
        database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + "*" + " FROM " + "completed_activities", null);

        ConstraintLayout view = views.findViewById(R.id.chart);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ScrollView scrollView=new ScrollView(getContext());
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        List<Activity> activities = new ArrayList<>();
        int index = 0;
        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity.setReturns(cursor.getFloat(cursor.getColumnIndex("returns")));
                activity.setId(cursor.getLong(cursor.getColumnIndex("address")));

                Cursor cursor1 = database.rawQuery("SELECT * FROM " + "activity_template WHERE id = ?", new String[] {String.valueOf(activity.getId())});
                cursor1.moveToFirst();
                activity.setSubcategory(cursor1.getString(cursor1.getColumnIndex("subcategory")));
                activity.setCategory(cursor1.getString(cursor1.getColumnIndex("category")));
                activity.setName(cursor1.getString(cursor1.getColumnIndex("name")));
                activities.add(activity);

            } while (cursor.moveToNext());
        }
        cursor.close();
        List<String> names = new ArrayList<>(new HashSet<>(activities.stream().map(Activity::getName).collect(Collectors.toList())));
        List<String> categories = new ArrayList<>(new HashSet<>(activities.stream().map(Activity::getCategory).collect(Collectors.toList())));
        List<String> subCategories = new ArrayList<>(new HashSet<>(activities.stream().map(Activity::getSubcategory).collect(Collectors.toList())));

        List<List<BarEntry>> barEntries = new ArrayList<>();
        List<BarEntry> barEntry = new ArrayList<>();
        List<List<String>> at = new ArrayList<>();
        at.add(names);at.add(categories);at.add(subCategories);
        for(List<String> categoriess : at) {
            index = 0;
            for (String category: categoriess){
                float ret=0;
                for(Activity activity : activities ){
                    if(category.equals(activity.getCategory())){
                        ret=ret+activity.getReturns();
                    }
                }
                Log.d(category+"hello",String.valueOf(index)+"  "+String.valueOf(ret));
                barEntry.add(new BarEntry(index,ret));
                index=index+1;

            }
            barEntries.add(barEntry);
        }
        BarDataSet barDataSet = new BarDataSet(barEntry, "categories");
        //barDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        BarData barData = new BarData(barDataSet);
        BarChart barChart = new BarChart(getContext());
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false); // Disable the description
        barChart.getAxisLeft().setDrawGridLines(false); // Disable grid lines for the left axis
        barChart.getAxisRight().setDrawGridLines(false); // Disable grid lines for the right axis
        barChart.getXAxis().setDrawGridLines(false); // Disable grid lines for the X axis
        barChart.getAxisLeft().setDrawLabels(false); // Disable axis labels for the left axis
        barChart.getAxisRight().setDrawLabels(false); // Disable axis labels for the right axis
        barChart.getXAxis().setDrawLabels(false); // Disable axis labels for the X axis
        barChart.getLegend().setEnabled(false); // Disable the legend
        barChart.setDrawBorders(false); // Disable drawing borders
        barChart.setDrawGridBackground(false); // Disable the grid background
        barChart.invalidate();
        barChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.addView(barChart);

    }





    private void setupPieChart1(PieChart pieChart, Map<String, Integer> data) {
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "Category");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();  // Refresh the chart
        Log.d("PieChart Data", "Entries: " + entries.toString());
    }
    private void setupPieChart(PieChart pieChart, Map<String, Integer> data) {
        List<PieEntry> entries = new ArrayList<>();
        int total = 0; // Variable to store the total value of all entries
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
            total += entry.getValue(); // Add the value to the total
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "Category");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart)); // Set the value formatter to show percentages
        pieData.setValueTextSize(12f); // Set the value text size to 12f
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true); // Set the pie chart to use percent values
        pieChart.setDrawEntryLabels(false); // Disable the draw entry labels to avoid overlapping
        pieChart.setCenterText("Total: " + total); // Set the center text to show the total value
        pieChart.setHoleRadius(25f); // Set the hole radius to 25f
        pieChart.setTransparentCircleRadius(30f); // Set the transparent circle radius to 30f
        pieChart.getDescription().setEnabled(false); // Disable the description
        pieChart.invalidate();  // Refresh the chart
        Log.d("PieChart Data", "Entries: " + entries.toString());
    }


    private List<String> getTables() {
        List<String> tables = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (cursor.moveToFirst()) {
            do {
                tables.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tables;
    }

    private Map<String, String> getColumns(String table) {
        Map<String, String> columns = new HashMap<>();
        Cursor cursor = database.rawQuery("PRAGMA table_info(" + table + ")", null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                columns.put(name, type);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return columns;
    }



}
