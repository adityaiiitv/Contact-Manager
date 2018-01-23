/*
        Activity to edit or save the contacts. When save or delete button is pressed it goes back to the List activity.
        Aditya Prakash      :   axp171931
        Shubhmeet Kaur    :   sxk162731
*/
package com.example.himan.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/*
*   Class for the activity Edit_Save. It works on two modes. Edit and Save. It also provides the option to delete a contact
*   */
public class Edit_Save extends AppCompatActivity {
    public int edit_flag=0; // edit_flag=0 for edit mode, 1 for save mode
    /*
            Method to detect the back button
    * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //  Author: Aditya Prakash
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(Edit_Save.this, List.class);
            finish();   // Finish the current activity
            Edit_Save.this.startActivity(myIntent);
        }
        return super.onKeyDown(keyCode, event);
    }
    /*
            Method to read a file
    * */
    public void readFile()  //  Author: Shubhmeet Kaur
    {
        int counter=0;      // To maintain the index
        try {
            // Try to open the file
            InputStream inputStream = openFileInput("HCI_Contacts");

            if ( inputStream != null ) {    // If opened
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                while ( (receiveString = bufferedReader.readLine()) != null ) { // Get the line
                    if(counter==List.num1) {    // If the position or the index matches to num1 from the List class
                        display(receiveString); // Display the given string
                        break;
                    }
                    else{
                    counter++;  // Change the counter
                    }
                }
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
    /*
    *       Method to display the data of a contact in the text fields*/
    public void display(String s)   //  Author: Aditya Prakash
    {
        // Split the received string on the basis of tabs.
        String[] data1=s.split("\\t");
        TextView fname = (TextView)findViewById(R.id.editText2);
        TextView lname = (TextView)findViewById(R.id.editText3);
        TextView phone = (TextView)findViewById(R.id.editText4);
        TextView email = (TextView)findViewById(R.id.editText5);
        fname.setText(data1[0]);    // First name displayed
        lname.setText(data1[1]);    // Last name
        phone.setText(data1[2]);    // Phone
        email.setText(data1[3]);    // Email
    }
    /*
    *       Method to save the data to the file. This method is also called in the edit mode*/
    public void saveFile()  //  Author: Shubhmeet Kaur
    {
            String filename="HCI_Contacts";
            TextView fname = (TextView)findViewById(R.id.editText2);
            TextView lname = (TextView)findViewById(R.id.editText3);
            TextView phone = (TextView)findViewById(R.id.editText4);
            TextView email = (TextView)findViewById(R.id.editText5);
            fname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            lname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            if(fname.getText().toString().length()==0)  // If the first name is left as blank
            {
                // Try again
                Toast.makeText(this, "Please use a first name to save.", Toast.LENGTH_LONG).show();
                return;
            }
            String l_name=lname.getText().toString(),phone_num=phone.getText().toString(),e_mail=email.getText().toString();
            // String to be finally written to the file
            // Check if other things are empty then replace with empty
            if(lname.getText().toString().length()==0)
            {
                l_name=" ";
            }
            if(phone.getText().toString().length()==0)
            {
                phone_num=" ";
            }
            if(email.getText().toString().length()==0)
            {
                e_mail=" ";
            }
            // Make the string
            String string = fname.getText() + "\t" + l_name + "\t" + phone_num+ "\t" + e_mail+ "\n";
            FileOutputStream fos = null;
            try {
                fos = openFileOutput(filename, Context.MODE_APPEND);
                // Write to the file
                fos.write(string.getBytes());
                fos.close();
                if(edit_flag==0) {
                    // Display success in saving
                    Toast.makeText(this, "Saved contact: " + fname.getText(), Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Failed to create a file", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            // Delete the old contact from the file
            deleteFile();
            Intent myIntent = new Intent(this, List.class);
            finish();   // Finish the current activity
            Edit_Save.this.startActivity(myIntent);
    }

    /*
    *       Method to delete data from the file*/
    public void deleteFile()    //  Author: Aditya Prakash
    {
        try {
            int counter=0;
            InputStream inputStream = openFileInput("HCI_Contacts");
            String all="";  // To get all data
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                while ( (receiveString = bufferedReader.readLine()) != null ) { // Read every line
                    if(counter==List.num1) {    // To get the index
                        String[] data1=receiveString.split("\\t");  // To split the line with tabs
                        if(edit_flag==0) {
                            // Show success in deletion
                            Toast.makeText(this, "Deleted contact: " + data1[0], Toast.LENGTH_LONG).show();
                        }
                        else{
                            // Show success in deletion
                            TextView fname = (TextView)findViewById(R.id.editText2);
                            Toast.makeText(this, "Saved contact:"+fname.getText()+"\nEdited contact: " + data1[0], Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        // Make the complete string
                        all+=receiveString+"\n";
                    }
                    counter++;  // Update index
                }
                FileOutputStream fos = null;
                String filename="HCI_Contacts";
                fos = openFileOutput(filename, Context.MODE_PRIVATE);   // Clear the file
                fos.write(all.getBytes());  // Write to the file
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        Intent myIntent = new Intent(Edit_Save.this, List.class);
        finish();   // Finish the current activity and start a new one
        Edit_Save.this.startActivity(myIntent);
    }
    /*
    *       Method which is called when the Activity is created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {//  Author: Both
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__save);
        final Button save= (Button) findViewById(R.id.save);    // For save button
        final Button delete = (Button) findViewById(R.id.delete);// For delete button
        if(List.num1!=-1) { //  For edit mode
            readFile(); // Read the file
            save.setText("Edit");   // change the text of the save button to edit
            // Disable all the text fields
            final TextView fname = (TextView) findViewById(R.id.editText2);
            fname.setEnabled(false);
            final TextView lname = (TextView) findViewById(R.id.editText3);
            lname.setEnabled(false);
            final TextView phone = (TextView) findViewById(R.id.editText4);
            phone.setEnabled(false);
            final TextView email = (TextView) findViewById(R.id.editText5);
            email.setEnabled(false);
            fname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            lname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            // Click listener for save
            save.setOnClickListener(new View.OnClickListener(){
                // Method for doing on click
                @Override
                public void onClick(View v)
                {
                    edit_flag=1;    // For edit flag
                    save.setText("Save");
                    fname.setEnabled(true);
                    lname.setEnabled(true);
                    phone.setEnabled(true);
                    email.setEnabled(true);
                    // Click listener for save
                    save.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v)
                        {
                            // Method to call save file
                            saveFile();
                        }

                    });
                }

            });
        }
        if(List.num1==-1) { // In "save" mode
            save.setText("Save");
            delete.setVisibility(View.GONE);    // Remove the visibility button
            // Clicklistener for save button
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (List.num1 == -1) {  // Save mode
                        saveFile();

                    }
                }
            });
        }
        // Clicklistener for delete button
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(List.num1!=-1)
                {
                    edit_flag=0;    // Set mode to save again
                    deleteFile();   // Delete the contact from the file
                }
            }

        });
    }
}
