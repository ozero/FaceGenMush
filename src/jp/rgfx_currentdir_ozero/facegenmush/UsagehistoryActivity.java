package jp.rgfx_currentdir_ozero.facegenmush;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class UsagehistoryActivity
	extends ListActivity 
	implements OnClickListener 
	{
	
	// properties ///////////////////////////////////////////////////////////
	
	private usageModel usageDbHelper = new usageModel(this);

	
	
	// GUIライフサイクル定義 ////////////////////////////////////////////////
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// init
		super.onCreate(savedInstanceState);
		usageDbHelper.open();
		setContentView(R.layout.usagehistory);
		dispUsageHistory();
		return;
	}
	
	
	// GUIイベント定義 ////////////////////////////////////////////////
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		return;
	}
	
	
	// その他処理 /////////////////////////////////////////////////////
	
	// 使用履歴を表示
	public void dispUsageHistory() {
		//元データへのカーソル
		Cursor itemCursor = usageDbHelper.fetchAllItems();
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
	
	
}
