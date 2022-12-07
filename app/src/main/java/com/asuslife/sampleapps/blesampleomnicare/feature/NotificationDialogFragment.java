package com.asuslife.sampleapps.blesampleomnicare.feature;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.asuslife.omnicaresdk.AsusLifeOmniCare;
import com.asuslife.omnicaresdk.Device;
import com.asuslife.omnicaresdk.DeviceCommunicationCallback;
import com.asuslife.omnicaresdk.DeviceConnectionStateListener;
import com.asuslife.omnicaresdk.DeviceManager;
import com.asuslife.omnicaresdk.VivoWatch5AdvancedFeature;
import com.asuslife.omnicaresdk.VivoWatchBPAdvancedFeature;
import com.asuslife.omnicaresdk.VivoWatchBPDeviceOption;
import com.asuslife.omnicaresdk.VivoWatchBPDeviceOptionCallback;
import com.asuslife.omnicaresdk.VivoWatchSPAdvancedFeature;
import com.asuslife.omnicaresdk.VivoWatchSPDeviceOption;
import com.asuslife.omnicaresdk.VivoWatchSPDeviceOptionCallback;
import com.asuslife.omnicaresdk.bluetooth.DeviceCommunicationException;
import com.asuslife.omnicaresdk.bluetooth.DeviceConnectionFailedException;
import com.asuslife.sampleapps.blesampleomnicare.R;
import com.asuslife.sampleapps.blesampleomnicare.util.MyProgressDialog;
import com.asuslife.sampleapps.blesampleomnicare.util.SharedPreferencesHelper;

import static com.asuslife.sampleapps.blesampleomnicare.devices.DevicesInfoDialogFragment.DEVICE;

public class NotificationDialogFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener, DeviceConnectionStateListener {

    private String TAG = NotificationDialogFragment.class.getSimpleName();

    private View mRootView;

    private DeviceManager deviceManager;
    private Device device;

    private SharedPreferencesHelper sharedPreferencesHelper;

    private Switch swGlobalVibration, swTimeToMoveReminder, swIncomingCallNotify, swIncomingMessageNotify;

