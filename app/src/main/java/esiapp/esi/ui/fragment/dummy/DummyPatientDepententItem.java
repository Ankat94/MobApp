package esiapp.esi.ui.fragment.dummy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import esiapp.esi.model.ChoosePatientDepdentListItemModel;

/**
 * Created by soorianarayanan on 9/2/17.
 */

public class DummyPatientDepententItem implements Serializable {
    public static List<ChoosePatientDepdentListItemModel> getITEMS() {
        List<ChoosePatientDepdentListItemModel> ITEMS = new ArrayList<ChoosePatientDepdentListItemModel>();
        ChoosePatientDepdentListItemModel choosePatientDepdentListItemModel = new ChoosePatientDepdentListItemModel("Kumar Deepak", "", false);
        ITEMS.add(choosePatientDepdentListItemModel);
        choosePatientDepdentListItemModel = new ChoosePatientDepdentListItemModel("Manoj Kumar", "1234ABCD4433AABB5", true);
        ITEMS.add(choosePatientDepdentListItemModel);
        choosePatientDepdentListItemModel = new ChoosePatientDepdentListItemModel("Arun Kumar", "", false);
        ITEMS.add(choosePatientDepdentListItemModel);

        return ITEMS;
    }

//    public static final List<ChoosePatientDepdentListItemModel> ITEMS = new ArrayList<ChoosePatientDepdentListItemModel>();

}
