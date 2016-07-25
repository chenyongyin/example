package com.example.choisemorepictures;

//有问题发邮箱:wschenyongyin@qq.com
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.CustomDiaLog;
import util.upload;

import com.example.logic.ImgFileListActivity;

import adapter.GridViewAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private CustomDiaLog dialog;

	ListView listView;
	GridView gridView;
	// ArrayList<String> listfile = new ArrayList<String>();
	List<String> listfile = new ArrayList<String>();
	Bitmap bmp;
	int count;
	LinearLayout ll_send, ll_cancel;
	EditText et_text;
	String content;
	String imageFilePath;
	File temp;
	String servletPath = "http://192.168.0.19:8080/ServletForUpload/servlet/ImageUploadServlet";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listView1);
		ll_send = (LinearLayout) findViewById(R.id.ll_send);
		ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
		et_text = (EditText) findViewById(R.id.et_text);

		intDatas();
		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			if (bundle.getStringArrayList("files") != null) {
				listfile = bundle.getStringArrayList("files");

				count = listfile.size() + 1;
				GridViewAdapter adapter = new GridViewAdapter(
						MainActivity.this, listfile, count, bmp);
				gridView.setAdapter(adapter);
			}
		}

		ll_send.setOnClickListener(this);
		ll_cancel.setOnClickListener(this);

	}

	public void showDailog() {
		dialog = new CustomDiaLog(MainActivity.this, R.layout.dialog_evalute,
				R.style.dialog, new CustomDiaLog.LeaveMyDialogListener() {

					@Override
					public void onClick(View view) {

						System.out.println("aaaaaaaaaaaaaa");
						switch (view.getId()) {
						case R.id.btn_takephoto:

							imageFilePath = Environment
									.getExternalStorageDirectory()
									.getAbsolutePath()
									+ "/filename.jpg";
							temp = new File(imageFilePath);
							Uri imageFileUri = Uri.fromFile(temp);// 获取文件的Uri
							Intent it = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);// 跳转到相机Activity
							it.putExtra(
									android.provider.MediaStore.EXTRA_OUTPUT,
									imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
							startActivityForResult(it, 102);
							dialog.dismiss();
							break;
						case R.id.btn_picture:
							Intent intent = new Intent(MainActivity.this,
									ImgFileListActivity.class);

							startActivity(intent);
							dialog.dismiss();

							break;
						case R.id.btn_cancel:

							dialog.dismiss();
							break;

						default:
							break;
						}

					}
				});
		// 设置dialog弹出框显示在底部，并且宽度和屏幕一样
		Window window = dialog.getWindow();
		dialog.show();
		window.setGravity(Gravity.BOTTOM);
		window.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
	}

	public void chise() {
		Intent intent = new Intent();
		intent.setClass(this, ImgFileListActivity.class);
		startActivity(intent);
	}

	public void intDatas() {
		Resources res = getResources();
		gridView = (GridView) findViewById(R.id.noScrollgridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		bmp = BitmapFactory.decodeResource(res, R.drawable.icon_addpic_focused);
		count = 1;
		GridViewAdapter adapter = new GridViewAdapter(MainActivity.this, bmp,
				count);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new GridViewItemOnClick2());

	}

	public class GridViewItemOnClick2 implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (position + 1 == count) {

				showDailog();
			}

		}
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			upload load = new upload();
			load.postMethod(listfile);

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_send:
			content = et_text.getText().toString();
			System.out.println("内容:" + content + "图片内容:" + listfile);
			if (listfile != null || content != null) {
				new Thread(runnable).start();

			} else {
				Toast.makeText(MainActivity.this, "请输入发布的内容",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.ll_cancel:

			finish();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && requestCode == 102) {

			// Bitmap bmp = BitmapFactory.decodeFile(imageFilePath);
			listfile.add(imageFilePath);
			count = listfile.size() + 1;
			GridViewAdapter adapter = new GridViewAdapter(MainActivity.this,
					listfile, count, bmp);
			gridView.setAdapter(adapter);

		}
	}
}
