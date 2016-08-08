package com.example.timemanagement.photogallery;

/**
 * Created by TimeManagement on 8/11/2015.
 */
public class GalleryItem
{
    private String mCaption;
    private String mOwner;
    private String mId;
    private String mURl;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmCaption() {
        return mCaption;
    }

    public void setmCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getmURl() {
        return mURl;
    }

    public void setmURl(String mURl) {
        this.mURl = mURl;
    }

    public void setmOwner(String owner)
    {
        mOwner=owner;
    }

    public String getmOwner() {
        return  mOwner;
    }

   public String getPhotoPageUrl()
   {
       return "http://www.flickr.com/photos/"+mOwner+"/"+mId;
   }

    public String toString()
    {
        return mCaption;
    }
}
