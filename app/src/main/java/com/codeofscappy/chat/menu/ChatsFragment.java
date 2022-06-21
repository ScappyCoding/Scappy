package com.codeofscappy.chat.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeofscappy.chat.R;
import com.codeofscappy.chat.adapter.ChatListAdapter;
import com.codeofscappy.chat.model.Chatlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Asimple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {



    public ChatsFragment() {
        // Required empty public constructor
    }

    private List<Chatlist> list = new ArrayList<>();
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getChatlist();
        return  view;
    }

    // UserList [-Demo_User-]--> Real User Implement later!
    private void getChatlist() {
        list.add(new Chatlist("11","Demo_User","Demo_Status","21.06.2022","https://pickaface.net/gallery/avatar/20160608_151321_4867_Jeffyou.png"));
        list.add(new Chatlist("12","Demo_User2","Demo_Status","21.06.2022","https://pickaface.net/gallery/avatar/20160110_040511_326_demo.png"));

        recyclerView.setAdapter(new ChatListAdapter(list,getContext()));
    }

}