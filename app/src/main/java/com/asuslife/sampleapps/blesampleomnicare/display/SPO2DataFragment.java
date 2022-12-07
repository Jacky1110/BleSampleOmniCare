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
import com.asuslife.sampleapps.blesampleomnicare.adapter.SPO2DataListAdapter;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

public class SPO2DataFragment extends Fragment {

    private String TAG = SPO2DataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView spo2DataList;
    private SPO2DataListAdapter spo2DataListAdapter;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_spo2_data, container, false);

        spo2DataListAdapter = new SPO2DataListAdapter(syncResultData.getSpo2Entries());
        spo2DataList = (RecyclerView) mRootView.findViewById(R.id.rv_spo2_list);
        spo2DataList.setHasFixedSize(true);
        spo2DataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        spo2DataList.setNestedScrollingEnabled(true);
        spo2DataList.setAdapter(spo2DataListAdapter);

        return mRootView;
    }
}
