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
import com.asuslife.sampleapps.blesampleomnicare.adapter.BloodPressureDataListAdapter;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

public class BloodPressureDataFragment extends Fragment {

    private String TAG = BloodPressureDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView bloodPressureDataList;
    private BloodPressureDataListAdapter bloodPressureDataListAdapter;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_blood_pressure_data, container, false);

        bloodPressureDataListAdapter = new BloodPressureDataListAdapter(syncResultData.getBloodPressureEntries());
        bloodPressureDataList = (RecyclerView) mRootView.findViewById(R.id.rv_blood_pressure_list);
        bloodPressureDataList.setHasFixedSize(true);
        bloodPressureDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bloodPressureDataList.setNestedScrollingEnabled(true);
        bloodPressureDataList.setAdapter(bloodPressureDataListAdapter);

        return mRootView;
    }
}
