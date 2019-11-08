/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.testexoplayer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.offline.DownloadService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An activity for selecting from a list of media samples.
 */
public class SampleChooserActivity extends Activity implements DownloadTracker.Listener {
    public static final String TAG = "SampleChooserActivity";

    private boolean useExtensionRenderers;
    public DownloadTracker downloadTracker;
    private SampleAdapter sampleAdapter;
    private MenuItem preferExtensionDecodersMenuItem;
    private MenuItem randomAbrMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_chooser_activity);
        sampleAdapter = new SampleAdapter(this);
        ExpandableListView sampleListView = findViewById(R.id.sample_list);
        sampleListView.setAdapter(sampleAdapter);
        sampleListView.setOnChildClickListener(SampleChooserActivity.this::onChildClick);


        DemoApplication application = (DemoApplication) getApplication();
        useExtensionRenderers = application.useExtensionRenderers();
        downloadTracker = application.getDownloadTracker();

        SampleListLoader loaderTask = new SampleListLoader(this);
        loaderTask.execute(getAssertResourceJsonFiless());

        // Start the download service if it should be running but it's not currently.
        // Starting the service in the foreground causes notification flicker if there is no scheduled
        // action. Starting it in the background throws an exception if the app is in the background too
        // (e.g. if device screen is locked).
        try {
            DownloadService.start(this, DemoDownloadService.class);
        } catch (IllegalStateException e) {
            DownloadService.startForeground(this, DemoDownloadService.class);
        }
    }

    private String[] getAssertResourceJsonFiless() {
        Intent intent = getIntent();
        String dataUri = intent.getDataString();
        String[] uris;
        if (dataUri != null) {
            uris = new String[]{dataUri};
        } else {
            ArrayList<String> uriList = new ArrayList<>();
            AssetManager assetManager = getAssets();
            try {
                for (String asset : assetManager.list("")) {
                    if (asset.endsWith(".exolist.json")) {
                        uriList.add("asset:///" + asset);
                    }
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), R.string.sample_list_load_error, Toast.LENGTH_LONG).show();
            }
            uris = new String[uriList.size()];
            uriList.toArray(uris);
            Arrays.sort(uris);
        }
        return uris;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sample_chooser_menu, menu);
        preferExtensionDecodersMenuItem = menu.findItem(R.id.prefer_extension_decoders);
        preferExtensionDecodersMenuItem.setVisible(useExtensionRenderers);
        randomAbrMenuItem = menu.findItem(R.id.random_abr);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        downloadTracker.addListener(this);
        sampleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        downloadTracker.removeListener(this);
        super.onStop();
    }

    @Override
    public void onDownloadsChanged() {
        sampleAdapter.notifyDataSetChanged();
    }

    public void onSampleGroups(final List<SampleGroup> groups, boolean sawError) {
        if (sawError) {
            Toast.makeText(getApplicationContext(), R.string.sample_list_load_error, Toast.LENGTH_LONG)
                    .show();
        }
        sampleAdapter.setSampleGroups(groups);
    }

    private boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
        Sample sample = (Sample) view.getTag();
        startActivity(sample.buildIntent(this, isNonNullAndChecked(preferExtensionDecodersMenuItem), isNonNullAndChecked(randomAbrMenuItem) ? PlayerActivity.ABR_ALGORITHM_RANDOM : PlayerActivity.ABR_ALGORITHM_DEFAULT));
        return true;
    }

    public void onSampleDownloadButtonClicked(Sample sample) {
        int downloadUnsupportedStringId = getDownloadUnsupportedStringId(sample);
        if (downloadUnsupportedStringId != 0) {
            Toast.makeText(getApplicationContext(), downloadUnsupportedStringId, Toast.LENGTH_LONG)
                    .show();
        } else {
            UriSample uriSample = (UriSample) sample;
            downloadTracker.toggleDownload(this, sample.name, uriSample.uri, uriSample.extension);
        }
    }

    public int getDownloadUnsupportedStringId(Sample sample) {
        if (sample instanceof PlaylistSample) {
            return R.string.download_playlist_unsupported;
        }
        UriSample uriSample = (UriSample) sample;
        if (uriSample.drmInfo != null) {
            return R.string.download_drm_unsupported;
        }
        if (uriSample.adTagUri != null) {
            return R.string.download_ads_unsupported;
        }
        String scheme = uriSample.uri.getScheme();
        if (!("http".equals(scheme) || "https".equals(scheme))) {
            return R.string.download_scheme_unsupported;
        }
        return 0;
    }

    private static boolean isNonNullAndChecked(@Nullable MenuItem menuItem) {
        // Temporary workaround for layouts that do not inflate the options menu.
        return menuItem != null && menuItem.isChecked();
    }

}