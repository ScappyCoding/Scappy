package com.codeofscappy.chat.view;


import static com.codeofscappy.chat.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.codeofscappy.chat.R;
import com.codeofscappy.chat.databinding.ActivityMainBinding;
import com.codeofscappy.chat.menu.CallsFragment;
import com.codeofscappy.chat.menu.ChatsFragment;
import com.codeofscappy.chat.menu.StatusFragment;
import com.codeofscappy.chat.view.contact.ContactsActivity;
import com.codeofscappy.chat.view.settings.SettingsActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, layout.activity_main);


        // Open Contact-List when Clicked on FabAction
        binding.fabAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContactsActivity.class));
            }
        });



       

        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        setSupportActionBar(binding.toolbar);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeFabIcon(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });





    }
    private void setUpWithViewPager(ViewPager viewPager) {
        MainActivity.SectionsPagerAdapter adpater = new SectionsPagerAdapter(getSupportFragmentManager());
        // Fragment im TabView ---> Tabs: "Chats, "Status", "Calls"
        adpater.addFragment(new ChatsFragment(),"Chats");
        adpater.addFragment(new StatusFragment(),"Status");
        adpater.addFragment(new CallsFragment(),"Calls");

        // need 3 fragments
        viewPager.setAdapter(adpater);

    }

    // Manager for Fragment Tabs
    private  static  class  SectionsPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment,  String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    // Create a Menu and Inflate the Menu from Menu_Main.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;


    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

       switch (id)
       {
           case R.id.menu_search : Toast.makeText(MainActivity.this, "Action Search", Toast.LENGTH_SHORT).show(); break;
           case R.id.action_new_group : Toast.makeText(MainActivity.this, "Action Groups", Toast.LENGTH_SHORT).show(); break;
           case R.id.action_new_broadcast : Toast.makeText(MainActivity.this, "Action Broadcast", Toast.LENGTH_SHORT).show(); break;
           case R.id.action_messenger_web : Toast.makeText(MainActivity.this, "Action Web", Toast.LENGTH_SHORT).show(); break;
           case R.id.action_logout : Toast.makeText(MainActivity.this, "Action logout", Toast.LENGTH_SHORT).show(); break;
           case R.id.action_settings :
               startActivity(new Intent(MainActivity.this, SettingsActivity.class)); break;

       }
        return  super.onOptionsItemSelected(item);
    }

    private void changeFabIcon(final int index){
        binding.fabAction.hide();

        new Handler().postDelayed(new Runnable() {

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void run() {
                switch (index){
                    case 0 : binding.fabAction.setImageDrawable(getDrawable(drawable.ic_chat));break;
                    case 1 : binding.fabAction.setImageDrawable(getDrawable(drawable.ic_camera)); break;
                    case 2 : binding.fabAction.setImageDrawable(getDrawable(drawable.ic_call)); break;
                }
                binding.fabAction.show();
            }
        },300);

    }



}