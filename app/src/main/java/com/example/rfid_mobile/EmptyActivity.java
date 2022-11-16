package com.example.rfid_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_rfid);
        Bundle arguments = getIntent().getExtras();
        String id = arguments.getString("id");
        Button addObjectButton = findViewById(R.id.addObjectButton);
        addObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addObject(id);
            }
        });
    }
    void addObject(String id) {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("type", "add");
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }
}