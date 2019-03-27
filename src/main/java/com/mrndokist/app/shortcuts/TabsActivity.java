package com.mrndokist.app.shortcuts;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.mrndokist.app.shortcuts.base.BaseActivity;
import com.mrndokist.app.shortcuts.tabs.ImagesFragment;
import com.mrndokist.app.shortcuts.tabs.VideoFragment;

import java.util.ArrayList;
import java.util.List;

public class TabsActivity extends BaseActivity {

    NavigationTabStrip a;
    Toolbar b;
    ViewPager c;
    ImagesFragment d;
    VideoFragment e;
    private long exitTime = 0L;

    String m = "";
    Dialog localDialog;
    final String APP_WCC = "com.mrndokist.app.worldcountriescodes";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.d = new ImagesFragment();
        this.e = new VideoFragment();
        this.c = (ViewPager) findViewById(R.id.viewpager);
        this.a = (NavigationTabStrip) findViewById(R.id.tabs);
        getSupportActionBar().setTitle(getString(R.string.recents));

        a(this.c);
        this.a.setViewPager(this.c);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TabsActivity.this,ShorcutsActivity.class));
                finish();
            }
        });


        //SHORTCUT ICON DESKTOP
        if (!getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).getBoolean("IS_ICON_CREATED", false)) {
            createShortCut(); //Add shortcut on Home screen
            getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).edit().putBoolean("IS_ICON_CREATED", true).commit();
        }
    }


    private void a(ViewPager paramViewPager)
    {
        a locala = new a(getSupportFragmentManager());
        locala.a(this.d, "IMAGES");
        locala.a(this.e, "VIDEOS");
        paramViewPager.setAdapter(locala);
    }

    //View pager fragment adapter
   private  class a extends FragmentPagerAdapter {
        private final List<Fragment> b = new ArrayList();
        private final List<String> c = new ArrayList();

        a(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int i) {
            return (Fragment) this.b.get(i);
        }

        public int getCount() {
            return this.b.size();
        }

        void a(Fragment fragment, String str) {
            this.b.add(fragment);
            this.c.add(str);
        }

        public CharSequence getPageTitle(int i) {
            return (CharSequence) this.c.get(i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider myShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        this.m = getPackageName();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "WhatstApp Shortcuts");
        StringBuilder stringBuilder = new StringBuilder();
        intent.putExtra("android.intent.extra.TEXT", stringBuilder
                .append("Try this App 'WhatsApp Shortcuts' which helps you to save all WhatsApp Statuses \nTo Write and to Call no saving number quickly to WhatsApp Messenger...!\n")
                .append(" https://play.google.com/store/apps/details?id=").append(this.m).toString());
       myShareActionProvider.setShareIntent(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            aboutDialog();
            return true;
        }


        if (id == R.id.action_wsapp) {
            startNewActivity(getApplicationContext(), "com.whatsapp");
            return true;
        }

        if (id == R.id.action_help) {
            //aboutMoreApps();

            return true;
        }

       if (id == R.id.action_rateus) {
           this.m = getPackageName();
           try {
               startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.m)));
               return true;
           } catch (ActivityNotFoundException e) {
               startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + this.m)));
               return true;
           }

        }

        return super.onOptionsItemSelected(item);
    }



    //Dialogue about

    private void aboutDialog() {
        localDialog = new Dialog(this);
        localDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        localDialog.setContentView(R.layout.dialog_about);
        localDialog.setCancelable(false);
        localDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.copyFrom(localDialog.getWindow().getAttributes());
        localLayoutParams.width = -2;
        localLayoutParams.height = -2;
        //((TextView) localDialog.findViewById(R.id.tv_version)).setText("Version 1.0");


        localDialog.findViewById(R.id.bt_rateapp).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Intent localIntent = new Intent("android.intent.action.VIEW");
                localIntent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" +  getPackageName()));
                TabsActivity.this.startActivity(localIntent);
            }
        });

        localDialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localDialog.dismiss();
            }
        });

        localDialog.show();
        localDialog.getWindow().setAttributes(localLayoutParams);
    }


    //Help More applications

    private void aboutMoreApps() {
        localDialog = new Dialog(this);
        localDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        localDialog.setContentView(R.layout.dialog_help_countries);
        localDialog.setCancelable(false);
        localDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.copyFrom(localDialog.getWindow().getAttributes());
        localLayoutParams.width = -2;
        localLayoutParams.height = -2;
        //((TextView) localDialog.findViewById(R.id.tv_version)).setText("Developped by MR NDOKIST\nVersion 1.0");


        localDialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localDialog.dismiss();
            }
        });

        localDialog.findViewById(R.id.bt_getcode).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramView)
            {
                Intent localIntent = new Intent("android.intent.action.VIEW");
                localIntent.setData(Uri.parse("http://play.google.com/store/apps/details?id="+APP_WCC));
                startActivity(localIntent);
            }
        });
        localDialog.show();
        localDialog.getWindow().setAttributes(localLayoutParams);
    }


    //Shotcuthjyukikiki_k_

    //SHorcuts
    public void createShortCut() {
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutintent.putExtra("duplicate", false);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_log);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), ShorcutsActivity.class));
        sendBroadcast(shortcutintent);
    }



    @Override
    public void onBackPressed() {
        exitApp();
    }


    public void exitApp()
    {
        if (System.currentTimeMillis() - this.exitTime > 2000L)
        {
            Toast.makeText(this, R.string.toast_exit_message, Toast.LENGTH_SHORT).show();
            this.exitTime = System.currentTimeMillis();
            return;
        }
        finish();
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


}
