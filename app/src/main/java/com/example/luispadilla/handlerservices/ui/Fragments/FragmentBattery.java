package com.example.luispadilla.handlerservices.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luispadilla.handlerservices.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentBattery extends Fragment {

    BatteryInteractionListener batteryListener;
    @BindView(R.id.textView_status_value) TextView statusField;
    @BindView(R.id.textView_time_available_value) TextView timeAvailableField;
    @BindView(R.id.textView_capacity_value) TextView capacityField;
    @BindView(R.id.charge_counter) TextView chargeCounter;
    @BindView(R.id.current_average) TextView currentAverage;
    @BindView(R.id.current_now) TextView currentNow;
    @BindView(R.id.energy_counter) TextView energyCounter;

    private Unbinder unbinder;

    public FragmentBattery() {
        // Required empty public constructor
    }

    public static FragmentBattery newInstance(String param1, String param2) {
        FragmentBattery fragment = new FragmentBattery();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_battery, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(batteryListener != null) {
            statusField.setText(batteryListener.batteryStatus());
            timeAvailableField.setText(String.valueOf(batteryListener.batteryTime()));
            HashMap<String, String> batteryProps = batteryListener.batteryProps();
            if(batteryProps != null){
                capacityField.setText(batteryProps.get("capacity"));
                chargeCounter.setText(batteryProps.get("charge_counter"));
                currentAverage.setText(batteryProps.get("current_average"));
                currentNow.setText(batteryProps.get("current_now"));
                energyCounter.setText(batteryProps.get("energy_counter"));
            }

        }
        return view;
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentBattery.BatteryInteractionListener) {
            batteryListener = (FragmentBattery.BatteryInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LocationInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        batteryListener = null;
    }

    public interface  BatteryInteractionListener {
        String batteryStatus();
        HashMap<String, String> batteryProps();
        long batteryTime();
    }
}
