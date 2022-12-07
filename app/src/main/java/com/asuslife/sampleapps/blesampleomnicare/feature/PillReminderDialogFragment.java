package com.asuslife.sampleapps.blesampleomnicare.feature;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.asuslife.omnicaresdk.AsusLifeOmniCare;
import com.asuslife.omnicaresdk.Device;
import com.asuslife.omnicaresdk.DeviceCommunicationCallback;
import com.asuslife.omnicaresdk.DeviceConnectionStateListener;
import com.asuslife.omnicaresdk.DeviceManager;
import com.asuslife.omnicaresdk.DeviceModel;
import com.asuslife.omnicaresdk.VivoWatch5AdvancedFeature;
import com.asuslife.omnicaresdk.VivoWatchBPAdvancedFeature;
import com.asuslife.omnicaresdk.VivoWatchSPAdvancedFeature;
import com.asuslife.omnicaresdk.bluetooth.DeviceCommunicationException;
import com.asuslife.omnicaresdk.bluetooth.DeviceConnectionFailedException;
import com.asuslife.omnicaresdk.settings.PillReminder;
import com.asuslife.sampleapps.blesampleomnicare.R;
import com.asuslife.sampleapps.blesampleomnicare.adapter.PillReminderListAdapter;
import com.asuslife.sampleapps.blesampleomnicare.util.MyProgressDialog;
import com.asuslife.sampleapps.blesampleomnicare.util.SharedPreferencesHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.asuslife.sampleapps.blesampleomnicare.devices.DevicesInfoDialogFragment.DEVICE;

public class PillReminderDialogFragment extends DialogFragment implements DeviceConnectionStateListener {

    private String TAG = PillReminderDialogFragment.class.getSimpleName();

    private View mRootView;

    private DeviceManager deviceManager;
    private Device device;

    private SharedPreferencesHelper sharedPreferencesHelper;

    private Calendar calendar;

    private SimpleDateFormat simpleDateFormat, simpleTimeFormat;

    private PillReminderListAdapter pillReminderListAdapter;
    private RecyclerView pillReminderList;

    private List<PillReminder> pillReminders = new ArrayList<>();

    private ImageView ivStartDate, ivEndDate, ivAdd, ivCheck;
    private EditText etStartDate, etEndDate;

    private MyProgressDialog myProgressDialog;

    private int sYear, sMonth, sDay, eYear, eMonth, eDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device = (Device) getArguments().getSerializable(DEVICE);
        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleTimeFormat = new SimpleDateFormat("HH:mm");

        calendar = Calendar.getInstance();
        sYear = calendar.get(Calendar.YEAR);
        sMonth = calendar.get(Calendar.MONTH);
        sDay = calendar.get(Calendar.DAY_OF_MONTH);
        eYear = calendar.get(Calendar.YEAR);
        eMonth = calendar.get(Calendar.MONTH);
        eDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_pill_reminder, container, false);

        pillReminderListAdapter = new PillReminderListAdapter(pillReminders);

        etStartDate = mRootView.findViewById(R.id.et_start_date);
        etStartDate.setText(simpleDateFormat.format(calendar.getTime()));

        etEndDate = mRootView.findViewById(R.id.et_end_date);
        etEndDate.setText(simpleDateFormat.format(calendar.getTime()));

        ivStartDate = mRootView.findViewById(R.id.iv_start_date);
        ivStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        sYear = i;
                        sMonth = i1;
                        sDay = i2;
                        etStartDate.setText(sYear+"-"+(sMonth+1)+"-"+sDay);
                    }
                },sYear,sMonth,sDay);
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                try {
                    datePickerDialog.getDatePicker().setMaxDate(simpleDateFormat.parse(eYear+"-"+(eMonth+1)+"-"+eDay).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.show();
            }
        });

        ivEndDate = mRootView.findViewById(R.id.iv_end_date);
        ivEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        eYear = i;
                        eMonth = i1;
                        eDay = i2;
                        etEndDate.setText(eYear+"-"+(eMonth+1)+"-"+eDay);
                    }
                },eYear,eMonth,eDay);
                try {
                    datePickerDialog.getDatePicker().setMinDate(simpleDateFormat.parse(sYear+"-"+(sMonth+1)+"-"+sDay).getTime());
                }catch (ParseException e){
                    Log.e(TAG,Log.getStackTraceString(e));
                }
                datePickerDialog.show();
            }
        });

        ivAdd = mRootView.findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        try {
                            long startDate = simpleDateFormat.parse(sYear+"-"+(sMonth+1)+"-"+sDay).getTime();
                            long endDate = simpleDateFormat.parse(eYear+"-"+(eMonth+1)+"-"+eDay).getTime();
                            long time = simpleTimeFormat.parse(i+":"+i1).getTime();
                            pillReminders.add(new PillReminder(startDate,endDate,time));
                            pillReminderListAdapter.refresh(pillReminders);
                        }catch (ParseException e){
                            Log.e(TAG, Log.getStackTraceString(e));
                        }
                    }
                },Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),false);
                timePickerDialog.show();
            }
        });

        ivCheck = (ImageView)mRootView.findViewById(R.id.iv_check);
        ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgressDialog(getActivity().getResources().getString(R.string.syncing));

                switch (device.deviceModel()){
                    case VIVOWATCH_BP:
                        ((VivoWatchBPAdvancedFeature) device.advancedFeature()).setPillReminders(pillReminders, new DeviceCommunicationCallback() {
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
                        break;
                    case VIVOWATCH_SP:
                        ((VivoWatchSPAdvancedFeature) device.advancedFeature()).setPillReminders(pillReminders, new DeviceCommunicationCallback() {
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
                        break;
                    case VIVOWATCH_5:
                        ((VivoWatch5AdvancedFeature) device.advancedFeature()).setPillReminders(pillReminders, new DeviceCommunicationCallback() {
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
                        break;
                }
            }
        });

        deviceManager = AsusLifeOmniCare.getDeviceManager();
        deviceManager.addConnectionStateListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        pillReminderList = (RecyclerView) mRootView.findViewById(R.id.rv_pill_reminder_list);
        pillReminderList.setHasFixedSize(true);
        pillReminderList.setLayoutManager(layoutManager);
        pillReminderList.setAdapter(pillReminderListAdapter);

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deviceManager.removeConnectionStateListener(this);
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

    @Override
    public void onDeviceConnected(Device device) {
        Log.d(TAG, "onDeviceConnected: ");
    }

    @Override
    public void onDeviceConnectionFailed(Device device, DeviceConnectionFailedException dcfe) {
        Log.d(TAG, "onDeviceConnectionFailed: " + dcfe.getMessage());
    }

    @Override
    public void onDeviceDisconnected(Device device) {
        Log.d(TAG, "onDeviceDisconnected: ");
    }
}
