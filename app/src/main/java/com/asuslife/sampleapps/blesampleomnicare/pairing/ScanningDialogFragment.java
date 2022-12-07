package com.asuslife.sampleapps.blesampleomnicare.pairing;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.asuslife.omnicaresdk.AsusLifeDeviceScanCallback;
import com.asuslife.omnicaresdk.AsusLifeOmniCare;
import com.asuslife.omnicaresdk.AuthCompletion;
import com.asuslife.omnicaresdk.Device;
import com.asuslife.omnicaresdk.DeviceManager;
import com.asuslife.omnicaresdk.PairingCallback;
import com.asuslife.omnicaresdk.ScannedDevice;
import com.asuslife.omnicaresdk.bluetooth.PairingFailedException;
import com.asuslife.sampleapps.blesampleomnicare.R;
import com.asuslife.sampleapps.blesampleomnicare.adapter.ScannedDevicesListAdapter;
import com.asuslife.sampleapps.blesampleomnicare.util.MyProgressDialog;

public class ScanningDialogFragment extends DialogFragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = ScanningDialogFragment.class.getSimpleName();

    private View mRootView;

    private DeviceManager deviceManager;

    private MyProgressDialog myProgressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView scannedDevicesList;
    private ScannedDevicesListAdapter scannedDevicesListAdapter;

    private AsusLifeDeviceScanCallback asusLifeDeviceScanCallback = new AsusLifeDeviceScanCallback() {
        @Override
        public void onScannedDevice(ScannedDevice device) {
            scannedDevicesListAdapter.addDevice(device);
        }

        @Override
        public void onScanStarted() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        public void onScanFinished() {
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onScanFailed() {
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private PairingCallback pairingCallback = new PairingCallback() {
        @Override
        public void pairingFailed(PairingFailedException cause) {
            myProgressDialog.dismiss();
            showToast(cause.getMessage());
        }

        @Override
        public void pairingSucceeded(Device device) {
            myProgressDialog.dismiss();
            showToast(R.string.pairing_succeeded);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        }

        @Override
        public void authRequested(AuthCompletion authCompletion) {
            //This callback only support Garmin devices currently

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.enter_passkey);

            final EditText input = new EditText(getContext());

            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            builder.setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
                String text = input.getText().toString();
                int passkey = Integer.parseInt(text);
                authCompletion.setPasskey(passkey);
            });

            builder.setNegativeButton(R.string.dialog_no, (dialog, which) -> dialog.cancel());
            builder.setCancelable(false);

            builder.show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_scanning, container, false);

        deviceManager = AsusLifeOmniCare.getDeviceManager();

        scannedDevicesListAdapter = new ScannedDevicesListAdapter();
        scannedDevicesListAdapter.setOnItemClickListener(new ScannedDevicesListAdapter.ClickListener() {
            @Override
            public void onItemClickListener(ScannedDevice scannedDevice) {
                deviceManager.stopScan(asusLifeDeviceScanCallback);

                myProgressDialog = new MyProgressDialog(getActivity(), "", getActivity().getResources().getString(R.string.pairing));
                myProgressDialog.show(false);

                deviceManager.requestDevicePair(scannedDevice, pairingCallback);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        scannedDevicesList = (RecyclerView) mRootView.findViewById(R.id.rv_scanned_list);
        scannedDevicesList.setHasFixedSize(true);
        scannedDevicesList.setLayoutManager(layoutManager);
        scannedDevicesList.setAdapter(scannedDevicesListAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.layout_smart_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        onRefresh();

        return mRootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        deviceManager.stopScan(asusLifeDeviceScanCallback);
    }

    @Override
    public void onRefresh() {
        scannedDevicesListAdapter.close();
        deviceManager.startScan(asusLifeDeviceScanCallback, 5000);//refresh and waitting time
    }

    public void showToast(int res) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showToast(String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
