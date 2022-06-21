package com.codeofscappy.chat.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeofscappy.chat.R;
import com.codeofscappy.chat.adapter.CallListAdapter;
import com.codeofscappy.chat.model.CallList;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallsFragment extends Fragment {



    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calls, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        List<CallList> lists = new ArrayList<>();


        // Demo: Call-List--> Real Call-List Implement later
        lists.add(new CallList(
                "001",
                "Demo_User",
                "21.06.2022",
                "https://pickaface.net/gallery/avatar/20160608_151321_4867_Jeffyou.png",
                "income"));

        lists.add(new CallList(
                "002",
                "Demo_User2",
                "21.06.2022",
                "https://pickaface.net/gallery/avatar/20160110_040511_326_demo.png",
                "missed"));

        recyclerView.setAdapter(new CallListAdapter(lists, getContext()));
        return view;

    }

}