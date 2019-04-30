package esiapp.esi.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import esiapp.esi.R;
import esiapp.esi.adapter.SelectDateFragmentAdapter;
import esiapp.esi.adapter.SpinnerItemAdapter;
import esiapp.esi.adapter.model.SpinnerItemModel;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.retroModel.HospitalTimeSlotModel;
import esiapp.esi.model.retroModel.IPDetails;
import esiapp.esi.model.retroModel.IpDetailsModel;
import esiapp.esi.model.retroModel.ScheduledDatesModel;
import esiapp.esi.network.ApiService;
import esiapp.esi.network.NetworkClient;
import esiapp.esi.ui.activity.MainActivity;
import esiapp.esi.util.Constant;
import esiapp.esi.util.EsiPrefUtil;
import esiapp.esi.util.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSelectDateAndTimeSlot} interface
 * to handle interaction events.
 * Use the {@link SelectDateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectDateFragment extends BaseFragment {

    private final static String TAG = SelectDateFragment.class.getName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private StaggeredGridLayoutManager _sGridLayoutManager;

    private List<HospitalTimeSlotModel> mHospitalTimeSlotModels = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnSelectDateAndTimeSlot mListener;
    private Spinner daySessionSpinner;
    private String mAppointmentDate;
    private String mHospitalCode;
    private String mInsSeqNo;
    private String mSelectedTimeSlot;
    private ProgressDialog progress;
    //    private List<HospitalTimeSlotModel> mHospitalTimeSlotList;
    private RecyclerView mRecyclerView;
    private TextView mHospitalAddress;

    private TextView mShowDate;
    private IpDetailsModel mIpDetailsModel;
    private IPDetails mIpDetails;
    private String mDaySessionSelected;

    private int mSelecetdSession = 1;
    private String mStringOfDate;
    private List<ScheduledDatesModel> mScheduledDatesModelList;
    private String mSchedulingRangeFrom;
    private String mSchedulingRangeTo;
    private boolean mIsFistTimeSelected = true;
    private boolean mIsSelectedDateIsHoliday = false;

    public SelectDateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectDateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectDateFragment newInstance(String param1, String param2) {
        SelectDateFragment fragment = new SelectDateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new ProgressDialog(mContext);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_date, container, false);

        mShowDate = (TextView) view.findViewById(R.id.date_fragment_show_date);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                updateDate(year, monthOfYear, dayOfMonth);
            }

        };

        mShowDate.setText(getString(R.string.select_date));
        mShowDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                DatePicker datePicker = datePickerDialog.getDatePicker();
                if (mSchedulingRangeFrom != null && mSchedulingRangeTo != null) {
                    Date minDate;
                    Date maxDate;
                    long milliSecondsMinDate = 0;
                    long milliSecondsMaxDate = 0;
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                    try {

                        minDate = f.parse(mSchedulingRangeFrom);
                        milliSecondsMinDate = minDate.getTime();

                        maxDate = f.parse(mSchedulingRangeTo);
                        milliSecondsMaxDate = maxDate.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //setting the min and max date for calendar
                    datePicker.setMinDate(milliSecondsMinDate);
                    datePicker.setMaxDate(milliSecondsMaxDate);
                    datePickerDialog.show();
                } else {
                    mShowDate.setText(getString(R.string.select_date));
                    Toast.makeText(mContext, getString(R.string.unable_to_fetch_date_message), Toast.LENGTH_LONG).show();
//                    mShowDate.setEnabled(false);
                }
            }
        });
        mHospitalAddress = (TextView) view.findViewById(R.id.date_fragment_address);
        daySessionSpinner = (Spinner) view.findViewById(R.id.date_fragment_spinner_session);

//        String[] items = new String[]{"Morning 12:00 - 11:59 AM", "Afternoon 12:00 - 3:59 PM", "Evening 4:00 - 6:59 PM", "Night 7:00 - 11:59 PM)"};

        ArrayList<SpinnerItemModel> spinnerItemList = new ArrayList<>();
        spinnerItemList.add(new SpinnerItemModel(getString(R.string.morning_session), R.drawable.morning));
        spinnerItemList.add(new SpinnerItemModel(getString(R.string.afternoon_session), R.drawable.afternoon));
        spinnerItemList.add(new SpinnerItemModel(getString(R.string.evening_session), R.drawable.evening));
        spinnerItemList.add(new SpinnerItemModel(getString(R.string.night_session), R.drawable.night));

        SpinnerItemAdapter spinnerItemAdapter = new SpinnerItemAdapter((Activity) mContext, R.layout.spinner_item_layout, R.id.spinner_item_txt, spinnerItemList);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, items);
        daySessionSpinner.setAdapter(spinnerItemAdapter);
