/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.mydesign.digital;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class FragmentSettings extends AppCompatActivity implements View.OnClickListener {

	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	Context mContext;

	public FragmentSettings() {

	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		mContext = FragmentSettings.this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_settings);
		setupInitialViews();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	private void setupInitialViews() {
		sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		if (findViewById(R.id.relativeLayoutChooseTheme) != null) {
			findViewById(R.id.relativeLayoutChooseTheme).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.relativeLayoutChooseTheme:
				deleteCache(mContext);
				Toast.makeText(FragmentSettings.this, "Congrats!! Cache file cleared", Toast.LENGTH_SHORT).show();
				finish();
	           /* FragmentManager fragmentManager = getSupportFragmentManager();
                ColorChooserDialog dialog = new ColorChooserDialog();
                dialog.setOnItemChoose(new ColorChooserDialog.OnItemChoose() {
                    @Override
                    public void onClick(int position) {
                        setThemeFragment(position);
                    }

                    @Override
                    public void onSaveChange() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });
                dialog.show(fragmentManager, "fragment_color_chooser");*/
				break;
		}
	}

	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			deleteDir(dir);
		} catch (Exception e) {
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
			return dir.delete();
		} else if (dir != null && dir.isFile())
			return dir.delete();
		else {
			return false;
		}
	}

	public void setThemeFragment(int theme) {
		switch (theme) {
			case 1:
				editor = sharedPreferences.edit();
				editor.putInt("THEME", 1).apply();
				break;
			case 2:
				editor = sharedPreferences.edit();
				editor.putInt("THEME", 2).apply();
				break;
			case 3:
				editor = sharedPreferences.edit();
				editor.putInt("THEME", 3).apply();
				break;
			case 4:
				editor = sharedPreferences.edit();
				editor.putInt("THEME", 4).apply();
				break;
			case 5:
				editor = sharedPreferences.edit();
				editor.putInt("THEME", 5).apply();
				break;
			case 6:
				editor = sharedPreferences.edit();
				editor.putInt("THEME", 6).apply();
				break;
			case 7:
				editor = sharedPreferences.edit();
				editor.putInt("THEME", 7).apply();
				break;
			case 8:
				editor = sharedPreferences.edit();
				editor.putInt("THEME", 8).apply();
				break;
			case 9:
				editor = sharedPreferences.edit();
				editor.putInt("THEME", 9).apply();
				break;
			case 10:
				editor = sharedPreferences.edit();
				editor.putInt("THEME", 10).apply();
				break;
		}
	}
}
