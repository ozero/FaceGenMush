package jp.rgfx_currentdir_ozero.facegenmush;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;



public class MycursorAdapter extends SimpleCursorAdapter implements Filterable {
	private int layout;
	private static Typeface faceDejaVu;
	private String[] colnamesToBind;
	private int[] widgetsToBind;
	
    public MycursorAdapter (
		Context context, int layout,
		Cursor c, String[] colnamesToBind, int[] widgetsToBind
	) {
        super(context, layout, c, colnamesToBind, widgetsToBind);
        this.layout = layout;
        this.colnamesToBind=colnamesToBind;
        this.widgetsToBind=widgetsToBind;
		//フォントへの参照を得る
		faceDejaVu =Typeface.createFromAsset(
				context.getAssets(), "fonts/DejaVuSans.ttf"
			); 
    }

    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);
        
        this.bindView(v, context, c);
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {
        int itemnum = this.colnamesToBind.length;
        for(int i = 0; i < itemnum; i++){
	        int colID = c.getColumnIndex(colnamesToBind[i]);
	        String itemtext = c.getString(colID);
	        TextView tv = (TextView) v.findViewById(widgetsToBind[i]);
	        if (tv != null) {
				tv.setTypeface(faceDejaVu);
	        	tv.setText(itemtext);
	        }
        }
        return;
    }

}


