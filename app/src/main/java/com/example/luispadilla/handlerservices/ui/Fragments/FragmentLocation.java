package com.example.luispadilla.handlerservices.ui.Fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.luispadilla.handlerservices.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentLocation extends Fragment {


    LocationInteractionListener locationListener;
    @BindView(R.id.listView_location_providers) ListView locationProvidersList;
    @BindView(R.id.textView_position_value) TextView positionField;
    private Unbinder unbinder;

    // Data
    private ArrayList<String> locationListData;
    ArrayAdapter<String> listAdapter;


    public FragmentLocation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fragment_location, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(locationListener != null){
            locationListData = new ArrayList(locationListener.getLocationProviders());
            Location currentPosition = locationListener.getUserLocation();
            if(currentPosition != null){
                positionField.setText(currentPosition.getLatitude() + "," + currentPosition.getLongitude());
            }
        }
        listAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, locationListData);
        locationProvidersList.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentLocation.LocationInteractionListener) {
            locationListener = (FragmentLocation.LocationInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LocationInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        locationListener = null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public interface  LocationInteractionListener {
        List<String> getLocationProviders();
        Location getUserLocation();
    }
}
