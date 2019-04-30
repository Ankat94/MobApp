package esiapp.esi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import esiapp.esi.R;
import esiapp.esi.adapter.model.SpinnerItemModel;

/**
 * Created by Randhir Kumar on 2/26/2017.
 */

public class SpinnerItemAdapter extends ArrayAdapter<SpinnerItemModel> {

    private int mGroupId;
    private Activity mActivity;
    private ArrayList<SpinnerItemModel> mSpinnerItemList;
    private LayoutInflater mLayoutInflater;

    public SpinnerItemAdapter(Activity activity, int groupId, int id, ArrayList<SpinnerItemModel> spinnerItemList) {
        super(activity, id, spinnerItemList);
        this.mSpinnerItemList = spinnerItemList;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mGroupId = groupId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = mLayoutInflater.inflate(mGroupId, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.spinner_item_image);
        imageView.setImageResource(mSpinnerItemList.get(position).getmImageResourceId());
        TextView textView = (TextView) itemView.findViewById(R.id.spinner_item_txt);
        textView.setText(mSpinnerItemList.get(position).getmSpinnerItemText());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent) {
        return getView(position, convertView, parent);

    }
}


