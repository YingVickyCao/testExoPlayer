package com.example.testexoplayer.bean;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.testexoplayer.player.PlayerActivity;
import com.example.testexoplayer.download.DrmInfo;

public final class UriSample extends Sample {

    public final Uri uri;
    public final String extension;
    public final String ad_tag_uri;
    public final String spherical_stereo_mode;

    public UriSample(
            String name,
            DrmInfo drmInfo,
            Uri uri,
            String ad_tag_uri,
            String adTagUri,
            String spherical_stereo_mode) {
        super(name, drmInfo);
        this.uri = uri;
        this.extension = ad_tag_uri;
        this.ad_tag_uri = adTagUri;
        this.spherical_stereo_mode = spherical_stereo_mode;
    }

    @Override
    public Intent buildIntent(Context context, boolean preferExtensionDecoders, String abrAlgorithm) {
        return super.buildIntent(context, preferExtensionDecoders, abrAlgorithm)
                .setData(uri)
                .putExtra(PlayerActivity.EXTENSION_EXTRA, extension)
                .putExtra(PlayerActivity.AD_TAG_URI_EXTRA, ad_tag_uri)
                .putExtra(PlayerActivity.SPHERICAL_STEREO_MODE_EXTRA, spherical_stereo_mode)
                .setAction(PlayerActivity.ACTION_VIEW);
    }

}
