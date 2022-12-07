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
import com.asuslife.sampleapps.blesampleomnicare.adapter.ActivityDataListAdapter;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

public class ActivityDataFragment extends Fragment {

    public static final String ACTIVITY = "activity";

    private String TAG = ActivityDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView activityDataList;
    private ActivityDataListAdapter activityDataListAdapter;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_activity_data, container, false);

        activityDataListAdapter = new ActivityDataListAdapter(syncResultData.getActivityEntries());
        activityDataList = (RecyclerView) mRootView.findViewById(R.id.rv_activity_list);
        activityDataList.setHasFixedSize(true);
        activityDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        activityDataList.setNestedScrollingEnabled(true);
        activityDataList.setAdapter(activityDataListAdapter);

        return mRootView;
    }
}
