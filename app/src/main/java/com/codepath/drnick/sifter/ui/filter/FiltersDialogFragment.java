package com.codepath.drnick.sifter.ui.filter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.codepath.drnick.sifter.R;
import com.codepath.drnick.sifter.models.SearchFilters;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by nick on 10/21/16.
 */

public class FiltersDialogFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener {

    @BindView(R.id.etBeginDate) EditText etBeginDate;
    @BindView(R.id.spSortOrder) Spinner spSortOrder;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbFashion) CheckBox cbFashion;
    @BindView(R.id.cbSports) CheckBox cbSports;

    // passed in using intent
    SearchFilters searchFilters;

    public FiltersDialogFragment() {

    }

    public static FiltersDialogFragment newInstance(String title) {
        FiltersDialogFragment frag = new FiltersDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        //ButterKnife.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Search Filters");
        getDialog().setTitle(title);

        cbArts.setChecked(true);

        //searchFilters = (SearchFilters) Parcels.unwrap(getIntent().getParcelableExtra("searchFilters"));
        //mapSearchFilterIntoInputs();

    }

    void mapSearchFilterIntoInputs() {
        etBeginDate.setText(searchFilters.getBeginDateForUserDisplay());
        setSpinnerToValue(spSortOrder, searchFilters.getSort());
        for (String news : searchFilters.getNewsDesk()) {
            Log.d("DEBUG", "news item: " + news);
            if (news.equals("arts")) {
                cbArts.setChecked(true);
            } else if (news.equals("fashion")) {
                cbFashion.setChecked(true);
            } else if (news.equals("sports")) {
                cbSports.setChecked(true);
            }
        }

    }

    void mapInputsIntoSearchFilter() {
        // update the searchFilter based on all our inputs
        ArrayList<String> news = new ArrayList<>();
        if (cbArts.isChecked()) {
            news.add("arts");
        }
        if (cbFashion.isChecked()) {
            news.add("fashion");
        }
        if (cbSports.isChecked()) {
            news.add("sports");
        }
        searchFilters.setSort(spSortOrder.getSelectedItem().toString());
        searchFilters.setNewsDesk(news);
    }

    @OnClick(R.id.etBeginDate)
    public void showDatePickerDialog(View v) {
        int year;
        int month;
        int day;

        Log.d("DEBUG", "open date picker dialogue");
        /*if (searchFilters.getBeginDate() != null) {
            Calendar c = searchFilters.getBeginDate();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

        } else {
            // let's default a date picker to a year ago if user hasn't set it.
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR)-1;
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog dialog = new DatePickerDialog(this,
                this, year, month, day);
        dialog.show();*/
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // when setting date, set the search filters now instead of mapping at the end...
        //etBeginDate.setText(month+"/"+dayOfMonth+"/"+year);
        searchFilters.setBeginDate(year,month,dayOfMonth);
        etBeginDate.setText(searchFilters.getBeginDateForUserDisplay());
    }

    @OnClick(R.id.btSave)
    public void onSave() {
        mapInputsIntoSearchFilter();
        /*// passback data
        Intent data = new Intent();
        data.putExtra("searchFilters", Parcels.wrap(searchFilters));
        setResult(RESULT_OK, data);*/
        dismiss();
    }

    @OnCheckedChanged({ R.id.cbArts, R.id.cbFashion, R.id.cbSports})
    public void onCheckedChanged(CompoundButton view, boolean checked) {
        // whereas we could update searchFilters on the fly, it's simpler and cleaner to do it on save
    }

    @OnItemSelected(R.id.spSortOrder)
    public void onSpinnerSelected(AdapterView<?> parent, View view,
                                  int pos, long id) {
        Log.d("DEBUG","selected spinner pos "+pos);
        // whereas we could update searchFilters on the fly, it's simpler and cleaner to do it on save
    }

    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }


}
