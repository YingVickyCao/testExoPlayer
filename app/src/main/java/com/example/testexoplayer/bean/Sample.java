package com.example.testexoplayer.bean;

import android.content.Context;
import android.content.Intent;

import com.example.testexoplayer.player.PlayerActivity;
import com.example.testexoplayer.download.DrmInfo;

public abstract class Sample {
    public final String name;
    public final DrmInfo drmInfo;

    public Sample(String name, DrmInfo drmInfo) {
        this.name = name;
        this.drmInfo = drmInfo;
    }

    public Intent buildIntent(Context context, boolean preferExtensionDecoders, String abrAlgorithm) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(PlayerActivity.PREFER_EXTENSION_DECODERS_EXTRA, preferExtensionDecoders);
        intent.putExtra(PlayerActivity.ABR_ALGORITHM_EXTRA, abrAlgorithm);
        if (drmInfo != null) {
            drmInfo.updateIntent(intent);
        }
        return intent;
    }

}
