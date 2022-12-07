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
import com.asuslife.sampleapps.blesampleomnicare.adapter.SleepDataListAdapter;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

public class SleepDataFragment extends Fragment {

    private String TAG = SleepDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView sleepDataList;
    private SleepDataListAdapter sleepDataListAdapter;

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

        sleepDataListAdapter = new SleepDataListAdapter(syncResultData.getSleepEntries());
        sleepDataList = (RecyclerView) mRootView.findViewById(R.id.rv_sleep_list);
        sleepDataList.setHasFixedSize(true);
        sleepDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        sleepDataList.setNestedScrollingEnabled(true);
        sleepDataList.setAdapter(sleepDataListAdapter);

        return mRootView;
    }
}
