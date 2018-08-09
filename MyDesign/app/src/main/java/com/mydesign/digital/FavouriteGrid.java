package com.mydesign.digital;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.AppConst;
import app.AppController;
import model.TemplateDataBaseAdapter;
import model.Wallpaper;
import util.PrefManager;

public class FavouriteGrid extends AppCompatActivity {
    private static final String TAG = FavouriteGrid.class.getSimpleName();
    private static final String bundleAlbumId = "albumId";
    // Picasa JSON response node keys
    private static final String TAG_FEED = "feed", TAG_ENTRY = "entry",
            TAG_MEDIA_GROUP = "media$group",
            TAG_MEDIA_CONTENT = "media$content", TAG_IMG_URL = "url",
            TAG_IMG_WIDTH = "width", TAG_IMG_HEIGHT = "height", TAG_ID = "id",
            TAG_T = "$t";
    InterstitialAd mInterstitialAd;
    AdView adView, adView2;
    int count = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Utils utils;
    private GVA adapter;
    private GridView gridView;
    private int columnWidth;

    private List<Wallpaper> photosList;
    private PrefManager pref;
    MovingImageView image;
    boolean toggleState = true;
    boolean toggleCustomMovement = true;
    int[] imageList = {R.drawable.tattoo1};
    int pos = 0;
    int selectedAlbumId;
    String selectedAlbumTitle = null;
    int selectedAlbumImg;
    //JsonObjectRequest jsonObjReq;
    String url;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public FavouriteGrid() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (Exception e) {
            Log.d("ActionBar not supported", "ActionBar not supported");
        }


        photosList = new ArrayList<Wallpaper>();
        pref = new PrefManager(getApplicationContext());


        try {
            getSupportActionBar().setTitle(selectedAlbumTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initItem();
        //Log.d(TAG, "Final request url: " + url);

        // Hiding the grid view and showing loader image before making the http request
        gridView = (GridView) findViewById(R.id.grid_view);
        // gridView.setExpanded(true);
        //try to use recycler view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setNestedScrollingEnabled(gridView, true);
        }

        adView = (AdView) findViewById(R.id.myAdView);

        utils = new Utils(getApplicationContext());

        requestNewInterstitial();

        makeQuery();
        if (count == 0)
            Toast.makeText(getApplicationContext(), "No favourite Image to display", Toast.LENGTH_SHORT).show();

        if (url != null)
            AppController.getInstance().getRequestQueue().getCache().remove(url);

        // Disable the cache for this url, so that it always fetches updated
        // json
//		jsonObjReq.setShouldCache(false);

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjReq);

        // Initilizing Grid View
        InitializeGridLayout();

        // Grid view adapter
        adapter = new GVA(FavouriteGrid.this, photosList, columnWidth);
        // setting grid view adapter
        gridView.setAdapter(adapter);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        // Grid item select listener
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // On selecting the grid image, we launch fullscreen activity
                Intent i = new Intent(getApplicationContext(),
                        FullActivity.class);
                // Passing selected image to fullscreen activity
                Wallpaper photo = photosList.get(position);
                i.putExtra(FullActivity.TAG_SEL_IMAGE, photo);
                startActivity(i);

            }
        });
        image = (MovingImageView) findViewById(R.id.image);

        switch (0) {
            case 0:
                if (image != null)
                    image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tattoo1));
                break;


        }

        /*image.getMovingAnimator().addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i("Sample MovingImageView", "Start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i("Sample MovingImageView", "End");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Sample MovingImageView", "Cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i("Sample MovingImageView", "Repeat");
            }
        });*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void makeQuery() {
        TemplateDataBaseAdapter templateDataBaseAdapter = new TemplateDataBaseAdapter(FavouriteGrid.this);
        templateDataBaseAdapter.open();
        List<String> c = templateDataBaseAdapter.getURL();
        //add image url to db
        Iterator iterator = c.iterator();
        count = c.size();
        while (iterator.hasNext()) {
            String str = (String) iterator.next();
            Log.d("DB total values", str);
            Wallpaper p = new Wallpaper(str);
            photosList.add(p);
        }
        templateDataBaseAdapter.close();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GF Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mydesign/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GF Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mydesign/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private void initItem() {
        switch (0) {
            case 0:
                url = AppConst.URL_TATOO_M;
                break;
            default:

        }
    }

	/*private void makeJsonCall() {
        */

    /**
     * Making volley's json object request to fetch list of photos of an
     * album
     *//*
        jsonObjReq = new JsonObjectRequest(Method.GET, url,
				null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				*//*Log.d(TAG,
						"List of photos json reponse: "
								+ response.toString());*//*
				try {
					// Parsing the json response
					JSONArray entry = response.getJSONObject(TAG_FEED)
							.getJSONArray(TAG_ENTRY);

					// looping through each photo and adding it to list
					// data set
					for (int i = 0; i < entry.length(); i++) {
						JSONObject photoObj = (JSONObject) entry.get(i);
						JSONArray mediacontentArry = photoObj
								.getJSONObject(TAG_MEDIA_GROUP)
								.getJSONArray(TAG_MEDIA_CONTENT);

						if (mediacontentArry.length() > 0) {
							JSONObject mediaObj = (JSONObject) mediacontentArry
									.get(0);

							String url = mediaObj
									.getString(TAG_IMG_URL);

							String photoJson = photoObj.getJSONObject(
									TAG_ID).getString(TAG_T)
									+ "&imgmax=d";

							int width = mediaObj.getInt(TAG_IMG_WIDTH);
							int height = mediaObj
									.getInt(TAG_IMG_HEIGHT);

							Wallpaper p = new Wallpaper(photoJson, url,
									width, height);

							// Adding the photo to list data set
							photosList.add(p);

						*//*	Log.d(TAG, "Photo: " + url + ", w: "
									+ width + ", h: " + height);*//*
						}
					}

					// Notify list adapter about dataset changes. So
					// that it renders grid again
					adapter.notifyDataSetChanged();

					// Hide the loader, make grid visible
					pbLoader.setVisibility(View.GONE);
					gridView.setVisibility(View.VISIBLE);

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),
							getString(R.string.msg_unknown_error),
							Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Error: " + error.getMessage());
				// unable to fetch wallpapers
				// either google username is wrong or
				// devices doesn't have internet connection
			    *//*Toast.makeText(getApplicationContext(),
                                getString(R.string.msg_wall_fetch_error),
								Toast.LENGTH_LONG).show();*//*

			}
		});
	}
*/
    public void clickImage(View v) {
        if (toggleState) {
            image.getMovingAnimator().pause();
            Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
        } else {
            image.getMovingAnimator().resume();
            Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
        }
        toggleState = !toggleState;
    }

    private void requestNewInterstitial() {
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        if (mInterstitialAd != null)
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        if (mInterstitialAd != null)
            mInterstitialAd.loadAd(adRequest);

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

    private void InitializeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConst.GRID_PADDING, r.getDisplayMetrics());

        // Column width
        columnWidth = (int) ((utils.getScreenWidth() - ((pref
                .getNoOfGridColumns() + 1) * padding)) / pref
                .getNoOfGridColumns());

        // Setting number of grid columns
        gridView.setNumColumns(pref.getNoOfGridColumns());
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);

        // Setting horizontal and vertical padding
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    @Override
    public void onBackPressed() {

        try {
            // Displaying Cached SmartWall Ad

            if (mInterstitialAd != null)
                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
        } catch (Exception e) {

        }
        super.onBackPressed();
    }

}
