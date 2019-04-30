package esiapp.esi.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esiapp.esi.R;
import esiapp.esi.model.UpcomingModel;
import esiapp.esi.model.retroModel.AppointMentModel;
import esiapp.esi.model.retroModel.DependantDetails;
import esiapp.esi.ui.activity.BaseActivity;
import esiapp.esi.ui.fragment.HistoryFragment;
import esiapp.esi.ui.fragment.UpcomingFragment;

/**
 * {@link RecyclerView.Adapter} that can display a {@link esiapp.esi.ui.fragment.UpcomingFragment} and makes a call to the
 * specified {@link UpcomingFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UpcomingItemRecyclerViewAdapter extends RecyclerView.Adapter<UpcomingItemRecyclerViewAdapter.ViewHolder> {

    private final static String TAG = UpcomingItemRecyclerViewAdapter.class.getName();
    //    private final List<DependantDetails> mValues;
    private List<AppointMentModel> mAppointmentList = new ArrayList<>();

    private final UpcomingFragment.OnListFragmentInteractionListener mUpcomingListener;
    private final HistoryFragment.OnListFragmentInteractionListener mHistoryListener;
    private Context mContext;
    private boolean mIsFromHistory;
//    public UpcomingItemRecyclerViewAdapter(List<DependantDetails> items, UpcomingFragment.OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//    }

    public UpcomingItemRecyclerViewAdapter(Context context, List<AppointMentModel> items, UpcomingFragment.OnListFragmentInteractionListener listener) {
        mContext = context;
        mAppointmentList = items;
        mUpcomingListener = listener;
        mHistoryListener = null;
    }

    public UpcomingItemRecyclerViewAdapter(Context context, List<AppointMentModel> items, boolean isFromHistory, HistoryFragment.OnListFragmentInteractionListener listener) {
        mContext = context;
        mAppointmentList = items;
        mHistoryListener = listener;
        mIsFromHistory = isFromHistory;
        mUpcomingListener = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_upcoming_appointment_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mAppointmentList != null && mAppointmentList.size() > 0) {
            final AppointMentModel appointMentModel = mAppointmentList.get(position);
            Log.d(TAG, "onBindViewHolder: " + appointMentModel.getPatientName());
            Log.d(TAG, "onBindViewHolder: " + appointMentModel.getAppointmentId());
            holder.mName.setText(appointMentModel.getPatientName());
            holder.mId.setText(" " + String.valueOf(appointMentModel.getReferenceNumber()));
            holder.mtime.setText(" " + appointMentModel.getAppointmentDateString());
            holder.mtime.setTypeface(Typeface.DEFAULT_BOLD);

            holder.mStatus.setText(" " + appointMentModel.getStatusDescription());
            if (appointMentModel.getInsSeqNo().equals("1")) {
                holder.mView.setBackgroundColor(mContext.getResources().getColor(R.color.panda));
            } else if (appointMentModel.getInsSeqNo().equals("0")) {
                holder.mView.setBackgroundColor(mContext.getResources().getColor(R.color.appointment_color));
            }
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mUpcomingListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mUpcomingListener.onListFragmentInteraction((holder.mItem = appointMentModel), true);
                    }
                    if (mHistoryListener != null) {
                        mHistoryListener.onListFragmentInteraction((holder.mItem = appointMentModel), false);
                    }
                }
            });
        } else {
            if (mIsFromHistory) {
                holder.mName.setText(R.string.no_history_available);
                holder.mName.setTextSize(20);
                holder.mName.setGravity(Gravity.CENTER);
            } else {
                holder.mName.setText(R.string.no_upcoming_appointments);
                holder.mName.setGravity(Gravity.CENTER);
                holder.mName.setTextSize(20);
            }
            holder.mId.setVisibility(View.INVISIBLE);
            holder.mtime.setVisibility(View.INVISIBLE);
            holder.mStatus.setVisibility(View.INVISIBLE);
            holder.mReferenceNumberName.setVisibility(View.INVISIBLE);
            holder.mTimeName.setVisibility(View.INVISIBLE);
            holder.mStatusName.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if (mAppointmentList != null && mAppointmentList.size() > 0) {
            return mAppointmentList.size();
        } else {
            return 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mId;
        public final TextView mtime;
        public final TextView mStatus;
        public final TextView mReferenceNumberName;
        public final TextView mTimeName;
        public final TextView mStatusName;
        public AppointMentModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.upcoming_frag_name);
            mId = (TextView) view.findViewById(R.id.upcoming_frag_appoinment_id);
            mtime = (TextView) view.findViewById(R.id.upcoming_frag_time);
            mStatus = (TextView) view.findViewById(R.id.fragment_appointment_status_et);
            mReferenceNumberName = (TextView) view.findViewById(R.id.upcoming_frag_appoinment);
            mTimeName = (TextView) view.findViewById(R.id.upcoming_frag_time_heading);
            mStatusName = (TextView) view.findViewById(R.id.fragment_appointment_status);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mtime.getText() + "'";
        }
    }
}
