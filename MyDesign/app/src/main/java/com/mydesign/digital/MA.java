package com.mydesign.digital;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import app.AppController;
import model.Category;

public class MA extends BaseAdapter {

	private static LayoutInflater inflater = null;
    private Activity context;
	private final String[] names = null;

	private List<Category> albumsList;
	public int sHeight, sWeight;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public MA(Activity context,
	          List<Category> albumsList) {
		super();
		this.context = context;
		this.albumsList = albumsList;
		inflater = (LayoutInflater) context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return albumsList.size();
	}

	@Override
	public Object getItem(int position) {
		return albumsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.main_grid_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.album_list);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.imageThumb);
			convertView.setTag(viewHolder);
		}

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		ViewHolder holder = (ViewHolder) convertView.getTag();

		if (holder != null) {
			holder.txtTitle.setText(albumsList.get(position).getTitle());
			//holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holder.image.setBackgroundResource(albumsList.get(position).getImg());
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView txtTitle;
		public ImageView image;
	}

}
