package jp.rgfx_currentdir_ozero.facegenmush;

/**
 * Copyright (c) 2009, adamrocker (http://www.adamrocker.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *	 http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

import java.util.Random;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.ClipboardManager;
import android.util.Log;
//from: http://www.adamrocker.com/blog/mushroom-collaborates-with-simeji/


public class FacegenmushActivity 
	extends ListActivity 
	implements OnClickListener 
	{
	
	// properties ///////////////////////////////////////////////////////////
	
	private static final String ACTION_INTERCEPT = 
		"com.adamrocker.android.simeji.ACTION_INTERCEPT";
	private static final String REPLACE_KEY = "replace_key";
	private String TAG = "FaceGenMush";
	private String FROM_NOTIBAR_KEY = "fromNotiBar";
	//
	private String mReplaceString = "";
	private Button mRegenerateBtn, mRegreetBtn, mReplaceBtn, mUsagehistoryBtn, mCancelBtn;
	private TextView mFacecharTV;
	private static final int NOTIFY_STAY_ID = R.id.menu_notify_stay;
	//
	private static NotificationManager mNM;
	private Random random = new Random();
	private generateModel genDbHelper = new generateModel(this);
	private usageModel usageDbHelper = new usageModel(this);
	//
	private String gen[] = null;
	private boolean isStandalone;
	private boolean isStayOnNotificaiton = false;
	private boolean isLaunchedFromNotificaiton = false;
	//
	public static Typeface faceDejaVu;
	
	
	
	
	// GUIライフサイクル定義 ////////////////////////////////////////////////
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// init
		super.onCreate(savedInstanceState);
		Intent it = getIntent();
		String action = it.getAction();
		genDbHelper.open();
		usageDbHelper.open();
		gen = generate();// 生成
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		//is stand-alone or mushroom
		if (action != null && ACTION_INTERCEPT.equals(action)) {
			isStandalone = false;
			Log.d(TAG, "init:isStandAlone:false");
		} else {
			isStandalone = true;
			Log.d(TAG, "init:isStandAlone:true");
	        //通知領域からの起動かどうか
			if (it.getBooleanExtra(FROM_NOTIBAR_KEY, false)) {
				isLaunchedFromNotificaiton = true;
				Log.d(TAG, "init:isLaunchedFromNoti:true");
			}else{
				Log.d(TAG, "init:isLaunchedFromNoti:false");
			}
		}

		// Construct View
		if (!isStandalone) {
			setContentView(R.layout.mushroom);
		} else {
			setContentView(R.layout.main);
		}
		
		//set typeface
		faceDejaVu =Typeface.createFromAsset(this.getAssets(), "fonts/DejaVuSans.ttf"); 
		mFacecharTV = (TextView) findViewById(R.id.facechar_tv);
		mFacecharTV.setTypeface(faceDejaVu);

		
		//
		if (!isStandalone) {
			/* Simejiから呼出された時 */
			mReplaceString = it.getStringExtra(REPLACE_KEY);// 置換元の文字を取得
			// bind
			mRegenerateBtn = (Button) findViewById(R.id.regenerate_btn);
			mRegenerateBtn.setOnClickListener(this);
			mReplaceBtn = (Button) findViewById(R.id.replace_btn);
			mReplaceBtn.setOnClickListener(this);
			mCancelBtn = (Button) findViewById(R.id.cancel_btn);
			mCancelBtn.setOnClickListener(this);
			mUsagehistoryBtn = (Button) findViewById(R.id.usagehistory_btn);
			mUsagehistoryBtn.setOnClickListener(this);
			// mod
			mFacecharTV.setText((String) gen[0]);
			mFacecharTV.setTextColor(Color.rgb(Integer.parseInt(gen[1]),
					Integer.parseInt(gen[2]), Integer.parseInt(gen[3])));
			// ヒストリを表示
			dispGenHistory();
			//Log.d(TAG, "init:dialog-mush:done");
			
		} else {
			// Simeji以外から呼出された時
			// bind
			mRegreetBtn = (Button) findViewById(R.id.regreet_btn);
			mRegreetBtn.setOnClickListener(this);
			mUsagehistoryBtn = (Button) findViewById(R.id.usagehistory_btn);
			mUsagehistoryBtn.setOnClickListener(this);
			// mod
			mFacecharTV.setText(gen[0] + " < Hello, This is a mushroom app.");
			// ヒストリを表示
			dispGenHistory();
			//Log.d(TAG, "init:dialog-norm:done");
		}
	}
	
	//resumeの際はappstateにnotifyにstayしたかどうか残ってるので。。
	@Override
	public void onResume() {
		// init
		super.onResume();
		genDbHelper.open();
		usageDbHelper.open();
		
        //通知領域からの起動かどうか
		if (isStayOnNotificaiton) {
			isLaunchedFromNotificaiton = true;
			Log.d(TAG, "resume:isLaunchedFromNoti:true");
		}else{
			Log.d(TAG, "resume:isLaunchedFromNoti:false");
		}
	}
	
	//終了する際はDBへの接続を仕舞う
	@Override
	public void onPause() {
		// init
		super.onPause();
		genDbHelper.close();
		usageDbHelper.close();
		Log.d(TAG, "onPause:closed DB conn.");
		return;
	}
	@Override
	public void onStop() {
		// init
		super.onStop();
		genDbHelper.close();
		usageDbHelper.close();
		Log.d(TAG, "onStop:closed DB conn.");
		return;
	}
	@Override
	public void onDestroy() {
		// init
		super.onDestroy();
		genDbHelper.close();
		usageDbHelper.close();
		Log.d(TAG, "onDestroy:closed DB conn.");
		return;
	}
	
	
	
	
	// GUIイベント定義 ////////////////////////////////////////////////
	
	// クリックイベントディスパッチャ
	public void onClick(View v) {
		String result = null;
		if (v == mReplaceBtn) {
			Log.d(TAG, "click:usethis");
			result = gen[0] + " " + mReplaceString;
			replace(result);
		} else if (v == mCancelBtn) {
			Log.d(TAG, "click:cancel");
			result = mReplaceString;
			replace(result);
		} else if (v == mRegenerateBtn) {
			Log.d(TAG, "click:regenerate");
			gen = generate();// 生成
			mFacecharTV.setText(gen[0]);
			mFacecharTV.setTextColor(Color.rgb(Integer.parseInt(gen[1]),
					Integer.parseInt(gen[2]), Integer.parseInt(gen[3])));
			// ヒストリを再表示
			dispGenHistory();
		} else if (v == mRegreetBtn) {
			Log.d(TAG, "click:regenerate");
			gen = generate();// 生成
			mFacecharTV.setText(gen[0] + " < Hello, This is a mushroom app.");
			mFacecharTV.setTextColor(Color.rgb(Integer.parseInt(gen[1]),
					Integer.parseInt(gen[2]), Integer.parseInt(gen[3])));
			// ヒストリを再表示
			dispGenHistory();
		} else if (v == mUsagehistoryBtn) {
			Log.d(TAG, "click:usagehistory");
			Intent i = new Intent(this,UsagehistoryActivity.class);
			startActivity(i);
			//dispUsageHistory();
		}
	}

	// リストアイテムがクリックされた際の処理
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG, "onListItemClick():" + id);
		super.onListItemClick(l, v, position, id);
		Cursor c = genDbHelper.fetchItem(id);
		String face = c.getString(c
				.getColumnIndexOrThrow(generateModel.KEY_FACE));
		
		//
		String result = face + " " + mReplaceString;
		replace(result);
		return;
	}
	
	
	// メニューの定義
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
    	//メニューインフレーターを取得
    	MenuInflater inflater = getMenuInflater();
    	//xmlのリソースファイルを使用してメニューにアイテムを追加
    	inflater.inflate(R.menu.main, menu);
    	//できたらtrueを返す
		return true;
	}

	// メニューがクリックされた際
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case NOTIFY_STAY_ID:
			notifyStay();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	

	// その他処理 /////////////////////////////////////////////////////

	/**
	 * 元の文字を置き換える
	 * 
	 * @param result
	 *            Replacing string
	 */
	private void replace(String result) {

		// simeji呼び出しかどうか
		if (!isStandalone) {
			Intent data = new Intent();
			data.putExtra(REPLACE_KEY, result);
			setResult(RESULT_OK, data);
			
		} else {
			// クリップボードマネージャを宣言。
			ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			// クリップボードへ値をコピー。
			cm.setText(result);
			Toast.makeText(this, result + getString(R.string.toast_short_copyedtoclipboard),
				Toast.LENGTH_SHORT).show();
		}
		
		// 使用履歴に記録する
		usageDbHelper.createItem(result);

		finish();
	}
	
	
	//このアプリを通知バーの実行中欄に登録する
	private void notifyStay(){
		if(isLaunchedFromNotificaiton){
			mNM.cancelAll();
			isStayOnNotificaiton = false;
			isLaunchedFromNotificaiton = false;
		}else{
			String notiStr = getString(R.string.app_name);
			Notification noti = new Notification(
				R.drawable.icon, notiStr, System.currentTimeMillis());
			Intent int0 = new Intent(this,FacegenmushActivity.class);
			int0.putExtra(FROM_NOTIBAR_KEY , true);
			PendingIntent contentIntent = PendingIntent.getActivity(
				this, 0, int0,
				Intent.FLAG_ACTIVITY_NEW_TASK
			);
			noti.setLatestEventInfo(
				this, getString(R.string.app_name),
				getString(R.string.notify_desc), contentIntent
			);
			noti.flags= Notification.FLAG_ONGOING_EVENT;
			mNM.notify(R.string.app_name, noti);
			isStayOnNotificaiton = true;
			isLaunchedFromNotificaiton = true;
		}
		return;
	}
	
	

	// ランダムに選択
	private String[] randSelect(String[][] data) {
		int length = data.length;
		int idx = random.nextInt(length);
		// Log.d(TAG, "randSelect:[" + idx + "/" + length + "]");
		String[] retval = data[idx];
		return retval;
	}

	// 顔文字の組み立て
	private String[] generate() {
		faceparts parts = new faceparts(0);

		// ランダム選択
		// Log.d(TAG, "fcolor:");
		String[] color = randSelect(parts.fdcolor);
		// Log.d(TAG, "frin/ote/ome:");
		String[] rinkaku = randSelect(parts.fdrinkaku);
		String[] otete = randSelect(parts.fdotete);
		String[] omeme = randSelect(parts.fdomeme);
		// Log.d(TAG, "foku/hop:");
		String[] okuti = randSelect(parts.fdokuti);
		String[] hoppe = randSelect(parts.fdhoppe);

		// 組み立て
		String ielm = otete[0] + rinkaku[0]
			+ ((otete[1] == "" || otete[3] == "") ? "" : hoppe[0])
			+ omeme[0] + ((otete[2] == "") ? okuti[0] : otete[2])
			+ omeme[1]
			+ ((otete[3] == "" || otete[1] == "") ? "" : hoppe[1])
			+ rinkaku[1] + otete[4]
		;

		String retval[] = { ielm, color[0], color[1], color[2] };

		// 生成履歴に記録
		genDbHelper.createItem(ielm);

		return retval;
	}

	// 使用履歴を表示
	public void dispUsageHistory() {
		
		//AlertDialogがCharSequence[]しか使えないのでCursorから変換
		Cursor c = usageDbHelper.fetchAllItems();
		startManagingCursor(c);
		c.moveToFirst();
		int rowcount = c.getCount();
		int i = 0;
		final CharSequence[] items = new CharSequence[(rowcount)];
		for (i = 0; i < rowcount; i++) {
			items[i] = (CharSequence) c.getString(1);
			c.moveToNext();
		}

		//ダイアログの生成
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		//アイテムのイベント定義
		b.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// 履歴がクリックされた場合
				String result = (String) items[id] + " " + mReplaceString;
				replace(result);
				finish();
			}
		});
		// アラートダイアログの否定ボタンクリック時イベントの登録
		b.setNegativeButton(R.string.cancel,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		// アラートダイアログのキャンセルが可能かどうかを設定します
		b.setCancelable(true);
		AlertDialog alertDialog = b.create();
		// アラートダイアログを表示します
		alertDialog.show();
		return;
	}

	// 生成履歴を表示
	public void dispGenHistory() {
		//元データへのカーソル
		Cursor itemCursor = genDbHelper.fetchAllItems();
		startManagingCursor(itemCursor);
		// 表示する列
		String[] colnamesToBind = new String[] { generateModel.KEY_FACE };
		// 表示する列に関連付けるwidget
		int[] widgetsToBind = new int[] { R.id.text0 };
		// Now create a simple cursor adapter and set it to display
		MycursorAdapter mca = new MycursorAdapter(
			this, R.layout.mushroom_list, itemCursor,
			colnamesToBind, widgetsToBind);
		setListAdapter(mca);		
		return;
	}


	

	//
	// from : http://amachang.sakura.ne.jp/misc/aamaker/
	// 生来の助平者につき(´人`)狼藉御免 RT @amachang ヽ|＞ェ＜；|ノ ソースコードを見るなんてエッチ過ぎです！

}