package com.example.luispadilla.handlerservices.ui.Fragments;

import android.content.Context;
import android.net.DhcpInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luispadilla.handlerservices.R;
import com.example.luispadilla.handlerservices.ui.Adapters.NetworkListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentWifi extends Fragment {

    // UI
    @BindView(R.id.editText_wifi_status) TextView wifiStatus;
    @BindView(R.id.textView_dhcp_ip_value) TextView dhcpIpValue;
    @BindView(R.id.textView_dhcp_gateway_value) TextView dhcpGatewayValue;
    @BindView(R.id.textView_dhcp_dns_value) TextView dhcpDNSValue;
    @BindView(R.id.textView_dhcp_serverAddress_value) TextView dhcpServerAddressValue;
    @BindView(R.id.recyclerView_networks) RecyclerView recyclerView;
    private Unbinder unbinder;

    //Data
    WifiInteractionListener mListener;
    private NetworkListAdapter netAdapter;
    ArrayList<String> netList = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentWifi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentWifi.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentWifi newInstance(String param1, String param2) {
        FragmentWifi fragment = new FragmentWifi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_wifi, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (mListener != null) {
            mListener.availableNetworks();
            String wifiLabel = mListener.wifiStatus();
            wifiStatus.setText(wifiLabel);
            DhcpInfo dhcpInf = mListener.dhcpInfo();
            if(wifiLabel.equals("enabled")){
                this.setDHCPInfo(dhcpInf);
            }
        }
        RecyclerView.LayoutManager displayLayout = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(displayLayout);
        netAdapter = new NetworkListAdapter(netList);
        recyclerView.setAdapter(netAdapter);
        return view;
    }

    private void setDHCPInfo(DhcpInfo data){
        dhcpIpValue.setText(String.valueOf(data.ipAddress));
        dhcpGatewayValue.setText(String.valueOf(data.gateway));
        dhcpDNSValue.setText(String.valueOf(data.dns1));
        dhcpServerAddressValue.setText(String.valueOf(data.serverAddress));
    }

    public void updateList(ArrayList<String> list){
        if (netAdapter != null) {
            netAdapter.updateNetworks(list);
            netAdapter.notifyDataSetChanged();
        }

    }

    @OnClick(R.id.button_scan_networks)
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.availableNetworks();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentWifi.WifiInteractionListener) {
            mListener = (FragmentWifi.WifiInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement WifiInteractionListener");
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface WifiInteractionListener {
        void availableNetworks();
        String wifiStatus();
        DhcpInfo dhcpInfo();
    }
}