    private MyProgressDialog myProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device = (Device) getArguments().getSerializable(DEVICE);
        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_notification, container, false);
        swGlobalVibration = (Switch) mRootView.findViewById(R.id.sw_global_vibration);
        swGlobalVibration.setOnCheckedChangeListener(this);

        swTimeToMoveReminder = (Switch) mRootView.findViewById(R.id.sw_time_to_move_reminder);
        swTimeToMoveReminder.setOnCheckedChangeListener(this);

        swIncomingCallNotify = (Switch) mRootView.findViewById(R.id.sw_incoming_call_notify);
        swIncomingCallNotify.setOnCheckedChangeListener(this);

        swIncomingMessageNotify = (Switch) mRootView.findViewById(R.id.sw_incoming_message_notify);
        swIncomingMessageNotify.setOnCheckedChangeListener(this);

        deviceManager = AsusLifeOmniCare.getDeviceManager();
        deviceManager.addConnectionStateListener(this);

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deviceManager.removeConnectionStateListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDeviceOptionSettings();
    }

    private void loadDeviceOptionSettings() {
        showProgressDialog(getActivity().getResources().getString(R.string.syncing));

        switch (device.deviceModel()) {
            case VIVOWATCH_BP:
                ((VivoWatchBPAdvancedFeature) device.advancedFeature()).getDeviceOptions(new VivoWatchBPDeviceOptionCallback() {
                    @Override
                    public void onCommunicationSucceeded(Device device, VivoWatchBPDeviceOption data) {
                        Log.d(TAG, "onCommunicationSucceeded: ");
                        setSwitchEnable(swGlobalVibration, data.isGlobalVibration);
                        setSwitchEnable(swTimeToMoveReminder, data.isTimeToMoveReminder);
                        setSwitchEnable(swIncomingCallNotify, data.isIncomingCallNotify);
                        setSwitchEnable(swIncomingMessageNotify, data.isIncomingMessageNotify);
                        clearProgressDialog();
                    }

                    @Override
                    public void onCommunicationFailed(Device device, DeviceCommunicationException cause) {
                        Log.d(TAG, "onCommunicationFailed: ");
                        clearProgressDialog();
                        showToast(cause.getMessage());
                    }
                });
                break;
            case VIVOWATCH_SP:
                ((VivoWatchSPAdvancedFeature) device.advancedFeature()).getDeviceOptions(new VivoWatchSPDeviceOptionCallback() {
                    @Override
                    public void onCommunicationSucceeded(Device device, VivoWatchSPDeviceOption data) {
                        Log.d(TAG, "onCommunicationSucceeded: ");
                        setSwitchEnable(swGlobalVibration, data.isGlobalVibration);
                        setSwitchEnable(swTimeToMoveReminder, data.isTimeToMoveReminder);
                        setSwitchEnable(swIncomingCallNotify, data.isIncomingCallNotify);
                        setSwitchEnable(swIncomingMessageNotify, data.isIncomingMessageNotify);
                        clearProgressDialog();
                    }

                    @Override
                    public void onCommunicationFailed(Device device, DeviceCommunicationException cause) {
                        Log.d(TAG, "onCommunicationFailed: ");
                        clearProgressDialog();
                        showToast(cause.getMessage());
                    }
                });
                break;
            case VIVOWATCH_5:
                ((VivoWatch5AdvancedFeature) device.advancedFeature()).getDeviceOptions(new VivoWatchSPDeviceOptionCallback() {
                    @Override
                    public void onCommunicationSucceeded(Device device, VivoWatchSPDeviceOption data) {
                        Log.d(TAG, "onCommunicationSucceeded: ");
                        setSwitchEnable(swGlobalVibration, data.isGlobalVibration);
                        setSwitchEnable(swTimeToMoveReminder, data.isTimeToMoveReminder);
                        setSwitchEnable(swIncomingCallNotify, data.isIncomingCallNotify);
                        setSwitchEnable(swIncomingMessageNotify, data.isIncomingMessageNotify);
                        clearProgressDialog();
                    }

                    @Override
                    public void onCommunicationFailed(Device device, DeviceCommunicationException cause) {
                        Log.d(TAG, "onCommunicationFailed: ");
                        clearProgressDialog();
                        showToast(cause.getMessage());
                    }
                });
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int viewId = buttonView.getId();
        switch (viewId) {
            case R.id.sw_global_vibration:
                if (buttonView.isPressed() && isChecked) {
                    //turn on global vibration
                    showProgressDialog(getActivity().getResources().getString(R.string.syncing));

                    switch (device.deviceModel()) {
                        case VIVOWATCH_BP:
                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).setGlobalVibrationOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swGlobalVibration, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_SP:
                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).setGlobalVibrationOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swGlobalVibration, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_5:
                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).setGlobalVibrationOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swGlobalVibration, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                    }

                } else if (buttonView.isPressed() && !isChecked) {
                    //turn off global vibration
                    showProgressDialog(getActivity().getResources().getString(R.string.syncing));

                    switch (device.deviceModel()) {
                        case VIVOWATCH_BP:
                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).setGlobalVibrationOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swGlobalVibration, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_SP:
                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).setGlobalVibrationOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swGlobalVibration, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_5:
                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).setGlobalVibrationOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swGlobalVibration, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                    }

                }
                break;
            case R.id.sw_time_to_move_reminder:
                if (buttonView.isPressed() && isChecked) {
                    //turn on time to move reminder
                    showProgressDialog(getActivity().getResources().getString(R.string.syncing));

                    switch (device.deviceModel()) {
                        case VIVOWATCH_BP:
                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).setTimeToMoveReminderOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swTimeToMoveReminder, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_SP:
                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).setTimeToMoveReminderOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swTimeToMoveReminder, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_5:
                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).setTimeToMoveReminderOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swTimeToMoveReminder, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                    }

                } else if (buttonView.isPressed() && !isChecked) {
                    //turn off time to move reminder
                    showProgressDialog(getActivity().getResources().getString(R.string.syncing));

                    switch (device.deviceModel()) {
                        case VIVOWATCH_BP:
                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).setTimeToMoveReminderOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swTimeToMoveReminder, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_SP:
                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).setTimeToMoveReminderOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swTimeToMoveReminder, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_5:
                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).setTimeToMoveReminderOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swTimeToMoveReminder, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                    }

                }
                break;
            case R.id.sw_incoming_call_notify:
                if (buttonView.isPressed() && isChecked) {
                    //turn on incoming call notify
                    showProgressDialog(getActivity().getResources().getString(R.string.syncing));

                    switch (device.deviceModel()) {
                        case VIVOWATCH_BP:
                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).setIncomingCallNotifyOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingCallNotify, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_SP:
                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).setIncomingCallNotifyOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingCallNotify, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_5:
                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).setIncomingCallNotifyOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingCallNotify, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                    }

                } else if (buttonView.isPressed() && !isChecked) {
                    //turn off incoming call notify
                    showProgressDialog(getActivity().getResources().getString(R.string.syncing));

                    switch (device.deviceModel()) {
                        case VIVOWATCH_BP:
                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).setIncomingCallNotifyOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingCallNotify, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_SP:
                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).setIncomingCallNotifyOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingCallNotify, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_5:
                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).setIncomingCallNotifyOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingCallNotify, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                    }

                }
                break;
            case R.id.sw_incoming_message_notify:
                if (buttonView.isPressed() && isChecked) {
                    //turn on incoming message notify
                    showProgressDialog(getActivity().getResources().getString(R.string.syncing));

                    switch (device.deviceModel()) {
                        case VIVOWATCH_BP:
                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).setIncomingMessageNotifyOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingMessageNotify, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_SP:
                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).setIncomingMessageNotifyOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingMessageNotify, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_5:
                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).setIncomingMessageNotifyOption(true, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingMessageNotify, false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                    }

                } else if (buttonView.isPressed() && !isChecked) {
                    //turn off incoming message notify
                    showProgressDialog(getActivity().getResources().getString(R.string.syncing));

                    switch (device.deviceModel()) {
                        case VIVOWATCH_BP:
                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).setIncomingMessageNotifyOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingMessageNotify, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_SP:
                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).setIncomingMessageNotifyOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingMessageNotify, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                        case VIVOWATCH_5:
                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).setIncomingMessageNotifyOption(false, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setSwitchEnable(swIncomingMessageNotify, true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }
                            });
                            break;
                    }
                }
                break;
        }
    }

    private void showProgressDialog(String msg) {
        myProgressDialog = new MyProgressDialog(getActivity(), "", msg);
        myProgressDialog.show(false);
    }

    private void clearProgressDialog() {
        if (myProgressDialog != null && myProgressDialog.isShowing())
            myProgressDialog.dismiss();
    }

    private void showToast(int res) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showToast(String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSwitchEnable(Switch s, boolean b) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                s.setChecked(b);
            }
        });
    }

    @Override
    public void onDeviceConnected(Device device) {
        Log.d(TAG, "onDeviceConnected: ");
    }

    @Override
    public void onDeviceConnectionFailed(Device device, DeviceConnectionFailedException dcfe) {
        Log.d(TAG, "onDeviceConnectionFailed: " + dcfe.getMessage());
        clearProgressDialog();
        showToast(dcfe.getMessage());
    }

    @Override
    public void onDeviceDisconnected(Device device) {
        Log.d(TAG, "onDeviceDisconnected: ");
        clearProgressDialog();
    }
}
