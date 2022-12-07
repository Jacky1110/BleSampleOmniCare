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
import com.asuslife.sampleapps.blesampleomnicare.adapter.ECGDataListAdapter;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

public class ECGDataFragment extends Fragment {

    private String TAG = ECGDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView ecgDataList;
    private ECGDataListAdapter ecgDataListAdapter;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_ecg_data, container, false);

        ecgDataListAdapter = new ECGDataListAdapter(syncResultData.getEcgEntries());
        ecgDataList = (RecyclerView) mRootView.findViewById(R.id.rv_ecg_list);
        ecgDataList.setHasFixedSize(true);
        ecgDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ecgDataList.setNestedScrollingEnabled(true);
        ecgDataList.setAdapter(ecgDataListAdapter);

        return mRootView;
    }
}
