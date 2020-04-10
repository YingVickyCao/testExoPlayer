http://www.nickjr.tv

mgid:arc:video:nickjr.tv:

```
# mgid:arc:video:nickjr.tv:
Request URL:
https://media-utils.mtvnservices.com/services/MediaGenerator/mgid:arc:video:nickjr.tv:0db8f02c-b96b-4609-8e2e-c793973ce229?arcStage=live&accountOverride=intl.mtvi.com&billingSection=intl&ep=516d8067&format=json&acceptMethods=hls

Response:
{"package": {
  "version": "1.7.1",
  "video": {
    "item": [
      {
        "origination_date": "04-09-2020 09:22:10",
        "rendition": [
          {
            "cdn": "level3",
            "method": "hls",
            "duration": "123",
            "type": "application/x-mpegURL",
            "src": "https://dlvrsvc.mtvnservices.com/api/gen/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/,stream_1920x1080_4208739,stream_512x288_501845,stream_1280x720_2451440,stream_640x360_932367,stream_384x216_286392,stream_960x540_1715825,stream_768x432_1197550/master.m3u8?account=intl.mtvi.com&cdn=level3&tk=st=1586438470~exp=1586524870~acl=/api/gen/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/*~hmac=1fa5a5c33f548d2dcf43350c1d096ed4f5e14c775f44de19cdb3473e6fb3be62",
            "rdcount": "7"
          }
        ],
        "transcript": [
          {
            "kind": "captions",
            "srclang": "en",
            "label": "English",
            "typographic": [
              {
                "format": "cea-608",
                "src": "https://akadl-a.akamaihd.net/5290/mtvnorigin/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/ayzIOpkgKE_7162258_1874722_20180313125609964.scc"
              },
              {
                "format": "ttml",
                "src": "https://akadl-a.akamaihd.net/5290/mtvnorigin/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/ayzIOpkgKE_7162258_1874722_20180313125609964.dfxp.xml"
              },
              {
                "format": "vtt",
                "src": "https://vs-tools.mtvnservices.com/caption/convert?mgid=mgid:file:gsp:alias:/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/ayzIOpkgKE_7162258_1874722_20180313125609964.dfxp.xml&accountName=intl.mtvi.com"
              }
            ]
          }
        ]
      }
    ]
  }
}}
```

src -> `master.m3u8`: 绝对路径，多码率适配流

```
#EXTM3U
#EXT-X-VERSION:4
## Created by Viacom Delivery Service(version=2.0.5)
#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=563114,BANDWIDTH=620286,FRAME-RATE=23.976,CODECS="avc1.4D401F,mp4a.40.2",RESOLUTION=512x288
https://dlvrsvc.mtvnservices.com/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_512x288_501845.m3u8?tk=st=1586438472~exp=1586452872~acl=/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_512x288_501845.m3u8*~hmac=9702d28ce0a94fe52994a26fd12e37247d23a44861b8f614b6fdf23c7011efeb&account=intl.mtvi.com&cdn=level3
#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=4351179,BANDWIDTH=5766351,FRAME-RATE=23.976,CODECS="avc1.640028,mp4a.40.2",RESOLUTION=1920x1080
https://dlvrsvc.mtvnservices.com/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_1920x1080_4208739.m3u8?tk=st=1586438472~exp=1586452872~acl=/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_1920x1080_4208739.m3u8*~hmac=585c8533ade13619f9c9d46c2aa30c8a8902ad654b2b554f29bc2dac20c77642&account=intl.mtvi.com&cdn=level3
#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=2555590,BANDWIDTH=3589221,FRAME-RATE=23.976,CODECS="avc1.640028,mp4a.40.2",RESOLUTION=1280x720
https://dlvrsvc.mtvnservices.com/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_1280x720_2451440.m3u8?tk=st=1586438472~exp=1586452872~acl=/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_1280x720_2451440.m3u8*~hmac=45bab34026510aafa9c896018217a4aa55cdf327aca957ea1fd0ff5faa646233&account=intl.mtvi.com&cdn=level3
#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=1002875,BANDWIDTH=1271134,FRAME-RATE=23.976,CODECS="avc1.4D401F,mp4a.40.2",RESOLUTION=640x360
https://dlvrsvc.mtvnservices.com/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_640x360_932367.m3u8?tk=st=1586438472~exp=1586452872~acl=/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_640x360_932367.m3u8*~hmac=9a9e9a961d7dba4751740c41095b01961d4393eb5cfce52175c484991664724c&account=intl.mtvi.com&cdn=level3
#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=342713,BANDWIDTH=404928,FRAME-RATE=23.976,CODECS="avc1.4D401E,mp4a.40.2",RESOLUTION=384x216
https://dlvrsvc.mtvnservices.com/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_384x216_286392.m3u8?tk=st=1586438472~exp=1586452872~acl=/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_384x216_286392.m3u8*~hmac=c4e99a5c406dc65cba017c9fc005492ab2c18fae6d1f1da044a14a24e377892c&account=intl.mtvi.com&cdn=level3
#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=1803746,BANDWIDTH=2520956,FRAME-RATE=23.976,CODECS="avc1.4D401F,mp4a.40.2",RESOLUTION=960x540
https://dlvrsvc.mtvnservices.com/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_960x540_1715825.m3u8?tk=st=1586438472~exp=1586452872~acl=/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_960x540_1715825.m3u8*~hmac=209bd9cc15ea8cc3bbb7ada3fb3bb63fe2acc56637d55fe1de948f4f2df36887&account=intl.mtvi.com&cdn=level3
#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=1274477,BANDWIDTH=1814271,FRAME-RATE=23.976,CODECS="avc1.4D401F,mp4a.40.2",RESOLUTION=768x432
https://dlvrsvc.mtvnservices.com/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_768x432_1197550.m3u8?tk=st=1586438472~exp=1586452872~acl=/api/playlist/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/stream_768x432_1197550.m3u8*~hmac=1a22fb57247b673836c3a128bbaabb7327d47f78fccb2455522afd337d0127b0&account=intl.mtvi.com&cdn=level3
```

-> `stream_512x288_501845.m3u8`

```
#EXTM3U
#EXT-X-VERSION:4
#EXT-X-PLAYLIST-TYPE:VOD
#EXT-X-INDEPENDENT-SEGMENTS
#EXT-X-TARGETDURATION:6
#EXT-X-MEDIA-SEQUENCE:0
#EXT-X-KEY:METHOD=AES-128,URI="https://keysvc.mtvnservices.com:443/get/encrypt.key?acl=gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0&tk=st=1586440010~exp=1586461610~acl=/get/encrypt.key?acl=gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0*~hmac=516abbf6ec0f54e2e57b2952cc087bc91ddedbbda0792a11401046ddf409164f",IV=0x4BAC84DF7101D525DA1BEFC5E2F5567A
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_0.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_1.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_2.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_3.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_4.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_5.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_6.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_7.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_8.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_9.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_10.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_11.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_12.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_13.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_14.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_15.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_16.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_17.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_18.ts
#EXTINF:6.006000,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_19.ts
#EXTINF:2.961292,
https://vimn-l3.ts.mtvnservices.com/gsp.alias/mediabus/kids.com/2018/03/13/12/56/09/eb83f7fdcf1647b080e457fd6936d10c/1874722/0/seg_512x288_501845_20.ts
#EXT-X-ENDLIST
```
