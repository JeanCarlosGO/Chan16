package com.example.jean.chan16.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jean.chan16.Adapters.PostAdapter;
import com.example.jean.chan16.Models.Post;
import com.example.jean.chan16.R;

import java.util.List;

public class DashboardFragment extends Fragment {

    private DataProvider listener;
    private List<Post> mData;

    // Views
    View view;

    public DashboardFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView rv = view.findViewById(R.id.list);
        TextView ph = view.findViewById(R.id.emptyPlaceholder);
        if(getData() != null) {
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(new PostAdapter(getContext(), getData()));
        } else {
            rv.setVisibility(View.GONE);
            ph.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DataProvider) {
            listener = (DataProvider) context;
            mData = listener.getData();
        } else {
            throw new RuntimeException(context.toString() + " must implement DataProvider");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public List<Post> getData() {
        return mData;
    }

    public void setData(List<Post> mData) {
        this.mData = mData;
    }

    public void reloadData() {
        setData(listener.getData());
    }

    public interface DataProvider {
        List<Post> getData();
    }
}
