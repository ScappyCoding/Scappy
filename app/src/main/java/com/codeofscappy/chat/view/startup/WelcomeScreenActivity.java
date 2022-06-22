package com.codeofscappy.chat.view.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.codeofscappy.chat.view.MainActivity;
import com.codeofscappy.chat.R;
import com.codeofscappy.chat.view.auth.PhoneLoginActivity;

public class WelcomeScreenActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);



        // Send User to Register "when" accept [Terms of Service]
        textView  = (TextView) findViewById(R.id.accept_button);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreenActivity.this, PhoneLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}