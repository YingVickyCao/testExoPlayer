package com.example.testexoplayer.download;

import android.content.Intent;

import com.example.testexoplayer.player.PlayerActivity;
import com.google.android.exoplayer2.util.Assertions;

public final class DrmInfo {
    public final String drm_scheme;
    public final String drm_license_url;
    public final String[] drmKeyRequestProperties;
    public final boolean drmMultiSession;

    public DrmInfo(String drm_scheme, String drm_license_url, String[] drmKeyRequestProperties, boolean drmMultiSession) {
        this.drm_scheme = drm_scheme;
        this.drm_license_url = drm_license_url;
        this.drmKeyRequestProperties = drmKeyRequestProperties;
        this.drmMultiSession = drmMultiSession;
    }

    public void updateIntent(Intent intent) {
        Assertions.checkNotNull(intent);
        intent.putExtra(PlayerActivity.DRM_SCHEME_EXTRA, drm_scheme);
        intent.putExtra(PlayerActivity.DRM_LICENSE_URL_EXTRA, drm_license_url);
        intent.putExtra(PlayerActivity.DRM_KEY_REQUEST_PROPERTIES_EXTRA, drmKeyRequestProperties);
        intent.putExtra(PlayerActivity.DRM_MULTI_SESSION_EXTRA, drmMultiSession);
    }
}