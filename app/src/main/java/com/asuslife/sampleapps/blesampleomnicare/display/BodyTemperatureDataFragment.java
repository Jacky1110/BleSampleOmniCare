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
import com.asuslife.sampleapps.blesampleomnicare.adapter.BodyTemperatureDataListAdapter;

//BodyTemperature
public class BodyTemperatureDataFragment extends Fragment {

    private String TAG = BodyTemperatureDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView bodyTemperatureDataList;
    private BodyTemperatureDataListAdapter bodyTemperatureDataListAdapter;

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

        bodyTemperatureDataListAdapter = new BodyTemperatureDataListAdapter(syncResultData.getBodyTemperatureEntries());
        bodyTemperatureDataList = (RecyclerView) mRootView.findViewById(R.id.rv_sleep_list);
        bodyTemperatureDataList.setHasFixedSize(true);
        bodyTemperatureDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bodyTemperatureDataList.setNestedScrollingEnabled(true);
        bodyTemperatureDataList.setAdapter(bodyTemperatureDataListAdapter);

        return mRootView;
    }
}
