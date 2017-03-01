package info.gogou.gogou.ui.photo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import info.gogou.gogou.adapter.FolderAdapter;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.model.Demand;
import info.gogou.gogou.model.Trip;
import info.gogou.gogou.ui.RequirementEditActivity;
import info.gogou.gogou.ui.ScheduleEditActivity;


/**
 * 这个类主要是用来进行显示包含图片的文件夹
 * Not being used for now!!!
 */
public class ImageFile extends Activity {

	private FolderAdapter folderAdapter;
	private Button bt_cancel;
	private Context mContext;

	private Trip tripResult;
	private Demand demandResult;

	private String flag;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*setContentView(Res.getLayoutID("plugin_camera_image_file"));
		PublicWay.activityList.add(this);
		mContext = this;
		bt_cancel = (Button) findViewById(Res.getWidgetID("cancel"));
		bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(Res.getWidgetID("fileGridView"));
		TextView textView = (TextView) findViewById(Res.getWidgetID("headerTitle"));
		textView.setText(Res.getString("photo"));
		folderAdapter = new FolderAdapter(this);
		gridView.setAdapter(folderAdapter);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		flag = intent.getStringExtra(GoGouIntents.FLAG);
		// 取出Bundle中的数据
		if(flag.equals(GoGouIntents.TRIP_FLAG))
			tripResult = (Trip)bundle.getSerializable(GoGouIntents.TRIP);
		else if(flag.equals(GoGouIntents.DEMAND_FLAG))
			demandResult = (Demand)bundle.getSerializable(GoGouIntents.DEMAND);*/

	}

	private class CancelListener implements OnClickListener {// 取消按钮的监听
		public void onClick(View v) {
			//清空选择的图片
			//Bimp.tempSelectBitmap.clear();
			//Bimp.max = 0;
			Intent intent = new Intent();
			Bundle bundle = new Bundle();

			if(flag.equals(GoGouIntents.TRIP_FLAG)){
				bundle.putSerializable(GoGouIntents.TRIP,tripResult);
				intent.putExtras(bundle);
				intent.putExtra(GoGouIntents.FLAG,flag);
				intent.setClass(mContext, ScheduleEditActivity.class);
				startActivity(intent);
				finish();
			}

			else if(flag.equals(GoGouIntents.DEMAND_FLAG)){
				bundle.putSerializable(GoGouIntents.DEMAND,demandResult);
				intent.putExtras(bundle);
				intent.putExtra(GoGouIntents.FLAG, flag);
				intent.setClass(mContext,RequirementEditActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();

			if(flag.equals(GoGouIntents.TRIP_FLAG)){
				bundle.putSerializable(GoGouIntents.TRIP,tripResult);
				intent.putExtras(bundle);
				intent.putExtra(GoGouIntents.FLAG,flag);
				intent.setClass(mContext, ScheduleEditActivity.class);
				startActivity(intent);
				finish();
			}

			else if(flag.equals(GoGouIntents.DEMAND_FLAG)){
				bundle.putSerializable(GoGouIntents.DEMAND,demandResult);
				intent.putExtras(bundle);
				intent.putExtra(GoGouIntents.FLAG, flag);
				intent.setClass(mContext,RequirementEditActivity.class);
				startActivity(intent);
				finish();
			}
		}
		
		return true;
	}

}