//        daySessionSpinner.setAdapter(adapter);
        daySessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mIsFistTimeSelected) {
                    mIsFistTimeSelected = false;

                    return;
                }

                Log.d(TAG, "onItemSelected: " + position);
                mSelecetdSession = position + 1;
                updateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.date_fragment_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        _sGridLayoutManager = new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(_sGridLayoutManager);


        int spacingInPixels = getResources().getDimensionPixelOffset(R.dimen.date_grid_spacing);

        // fetching the ip details ~ from the db
        mIpDetailsModel = (IpDetailsModel) NoSqlDao.getInstance().findSerializeData(Constant.IPNUMBER_DETAILS);
        mIpDetails = mIpDetailsModel.getIpDetails();
        mHospitalAddress.setText(mIpDetails.getLocName() + " " + mIpDetails.getLocAddress());

        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAvailableDates(EsiPrefUtil.getIpNumber(mContext), EsiPrefUtil.getKeyUserSession(mContext), mIpDetails.getDispCode());

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String appointmentDate, String hospitalCode, String insSeqNo) {
        if (mListener != null) {
            mListener.onDateAndTimeSlotSelected(appointmentDate, mSelectedTimeSlot, hospitalCode, insSeqNo);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectDateAndTimeSlot) {
            mListener = (OnSelectDateAndTimeSlot) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ChoosePatientDetials");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSelectDateAndTimeSlot {
        // TODO: Update argument type and name
        void onDateAndTimeSlotSelected(String appointmentDate, String timeSlot, String hospitalCode, String insSeqNo);
    }

    /**
     * Api for getting time slots
     *
     * @param ipNumber
     * @param sessionId
     * @param hospitalCode
     * @param selectedDateForAppointment
     */
    private void getHospitalTimeSlots(String ipNumber, String sessionId, String hospitalCode, String selectedDateForAppointment) {
        progress.show();
        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();

        JsonArray jsonArray = new JsonArray();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("SessionId", sessionId);
        jsonObject.addProperty("HospitalCode", hospitalCode);
        jsonObject.addProperty("SelectedDateForAppointment", selectedDateForAppointment);
        jsonArray.add(jsonObject);
        Call<List<HospitalTimeSlotModel>> getHospitalTimeSlotRespCall = networkClient.getHospitalTimeSlotRespCall(jsonArray);
        getHospitalTimeSlotRespCall.enqueue(new Callback<List<HospitalTimeSlotModel>>() {
                                                @Override
                                                public void onResponse(Call<List<HospitalTimeSlotModel>> call, final Response<List<HospitalTimeSlotModel>> response) {
                                                    if (getActivity() == null)
                                                        return;
                                                    getActivity().runOnUiThread(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        progress.dismiss();
                                                                                        if (response != null) {
                                                                                            Log.d(TAG, "run: ");
                                                                                            List<HospitalTimeSlotModel> hospitalTimeSlotModelList = response.body();
                                                                                            if (hospitalTimeSlotModelList != null && hospitalTimeSlotModelList.size() > 0) {
                                                                                                HospitalTimeSlotModel hospitalTimeSlotModel = hospitalTimeSlotModelList.get(0);
                                                                                                if (hospitalTimeSlotModel != null && hospitalTimeSlotModel.getResponseCode() != null) {
                                                                                                    if (hospitalTimeSlotModel.getResponseCode() != null && hospitalTimeSlotModel.getResponseCode().equals(Constant.ERROR_CODE_2103)) {
                                                                                                        Toast.makeText(mContext, getString(R.string.error_code_2103), Toast.LENGTH_LONG).show();
                                                                                                        mIsSelectedDateIsHoliday = true;
                                                                                                        mHospitalTimeSlotModels.clear();
                                                                                                        mHospitalTimeSlotModels.addAll(hospitalTimeSlotModelList);
                                                                                                        updateList(false);
                                                                                                        return;
                                                                                                    }
                                                                                                }
                                                                                            }
//                                                                                            else {
//                                                                                                Toast.makeText(mContext, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
//                                                                                            }
                                                                                            if (hospitalTimeSlotModelList != null && hospitalTimeSlotModelList.size() > 0) {
                                                                                                Log.d(TAG, "run: " + hospitalTimeSlotModelList.size());
                                                                                                mIsSelectedDateIsHoliday = false;
                                                                                                mHospitalTimeSlotModels.clear();
                                                                                                mHospitalTimeSlotModels.addAll(hospitalTimeSlotModelList);
                                                                                                updateList();
                                                                                            }
//                                                                                            HospitalTimeSlotModel hospitalTimeSlotModel = hospitalTimeSlotModelList.get(0);
//
//                                                                                            Log.d(TAG, "run: " + hospitalTimeSlotModel.getTimeSlotMinutes());


                                                                                        }
                                                                                    }
                                                                                }
                                                    );
                                                }

                                                @Override
                                                public void onFailure(Call<List<HospitalTimeSlotModel>> call, final Throwable t) {
                                                    if (getActivity() == null)
                                                        return;
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Log.v(TAG, "onFailure");
                                                            Toast.makeText(mContext, getString(R.string.unable_to_connnect_message), Toast.LENGTH_LONG).show();
                                                            t.printStackTrace();
                                                            progress.dismiss();
                                                        }
                                                    });
                                                }
                                            }

        );

    }

    private void updateList() {
        updateList(true);
    }

    /**
     * This method update the time slots according to day session
     */
    private void updateList(boolean needToShowToast) {
        int currentSlot = mSelecetdSession;

        //checking whether time slot is selected or not
        if (mListener != null) {
            mListener.onDateAndTimeSlotSelected(mStringOfDate, "", "", "");
        }

        Log.d(TAG, "updateList: " + mHospitalTimeSlotModels.size());
        List<HospitalTimeSlotModel> updateList = segregateTimeSlot(mHospitalTimeSlotModels, currentSlot);

        if (updateList != null && updateList.size() == 0 && needToShowToast) {
            Toast.makeText(mContext, R.string.no_time_slot_available_message, Toast.LENGTH_LONG).show();
        }

        SelectDateFragmentAdapter rcAdapter = new SelectDateFragmentAdapter(
                mContext, updateList, new SelectDateFragmentAdapter.ChooseTimeSlot() {
            @Override
            public void onChooseTimeSlot(String selectedTime) {
                mSelectedTimeSlot = selectedTime;
                if (mListener != null) {
                    mListener.onDateAndTimeSlotSelected(mStringOfDate, mSelectedTimeSlot, mHospitalCode, mInsSeqNo);
                }
                Log.d(TAG, "onChooseTimeSlot: " + mSelectedTimeSlot);
            }
        });

        mRecyclerView.setAdapter(rcAdapter);
    }

    private void updateDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.date = day;

        String mMonth = new DateFormatSymbols().getMonths()[month];
        mStringOfDate = year + "-" + (month + 1) + "-" + day;
        String stringDateforcalendar = getDate(day) + " " + mMonth + " " + year;
        mShowDate.setText(stringDateforcalendar);
        if (mListener != null) {
            mListener.onDateAndTimeSlotSelected(mStringOfDate, "", "", "");
        }

        getHospitalTimeSlots(EsiPrefUtil.getIpNumber(mContext), EsiPrefUtil.getKeyUserSession(mContext), mIpDetails.getDispCode(), mStringOfDate);

    }


    private String getDate(int day) {
        if (day <= 9) {
            return "0" + day;
        } else {
            return "" + day;
        }
    }

    private int year;
    private int month;
    private int date;

    /**
     * This method segregate time slot according to day session
     *
     * @param list
     * @param daySession
     * @return
     */
    private List<HospitalTimeSlotModel> segregateTimeSlot(List<HospitalTimeSlotModel> list, int daySession) {
        List<HospitalTimeSlotModel> hospitalTimeSlotModelList = new ArrayList<>();

        if (list == null) {
            return hospitalTimeSlotModelList;
        }

        for (int i = 0; i < list.size(); i++) {
            HospitalTimeSlotModel hospitalTimeSlotModel = list.get(i);
            String slot = hospitalTimeSlotModel.getSlotPartofDay();
            Log.d(TAG, "segregateTimeSlot: i " + i + " slot " + slot + " daySession " + daySession);
            if (TextUtils.isEmpty(slot) == false && TextUtils.isDigitsOnly(slot) && (Integer.parseInt(slot) == daySession)) {
                Log.d(TAG, "segregateTimeSlot: i " + i + " slot " + slot);
                hospitalTimeSlotModelList.add(list.get(i));
            }
        }
        return hospitalTimeSlotModelList;
    }

    /**
     * Api for getting available dates for hospital
     *
     * @param ipNumber
     * @param sessionId
     * @param hospitalCode
     */
    public void getAvailableDates(String ipNumber, String sessionId, String hospitalCode) {
        progress.show();


        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();

        JsonArray jsonArray = new JsonArray();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("SessionId", sessionId);
        jsonObject.addProperty("HospitalCode", hospitalCode);
        jsonArray.add(jsonObject);
        Call<List<ScheduledDatesModel>> call = networkClient.getScheduledDatesCall(jsonArray);
        call.enqueue(new Callback<List<ScheduledDatesModel>>() {
            @Override
            public void onResponse(Call<List<ScheduledDatesModel>> call, final Response<List<ScheduledDatesModel>> response) {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (response != null) {
                            Log.d(TAG, "run: ");

                            List<ScheduledDatesModel> scheduledDatesModelList = response.body();
                            if (scheduledDatesModelList != null && scheduledDatesModelList.size() > 0) {
                                ScheduledDatesModel scheduledDatesModel = scheduledDatesModelList.get(0);
                                if (scheduledDatesModel != null && scheduledDatesModel.getResponseCode() != null) {
                                    if (scheduledDatesModel.getResponseCode().equals(Constant.ERROR_CODE_1504)) {
                                        Toast.makeText(mContext, getString(R.string.session_id_check), Toast.LENGTH_LONG).show();
                                        EsiPrefUtil.clearUserInfo(mContext);
                                        NoSqlDao.getInstance().deleteAll();
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        getActivity().finish();
                                        return;
                                    } else if (scheduledDatesModel.getResponseCode().equals(Constant.ERROR_CODE_1503)) {
                                        Toast.makeText(mContext, getString(R.string.error_code_1503), Toast.LENGTH_LONG).show();
                                    } else if (scheduledDatesModel.getResponseCode().equals(Constant.ERROR_CODE_2000)) {
                                        mSchedulingRangeFrom = scheduledDatesModel.getSchedulingRangeFrom();
                                        mSchedulingRangeTo = scheduledDatesModel.getSchedulingRangeTo();
                                        if (mSchedulingRangeFrom != null) {
                                            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                                            try {
                                                Date minDate = f.parse(mSchedulingRangeFrom);
                                                SimpleDateFormat showFormat = new SimpleDateFormat("dd MMM");
                                                mShowDate.setText(showFormat.format(minDate));

                                                year = minDate.getYear();
                                                month = minDate.getMonth();
                                                date = minDate.getDay();

                                                Calendar cal = Calendar.getInstance();
                                                cal.setTime(minDate);

                                                month = cal.get(Calendar.MONTH);
                                                date = cal.get(Calendar.DAY_OF_MONTH);
                                                year = cal.get(Calendar.YEAR);

                                                mStringOfDate = year + "-" + (month + 1) + "-" + date;

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            getHospitalTimeSlots(EsiPrefUtil.getIpNumber(mContext), EsiPrefUtil.getKeyUserSession(mContext), mIpDetails.getDispCode(), mSchedulingRangeFrom);
                                            String[] stringDate = mSchedulingRangeFrom.split("-");
                                            String dateToset = stringDate[2] + " " + new DateFormatSymbols().getMonths()[Integer.parseInt(stringDate[1]) - 1] + " " + stringDate[0];
                                            mShowDate.setText(dateToset);

                                        } else {
                                            mShowDate.setText(getString(R.string.select_date));
                                        }
                                        Log.d(TAG, "run: " + mSchedulingRangeFrom);
                                        Log.d(TAG, "run: " + mSchedulingRangeTo);
                                    } else if (scheduledDatesModel.getResponseCode().equals(Constant.ERROR_CODE_2001)) {
                                        Toast.makeText(mContext, getString(R.string.error_code_2001), Toast.LENGTH_LONG).show();
                                    } else if (scheduledDatesModel.getResponseCode().equals(Constant.ERROR_CODE_2002)) {
                                        Toast.makeText(mContext, getString(R.string.error_code_2002), Toast.LENGTH_LONG).show();
                                    } else if (scheduledDatesModel.getResponseCode().equals(Constant.ERROR_CODE_2003)) {
                                        Toast.makeText(mContext, getString(R.string.error_code_2003), Toast.LENGTH_LONG).show();
                                    } else if (scheduledDatesModel.getResponseCode().equals(Constant.ERROR_CODE_2004)) {
                                        Toast.makeText(mContext, getString(R.string.error_code_2004), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<ScheduledDatesModel>> call, final Throwable t) {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "onFailure");
                        Toast.makeText(mContext, getString(R.string.unable_to_connnect_message), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });
            }
        });
    }

}
