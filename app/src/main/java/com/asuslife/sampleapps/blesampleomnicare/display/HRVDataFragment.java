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
import com.asuslife.sampleapps.blesampleomnicare.adapter.HRVDataListAdapter;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

public class HRVDataFragment extends Fragment {

    private String TAG = HRVDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView hrvDataList;
    private HRVDataListAdapter hrvDataListAdapter;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_hrv_data, container, false);

        hrvDataListAdapter = new HRVDataListAdapter(syncResultData.getHrvEntries());
        hrvDataList = (RecyclerView) mRootView.findViewById(R.id.rv_hrv_list);
        hrvDataList.setHasFixedSize(true);
        hrvDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        hrvDataList.setNestedScrollingEnabled(true);
        hrvDataList.setAdapter(hrvDataListAdapter);

        return mRootView;
    }
}
