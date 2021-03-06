package uk.co.qubitssolutions.bharatradios.app.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;

import uk.co.qubitssolutions.bharatradios.R;
import uk.co.qubitssolutions.bharatradios.app.BharatRadiosApplication;
import uk.co.qubitssolutions.bharatradios.app.adapters.LanguageListAdapter;
import uk.co.qubitssolutions.bharatradios.app.others.DividerItemDecoration;
import uk.co.qubitssolutions.bharatradios.databinding.ActivityLanguagesBinding;
import uk.co.qubitssolutions.bharatradios.viewmodel.LanguagesViewModel;

public class LanguagesActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLanguagesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_languages);
        LanguageListAdapter adapter = new LanguageListAdapter((BharatRadiosApplication) getApplication());

        binding.setViewModel(new LanguagesViewModel(this));

        binding.languageListRecyclerView.setAdapter(adapter);
        binding.languageListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.languageListRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }
}
