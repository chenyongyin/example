package com.example.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.choisemorepictures.MainActivity;
import com.example.choisemorepictures.R;
import com.example.logic.ImgsAdapter.OnItemClickClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImgsActivity extends Activity implements OnClickListener {

	Bundle bundle;
	FileTraversal fileTraversal;
	GridView imgGridView;
	ImgsAdapter imgsAdapter;
	LinearLayout select_layout;
	Util util;
	RelativeLayout relativeLayout2;
	HashMap<Integer, ImageView> hashImage;
	LinearLayout btn_ok, btn_back;
	ArrayList<String> filelist;
	RelativeLayout back;
	Button count;
	TextView btn_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogrally);

		imgGridView = (GridView) findViewById(R.id.gridView1);
		bundle = getIntent().getExtras();
		fileTraversal = bundle.getParcelable("data");
		imgsAdapter = new ImgsAdapter(this, fileTraversal.filecontent,
				onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);
		select_layout = (LinearLayout) findViewById(R.id.selected_image_layout);
		relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
		btn_ok = (LinearLayout) findViewById(R.id.btn_ok);
		back = (RelativeLayout) findViewById(R.id.back);
		count = (Button) findViewById(R.id.count);
		btn_back=(LinearLayout) findViewById(R.id.btn_back);
		btn_cancel=(TextView) findViewById(R.id.btn_cancel);
		
		

		hashImage = new HashMap<Integer, ImageView>();
		filelist = new ArrayList<String>();
		// imgGridView.setOnItemClickListener(this);
		util = new Util(this);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendfiles();

			}
		});
		btn_back.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

	}

	class BottomImgIcon implements OnItemClickListener {

		int index;

		public BottomImgIcon(int index) {
			this.index = index;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		}
	}

	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath, int index, CheckBox checkBox)
			throws FileNotFoundException {
		LinearLayout.LayoutParams params = new LayoutParams(
				relativeLayout2.getMeasuredHeight() - 10,
				relativeLayout2.getMeasuredHeight() - 10);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.drawable.imgbg);
		float alpha = 100;
		imageView.setAlpha(alpha);
		util.imgExcute(imageView, imgCallBack, filepath);
		imageView.setOnClickListener(new ImgOnclick(filepath, checkBox));
		return imageView;
	}

	ImgCallBack imgCallBack = new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};

	class ImgOnclick implements OnClickListener {
		String filepath;
		CheckBox checkBox;

		public ImgOnclick(String filepath, CheckBox checkBox) {
			this.filepath = filepath;
			this.checkBox = checkBox;
		}

		@Override
		public void onClick(View arg0) {
			checkBox.setChecked(false);
			select_layout.removeView(arg0);
			count.setText(select_layout.getChildCount() + "");
			filelist.remove(filepath);
		}
	}

	ImgsAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filapath = fileTraversal.filecontent.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				select_layout.removeView(hashImage.get(Position));
				filelist.remove(filapath);
				count.setText(select_layout.getChildCount() + "");
			} else {
				try {
					checkBox.setChecked(true);
					Log.i("img", "img choise position->" + Position);
					ImageView imageView = iconImage(filapath, Position,
							checkBox);
					if (imageView != null) {
						if (select_layout.getChildCount() < 9) {
							hashImage.put(Position, imageView);
							filelist.add(filapath);
							select_layout.addView(imageView);
							count.setText(select_layout.getChildCount() + "");
						} else {
							checkBox.setChecked(false);
							Toast.makeText(ImgsActivity.this, "最多只能选9张图片",
									Toast.LENGTH_SHORT).show();
						}

					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void tobreak(View view) {
		finish();
	}

	public void sendfiles() {
		Intent intent = new Intent(this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("files", filelist);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();

	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(ImgsActivity.this, ImgFileListActivity.class));
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			startActivity(new Intent(ImgsActivity.this, ImgFileListActivity.class));
			finish();
			break;
		case R.id.btn_cancel:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		
		default:
			break;
		}

	}
}
