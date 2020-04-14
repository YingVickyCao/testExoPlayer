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
package com.google.android.exoplayer2.demo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaDrm;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.C.ContentType;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.demo.Sample.UriSample;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.RandomTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.spherical.SphericalGLSurfaceView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.ErrorMessageProvider;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import java.lang.reflect.Constructor;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * An activity that plays media using {@link SimpleExoPlayer}.
 */
public class PlayerActivity extends AppCompatActivity implements OnClickListener, PlaybackPreparer, PlayerControlView.VisibilityListener {
    private static final String TAG = PlayerActivity.class.getSimpleName();

    // Activity extras.

    public static final String SPHERICAL_STEREO_MODE_EXTRA = "spherical_stereo_mode";
    public static final String SPHERICAL_STEREO_MODE_MONO = "mono";
    public static final String SPHERICAL_STEREO_MODE_TOP_BOTTOM = "top_bottom";
    public static final String SPHERICAL_STEREO_MODE_LEFT_RIGHT = "left_right";

    // Actions.

    public static final String ACTION_VIEW = "com.google.android.exoplayer.demo.action.VIEW";
    public static final String ACTION_VIEW_LIST =
            "com.google.android.exoplayer.demo.action.VIEW_LIST";

    // Player configuration extras.

    public static final String ABR_ALGORITHM_EXTRA = "abr_algorithm";
    public static final String ABR_ALGORITHM_DEFAULT = "default";
    public static final String ABR_ALGORITHM_RANDOM = "random";

    // Media item configuration extras.

    public static final String URI_EXTRA = "uri";
    public static final String EXTENSION_EXTRA = "extension";
    public static final String IS_LIVE_EXTRA = "is_live";

    public static final String DRM_SCHEME_EXTRA = "drm_scheme";
    public static final String DRM_LICENSE_URL_EXTRA = "drm_license_url";
    public static final String DRM_KEY_REQUEST_PROPERTIES_EXTRA = "drm_key_request_properties";
    public static final String DRM_MULTI_SESSION_EXTRA = "drm_multi_session";
    public static final String PREFER_EXTENSION_DECODERS_EXTRA = "prefer_extension_decoders";
    public static final String TUNNELING_EXTRA = "tunneling";
    public static final String AD_TAG_URI_EXTRA = "ad_tag_uri";
    public static final String SUBTITLE_URI_EXTRA = "subtitle_uri";
    public static final String SUBTITLE_MIME_TYPE_EXTRA = "subtitle_mime_type";
    public static final String SUBTITLE_LANGUAGE_EXTRA = "subtitle_language";
    // For backwards compatibility only.
    public static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";

    // Saved instance state keys.

    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";

    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private PlayerView playerView;
    private LinearLayout debugRootView;
    private Button selectTracksButton;
    private TextView debugTextView;
    private boolean isShowingTrackSelectionDialog;

    private MyVideoListener mVideoListener;
    private DataSource.Factory dataSourceFactory;
    private SimpleExoPlayer mPlayer;
    private MediaSource mediaSource;
    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private DebugTextViewHelper debugViewHelper;
    private TrackGroupArray lastSeenTrackGroupArray;

    private boolean startAutoPlay;
    private int startWindow;
    private int currentIndex = -1;
    private long startPosition;

    // Fields used only for ad playback. The ads loader is loaded via reflection.

    private AdsLoader adsLoader;
    private Uri loadedAdTagUri;

    // Activity lifecycle

    private ImageButton playButton;
    private ImageButton pauseButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String sphericalStereoMode = intent.getStringExtra(SPHERICAL_STEREO_MODE_EXTRA);
        if (sphericalStereoMode != null) {
            setTheme(R.style.PlayerTheme_Spherical);
        }
        super.onCreate(savedInstanceState);
        dataSourceFactory = buildDataSourceFactory();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        setContentView(R.layout.player_activity);
        debugRootView = findViewById(R.id.controls_root);
        debugTextView = findViewById(R.id.debug_text_view);
        selectTracksButton = findViewById(R.id.select_tracks_button);
        selectTracksButton.setOnClickListener(this);

