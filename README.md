# testExoPlayer

| Exoplayer Version | Support Lib         |
| ----------------- | ------------------- |
| 2.9.6             | com.android.support |
| `>` 2.9.6         | androidx            |

| exoplayer (All)          | Part     |
| ------------------------ | -------- |
| exoplayer-core           | Required |
| exoplayer-dash           | Optional |
| exoplayer-hls            | Optional |
| exoplayer-smothstreaming | Optional |
| exoplayer-ui             | Optional |

- ExoPlayer instances must be accessed from a single application thread.
- 测试  
  https://bitmovin.com/mpeg-dash-hls-examples-sample-streams/

# 1 Get time

| Without Ads           | If not have Ads,equal. <br/>If have, about current content | ms                   |
| --------------------- | ---------------------------------------------------------- | -------------------- |
| getDuration()         | getContentDuration()                                       | 获取视频总时长       |
| getCurrentPosition()  | getContentPosition()                                       | 获取视频当前播放时间 |
| getBufferedPosition() | getContentBufferedPosition()                               | 获取视频缓冲时间     |

# 2 Log

## Online video mp4, player.setPlayWhenReady(true)

```

onPlayerStateChanged: playWhenReady=true,state=STATE_IDLE,duration=-9223372036854775807,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=-9223372036854775807,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=128161,with=1920

// Pause
onPlayerStateChanged: playWhenReady=false,state=STATE_READY,duration=128161,with=1920

// Resume
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=128161,with=1920

// Seek
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=128161,with=1920
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=128161,with=1920

// Complete
onPlayerStateChanged: playWhenReady=true,state=STATE_ENDED,duration=128161,with=1920
```

## Downloaded video mp4, player.setPlayWhenReady(true)

```
onPlayerStateChanged: playWhenReady=true,state=STATE_IDLE,duration=-9223372036854775807,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=-9223372036854775807,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=214274,with=860

// Pause
onPlayerStateChanged: playWhenReady=false,state=STATE_READY,duration=214274,with=860

// Resume
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=214274,with=860

// Seek
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=214274,with=860
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=214274,with=860

// Complete
onPlayerStateChanged: playWhenReady=true,state=STATE_ENDED,duration=214274,with=860
```

## Downloaded video mp4, player.setPlayWhenReady(false);

```
onPlayerStateChanged: playWhenReady=false,state=STATE_BUFFERING,duration=-9223372036854775807,with=0
onPlayerStateChanged: playWhenReady=false,state=STATE_READY,duration=214274,with=860
```

## Online mp3, player.setPlayWhenReady(true)

```
onPlayerStateChanged: playWhenReady=true,state=STATE_IDLE,duration=-9223372036854775807,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=-9223372036854775807,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=59271,with=0

// Pause
onPlayerStateChanged: playWhenReady=false,state=STATE_READY,duration=59271,with=0

// Resume
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=59271,with=0

// Seek
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=59271,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=59271,with=0

// Complete
onPlayerStateChanged: playWhenReady=true,state=STATE_ENDED,duration=59271,with=0
```

## Downloaded mp3, player.setPlayWhenReady(true)

```
onPlayerStateChanged: playWhenReady=true,state=STATE_IDLE,duration=-9223372036854775807,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=-9223372036854775807,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=198452,with=0

// Pause
onPlayerStateChanged: playWhenReady=false,state=STATE_READY,duration=198452,with=0
// Resume
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=198452,with=0

// Seek
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=198452,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=198452,with=0

// Complete
onPlayerStateChanged: playWhenReady=true,state=STATE_ENDED,duration=198452,with=0
```

## HLS, player.setPlayWhenReady(true)

```
onPlayerStateChanged: playWhenReady=true,state=STATE_IDLE,duration=-9223372036854775807,with=0
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=-9223372036854775807,with=0
// Before STATE_READY, EndTime has arleady showed.
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800000,with=400

// Pause
onPlayerStateChanged: playWhenReady=false,state=STATE_READY,duration=1800000,with=400

// Resume
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800000,with=400

// Seek
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=1800000,with=400
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800000,with=400

// Complete
onPlayerStateChanged: playWhenReady=true,state=STATE_ENDED,duration=1800000,with=400
```

