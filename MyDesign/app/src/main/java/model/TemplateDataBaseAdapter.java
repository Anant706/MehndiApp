package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TemplateDataBaseAdapter {
	static final String DATABASE_NAME = "-------------";

	static final int DATABASE_VERSION = 1;

	public static final int NAME_COLUMN = 1;
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	static final String DATABASE_CREATE = "create table TEMPLATE " + "( "
			+ "ID integer primary key autoincrement, TEMPLATETEXT text unique); ";

	// Variable to hold the database instance
	public SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;

	public TemplateDataBaseAdapter(Context _context) {
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public TemplateDataBaseAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}

	public void insertEntry(String templateText) {
		// TODO: Create a new ContentValues to represent my row
		// and insert it into the database.
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("TEMPLATETEXT", templateText);

		// Insert the row into your table
		db.insert("TEMPLATE", null, newValues);
		Toast.makeText(context, "Added to favourite",
				Toast.LENGTH_LONG).show();

	}

	public int deleteEntry(String templateText) {
		//String id = templateText;

		String where = "TEMPLATETEXT=?";

		int numberOFEntriesDeleted = db.delete("TEMPLATE", where,
				new String[]{templateText});
		Toast.makeText(context, "Removed from favourite", Toast.LENGTH_LONG).show();
		return numberOFEntriesDeleted;

	}

	public List<String> getURL() {
		List<String> url = new ArrayList<String>();
		Cursor localCursor = db.rawQuery("SELECT  * FROM TEMPLATE", null);
		if (localCursor.moveToFirst()) {

			do {
				url.add(localCursor.getString(1));
			} while (localCursor.moveToNext());


		}
		return url;
	}

	public Cursor getAllEntries() {
		return db.query("TEMPLATE", null, null, null, null, null, null);
	}

	public void updateEntry(String text, long id) {
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("TEMPLATETEXT", text);

		String where = "ID = ?";
		db.update("TEMPLATE", updatedValues, where,
				new String[]{String.valueOf(id)});
		Toast.makeText(context, "Tempalte Updated and Saved ",
				Toast.LENGTH_SHORT).show();

	}

}