        playButton = findViewById(R.id.exo_play);
        pauseButton = findViewById(R.id.exo_pause);

        playerView = findViewById(R.id.player_view);
        playerView.setControllerVisibilityListener(this);
        playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        playerView.requestFocus();
        if (sphericalStereoMode != null) {
            int stereoMode;
            if (SPHERICAL_STEREO_MODE_MONO.equals(sphericalStereoMode)) {
                stereoMode = C.STEREO_MODE_MONO;
            } else if (SPHERICAL_STEREO_MODE_TOP_BOTTOM.equals(sphericalStereoMode)) {
                stereoMode = C.STEREO_MODE_TOP_BOTTOM;
            } else if (SPHERICAL_STEREO_MODE_LEFT_RIGHT.equals(sphericalStereoMode)) {
                stereoMode = C.STEREO_MODE_LEFT_RIGHT;
            } else {
                showToast(R.string.error_unrecognized_stereo_mode);
                finish();
                return;
            }
            ((SphericalGLSurfaceView) playerView.getVideoSurfaceView()).setDefaultStereoMode(stereoMode);
        }

        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            DefaultTrackSelector.ParametersBuilder builder =
                    new DefaultTrackSelector.ParametersBuilder(/* context= */ this);
            boolean tunneling = intent.getBooleanExtra(TUNNELING_EXTRA, false);
            if (Util.SDK_INT >= 21 && tunneling) {
                builder.setTunnelingAudioSessionId(C.generateAudioSessionIdV21(/* context= */ this));
            }
            trackSelectorParameters = builder.build();
            clearStartPosition();
        }
    }

    private void performPlayBtnClick() {
        runOnUiThread(() -> playButton.performClick());
    }

    private void performPauseBtnClick() {
        runOnUiThread(() -> pauseButton.performClick());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        releasePlayer();
        releaseAdsLoader();
        clearStartPosition();
        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseAdsLoader();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length == 0) {
            // Empty results are triggered if a permission is requested while another request was already
            // pending and can be safely ignored in this case.
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializePlayer();
        } else {
            showToast(R.string.storage_permission_denied);
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateTrackSelectorParameters();
        updateStartPosition();
        outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters);
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
    }

    // Activity input

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // See whether the player view wants to handle media or DPAD keys events.
        return playerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    // OnClickListener methods

    @Override
    public void onClick(View view) {
        if (view == selectTracksButton
                && !isShowingTrackSelectionDialog
                && TrackSelectionDialog.willHaveContent(trackSelector)) {
            isShowingTrackSelectionDialog = true;
            TrackSelectionDialog trackSelectionDialog =
                    TrackSelectionDialog.createForTrackSelector(
                            trackSelector,
                            /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
            trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);
        }
    }

    // PlaybackControlView.PlaybackPreparer implementation

    @Override
    public void preparePlayback() {
        mPlayer.retry();
    }

    // PlaybackControlView.VisibilityListener implementation

    @Override
    public void onVisibilityChange(int visibility) {
        debugRootView.setVisibility(visibility);
    }

    // Internal methods

    private void initializePlayer() {
        if (mPlayer == null) {
            Intent intent = getIntent();

            mediaSource = createTopLevelMediaSource(intent);
            if (mediaSource == null) {
                return;
            }

            TrackSelection.Factory trackSelectionFactory;
            String abrAlgorithm = intent.getStringExtra(ABR_ALGORITHM_EXTRA);
            if (abrAlgorithm == null || ABR_ALGORITHM_DEFAULT.equals(abrAlgorithm)) {
                trackSelectionFactory = new AdaptiveTrackSelection.Factory();
            } else if (ABR_ALGORITHM_RANDOM.equals(abrAlgorithm)) {
                trackSelectionFactory = new RandomTrackSelection.Factory();
            } else {
                showToast(R.string.error_unrecognized_abr_algorithm);
                finish();
                return;
            }

            boolean preferExtensionDecoders =
                    intent.getBooleanExtra(PREFER_EXTENSION_DECODERS_EXTRA, false);
            RenderersFactory renderersFactory =
                    ((DemoApplication) getApplication()).buildRenderersFactory(preferExtensionDecoders);

            trackSelector = new DefaultTrackSelector(/* context= */ this, trackSelectionFactory);
            trackSelector.setParameters(trackSelectorParameters);
            lastSeenTrackGroupArray = null;

            mPlayer = new SimpleExoPlayer.Builder(/* context= */ this, renderersFactory)
                    .setTrackSelector(trackSelector)
                    .build();
            initAudioFocus();
            mPlayer.addListener(new PlayerEventListener());
            mVideoListener = new MyVideoListener();
            mPlayer.addVideoListener(mVideoListener);
            mPlayer.setPlayWhenReady(startAutoPlay);
            setPlayWhenReady(startAutoPlay);
            mPlayer.addAnalyticsListener(new EventLogger(trackSelector));
            playerView.setPlayer(mPlayer);
            playerView.setPlaybackPreparer(this);
            debugViewHelper = new DebugTextViewHelper(mPlayer, debugTextView);
            debugViewHelper.start();
            if (adsLoader != null) {
                adsLoader.setPlayer(mPlayer);
            }
        }
        boolean haveStartPosition = startWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            mPlayer.seekTo(startWindow, startPosition);
        }
        mPlayer.prepare(mediaSource, !haveStartPosition, false);
        updateButtonVisibility();
    }

    @Nullable
    private MediaSource createTopLevelMediaSource(Intent intent) {
        String action = intent.getAction();
        boolean actionIsListView = ACTION_VIEW_LIST.equals(action);
        if (!actionIsListView && !ACTION_VIEW.equals(action)) {
            showToast(getString(R.string.unexpected_intent_action, action));
            finish();
            return null;
        }

        Sample intentAsSample = Sample.createFromIntent(intent);
        UriSample[] samples =
                intentAsSample instanceof Sample.PlaylistSample
                        ? ((Sample.PlaylistSample) intentAsSample).children
                        : new UriSample[]{(UriSample) intentAsSample};

        boolean seenAdsTagUri = false;
        for (UriSample sample : samples) {
            seenAdsTagUri |= sample.adTagUri != null;
            if (!Util.checkCleartextTrafficPermitted(sample.uri)) {
                showToast(R.string.error_cleartext_not_permitted);
                return null;
            }
            if (Util.maybeRequestReadExternalStoragePermission(/* activity= */ this, sample.uri)) {
                // The player will be reinitialized if the permission is granted.
                return null;
            }
        }

        MediaSource[] mediaSources = new MediaSource[samples.length];
        for (int i = 0; i < samples.length; i++) {
            mediaSources[i] = createLeafMediaSource(samples[i]);
            Sample.SubtitleInfo subtitleInfo = samples[i].subtitleInfo;
            if (subtitleInfo != null) {
                if (Util.maybeRequestReadExternalStoragePermission(
                        /* activity= */ this, subtitleInfo.uri)) {
                    // The player will be reinitialized if the permission is granted.
                    return null;
                }
                Format subtitleFormat =
                        Format.createTextSampleFormat(
                                /* id= */ null,
                                subtitleInfo.mimeType,
                                C.SELECTION_FLAG_DEFAULT,
                                subtitleInfo.language);
                MediaSource subtitleMediaSource =
                        new SingleSampleMediaSource.Factory(dataSourceFactory)
                                .createMediaSource(subtitleInfo.uri, subtitleFormat, C.TIME_UNSET);
                mediaSources[i] = new MergingMediaSource(mediaSources[i], subtitleMediaSource);
            }
        }
        MediaSource mediaSource =
                mediaSources.length == 1 ? mediaSources[0] : new ConcatenatingMediaSource(mediaSources);

        if (seenAdsTagUri) {
            Uri adTagUri = samples[0].adTagUri;
            if (actionIsListView) {
                showToast(R.string.unsupported_ads_in_concatenation);
            } else {
                if (!adTagUri.equals(loadedAdTagUri)) {
                    releaseAdsLoader();
                    loadedAdTagUri = adTagUri;
                }
                MediaSource adsMediaSource = createAdsMediaSource(mediaSource, adTagUri);
                if (adsMediaSource != null) {
                    mediaSource = adsMediaSource;
                } else {
                    showToast(R.string.ima_not_loaded);
                }
            }
        } else {
            releaseAdsLoader();
        }

        return mediaSource;
    }

    private MediaSource createLeafMediaSource(UriSample parameters) {
        Sample.DrmInfo drmInfo = parameters.drmInfo;
        int errorStringId = R.string.error_drm_unknown;
        DrmSessionManager<ExoMediaCrypto> drmSessionManager = null;
        if (drmInfo == null) {
            drmSessionManager = DrmSessionManager.getDummyDrmSessionManager();
        } else if (Util.SDK_INT < 18) {
            errorStringId = R.string.error_drm_unsupported_before_api_18;
        } else if (!MediaDrm.isCryptoSchemeSupported(drmInfo.drmScheme)) {
            errorStringId = R.string.error_drm_unsupported_scheme;
        } else {
            MediaDrmCallback mediaDrmCallback =
                    createMediaDrmCallback(drmInfo.drmLicenseUrl, drmInfo.drmKeyRequestProperties);
            drmSessionManager =
                    new DefaultDrmSessionManager.Builder()
                            .setUuidAndExoMediaDrmProvider(drmInfo.drmScheme, FrameworkMediaDrm.DEFAULT_PROVIDER)
                            .setMultiSession(drmInfo.drmMultiSession)
                            .build(mediaDrmCallback);
        }

        if (drmSessionManager == null) {
            showToast(errorStringId);
            finish();
            return null;
        }

        DownloadRequest downloadRequest =
                ((DemoApplication) getApplication())
                        .getDownloadTracker()
                        .getDownloadRequest(parameters.uri);
        if (downloadRequest != null) {
            return DownloadHelper.createMediaSource(downloadRequest, dataSourceFactory);
        }
        return createLeafMediaSource(parameters.uri, parameters.extension, drmSessionManager);
    }

    private MediaSource createLeafMediaSource(
            Uri uri, String extension, DrmSessionManager<?> drmSessionManager) {
        @ContentType int type = Util.inferContentType(uri, extension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .setDrmSessionManager(drmSessionManager)
                        .createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private HttpMediaDrmCallback createMediaDrmCallback(
            String licenseUrl, String[] keyRequestPropertiesArray) {
        HttpDataSource.Factory licenseDataSourceFactory =
                ((DemoApplication) getApplication()).buildHttpDataSourceFactory();
        HttpMediaDrmCallback drmCallback =
                new HttpMediaDrmCallback(licenseUrl, licenseDataSourceFactory);
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        return drmCallback;
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            abandonAudioFocus();
            updateTrackSelectorParameters();
            updateStartPosition();
            debugViewHelper.stop();
            debugViewHelper = null;
            if (null != mVideoListener) {
                mPlayer.removeVideoListener(mVideoListener);
            }
            mPlayer.release();
            mPlayer = null;
            mediaSource = null;
            trackSelector = null;
        }
        if (adsLoader != null) {
            adsLoader.setPlayer(null);
        }
    }

    private void releaseAdsLoader() {
        if (adsLoader != null) {
            adsLoader.release();
            adsLoader = null;
            loadedAdTagUri = null;
            playerView.getOverlayFrameLayout().removeAllViews();
        }
    }

    private void updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector.getParameters();
        }
    }

    private void updateStartPosition() {
        if (mPlayer != null) {
            startAutoPlay = mPlayer.getPlayWhenReady();
            startWindow = mPlayer.getCurrentWindowIndex();
            startPosition = Math.max(0, mPlayer.getContentPosition());
        }
    }

    private void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    /**
     * Returns a new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory() {
        return ((DemoApplication) getApplication()).buildDataSourceFactory();
    }

    /**
     * Returns an ads media source, reusing the ads loader if one exists.
     */
    @Nullable
    private MediaSource createAdsMediaSource(MediaSource mediaSource, Uri adTagUri) {
        // Load the extension source using reflection so the demo app doesn't have to depend on it.
        // The ads loader is reused for multiple playbacks, so that ad playback can resume.
        try {
            Class<?> loaderClass = Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsLoader");
            if (adsLoader == null) {
                // Full class names used so the LINT.IfChange rule triggers should any of the classes move.
                // LINT.IfChange
                Constructor<? extends AdsLoader> loaderConstructor =
                        loaderClass
                                .asSubclass(AdsLoader.class)
                                .getConstructor(android.content.Context.class, android.net.Uri.class);
                // LINT.ThenChange(../../../../../../../../proguard-rules.txt)
                adsLoader = loaderConstructor.newInstance(this, adTagUri);
            }
            MediaSourceFactory adMediaSourceFactory =
                    new MediaSourceFactory() {

                        private DrmSessionManager<?> drmSessionManager =
                                DrmSessionManager.getDummyDrmSessionManager();

                        @Override
                        public MediaSourceFactory setDrmSessionManager(DrmSessionManager<?> drmSessionManager) {
                            this.drmSessionManager = drmSessionManager;
                            return this;
                        }

                        @Override
                        public MediaSource createMediaSource(Uri uri) {
                            return PlayerActivity.this.createLeafMediaSource(
                                    uri, /* extension=*/ null, drmSessionManager);
                        }

                        @Override
                        public int[] getSupportedTypes() {
                            return new int[]{C.TYPE_DASH, C.TYPE_SS, C.TYPE_HLS, C.TYPE_OTHER};
                        }
                    };
            return new AdsMediaSource(mediaSource, adMediaSourceFactory, adsLoader, playerView);
        } catch (ClassNotFoundException e) {
            // IMA extension not loaded.
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // User controls

    private void updateButtonVisibility() {
        selectTracksButton.setEnabled(
                mPlayer != null && TrackSelectionDialog.willHaveContent(trackSelector));
    }

    private void showControls() {
        debugRootView.setVisibility(View.VISIBLE);
    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private String printState(@Player.State int playbackState) {
        switch (playbackState) {
            case Player.STATE_IDLE:
                return "STATE_IDLE";

            case Player.STATE_BUFFERING:
                return "STATE_BUFFERING";

            case Player.STATE_READY:
                return "STATE_READY";

            case Player.STATE_ENDED:
                return "STATE_ENDED";
        }
        return "InValid state";
    }

    private long getDuration() {
        if (null == mPlayer) {
            return 0;
        }
        return mPlayer.getDuration();
    }

    private long getVideoWidth() {
        if (null == mPlayer) {
            return 0;
        }
        Format videoFormat = mPlayer.getVideoFormat();
        if (null == videoFormat) {
            return 0;
        }
        return videoFormat.width;
    }

    private long getVideoHeight() {
        if (null == mPlayer) {
            return 0;
        }
        Format videoFormat = mPlayer.getVideoFormat();
        if (null == videoFormat) {
            return 0;
        }
        return videoFormat.height;
    }

    private class PlayerEventListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, @Player.State int playbackState) {
            Log.d(TAG, "onPlayerStateChanged: playWhenReady=" + playWhenReady + ",state=" + printState(playbackState) + ",duration=" + getDuration() + ",with=" + getVideoWidth() + ",height=" + getVideoHeight());
            if (playbackState == Player.STATE_ENDED) {
                showControls();
            }
            updateButtonVisibility();
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            if (isBehindLiveWindow(e)) {
                clearStartPosition();
                initializePlayer();
            } else {
                updateButtonVisibility();
                showControls();
            }
        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            updateButtonVisibility();
            if (trackGroups != lastSeenTrackGroupArray) {
                MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_video);
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                            == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_audio);
                    }
                }
                lastSeenTrackGroupArray = trackGroups;
            }
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            if (null == mPlayer) {
                return;
            }
            int newIndex = mPlayer.getCurrentWindowIndex();
            if (newIndex != currentIndex) {
                // The index has changed, update the UI to show info for source at newIndex.
                Log.d(TAG, "onPositionDiscontinuity: currentIndex=" + currentIndex + ",newIndex=" + newIndex);
                currentIndex = newIndex;
            }
        }
    }

    private class PlayerErrorMessageProvider implements ErrorMessageProvider<ExoPlaybackException> {

        @Override
        public Pair<Integer, String> getErrorMessage(ExoPlaybackException e) {
            String errorString = getString(R.string.error_generic);
            if (e.type == ExoPlaybackException.TYPE_RENDERER) {
                Exception cause = e.getRendererException();
                if (cause instanceof DecoderInitializationException) {
                    // Special case for decoder initialization failures.
                    DecoderInitializationException decoderInitializationException =
                            (DecoderInitializationException) cause;
                    if (decoderInitializationException.codecInfo == null) {
                        if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
                            errorString = getString(R.string.error_querying_decoders);
                        } else if (decoderInitializationException.secureDecoderRequired) {
                            errorString =
                                    getString(
                                            R.string.error_no_secure_decoder, decoderInitializationException.mimeType);
                        } else {
                            errorString =
                                    getString(R.string.error_no_decoder, decoderInitializationException.mimeType);
                        }
                    } else {
                        errorString =
                                getString(
                                        R.string.error_instantiating_decoder,
                                        decoderInitializationException.codecInfo.name);
                    }
                }
            }
            return Pair.create(0, errorString);
        }
    }

    private class MyVideoListener implements VideoListener {
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            Log.d(TAG, "onVideoSizeChanged: width=" + +width + ",height=" + height + ",duration=" + getDuration() + ",with=" + getVideoWidth() + ",height=" + getVideoHeight());
        }

        @Override
        public void onSurfaceSizeChanged(int width, int height) {
            Log.d(TAG, "onSurfaceSizeChanged: width=" + width + ",height=" + height + ",duration=" + getDuration() + ",with=" + getVideoWidth() + ",height=" + getVideoHeight());
        }

        @Override
        public void onRenderedFirstFrame() {
            Log.d(TAG, "onRenderedFirstFrame: duration = " + getDuration() + ", with = " + getVideoWidth() + ",height=" + getVideoHeight());
        }
    }

    // Audio Focus
    private AudioManager audioManager;
    private AudioAttributes audioAttributes;
    private boolean shouldPlayWhenReady = false;
    private final float MEDIA_VOLUME_DEFAULT = 1.0f;
    private final float MEDIA_VOLUME_DUCK = 0.2f;

    private void initAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA).build();
    }

    void setPlayWhenReady(boolean playWhenReady) {
        if (playWhenReady) {
            requestAudioFocus();
        } else {
            abandonAudioFocus();
        }
    }

    private void requestAudioFocus() {
        int result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            result = audioManager.requestAudioFocus(buildAudioFocusRequest());
        } else {
            result = audioManager.requestAudioFocus(audioFocusListener, audioAttributes.getContentType(), AudioManager.AUDIOFOCUS_GAIN);
        }

        // Call the listener whenever focus is granted - even the first time!
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            shouldPlayWhenReady = true;
            audioFocusListener.onAudioFocusChange(AudioManager.AUDIOFOCUS_GAIN);
        } else {
            Log.w(TAG, "Playback not started: Audio focus request denied");
        }
    }

    private void abandonAudioFocus() {
        if (null == mPlayer) {
            return;
        }
        mPlayer.setPlayWhenReady(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(buildAudioFocusRequest());
        } else {
            audioManager.abandonAudioFocus(audioFocusListener);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private AudioFocusRequest buildAudioFocusRequest() {
        return new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setOnAudioFocusChangeListener(audioFocusListener)
                .build();
    }

    // TODO: 2020/3/13
    private boolean isPlaying() {
        return mPlayer != null
                && mPlayer.getPlaybackState() != Player.STATE_ENDED
                && mPlayer.getPlaybackState() != Player.STATE_IDLE
                && mPlayer.getPlayWhenReady();
    }

    private AudioManager.OnAudioFocusChangeListener audioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_NONE:
                    Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_NONE");
                    break;

                case AudioManager.AUDIOFOCUS_GAIN:  // 长时间获得AudioFocus
                    Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_GAIN");
                    if (null != mPlayer) {
                        if (shouldPlayWhenReady || mPlayer.getPlayWhenReady()) {
                            mPlayer.setPlayWhenReady(true);
                            mPlayer.setVolume(MEDIA_VOLUME_DEFAULT);

                            if (!isPlaying()) {
                                performPlayBtnClick();
                            }
                        }
                        shouldPlayWhenReady = false;
                    }
                    break;

                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:    // 暂时获取焦点
                    Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_GAIN_TRANSIENT");
                    /**
                     * 申请的AudioFocus是暂时性的，会很快用完释放的
                     */
                    break;

                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:   // 应用跟其他应用共用焦点但播放的时候其他音频会降低音量
                    /**
                     * 暂时获取焦点. 正在使用AudioFocus的可以继续播放， 并降低音量
                     * Example:voice memo recording and speech recognition
                     */
                    if (mPlayer.getPlayWhenReady()) {
                        mPlayer.setVolume(MEDIA_VOLUME_DUCK);
                    }
                    Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                    break;

                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE:
                    Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE");
                    break;

                case AudioManager.AUDIOFOCUS_LOSS:  // 长时间失去了Audio Focus
                    Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_LOSS");
                    /**
                     * 停止Audio播放，并释放 Media 资源。
                     * 如果程序因为这个原因而失去AudioFocus，最好不要让它再次自动获得AudioFocus而继续播放，不然突然冒出来的声音会让用户感觉莫名其妙，感受很不好。这里直接放弃AudioFocus，当然也不用再侦听远程播放控制【如下面代码的处理】。要再次播放，除非用户再在界面上点击开始播放，才重新初始化Media，进行播放
                     */
                    abandonAudioFocus();
                    performPauseBtnClick();
                    releasePlayer();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:    // 暂时失去AudioFocus
                    Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_LOSS_TRANSIENT");
                    /**
                     * 暂时失去Audio Focus，并很快再次获得。必须停止Audio的播放，但是因为可能会很快再次获得AudioFocus，可以不释放Media资源
                     */
                    shouldPlayWhenReady = mPlayer.getPlayWhenReady();
                    mPlayer.setPlayWhenReady(false);
                    performPauseBtnClick();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    /**
                     * 暂时失去Audio Focus。 可以继续播放，降低音量
                     */
                    Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    if (mPlayer.getPlayWhenReady()) {
                        mPlayer.setVolume(MEDIA_VOLUME_DUCK);
                    }
                    break;
            }
        }
    };

    private void changeSpeed() {
        if (null == mPlayer) {
            return;
        }
        PlaybackParameters parameters = new PlaybackParameters(2f);
        mPlayer.setPlaybackParameters(parameters);
    }

    private void test() {
        mPlayer.seekTo(1000);
    }
}
