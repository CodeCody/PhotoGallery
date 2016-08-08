package com.example.timemanagement.photogallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by TimeManagement on 8/8/2015.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected  abstract Fragment createFragment();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        //To add a fragment to an activity in code,you make explicit calls to the activity's FragmentManager.
        //You call getSupportFragmentManager() because you are using the support library and the FragmentActivity class.
        //If you are not interested in compatibility you would just subclass Activity and call getFragmentManager()
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);//get fragmentContainer,assign to fragment

        if (fragment == null)//if fragment is null,in other words fragmentContainer has not been found
        {
            fragment = createFragment();//create fragment
            //Fragment transactions are used to add,remove,attach,detach or replace fragments in the fragment list.
            //They are the heart of how you use fragments to compose and recompose screens at runtime.
            //The Fragment manager uses a back stack of fragment transactions that you can navigate.
            //FragmentManger.beginTransaction() method creates and returns an instance of FragmentTransaction.
            //The FragmentTransaction class uses a fluent interface,methods that configure FragmentTransaction return that same object instead of void
            //to chain them together. So the following code says, "Create a new fragment transaction,include one add operation in it, and then commit it."
            //The add method is the meat of the transaction. It has two parameters, a container view ID and the CrimeFragment. The container view ID is the
            //resource ID of the FrameLayout you defined in activity_crime.xml. A container view tells the FragmentManger where in the activity's view
            //the fragment's view should appear,and it is use as a unique identifier for a fragment in the FragmentManager's list.
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }
}
