package com.mrndokist.app.shortcuts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.mrndokist.app.shortcuts.fragments.FragmentBottomSheetDialogFull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShorcutsActivity extends AppCompatActivity {


    Button btnview, btnSend;
    TextView txtname, txtphno, text, text2;
    ImageView mImageView;
    android.support.design.widget.CheckableImageButton btn_call, btn_msg;

    String cNumber;

    //String contactID;
    private static String contactNumber;
    Cursor cursor;


    String mimeString = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call";

    String displayName = null;
    String name = "Peters Brothers"; // here you can give static name.
    Long _id;

    private Button mLevelTwoButton;

    private TextView mLevelTextView;
    String androidId;
    EditText editTextSearch;
    String yourPhoneNumber;

    Dialog localDialog;

    Typeface localTypeface1;
    Typeface localTypeface2;
    static final int PICK_CONTACT = 1;
    String st;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;

    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        localTypeface1 = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Regular.ttf");
        localTypeface2 = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Bold.ttf");


        AccessContact();
        init();


        //Image Bouton recherche
        findViewById(R.id.imgbtnContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });//fin


        //Bouton envoi Message
        btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aboutDialog();
                yourPhoneNumber = editTextSearch.getText().toString();
                if (yourPhoneNumber.isEmpty()) {
                    Toast.makeText(ShorcutsActivity.this, R.string.editNumber, Toast.LENGTH_SHORT).show();
                    //Snackbar.make(v, R.string.editNumber, Snackbar.LENGTH_LONG).show();
                } else
                    sendWhatsapppMessage(yourPhoneNumber);


            }
        });

        //DEBUT
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                yourPhoneNumber = editTextSearch.getText().toString();
                if (yourPhoneNumber.isEmpty()) {
                    Toast.makeText(ShorcutsActivity.this, R.string.editNumber, Toast.LENGTH_SHORT).show();
                    //Snackbar.make(v, R.string.editNumber, Snackbar.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    String toNumber = yourPhoneNumber.replace("+", "").replace(" ", "");
                    //here you have to pass whatsApp contact  number  as  contact_number .
                    //test
                    String name = getContactName(toNumber, ShorcutsActivity.this);
                    int whatsappcall = getContactIdForWhatsAppCall(name, ShorcutsActivity.this);
                    if (whatsappcall != 0) {
                        intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + whatsappcall),
                                "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                        intent.setPackage("com.whatsapp");

                        startActivity(intent);
                        // editTextSearch.setText("");


                    }


                }

            }
        });
    }//FIN APPEL


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.return_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(ShorcutsActivity.this, TabsActivity.class));
            finish();
            return true;
            //Repost to whatsapp
        }

        if (id == R.id.action_whtsapp) {
            startNewActivity(getApplicationContext(), "com.whatsapp");
            return true;
            //Repost to whatsapp
        }

        if (id == R.id.action_help) {
            FragmentBottomSheetDialogFull fragment = new FragmentBottomSheetDialogFull();
            fragment.show(getSupportFragmentManager(), fragment.getTag());
            return true;
            //Repost to whatsapp
        }


        return super.onOptionsItemSelected(item);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AccessContact() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_MULTIPLE_PERMISSIONS);
            return;
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ShorcutsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        try {
                            if (hasPhone.equalsIgnoreCase("1")) {
                                Cursor phones = getContentResolver().query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                        null, null);
                                phones.moveToFirst();
                                cNumber = phones.getString(phones.getColumnIndex("data1"));
                                System.out.println("number is:" + cNumber);
                                //  txtphno.setText("Phone Number is: " + cNumber);
                                editTextSearch.setText(cNumber);
                            }
                            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            txtphno.setText(name);
                            if (txtphno.length() > 0) {
                                txtphno.setVisibility(View.VISIBLE);
                            } else
                                txtphno.setVisibility(View.INVISIBLE);


                            long contactId = c.getPosition();
                            long photoId = c.getLong(c.getColumnIndex(
                                    ContactsContract.Contacts.PHOTO_ID));
                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    }
                }
                break;
        }
    }


    //Envoi du message par sur Whatsapp
    private String a(String paramString) {
        return PhoneNumberUtils.stripSeparators(paramString);
    }

    private void sendWhatsapppMessage(String number) {
        try {
            String toNumber = number.replace("+", "").replace(" ", "");
            String str = a(toNumber) + "@s.whatsapp.net";
            Intent localIntent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + str));
            localIntent.putExtra("jid", str);
            localIntent.putExtra("sms_body", "");
            localIntent.setPackage("com.whatsapp");
            startActivity(localIntent);
            return;
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Toast.makeText(this, "Make Sure you have WhatsApp App Installed on Your Device", Toast.LENGTH_SHORT).show();
        }
    }


    //Lancement d'une autre application whatsapp

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + packageName));
            context.startActivity(intent);
        }
    }//FIN

    public void init() {
        // txtname = (TextView) findViewById(R.id.txtname);
        txtphno = (TextView) findViewById(R.id.txtphno);
        text = (TextView) findViewById(R.id.text);
        text2 = (TextView) findViewById(R.id.text2);
        editTextSearch = findViewById(R.id.editTextSearch);
        btn_call = findViewById(R.id.imageView2);
        btn_msg = findViewById(R.id.imageView3);

        //btnSend = (Button) findViewById(R.id.btnSend);

        text.setTypeface(localTypeface2);
        text2.setTypeface(localTypeface1);
        txtphno.setTypeface(localTypeface2);
        editTextSearch.setTypeface(localTypeface1);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ShorcutsActivity.this, TabsActivity.class));
        finish();
    }


    //Gestion des appels whatsapp

    //Verification du numero
    // first  check wether  number have whatsapp   or  not  ...if you dont know

    //if  rowContactId (retour type of hasWhatsapp) is  not equal to '0'   then this number has whatsapp..

    //DEUXIEME METHODE
    //   public String hasWhatsapp(  getContactIDFromNumber(795486179).toString(),ShortcutsActivity.this )

    public String hasWhatsapp(String contactID) {
        String rowContactId = null;
        boolean hasWhatsApp;

        String[] projection = new String[]{ContactsContract.RawContacts._ID};
        String selection = ContactsContract.Data.CONTACT_ID + " = ? AND account_type IN (?)";
        String[] selectionArgs = new String[]{contactID, "com.whatsapp"};
        Cursor cursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
        if (cursor != null) {
            hasWhatsApp = cursor.moveToNext();
            if (hasWhatsApp) {
                rowContactId = cursor.getString(0);
            }
            cursor.close();
        }
        return rowContactId;
    }


    //FIRST METHODE

    public static int getContactIDFromNumber(String contactNumber, Context context) {
        contactNumber = Uri.encode(contactNumber);
        int phoneContactID = new Random().nextInt();
        Cursor contactLookupCursor = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, contactNumber), new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, null, null, null);
        while (contactLookupCursor.moveToNext()) {
            phoneContactID = contactLookupCursor.getInt(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
        }
        contactLookupCursor.close();

        return phoneContactID;
    }

    public String getContactName(final String phoneNumber, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }


    public int getContactIdForWhatsAppCall(String name, Context context) {

        cursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data._ID},
                ContactsContract.Data.DISPLAY_NAME + "=? and " + ContactsContract.Data.MIMETYPE + "=?",
                new String[]{name, "vnd.android.cursor.item/vnd.com.whatsapp.voip.call"},
                ContactsContract.Contacts.DISPLAY_NAME);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data._ID));
            System.out.println("9999999999999999          name  " + name + "      id    " + phoneContactID);
            return phoneContactID;
        } else {
            System.out.println("8888888888888888888          ");

            //Dialogue information
            View info = LayoutInflater.from(context).inflate(R.layout.call_help, null);

            TextView titre = info.findViewById(R.id.tv_nameP);
            TextView mInfo = info.findViewById(R.id.tv_info);

            titre.setTypeface(localTypeface1);
            mInfo.setTypeface(localTypeface1);

            BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setContentView(info);
            dialog.show();

            return 0;
        }
    }


    //ID POUR VIDEO CALL
    public int getContactIdForWhatsAppVideoCall(String name, Context context) {
        Cursor cursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data._ID},
                ContactsContract.Data.DISPLAY_NAME + "=? and " + ContactsContract.Data.MIMETYPE + "=?",
                new String[]{name, "vnd.android.cursor.item/vnd.com.whatsapp.video.call"},
                ContactsContract.Contacts.DISPLAY_NAME);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data._ID));
            return phoneContactID;
        } else {
            System.out.println("8888888888888888888          ");
            return 0;
        }
    }

}
