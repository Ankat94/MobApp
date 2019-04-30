package esiapp.esi.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
//import esiapp.esi.adapter.HistoryRecyclerViewAdapter;
import esiapp.esi.adapter.UpcomingItemRecyclerViewAdapter;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.retroModel.AppointMentModel;
import esiapp.esi.model.retroModel.DependantDetails;
import esiapp.esi.model.retroModel.IPDetails;
import esiapp.esi.model.retroModel.IPNumberDetails;
import esiapp.esi.network.ApiService;
import esiapp.esi.network.NetworkClient;
import esiapp.esi.ui.activity.LandingActivity;
import esiapp.esi.ui.activity.MainActivity;
import esiapp.esi.ui.fragment.dummy.DummyContent;
import esiapp.esi.ui.fragment.dummy.DummyContent.DummyItem;
import esiapp.esi.util.Constant;
import esiapp.esi.util.EsiPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static esiapp.esi.util.Constant.IPNUMBER_DETAILS;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HistoryFragment extends BaseFragment {

    private final static String TAG = HistoryFragment.class.getName();

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private List<AppointMentModel> mAppointmentList = new ArrayList<>();
    private List<AppointMentModel> mAppointmentHistory = new ArrayList<>();

    private ProgressDialog progress;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistoryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HistoryFragment newInstance(int columnCount) {
        HistoryFragment fragment = new HistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_item_list2, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            IPNumberDetails ipNumberDetails = (IPNumberDetails) NoSqlDao.getInstance().findSerializeData(IPNUMBER_DETAILS);
//            if (ipNumberDetails != null) {
//                IPDetails ipDetails = ipNumberDetails.getIpDetails();
//                List<DependantDetails> upcomingFragmentList = new ArrayList<>();
//                upcomingFragmentList.addAll(ipDetails.getIPDependentDetail());
//            }
//            recyclerView.setAdapter(new HistoryRecyclerViewAdapter(DummyContent.ITEMS, mListener));
//            mRecyclerView.setAdapter(new UpcomingItemRecyclerViewAdapter(mAppointmentHistory));
        }
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
    public void onResume() {
        super.onResume();
        //api call
        doGetAllAppointments(EsiPrefUtil.getIpNumber(mContext), EsiPrefUtil.getKeyUserSession(mContext));
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
                                if (appointMentModel.getResponseCode().equals(Constant.ERROR_CODE_1200) || appointMentModel.getResponseCode().equals(Constant.ERROR_CODE_1201)) {
                                    mAppointmentList.clear();
                                    mAppointmentHistory.clear();
                                    mAppointmentList.addAll(getAllAppointmentResps);
                                    mAppointmentHistory = segreateList(mAppointmentList);
                                    if (mAppointmentHistory != null && mAppointmentHistory.size() > 0) {
                                        Collections.sort(mAppointmentHistory, labelComparator);
                                    }
                                    mRecyclerView.setAdapter(new UpcomingItemRecyclerViewAdapter(mContext, mAppointmentHistory, true, mListener));
                                } else if (appointMentModel.getResponseCode().equals(Constant.ERROR_CODE_1504)) {
                                    Toast.makeText(mContext, getString(R.string.session_id_check), Toast.LENGTH_LONG).show();
                                    EsiPrefUtil.clearUserInfo(mContext);
                                    NoSqlDao.getInstance().deleteAll();
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
                                    return;
                                } else if (appointMentModel.getResponseCode().equals(Constant.ERROR_CODE_1503)) {
                                    Toast.makeText(mContext, getString(R.string.valid_ip_session), Toast.LENGTH_LONG).show();
                                }
//                                else if (appointMentModel.getResponseCode().equals(Constant.ERROR_CODE_1201)) {
//                                    Toast.makeText(mContext, getString(R.string.invalid_ip), Toast.LENGTH_LONG).show();
//                                }
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
            if (appointMentModel.getShowAppointmentIn() != null && appointMentModel.getShowAppointmentIn().equals("History")) {
                segreatedList.add(appointMentList.get(i));
            }
        }
        return segreatedList;
    }
}
