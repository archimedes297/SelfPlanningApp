package com.peter.foward;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
@RequiresApi(api = Build.VERSION_CODES.N)
public class PersonActivity extends AppCompatActivity {

    private TextView tvName, tvNickname, tvType, tvLastContact, tvContactTime, tvTopicPast, tvTopicPresent, tvFuture, tvEarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        // Initialize the views
        tvName = findViewById(R.id.tvName);
        tvNickname = findViewById(R.id.tvNickname);
        tvType = findViewById(R.id.tvType);
        tvLastContact = findViewById(R.id.tvLastContact);
        tvContactTime = findViewById(R.id.tvContactTime);
        tvTopicPast = findViewById(R.id.tvTopicPast);
        tvTopicPresent = findViewById(R.id.tvTopicPresent);
        tvFuture = findViewById(R.id.tvFuture);
        tvEarning = findViewById(R.id.tvEarning);

        int personId = getIntent().getIntExtra("PERSON_ID", -1);
        if (personId != -1) {
            fetchAndDisplayPersonDetails(personId);
        }
    }


    private void fetchAndDisplayPersonDetails(int personId) {
        SQLiteOpenHelper dbHelper=new DatabaseHelper(this);;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("people", null, "id = ?", new String[]{String.valueOf(personId)}, null, null, null);

        if (cursor.moveToFirst()) {
            tvName.setText(cursor.getString(cursor.getColumnIndex("name")));
            tvNickname.setText(cursor.getString(cursor.getColumnIndex("nickname")));
            tvType.setText(cursor.getString(cursor.getColumnIndex("type")));
            tvLastContact.setText(cursor.getString(cursor.getColumnIndex("lastcontact")));
            tvContactTime.setText(cursor.getString(cursor.getColumnIndex("contacttime")));
            tvTopicPast.setText(cursor.getString(cursor.getColumnIndex("topicpast")));
            tvTopicPresent.setText(cursor.getString(cursor.getColumnIndex("topicpresent")));
            tvFuture.setText(cursor.getString(cursor.getColumnIndex("future")));
            tvEarning.setText(String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("earning"))));

            // Set other fields as needed
        }

        cursor.close();
        db.close();
    }
}

