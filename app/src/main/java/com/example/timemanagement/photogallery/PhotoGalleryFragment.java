package com.example.timemanagement.photogallery;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.annotation.Target;
import java.util.ArrayList;

/**
 * Created by TimeManagement on 8/10/2015.
 */
public class PhotoGalleryFragment extends VisibleFragment
{
    GridView mGridView;
    ArrayList<GalleryItem> mItems;
    private static final String TAG="PhotoGalleryFragment";

    ThumbNailDownloader<ImageView>mThumbnailThread;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        updateItems();

   //   Intent i=new Intent(getActivity(),PollService.class);
   //   getActivity().startService(i);

     //   PollService.setServiceAlarm(getActivity(),true);

        mThumbnailThread=new ThumbNailDownloader<>(new Handler());
        mThumbnailThread.setListener(new ThumbNailDownloader.Listener<ImageView>()
        {
            @Override
            public void onThumbnailDownload(ImageView imageView, Bitmap thumbnail) {
                if(isVisible())
                    imageView.setImageBitmap(thumbnail);
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        Log.i(TAG, "Background thread started.");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_photo_gallery, menu);
    }

    @Override
    @TargetApi(11)
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_item_clear:
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(FlickrFetchr.PREF_SEARCH_QUERY,null).commit();
                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm=!PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(),shouldStartAlarm);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    getActivity().invalidateOptionsMenu();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);

        MenuItem toggleItem=menu.findItem(R.id.menu_item_toggle_polling);
        if(PollService.isServiceAlarmOn(getActivity()))
        {
            toggleItem.setTitle(R.string.stop_polling);
        }
        else
            toggleItem.setTitle(R.string.start_polling);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i(TAG, "Background thread destroyed");
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_photo_gallery,parent,false);

        mGridView=(GridView)v.findViewById(R.id.gridView);
        setUpAdapter();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GalleryItem item=mItems.get(position);
                Uri photoPageUri=Uri.parse(item.getPhotoPageUrl());
                Intent i=new Intent(getActivity(),PhotoPageActivity.class);
                i.setData(photoPageUri);

                startActivity(i);

            }
        });
        return v;
    }



    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<GalleryItem>>
    {
        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params)
        {
            Activity activity=getActivity();
            if(activity==null)
                return new ArrayList<GalleryItem>();

            String query= PreferenceManager.getDefaultSharedPreferences(activity).getString(FlickrFetchr.PREF_SEARCH_QUERY,null);
            if(query!=null) {
                return new FlickrFetchr().search(query);
            }
            else
            {
                return new FlickrFetchr().fetchItems();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items)
        {
            mItems=items;
            setUpAdapter();
        }

    }

    public void updateItems()
    {
        new FetchItemsTask().execute();
    }

    void setUpAdapter()
    {
        if(getActivity()==null || mGridView==null)
            return;

        if(mItems!=null)
        {
            mGridView.setAdapter(new GalleryItemAdapter(mItems));
        }
        else
        {
            mGridView.setAdapter(null);
        }
    }


    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem>
    {
        public GalleryItemAdapter(ArrayList<GalleryItem> items)
        {
            super(getActivity(),0,items);
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent)
        {
            if(convertView==null)
                convertView=getActivity().getLayoutInflater().inflate(R.layout.gallery_item,parent,false);

            ImageView imageView=(ImageView)convertView.findViewById(R.id.gallery_item_imageview);
            imageView.setImageResource(R.drawable.brian_up_close);
            GalleryItem item=getItem(position);
            mThumbnailThread.queueThumbnail(imageView,item.getmURl());

            return convertView;
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }
}
