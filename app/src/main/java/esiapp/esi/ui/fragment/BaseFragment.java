package esiapp.esi.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by soorianarayanan on 7/2/17.
 */

public class BaseFragment extends Fragment {

    public static Activity mActivity;
    public static Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
