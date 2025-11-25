package com.example.lab5contactdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contact_details";
    private static final String ID_COLUMN_NAME = "person_id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String DOB_COLUMN_NAME = "dob";
    private static final String EMAIL_COLUMN_NAME = "email";
    private static final String IMAGE_COLUMN_NAME = "image";
    private SQLiteDatabase database;

    private static final String DATABASE_CREATE_QUERY = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            DATABASE_NAME, ID_COLUMN_NAME, NAME_COLUMN_NAME,
            DOB_COLUMN_NAME,
            EMAIL_COLUMN_NAME,
            IMAGE_COLUMN_NAME);

    public DatabaseHelper(Context context) {
        super (context, DATABASE_NAME, null, 2);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL((DATABASE_CREATE_QUERY));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        Log.w(this.getClass().getName(), DATABASE_NAME + " database upgrade to version "
                                        + newVersion + " - old data lost");
        onCreate(db);
    }

    public long insertDetails(String name, String dob, String email, String image) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(NAME_COLUMN_NAME, name);
        rowValues.put(DOB_COLUMN_NAME, dob);
        rowValues.put(EMAIL_COLUMN_NAME, email);
        rowValues.put(IMAGE_COLUMN_NAME, image);
        return database.insertOrThrow(DATABASE_NAME, null, rowValues);
    }

    public String getDetails() {
        Cursor results = database.query("contact_details",
                new String[] {"person_id", "name", "dob", "email", "image"},
                null, null, null, null, "name");
        String resultText = "";
        results.moveToFirst();
        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name =  results.getString(1);
            String dob = results.getString(2);
            String email = results.getString(3);
            String image = results.getString(4);

            resultText += id + " " + "Name: "+ name + "\n" + "DoB: " + dob + "\n" + "Email: " + email + "\n" + "Image: " + image + "\n";
            results.moveToNext();
        }
        return resultText;
    }
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contact_details", null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndexOrThrow("name");
            int emailIndex = cursor.getColumnIndexOrThrow("email");
            int dobIndex = cursor.getColumnIndexOrThrow("dob");
            int imageIndex = cursor.getColumnIndexOrThrow("image");

            do {
                String name = cursor.getString(nameIndex);
                String email = cursor.getString(emailIndex);
                String dob = cursor.getString(dobIndex);
                String image= cursor.getString(imageIndex);

                contacts.add(new Contact(name, email, dob, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }

}
