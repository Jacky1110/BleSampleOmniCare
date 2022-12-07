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
import com.asuslife.sampleapps.blesampleomnicare.adapter.PPGDataListAdapter;

import static com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment.SYNC_RESULT;

public class PPGDataFragment extends Fragment {

    private String TAG = PPGDataFragment.class.getSimpleName();

    private View mRootView;

    private RecyclerView ppgDataList;
    private PPGDataListAdapter ppgDataListAdapter;

    private SyncResultData syncResultData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncResultData = (SyncResultData) getArguments().getSerializable(SYNC_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_ppg_data, container, false);

        ppgDataListAdapter = new PPGDataListAdapter(syncResultData.getPpgEntries());
        ppgDataList = (RecyclerView) mRootView.findViewById(R.id.rv_ppg_list);
        ppgDataList.setHasFixedSize(true);
        ppgDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ppgDataList.setNestedScrollingEnabled(true);
        ppgDataList.setAdapter(ppgDataListAdapter);

        return mRootView;
    }
}
