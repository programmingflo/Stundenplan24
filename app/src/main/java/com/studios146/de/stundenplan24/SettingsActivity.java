package com.studios146.de.stundenplan24;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] arraySchools;
    private String[] arraySchoolIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*Connection with 146Server for list of schools*/
        ServerConnection serverConnection = new ServerConnection(this);
        String request = "/schools";
        try{
            JSONObject json = serverConnection.execute(request).get();
            if(json == null){
                Log.d("146s.set.34", "json object null");
            }else {
                Integer status = json.getInt("status");
                Log.d("146s.set.37", "status " + status);
                //JSONObject data = json.getJSONObject("data");
                JSONArray dataarray = json.getJSONArray("schools");
                ArrayList<String> schools = new ArrayList<>();
                schools.add("keine Schule ausgewählt");
                ArrayList<String> schoolIDs = new ArrayList<>();
                schoolIDs.add("");
                for(int i=0; i< dataarray.length();i++){
                    JSONObject item = dataarray.getJSONObject(i);
                    schools.add(item.getString("name"));
                    schoolIDs.add(item.getString("_id"));
                }
                this.arraySchools = schools.toArray(new String[schools.size()]);
                this.arraySchoolIDs = schoolIDs.toArray(new String[schoolIDs.size()]);
            }
            //return (status == 200);

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
            Log.d("146s.set.56", e.toString());
            //return false;
            this.arraySchools = new String[]{"Fehler bei der Verbindung"};
            this.arraySchoolIDs = new String[]{""};
        }

        /*Create Spinner and select item*/
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSchools);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arraySchools);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        SharedPreferences preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String schoolID = preferences.getString("schoolID","");
        Integer index = 0;
        Log.d("146s.set","schoolID: "+schoolID);
        for (int i=0; i<arraySchoolIDs.length;i++){
            if(schoolID.equals(arraySchoolIDs[i])){
                index = i;
            }
        }
        String schoolName = arraySchools[index];
        if(!schoolID.isEmpty()){
            int spinnerPosition = arrayAdapter.getPosition(schoolName);
            spinner.setSelection(spinnerPosition);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Log.d("146s.set.71","item " + item);
        String selectedID = "";
        for(int i=0;i<arraySchools.length;i++){
            if(item.equals(arraySchools[i])){
                selectedID = arraySchoolIDs[i];
            }
        }
        Log.d("146s.set.78","schoolID "+selectedID);
        SharedPreferences sharedPreferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("schoolID",selectedID);
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO: add algorithm for nothing
    }
}
