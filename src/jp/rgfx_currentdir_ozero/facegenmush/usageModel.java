/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package jp.rgfx_currentdir_ozero.facegenmush;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class usageModel {

	public static final String KEY_FACE = "face";
	public static final String KEY_ROWID = "_id";

	private static final String TAG = "usageModel";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;// DBへの接続オブジェクト

	private static final String DATABASE_NAME = "usagehistory";
	private static final String DATABASE_TABLE = "usageitem";
	private static final int DATABASE_VERSION = 2;

	private static final int maxRowNumLimit = 50;	
	
	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + "_id integer primary key autoincrement, "
			+ "face text not null);";

	// 呼び出し元Activityへの参照をContextとして持ち回りできるように
	private final Context mCtx;

	// DBアクセスのラッパークラス（SQLiteクラスをベースに
	private static class DatabaseHelper extends SQLiteOpenHelper {

		// コンストラクタ。使用するDBファイルなどを指定
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// 初めて作成された場合はこのcreate文を実行
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			//あと初期データの投入
			return;
		}
		
		@Override
		// スケーマのアップグレード処理に付けたし
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	/**
	 * コンストラクタ - takes the context to allow the database to be opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public usageModel(Context ctx) {
		// mCtxプロパティに呼び出し元Activityへの参照をコンテクストとして持っておく
		this.mCtx = ctx;
	}

	/**
	 * Open the items database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public usageModel open() throws SQLException {
		// DBヘルパインスタンスを持つ
		mDbHelper = new DatabaseHelper(mCtx);
		// DBヘルパよりDB接続を受け取り、このクラスにプロパティとして持っておく
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a new item using the title and body provided. If the item is
	 * successfully created return the new rowId for that item, otherwise return
	 * a -1 to indicate failure.
	 */
	public long createItem(String face) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_FACE, face);
		
		//重複があればdeleteしてからinsert（≒項目のpop
		Cursor cur = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_FACE},
				KEY_FACE + " = '" + face + "'", null, null, null, KEY_ROWID + " desc", "0,1");
		if(cur.getCount() > 0){
			String sql = "delete from " + DATABASE_TABLE + 
			" where " + KEY_FACE + " = '" + face + "'  ;";
			mDb.execSQL(sql);
		}
		Long ret = mDb.insert(DATABASE_TABLE, null, initialValues);

		//erase item over 50.
		cur = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID},
				null, null, null, null, KEY_ROWID + " desc", "0,1");
		cur.moveToFirst();
		int maxrowid = cur.getInt(0);
		if(
			(cur.getCount() > 0)
			&&(maxrowid > maxRowNumLimit)
		){
			String sql = "delete from " + DATABASE_TABLE + 
			" where " + KEY_ROWID + " < " + (maxrowid - maxRowNumLimit + 1) + "  ;";
			Log.d(TAG, "maxLimit:"+sql);
			mDb.execSQL(sql);
		}

		return ret;
	}

	/**
	 * Delete the item with the given rowId
	 * 
	 * @param rowId
	 *            id of item to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteItem(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all items in the database
	 * 
	 * @return Cursor over all item
	 */
	public Cursor fetchAllItems() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_FACE},
				null, null, null, null, KEY_ROWID + " desc" );
	}

	/**
	 * Return a Cursor positioned at the item that matches the given rowId
	 * 
	 * @param rowId
	 *            id of item to retrieve
	 * @return Cursor positioned to matching item, if found
	 * @throws SQLException
	 *             if item could not be found/retrieved
	 */
	public Cursor fetchItem(long rowId) throws SQLException {

		Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_FACE},
				KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Update the item using the details provided. The item to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 */
	public boolean updateItem(long rowId, String face) {
		ContentValues args = new ContentValues();
		args.put(KEY_FACE, face);
		
		//TODO : 30件越えた分については消すように

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	
	
	//discard and initialize current db
	public void initdb() {
		mDb.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		mDb.execSQL(DATABASE_CREATE);
		//あと初期データの投入
		return;
	}
	

	
}
