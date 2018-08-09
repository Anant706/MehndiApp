package com.mydesign.digital;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import app.AppController;
import model.Category;
import model.Wallpaper;

public class GVA extends BaseAdapter {

    private Context _activity;
    public Category albumsList;
    private static LayoutInflater inflater = null;
    private List<Wallpaper> wallpapersList = new ArrayList<Wallpaper>();
    private int imageWidth;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public GVA(Activity activity, List<Wallpaper> wallpapersList,
               int imageWidth) {
        this._activity = activity;
        this.wallpapersList = wallpapersList;
        this.imageWidth = imageWidth;
        albumsList = new Category(_activity);
        inflater = (LayoutInflater) _activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.wallpapersList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.wallpapersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.grid_item_photo, null);
            ViewHolder viewHolder = new ViewHolder();
            // Grid thumbnail image view
            viewHolder.thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
            // viewHolder.favButton = (ToggleButton) convertView.findViewById(R.id.favourite);
            convertView.setTag(viewHolder);

        }
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final Wallpaper p = wallpapersList.get(position);


        if (holder != null) {

            holder.thumbNail.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.thumbNail.setLayoutParams(new RelativeLayout.LayoutParams(imageWidth,
                    imageWidth));
            holder.thumbNail.setImageUrl(p.getUrl(), imageLoader);
            // holder.thumbNail.setTag(p.getUrl());


/*
            holder.favButton.setChecked(tgpref);
            holder.favButton.setBackgroundDrawable(ContextCompat.getDrawable(_activity.getApplicationContext(), R.drawable.ic_favorite_border_black_96dp));
            holder.favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        albumsList.addToFavourite(holder.thumbNail.getTag().toString());
                        holder.favButton.setBackgroundDrawable(ContextCompat.getDrawable(_activity.getApplicationContext(), R.drawable.ic_favorite_black_96dp));
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("Favourite", holder.favButton.isChecked()); // value to store
                        editor.commit();

                    } else {
                        albumsList.removeFromFavourite(holder.thumbNail.getTag().toString());
                        holder.favButton.setBackgroundDrawable(ContextCompat.getDrawable(_activity.getApplicationContext(), R.drawable.ic_favorite_border_black_96dp));
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("Favourite", holder.favButton.isChecked()); // value to store
                        editor.commit();
                    }
                }

            });*/


        }
        return convertView;
    }

    public class ViewHolder {
        public NetworkImageView thumbNail;
    }
}