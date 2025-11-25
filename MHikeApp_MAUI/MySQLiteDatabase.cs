using System;
using System.Collections.Generic;
using System.Text;
using SQLite;
using Microsoft.Maui.Controls; // Add this using directive

using System.Collections.ObjectModel;
namespace MauiApp2
{
    public class MySQLiteDatabase
    {
        private SQLiteConnection dbConnection;
        public const string DatabaseFilename = "MySQLiteDatabase.db3";

        public const SQLiteOpenFlags Flags =
            // open the database in read/write mode
            SQLiteOpenFlags.ReadWrite |
            // create the database if it doesn't exist
            SQLiteOpenFlags.Create |
            // enable multi-threaded database access
            SQLiteOpenFlags.SharedCache;

        public string dbPath = "";

        public const string HIKE_TABLE = "Hike";
        public const string COLUMN_HIKE_ID = "Hike_id";
        public const string COLUMN_NAME = "Name";
        public const string COLUMN_LOCATION = "Location";
        public const string COLUMN_DATE = "Date";
        public const string COLUMN_IS_PARKING = "IsParking";
        public const string COLUMN_LENGTH = "Length";
        public const string COLUMN_DIFFICULTY = "Difficulty";
        public const string COLUMN_DESCRIPTION = "Description";

        public MySQLiteDatabase()
        {
            init();
        }

        public void init()
        {
            dbPath = System.IO.Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), DatabaseFilename);

            dbConnection = new SQLiteConnection(dbPath, Flags);
            dbConnection.CreateTable<Hike>();
        }

        public int insertHike(Hike hike)
        {
            return dbConnection.Insert(hike);
        }
        public int DeleteItem(Hike hike)
        {
            return dbConnection.Delete(hike);
        }

        public int UpdateHike(Hike hike)
        {
            return dbConnection.Update(hike);
        }

        public ObservableCollection<Hike> loadHike()      {
            var hikes = dbConnection.Table<Hike>();
            return new ObservableCollection<Hike>(hikes);
        }
    

    }

}