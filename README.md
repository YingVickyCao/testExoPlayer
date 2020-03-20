# testExoPlayer

https://github.com/google/Exoplayer  
https://exoplayer.dev/

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
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800000,with=400

// Resume
onPlayerStateChanged: playWhenReady=false,state=STATE_READY,duration=1800000,with=400

// Seek
onPlayerStateChanged: playWhenReady=true,state=STATE_BUFFERING,duration=1800000,with=400
onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800000,with=400

// Complete
onPlayerStateChanged: playWhenReady=true,state=STATE_ENDED,duration=1800000,with=400
```

# 3 When can player.getDuration() returning true value?

When onTimelineChanged invorked.

# 4 When can get video width and height?

onPlayerStateChanged: playWhenReady=true,state=STATE_READY,duration=1800000,with=400

# 5 MediaPlayer -> ExoPlayer, how to listen onPrepare()?

ExoPlayer release-v2 not have onPrepare().

# TO DO

https://blog.csdn.net/zp0203/article/details/52181670
