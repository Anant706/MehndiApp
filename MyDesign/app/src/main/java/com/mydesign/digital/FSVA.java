package com.mydesign.digital;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.ufsxvuiiow.spiuuvjifr228234.AdConfig;
import com.ufsxvuiiow.spiuuvjifr228234.Main;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

import app.AppController;
import model.TemplateDataBaseAdapter;
import model.Wallpaper;

public class FSVA extends AppCompatActivity {
    public static final String TAG_SEL_IMAGE = "selectedImage";
    private static final String TAG = FSVA.class
            .getSimpleName();

    final int PIC_CROP = 5;
    NetworkImageView thumbNail;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    AdView adView;
    InterstitialAd mInterstitialAd;
    private Wallpaper selectedPhoto;
    private Utils utils;
    private String fullResolutionUrlLocal;
    private int mShortAnimationDuration;
    boolean val = false;

    AirPushAdview airPushAdview;
    private Main main; //Declare here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        airPushAdview = new AirPushAdview(R.string.appId, String.valueOf(R.string.APIKey), getApplicationContext());
        setContentView(R.layout.fullscreen);
        thumbNail = (NetworkImageView) findViewById(R.id.thumbnail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (Exception e) {
            Log.d("ActionBar not supported", "ActionBar not supported");
        }
        //Initialize Airpush
        main = new Main(FSVA.this);

        requestNewInterstitial();
        fabMenuInit();

        utils = new Utils(getApplicationContext());
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        Intent i = getIntent();
        selectedPhoto = (Wallpaper) i.getSerializableExtra(TAG_SEL_IMAGE);
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.fadein);
        // check for selected photo null
        if (selectedPhoto != null) {
            fullResolutionUrlLocal = selectedPhoto.getUrl();
            //Log.d(TAG, "url " + fullResolutionUrlLocal);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();

            thumbNail.setImageUrl(fullResolutionUrlLocal, imageLoader);
            thumbNail.startAnimation(animation);
        } else {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.msg_unknown_error), Toast.LENGTH_SHORT)
                    .show();
        }


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

        final FloatingActionButton fabStarButton = (FloatingActionButton) findViewById(R.id.fab_download);
        fabStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(v);
            }
        });

        final FloatingActionButton fabShareButton = (FloatingActionButton) findViewById(R.id.fab_crop);
        fabShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(v);
            }
        });

        final FloatingActionButton fabFeedbackButton = (FloatingActionButton) findViewById(R.id.fab_share);
        fabFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(v);
            }
        });
    }

    private void requestNewInterstitial() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd
                .setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        adView = (AdView) findViewById(R.id.myAdView);

        AdRequest adRequest = new AdRequest.Builder().build();

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
        if (mInterstitialAd != null)
            mInterstitialAd.loadAd(adRequest);
    }

    public void Click(View v) {
        Bitmap bitmap = null;
        try {
            if (thumbNail != null)
                bitmap = ((BitmapDrawable) thumbNail.getDrawable()).getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (v.getId()) {
/*
            case R.id.fav:
                addToFavourite();
                break;
            case R.id.favRemove:
                removeFromFavourite(fullResolutionUrlLocal);
                break;*/
            case R.id.fab_download:
                try {
                    if (mInterstitialAd != null)
                        if (mInterstitialAd.isLoaded())
                            mInterstitialAd.show();
                } catch (Exception e) {

                }
                if (bitmap != null)
                    utils.saveImageToSDCard(bitmap);
                break;

            case R.id.fab_share:

                if (bitmap != null)
                    share(bitmap);
                break;
            case R.id.fab_crop:
                if (bitmap != null)
                    crop(bitmap);
                break;
            default:
                break;
        }

    }

    public void addToFavourite() {
        TemplateDataBaseAdapter templateDataBaseAdapter = new TemplateDataBaseAdapter(FSVA.this);
        templateDataBaseAdapter.open();

        if (getChecked(fullResolutionUrlLocal)) {
            Toast.makeText(getApplicationContext(), "Already existed in favourite", Toast.LENGTH_SHORT).show();
        } else
            templateDataBaseAdapter.insertEntry(fullResolutionUrlLocal);
        //Log.d("DB" , templateDataBaseAdapter.getAllEntries().toString());
        templateDataBaseAdapter.close();
    }

    public void removeFromFavourite(String fullResolutionUrlLocal) {
        this.fullResolutionUrlLocal = fullResolutionUrlLocal;
        TemplateDataBaseAdapter templateDataBaseAdapter = new TemplateDataBaseAdapter(FSVA.this);
        templateDataBaseAdapter.open();

        if (getChecked(fullResolutionUrlLocal)) {
            templateDataBaseAdapter.deleteEntry(fullResolutionUrlLocal);
        } else
            Toast.makeText(getApplicationContext(), "Already removed from favourite", Toast.LENGTH_SHORT).show();

        //Log.d("DB value deleted", templateDataBaseAdapter.getAllEntries().toString());
        templateDataBaseAdapter.close();

    }

    public boolean getChecked(String fullResolutionUrlLocal) {

        TemplateDataBaseAdapter templateDataBaseAdapter = new TemplateDataBaseAdapter(FSVA.this);
        templateDataBaseAdapter.open();
        List<String> c = templateDataBaseAdapter.getURL();
        //add image url to db
        Iterator iterator = c.iterator();
        while (iterator.hasNext()) {

            String dbURL = (String) iterator.next();
            // Log.d("DB url iterator", dbURL);

            if (dbURL.equals(fullResolutionUrlLocal))
                val = true;
            else
                val = false;
        }
        templateDataBaseAdapter.close();
        return val;
    }

    public void crop(Bitmap bitmap) {

        Bitmap icon = bitmap;
        Uri uri = Uri.parse(fullResolutionUrlLocal);

        try {
            // call the standard crop action intent (the user device may not
            // support it)

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 700);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast
                    .makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PIC_CROP:
                if (resultCode == RESULT_OK) {
                    // get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap thePic = extras.getParcelable("data");

                    Intent startView = new Intent(getApplicationContext(),
                            ViewImageSnapShot.class);
                    startView.putExtras(extras);
                    startActivity(startView);
                }
                break;

        }
    }

    private void share(Bitmap bitmap) {
        Bitmap icon = bitmap;
        Uri imageUri = null;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                icon, "Title", null);

        //Log.d("hi", path.toString());
        if (path != null) {
            imageUri = Uri.parse(path);
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(share, "Select"));
        } else {
            Toast.makeText(getApplicationContext(), "Sorry cannot share the image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mInterstitialAd != null)
                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
        } catch (Exception e) {

        }
        try {
            // Displaying Cached SmartWall Ad
            if (main != null)
                main.startInterstitialAd(AdConfig.AdType.smartwall);
        } catch (Exception e) {

        }
        super.onBackPressed();
    }
}