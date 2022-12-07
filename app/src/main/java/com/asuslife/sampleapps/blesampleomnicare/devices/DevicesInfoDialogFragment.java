package com.asuslife.sampleapps.blesampleomnicare.devices;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.asuslife.omnicaresdk.AsusLifeOmniCare;
import com.asuslife.omnicaresdk.Completion;
import com.asuslife.omnicaresdk.Device;
import com.asuslife.omnicaresdk.DeviceCommunicationCallback;
import com.asuslife.omnicaresdk.DeviceConnectionStateListener;
import com.asuslife.omnicaresdk.DeviceFirmwareUpdateCallback;
import com.asuslife.omnicaresdk.DeviceManager;
import com.asuslife.omnicaresdk.OmniCareAPI;
import com.asuslife.omnicaresdk.VioletFirmwareInfo;
import com.asuslife.omnicaresdk.VivoWatch5AdvancedFeature;
import com.asuslife.omnicaresdk.VivoWatchBPAdvancedFeature;
import com.asuslife.omnicaresdk.VivoWatchBPDeviceParameter;
import com.asuslife.omnicaresdk.VivoWatchBPDeviceParameterCallback;
import com.asuslife.omnicaresdk.VivoWatchSPAdvancedFeature;
import com.asuslife.omnicaresdk.VivoWatchSPDeviceParameter;
import com.asuslife.omnicaresdk.VivoWatchSPDeviceParameterCallback;
import com.asuslife.omnicaresdk.bluetooth.DeviceCommunicationException;
import com.asuslife.omnicaresdk.bluetooth.DeviceConnectionFailedException;
import com.asuslife.omnicaresdk.settings.BackgroundPulseTransitTimeChecksFrequency;
import com.asuslife.omnicaresdk.sync.DataRequestException;
import com.asuslife.omnicaresdk.sync.SyncResultData;
import com.asuslife.sampleapps.blesampleomnicare.R;
import com.asuslife.sampleapps.blesampleomnicare.display.DataDisplayDialogFragment;
import com.asuslife.sampleapps.blesampleomnicare.feature.NotificationDialogFragment;
import com.asuslife.sampleapps.blesampleomnicare.feature.PillReminderDialogFragment;
import com.asuslife.sampleapps.blesampleomnicare.util.MyProgressDialog;
import com.asuslife.sampleapps.blesampleomnicare.util.SharedPreferencesHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class DevicesInfoDialogFragment extends DialogFragment implements DeviceConnectionStateListener {

    public static final String DEVICE = "device";

    private String TAG = DevicesInfoDialogFragment.class.getSimpleName();

    private View mRootView;

    private DeviceManager deviceManager;
    private Device device;

    private SharedPreferencesHelper sharedPreferencesHelper;

    private Switch swBackgroundBpMeasurement;
    private Spinner spinnerFrequencyOfChecks;
    private RelativeLayout layoutCalibration, layoutPillReminder, layoutFirmwareUpdate, layoutNotification;
    private EditText etTime, etEndTime;
    private ImageView ivTime, ivEndTime;
    private Button btnRequest, btnRequestLocal;

    private boolean isFrequencyOfChecksSpinnerTapped = true;

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
        mRootView = inflater.inflate(R.layout.fragment_devices_info, container, false);

        deviceManager = AsusLifeOmniCare.getDeviceManager();
        deviceManager.addConnectionStateListener(this);

        switch (device.deviceModel()) {
            case VIVOWATCH_BP:
                initVivoWatchBPView();
                loadVivoWatchBPDeviceParameterSettings();
                break;
            case VIVOWATCH_SP:
                initVivoWatchSPView();
                loadVivoWatchSPDeviceParameterSettings();
                break;
            case VIVOWATCH_5:
                initVivoWatch5View();
                loadVivoWatch5DeviceParameterSettings();
                break;
            case VIVOSMART_4:
                initVivoSmart4View();
                break;
            case GERD_CAP:
                initCapsuleView();
                break;
        }

        etTime = (EditText) mRootView.findViewById(R.id.et_time);
        ivTime = (ImageView) mRootView.findViewById(R.id.iv_time);

        ivTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePicker(new TimestampSetListener() {
                    @Override
                    public void onDateTimeSet(String dateTime) {
                        etTime.setText(dateTime);
                    }
                }, getActivity().getFragmentManager());
            }
        });

        etEndTime = (EditText) mRootView.findViewById(R.id.et_end_time);
        ivEndTime = (ImageView) mRootView.findViewById(R.id.iv_end_time);

        ivEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePicker(new TimestampSetListener() {
                    @Override
                    public void onDateTimeSet(String dateTime) {
                        etEndTime.setText(dateTime);
                    }
                }, getActivity().getFragmentManager());
            }
        });

        btnRequest = (Button) mRootView.findViewById(R.id.btn_request);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog(getActivity().getResources().getString(R.string.waiting), false);
                OmniCareAPI.getDataByDevcie(device, etTime.getText().toString(), etEndTime.getText().toString(), new OmniCareAPI.DataRequestCallback() {
                    @Override
                    public void onSuccess(SyncResultData syncResultData) {
                        Log.d(TAG, "onSuccess: ");

                        DataDisplayDialogFragment dataDisplayDialogFragment = new DataDisplayDialogFragment();
                        Bundle args = new Bundle();
                        args.putSerializable(DataDisplayDialogFragment.SYNC_RESULT, syncResultData);
                        dataDisplayDialogFragment.setArguments(args);

                        androidx.fragment.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.replace(R.id.main_root, dataDisplayDialogFragment, dataDisplayDialogFragment.getTag()).addToBackStack(getTag()).commit();

                        clearProgressDialog();

                    }

                    @Override
                    public void onError(DataRequestException dre) {
                        Log.d(TAG, "onError: " + dre.getMessage());
                        clearProgressDialog();
                        showToast(dre.getMessage());
                    }
                });

            }
        });

        btnRequestLocal = (Button) mRootView.findViewById(R.id.btn_request_local);
        btnRequestLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog(getActivity().getResources().getString(R.string.waiting), false);
                OmniCareAPI.getLocalDataByDevcie(device, etTime.getText().toString(), etEndTime.getText().toString(), new OmniCareAPI.DataRequestCallback() {
                    @Override
                    public void onSuccess(SyncResultData syncResultData) {
                        Log.d(TAG, "onSuccess: ");

                        DataDisplayDialogFragment dataDisplayDialogFragment = new DataDisplayDialogFragment();
                        Bundle args = new Bundle();
                        args.putSerializable(DataDisplayDialogFragment.SYNC_RESULT, syncResultData);
                        dataDisplayDialogFragment.setArguments(args);

                        androidx.fragment.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.replace(R.id.main_root, dataDisplayDialogFragment, dataDisplayDialogFragment.getTag()).addToBackStack(getTag()).commit();

                        clearProgressDialog();

                    }

                    @Override
                    public void onError(DataRequestException dre) {
                        Log.d(TAG, "onError: " + dre.getMessage());
                        clearProgressDialog();
                        showToast(dre.getMessage());
                    }
                });
            }
        });

        return mRootView;
    }

    private void initVivoSmart4View() {
        ((LinearLayout) mRootView.findViewById(R.id.view_vivosmart4)).setVisibility(View.VISIBLE);

        layoutNotification = (RelativeLayout) mRootView.findViewById(R.id.layout_vivosmart4_notification);
        layoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
    }

    private void initCapsuleView() {
        ((TextView) mRootView.findViewById(R.id.tv_features)).setVisibility(View.GONE);
    }

    private void initVivoWatchBPView() {
        ((LinearLayout) mRootView.findViewById(R.id.view_vivowatch)).setVisibility(View.VISIBLE);

        spinnerFrequencyOfChecks = (Spinner) mRootView.findViewById(R.id.spinner_frequency_of_checks);
        spinnerFrequencyOfChecks.post(new Runnable() {
            @Override
            public void run() {
                spinnerFrequencyOfChecks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (isFrequencyOfChecksSpinnerTapped) {
                            Log.d(TAG, "onItemSelected: ");

                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).turnOnBackgroundBpMeasurement(BackgroundPulseTransitTimeChecksFrequency.values()[i], new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    sharedPreferencesHelper.putInt(device.deviceId() + "_backgroundBPMeasurementIndex", spinnerFrequencyOfChecks.getSelectedItemPosition());
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        } else {
                            isFrequencyOfChecksSpinnerTapped = true;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.d(TAG, "onNothingSelected: ");
                    }
                });
            }
        });


        swBackgroundBpMeasurement = (Switch) mRootView.findViewById(R.id.sw_background_bp_measurement);
        if (swBackgroundBpMeasurement.isChecked())
            spinnerFrequencyOfChecks.setEnabled(true);
        else
            spinnerFrequencyOfChecks.setEnabled(false);
        swBackgroundBpMeasurement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int viewId = buttonView.getId();
                switch (viewId) {
                    case R.id.sw_background_bp_measurement:
                        if (buttonView.isPressed() && isChecked) {
                            //turn on background bp measurement
                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).turnOnBackgroundBpMeasurement(BackgroundPulseTransitTimeChecksFrequency.values()[spinnerFrequencyOfChecks.getSelectedItemPosition()], new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    sharedPreferencesHelper.putInt(device.deviceId() + "_backgroundBPMeasurementIndex", spinnerFrequencyOfChecks.getSelectedItemPosition());
                                    setBackgroundBpMeasurementEnable(true);
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setBackgroundBpMeasurementEnable(false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        } else if (buttonView.isPressed() && !isChecked) {
                            //turn off background bp measurement
                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).turnOffBackgroundBpMeasurement(new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    setBackgroundBpMeasurementEnable(false);
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setBackgroundBpMeasurementEnable(true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        }
                        break;
                }
            }
        });

        layoutPillReminder = (RelativeLayout) mRootView.findViewById(R.id.layout_pill_reminder);
        layoutPillReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PillReminderDialogFragment fragment = new PillReminderDialogFragment();
                Bundle args = new Bundle();
                args.putSerializable(DevicesInfoDialogFragment.DEVICE, device);
                fragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_root, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        layoutCalibration = (RelativeLayout) mRootView.findViewById(R.id.layout_calibration);
        layoutCalibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etSys = new EditText(getActivity());
                EditText etDia = new EditText(getActivity());
                etSys.setInputType(InputType.TYPE_CLASS_NUMBER);
                etDia.setInputType(InputType.TYPE_CLASS_NUMBER);
                etSys.setHint("SYS");
                etDia.setHint("DIA");

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                linearLayout.addView(etSys);
                linearLayout.addView(etDia);

                new AlertDialog.Builder(getActivity()).setView(linearLayout).setCancelable(true).setPositiveButton(R.string.response_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int sys = Integer.parseInt(etSys.getText().toString().trim());
                            int dia = Integer.parseInt(etDia.getText().toString().trim());

                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatchBPAdvancedFeature) device.advancedFeature()).startCalibration(sys, dia, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        } catch (NumberFormatException e) {
                            showToast(R.string.toast_invalid_value);
                        }
                    }
                }).show();
            }
        });

        layoutFirmwareUpdate = (RelativeLayout) mRootView.findViewById(R.id.layout_fw_update);
        layoutFirmwareUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                ((VivoWatchBPAdvancedFeature) device.advancedFeature()).startFirmwareUpdate(new DeviceFirmwareUpdateCallback() {
                    @Override
                    public void onNewFirmwareVersionReady(VioletFirmwareInfo violetFirmwareInfo, Completion completion) {
                        Log.d(TAG, "onNewFirmwareVersionReady: ");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(getContext()).setCancelable(false).setMessage(violetFirmwareInfo.releaseNote.releaseNote).setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        clearProgressDialog();
                                        showProgressDialog(getActivity().getResources().getString(R.string.syncing), true);
                                        completion.complete();
                                    }
                                }).setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        completion.cancel();
                                    }
                                }).create().show();
                            }
                        });
                    }

                    @Override
                    public void onSyncProgress(Device device, int progress) {
                        Log.d(TAG, "onSyncProgress: ");
                        updateProgress(progress);
                    }

                    @Override
                    public void onCommunicationSucceeded(Device device) {
                        Log.d(TAG, "onCommunicationSucceeded: ");
                        clearProgressDialog();
                        showToast(R.string.fw_version_up_to_date);
                    }

                    @Override
                    public void onCommunicationFailed(Device device, DeviceCommunicationException cause) {
                        Log.d(TAG, "onCommunicationFailed: ");
                        clearProgressDialog();
                        showToast(cause.getMessage());
                    }
                });
            }
        });

        layoutNotification = (RelativeLayout) mRootView.findViewById(R.id.layout_vivobp_notification);
        layoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDialogFragment fragment = new NotificationDialogFragment();
                Bundle args = new Bundle();
                args.putSerializable(DevicesInfoDialogFragment.DEVICE, device);
                fragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_root, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void initVivoWatchSPView() {
        ((LinearLayout) mRootView.findViewById(R.id.view_vivowatch)).setVisibility(View.VISIBLE);

        spinnerFrequencyOfChecks = (Spinner) mRootView.findViewById(R.id.spinner_frequency_of_checks);
        spinnerFrequencyOfChecks.post(new Runnable() {
            @Override
            public void run() {
                spinnerFrequencyOfChecks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (isFrequencyOfChecksSpinnerTapped) {
                            Log.d(TAG, "onItemSelected: ");

                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).turnOnBackgroundBpMeasurement(BackgroundPulseTransitTimeChecksFrequency.values()[i], new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    sharedPreferencesHelper.putInt(device.deviceId() + "_backgroundBPMeasurementIndex", spinnerFrequencyOfChecks.getSelectedItemPosition());
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        } else {
                            isFrequencyOfChecksSpinnerTapped = true;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.d(TAG, "onNothingSelected: ");
                    }
                });
            }
        });


        swBackgroundBpMeasurement = (Switch) mRootView.findViewById(R.id.sw_background_bp_measurement);
        if (swBackgroundBpMeasurement.isChecked())
            spinnerFrequencyOfChecks.setEnabled(true);
        else
            spinnerFrequencyOfChecks.setEnabled(false);
        swBackgroundBpMeasurement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int viewId = buttonView.getId();
                switch (viewId) {
                    case R.id.sw_background_bp_measurement:
                        if (buttonView.isPressed() && isChecked) {
                            //turn on background bp measurement
                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).turnOnBackgroundBpMeasurement(BackgroundPulseTransitTimeChecksFrequency.values()[spinnerFrequencyOfChecks.getSelectedItemPosition()], new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    sharedPreferencesHelper.putInt(device.deviceId() + "_backgroundBPMeasurementIndex", spinnerFrequencyOfChecks.getSelectedItemPosition());
                                    setBackgroundBpMeasurementEnable(true);
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setBackgroundBpMeasurementEnable(false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        } else if (buttonView.isPressed() && !isChecked) {
                            //turn off background bp measurement
                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).turnOffBackgroundBpMeasurement(new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    setBackgroundBpMeasurementEnable(false);
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setBackgroundBpMeasurementEnable(true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        }
                        break;
                }
            }
        });

        layoutPillReminder = (RelativeLayout) mRootView.findViewById(R.id.layout_pill_reminder);
        layoutPillReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PillReminderDialogFragment fragment = new PillReminderDialogFragment();
                Bundle args = new Bundle();
                args.putSerializable(DevicesInfoDialogFragment.DEVICE, device);
                fragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_root, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        layoutCalibration = (RelativeLayout) mRootView.findViewById(R.id.layout_calibration);
        layoutCalibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etSys = new EditText(getActivity());
                EditText etDia = new EditText(getActivity());
                etSys.setInputType(InputType.TYPE_CLASS_NUMBER);
                etDia.setInputType(InputType.TYPE_CLASS_NUMBER);
                etSys.setHint("SYS");
                etDia.setHint("DIA");

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                linearLayout.addView(etSys);
                linearLayout.addView(etDia);

                new AlertDialog.Builder(getActivity()).setView(linearLayout).setCancelable(true).setPositiveButton(R.string.response_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int sys = Integer.parseInt(etSys.getText().toString().trim());
                            int dia = Integer.parseInt(etDia.getText().toString().trim());

                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatchSPAdvancedFeature) device.advancedFeature()).startCalibration(sys, dia, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        } catch (NumberFormatException e) {
                            showToast(R.string.toast_invalid_value);
                        }
                    }
                }).show();
            }
        });

        layoutFirmwareUpdate = (RelativeLayout) mRootView.findViewById(R.id.layout_fw_update);
        layoutFirmwareUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                ((VivoWatchSPAdvancedFeature) device.advancedFeature()).startFirmwareUpdate(new DeviceFirmwareUpdateCallback() {
                    @Override
                    public void onNewFirmwareVersionReady(VioletFirmwareInfo violetFirmwareInfo, Completion completion) {
                        Log.d(TAG, "onNewFirmwareVersionReady: ");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(getContext()).setCancelable(false).setMessage(violetFirmwareInfo.releaseNote.releaseNote).setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        clearProgressDialog();
                                        showProgressDialog(getActivity().getResources().getString(R.string.syncing), true);
                                        completion.complete();
                                    }
                                }).setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        completion.cancel();
                                    }
                                }).create().show();
                            }
                        });
                    }

                    @Override
                    public void onSyncProgress(Device device, int progress) {
                        Log.d(TAG, "onSyncProgress: ");
                        updateProgress(progress);
                    }

                    @Override
                    public void onCommunicationSucceeded(Device device) {
                        Log.d(TAG, "onCommunicationSucceeded: ");
                        clearProgressDialog();
                        showToast(R.string.fw_version_up_to_date);
                    }

                    @Override
                    public void onCommunicationFailed(Device device, DeviceCommunicationException cause) {
                        Log.d(TAG, "onCommunicationFailed: ");
                        clearProgressDialog();
                        showToast(cause.getMessage());
                    }
                });
            }
        });

        layoutNotification = (RelativeLayout) mRootView.findViewById(R.id.layout_vivobp_notification);
        layoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDialogFragment fragment = new NotificationDialogFragment();
                Bundle args = new Bundle();
                args.putSerializable(DevicesInfoDialogFragment.DEVICE, device);
                fragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_root, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void initVivoWatch5View() {
        Log.d(TAG, "initVivoWatch5View ...");
        ((LinearLayout) mRootView.findViewById(R.id.view_vivowatch)).setVisibility(View.VISIBLE);

        spinnerFrequencyOfChecks = (Spinner) mRootView.findViewById(R.id.spinner_frequency_of_checks);
        spinnerFrequencyOfChecks.post(new Runnable() {
            @Override
            public void run() {
                spinnerFrequencyOfChecks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (isFrequencyOfChecksSpinnerTapped) {
                            Log.d(TAG, "onItemSelected: ");

                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).turnOnBackgroundBpMeasurement(BackgroundPulseTransitTimeChecksFrequency.values()[i], new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    sharedPreferencesHelper.putInt(device.deviceId() + "_backgroundBPMeasurementIndex", spinnerFrequencyOfChecks.getSelectedItemPosition());
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        } else {
                            isFrequencyOfChecksSpinnerTapped = true;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.d(TAG, "onNothingSelected: ");
                    }
                });
            }
        });


        swBackgroundBpMeasurement = (Switch) mRootView.findViewById(R.id.sw_background_bp_measurement);
        if (swBackgroundBpMeasurement.isChecked())
            spinnerFrequencyOfChecks.setEnabled(true);
        else
            spinnerFrequencyOfChecks.setEnabled(false);
        swBackgroundBpMeasurement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int viewId = buttonView.getId();
                switch (viewId) {
                    case R.id.sw_background_bp_measurement:
                        if (buttonView.isPressed() && isChecked) {
                            //turn on background bp measurement
                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).turnOnBackgroundBpMeasurement(BackgroundPulseTransitTimeChecksFrequency.values()[spinnerFrequencyOfChecks.getSelectedItemPosition()], new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    sharedPreferencesHelper.putInt(device.deviceId() + "_backgroundBPMeasurementIndex", spinnerFrequencyOfChecks.getSelectedItemPosition());
                                    setBackgroundBpMeasurementEnable(true);
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setBackgroundBpMeasurementEnable(false);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        } else if (buttonView.isPressed() && !isChecked) {
                            //turn off background bp measurement
                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).turnOffBackgroundBpMeasurement(new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    setBackgroundBpMeasurementEnable(false);
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    setBackgroundBpMeasurementEnable(true);
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        }
                        break;
                }
            }
        });

        layoutPillReminder = (RelativeLayout) mRootView.findViewById(R.id.layout_pill_reminder);
        layoutPillReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PillReminderDialogFragment fragment = new PillReminderDialogFragment();
                Bundle args = new Bundle();
                args.putSerializable(DevicesInfoDialogFragment.DEVICE, device);
                fragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_root, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        layoutCalibration = (RelativeLayout) mRootView.findViewById(R.id.layout_calibration);
        layoutCalibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etSys = new EditText(getActivity());
                EditText etDia = new EditText(getActivity());
                etSys.setInputType(InputType.TYPE_CLASS_NUMBER);
                etDia.setInputType(InputType.TYPE_CLASS_NUMBER);
                etSys.setHint("SYS");
                etDia.setHint("DIA");

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                linearLayout.addView(etSys);
                linearLayout.addView(etDia);

                new AlertDialog.Builder(getActivity()).setView(linearLayout).setCancelable(true).setPositiveButton(R.string.response_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int sys = Integer.parseInt(etSys.getText().toString().trim());
                            int dia = Integer.parseInt(etDia.getText().toString().trim());

                            showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                            ((VivoWatch5AdvancedFeature) device.advancedFeature()).startCalibration(sys, dia, new DeviceCommunicationCallback() {
                                @Override
                                public void onCommunicationSucceeded(Device device) {
                                    Log.d(TAG, "onCommunicationSucceeded: ");
                                    clearProgressDialog();
                                }

                                @Override
                                public void onCommunicationFailed(Device device, DeviceCommunicationException dce) {
                                    Log.d(TAG, "onCommunicationFailed: " + dce.getMessage());
                                    clearProgressDialog();
                                    showToast(dce.getMessage());
                                }

                            });
                        } catch (NumberFormatException e) {
                            showToast(R.string.toast_invalid_value);
                        }
                    }
                }).show();
            }
        });

        layoutFirmwareUpdate = (RelativeLayout) mRootView.findViewById(R.id.layout_fw_update);
        layoutFirmwareUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgressDialog(getActivity().getResources().getString(R.string.syncing), false);

                ((VivoWatch5AdvancedFeature) device.advancedFeature()).startFirmwareUpdate(new DeviceFirmwareUpdateCallback() {
                    @Override
                    public void onNewFirmwareVersionReady(VioletFirmwareInfo violetFirmwareInfo, Completion completion) {
                        Log.d(TAG, "onNewFirmwareVersionReady: "+violetFirmwareInfo.mcu.version);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(getContext()).setCancelable(false).setMessage(violetFirmwareInfo.releaseNote.releaseNote).setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        clearProgressDialog();
                                        showProgressDialog(getActivity().getResources().getString(R.string.syncing), true);
                                        completion.complete();
                                    }
                                }).setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        completion.cancel();
                                    }
                                }).create().show();
                            }
                        });
                    }

                    @Override
                    public void onSyncProgress(Device device, int progress) {
                        Log.d(TAG, "onSyncProgress: ");
                        updateProgress(progress);
                    }

                    @Override
                    public void onCommunicationSucceeded(Device device) {
                        Log.d(TAG, "onCommunicationSucceeded: ");
                        clearProgressDialog();
                        showToast(R.string.fw_version_up_to_date);
                    }

                    @Override
                    public void onCommunicationFailed(Device device, DeviceCommunicationException cause) {
                        Log.d(TAG, "onCommunicationFailed: ");
                        clearProgressDialog();
                        showToast(cause.getMessage());
                    }
                });
            }
        });

        layoutNotification = (RelativeLayout) mRootView.findViewById(R.id.layout_vivobp_notification);
        layoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDialogFragment fragment = new NotificationDialogFragment();
                Bundle args = new Bundle();
                args.putSerializable(DevicesInfoDialogFragment.DEVICE, device);
                fragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_root, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void loadVivoWatchBPDeviceParameterSettings() {
        showProgressDialog(getActivity().getResources().getString(R.string.waiting), false);
        ((VivoWatchBPAdvancedFeature) device.advancedFeature()).getDeviceParameters(new VivoWatchBPDeviceParameterCallback() {
            @Override
            public void onCommunicationSucceeded(Device device, VivoWatchBPDeviceParameter data) {
                Log.d(TAG, "onCommunicationSucceeded: ");
                if (data.backgroundBPMeasurementPeriod == BackgroundPulseTransitTimeChecksFrequency.DISABLE) {
                    setBackgroundBpMeasurementEnable(false);
                    setFrequencyOfChecksSpinnerSelection(sharedPreferencesHelper.getInt(device.deviceId() + "_backgroundBPMeasurementIndex"));
                } else {
                    setBackgroundBpMeasurementEnable(true);
                    setFrequencyOfChecksSpinnerSelection(data.backgroundBPMeasurementPeriod.rawValue());
                }
                clearProgressDialog();
            }

            @Override
            public void onCommunicationFailed(Device device, DeviceCommunicationException cause) {
                Log.d(TAG, "onCommunicationFailed: " + cause.getMessage());
                clearProgressDialog();
                showToast(cause.getMessage());
            }
        });
    }

    private void loadVivoWatchSPDeviceParameterSettings() {
        showProgressDialog(getActivity().getResources().getString(R.string.waiting), false);
        ((VivoWatchSPAdvancedFeature) device.advancedFeature()).getDeviceParameters(new VivoWatchSPDeviceParameterCallback() {
            @Override
            public void onCommunicationSucceeded(Device device, VivoWatchSPDeviceParameter data) {
                Log.d(TAG, "onCommunicationSucceeded: ");
                if (data.backgroundBPMeasurementPeriod == BackgroundPulseTransitTimeChecksFrequency.DISABLE) {
                    setBackgroundBpMeasurementEnable(false);
                    setFrequencyOfChecksSpinnerSelection(sharedPreferencesHelper.getInt(device.deviceId() + "_backgroundBPMeasurementIndex"));
                } else {
                    setBackgroundBpMeasurementEnable(true);
                    setFrequencyOfChecksSpinnerSelection(data.backgroundBPMeasurementPeriod.rawValue());
                }
                clearProgressDialog();
            }

            @Override
            public void onCommunicationFailed(Device device, DeviceCommunicationException cause) {
                Log.d(TAG, "onCommunicationFailed: " + cause.getMessage());
                clearProgressDialog();
                showToast(cause.getMessage());
            }
        });
    }

    private void loadVivoWatch5DeviceParameterSettings() {
        showProgressDialog(getActivity().getResources().getString(R.string.waiting), false);
        ((VivoWatch5AdvancedFeature) device.advancedFeature()).getDeviceParameters(new VivoWatchSPDeviceParameterCallback() {
            @Override
            public void onCommunicationSucceeded(Device device, VivoWatchSPDeviceParameter data) {
                Log.d(TAG, "onCommunicationSucceeded: ");
                if (data.backgroundBPMeasurementPeriod == BackgroundPulseTransitTimeChecksFrequency.DISABLE) {
                    setBackgroundBpMeasurementEnable(false);
                    setFrequencyOfChecksSpinnerSelection(sharedPreferencesHelper.getInt(device.deviceId() + "_backgroundBPMeasurementIndex"));
                } else {
                    setBackgroundBpMeasurementEnable(true);
                    setFrequencyOfChecksSpinnerSelection(data.backgroundBPMeasurementPeriod.rawValue());
                }
                clearProgressDialog();
            }

            @Override
            public void onCommunicationFailed(Device device, DeviceCommunicationException cause) {
                Log.d(TAG, "onCommunicationFailed: " + cause.getMessage());
                clearProgressDialog();
                showToast(cause.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deviceManager.removeConnectionStateListener(this);
    }

    private void showProgressDialog(String msg, boolean enableProgressBar) {
        myProgressDialog = new MyProgressDialog(getActivity(), "", msg);
        myProgressDialog.show(enableProgressBar);
    }

    private void clearProgressDialog() {
        if (myProgressDialog != null && myProgressDialog.isShowing())
            myProgressDialog.dismiss();
    }

    private void updateProgress(int progress) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myProgressDialog != null && myProgressDialog.isShowing())
                    myProgressDialog.setProgress(progress);
            }
        });
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

    private void setBackgroundBpMeasurementEnable(boolean b) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swBackgroundBpMeasurement.setChecked(b);
                spinnerFrequencyOfChecks.setEnabled(b);
            }
        });
    }

    private void setFrequencyOfChecksSpinnerSelection(int index) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isFrequencyOfChecksSpinnerTapped = false;
                spinnerFrequencyOfChecks.setSelection(index);
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

    private static class DateTimePicker implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
        private TimestampSetListener mTimestampSetListener;
        private FragmentManager mFragmentManager;

        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private int second;

        public DateTimePicker(TimestampSetListener listener, FragmentManager fragmentManager) {
            mTimestampSetListener = listener;
            mFragmentManager = fragmentManager;

            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setVersion(DatePickerDialog.Version.VERSION_2);

            dpd.show(mFragmentManager, "Datepickerdialog");
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            this.year = year;
            this.month = monthOfYear + 1;
            this.day = dayOfMonth;

            Calendar now = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            );
            tpd.setVersion(TimePickerDialog.Version.VERSION_2);

            tpd.show(mFragmentManager, "TimepickerDialog");
        }

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            this.hour = hourOfDay;
            this.minute = minute;
            this.second = second;
            mTimestampSetListener.onDateTimeSet(String.format("%4d%02d%02d%02d%02d%02d", year, month, day, hour, this.minute, this.second));
        }
    }

    private interface TimestampSetListener {
        void onDateTimeSet(String dateTime);
    }
}
