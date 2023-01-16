package android.example.contactapp_e020320107;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialization
        FloatingActionButton fab = findViewById(R.id.fab);

        // add listener
        fab.setOnClickListener(v -> {
            // move to new activity to add contact
            Intent intent = new Intent(MainActivity.this,AddEditContact.class );
            startActivity(intent);
        });
    }

//    add dependency
//    add  colour code
//    design main activity
//    create new activity called AddEditContact
}