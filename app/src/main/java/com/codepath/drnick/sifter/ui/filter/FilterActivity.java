package com.codepath.drnick.sifter.ui.filter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.codepath.drnick.sifter.R;
import com.codepath.drnick.sifter.models.SearchFilters;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.etBeginDate) EditText etBeginDate;
    @BindView(R.id.spSortOrder) Spinner spSortOrder;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbFashion) CheckBox cbFashion;
    @BindView(R.id.cbSports) CheckBox cbSports;
    @BindView(R.id.toolbar) Toolbar toolbar;

    // passed in using intent
    SearchFilters searchFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Filter");

        searchFilters = (SearchFilters) Parcels.unwrap(getIntent().getParcelableExtra("searchFilters"));

        mapSearchFilterIntoInputs();

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
        if (searchFilters.getBeginDate() != null) {
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
        dialog.show();
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
        // passback data
        Intent data = new Intent();
        data.putExtra("searchFilters", Parcels.wrap(searchFilters));
        setResult(RESULT_OK, data);
        finish();
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