# 3 When can player.getDuration() returning true value?

When onTimelineChanged invorked.

# 4 When can get video width or height? or When video is prepared welll to play, and can set SurfaceView?

Way 1 ： onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800000,with=400

Condition : state=STATE_READY && duration > 0 && with/height > 0

```java
  public void setPlayer(@Nullable Player player) {
    Assertions.checkState(Looper.myLooper() == Looper.getMainLooper());
    Assertions.checkArgument(
        player == null || player.getApplicationLooper() == Looper.getMainLooper());
    if (this.player == player) {
      return;
    }
    @Nullable Player oldPlayer = this.player;
    if (oldPlayer != null) {
      oldPlayer.removeListener(componentListener);
      @Nullable Player.VideoComponent oldVideoComponent = oldPlayer.getVideoComponent();
      if (oldVideoComponent != null) {
        oldVideoComponent.removeVideoListener(componentListener);
        oldVideoComponent.clearVideoSurfaceView((SurfaceView) surfaceView);
      }
    }
    this.player = player;
    if (useController()) {
      controller.setPlayer(player);
    }
    if (player != null) {
      @Nullable Player.VideoComponent newVideoComponent = player.getVideoComponent();
        newVideoComponent.setVideoSurfaceView((SurfaceView) surfaceView);
        newVideoComponent.addVideoListener(componentListener);
      }
      player.addListener(componentListener);
    }
  }
```

Way 2 : onVideoSizeChanged

```
// Init
onPlayerStateChanged: playWhenReady=true,state=STATE_IDLE,duration=-9223372036854775807,with=0,height=0
onAudioFocusChange: AUDIOFOCUS_GAIN
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=-9223372036854775807,with=0,height=0
onSurfaceSizeChanged: width=1600,height=2398,duration=-9223372036854775807,with=0,height=0

// Play
onVideoSizeChanged: width=1920,height=1080,duration=1800045,with=1920,height=1080
onSurfaceSizeChanged: width=1600,height=900,duration=1800045,with=416,height=234
onRenderedFirstFrame:,duration = 1800045, with = 416,height=234
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800045,with=416,height=234

// Seek
onRenderedFirstFrame:,duration = 1800045, with = 416,height=234
onRenderedFirstFrame:,duration = 1800045, with = 416,height=234
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800045,with=416,height=234

// Back
onSurfaceSizeChanged: width=0,height=0,duration=1800045,with=416,height=234
onPlayerStateChanged: playWhenReady=false,state=STATE_BUFFERING,duration=1800045,with=416,height=234
```

- onRenderedFirstFrame  
  可以调用多次。  
  调用在 onPlayerStateChanged state=STATE_READY 之前。
- onVideoSizeChanged  
  调用 1 次

# 5 When can get audio duration ?

onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800000,with=400

Condition : state=STATE_READY && duration

# 6 MediaPlayer -> ExoPlayer, how to listen onPrepare()?

ExoPlayer release-v2 not have onPrepare().

# 7 Player state changes

```java
player.addListener(new PlayerEventListener()); // Player.EventListener

public void onPlayerStateChanged(boolean playWhenReady, @Player.State int playbackState)
```

| Value           | Desc                                                    |
| --------------- | ------------------------------------------------------- |
| STATE_IDLE      | Not have any media to play / failure                    |
| STATE_BUFFERING | load more data to be able to play from current position |
| STATE_READY     | play / pause (getPlayWhenReady() = true/false)          |
| STATE_ENDED     | finish playing media                                    |

# 8 Player errors

```java

player.addListener(new PlayerEventListener()); // Player.EventListener
// Case 1 :
class PlayerEventListener implements Player.EventListener{
    public void onPlayerError(ExoPlaybackException e){
    }
}

// Case 2 :
class PlayerEventListener implements Player.EventListener{
    void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections){
    }
}


// Case 3:
private class PlayerErrorMessageProvider implements ErrorMessageProvider<ExoPlaybackException> {
    @Override
    public Pair<Integer, String> getErrorMessage(ExoPlaybackException e) {
        // ...
    }
}
```

STATE_IDLE + mError is not null -> play failure occurs

