package com.mrndokist.app.shortcuts.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mrndokist.app.shortcuts.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Acer on 15/02/2019.
 */

public class ImageRecentAdapter extends BaseAdapter {
    private Context a;
    private ArrayList<File> filesList;


    public ImageRecentAdapter(ArrayList<File> filesList, Context context) {

        this.filesList = filesList;
        this.a = context;
    }

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) this.a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = layoutInflater.inflate(R.layout.recents_grid_item, null);
        a locala = new a();
        locala.a = (ImageView) inflate.findViewById(R.id.img_theme);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.isVideo);
        inflate.setTag(locala);
        File currentFile = filesList.get(i);
        if (filesList.get(i) != null)
        {
            Uri  localUri = Uri.fromFile(currentFile);
            if ((localUri.toString() != null) && (localUri.toString().length() > 0))
                Glide.with(this.a).load(localUri).into(locala.a);
        }
        else
            Toast.makeText(a,"Erreur",Toast.LENGTH_SHORT).show();

        inflate.setLayoutParams(new AbsListView.LayoutParams(new LinearLayout.LayoutParams(-1, -1)));
        return inflate;
    }

    public int getCount() {
        return this.filesList.size();
    }

    public Object getItem(int i) {
        return null;
    }


    public long getItemId(int i) {
        return 0;
    }

    private static class a {
        ImageView a;

        private a() {
        }
    }
}