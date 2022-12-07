package com.asuslife.sampleapps.blesampleomnicare.display;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asuslife.omnicaresdk.sync.SyncResultData;
import com.asuslife.sampleapps.blesampleomnicare.R;
import com.asuslife.sampleapps.blesampleomnicare.adapter.AirPressureDataListAdapter;

//大氣壓力
public class AirPressureDataFragment extends Fragment {

    private String TAG = AirPressureDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView airPressureDataList;
    private AirPressureDataListAdapter airPressureDataListAdapter;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sleep_data, container, false);

        airPressureDataListAdapter = new AirPressureDataListAdapter(syncResultData.getAirPressureEntries());
        airPressureDataList = (RecyclerView) mRootView.findViewById(R.id.rv_sleep_list);
        airPressureDataList.setHasFixedSize(true);
        airPressureDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        airPressureDataList.setNestedScrollingEnabled(true);
        airPressureDataList.setAdapter(airPressureDataListAdapter);

        return mRootView;
    }
}
