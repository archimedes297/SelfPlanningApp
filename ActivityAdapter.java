package com.peter.foward;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ActivityAdapter extends ArrayAdapter<Activity> {
        private LayoutInflater inflater;
        private SparseBooleanArray selectedItems;

        public ActivityAdapter(Context context, List<Activity> activities, SparseBooleanArray selectedItems) {
            super(context, 0, activities);
            inflater = LayoutInflater.from(context);
            this.selectedItems = selectedItems;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_person, parent, false);
            }

            Activity activity = getItem(position);

            TextView nameTextView = convertView.findViewById(R.id.nameTextView);
            TextView nicknameTextView = convertView.findViewById(R.id.nicknameTextView);
            CheckBox checkBox = convertView.findViewById(R.id.checkBox);
            checkBox.setFocusable(false);
            checkBox.setFocusableInTouchMode(false);

            nameTextView.setText(activity.getName());
            nicknameTextView.setText(activity.getCategory());

            checkBox.setChecked(selectedItems.get(position, false));
            checkBox.setOnClickListener(view -> {
                selectedItems.put(position, checkBox.isChecked());
            });

            return convertView;
        }
        public SparseBooleanArray getSelectedItems() {
            return selectedItems;
        }
    }
