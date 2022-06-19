package com.codeofscappy.chat;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.codeofscappy.chat.databinding.ActivityMainBinding;
import com.codeofscappy.chat.menu.CallsFragment;
import com.codeofscappy.chat.menu.ChatsFragment;
import com.codeofscappy.chat.menu.StatusFragment;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);



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




}