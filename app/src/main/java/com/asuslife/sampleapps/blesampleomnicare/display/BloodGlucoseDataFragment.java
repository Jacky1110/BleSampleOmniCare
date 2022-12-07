package com.asuslife.sampleapps.blesampleomnicare.display;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asuslife.omnicaresdk.sync.SyncResultData;
import com.asuslife.sampleapps.blesampleomnicare.R;
import com.asuslife.sampleapps.blesampleomnicare.adapter.BloodGlucoseDataListAdapter;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

public class BloodGlucoseDataFragment extends Fragment {

    private String TAG = BloodGlucoseDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView bloodGlucoseDataList;
    private BloodGlucoseDataListAdapter bloodGlucoseDataListAdapter;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_blood_glucose_data, container, false);

        bloodGlucoseDataListAdapter = new BloodGlucoseDataListAdapter(syncResultData.getBloodGlucoseEntries());
        bloodGlucoseDataList = (RecyclerView) mRootView.findViewById(R.id.rv_blood_glucose_list);
        bloodGlucoseDataList.setHasFixedSize(true);
        bloodGlucoseDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bloodGlucoseDataList.setNestedScrollingEnabled(true);
        bloodGlucoseDataList.setAdapter(bloodGlucoseDataListAdapter);

        return mRootView;
    }
}
