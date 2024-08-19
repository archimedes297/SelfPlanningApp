package com.peter.foward;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BusinessActivity extends AppCompatActivity {

    private TextView tvGoodsAndServices, tvAverageProfit, tvProfitLastMonth, tvWhenStarted, tvWorth, tvStepsToRun, tvStepsToGrow, tvAssociatedPeople, tvProjectedProfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        // Initialize the views
        tvGoodsAndServices = findViewById(R.id.tvGoodsAndServices);
        tvAverageProfit = findViewById(R.id.tvAverageProfit);
        tvProfitLastMonth = findViewById(R.id.tvProfitLastMonth);
        tvWhenStarted = findViewById(R.id.tvWhenStarted);
        tvWorth = findViewById(R.id.tvWorth);
        tvStepsToRun = findViewById(R.id.tvStepsToRun);
        tvStepsToGrow = findViewById(R.id.tvStepsToGrow);
        tvAssociatedPeople = findViewById(R.id.tvAssociatedPeople);
        tvProjectedProfit = findViewById(R.id.tvProjectedProfit);

        int businessId = getIntent().getIntExtra("Business_ID", -1);
        if (businessId != -1) {
            fetchAndDisplayBusinessDetails(businessId);
        }
    }

    private void fetchAndDisplayBusinessDetails(int businessId) {
        SQLiteOpenHelper dbHelper=new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("business", null, "id = ?", new String[]{String.valueOf(businessId)}, null, null, null);

        if (cursor.moveToFirst()) {
            tvGoodsAndServices.setText(cursor.getString(cursor.getColumnIndex("goodsandservices")));
            tvAverageProfit.setText(String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("averageprofitpermonth"))));
            tvProfitLastMonth.setText(String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("profitlastmonth"))));
            tvWhenStarted.setText(cursor.getString(cursor.getColumnIndex("whenstarted")));
            tvWorth.setText(String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("worth"))));
            tvStepsToRun.setText(cursor.getString(cursor.getColumnIndex("stepstorun")));
            tvStepsToGrow.setText(cursor.getString(cursor.getColumnIndex("stepstogrow")));
            tvAssociatedPeople.setText(cursor.getString(cursor.getColumnIndex("associatedpeople_ids")));
            tvProjectedProfit.setText(String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("projectedprofit"))));

            // Display other fields as needed
        }

        cursor.close();
        db.close();
    }
}
