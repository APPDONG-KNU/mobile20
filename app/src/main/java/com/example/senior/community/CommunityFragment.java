package com.example.senior.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senior.R;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);

        recyclerView = view.findViewById(R.id.fragment_community_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        List<CommunityFeedData> test = new ArrayList<>();
        test.add(new CommunityFeedData("김민수", "2020.10.10", "안녕하세요."));
        test.add(new CommunityFeedData("이민수", "2020.10.30", "반갑습니다."));
        test.add(new CommunityFeedData("박민수", "2020.08.02", "테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다테스트입니다"));
        test.add(new CommunityFeedData("최민수", "2020.01.19", "Hello?"));
        test.add(new CommunityFeedData("정민수", "2020.03.25", "Good!"));
        recyclerView.setAdapter(new CommunityFeedAdapter(test));

        return view;
    }
}