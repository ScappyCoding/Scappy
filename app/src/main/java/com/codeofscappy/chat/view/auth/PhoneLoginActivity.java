package com.codeofscappy.chat.view.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codeofscappy.chat.R;
import com.codeofscappy.chat.databinding.ActivityPhoneLoginBinding;
import com.codeofscappy.chat.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private ActivityPhoneLoginBinding binding;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_phone_login);









        //Firebase Instance
        mAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.btnNext.getText().toString().equals("Next"))
                {
                    progressDialog.setMessage("Please wait");
                    progressDialog.show();

                    String phone = "+" +binding.edCodeCountry.getText().toString() + binding.edPhone.getText().toString();
                    startPhoneNumberVerification(phone);
                }
                else
                {
                    progressDialog.setMessage("Verifying...");
                    progressDialog.show();
                    verifyPhoneNumberWithCode(mVerificationId,binding.edCode.getText().toString());
                }
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
                Log.d("TAG", "onVerificationCompleted: Complete");
                signInWithPhoneAuthCredentials(phoneAuthCredential);
                progressDialog.dismiss();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e)
            {
                Log.d("TAG","onVerificationFailed: " + e.getMessage());

            }


            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to Provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                //by combining the code with a specification ID.
                Log.d("TAG","onCodeSent" + verificationId);

                // Save verification ID and resending token so we can use the later
                mVerificationId = verificationId;
                mResendToken = token;

                binding.btnNext.setText("Confirm");
                progressDialog.dismiss();
                // START EXCLUDE
                // END EXCLUDE
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        //  [START start_phone-auth]

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phnone numer to verify
                60,         //  Timeout duration
                TimeUnit.SECONDS,  //  Unit of Timeout
                this,       //  Activity (for callback binding)
                mCallbacks);       //  OnVerificationStatsChangeCallbacks
        //END start-phone_auth


    }

    private  void verifyPhoneNumberWithCode(String VerificationId, String code) {
        //[START start_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId, code);
        //[END verify-with_code]
        signInWithPhoneAuthCredentials(credential);
    }

    private void signInWithPhoneAuthCredentials(PhoneAuthCredential credential) {
        // [START sign_in_with_phone]
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {


                            //Sign in success, update UI with signed in User´s Information
                            progressDialog.dismiss();
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(PhoneLoginActivity.this, MainActivity.class));
                        }
                        else
                        {
                            // Sign in failed, display a message and update the UI
                            progressDialog.dismiss();
                            Log.d("TAG","signInWithCredentials:failure",task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                               Log.d("TAG","onComplete: Error Code");
                            }
                        }
                    }
                });
    }
    // [END sign_in_with_phone]





}