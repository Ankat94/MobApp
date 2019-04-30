package esiapp.esi.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

import esiapp.esi.R;
import esiapp.esi.adapter.ViewPagerAdapter;
import esiapp.esi.db.dao.NoSqlDao;
import esiapp.esi.model.UpcomingModel;
import esiapp.esi.model.retroModel.AppointMentModel;
import esiapp.esi.model.retroModel.DependantDetails;
import esiapp.esi.model.retroModel.GetAllAppointmentResp;
import esiapp.esi.model.retroModel.IPDetails;
import esiapp.esi.model.retroModel.IPNumberDetails;
import esiapp.esi.model.retroModel.IpDetailsModel;
import esiapp.esi.network.ApiService;
import esiapp.esi.network.NetworkClient;
import esiapp.esi.ui.fragment.HistoryFragment;
import esiapp.esi.ui.fragment.UpcomingFragment;
import esiapp.esi.ui.fragment.dummy.DummyContent;
import esiapp.esi.util.Constant;
import esiapp.esi.util.EsiPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, UpcomingFragment.OnListFragmentInteractionListener, HistoryFragment.OnListFragmentInteractionListener, View.OnClickListener {

    private final static String TAG = LandingActivity.class.getName();

    private ViewPager viewPager;

    private TabLayout tabLayout;
    private Button bookAppointmentButton;
    private ProgressDialog progress;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private TextView mDispensaryAddress;
    private TextView mHeaderName;
    private TextView mIpNumber;
    //private TextView mAadhaarNumber;
    private TextView mUHID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(mToolbar);
        setTitle("");


        progress = new ProgressDialog(this);


        //doCallIpDetails(EsiPrefUtil.getIpNumber(this), EsiPrefUtil.getKeyUserSession(this));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        TextView appointments = (TextView) headerView.findViewById(R.id.appointments);
        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });

        TextView dependents = (TextView) headerView.findViewById(R.id.dependents);
        dependents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, DependantDetailsActivity.class);
                startActivity(intent);
            }
        });

        TextView logOut = (TextView) headerView.findViewById(R.id.settings);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.closeDrawers();
                //confirmation dialog before exiting the app
                new AlertDialog.Builder(LandingActivity.this)
                        .setMessage(getString(R.string.log_out_message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.custome_dialog_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EsiPrefUtil.clearUserInfo(mContext);
                                NoSqlDao.getInstance().deleteAll();
                                Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.custome_dialog_no), null)
                        .show();
            }
        });

        mDispensaryAddress = (TextView) headerView.findViewById(R.id.dispensary_address);
        mDispensaryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });


        mHeaderName = (TextView) headerView.findViewById(R.id.nav_header_name);
        mIpNumber = (TextView) headerView.findViewById(R.id.nav_ipnumber);
        //mAadhaarNumber = (TextView) headerView.findViewById(R.id.nav_aadhar_number);
        mUHID = (TextView) headerView.findViewById(R.id.nav_uhid);
        viewPager = (ViewPager) findViewById(R.id.content_landing_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.white));

        bookAppointmentButton = (Button) findViewById(R.id.content_landing_book_appointment_button);
        bookAppointmentButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();

            //confirmation dialog before exiting the app
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.exit_alert_msg))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.custome_dialog_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            LandingActivity.this.finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.custome_dialog_no), null)
                    .show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.appointments:
                mDrawerLayout.closeDrawers();
                break;
            case R.id.dependents:
                Intent intent = new Intent(LandingActivity.this, BookAppointmentActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                break;
            case R.id.dispensary_address:
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UpcomingFragment(), getString(R.string.upcoming_appointment));
        adapter.addFragment(new HistoryFragment(), getString(R.string.history_appointment));
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_landing_book_appointment_button:
                startActivity(new Intent(LandingActivity.this, BookAppointmentActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doCallIpDetails(EsiPrefUtil.getIpNumber(this), EsiPrefUtil.getKeyUserSession(this));
    }


    @Override
    public void onListFragmentInteraction(AppointMentModel item, boolean isUpcoming) {
        Intent intent = new Intent(this, ManageAppointmentActivity.class);
        intent.putExtra(Constant.APPOINTMENT_MODEL_ITEM, (Serializable) item);
        intent.putExtra(Constant.ISFROMUPCOMING, isUpcoming);
        startActivity(intent);
    }

    /**
     * Api for getting ip details
     *
     * @param ipNumber
     * @param sessionId
     */
    public void doCallIpDetails(String ipNumber, String sessionId) {

        progress.show();

        NetworkClient networkClient = new NetworkClient();
        ApiService apiService = networkClient.getApiService();

        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("IPNumber", ipNumber);
        jsonObject.addProperty("SessionId", sessionId);
        jsonArray.add(jsonObject);

        Call<IpDetailsModel> getIpDetailsModelCall = networkClient.getIpDetailsCall(jsonArray);
        getIpDetailsModelCall.enqueue(new Callback<IpDetailsModel>() {
            @Override
            public void onResponse(Call<IpDetailsModel> call, final Response<IpDetailsModel> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Log.d(TAG, "onResponse: " + response.message() + response.body());
                        IpDetailsModel ipDetailsModelResponse = response.body();
                        if (ipDetailsModelResponse != null && ipDetailsModelResponse.getResponseCode() != null) {
                            if (ipDetailsModelResponse.getResponseCode().equals(Constant.ERROR_CODE_1504)) {
                                Toast.makeText(LandingActivity.this, R.string.session_id_check, Toast.LENGTH_LONG).show();
                                EsiPrefUtil.clearUserInfo(mContext);
                                NoSqlDao.getInstance().deleteAll();
                                Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        } else {
                            Toast.makeText(LandingActivity.this, getString(R.string.something_wrong_message), Toast.LENGTH_LONG).show();
                        }
//                        else if (ipDetailsModelResponse.getResponseCode().equals(Constant.ERROR_CODE_1801) || ipDetailsModelResponse.getResponseCode().equals(Constant.ERROR_CODE_1802)) {

                        NoSqlDao.getInstance().insertValues(Constant.IPNUMBER_DETAILS, (IpDetailsModel) response.body());
                        IpDetailsModel ipDetailsModel = (IpDetailsModel) NoSqlDao.getInstance().findSerializeData(Constant.IPNUMBER_DETAILS);
                        if (ipDetailsModel != null) {
                            IPDetails ipDetails = ipDetailsModel.getIpDetails();
                            if (ipDetails != null) {

                                // setting the values for navigation drawer
                                setTitle(ipDetails.getIPName());
                                mHeaderName.setText(ipDetails.getIPName());
                                mIpNumber.setText(" " + ipDetails.getIPNumber());
                                //mAadhaarNumber.setText(" " + ipDetails.getIP_AadharNo());
                                if (TextUtils.isEmpty(ipDetails.getLocAddress()) == false && TextUtils.isEmpty(ipDetails.getLocName()) == false) {
                                    mDispensaryAddress.setText(getString(R.string.review_booking_dispensary_address) + "\n" + ipDetails.getLocName() + "\n" + ipDetails.getLocAddress());
                                } else {
                                    mDispensaryAddress.setText("Dispensary Address : " + "\n" + getString(R.string.not_available));
                                }
                                if (TextUtils.isEmpty(ipDetails.getEmpeuhid()) == false && ipDetails.getEmpeuhid() != null) {
                                    mUHID.setText(" " + ipDetails.getEmpeuhid());
                                } else {
                                    mUHID.setText("" + getString(R.string.not_available));
                                }
                            }
                        } else {
                            Toast.makeText(LandingActivity.this, getString(R.string.unable_to_fetch_ipdetails_message), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<IpDetailsModel> call, final Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "onFailure");
                        Toast.makeText(LandingActivity.this, getString(R.string.unable_to_fetch_ipdetails_message), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });
            }
        });
    }
}
