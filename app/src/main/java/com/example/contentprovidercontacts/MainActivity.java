package com.example.contentprovidercontacts;

/* Here the CONTENT_URI is the default (from android itself), meaning it is not customized.
 *
 * In this project, content provider is used to access the contacts that are present in the emulator.
 * The permission in the application is given manually and then only the contacts can be received.
 * This manual permission is fine only upto MarshMallow version, starting Marshmallow version, permission
 * needs to given in the app itself displaying the permission dialog box, known as MarshMallow permission.
 * */

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_view = (TextView)findViewById(R.id.textView1);
        fetchContacts();
    }

    public void fetchContacts() {
        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String Email_CONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();
        //ContentResolver contentResolver = getContentResolver();

        Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
        // Loop for every contact in the phone
        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if(hasPhoneNumber > 0) {
                    output.append("\n First name:" + name);

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = getContentResolver().query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] {contact_id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number: " + phoneNumber);
                    }
                    phoneCursor.close();

                    // Query and loop for every email of the contact
                    Cursor emailCursor = getContentResolver().query(EmailCONTENT_URI, null, Email_CONTACT_ID + " = ?", new String[] {contact_id}, null);
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        output.append("\n Email: " + email);
                    }
                    emailCursor.close();
                }
                output.append("\n");
            }
            text_view.setText(output);
        }
    }
}
