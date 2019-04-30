package esiapp.esi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import esiapp.esi.R;
import esiapp.esi.model.Fragment_date_grid_item;
import esiapp.esi.model.retroModel.HospitalTimeSlotModel;
import esiapp.esi.ui.viewholder.SelectDateFragmentViewHolder;

/**
 * Created by soorianarayanan on 8/2/17.
 */

public class SelectDateFragmentAdapter extends RecyclerView.Adapter<SelectDateFragmentViewHolder> {

    private List<Fragment_date_grid_item> itemList;
    private Context mContext;
    private HospitalTimeSlotModel mSelectedItem;
    private ChooseTimeSlot mChooseTimeSlot;
    private List<HospitalTimeSlotModel> mHospitalTimeSlotList;

    public SelectDateFragmentAdapter(List<Fragment_date_grid_item> itemList, Context context, ChooseTimeSlot chooseTimeSlot) {
        this.itemList = itemList;
        this.mContext = context;
        mChooseTimeSlot = chooseTimeSlot;
    }

    public SelectDateFragmentAdapter(Context context, List<HospitalTimeSlotModel> timeSlotList, ChooseTimeSlot chooseTimeSlot) {
        this.mContext = context;
//        if (timeSlotList != null) {
//            mHospitalTimeSlotList.addAll(timeSlotList);
//        }
        mHospitalTimeSlotList = timeSlotList;
        mChooseTimeSlot = chooseTimeSlot;
    }

    public SelectDateFragmentAdapter(List<HospitalTimeSlotModel> timeSlotList, Context context, ChooseTimeSlot chooseTimeSlot, boolean b) {
        mContext = context;
        mChooseTimeSlot = chooseTimeSlot;
        if (timeSlotList != null) {
            mHospitalTimeSlotList.addAll(timeSlotList);
        }
    }

    @Override
    public SelectDateFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.date_fragment_item, null);
        SelectDateFragmentViewHolder rcv = new SelectDateFragmentViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final SelectDateFragmentViewHolder holder, int position) {

        final HospitalTimeSlotModel hospitalTimeSlot = mHospitalTimeSlotList.get(position);
        if (hospitalTimeSlot != null) {
            String[] timeArr = hospitalTimeSlot.getTimeSlotMinutes().split(" ");

            holder.time.setText(timeArr[0]);
            holder.am_pm.setText(timeArr[1]);

            if (hospitalTimeSlot.getSlotAppointmentStatus().equals("1")) {
                holder.time.setTextColor(mContext.getResources().getColor(R.color.booked_color));
                holder.am_pm.setTextColor(mContext.getResources().getColor(R.color.booked_color));
            } else if (hospitalTimeSlot.getSlotAppointmentStatus().equals("0")) {
                holder.time.setTextColor(mContext.getResources().getColor(R.color.available));
                holder.am_pm.setTextColor(mContext.getResources().getColor(R.color.available));
            }
        }


        if (mSelectedItem != null && (mSelectedItem == hospitalTimeSlot)) {
            holder.time.setTextColor(mContext.getResources().getColor(R.color.selected_color));
            holder.am_pm.setTextColor(mContext.getResources().getColor(R.color.selected_color));
        } else {
//            holder.mView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = hospitalTimeSlot;
                if (mSelectedItem.getSlotAppointmentStatus().equals("1")) {
                    Toast.makeText(mContext, mContext.getString(R.string.time_slot_not_available_message), Toast.LENGTH_LONG).show();
                } else {
                    String selectedTimeSlot = holder.time.getText() + " " + holder.am_pm.getText();
                    mChooseTimeSlot.onChooseTimeSlot(selectedTimeSlot);
                    notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mHospitalTimeSlotList.size();
    }

    public interface ChooseTimeSlot {
        void onChooseTimeSlot(String selectedTime);
    }
}
