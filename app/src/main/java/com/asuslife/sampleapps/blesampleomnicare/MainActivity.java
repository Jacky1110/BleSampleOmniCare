package com.asuslife.sampleapps.blesampleomnicare;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.legacy.app.ActivityCompat;

import android.util.Log;
import android.widget.Toast;

import com.asuslife.omnicaresdk.AsusLifeOmniCare;
import com.asuslife.omnicaresdk.AsusLifeOmniCareInitializationCallback;
import com.asuslife.omnicaresdk.AsusLifeOmniCareInitializationException;
import com.asuslife.sampleapps.blesampleomnicare.devices.PairedDevicesDialogFragment;
import com.asuslife.sampleapps.blesampleomnicare.util.ConfirmationDialog;
import com.asuslife.sampleapps.blesampleomnicare.util.MyProgressDialog;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private BluetoothAdapter bluetoothAdapter;

    private MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!verifyPermissions()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, REQUEST_COARSE_LOCATION);
        } else
            verifyLocationServices();

        if (verifyBluetooth()) {
            requestBluetooth();
        }

        showProgressDialog(this.getResources().getString(R.string.waiting));
        //If you want to enable Garmin service, please remember to pass the license key to this function
        AsusLifeOmniCare.initialize(getApplicationContext(), "<account>", "<password>", new AsusLifeOmniCareInitializationCallback() {
            @Override
            public void onSuccess() {
                pairedDevicesTransition();
                clearProgressDialog();
            }

            @Override
            public void onFailed(AsusLifeOmniCareInitializationException e) {
                Log.e(TAG, Log.getStackTraceString(e));
                clearProgressDialog();
                showToast(e.getMessage());
                finishAndRemoveTask();
            }
        });
    }

    private static final int REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    verifyLocationServices();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.loc_service_unavailable, Toast.LENGTH_LONG).show();
                    finishAndRemoveTask();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_CANCELED) {
                    finishAndRemoveTask();
                }
                break;
        }
    }

    private boolean verifyPermissions() {

        int locationPermission = checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocationPermission = checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);//some phone need add this
        int storagePermission = checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int phoneStatePermission = checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE);
        int callLogPermission = checkCallingOrSelfPermission(Manifest.permission.READ_CALL_LOG);
        int readContactPermission = checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS);

        return (locationPermission == PackageManager.PERMISSION_GRANTED) && (fineLocationPermission == PackageManager.PERMISSION_GRANTED) && (storagePermission == PackageManager.PERMISSION_GRANTED) && (phoneStatePermission == PackageManager.PERMISSION_GRANTED) && (callLogPermission == PackageManager.PERMISSION_GRANTED) && (readContactPermission == PackageManager.PERMISSION_GRANTED);
    }

    private boolean verifyBluetooth() {
        bluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), R.string.ble_not_supported, Toast.LENGTH_LONG).show();
            finishAndRemoveTask();
            return false;
        }
        return true;
    }

    private void requestBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void verifyLocationServices() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        ConfirmationDialog enableGpsConfirm = new ConfirmationDialog(MainActivity.this, "", getString(R.string.require_gps), getString(R.string.response_yes),
                getString(R.string.response_no), new enableGpsClickListener());
        enableGpsConfirm.show();
    }

    public void showToast(String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(String msg) {
        myProgressDialog = new MyProgressDialog(MainActivity.this, "", msg);
        myProgressDialog.show(false);
    }

    private void clearProgressDialog() {
        if (myProgressDialog != null && myProgressDialog.isShowing())
            myProgressDialog.dismiss();
    }

    private void pairedDevicesTransition() {
        PairedDevicesDialogFragment pairedDevicesDialogFragment = new PairedDevicesDialogFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(R.id.main_root, pairedDevicesDialogFragment, pairedDevicesDialogFragment.getTag()).commitAllowingStateLoss();
    }

    private class enableGpsClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } else {
                dialog.cancel();
                Toast.makeText(getApplicationContext(), R.string.loc_service_unavailable, Toast.LENGTH_LONG).show();
                finishAndRemoveTask();
            }
        }
    }
}
