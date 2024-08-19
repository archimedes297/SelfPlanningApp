package com.peter.foward;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyData.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PEOPLE);
        db.execSQL(CREATE_TABLE_TOPIC_RECORDS);
        db.execSQL(CREATE_TABLE_TOPICS);
        db.execSQL(CREATE_TABLE_MONEY);
        db.execSQL(CREATE_TABLE_EXPENDITURE_TEMPLATE);
        db.execSQL(CREATE_TABLE_EXPENDITURE_RECORDS);
        db.execSQL(CREATE_TABLE_BUSINESS);
        db.execSQL(CREATE_TABLE_BUSINESS_CASH_FLOW);
        db.execSQL(CREATE_TABLE_CASH_FLOW_TEMPLATE);
        db.execSQL(CREATE_TABLE_BUSINESS_PLAN);
        db.execSQL(CREATE_TABLE_COMPLETED_ACTIVITIES);
        db.execSQL(CREATE_TABLE_PLAN);
        db.execSQL(CREATE_TABLE_ACTIVITY_TEMPLATE);
        db.execSQL(CREATE_TABLE_PROCEDURE);
        for(int i = 0; i < 20; i++) {
            insertSampleData(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPIC_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONEY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENDITURE_TEMPLATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENDITURE_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSINESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSINESS_CASH_FLOW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASH_FLOW_TEMPLATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSINESS_PLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLETED_ACTIVITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY_TEMPLATE);
        db.execSQL("DROP TABLE IF EXISTS " + "Procedure");

        onCreate(db);
    }


    private static final String TABLE_PEOPLE = "people";
    private static final String CREATE_TABLE_PEOPLE = "CREATE TABLE " + TABLE_PEOPLE +
            "(id INTEGER PRIMARY KEY, " +
            "name TEXT, " +
            "nickname TEXT, " +
            "type TEXT, " +
            "lastcontact DATETIME, " +
            "contacttime DATETIME, " +
            "topicpast TEXT, " +
            "topicpresent TEXT, " +
            "future TEXT,"+
            "earning REAL)";


    private static final String TABLE_TOPIC_RECORDS = "topic_records";
    private static final String CREATE_TABLE_TOPIC_RECORDS = "CREATE TABLE " + TABLE_TOPIC_RECORDS +
            "(id INTEGER PRIMARY KEY, " +
            "topic_id INTEGER, " +
            "time DATETIME, " +
            "person_ids TEXT, " +
            "result REAL, " +
            "earning REAL)";

    private static final String TABLE_TOPICS = "topics";
    private static final String CREATE_TABLE_TOPICS = "CREATE TABLE " + TABLE_TOPICS +
            "(id INTEGER PRIMARY KEY, " +
            "topic TEXT, " +
            "bringupmethod TEXT, " +
            "factsabout TEXT, " +
            "notes TEXT, " +
            "indexes TEXT, " +
            "avgresult TEXT, " +
            "avgreward REAL)";
    private static final String TABLE_MONEY = "money";
    private static final String TABLE_EXPENDITURE_TEMPLATE = "expenditure_template";
    private static final String TABLE_EXPENDITURE_RECORDS = "expenditure_records";
    private static final String TABLE_BUSINESS = "business";
    private static final String TABLE_BUSINESS_CASH_FLOW = "business_cash_flow";
    private static final String TABLE_CASH_FLOW_TEMPLATE = "cash_flow_template";
    private static final String TABLE_BUSINESS_PLAN = "business_plan";
    private static final String TABLE_COMPLETED_ACTIVITIES = "completed_activities";
    private static final String TABLE_PLAN = "plan";
    private static final String TABLE_ACTIVITY_TEMPLATE = "activity_template";

    private static final String CREATE_TABLE_MONEY = "CREATE TABLE " + TABLE_MONEY +
            "(id INTEGER PRIMARY KEY, " +
            "amount REAL, " +
            "source TEXT, " +
            "category TEXT, " +
            "address INTEGER, " +
            "time DATETIME)";

    private static final String CREATE_TABLE_EXPENDITURE_TEMPLATE = "CREATE TABLE " + TABLE_EXPENDITURE_TEMPLATE +
            "(id INTEGER PRIMARY KEY, " +
            "name TEXT, " +
            "category TEXT, " +
            "avgamountpermonth REAL, " +
            "whennextneeded DATE, " +
            "eliminationstrategy TEXT)";

    private static final String CREATE_TABLE_EXPENDITURE_RECORDS = "CREATE TABLE " + TABLE_EXPENDITURE_RECORDS +
            "(id INTEGER PRIMARY KEY, " +
            "date DATE, " +
            "address INTEGER, " +
            "people_ids TEXT, " +
            "whyeliminationstrategyfailed TEXT)";


    private static final String CREATE_TABLE_BUSINESS = "CREATE TABLE " + TABLE_BUSINESS +
            "(id INTEGER PRIMARY KEY, " +
            "goodsandservices TEXT, " +
            "averageprofitpermonth REAL, " +
            "profitlastmonth REAL, " +
            "whenstarted DATE, " +
            "worth REAL, " +
            "stepstorun TEXT, " +
            "stepstogrow TEXT, " +
            "associatedpeople_ids TEXT, " +
            "projectedprofit REAL, " +
            "plan_id INTEGER)";


    private static final String CREATE_TABLE_BUSINESS_CASH_FLOW = "CREATE TABLE " + TABLE_BUSINESS_CASH_FLOW +
            "(id INTEGER PRIMARY KEY, " +
            "address INTEGER, " +
            "business_id INTEGER, " +
            "amounteach REAL, " +
            "count REAL, " +
            "amountksh REAL, " +
            "date DATETIME, " +
            "quality TEXT)";
    private static final String CREATE_TABLE_CASH_FLOW_TEMPLATE = "CREATE TABLE " + TABLE_CASH_FLOW_TEMPLATE +
            "(id INTEGER PRIMARY KEY, " +
            "name TEXT, " +
            "averageamounteach REAL, " +
            "averagecount REAL, " +
            "averageamount REAL, " +
            "improvestrategy TEXT, " +
            "reductionstrategy TEXT)";

    private static final String CREATE_TABLE_BUSINESS_PLAN = "CREATE TABLE " + TABLE_BUSINESS_PLAN +
            "(id INTEGER PRIMARY KEY, " +
            "name TEXT, " +
            "startingdate DATE, " +
            "possibleprofit REAL, " +
            "procedure TEXT, " +
            "initialcapital REAL, " +
            "otherresources TEXT, " +
            "associated_people_ids TEXT)";

    private static final String CREATE_TABLE_COMPLETED_ACTIVITIES = "CREATE TABLE " + TABLE_COMPLETED_ACTIVITIES +
            "(id INTEGER PRIMARY KEY, " +
            "address INTEGER, " +
            "datestarted DATETIME, " +
            "datecompleted DATETIME, " +
            "returns REAL, " +
            "benefits TEXT, " +
            "associatedpeople_ids TEXT, " +
            "associatedbusiness_id INTEGER, " +
            "cost REAL, " +
            "otherresources TEXT, " +
            "procedure TEXT," +
            "planId INTEGER)";

    private static final String CREATE_TABLE_PLAN = "CREATE TABLE " + TABLE_PLAN +
            "(id INTEGER PRIMARY KEY, " +
            "address INTEGER, " +
            "datestarted DATETIME, " +
            "dateplannedstart DATETIME, " +
            "dateplannedend DATETIME, " +
            "anticipatedreturns REAL, " +
            "returns REAL, " +
            "realisedbenefits TEXT, " +
            "anticipatedbenefits TEXT, " +
            "associatedpeople_ids TEXT, " +
            "associatedbusiness_ids TEXT, " +
            "cost REAL, " +
            "otherresources TEXT, " +
            "procedure TEXT," +
            "completed INTEGER)";

    private static final String CREATE_TABLE_ACTIVITY_TEMPLATE = "CREATE TABLE " + TABLE_ACTIVITY_TEMPLATE +
            "(id INTEGER PRIMARY KEY, " +
            "name TEXT, " +
            "category TEXT, " +
            "subcategory TEXT, " +
            "avgreturnpermonth REAL, " +
            "successindexlist TEXT, " +
            "avgtimetaken REAL, " +
            "timechange REAL)";
    private static final String CREATE_TABLE_PROCEDURE = "CREATE TABLE " + "Procedure" +
            "(id INTEGER PRIMARY KEY, " +
            "name TEXT, " +
            "Steps TEXT)";
    public long insertProcedure(String name, String steps) {

        String TABLE_PROCEDURE = "Procedure";
        String COLUMN_NAME = "name";
        String COLUMN_STEPS = "Steps";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_STEPS, steps);
        return db.insert(TABLE_PROCEDURE, null, values);
    }
    public Procedure fetchProcedure(long primaryKey) {
        SQLiteDatabase db = this.getReadableDatabase();
        Procedure procedure = null;
        String TABLE_PROCEDURE = "Procedure";
        String COLUMN_NAME = "name";
        String COLUMN_STEPS = "Steps";
        String COLUMN_ID = "id";

        Cursor cursor = db.query(
                TABLE_PROCEDURE,
                new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_STEPS },
                COLUMN_ID + " = ?",
                new String[] { String.valueOf(primaryKey) },
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String steps = cursor.getString(cursor.getColumnIndex(COLUMN_STEPS));

            procedure = new Procedure();
            procedure.setId(id);
            procedure.setName(name);
            procedure.setSteps(steps);

            cursor.close();
        }

        db.close();
        return procedure;
    }
    public void updateProcedure(long primaryKey, String name, String steps) {
        String TABLE_PROCEDURE = "Procedure";
        String COLUMN_NAME = "name";
        String COLUMN_STEPS = "Steps";
        String COLUMN_ID = "id";
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_STEPS, steps);

        db.update(TABLE_PROCEDURE, values, COLUMN_ID + " = ?", new String[]{String.valueOf(primaryKey)});

        db.close();
    }
    long primaryKe=-1;
    public long showAddProcedureDialog(final Context context, final long primaryKey,final DialogCallback callback) {
        Log.d( "primaryStart", primaryKey +"AAND"+primaryKe);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_procedure, null);
        dialogBuilder.setView(dialogView);

        final EditText nameEditText = dialogView.findViewById(R.id.edit_text_name);
        final LinearLayout stepsLayout = dialogView.findViewById(R.id.layout_steps);
        final List<EditText> stepsEditTextList = new ArrayList<>();

        Button plusButton = dialogView.findViewById(R.id.button_plus);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout stepLayout = new LinearLayout(context);
                stepLayout.setOrientation(LinearLayout.HORIZONTAL);

                final EditText newStepEditText = new EditText(context);
                newStepEditText.setHint("Step " + (stepsEditTextList.size() + 1));
                stepsEditTextList.add(newStepEditText);

                Button minusButton = new Button(context);
                minusButton.setText("-");
                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stepsLayout.removeView(stepLayout);  // Remove the entire step layout
                        stepsEditTextList.remove(newStepEditText);
                    }
                });

                stepLayout.addView(newStepEditText, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                stepLayout.addView(minusButton);

                stepsLayout.addView(stepLayout);
            }
        });

        dialogBuilder.setTitle("Add Procedure");
        if (primaryKey > 0) {
            // Editing an existing procedure, load its details
            Procedure existingProcedure = fetchProcedure(primaryKey);  // Implement this method to retrieve data
            if (existingProcedure != null) {
                nameEditText.setText(existingProcedure.getName());
                String[] stepsArray = existingProcedure.getSteps().split(",");
                for (String step : stepsArray) {
                    final LinearLayout stepLayout = new LinearLayout(context);
                    stepLayout.setOrientation(LinearLayout.HORIZONTAL);

                    EditText stepEditText = new EditText(context);
                    stepEditText.setText(step.trim());
                    stepsEditTextList.add(stepEditText);

                    Button minusButton = new Button(context);
                    minusButton.setText("-");
                    minusButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            stepsLayout.removeView(stepLayout);
                            stepsEditTextList.remove(stepEditText);
                        }
                    });

                    stepLayout.addView(stepEditText, new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    stepLayout.addView(minusButton);

                    stepsLayout.addView(stepLayout);
                }
            }
        }

        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = nameEditText.getText().toString();

                StringBuilder stepsBuilder = new StringBuilder();
                for (EditText stepEditText : stepsEditTextList) {
                    String step = stepEditText.getText().toString().trim();
                    if (!step.isEmpty()) {
                        stepsBuilder.append(step).append(",");
                    }
                }
                String steps = stepsBuilder.toString();
                if (steps.endsWith(",")) {
                    steps = steps.substring(0, steps.length() - 1);
                }

                if (primaryKey > 0) {
                    Log.d( "primary", primaryKey +"AAND"+primaryKe);

                    // Update existing procedure
                    updateProcedure(primaryKey, name, steps);  // Implement this method
                    primaryKe=primaryKey;
                    } else {
                    // Create new procedure
                    primaryKe =  insertProcedure(name, steps);
                    Log.d( "primary", primaryKey +" and "+primaryKe);

                }
                if (callback != null) {
                    callback.onDialogComplete(primaryKe);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        return primaryKe;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public long insertPerson(SQLiteDatabase db,String name, String nickname, String type, Date lastContact, Date contactTime,
                              List<Long> topicPast, List<Long> topicPresent, List<Long> future,double earning) {

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("nickname", nickname);
        values.put("type", type);
        values.put("lastcontact", dateFormat.format(lastContact));
        values.put("contacttime", dateFormat.format(contactTime));
        values.put("topicpast", TextUtils.join(",",topicPast));
        values.put("topicpresent", TextUtils.join(",",topicPresent));
        values.put("future", TextUtils.join(",",future));
        values.put("earning", earning);
        return db.insert(TABLE_PEOPLE, null, values);
    }


    public long insertTopicRecord(SQLiteDatabase db,long topicId, Date time, List<Long> personIds, double result, double earning) {

        ContentValues values = new ContentValues();
        values.put("topic_id", topicId);
        values.put("time", dateFormat.format(time));
        values.put("person_ids", TextUtils.join(",", personIds)); // Convert list to comma-separated string
        values.put("result", result);
        values.put("earning", earning);
        return db.insert(TABLE_TOPIC_RECORDS, null, values);
        
    }

    public long insertTopic(SQLiteDatabase db,String topic, String bringUpMethod, String factsAbout, String notes,
                            List<String> indexes, String avgResult, double avgReward) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topic", topic);
        values.put("bringupmethod", bringUpMethod);
        values.put("factsabout", factsAbout);
        values.put("notes", notes);
        values.put("indexes", TextUtils.join(",",indexes));
        values.put("avgresult", avgResult);
        values.put("avgreward", avgReward);
        return db.insert(TABLE_TOPICS, null, values);
    }

    public long insertMoney(SQLiteDatabase db,double amount, String source, String category, long address, Date time) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", amount);
        values.put("source", source);
        values.put("category", category);
        values.put("address", address);
        values.put("time", dateFormat.format(time));
        return db.insert(TABLE_MONEY, null, values);
    }

    public long insertExpenditureTemplate(SQLiteDatabase db,String name, String category, double avgAmountPerMonth,
                                          Date whenNextNeeded, String eliminationStrategy) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("category", category);
        values.put("avgamountpermonth", avgAmountPerMonth);
        values.put("whennextneeded", dateFormat.format(whenNextNeeded));
        values.put("eliminationstrategy", eliminationStrategy);
        return db.insert(TABLE_EXPENDITURE_TEMPLATE, null, values);
    }

    public long insertExpenditureRecord(SQLiteDatabase db,Date date, long address, List<Long> people, String whyEliminationStrategyFailed) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", dateFormat.format(date));
        values.put("address", address);
        values.put("people_ids", TextUtils.join(",",people));
        values.put("whyeliminationstrategyfailed", whyEliminationStrategyFailed);
        return db.insert(TABLE_EXPENDITURE_RECORDS, null, values);
    }

    public long insertBusiness(SQLiteDatabase db,String goodsAndServices, double averageProfitPerMonth, double profitLastMonth,
                               Date whenStarted, double worth, String stepsToRun, String stepsToGrow,
                               List<Long> associatedPeople, double projectedProfit, long plan) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("goodsandservices", goodsAndServices);
        values.put("averageprofitpermonth", averageProfitPerMonth);
        values.put("profitlastmonth", profitLastMonth);
        values.put("whenstarted", dateFormat.format(whenStarted));
        values.put("worth", worth);
        values.put("stepstorun", stepsToRun);
        values.put("stepstogrow", stepsToGrow);
        values.put("associatedpeople_ids", TextUtils.join(",",associatedPeople));
        values.put("projectedprofit", projectedProfit);
        values.put("plan_id", plan);
        return db.insert(TABLE_BUSINESS, null, values);
    }


    public long insertBusinessCashFlow(SQLiteDatabase db,long address, long business, double amountEach, float count,
                                       float amountKsh, Date date, String quality) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("business_id", business);
        values.put("amounteach", amountEach);
        values.put("count", count);
        values.put("amountksh", amountKsh);
        values.put("date", dateFormat.format(date));
        values.put("quality", quality);
        return db.insert(TABLE_BUSINESS_CASH_FLOW, null, values);
    }


    public long insertCashFlowTemplate(SQLiteDatabase db,String name, float averageAmountEach, float averageCount,
                                       float averageAmount, String improveStrategy, String reductionStrategy) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("averageamounteach", averageAmountEach);
        values.put("averagecount", averageCount);
        values.put("averageamount", averageAmount);
        values.put("improvestrategy", improveStrategy);
        values.put("reductionstrategy", reductionStrategy);
        return db.insert(TABLE_CASH_FLOW_TEMPLATE, null, values);
    }




    public long insertBusinessPlan(SQLiteDatabase db,String name, Date startingDate, float possibleProfit, String procedure,
                                   float initialCapital, String otherResources, List<Long> associatedPeople) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("startingdate", dateFormat.format(startingDate));
        values.put("possibleprofit", possibleProfit);
        values.put("procedure", procedure);
        values.put("initialcapital", initialCapital);
        values.put("otherresources", otherResources);
        values.put("associated_people_ids", TextUtils.join(",",associatedPeople));
        return db.insert(TABLE_BUSINESS_PLAN, null, values);
    }

    public long insertCompletedActivity(SQLiteDatabase db,long address, Date dateStarted,
                                        Date dateCompleted, float returns, String benefits,
                                        List<Long> associatedPeople, int associatedBusiness,
                                        float cost, String otherResources, String procedure,long planId) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("datestarted", dateFormat.format(dateStarted));
        values.put("datecompleted", dateFormat.format(dateCompleted));
        values.put("returns", returns);
        values.put("benefits", benefits);
        values.put("associatedpeople_ids", TextUtils.join(",",associatedPeople));
        values.put("associatedbusiness_id", associatedBusiness);
        values.put("cost", cost);
        values.put("otherresources", otherResources);
        values.put("procedure", procedure);
        values.put("planId", planId);

        return db.insert(TABLE_COMPLETED_ACTIVITIES, null, values);
    }

    public long insertPlan(SQLiteDatabase db,long address, Date dateStarted, Date datePlannedStart, Date datePlannedEnd,
                           float anticipatedReturns, float returns, String realisedBenefits,
                           String anticipatedBenefits, List<Long> associatedPeople,
                           List<Long> associatedBusiness, float cost, String otherResources,
                           String procedure) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("datestarted", dateFormat.format(dateStarted));
        values.put("dateplannedstart", dateFormat.format(datePlannedStart));
        values.put("dateplannedend", dateFormat.format(datePlannedEnd));
        values.put("anticipatedreturns", anticipatedReturns);
        values.put("returns", returns);
        values.put("realisedbenefits", realisedBenefits);
        values.put("anticipatedbenefits", anticipatedBenefits);
        values.put("associatedpeople_ids", TextUtils.join(",",associatedPeople));
        values.put("associatedbusiness_ids", TextUtils.join(",",associatedBusiness));
        values.put("cost", cost);
        values.put("otherresources", otherResources);
        values.put("procedure", procedure);
        return db.insert(TABLE_PLAN, null, values);
    }

    public long insertActivityTemplate(SQLiteDatabase db,String name, String category, String subcategory,
                                       float avgReturnPerMonth, List<Float> successIndexList,
                                       float avgTimeTaken, float timeChange) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("category", category);
        values.put("subcategory", subcategory);
        values.put("avgreturnpermonth", avgReturnPerMonth);
        values.put("successindexlist", TextUtils.join(",",successIndexList));
        values.put("avgtimetaken", avgTimeTaken);
        values.put("timechange", timeChange);
        return db.insert(TABLE_ACTIVITY_TEMPLATE, null, values);
    }
    public void insertSampleData(SQLiteDatabase db) {
        //SQLiteDatabase db = this.getWritableDatabase();

        // Insert sample data into PEOPLE table
        insertPerson(db,"John Doe", "JD", "Type1", new Date(), new Date(),
                Arrays.asList(1L, 2L), Arrays.asList(3L, 4L), Arrays.asList(5L, 6L), 1000.0);

        // Insert sample data into TOPIC-RECORDS table
        insertTopicRecord(db,1L, new Date(), Arrays.asList(1L, 2L), 8.5f, 200.0);

        // Insert sample data into TOPICS table
        insertTopic(db,"Technology", "Online tutorials", "Facts about latest technologies",
                "Notes about tech trends", Arrays.asList("Index1", "Index2"), "High", 150.0);

        // Insert sample data into MONEY table
        insertMoney(db, 500.0, "Salary", "Expenditure", 1L, new Date());

        // Insert sample data into EXPENDITURE-TEMPLATE table
        insertExpenditureTemplate(db,"Groceries", "Groceries", 200.0, new Date(),
                "Strategy to reduce expenses");

        // Insert sample data into EXPENDITURE-RECORDS table
        insertExpenditureRecord(db,new Date(), 1L, Arrays.asList(1L, 2L), "Strategy failed");

        // Insert sample data into BUSINESS table
        insertBusiness(db,"Tech Startup", 5000.0, 1000.0, new Date(), 20000.0,
                "Steps to start a tech business", "Steps to grow the business",
                Arrays.asList(1L, 2L), 5000.0, 1L);

        // Insert sample data into BUSINESS-CASH-FLOW table
        insertBusinessCashFlow(db,1L, 1L, 100.0, 5.0f, 500.0f, new Date(), "High");

        // Insert sample data into CASH-FLOW-TEMPLATE table
        insertCashFlowTemplate(db,"Monthly Expenses", 150.0f, 4.0f, 600.0f,
                "Improve cash flow", "Reduce unnecessary expenses");

        // Insert sample data into BUSINESS-PLAN table
        insertBusinessPlan(db,"Tech Startup Plan", new Date(), 50000.0f,
                "Detailed plan for the startup", 20000.0f,
                "Additional resources needed", Arrays.asList(1L, 2L));

        // Insert sample data into COMPLETED-ACTIVITIES table
        insertCompletedActivity(db,1L,
                new Date(), new Date(), 1000.0f, "Positive feedback",
                Arrays.asList(1L, 2L), 1, 300.0f, "Project documentation",
                "1",1);

        Calendar calendar = Calendar.getInstance();

        // Set the date components
        calendar.set(Calendar.YEAR, 1980);
        calendar.set(Calendar.MONTH, Calendar.JANUARY); // January is 0-based
        calendar.set(Calendar.DAY_OF_MONTH, 4);

        // Set the time components
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 11);
        calendar.set(Calendar.SECOND, 10);

        // Convert the Calendar object to a Date
        Date date = calendar.getTime();
        // Insert sample data into PLAN table
        insertPlan(db,1L, new Date(), new Date(), new Date(), 800.0f, 900.0f,
                "Realized benefits of the plan", "Expected benefits of the plan",
                Arrays.asList(1L, 2L), Arrays.asList(1L, 2L), 400.0f,
                "Additional resources required", "1");

        // Insert sample data into ACTIVITY-TEMPLATE table
        insertActivityTemplate(db,"Coding Workshop", "Workshop", "Programming",
                50.0f, Arrays.asList(0.8f, 0.9f), 2.5f, 0.5f);
    }

    public double getProportionOfCompletedWithinWeek(int weeks) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Calculate the date one week from now and one week ago
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, weeks);
        Date oneWeekForward = calendar.getTime();
        calendar.add(Calendar.WEEK_OF_YEAR, -(weeks+1));
        Date oneWeekBehind = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String oneWeekForwardStr = dateFormat.format(oneWeekForward);
        String oneWeekBehindStr = dateFormat.format(oneWeekBehind);

        // Query to count the number of rows where "dateplannedstart" is within one week from now
        String countQuery = "SELECT COUNT(*) FROM plan WHERE completed IS NOT NULL " +
                "AND dateplannedstart >= '" + oneWeekBehindStr + "' " +
                "AND dateplannedstart <= '" + oneWeekForwardStr + "'";

        Cursor cursor = db.rawQuery(countQuery, null);
        int totalCount = 0;

        if (cursor.moveToFirst()) {
            totalCount = cursor.getInt(0);
        }

        // Query to count the total number of rows in the table
        countQuery = "SELECT COUNT(*) FROM plan "+
                "WHERE dateplannedstart >= '" + oneWeekBehindStr + "' " +
                "AND dateplannedstart <= '" + oneWeekForwardStr + "'";

        cursor = db.rawQuery(countQuery, null);
        int totalRows = 0;

        if (cursor.moveToFirst()) {
            totalRows = cursor.getInt(0);
        }

        // Calculate the proportion
        double proportion = (double) totalCount / totalRows;

        cursor.close();
        db.close();


        return proportion;
    }



}

