package com.example.testexoplayer.list;

import com.example.testexoplayer.list.Sample;

import java.util.ArrayList;
import java.util.List;

public final class SampleGroup {

    public final String title;
    public final List<Sample> samples;

    public SampleGroup(String title) {
        this.title = title;
        this.samples = new ArrayList<>();
    }

}
