package com.example.timemanagement.photogallery;

import android.support.v4.app.Fragment;

/**
 * Created by TimeManagement on 8/15/2015.
 */
public class PhotoPageActivity extends SingleFragmentActivity
{
    @Override
    public Fragment createFragment()
    {
        return new PhotoPageFragment();
    }
}
