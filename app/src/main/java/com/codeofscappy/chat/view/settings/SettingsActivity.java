package com.codeofscappy.chat.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.codeofscappy.chat.R;
import com.codeofscappy.chat.databinding.ActivitySettingsBinding;
import com.codeofscappy.chat.view.MainActivity;
import com.codeofscappy.chat.view.profile.ProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_settings);


       Toolbar toolbar = findViewById(R.id.toolbar);
       toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(SettingsActivity.this, MainActivity.class));
           }
       });



       // Instance of Firebase Services
       firestore = FirebaseFirestore.getInstance();
       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


       // When User Ok! -->  getInfo Methode in Action
       if (firebaseUser != null){
           getInfo();

       }
       initClickAction();

    }

    private void initClickAction() {
        binding.setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
            }
        });
    }


    // This Methode Read the Data from Current-User and Push this Into the View-Elements
    private void getInfo() {
        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userName = documentSnapshot.get("userName").toString();
                binding.tvUsername.setText(userName);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","onFailure" + e.getMessage());

            }
        });
    }
}