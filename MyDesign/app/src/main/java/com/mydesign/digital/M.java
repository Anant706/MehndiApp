package com.mydesign.digital;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import com.ufsxvuiiow.spiuuvjifr228234.AdConfig;
import com.ufsxvuiiow.spiuuvjifr228234.Main;

import java.util.ArrayList;
import java.util.List;

import model.Category;

public class M extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = M.class.getSimpleName();
    //private ActionBarDrawerToggle mDrawerToggle;
    int count = 0;
    String[] album_title;

    TypedArray imgs;
    Resources res;
    private ListView mDrawerList;
    // Navigation drawer title
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public List<Category> albumsList;
    private MA adapter;
    // Insert your Application Package Name
    final String PACKAGE_NAME = "com.mydesign.digital";
    private SharedPreferences sharedPreferences;
    private ActionBarDrawerToggle mDrawerToggle;
    private int theme;
    Context mContext;

    private CoordinatorLayout coordinatorLayout;

    InterstitialAd mInterstitialAd;
    AdView adView;
    AirPushAdview airPushAdview;
    private Main main; //Declare here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = M.this;
        //theme();

        super.onCreate(savedInstanceState);

        airPushAdview = new AirPushAdview(R.string.appId, String.valueOf(R.string.APIKey), getApplicationContext());
        main = new Main(M.this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Initialize Airpush


        //grid album view
        mDrawerList = (ListView) findViewById(R.id.album_grid_list);
        // Getting the albums from shared preferences
        albumsList = new ArrayList<Category>();
        res = getResources();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        imgs = res.obtainTypedArray(R.array.album_img);
        album_title = res.getStringArray(R.array.album_list);

        //album_image = res.getStringArray(R.array.album_image);

        //Log.d(TAG, "album list" + album_title.length);

        // Loop through album_list to create your custom and specific list only

        for (int i = 0; i < album_title.length; i++) {
            albumsList.add(i, new Category(mContext, String.valueOf(i), album_title[i], imgs.getResourceId(i, -1)));
        }


        // Setting the list adapter
        adapter = new MA(this,albumsList);
        if (mDrawerList != null) {
            mDrawerList.setAdapter(adapter);
            mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        }
        imgs.recycle();

        fabMenuInit();

        requestNewInterstitial();
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);*/

       /* TemplateDataBaseAdapter templateDataBaseAdapter = new TemplateDataBaseAdapter(M.this);
        templateDataBaseAdapter.open();
        List<String> c = templateDataBaseAdapter.getURL();
        //add image url to db
        Iterator iterator = c.iterator();
        while (iterator.hasNext()) {
            Log.d("DB total values", (String) iterator.next());
        }
        templateDataBaseAdapter.close();*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Selected settings menu item
                // launch Settings activity
                Intent intent = new Intent(M.this,
                        FavouriteGrid.class);
                startActivity(intent);
                *//*try {
                    if (mInterstitialAd != null)
                        if (mInterstitialAd.isLoaded())
                            mInterstitialAd.show();
                } catch (Exception e) {

                }*//*
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    private void requestNewInterstitial() {
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        if (mInterstitialAd != null)
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        if (mInterstitialAd != null)
            mInterstitialAd.loadAd(adRequest);

        adView = (AdView) findViewById(R.id.myAdView);
        if (adView != null)
            adView.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    adView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int error) {
                    adView.setVisibility(View.GONE);
                }
            });

        if (adView != null)
            adView.loadAd(adRequest);
    }

    private void fabMenuInit() {
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(100);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });

        final com.getbase.floatingactionbutton.FloatingActionButton fabStarButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_star);
        fabStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateUsPlayStore();
            }
        });

        final com.getbase.floatingactionbutton.FloatingActionButton fabShareButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_share);
        fabShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTextUrl();
            }
        });

        final com.getbase.floatingactionbutton.FloatingActionButton fabFeedbackButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_feedback);
        fabFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"entertain706.zone@gmail.com"});
                Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
            }
        });
    }

    private void rateUsPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + PACKAGE_NAME);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + PACKAGE_NAME)));
        }
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT,
                "Beautiful Collections Store ");
        share.putExtra(
                Intent.EXTRA_TEXT,
                "Download Now:https://play.google.com/store/apps/details?id=" + PACKAGE_NAME);
        startActivity(Intent.createChooser(share, "Share link!"));
    }

    /**
     * Diplaying fragment view for selected list item
     */
    private void displayView(int position) {


        String albumId = albumsList.get(position).getId();
        Intent i = new Intent(mContext,
                GF.class);
        i.putExtra("albumList", Integer.parseInt(albumId));
        i.putExtra("albumTitle", albumsList.get(position).getTitle());
        i.putExtra("albumImg", position);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.logo)
                    .setTitle("Exit")
                    .setMessage("Do you want to Exit from this app?")
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface paramAnonymousDialogInterface,
                                        int paramAnonymousInt) {
                                    M.this.finish();
                                }
                            }).setNegativeButton("No", null).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent i = new Intent(mContext,
                    FragmentSettings.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Grid view item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            if (inNetworkAvailable()) {
                displayView(position);
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No Internet. Check your Connection !", Snackbar.LENGTH_LONG);
                        /*.setAction("TRY AGAIN", new View.OnClickListener() {
                            @Override
							public void onClick(View view) {

							}
						});*/

                // Message text color
                //snackbar.setActionTextColor(Color.WHITE);

                // Action button text color
                View snackBarView = snackbar.getView();
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);

                snackbar.show();
            }
        }
    }

    private boolean inNetworkAvailable() {
        ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connection.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    //Catch  theme changed from settings
    public void theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        Log.d(TAG, "theme color " + theme);
        settingTheme(mContext, theme);
    }

    //Theme set
    public static void settingTheme(Context context, int theme) {
        switch (theme) {
            case 1:
                context.setTheme(R.style.AppTheme);
                break;
            case 2:
                context.setTheme(R.style.AppTheme2);
                break;
            case 3:
                context.setTheme(R.style.AppTheme3);
                break;
            case 4:
                context.setTheme(R.style.AppTheme4);
                break;
            case 5:
                context.setTheme(R.style.AppTheme5);
                break;
            case 6:
                context.setTheme(R.style.AppTheme6);
                break;
            case 7:
                context.setTheme(R.style.AppTheme7);
                break;
            case 8:
                context.setTheme(R.style.AppTheme8);
                break;
            case 9:
                context.setTheme(R.style.AppTheme9);
                break;
            case 10:
                context.setTheme(R.style.AppTheme10);
                break;
            default:
                context.setTheme(R.style.AppTheme);
                break;
        }
    }

    @Override
    protected void onResume() {
        try {
            // Displaying Cached SmartWall Ad
            if (main != null)
                main.startInterstitialAd(AdConfig.AdType.smartwall);
        } catch (Exception e) {

        }
        super.onResume();
    }
}
