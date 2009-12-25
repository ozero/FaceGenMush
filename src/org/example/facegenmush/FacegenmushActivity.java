package org.example.facegenmush;

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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

public class FacegenmushActivity extends Activity implements OnClickListener {
	private static final String ACTION_INTERCEPT = "com.adamrocker.android.simeji.ACTION_INTERCEPT";
	private static final String REPLACE_KEY = "replace_key";
	private String mReplaceString;
	private Button mRegenerateBtn;
	private Button mRegreetBtn;
	private Button mReplaceBtn;
	private Button mCancelBtn;
	private TextView mFacecharTV;
	private Random random = new Random();
	private String gen[] = null;
	private String TAG = "FaceGenMush";

	// http://www.adamrocker.com/blog/mushroom-collaborates-with-simeji/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent it = getIntent();
		String action = it.getAction();
		gen = generate();
		if (action != null && ACTION_INTERCEPT.equals(action)) {
			/* Simejiから呼出された時 */
			mReplaceString = it.getStringExtra(REPLACE_KEY);// 置換元の文字を取得
			// Construct View
			setContentView(R.layout.mushroom);
			// bind
			mRegenerateBtn = (Button) findViewById(R.id.regenerate_btn);
			mRegenerateBtn.setOnClickListener(this);
			mReplaceBtn = (Button) findViewById(R.id.replace_btn);
			mReplaceBtn.setOnClickListener(this);
			mCancelBtn = (Button) findViewById(R.id.cancel_btn);
			mCancelBtn.setOnClickListener(this);
			// mod
			mFacecharTV = (TextView) findViewById(R.id.facechar_tv);
			mFacecharTV.setText((String) gen[0]);
			mFacecharTV.setTextColor(Color.rgb(Integer.parseInt(gen[1]),
					Integer.parseInt(gen[2]), Integer.parseInt(gen[3])));

			Log.d(TAG, "init:dialog-mush:done");
		} else {
			// Simeji以外から呼出された時
			// Construct View
			setContentView(R.layout.main);
			// bind
			mRegreetBtn = (Button) findViewById(R.id.regreet_btn);
			mRegreetBtn.setOnClickListener(this);
			// mod
			mFacecharTV = (TextView) findViewById(R.id.facechar_tv);
			mFacecharTV.setText(gen[0] + " < Hello, This is a mushroom app.");
			Log.d(TAG, "init:dialog-norm:done");
		}
	}

	// event dispatcher
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
			gen = generate();
			mFacecharTV.setText(gen[0]);
			mFacecharTV.setTextColor(Color.rgb(Integer.parseInt(gen[1]),
					Integer.parseInt(gen[2]), Integer.parseInt(gen[3])));
		} else if (v == mRegreetBtn) {
			Log.d(TAG, "click:regenerate");
			gen = generate();
			mFacecharTV.setText(gen[0] + " < Hello, This is a mushroom app.");
			mFacecharTV.setTextColor(Color.rgb(Integer.parseInt(gen[1]),
					Integer.parseInt(gen[2]), Integer.parseInt(gen[3])));
		}
	}

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * 元の文字を置き換える
	 * 
	 * @param result
	 *            Replacing string
	 */
	private void replace(String result) {
		Intent data = new Intent();
		data.putExtra(REPLACE_KEY, result);
		setResult(RESULT_OK, data);
		finish();
	}

	// ランダムに選択
	private String[] randSelect(String[][] data) {
		int length = data.length;
		int idx = random.nextInt(length);
		Log.d(TAG, "randSelect:[" + idx + "/" + length + "]");
		String[] retval = data[idx];
		return retval;
	}

	// 組み立て
	private String[] generate() {

		faceparts parts = new faceparts(0);

		// ランダム選択
		Log.d(TAG, "fcolor:");
		String[] color = randSelect(parts.fdcolor);
		Log.d(TAG, "frin/ote/ome:");
		String[] rinkaku = randSelect(parts.fdrinkaku);
		String[] otete = randSelect(parts.fdotete);
		String[] omeme = randSelect(parts.fdomeme);
		Log.d(TAG, "foku/hop:");
		String[] okuti = randSelect(parts.fdokuti);
		String[] hoppe = randSelect(parts.fdhoppe);

		// 組み立て
		String ielm = otete[0] + rinkaku[0]
				+ ((otete[1] == "" || otete[3] == "") ? "" : hoppe[0])
				+ omeme[0] + ((otete[2] == "") ? okuti[0] : otete[2])
				+ omeme[1]
				+ ((otete[3] == "" || otete[1] == "") ? "" : hoppe[1])
				+ rinkaku[1] + otete[4]
		// + " http://bit.ly/1YKyIG"
		;

		String retval[] = { ielm, color[0], color[1], color[2] };
		Log.d(TAG, "generate: [" + retval[0] + " / " + retval[1] + retval[2]
				+ retval[3] + "]");
		
		//TODO: ヒストリに記録
		
		//TODO: ヒストリを再表示

		return retval;
	}
	
	
	//TODO: ヒストリを表示
	public void dispHistory(){
		
		return;
	}

	//
	// from : http://amachang.sakura.ne.jp/misc/aamaker/
	// 生来の助平者につき(´人`)狼藉御免 RT @amachang ヽ|＞ェ＜；|ノ ソースコードを見るなんてエッチ過ぎです！

}