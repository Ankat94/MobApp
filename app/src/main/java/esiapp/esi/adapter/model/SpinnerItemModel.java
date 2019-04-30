package esiapp.esi.adapter.model;

/**
 * Created by Randhir Kumar on 2/26/2017.
 */

public class SpinnerItemModel {
    private String mSpinnerItemText;
    private Integer mImageResourceId;

    public SpinnerItemModel(String spinnerItemText, Integer imageResourceId) {
        this.mSpinnerItemText = spinnerItemText;
        this.mImageResourceId = imageResourceId;
    }

    public String getmSpinnerItemText() {
        return mSpinnerItemText;
    }

    public void setmSpinnerItemText(String mSpinnerItemText) {
        this.mSpinnerItemText = mSpinnerItemText;
    }

    public Integer getmImageResourceId() {
        return mImageResourceId;
    }

    public void setmImageResourceId(Integer mImageResourceId) {
        this.mImageResourceId = mImageResourceId;
    }
}
