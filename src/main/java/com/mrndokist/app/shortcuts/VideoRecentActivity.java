package com.mrndokist.app.shortcuts;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.eftimoff.viewpagertransformers.FlipHorizontalTransformer;
import com.mrndokist.app.shortcuts.Adapters.ViewPagerAdepterForVideo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class VideoRecentActivity extends AppCompatActivity {


    ViewPagerAdepterForVideo a;
    ViewPager b;
    Integer c;
    String e;
    // ArrayList<String> f;
    ArrayList<File> f;
    Parcelable uriForFile;

    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/Download/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar.setTitleTextColor("");
        toolbar.setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(" ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getIntent().hasExtra("FileArray")) {
            this.f = (ArrayList<File>) getIntent().getSerializableExtra("FileArray");
        }
        this.e = Environment.getExternalStorageDirectory().toString() + DIRECTORY_TO_SAVE_MEDIA_NOW;
        this.c = Integer.valueOf(getIntent().getIntExtra("Position", 0));
        this.a = new ViewPagerAdepterForVideo(getBaseContext(), this.f);
        this.b = (ViewPager) findViewById(R.id.pagerV);
        this.b.setPageTransformer(true, new FlipHorizontalTransformer());
        this.b.setAdapter(this.a);
        this.b.setCurrentItem(this.c.intValue(), true);
        this.b.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                VideoRecentActivity.this.c = Integer.valueOf(i);
            }

            public void onPageScrollStateChanged(int i) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Integer.valueOf(b.getCurrentItem());
                //Video file path
                File file = new File(String.valueOf(f.get(c.intValue())));
                Uri uriForFile;
               Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uriForFile = FileProvider.getUriForFile(VideoRecentActivity.this, BuildConfig.APPLICATION_ID + ".provider",file);
                    intent.setDataAndType(uriForFile, "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    final String str = (String) uriForFile.toString();
                    if (str.contains(".mp4") || str.contains(".3gp") || str.contains(".gif")) {
                        intent.setDataAndType((Uri) uriForFile, "video/*");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "No application found to open this file.", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else{
                    uriForFile = Uri.fromFile(file);
                    Log.v("Test uri", "LINK URIFILE 23" + uriForFile);
                    final String str = (String) uriForFile.toString();
                    if (str.contains(".mp4") || str.contains(".3gp") || str.contains(".gif")) {
                        intent.setDataAndType((Uri) uriForFile, "video/*");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "No application found to open this file.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        getMenuInflater().inflate(R.menu.status, paramMenu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        Intent intent;
        if (itemId == android.R.id.home) {

            finish();

            //Repost to whatsapp
        } else if (itemId == R.id.repost) {
            this.c = Integer.valueOf(this.b.getCurrentItem());
            uriForFile = Uri.fromFile(this.f.get(this.c.intValue()).getAbsoluteFile());
            final String str = (String) uriForFile.toString();
            intent = new Intent("android.intent.action.SEND");
            if (str.contains(".mp4") || str.contains(".gif") || str.contains(".3gp")) {
                intent.setType("video/*");
            } else {
                intent.setType("image/*");
            }
            intent.setPackage("com.whatsapp");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.putExtra("android.intent.extra.TEXT", "Downloaded with " + getResources().getString(R.string.app_name) + " android app\nhttp://play.google.com/store/apps/details?id=com.mrndokist.app.shortcuts");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Whatsapp have not been installed", Toast.LENGTH_LONG).show();
            }
        }
        //Save in gallery

        else if (itemId == R.id.download) {
            this.c = Integer.valueOf(this.b.getCurrentItem());
            File currentFile = f.get(c);
            final String str = (String) currentFile.toString();
            File localFile1 = this.f.get(this.c.intValue()).getAbsoluteFile();

            String str1 = (str.substring(1 + ((String) this.f.get(this.c.intValue()).getAbsolutePath()).lastIndexOf("/")));
            String str2 = this.e + str1;
            File localFile2 = new File(str2);
            try {
                a(localFile1, localFile2);
                Intent localIntent1 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                localIntent1.setData(Uri.fromFile(localFile2));
                sendBroadcast(localIntent1);
                Toast.makeText(getApplicationContext(), "Status save successfully to your Gallery", Toast.LENGTH_SHORT).show();
            } catch (IOException localIOException) {
                while (true)
                    localIOException.printStackTrace();
            }
            //Share
        } else if (itemId == R.id.share) {
            uriForFile = Uri.fromFile(new File((String) this.f.get(this.c.intValue()).getAbsolutePath()));
            intent = new Intent("android.intent.action.SEND");
            if (((String) this.f.get(this.c.intValue()).getAbsolutePath()).contains(".mp4") || ((String) this.f.get(this.c.intValue()).getAbsolutePath()).contains(".gif")
                    || ((String) this.f.get(this.c.intValue()).getAbsolutePath()).contains(".3gp")) {
                intent.setType("video/*");
            } else {
                intent.setType("image/*");
            }
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.putExtra("android.intent.extra.TEXT", "Downloaded with " + getResources().getString(R.string.app_name) + " android app\nhttp://play.google.com/store/apps/details?id=com.mrndokist.app.shortcuts");
            startActivity(Intent.createChooser(intent, "Share image using"));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private boolean a(File paramFile1, File paramFile2)
            throws IOException {
        if (paramFile1.getAbsolutePath().equals(paramFile2.getAbsolutePath()))
            return true;
        FileInputStream localFileInputStream = new FileInputStream(paramFile1);
        FileOutputStream localFileOutputStream = new FileOutputStream(paramFile2);
        byte[] arrayOfByte = new byte[1024];
        while (true) {
            int i = localFileInputStream.read(arrayOfByte);
            if (i <= 0)
                break;
            localFileOutputStream.write(arrayOfByte, 0, i);
        }
        localFileInputStream.close();
        localFileOutputStream.close();
        return true;
    }


    /**
     * copy file to destination.
     *
     * @param sourceFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


}
