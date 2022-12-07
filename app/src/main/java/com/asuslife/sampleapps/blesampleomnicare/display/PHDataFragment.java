package com.asuslife.sampleapps.blesampleomnicare.display;

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
import com.asuslife.sampleapps.blesampleomnicare.adapter.LocationDataListAdapter;
import com.asuslife.sampleapps.blesampleomnicare.adapter.PHDataListAdapter;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

public class PHDataFragment extends Fragment {

    private String TAG = PHDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView phDataList;
    private PHDataListAdapter phDataListAdapter;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_ph_data, container, false);

        phDataListAdapter = new PHDataListAdapter(syncResultData.getPhEntries());
        phDataList = (RecyclerView) mRootView.findViewById(R.id.rv_ph_list);
        phDataList.setHasFixedSize(true);
        phDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        phDataList.setNestedScrollingEnabled(true);
        phDataList.setAdapter(phDataListAdapter);

        return mRootView;
    }
}
