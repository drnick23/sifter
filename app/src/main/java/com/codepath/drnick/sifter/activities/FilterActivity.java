package com.codepath.drnick.sifter.activities;

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

    // passed in using intent
    SearchFilters searchFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Filter");

        ButterKnife.bind(this);

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
        searchFilters.setBeginDate(null);
    }

    @OnClick(R.id.etBeginDate)
    public void showDatePickerDialog(View v) {
        Log.d("DEBUG", "open date picker dialogue");
        DatePickerDialog dialog = new DatePickerDialog(this,
                this, 2015, 1, 1);
        dialog.show();
   }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // when setting date, set the search filters now instead of mapping at the end...
        //etBeginDate.setText(month+"/"+dayOfMonth+"/"+year);
        searchFilters.setBeginDate(year,month+1,dayOfMonth);
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
