package esiapp.esi.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import esiapp.esi.R;

/**
 * Created by soorianarayanan on 8/2/17.
 */

public class SelectDateFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView time;
    public TextView am_pm;
    public final View mView;

    public SelectDateFragmentViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(this);
        time = (TextView) itemView.findViewById(R.id.date_fragment_item_no);
        am_pm = (TextView) itemView.findViewById(R.id.date_fragment_item_am_pm);
    }

    @Override
    public void onClick(View view) {

    }
}
