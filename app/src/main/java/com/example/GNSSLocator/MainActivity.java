package com.example.GNSSLocator;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_gnss = findViewById(R.id.button_gnss);
        button_gnss.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), GNSSActivity.class);
            startActivity(i);
        });
    }
}

