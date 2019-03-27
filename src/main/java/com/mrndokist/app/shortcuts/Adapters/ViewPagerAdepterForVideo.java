package com.mrndokist.app.shortcuts.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mrndokist.app.shortcuts.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Acer on 14/02/2019.
 */

public class ViewPagerAdepterForVideo extends PagerAdapter {

    private Context a;
    private LayoutInflater b;
    private ArrayList<File> c;


    public ViewPagerAdepterForVideo(Context context, ArrayList<File> arrayList) {
        this.a = context;
        this.b = ((LayoutInflater) this.a.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        this.c = arrayList;
    }

    public int getCount() {
        return this.c.size();
    }

    public Object getItem(int i) {
        return c.get(i);
    }


    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public Object instantiateItem(ViewGroup viewGroup, final int i) {
        View inflate = this.b.inflate(R.layout.viewpager_item_for_recents, viewGroup, false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.isVideo);
        final String str = (String) this.c.get(i).getAbsolutePath();

        if (str.contains(".mp4") || str.contains(".gif") ||str.contains(".3gp")) {
            imageView2.setVisibility(View.VISIBLE);
        }

        Uri uriForFile = Uri.fromFile(new File(str));
        if (this.c.get(i) != null) {
            if ((uriForFile.toString() != null) && (uriForFile.toString().length() > 0))
                Glide.with(this.a).load(uriForFile).into(imageView);
        } else
            Toast.makeText(a, "Erreur", Toast.LENGTH_SHORT).show();
        viewGroup.addView(inflate);

        return inflate;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((RelativeLayout) obj);
    }




}