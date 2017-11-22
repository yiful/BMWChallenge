package com.yiful.bmwchallenge.Activity;
//Main Activity to display a list of locations
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.yiful.bmwchallenge.Model.Location;
import com.yiful.bmwchallenge.R;
import com.yiful.bmwchallenge.Utility.HttpHandler;
import com.yiful.bmwchallenge.Utility.LocationListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LocationListActivity extends AppCompatActivity {
    private ListView listView;
    private ProgressDialog progressDialog;
    ArrayList<Location> locationList;
    String urlString;
    int sortId = R.id.sortByName; //save sort by type
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            sortId = savedInstanceState.getInt("sortId");
        }
        setContentView(R.layout.activity_location_list);

        listView = findViewById(R.id.lv);
        urlString = "http://localsearch.azurewebsites.net/api/Locations";
        locationList = new ArrayList<>();

        new GetLocationList().execute();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("sortId", sortId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (sortId=item.getItemId()){
            case R.id.sortByName:
                //Toast.makeText(this, "Sorted by name", Toast.LENGTH_SHORT).show();
                sortLocation(R.id.sortByName);
                listView.setAdapter(new LocationListAdapter(LocationListActivity.this,R.layout.list_item_view, locationList));
                return true;
            case R.id.sortByDistance:
                sortLocation(R.id.sortByDistance);
                listView.setAdapter(new LocationListAdapter(LocationListActivity.this,R.layout.list_item_view, locationList));
                return true;
            case R.id.sortByArrivalTime:
                sortLocation(R.id.sortByArrivalTime);
                listView.setAdapter(new LocationListAdapter(LocationListActivity.this,R.layout.list_item_view, locationList));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void sortLocation(int sortId){
        Comparator<Location> comparator=null;
        switch (sortId){
            case R.id.sortByName:
                comparator = new Comparator<Location>() {
                    @Override
                    public int compare(Location location1, Location location2) {
                        return location1.getName().compareTo(location2.getName());
                    }
                };
                break;
            case R.id.sortByDistance:
                comparator = new Comparator<Location>() {
                    @Override
                    public int compare(Location location1, Location location2) {
                        return String.valueOf(location1.getDistance()).compareTo(String.valueOf(location2.getDistance()));
                    }
                };
                break;
            case R.id.sortByArrivalTime:
                comparator = new Comparator<Location>() {
                    @Override
                    public int compare(Location location1, Location location2) {
                        return location1.getArrivalTime().compareTo(location2.getArrivalTime());
                    }
                };
        }

        Collections.sort(locationList, comparator);
    }
//this class is used to get JSON and parse JSON with AsyncTask
    private class GetLocationList extends AsyncTask{
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LocationListActivity.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            //get json String
            HttpHandler httpHandler = new HttpHandler();
            response = httpHandler.getJsonString(urlString);
            //parse JSON array
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("ID");
                    String name = jsonObject.getString("Name");
                    double latitude = jsonObject.getDouble("Latitude");
                    double longitude = jsonObject.getDouble("Longitude");
                    String address = jsonObject.getString("Address");
                    String arrivalTime = jsonObject.getString("ArrivalTime");

                    Location location = new Location(id,name,latitude,longitude,address,arrivalTime);
                    locationList.add(location);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
      //      tvList.setText(response);
            if(progressDialog.isShowing())progressDialog.dismiss();
            sortLocation(sortId);
            ListAdapter adapter = new LocationListAdapter(LocationListActivity.this, R.layout.list_item_view, locationList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Location location = locationList.get(i);
                    //Toast.makeText(LocationListActivity.this, "you selected location "+latitude+", "+longitude, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LocationListActivity.this, ShowMapActivity.class);
                    intent.putExtra("location", location);
                    startActivity(intent);
                }
            });
        }
    }
}
