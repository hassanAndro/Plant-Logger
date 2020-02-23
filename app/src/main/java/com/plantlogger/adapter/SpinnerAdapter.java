package com.plantlogger.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.plantlogger.R;
import com.plantlogger.activities.ParameterDataEntryInputActivity;
import com.plantlogger.models.SpinnerItemModel;

/***** Adapter class extends with ArrayAdapter ******/
public class SpinnerAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private ArrayList data;
    public Resources res;
    SpinnerItemModel tempValues = null;
    LayoutInflater inflater;

    /*************  SpinnerAdapter Constructor *****************/
//    public SpinnerAdapter(Activity activity, ArrayList<SpinnerItemModel> modelArrayList, Context context) {
    public SpinnerAdapter(ParameterDataEntryInputActivity activitySpinner,
                          int textViewResourceId,
                          ArrayList objects,
                          Resources resLocal) {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data = objects;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return super.getCount() - 1;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_row, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (SpinnerItemModel) data.get(position);

        TextView label = (TextView) row.findViewById(R.id.item);
        label.setText(tempValues.getItem_name());

        return row;
    }
}