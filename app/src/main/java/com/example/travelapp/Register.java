package com.example.travelapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp.database.DatabaseHandler;
import com.example.travelapp.model.UsersData;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHandler=new DatabaseHandler(this);

        Button submit = (Button) findViewById(R.id.submitregister);
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                EditText fname = (EditText) findViewById(R.id.editTextTextPersonNom);
                EditText lname = (EditText) findViewById(R.id.editTextTextPersonPrenom);
                EditText username = (EditText) findViewById(R.id.editTextTextPersonUsername);
                EditText pass = (EditText) findViewById(R.id.editTextTextPasswordnew);
                EditText passconf = (EditText) findViewById(R.id.editTextTextPasswordconf);
                if ((fname.getText().toString().equals("")) || (lname.getText().toString().equals(""))
                    || (pass.getText().toString().equals("")) || (passconf.getText().toString().equals("")))
                {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                }
                else if (!pass.getText().toString().equals(passconf.getText().toString())){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.pass_dont_match), Toast.LENGTH_SHORT).show();
                }
                else {
                    UsersData newUser=new UsersData(0, username.getText().toString(), fname.getText().toString(), lname.getText().toString(), pass.getText().toString());
                    dbHandler.insertUser(newUser);
                    finish();
                }
            }
        });

    }
}