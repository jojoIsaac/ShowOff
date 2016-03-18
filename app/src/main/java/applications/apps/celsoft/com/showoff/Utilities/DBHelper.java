package applications.apps.celsoft.com.showoff.Utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DBNAME = "SHOWOFF";

	
	private static final int DATABASE_VERSION = 1;
	Context context;

	public DBHelper(Context context, String name, CursorFactory factory,
					int version) {
		super(context, DBNAME, factory, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public DBHelper(Context x) {
		// TODO Auto-generated constructor stub
		super(x, DBNAME, null, DATABASE_VERSION);
		context = x;
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.v("DB", "Creating DB");
		try {

			
		} catch (SQLiteException e) {
			Log.v("DB", e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.w("DB", "Upgrading the database from version " + arg1 + " to "
				+ arg2 + ", This will destroy all data");
//		arg0.execSQL("DROP TABLE IF EXISTS " + Bookmark.TBNAME);
//		arg0.execSQL("DROP TABLE IF EXISTS " + Notes.TBNAME);
		onCreate(arg0);
	}

	
	
	
	
	


}
