/*
Activity to display the list of contacts. The floating action button acts as a mode changer to "Edit/Save mode."
Aditya Prakash      :   axp171931
Shubhmeet Kaur    :   sxk162731
* */
package com.example.himan.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
/*
* The activity to display the contacts.
*/
public class List extends AppCompatActivity {
    public static File file = new File("HCI_Contacts.txt"); // The file name to store the contacts
    public static int num1=-1;      // Flag for "adding new contact" mode. If num=-1 then a new contact is to be added.
    ArrayList<String> data1 = new ArrayList<String>();  // To store the data to be displayed in the list
    ArrayList<String> all_data = new ArrayList<String>();   // To get all the data from the file
    /*
                Key Listener for the back button on android phones. On the List Activity it will close the app and not navigate to any other activity.
         */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //  Author: Shubhmeet Kaur
        if (keyCode == KeyEvent.KEYCODE_BACK) { // Denoting the back key
            finish();   // Finish the current activity
            moveTaskToBack(true);   // Close the application
        }
        return super.onKeyDown(keyCode, event);
    }
    /*
            The method gets all the data from the file and stores it in a array list. Then it clears the file and writes down the same data which is now sorted.
         */
    public void sortFile()//  Author: Aditya Prakash
    {
        try {
            String all1=""; // To get all data in a string
            for(int k=0;k<all_data.size();k++)
            {
                all1+=all_data.get(k)+"\n"; // Store the data from the array list to the string
            }
            FileOutputStream fos = null;
            String filename = "HCI_Contacts";
            fos = openFileOutput(filename, Context.MODE_PRIVATE);   // Clear the file
            fos.write(all1.getBytes()); // Write the data to the file.
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
    /*
                This method is called whenever an Instance of this activity is created.
        */
    @Override
    protected void onCreate(Bundle savedInstanceState) {    //  Author: Both
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Here the floating action button is declared. The icon is a plus indicating the addition of a new contact.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // The code below will listen to clicks on the FAB and will start the next activity when the click is detected.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num1=-1;    // Save mode
                Intent myIntent = new Intent(List.this, Edit_Save.class);   // Start Edit_Save
                List.this.startActivity(myIntent);
            }
        });
        final ListView lv = (ListView)findViewById(R.id.contact_List);  // Listview to contain contacts
        final ArrayList<String> lv1 = new ArrayList<String>();  // Arraylist to store details
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lv1);

        // Array adapter to display contacts in the List View
        lv.setAdapter(adapter);
        try {
            // Create an input stream to open the file
            InputStream inputStream = openFileInput("HCI_Contacts");
            if ( inputStream != null ) {    // If the file is opened
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  // A reader
                String receiveString = "";  // String received from reading each line
                while ( (receiveString = bufferedReader.readLine()) != null ) { // To read all lines
                    String[] data2=receiveString.split("\\t");  // Split the string into smaller strings separated by tabs
                    data1.add(data2[0]+" "+data2[1]+", \nPhone:\t "+data2[2]);  // Add to the array adapter to show in the list
                    // First name is data2[0] and then comes Last Name and finaly the Phone number
                    all_data.add(receiveString);
                    // Add the received string to all the data
                }
                Collections.sort(data1,String.CASE_INSENSITIVE_ORDER);  // Sort both the arraylists
                Collections.sort(all_data,String.CASE_INSENSITIVE_ORDER);
                sortFile(); // Call to the method
                for(int j=0;j<data1.size();j++)
                {
                    lv1.add(data1.get(j));  // Add items to the listview in the contacts
                }
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        adapter.notifyDataSetChanged(); // Notify the list adapter of change in contents
        // On clicking any item in the list, the clicklistener is called
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);    // Position is the index of the item
                num1=position;  // Set the flag to the index for "Edit" mode
                Intent myIntent = new Intent(List.this, Edit_Save.class);
                List.this.startActivity(myIntent);  // Launch the Edit_Save activity
            }

        });
    }

    /*
    *       When the options menu is created
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }
    /*
    *       When an item is selected from the options menu.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

