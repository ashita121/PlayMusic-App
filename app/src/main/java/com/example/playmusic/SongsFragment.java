package com.example.playmusic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.playmusic.MainActivity.musicFiles;

public class SongsFragment extends Fragment implements MusicAdapter.OnOptionClick {
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;

    public SongsFragment() {

        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if(!(musicFiles.size() < 1))
        {
            musicAdapter = new MusicAdapter(getContext(), musicFiles,this::onDeleteOptionClick);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        }
        return view;
    }

    @Override
    public void onDeleteOptionClick(int position) {
     Log.e("Current POs", String.valueOf(position));
    }
}