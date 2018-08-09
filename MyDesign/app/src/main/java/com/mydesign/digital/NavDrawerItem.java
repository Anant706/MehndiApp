package com.mydesign.digital;

public class NavDrawerItem {

	private String albumId, albumTitle;
	// boolean flag to check for recent album
	private boolean isRecentAlbum = false;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;

	public NavDrawerItem() {
	}

	public NavDrawerItem(String albumId, String albumTitle) {
		this.albumId = albumId;
		this.albumTitle = albumTitle;
	}

	public NavDrawerItem(String albumId, String albumTitle,
			boolean isRecentAlbum) {
		this.albumTitle = albumTitle;
		this.isRecentAlbum = isRecentAlbum;
	}

	public NavDrawerItem(String albumId, String title,
			boolean isCounterVisible, String count) {
		this.albumId = albumId;
		this.albumTitle = title;
		// this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}

	public String getAlbumId() {
		return albumId;
	}

	public String getCount() {
		return this.count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public boolean getCounterVisibility() {
		return this.isCounterVisible;
	}

	public void setCounterVisibility(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getTitle() {
		return this.albumTitle;
	}

	public void setTitle(String title) {
		this.albumTitle = title;
	}

	public boolean isRecentAlbum() {
		return isRecentAlbum;
	}

	public void setRecentAlbum(boolean isRecentAlbum) {
		this.isRecentAlbum = isRecentAlbum;
	}
}
