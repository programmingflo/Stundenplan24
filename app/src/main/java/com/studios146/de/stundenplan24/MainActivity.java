package com.studios146.de.stundenplan24;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{
    Context context = this;
    private ScheduleDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServerConnection serverConnection = new ServerConnection(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String request = sharedPreferences.getString("schoolID", "");
        Boolean school_selected = false;
        if(request.isEmpty()){
            request = "/schools";
            school_selected = false;
        }else{
            request = "/plan/"+request;
            school_selected = true;
        }

        try{
            JSONObject json = serverConnection.execute(request).get();
            if(json == null){
                Log.d("146s","json object null");
            }else {
                Integer status = json.getInt("status");
                Log.d("146s", "status " + status);
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
            Log.d("com.146s.miit", e.toString());
        }

        /*String[] schedule = new String[48];
        Arrays.fill(schedule,"");
        Integer temp;
        for(Integer i = 0; i < 8; i++){
            temp = i+1;
            schedule[i*6] = temp.toString();
        }
        for(Integer i = 0; i < 48;i++){
            Log.d("146s.main.66","schedule "+schedule[i]);
        }*/

        Lesson testLesson = new Lesson("11.2","1","Physik","Niekau","S011",null);
        Log.d("146s", "testLesson "+testLesson.toString());

        dataSource = new ScheduleDataSource(this);

        //TODO: Convert 2-dimensional Array to 1-dimensional
        /*GridView gridView = (GridView) findViewById(R.id.schedule_main);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,schedule);
        //TODO: Create a layout for item of gridview
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("146s.main.68","selected item: "+((TextView)view).getText());
                // editDialog
                final Dialog editdialog = new Dialog(context);
                editdialog.setContentView(R.layout.edit_lesson);

                ImageButton saveButton = (ImageButton) editdialog.findViewById(R.id.saveEditButton);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //TODO: save lesson details
                    }
                });
                ImageButton closeEditButton = (ImageButton) editdialog.findViewById(R.id.closeEditButton);
                closeEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editdialog.dismiss();
                    }
                });

                // detailDialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_lesson);

                TextView textFach = (TextView) dialog.findViewById(R.id.lesson_fach);
                textFach.setText("Fach");
                TextView textRaum = (TextView) dialog.findViewById(R.id.lesson_raum);
                textRaum.setText("Raum");
                TextView textLehrer = (TextView) dialog.findViewById(R.id.lesson_lehrer);
                textLehrer.setText("Lehrer");
                TextView textInfo = (TextView) dialog.findViewById(R.id.lesson_info);
                textInfo.setText("Info");

                ImageButton editButton = (ImageButton) dialog.findViewById(R.id.editButton);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editdialog.show();
                    }
                });
                ImageButton closeButton = (ImageButton) dialog.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
