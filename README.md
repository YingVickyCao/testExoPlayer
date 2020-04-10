# testExoPlayer

| Exoplayer Version | Support Lib         |
| ----------------- | ------------------- |
| 2.9.6             | com.android.support |
| `>` 2.9.6         | androidx            |

# 1 Get time

| Without Ads           | If not have Ads,equal. <br/>If have, about current content | -                    |
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

onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800000,with=400

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

# 11 流媒体

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
- 直播

## 劣势

- 下载实现很复杂，而且很多格式不支持下载

# TO DO

https://blog.csdn.net/zp0203/article/details/52181670

# Refs:

https://github.com/google/Exoplayer  
https://exoplayer.dev/
