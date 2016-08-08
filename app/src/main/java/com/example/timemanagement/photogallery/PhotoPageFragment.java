package com.example.timemanagement.photogallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by TimeManagement on 8/15/2015.
 */
public class PhotoPageFragment extends VisibleFragment
{
    private String mUrl;
    private WebView mWebView;

    @Override
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
        mUrl=getActivity().getIntent().getData().toString();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle saveInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_photo_page,parent,false);

        final ProgressBar progressBar=(ProgressBar)v.findViewById(R.id.progessBar);
        progressBar.setMax(100);
        final TextView titleTextView=(TextView)v.findViewById(R.id.title_Text_View);
        mWebView=(WebView)v.findViewById(R.id.webView);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView webView,int progess)
            {
                if(progess==100)
                    progressBar.setVisibility(View.INVISIBLE);
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(progess);
                }
            }
        });

        mWebView.loadUrl(mUrl);
        return v;
    }
}
