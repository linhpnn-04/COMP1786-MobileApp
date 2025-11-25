
using SQLite;
using System;
using System.Collections.Generic;
using System.Text;

namespace MauiApp2
{
    [SQLite.Table("Hike")]
    public class Hike
    {
        [PrimaryKey, AutoIncrement]
        public int Hike_id{get; set;}

        public string Name { get; set; }  
        
        public string Location { get; set; }
        public DateTime Date { get; set; }

        public bool IsParking { get; set; }
        public string Length { get; set; }
        public string Difficulty { get; set; }
        public string Description { get; set; }
        public string ParkingText => IsParking ? "Yes" : "No";
        public Hike() { }


        public Hike(int hike_id, string name, string location, DateTime date, bool isParking, string length, string difficulty, string description)
        {
            Hike_id = hike_id;
            Name = name;
            Location = location;
            Date = date;
            IsParking = isParking;
            Length = length;
            Difficulty = difficulty;
            Description = description;
        }
    }
}
