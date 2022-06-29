package com.codeofscappy.chat.view.display;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.codeofscappy.chat.R;
import com.codeofscappy.chat.common.Common;
import com.codeofscappy.chat.databinding.ActivityViewImageBinding;

public class ViewImageActivity extends AppCompatActivity {

    ActivityViewImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_image);

        binding.ImageView.setImageBitmap(Common.IMAGE_BITMAP);


    }
}