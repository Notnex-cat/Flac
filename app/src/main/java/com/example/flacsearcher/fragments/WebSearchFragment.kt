package com.example.flacsearcher.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.flacsearcher.R

class WebSearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.fragment_web_search, container, false)
        val mWebView = v.findViewById<View>(R.id.webse) as WebView
        mWebView.loadUrl("https://duckduckgo.com/")

        val webSettings: WebSettings = mWebView.getSettings()
        webSettings.javaScriptEnabled = true

        mWebView.setWebViewClient(WebViewClient())

        return v
    }
}