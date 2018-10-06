package com.example.luispadilla.handlerservices.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.luispadilla.handlerservices.R;
import com.example.luispadilla.handlerservices.ui.Adapters.ViewPagerAdapter;
import com.example.luispadilla.handlerservices.ui.Fragments.FragmentBattery;
import com.example.luispadilla.handlerservices.ui.Fragments.FragmentLocation;
import com.example.luispadilla.handlerservices.ui.Fragments.FragmentWifi;
import com.example.luispadilla.handlerservices.ui.Utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentBattery.BatteryInteractionListener, FragmentLocation.LocationInteractionListener, FragmentWifi.WifiInteractionListener {

    private static Context mContext;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.strip)
    PagerTabStrip pagerStrip;
    WifiManager wifiManager;
    LocationManager locationManager;
    BatteryManager batteryManager;
    private List<ScanResult> results;
    ViewPagerAdapter pageAdapter;
    BroadcastAppReceiver appReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);
        pageAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);

        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        batteryManager = (BatteryManager) mContext.getSystemService(Context.BATTERY_SERVICE);
        appReceiver = new BroadcastAppReceiver();
        if(!checkPermissions()){
            askForPermission();
        }
        if (!this.wifiEnabled()) {
            Toast.makeText(this, "Wifi disabled enabling...", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
         }

        viewPager.setOffscreenPageLimit(3);
    }

    public Boolean wifiEnabled() {
        return wifiManager.isWifiEnabled();
    }


    public void askForPermission() {
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };
        ActivityCompat.requestPermissions(this, PERMISSIONS, Constants.PERM_CODE);
    }


    public boolean checkPermissions() {
        int currentFineLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int currentCoarseLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return currentFineLocationPermission == PackageManager.PERMISSION_GRANTED || currentCoarseLocationPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void scanSuccess() {
        results = wifiManager.getScanResults();  // use new scan results
        ArrayList<String> netList = new ArrayList<>();
        for(ScanResult scanResult : results){
            netList.add(scanResult.SSID);
        }
        pageAdapter.fragmentWifi.updateList(netList);
    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        results = wifiManager.getScanResults(); // potentially use older scan results
    }

    @Override
    public void availableNetworks() {
        registerReceiver(appReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
//        if (!success) {
//            scanFailure();
//        }
        Toast.makeText(this, "Scanning Wifi...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public String wifiStatus() {
        switch (wifiManager.getWifiState()) {
            case WifiManager.WIFI_STATE_DISABLING:
                return "disabling";
            case WifiManager.WIFI_STATE_DISABLED:
                return "disabled";
            case WifiManager.WIFI_STATE_ENABLING:
                return "enabling";
            case WifiManager.WIFI_STATE_ENABLED:
                return "enabled";
            case WifiManager.WIFI_STATE_UNKNOWN:
                return "unknown";
            default:
                return null;  //or whatever you want for an error string
        }
    }

    @Override
    public DhcpInfo dhcpInfo() {
        DhcpInfo infoResult = wifiManager.getDhcpInfo();
        return infoResult;
    }

    @Override
    public List<String> getLocationProviders() {
        List<String> providers = locationManager.getAllProviders();
        return providers;
    }
    @SuppressLint("MissingPermission")
    @Override
    public Location getUserLocation() {
        Location result = null;
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        if (this.checkPermissions()) {
            result = locationManager.getLastKnownLocation(locationProvider);
        } else {
            askForPermission();
        }
        return result;
    }

    public void bateryLevelStatus(){
        registerReceiver(appReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    public String getBatteryStatus(int status) {
        List<String> statuses = Arrays.asList("UNKNOWN", "CHARGING", "DISCHARGING", "FULL", "NOT CHARGING");
        String response = statuses.get(0);
        switch(status){
            case BatteryManager.BATTERY_STATUS_CHARGING:
                response = statuses.get(1);
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                response = statuses.get(2);
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                response = statuses.get(3);
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                response = statuses.get(4);
                break;
        }
        return response;
    }

    @Override
    public String batteryStatus() {
        return getBatteryStatus(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS));
    }

    @Override
    public HashMap<String, String> batteryProps() {
        HashMap<String, String> map = new HashMap<>();
        map.put("capacity", String.valueOf(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)));
        map.put("charge_counter", String.valueOf(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)));
        map.put("current_average", String.valueOf(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)));
        map.put("current_now", String.valueOf(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)));
        map.put("energy_counter", String.valueOf(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER)));
        return map;
    }

    @Override
    public long batteryTime() {
        if (Build.VERSION.SDK_INT >= 28) {
            return batteryManager.computeChargeTimeRemaining();
        } else {
            return 0;
        }
    }

    class BroadcastAppReceiver extends BroadcastReceiver {

        public void BroadcastAppReceiver(){
            // constructor
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            if (success) {
                scanSuccess();
            } else {
                // scan failure handling
                scanFailure();
            }
        }
    }

}
