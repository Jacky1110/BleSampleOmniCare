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
import com.asuslife.sampleapps.blesampleomnicare.adapter.LocationDataListAdapter;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

public class LocationDataFragment extends Fragment {

    private String TAG = LocationDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView locationDataList;
    private LocationDataListAdapter locationDataListAdapter;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_location_data, container, false);

        locationDataListAdapter = new LocationDataListAdapter(syncResultData.getLocationEntries());
        locationDataList = (RecyclerView) mRootView.findViewById(R.id.rv_location_list);
        locationDataList.setHasFixedSize(true);
        locationDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        locationDataList.setNestedScrollingEnabled(true);
        locationDataList.setAdapter(locationDataListAdapter);

        return mRootView;
    }
}
