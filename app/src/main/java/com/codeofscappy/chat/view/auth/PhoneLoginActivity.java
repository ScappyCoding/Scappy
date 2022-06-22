package com.codeofscappy.chat.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.codeofscappy.chat.R;
import com.codeofscappy.chat.databinding.ActivityPhoneLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneLoginActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ActivityPhoneLoginBinding binding;
    String[] country = { "Germany", "USA", "China", "Japan", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_phone_login);




        //Getting the instance of Spinner and applying OnItemSelectedListener on it.
        Spinner spin = findViewById(R.id.spinner_country);
        spin.setOnItemSelectedListener(this);

        //Creating box ArrayAdapter instance having the country list
        ArrayAdapter<String>  aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting he ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        //Firebase Instance
        mAuth = FirebaseAuth.getInstance();
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}