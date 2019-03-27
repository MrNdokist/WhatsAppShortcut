package com.mrndokist.app.shortcuts.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mrndokist.app.shortcuts.Adapters.ImageRecentAdapter;
import com.mrndokist.app.shortcuts.R;
import com.mrndokist.app.shortcuts.ViewRecentStatusActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment {

    GridView mGridView;
    ImageRecentAdapter mRecentAdapter;
    ArrayList<String> c = new ArrayList();
    Boolean d = Boolean.valueOf(false);
    private static final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";
    private ArrayList<File> filesList;

    private ArrayList<File> files;
    private File file;




    public ImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.frag_recent, container, false);
        mGridView = (GridView) inflate.findViewById(R.id.grid_view);

        files =  this.getListFiles(new File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION));
        this.mRecentAdapter =  new ImageRecentAdapter(files,getContext());
        this.mGridView.setAdapter(this.mRecentAdapter);

        //item click

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ViewRecentStatusActivity.class);
                intent.putExtra("FileArray", files);
                intent.putExtra("Position", i);
                startActivity(intent);
            }
        });

            return inflate;
    }


    /**
     * get all the files in specified directory
     *
     * @param parentDir
     * @return
     */
    private ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {

                if (file.getName().endsWith(".jpg") ||file.getName().endsWith(".png")) {
                    if (!inFiles.contains(file))
                        inFiles.add(file);
                }
            }
        }
        return inFiles;
    }
}