# 9 Seeking

```java
player.seekTo(long positionMs); // For simple
/
player.seekTo(int windowIndex, long positionMs) // For With ads / simple
/
com.google.android.exoplayer2.ControlDispatcher controlDispatcher.dispatchSeekTo(player, windowIndex, positionMs) //  For With ads / simple
```

# 10 Log for debugging playback issues

By default ExoPlayer only logs errors.  
How to see detail logs:

```
// In Debug mode
player.addAnalyticsListener(new EventLogger(trackSelector));
```

# 11 Next track listener

```java
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
```

# 12 Change playback speed

```java
PlaybackParameters parameters = new PlaybackParameters(2f);
mPlayer.setPlaybackParameters(parameters);
```

# 13 Q:getCurrentPosition is bigger than getDuration?

A:  
media file has wrong media information.

# 14 TBD:[Timeline](https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Timeline.html)、[Window](https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Timeline.Window.html)、[Period](https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Timeline.Period.html)

Timeline:representation structure of media.  
e.g,compositions of media such as playlists,streams with inserted ads,live streams  
Timeline = Period + Window

# 15 MediaSource

| Type                   | Desc             |
| ---------------------- | ---------------- |
| AdsMediaSource         | Ads              |
| HlsMediaSource         | HLS              |
| SsMediaSource          | Smooth Streaming |
| DashMediaSource        | Dash             |
| ProgressiveMediaSource | Normal           |

# 流媒体

## 优势

- 体积更小
  | Value | Desc |
  | -------- | ------- |
  | hls.m3u8 | 2 kb |
  | mp4.mp4 | 33.5 mb |
  | mp3.mp3 | 3.4 mb |
- seek to 后，直接到对应位置，不用下载多余的部分
- 真正播放的内容存在服务器
- 自适应：根据网络情况，自动调整清晰度
- 大规模用户接入
- 适合直播、点播
- 内容版权保护  
  内存中维持一个较小的解码缓冲区，播放后得的媒体数据能随时清除，用户不容易截取和复制。
  可利用 DRM 等版权保护系统进行加密处理。

## 劣势

- 下载实现很复杂，而且很多格式不支持下载

## 流媒体传输协议

| 协议             | Smooth Streaming | Http Live Steaming | Dash     |
| ---------------- | ---------------- | ------------------ | -------- |
| 规范             | Microsoft        | Apple              | Adobe    |
| 协议             | HTTP             | HTTP               | HTTP     |
| 最常见媒体格式   | MP4              | TS (MTS/MPEG-TS)   | mp4/3GP  |
| 建议切片时间     | 2s               | 10s                | 灵活切片 |
| 服务器端平均时延 | 必须连续         | 可分割             | 可分割   |
| 服务器类型       | MS IIS           | HTTP               | HTTP     |
| 常见使用         |                  | Apple              | YouTube  |

- 直播协议
  | Types | 使用场景 | 使用什么协议传输 |
  | -------- | --------------------------- | ---------------- |
  | RTMP | 点播。FlashPlayer | TCP |
  | HTTP | |HTTP
  | HLS | | HTTP |
  | Dash | |-
  | RTP | 视频监控、视频会议、IP 电话 | UDP |
  | HTTP-FLV | | HTTP |

- RTP  
  Real-time Transport Protocol，传输层协议。  
  实际应用场景下经常需要 RTCP（RTP Control Protocol）配合来使用，可以简单理解为 RTCP 传输交互控制的信令，RTP 传输实际的媒体数据。
  视频监控、视频会议、IP 电话

- RTMP  
  RTMP 是 Real Time Messaging Protocol（实时消息传输协议）  
  RTMP 是 Real 是一个协议族，包括 RTMP 基本协议及 RTMPT/RTMPS/RTMPE 等多种变种。  
  属于 TCP/IP 四层模型的应用层  
  私有协议，Adobe。  
  rtmp 现在大部分国外的 CDN 已不支持，在国内流行度很高

- What is Adaptive Steaming？  
  自适应流媒体

  如何选择自适应技术？  
  平台、协议、数字版权管理

