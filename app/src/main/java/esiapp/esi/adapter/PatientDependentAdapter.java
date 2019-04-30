package esiapp.esi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import esiapp.esi.R;
import esiapp.esi.model.retroModel.IPDependentDetail;
import esiapp.esi.model.retroModel.IPDetails;
import esiapp.esi.ui.activity.UpdateAdhaarActivity;
import esiapp.esi.ui.fragment.ChoosePatientFragment;

/**
 * Created by soorianarayanan on 9/2/17.
 */

public class PatientDependentAdapter extends RecyclerView.Adapter<PatientDependentAdapter.ViewHolder> {

    private final static String TAG = PatientDependentAdapter.class.getName();

    //    private List<ChoosePatientDepdentListItemModel> mValues = new ArrayList<>();
//    private final ChoosePatientFragment.ChoosePatientDetials mListener;
    private Activity mContext;
    private int selected = 0;
    private SelectedItemDetails mSelecetedItemDetails;
    private IPDetails mIpDetails;
    private IPDependentDetail mSelectedItem;
    private List<IPDependentDetail> mIpDependentDetailList = new ArrayList<>();
    private boolean mIsFromDependant;

    public PatientDependentAdapter(Activity context, List<IPDependentDetail> dependentDetailList, IPDetails ipDetails, Boolean isFromDependant, SelectedItemDetails selectedItemDetails) {
//        this.mListener = mListener;
        if (dependentDetailList != null) {
            mIpDependentDetailList.addAll(dependentDetailList);
        }
//        this.mIpDependentDetailList = dependentDetailList;
        mContext = context;
        mIpDetails = ipDetails;
        mSelecetedItemDetails = selectedItemDetails;
        mIsFromDependant = isFromDependant;
    }

    public void clearSelectedItem() {
        mSelectedItem = null;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_patient_item, parent, false);
        return new PatientDependentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final IPDependentDetail ipDependentDetail = mIpDependentDetailList.get(position);
        Log.d(TAG, "onBindViewHolder: " + ipDependentDetail.getDependatName());
        holder.mName.setText(ipDependentDetail.getDependatName());
        holder.mRealationShip.setText(" " + ipDependentDetail.getRelationship());

//        if (TextUtils.isEmpty(ipDependentDetail.getDEpendent_AadharNo()) == false) {
//            holder.mAadharNo.setText(" " + ipDependentDetail.getDEpendent_AadharNo());
//        } else {
//            holder.mAadharNo.setText(" " + mContext.getString(R.string.not_available));
//        }
        if (TextUtils.isEmpty(ipDependentDetail.getDependentUHID()) == false) {
            holder.mUhid.setText(ipDependentDetail.getDependentUHID());
        } else {
            holder.mUhid.setText(" " + mContext.getString(R.string.not_available));
        }
//        if (TextUtils.isEmpty(ipDependentDetail.getDEpendent_AadharNo()) == false) {
//            holder.mUpdateButton.setVisibility(View.INVISIBLE);
//        } else {
//            holder.mUpdateButton.setVisibility(View.VISIBLE);
//            holder.mUpdateButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent updateAdhaarIntent = new Intent(mContext, UpdateAdhaarActivity.class);
//                    updateAdhaarIntent.putExtra("InsSeqNo", ipDependentDetail.getInsSeqNo());
//                    updateAdhaarIntent.putExtra("patient_name", ipDependentDetail.getDependatName());
//                    mContext.startActivity(updateAdhaarIntent);
//                    mContext.finish();
//                }
//            });
//        }

        if (mIsFromDependant == false) {
            if (mSelectedItem != null && (mSelectedItem == ipDependentDetail)) {
                holder.mView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.mView.setBackgroundColor(mContext.getResources().getColor(R.color.panda));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // if aadhaar number is not present
//                    if (TextUtils.isEmpty(ipDependentDetail.getDEpendent_AadharNo()) == true) {
//                        Toast.makeText(mContext, mContext.getString(R.string.update_aadhaar_number_message), Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    /** setting the selected position for dependants and sending
                     *  it to the listener
                     */
                    mSelectedItem = ipDependentDetail;
                    if (mIsFromDependant == false) {
                        mSelecetedItemDetails.selectedPatientsDetails(mIpDetails.getIPNumber(), mSelectedItem.getDEpendent_AadharNo(),
                                mSelectedItem.getDependatName(), ipDependentDetail.getDependentUHID(), mSelectedItem.getDep_DateofBirth(), mSelectedItem.getDependent_Gender(),
                                mSelectedItem.getInsSeqNo(), mIpDetails.getDispCode(), mIpDetails.getLocAddress(), mIpDetails.getLocName());
                    }
                    notifyDataSetChanged();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mIpDependentDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
//        public final TextView mAadharNo;
        public final TextView mRealationShip;
        public final TextView mUhid;
//        public final Button mUpdateButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.choose_patient_item_name);
            //mAadharNo = (TextView) view.findViewById(R.id.choose_patient_item_aadhar_no);
            //mUpdateButton = (Button) view.findViewById(R.id.choose_patient_update_button);
            mRealationShip = (TextView) view.findViewById(R.id.choose_patient_item_relation);
            mUhid = (TextView) view.findViewById(R.id.choose_patient_item_dependent_uhid);
        }
    }

    public interface SelectedItemDetails {
        void selectedPatientsDetails(String ipNumber, String adhaarNumber, String patientName, String patientUHID, String patientDOB, String gender, String insSeqNo, String hospitaCode, String hospitalAddress, String hospitalName);
    }
}
