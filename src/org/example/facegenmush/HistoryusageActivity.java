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
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.util.Log;

public class HistoryusageActivity extends Activity implements OnClickListener {
	private Button mRBtn;
	//private TextView mFacecharTV;
	private String TAG = "Historyusage";

	// http://www.adamrocker.com/blog/mushroom-collaborates-with-simeji/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// event dispatcher
	public void onClick(View v) {
		if (v == mRBtn) {
			Log.d(TAG, "click:usethis");
		} 
	}
	
	///////////////////////////////////////////////////////////////
	
	
	
	
	
	
}