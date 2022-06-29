package com.codeofscappy.chat.view.display;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.codeofscappy.chat.R;
import com.codeofscappy.chat.common.Common;
import com.codeofscappy.chat.databinding.ActivityViewImageBinding;
import com.codeofscappy.chat.view.profile.ProfileActivity;

public class ViewImageActivity extends AppCompatActivity {

    ActivityViewImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_image);

        binding.ImageView.setImageBitmap(Common.IMAGE_BITMAP);

        //Toolbar Navigation: --> Button --> Back-Arrow
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewImageActivity.this, ProfileActivity.class));
            }
        });


    }
}