- 推流 / 拉流  
  推流，指的是把采集阶段封包好的内容传输到服务器的过程。推流端->服务器
  拉流：服务器 -> CDN -> 播放端
- Dash （MPEG-Dash）is short for `Dynamic Adaptive Steaming Over HTTP`, is an adaptive bitrate steaming.

## 专业术语

- .TS =MPEG-TS=MPEG2-TS = MTS = MPEG transport stream = transport stream  
  .TS is an mpeg-2 or h.264 stream.
- MP4 =MPEG 4 container.
- .m2ts is an MPEG2 transport stream,not h.264
- MPEG-2 标准(TS,PS)  
  TS：传输流  
  PS:节目流
- MPEG  
  是一种压缩标准。分为 MPEG layer 1、MPEG layer 2、MPEG layer 3.
- MP3
  is short for `MPEG layer 3 / MPEG audio layer 3`  
   一种有损的音频格式
- WAV  
  一种无损的音频格式
- normal mp4 vs fmp4?  
  mp4 = Non-Fragmented MP4.  
  含义是 MPEG-4 Part14，是 MPEG 标准中的 14 部分

  fmp4 = Fragmented MP4 = FFMpeg MPEG-4。
  MPEG-4 Part12。  
  是 Dash 采用的媒体文件格式。
  扩展名通常为.m4s/.mp4

- .mpd  
  Media Presentation Descriptor file

- 容器格式？  
  播放一个电影，就是至少播放一个视频流和一个音频流。  
  打包视频流、音频流、字幕、以及其他信息到一个文件，这个文件就叫容器格式。
- 字幕文件  
  格式：WebVTT,SRT,TTML,Close Caption(为听力障碍设计)
- Close Caption（CC 字幕）  
  隐藏的带有解释意味的字幕（CAPTION），为听力障碍设计  
  作用：无音状态下通过进行一些解释性的语言来描述当前画面中所发生的事情。

- Subtitle  
  对话翻译，一般的字幕。  
  为了翻译成不同的语言而设计。

- 版权保护策略  
  `Widevine`：  
  Google 在 ICS 版本上推出的一种 `DRM 数字版权管理`功能：从 Google 指定的服务器上，下载 Google 加密的版权文件，例如视频等。

  PlayReady：微软，e.g, PlayReady SL2000

  ClearKey

  Flash Access:Adobe

- Progressive?  
  渐进式。
  需要先缓冲一定数量的媒体数据才能开始播放。e.g, Mp4

- Track
  Adaptive streams normally contain multiple media tracks.  
  There are often multiple tracks that contain same content in different qualities(e.g, SD,HD, and 4K video tracks).  
  There may alse be multiple tracks of the same type containing different content(e.g, multiple audio tracks in different languages)

  总结：  
  同内容，不同分辨率；  
  同内容，同分辨率，语言不同  
  同内容，同分辨率，字幕不同  
  Audio and video

![adaptive_streams_track_text](https://yingvickycao.github.io/img/adaptive_streams_track_text.png)

![adaptive_streams_track_audio](https://yingvickycao.github.io/img/adaptive_streams_track_audio.png)

![adaptive_streams_track_video](https://yingvickycao.github.io/img/adaptive_streams_track_video.png)

- IMA
  Interactive Media Ads SDKs.  
  互动媒体广告：点击跳转；X 等

# TO DO

https://blog.csdn.net/zp0203/article/details/52181670

# Refs:

- https://github.com/google/Exoplayer
- https://exoplayer.dev/
- [Interactive Media Ads SDKs](https://developers.google.com/interactive-media-ads/)
- [Interactive Media Ads SDKs](https://developers.google.com/interactive-media-ads/docs/sdks/html5)
- [Interactive Media Ads SDKs](https://developers.google.com/interactive-media-ads/docs/sdks/html5/tags)
- [Google Cast - Use Media Tracks](https://developers.google.com/cast/docs/android_sender/media_tracks)
- [Supported media formats](https://developer.android.google.cn/guide/topics/media/media-formats#image-formats)
- [AudioManager](https://www.runoob.com/w3cnote/android-tutorial-audiomanager.html)
- [AudioFocus](https://developer.android.google.cn/guide/topics/media-apps/audio-focus#java)