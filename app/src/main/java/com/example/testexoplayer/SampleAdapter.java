package com.example.testexoplayer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

final class SampleAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    private SampleChooserActivity sampleChooserActivity;
    private List<SampleGroup> sampleGroups;

    public SampleAdapter(SampleChooserActivity sampleChooserActivity) {
        this.sampleChooserActivity = sampleChooserActivity;
        sampleGroups = Collections.emptyList();
    }

    public void setSampleGroups(List<SampleGroup> sampleGroups) {
        this.sampleGroups = sampleGroups;
        notifyDataSetChanged();
    }

    @Override
    public Sample getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).samples.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = sampleChooserActivity.getLayoutInflater().inflate(R.layout.sample_list_item, parent, false);
            View downloadButton = view.findViewById(R.id.download_button);
            downloadButton.setOnClickListener(this);
            downloadButton.setFocusable(false);
        }
        initializeChildView(view, getChild(groupPosition, childPosition));
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).samples.size();
    }

    @Override
    public SampleGroup getGroup(int groupPosition) {
        return sampleGroups.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = sampleChooserActivity.getLayoutInflater().inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }
        ((TextView) view).setText(getGroup(groupPosition).title);
        return view;
    }

    @Override
    public int getGroupCount() {
        return sampleGroups.size();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onClick(View view) {
        sampleChooserActivity.onSampleDownloadButtonClicked((Sample) view.getTag());
    }

    private void initializeChildView(View view, Sample sample) {
        view.setTag(sample);
        TextView sampleTitle = view.findViewById(R.id.sample_title);
        sampleTitle.setText(sample.name);

        boolean canDownload = sampleChooserActivity.getDownloadUnsupportedStringId(sample) == 0;
        boolean isDownloaded = canDownload && sampleChooserActivity.downloadTracker.isDownloaded(((UriSample) sample).uri);
        ImageButton downloadButton = view.findViewById(R.id.download_button);
        downloadButton.setTag(sample);
        downloadButton.setColorFilter(canDownload ? (isDownloaded ? 0xFF42A5F5 : 0xFFBDBDBD) : 0xFFEEEEEE);
        downloadButton.setImageResource(isDownloaded ? R.drawable.ic_download_done : R.drawable.ic_download);
    }
}
