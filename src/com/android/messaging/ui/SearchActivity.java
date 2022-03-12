package com.android.messaging.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.messaging.R;
import com.android.messaging.datamodel.BugleDatabaseOperations;
import com.android.messaging.datamodel.DataModel;
import com.android.messaging.datamodel.DatabaseWrapper;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void doSearch(View view) {
        final DatabaseWrapper db = DataModel.get().getDatabase();
        TextView search = (TextView)findViewById(R.id.query);
        BugleDatabaseOperations.searchMessages(db, (String) search.getText(), 12);
    }
}