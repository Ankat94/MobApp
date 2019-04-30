//package esiapp.esi.adapter;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.List;
//
//import esiapp.esi.R;
//import esiapp.esi.model.retroModel.AppointMentModel;
//import esiapp.esi.ui.fragment.HistoryFragment;
//import esiapp.esi.ui.fragment.dummy.DummyContent.DummyItem;
//
///**
// * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
// * specified {@link HistoryFragment.OnListFragmentInteractionListener}.
// * TODO: Replace the implementation with code for your data type.
// */
//public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {
//
//    private final List<DummyItem> mValues;
//    private final HistoryFragment.OnListFragmentInteractionListener mListener;
//
//    public HistoryRecyclerViewAdapter(List<DummyItem> items, HistoryFragment.OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.fragment_item2, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText("No history available");
//        holder.mContentView.setText("");
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return 1;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public final View mView;
//        public final TextView mIdView;
//        public final TextView mContentView;
//        public AppointMentModel mItem;
//
//        public ViewHolder(View view) {
//            super(view);
//            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.id);
//            mContentView = (TextView) view.findViewById(R.id.content);
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
//    }
//}
