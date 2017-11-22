package com.yiful.bmwchallenge.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yifu on 11/19/2017.
 */

public class Location implements Parcelable{
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private String arrivalTime;
   // private double distance;

    public Location(int id, String name, double latitude, double longitude, String address, String arrivalTime) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.arrivalTime = arrivalTime;
    }
    public Location(String name, String address){
        this.name = name;
        this.address = address;
    }


    protected Location(Parcel in) {
        id = in.readInt();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        arrivalTime = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public double getDistance() {

        final int R = 6371; // Radius of the earth
        double lat1 = latitude;
        double lon1 = longitude;
        double lat2 = 41.917715; //lat2, lon2 is location of St.Charles, IL
        double lon2 = -88.266027;
        double el1=0,el2 = 0;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(address);
        parcel.writeString(arrivalTime);
    }
}
