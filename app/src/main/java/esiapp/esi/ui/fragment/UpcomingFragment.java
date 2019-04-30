package esiapp.esi.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import esiapp.esi.R;
import esiapp.esi.adapter.UpcomingItemRecyclerViewAdapter;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.retroModel.AppointMentModel;
import esiapp.esi.network.ApiService;
import esiapp.esi.network.NetworkClient;
import esiapp.esi.ui.activity.LandingActivity;
import esiapp.esi.ui.activity.MainActivity;
import esiapp.esi.util.Constant;
import esiapp.esi.util.EsiPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class UpcomingFragment extends BaseFragment {

    private final static String TAG = UpcomingFragment.class.getName();

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ProgressDialog progress;
    private List<AppointMentModel> mAppointmentList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private List<AppointMentModel> mUpcomingAppointList = new ArrayList<>();
    private List<AppointMentModel> historyAppointmentList = new ArrayList<>();
    private boolean mIsFromUpcoming = false;
    private boolean mIsFromHistroy = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UpcomingFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UpcomingFragment newInstance(int columnCount) {
        UpcomingFragment fragment = new UpcomingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = new ProgressDialog(mContext);

        //api call
        doGetAllAppointments(EsiPrefUtil.getIpNumber(mContext), EsiPrefUtil.getKeyUserSession(mContext));

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.upcoming_recycler_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AppointMentModel item, boolean isFrom);
    }

    @Override
    public void onResume() {
        super.onResume();
        //api call
        doGetAllAppointments(EsiPrefUtil.getIpNumber(mContext), EsiPrefUtil.getKeyUserSession(mContext));
    }

    /**
     * Api call for getting the list of appointments
     *
     * @param ipNumber
     * @param sessionId
     */
    public void doGetAllAppointments(String ipNumber, String sessionId) {

        progress.show();

        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();

        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("SessionId", sessionId);
        jsonArray.add(jsonObject);

        Call<List<AppointMentModel>> call = networkClient.getAllAppointmentRespCall(jsonArray);
        call.enqueue(new Callback<List<AppointMentModel>>() {
            @Override
            public void onResponse(Call<List<AppointMentModel>> call, final Response<List<AppointMentModel>> response) {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();

                        Log.d(TAG, "onResponse: ");
                        List<AppointMentModel> getAllAppointmentResps = response.body();
                        if (getAllAppointmentResps != null && getAllAppointmentResps.size() > 0) {
                            AppointMentModel appointMentModel = getAllAppointmentResps.get(0);
                            if (appointMentModel != null && appointMentModel.getResponseCode() != null) {
                                if (appointMentModel.getResponseCode().equals(Constant.ERROR_CODE_1504)) {
                                    Toast.makeText(mContext, R.string.session_id_check, Toast.LENGTH_LONG).show();
                                    EsiPrefUtil.clearUserInfo(mContext);
                                    NoSqlDao.getInstance().deleteAll();
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
                                    return;
                                } else if (appointMentModel.getResponseCode().equals(Constant.ERROR_CODE_1503)) {
                                    Toast.makeText(mContext, getString(R.string.error_code_1503), Toast.LENGTH_LONG).show();
                                }
// else if (appointMentModel.getResponseCode().equals(Constant.ERROR_CODE_1201)) {
//                                    Toast.makeText(mContext, getString(R.string.error_code_1201), Toast.LENGTH_LONG).show();
//                                    EsiPrefUtil.clearUserInfo(mContext);
//                                    NoSqlDao.getInstance().deleteAll();
//                                    Intent intent = new Intent(mContext, MainActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    startActivity(intent);
//                                    getActivity().finish();
//                                    return;
//                                }
                                else if (appointMentModel.getResponseCode().equals(Constant.ERROR_CODE_1200) || appointMentModel.getResponseCode().equals(Constant.ERROR_CODE_1201)) {
                                    mAppointmentList.clear();
                                    mAppointmentList.addAll(getAllAppointmentResps);
                                    mUpcomingAppointList = segreateList(mAppointmentList);
                                    if (mUpcomingAppointList != null && mUpcomingAppointList.size() > 0) {
                                        Collections.sort(mUpcomingAppointList, labelComparator);
                                    }
                                    mRecyclerView.setAdapter(new UpcomingItemRecyclerViewAdapter(mContext, mUpcomingAppointList, mListener));
                                }
                            } else {
                                Toast.makeText(mContext, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<AppointMentModel>> call, final Throwable t) {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "onFailure");
                        Toast.makeText(mContext, getString(R.string.unable_to_fetch_appointment_list), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });
            }
        });
    }

    /**
     * This method is used to sort list according to date
     */
    public Comparator<AppointMentModel> labelComparator = new Comparator<AppointMentModel>() {
        public int compare(AppointMentModel m1, AppointMentModel m2) {
            final SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
            try {
                final Date date = format.parse(m1.getAppointmentDateString());
                final Date date2 = format.parse(m2.getAppointmentDateString());

                final long time1 = date.getTime();
                final long time2 = date2.getTime();

                return Long.valueOf(time1).compareTo(Long.valueOf(time2));
            } catch (ParseException e) {
//                e.printStackTrace();
            }

            return 0;
        }
    };

    /**
     * This method is used to segreagate list
     *
     * @param appointMentList
     * @return
     */
    private List<AppointMentModel> segreateList(List<AppointMentModel> appointMentList) {
        List<AppointMentModel> segreatedList = new ArrayList<>();
        for (int i = 0; i < appointMentList.size(); i++) {
            AppointMentModel appointMentModel = appointMentList.get(i);
            if (appointMentModel != null) {
                if (appointMentModel.getShowAppointmentIn() != null && appointMentModel.getShowAppointmentIn().equals("Upcoming")) {
                    segreatedList.add(appointMentList.get(i));
                }
            }
        }
        return segreatedList;
    }
}
