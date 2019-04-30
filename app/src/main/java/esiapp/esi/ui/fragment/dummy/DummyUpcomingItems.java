package esiapp.esi.ui.fragment.dummy;

import java.util.ArrayList;
import java.util.List;

import esiapp.esi.model.UpcomingModel;

/**
 * Created by soorianarayanan on 9/2/17.
 */

public class DummyUpcomingItems {

    public static List<UpcomingModel> getITEMS() {
//        UpcomingModel upcomingModel = new UpcomingModel("Abhimanyu Panda", "7654321", "February 9, 10:30 AM", true);
//        ITEMS.add(upcomingModel);
//        upcomingModel = new UpcomingModel("Kumar Deepak", "77654321", "February 9, 11:30 AM", false);
//        ITEMS.add(upcomingModel);
        return ITEMS;
    }

    public static final List<UpcomingModel> ITEMS = new ArrayList<UpcomingModel>();

}
