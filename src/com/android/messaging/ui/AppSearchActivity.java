package com.android.messaging.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.appsearch.SearchResults;

import com.android.messaging.R;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import android.app.appsearch.SearchSpec;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AppSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_search);
    }

    public void search(View view) {
    }
}