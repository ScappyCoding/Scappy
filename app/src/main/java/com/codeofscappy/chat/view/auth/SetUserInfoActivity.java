package com.codeofscappy.chat.view.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.codeofscappy.chat.R;
import com.codeofscappy.chat.databinding.ActivitySetUserInfoBinding;
import com.codeofscappy.chat.model.user.Users;
import com.codeofscappy.chat.view.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetUserInfoActivity extends AppCompatActivity {
    private ActivitySetUserInfoBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_set_user_info);

        progressDialog = new ProgressDialog( this);
        initButtonClick();
    }

    private void initButtonClick() {
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.edName.getText().toString())){
                    // User see this Message when he/her not has input  "Username"
                    Toast.makeText(getApplicationContext(), "Please input your Username", Toast.LENGTH_SHORT).show();
                }else {
                    // when User has input the "Username" then get Update-function in action
                    doUpdate();
                }

            }
        });

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pickImage();
                // i will do later
                Toast.makeText(getApplicationContext(), "this function is not ready to use...", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void doUpdate() {
        //////
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userID = firebaseUser.getUid();
            Users users = new Users(
                    userID,
                    binding.edName.getText().toString(),
                    firebaseUser.getPhoneNumber(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "");
            // Update "userName" in Firestore Collection "Users"-->Uid-->users
            firebaseFirestore.collection("Users").document(firebaseUser.getUid()).set(users)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Info to User --> "Update are Successful"
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "update Successful", Toast.LENGTH_SHORT).show();
                            // when Update-Process Success send User to MainActivity
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Update Failure :"+ e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        }else {
            // User are not Login --> Message " Please SignUp/SignIn first"

            Toast.makeText(getApplicationContext(), "you need login first", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }
}