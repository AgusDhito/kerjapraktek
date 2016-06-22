package com.example.user.mana_livechat;

import java.util.ArrayList;
import java.util.Random;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by user on 22/06/2016.
 */
public class StatsTest extends Fragment {
    private TextView statstext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_layout, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(
                "Contacts");
        statstext = (TextView) view.findViewById(R.id.statsText);
        statstext.setText("Ini text status");
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
    }
}
