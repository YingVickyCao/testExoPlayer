package com.example.testexoplayer.list;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;

import com.example.testexoplayer.bean.PlaylistSample;
import com.example.testexoplayer.bean.Sample;
import com.example.testexoplayer.bean.UriSample;
import com.example.testexoplayer.download.DemoApplication;
import com.example.testexoplayer.download.DrmInfo;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class SampleListLoader extends AsyncTask<String, Void, List<SampleGroup>> {
    private SampleChooserActivity sampleChooserActivity;
    private boolean sawError;

    public SampleListLoader(SampleChooserActivity sampleChooserActivity) {
        this.sampleChooserActivity = sampleChooserActivity;
    }

    @Override
    protected List<SampleGroup> doInBackground(String... uris) {
        List<SampleGroup> result = new ArrayList<>();
        Context context = sampleChooserActivity.getApplicationContext();
        String userAgent = ((DemoApplication) context).getUserAgent();
        DataSource dataSource = new DefaultDataSource(context, userAgent, false);

        for (String uri : uris) {
            // asset:///media.exolist.json
            DataSpec dataSpec = new DataSpec(Uri.parse(uri));
            InputStream inputStream = new DataSourceInputStream(dataSource, dataSpec);
            try {
                readSampleGroups(new JsonReader(new InputStreamReader(inputStream, "UTF-8")), result);
            } catch (Exception e) {
                Log.e(SampleChooserActivity.TAG, "Error loading sample list: " + uri, e);
                sawError = true;
            } finally {
                Util.closeQuietly(dataSource);
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<SampleGroup> result) {
        sampleChooserActivity.setAdapterDataSource(result, sawError);
    }

    private void readSampleGroups(JsonReader reader, List<SampleGroup> groups) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            readSampleGroup(reader, groups);
        }
        reader.endArray();
    }

    private void readSampleGroup(JsonReader reader, List<SampleGroup> groups) throws IOException {
        String groupName = "";
        ArrayList<Sample> samples = new ArrayList<>();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "name":
                    groupName = reader.nextString();
                    break;
                case "samples":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        samples.add(readEntry(reader, false));
                    }
                    reader.endArray();
                    break;
                case "_comment":
                    reader.nextString(); // Ignore.
                    break;
                default:
                    throw new ParserException("Unsupported name: " + name);
            }
        }
        reader.endObject();

        SampleGroup group = getGroup(groupName, groups);
        group.samples.addAll(samples);
    }

    private Sample readEntry(JsonReader reader, boolean insidePlaylist) throws IOException {
        String sampleName = null;
        Uri uri = null;
        String extension = null;
        String drm_scheme = null;
        String drm_license_url = null;
        String[] drm_key_request_properties = null;
        boolean drm_multi_session = false;
        ArrayList<UriSample> playlist = null;
        String ad_tag_uri = null;
        String spherical_stereo_mode = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "name":
                    sampleName = reader.nextString();
                    break;
                case "uri":
                    uri = Uri.parse(reader.nextString());
                    break;
                case "extension":
                    extension = reader.nextString();
                    break;
                case "drm_scheme":
                    Assertions.checkState(!insidePlaylist, "Invalid attribute on nested item: drm_scheme");
                    drm_scheme = reader.nextString();
                    break;
                case "drm_license_url":
                    Assertions.checkState(!insidePlaylist, "Invalid attribute on nested item: drm_license_url");
                    drm_license_url = reader.nextString();
                    break;
                case "drm_key_request_properties":
                    Assertions.checkState(!insidePlaylist, "Invalid attribute on nested item: drm_key_request_properties");
                    ArrayList<String> drmKeyRequestPropertiesList = new ArrayList<>();
                    reader.beginObject();
                    while (reader.hasNext()) {
                        drmKeyRequestPropertiesList.add(reader.nextName());
                        drmKeyRequestPropertiesList.add(reader.nextString());
                    }
                    reader.endObject();
                    drm_key_request_properties = drmKeyRequestPropertiesList.toArray(new String[0]);
                    break;
                case "drm_multi_session":
                    drm_multi_session = reader.nextBoolean();
                    break;
                case "playlist":
                    Assertions.checkState(!insidePlaylist, "Invalid nesting of playlists");
                    playlist = new ArrayList<>();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        playlist.add((UriSample) readEntry(reader, true));
                    }
                    reader.endArray();
                    break;
                case "ad_tag_uri":
                    ad_tag_uri = reader.nextString();
                    break;
                case "spherical_stereo_mode":
                    Assertions.checkState(!insidePlaylist, "Invalid attribute on nested item: spherical_stereo_mode");
                    spherical_stereo_mode = reader.nextString();
                    break;
                default:
                    throw new ParserException("Unsupported attribute name: " + name);
            }
        }
        reader.endObject();
        DrmInfo drmInfo = drm_scheme == null ? null : new DrmInfo(drm_scheme, drm_license_url, drm_key_request_properties, drm_multi_session);
        if (playlist != null) {
            UriSample[] playlistSamplesArray = playlist.toArray(new UriSample[playlist.size()]);
            return new PlaylistSample(sampleName, drmInfo, playlistSamplesArray);
        } else {
            return new UriSample(sampleName, drmInfo, uri, extension, ad_tag_uri, spherical_stereo_mode);
        }
    }

    private SampleGroup getGroup(String groupName, List<SampleGroup> groups) {
        for (int i = 0; i < groups.size(); i++) {
            if (Util.areEqual(groupName, groups.get(i).title)) {
                return groups.get(i);
            }
        }
        SampleGroup group = new SampleGroup(groupName);
        groups.add(group);
        return group;
    }
}