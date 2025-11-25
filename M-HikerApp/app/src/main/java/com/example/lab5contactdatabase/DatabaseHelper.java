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
    private static final String HIKE_DETAIL = "hike_details";
    private static final String ID_COLUMN_NAME = "hike_id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String LOCATION_COLUMN_NAME = "location";
    private static final String DATE_COLUMN_NAME = "dateandtime";
    private static final String PARKING_COLUMN_NAME = "parkingAvailable";
    private static final String LENGTH_COLUMN_NAME = "lengthofhike";
    private static final String HIKELEVEL_COLUMN_NAME = "hike_lv";
    private static final String HIKETYPE_COLUMN_NAME = "hikeType";
    private static final String SCENICPOINTS_COLUMN_NAME = "scenicPoints";

    private static final String DESCRIPTION_COLUMN_NAME = "description";


    // Tên bảng và cột cho Observation
    private static final String TABLE_OBSERVATIONS = "observations";
    private static final String COLUMN_OBS_ID = "obs_id";
    private static final String COLUMN_HIKE_ID_FK = "hike_id"; // Khóa ngoại liên kết với bảng Hikes
    private static final String COLUMN_OBSERVATION = "observation_name";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_COMMENTS = "comments";

    private static final String COLUMN_IMAGES = "images";

    private SQLiteDatabase database;

    private static final String DATABASE_CREATE_QUERY = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s INTEGER, " +
                    "%s INTEGER, " +
                    "%s INTEGER, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            HIKE_DETAIL, ID_COLUMN_NAME, NAME_COLUMN_NAME,
            LOCATION_COLUMN_NAME, DATE_COLUMN_NAME,
            PARKING_COLUMN_NAME, LENGTH_COLUMN_NAME,
            HIKELEVEL_COLUMN_NAME, HIKETYPE_COLUMN_NAME,
            SCENICPOINTS_COLUMN_NAME, DESCRIPTION_COLUMN_NAME);
    private static final String CREATE_TABLE_OBSERVATIONS = "CREATE TABLE " + TABLE_OBSERVATIONS + " ("
            + COLUMN_OBS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_HIKE_ID_FK + " INTEGER, "
            + COLUMN_OBSERVATION + " TEXT, "
            + COLUMN_TIME + " TEXT, "
            + COLUMN_COMMENTS + " TEXT, "
            + COLUMN_IMAGES + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_HIKE_ID_FK + ") REFERENCES " + HIKE_DETAIL + "(" + ID_COLUMN_NAME + "));";

    public DatabaseHelper(Context context) {
        super(context, HIKE_DETAIL, null, 3);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL((DATABASE_CREATE_QUERY));
        db.execSQL((CREATE_TABLE_OBSERVATIONS));
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HIKE_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        Log.w(this.getClass().getName(), HIKE_DETAIL + " database upgrade to version "
                                        + newVersion + " - old data lost");
        Log.w(this.getClass().getName(), TABLE_OBSERVATIONS + " database upgrade to version "
                + newVersion + " - old data lost");
        onCreate(db);
    }

    public long insertDetails(String name, String location, String date,
                              int hike_lv,
                              String description,
                              double lengthofhike,
                              String hikeType,
                              String scenicPoints,
                              int parkingAvailable)

                               {
        ContentValues rowValues = new ContentValues();
        rowValues.put(NAME_COLUMN_NAME, name);
        rowValues.put(LOCATION_COLUMN_NAME, location);
        rowValues.put(DATE_COLUMN_NAME, date);
        rowValues.put(PARKING_COLUMN_NAME, parkingAvailable);
        rowValues.put(LENGTH_COLUMN_NAME, lengthofhike);
        rowValues.put(HIKELEVEL_COLUMN_NAME, hike_lv);
        rowValues.put(SCENICPOINTS_COLUMN_NAME, scenicPoints);
        rowValues.put(DESCRIPTION_COLUMN_NAME, description);
        rowValues.put(HIKETYPE_COLUMN_NAME, hikeType);

        return database.insertOrThrow(HIKE_DETAIL, null, rowValues);
    }


    public long addObservation( int hikeId, String observation, String time, String comments, String images) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues rowValues = new ContentValues();

        rowValues.put(COLUMN_HIKE_ID_FK, hikeId);
        rowValues.put(COLUMN_OBSERVATION, observation);
        rowValues.put(COLUMN_TIME, time);
        rowValues.put(COLUMN_COMMENTS, comments);
        rowValues.put(COLUMN_IMAGES,images);

        long result = db.insert(TABLE_OBSERVATIONS, null, rowValues);
        db.close();
        return result;
    }
    public void deleteAllhikes() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM hike_details");
        db.close();
    }
    // DeleteAll cua Obs
    public void deleteAllObservationsForHike(int hikeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Lệnh này chỉ xóa các dòng có cột COLUMN_HIKE_ID_FK khớp với hikeId được truyền vào.
        db.delete(TABLE_OBSERVATIONS, COLUMN_HIKE_ID_FK + " = ?", new String[]{String.valueOf(hikeId)});
        db.close();
    }

    public String getDetails() {
        Cursor results = database.query("hike_details",
                new String[] {"hike_id", "name",
                        "location", "dateandtime",
                        "parkingAvailable", "lengthofhike",
                        "hike_lv", "scenicPoints", "description", "hikeType"},

                null, null, null, null, "name");
        String resultText = "";
        results.moveToFirst();
        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name =  results.getString(1);
            String location = results.getString(2);
            String dateandtime = results.getString(3);
            int parkingAvailable = results.getInt(4);
            String lengthofhike = results.getString(5);
            int hike_lv = results.getInt(6);
            String scenicPoints = results.getString(7);
            String description = results.getString(8);
            String hikeType = results.getString(9);

            resultText += id + " " + "Name: "+ name + "\n" + "Location: " + location + "\n" + "Date and Time: " + dateandtime + "\n" +
                    "Hike Type: " + hikeType + "\n" +
                    "Parking Available: " + parkingAvailable + "\n" +
                    "Length of Hike: " + lengthofhike + "\n" +
                    "Hike Level: " + hike_lv + "\n" +
                    "Scenic Points: " + scenicPoints + "\n" +
                    "Description: " + description + "\n";
            results.moveToNext();
        }
        return resultText;
    }
    public List<Hike> getAllHike() {
        List<Hike> hike = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM hike_details", null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndexOrThrow("hike_id");
            int nameIndex = cursor.getColumnIndexOrThrow("name");
            int locationIndex = cursor.getColumnIndexOrThrow("location");
            int dateandtimeIndex = cursor.getColumnIndexOrThrow("dateandtime");
            int parkingAvailableIndex = cursor.getColumnIndexOrThrow("parkingAvailable");
            int lengthofhikeIndex = cursor.getColumnIndexOrThrow("lengthofhike");
            int hike_lvIndex = cursor.getColumnIndexOrThrow("hike_lv");
            int scenicPointsIndex = cursor.getColumnIndexOrThrow("scenicPoints");
            int descriptionIndex = cursor.getColumnIndexOrThrow("description");
            int hikeTypeIndex = cursor.getColumnIndexOrThrow("hikeType");


            do {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String location = cursor.getString(locationIndex);
                String dateandtime = cursor.getString(dateandtimeIndex);
                int parkingAvailable = cursor.getInt(parkingAvailableIndex);
                double lengthofhike = cursor.getInt(lengthofhikeIndex);
                int hike_lv = cursor.getInt(hike_lvIndex);
                String scenicPoints = cursor.getString(scenicPointsIndex);
                String description = cursor.getString(descriptionIndex);
                String hikeType = cursor.getString(hikeTypeIndex);


                hike.add(new Hike(id,name, location, dateandtime, parkingAvailable, lengthofhike, hike_lv, scenicPoints, description, hikeType));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hike;
    }

    public Hike getHikeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Hike hike = null;
        Cursor cursor = null;

        try {
            cursor = db.query(HIKE_DETAIL,
                    null,
                    ID_COLUMN_NAME + "=?", // Mệnh đề WHERE: hike_id = ?
                    new String[]{String.valueOf(id)}, // Tham số cho ?
                    null,
                    null,
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                // Lấy chỉ số cột (đảm bảo thứ tự cột khớp với constructor của Hike)
                int idIndex = cursor.getColumnIndexOrThrow(ID_COLUMN_NAME);
                int nameIndex = cursor.getColumnIndexOrThrow(NAME_COLUMN_NAME);
                int locationIndex = cursor.getColumnIndexOrThrow(LOCATION_COLUMN_NAME);
                int dateandtimeIndex = cursor.getColumnIndexOrThrow(DATE_COLUMN_NAME);
                int parkingAvailableIndex = cursor.getColumnIndexOrThrow(PARKING_COLUMN_NAME);
                int lengthofhikeIndex = cursor.getColumnIndexOrThrow(LENGTH_COLUMN_NAME);
                int hike_lvIndex = cursor.getColumnIndexOrThrow(HIKELEVEL_COLUMN_NAME);
                int scenicPointsIndex = cursor.getColumnIndexOrThrow(SCENICPOINTS_COLUMN_NAME);
                int descriptionIndex = cursor.getColumnIndexOrThrow(DESCRIPTION_COLUMN_NAME);
                int hikeTypeIndex = cursor.getColumnIndexOrThrow(HIKETYPE_COLUMN_NAME);

                // Đọc dữ liệu từ Cursor
                int hikeId = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String location = cursor.getString(locationIndex);
                String dateandtime = cursor.getString(dateandtimeIndex);
                int parkingAvailable = cursor.getInt(parkingAvailableIndex);
                double lengthofhike = cursor.getInt(lengthofhikeIndex);
                int hike_lv = cursor.getInt(hike_lvIndex);
                String scenicPoints = cursor.getString(scenicPointsIndex);
                String description = cursor.getString(descriptionIndex);
                String hikeType = cursor.getString(hikeTypeIndex);

                // Tạo đối tượng Hike
                hike = new Hike(hikeId, name, location, dateandtime, parkingAvailable, lengthofhike,
                        hike_lv, scenicPoints, description, hikeType);
            }
        } catch (Exception e) {
            // Ghi log lỗi nếu có vấn đề khi truy vấn
            Log.e("DatabaseHelper", "Error getting hike by ID: " + id, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return hike;
    }


    public List<Hike> searchHikes(String query) {
        List<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String selection = NAME_COLUMN_NAME + " LIKE ?";
            String[] selectionArgs = {"%" + query + "%"}; // Tìm kiếm chứa chuỗi, không phân biệt hoa thường

            cursor = db.query(HIKE_DETAIL,
                    null,
                    selection,
                    selectionArgs,
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int hikeId = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN_NAME));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN_NAME));
                    String location = cursor.getString(cursor.getColumnIndexOrThrow(LOCATION_COLUMN_NAME));
                    String dateandtime = cursor.getString(cursor.getColumnIndexOrThrow(DATE_COLUMN_NAME));
                    int parkingAvailable = cursor.getColumnIndexOrThrow(PARKING_COLUMN_NAME);
                    double lengthofhike = cursor.getDouble(cursor.getColumnIndexOrThrow(LENGTH_COLUMN_NAME));
                    int hike_lv = cursor.getInt(cursor.getColumnIndexOrThrow(HIKELEVEL_COLUMN_NAME));
                    String scenicPoints = cursor.getString(cursor.getColumnIndexOrThrow(SCENICPOINTS_COLUMN_NAME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION_COLUMN_NAME));
                    String hikeType = cursor.getString(cursor.getColumnIndexOrThrow(HIKETYPE_COLUMN_NAME));

                    Hike hike = new Hike(hikeId, name, location, dateandtime, parkingAvailable, lengthofhike,
                            hike_lv, scenicPoints, description, hikeType);
                    hikeList.add(hike);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error searching hikes for query: " + query, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return hikeList;
    }
    public List<Observation> getObservationsByHikeId(int hikeId) {
        List<Observation> obs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Lọc theo obs_id
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OBSERVATIONS + " WHERE " + COLUMN_HIKE_ID_FK + " = ?", new String[]{String.valueOf(hikeId)});

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndexOrThrow(COLUMN_OBS_ID);
            int h_idIndex = cursor.getColumnIndexOrThrow(COLUMN_HIKE_ID_FK);
            int nameIndex = cursor.getColumnIndexOrThrow(COLUMN_OBSERVATION);
            int timeIndex = cursor.getColumnIndexOrThrow(COLUMN_TIME);
            int cmtIndex = cursor.getColumnIndexOrThrow(COLUMN_COMMENTS);
            int imgIndex = cursor.getColumnIndexOrThrow(COLUMN_IMAGES);

            do {
                // Đảm bảo thứ tự cột đúng với lúc tạo bảng
                int id = cursor.getInt(idIndex);
                int hId = cursor.getInt(h_idIndex);
                String name = cursor.getString(nameIndex);
                String time = cursor.getString(timeIndex);
                String cmt = cursor.getString(cmtIndex);
                String img = cursor.getString(imgIndex);


                obs.add(new Observation(id, hId, name, time, cmt, img));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obs;
    }
    public int deleteHike(int hikeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Sử dụng hằng số COLUMN_ID  đã định nghĩa cho cột ID
        // Đảm bảo hằng số COLUMN_ID đã được khai báo ở đầu lớp DatabaseHelper
        // Ví dụ: public static final String COLUMN_ID = "_id";
        int result = db.delete(HIKE_DETAIL, ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(hikeId)});

        db.close();
        return result; // Trả về số dòng đã bị xóa (thường là 1 nếu thành công, 0 nếu không tìm thấy)
    }

    public int deleteObs(int obs_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_OBSERVATIONS, COLUMN_OBS_ID + " = ?", new String[]{String.valueOf(obs_id)});

        db.close();
        return result; // Trả về số dòng đã bị xóa (thường là 1 nếu thành công, 0 nếu không tìm thấy)
    }
    public int updateHike(Hike hike) {
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues rowValues = new ContentValues();

            rowValues.put(NAME_COLUMN_NAME, hike.getName());
            rowValues.put(LOCATION_COLUMN_NAME, hike.getLocation());
            rowValues.put(DATE_COLUMN_NAME, hike.getDateandtime());
            rowValues.put(PARKING_COLUMN_NAME, hike.getParkingAvailable());
            rowValues.put(LENGTH_COLUMN_NAME, hike.getLengthofhike()); // double
            rowValues.put(HIKELEVEL_COLUMN_NAME, hike.getHike_lv()); // int
            rowValues.put(HIKETYPE_COLUMN_NAME, hike.getHikeType());
            rowValues.put(SCENICPOINTS_COLUMN_NAME, hike.getScenicPoints());
            rowValues.put(DESCRIPTION_COLUMN_NAME, hike.getDescription());


            // Cập nhật dòng dựa trên ID của chuyến đi
            int rowsAffected = db.update(HIKE_DETAIL, rowValues, ID_COLUMN_NAME + " = ?",
                    new String[]{String.valueOf(hike.getId())});
            db.close();
            return rowsAffected;
        }

    }
    public int updateObs(Observation obs) {
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues rowValues = new ContentValues();

            rowValues.put(COLUMN_OBSERVATION, obs.getObservation());
            rowValues.put(COLUMN_TIME, obs.getTime());
            rowValues.put(COLUMN_COMMENTS, obs.getComments());
            rowValues.put(COLUMN_IMAGES, obs.getImages());


            // Cập nhật observation dựa trên ID của chuyến đi
            int rowsAffected = db.update(TABLE_OBSERVATIONS, rowValues, COLUMN_OBS_ID + " = ?",
                    new String[]{String.valueOf(obs.getId())});
            db.close();
            return rowsAffected;
        }
}}
