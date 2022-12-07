package com.asuslife.sampleapps.blesampleomnicare.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.asuslife.omnicaresdk.sync.SyncResultData;
import com.asuslife.sampleapps.blesampleomnicare.R;
import com.asuslife.sampleapps.blesampleomnicare.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class DataDisplayDialogFragment extends DialogFragment {

    public static final String SYNC_RESULT = "sync_result";

    private String TAG = DataDisplayDialogFragment.class.getSimpleName();

    private View mRootView;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_data_display, container, false);

        viewPager = (ViewPager) mRootView.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) mRootView.findViewById(R.id.tabs);
        setViewPager();

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new ActivityDataFragment(), "Activity", syncResultData);
        viewPagerAdapter.addFragment(new BloodGlucoseDataFragment(), "BloodGlucose", syncResultData);
        viewPagerAdapter.addFragment(new BloodPressureDataFragment(), "BloodPressure", syncResultData);
        viewPagerAdapter.addFragment(new BodyTemperatureDataFragment(), "BodyTemperature", syncResultData);
        viewPagerAdapter.addFragment(new AltitudeDataFragment(), "Altitude", syncResultData);
        viewPagerAdapter.addFragment(new GsensorDataFragment(), "Gsensor", syncResultData);
        viewPagerAdapter.addFragment(new AirPressureDataFragment(), "AirPressure", syncResultData);
        viewPagerAdapter.addFragment(new ECGDataFragment(), "ECG", syncResultData);
        viewPagerAdapter.addFragment(new PPGDataFragment(), "PPG", syncResultData);
        viewPagerAdapter.addFragment(new HRVDataFragment(), "HRV", syncResultData);
        viewPagerAdapter.addFragment(new SleepDataFragment(), "Sleep", syncResultData);
        viewPagerAdapter.addFragment(new SPO2DataFragment(), "SPO2", syncResultData);
        viewPagerAdapter.addFragment(new LocationDataFragment(), "Location", syncResultData);
        viewPagerAdapter.addFragment(new PHDataFragment(), "PH", syncResultData);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }
}
