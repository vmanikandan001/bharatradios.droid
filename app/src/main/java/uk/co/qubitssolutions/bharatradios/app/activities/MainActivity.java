package uk.co.qubitssolutions.bharatradios.app.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;
import java.util.concurrent.Callable;

import uk.co.qubitssolutions.bharatradios.BR;
import uk.co.qubitssolutions.bharatradios.R;
import uk.co.qubitssolutions.bharatradios.app.BharatRadiosApplication;
import uk.co.qubitssolutions.bharatradios.app.adapters.RadioListViewPagerAdapter;
import uk.co.qubitssolutions.bharatradios.model.Language;
import uk.co.qubitssolutions.bharatradios.services.data.radio.LanguageDataAsyncTask;
import uk.co.qubitssolutions.bharatradios.services.preferences.FavoriteLanguagePreferenceService;
import uk.co.qubitssolutions.bharatradios.viewmodel.ToolbarViewModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private BharatRadiosApplication application;
    private FloatingActionButton playStopToggleButton;
    private ViewPager viewPager;
    private ProgressBar progressBar;
    private ToolbarViewModel toolbarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = (BharatRadiosApplication) getApplication();

        View playerControls = findViewById(R.id.radio_player_controls);
        ViewDataBinding viewDataBinding = DataBindingUtil.bind(playerControls);
        viewDataBinding.setVariable(BR.viewModel, application.getPlayerViewModel());

        progressBar = (ProgressBar) findViewById(R.id.activity_main_progress_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbarViewModel = this.application.getToolbarViewModel();

        setSupportActionBar(toolbar);
        setupDrawer(toolbar);
        setupNav();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressBar();
        if (application.getLanguages().isEmpty()) {
            loadLanguages(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    setupRadiosOrShowLanguagesActivity();
                    return 0;
                }
            });
        } else {
            setupRadiosOrShowLanguagesActivity();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the run bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle run bar item clicks here. The run bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_action_fav_toggle:
                toggleFav(item);
                return true;
//            case R.id.menu_action_off_timer:
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_language:
                startActivity(new Intent(this, LanguagesActivity.class));
                break;
//            case R.id.nav_preference:
//                break;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
//            case R.id.nav_help:
//                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }




    /**********************************************************************************************
     * ******************************* PRIVATE METHODS*********************************************
     **********************************************************************************************/
    private void setupDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void setupNav() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadLanguages(final Callable<Integer> callable) {
        showProgressBar();
        LanguageDataAsyncTask languageAsync = new LanguageDataAsyncTask(
                getApplicationContext(),
                new LanguageDataAsyncTask.Callback() {
                    @Override
                    public void run(List<Language> languages) {
                        application.setLanguages(languages);
                        hideProgressBar();
                        try {
                            callable.call();
                        } catch (Exception e) {
                            Log.e(MainActivity.class.getName(), e.getMessage());
                        }
                    }
                });

        languageAsync.execute();
    }

    private void setupRadioListViewPager() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.language_tab);

        viewPager = (ViewPager) findViewById(R.id.radio_list_view_pager);
        RadioListViewPagerAdapter adapter = new RadioListViewPagerAdapter(getSupportFragmentManager(), application);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    // todo: move this to view model using binding
    private void toggleFav(MenuItem item) {
        this.toolbarViewModel.setFavoriteOnly(!this.toolbarViewModel.isFavoriteOnly());
        setupRadioListViewPager();
        if (this.toolbarViewModel.isFavoriteOnly()) {
            item.setIcon(R.drawable.ic_favorite_white_24dp_wrapped);
        } else {
            item.setIcon(R.drawable.ic_favorite_border_white_24dp_wrapped);
        }
    }

    private void setupRadiosOrShowLanguagesActivity() {
        if (FavoriteLanguagePreferenceService.getInstance(this).getAll().isEmpty()) {
            // languages are not selected - send the user to languages activity
            startActivity(new Intent(MainActivity.this, LanguagesActivity.class));
        }else{
            setupRadioListViewPager();
        }
    }
}
