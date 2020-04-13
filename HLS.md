# HLS

- HLS(HTTP LIVE Streaming) 是什么？  
  苹果公司定义的用于直播的流媒体传输协议。

- HLS 的工作原理
  把整个流分成一个个小的基于 HTTP 的文件来下载，每次只下载一些。当媒体流正在播放时，客户端可以选择从许多不同的备用源中以不同的速率下载同样的资源，允许流媒体会话适应不同的数据速率。
  在开始一个流媒体会话时，客户端会下载一个包含元数据的 extended M3U (m3u8) playlist 文件，用于寻找可用的媒体流。  
  HLS 采用 HTTP 协议。  
  HLS 只请求基本的 HTTP 报文，与实时传输协议（RTP）不同，HLS 可以穿过任何允许 HTTP 数据通过的防火墙或者代理服务器。它也很容易使用内容分发网络来传输媒体流。  
  HLS 是新一代流媒体传输协议，其基本实现原理为将一个大的媒体文件进行分片，将该分片文件资源路径记录于 m3u8 文件（即 playlist）内，其中附带一些额外描述（比如该资源的多带宽信息···）用于提供给客户端。客户端依据该 m3u8 文件即可获取对应的媒体资源，进行播放。  
  因此，客户端获取 HLS 流文件，主要就是对 m3u8 文件进行解析操作。

- m3u8 是什么？  
  m3U8 文件是 HLS 协议的部分内容。  
  m3U8 是指 UTF-8 编码格式的 M3U 文件.  
  m3u8 是 HLS 的视频纯文件索引。 M3U 文件是记录了一个索引纯文本文件，打开它时播放软件并不是播放它，而是根据它的索引找到对应的音视频文件的网络地址进行在线播放。  
  m3u8 只是视频的索引文件，因此只有 m3u8 是不完整的。还要有实体视频文件。  
  直接打开 ts 文件，可以直接播放该视频片段。
  m3u8 文件实质是一个播放列表（playlist),其可能是一个媒体播放列表（Media Playlist），或者是一个主列表（Master Playlist）.

模式 1: 本地电脑包含 m3u8 视频索引文件+ts 格式的视频切片文件。若 ts 没有加密，可直接播放

模式 2:本地只有 m3u8 文件，m3U8 文件将索引指向了远程的服务器中的 ts 文件
![m3u8_ts](https://yingvickycao.github.io/img/m3u8_ts.png)

- 那如何打开 m3u8 的文件呢？  
  能文本文档的 app

- m3u8 的好处：  
   做多码率的适配：根据网络带宽，客户端会选择一个适合自己码率的文件进行播放，保证视频流的流畅。

  分为：  
   多码率适配流  
   单码率适配流

点播:  
M3U8 分顶级 M3U8 和二级 M3U8， 顶级 M3U8 主要是做多码率适配的， 二级 M3U8 才是真正的切片文件。  
客户端默认会首先选择码率最高的请求，如果发现码率达不到，会请求郊低码率的流。  
客户端按顺序下载片段资源，依次进行播放.

直播:  
M3U8 文件里面会有属性告诉是直播，客户端会定时来请求新的 M3U8 文件。  
客户端需要定时重新请求 该 m3u8 文件，看下是否有新的片段数据需要进行下载并播放。

- m3u8 指向的视频片段只能是 ts 文件吗？  
  不是。

```
// 多码率适配流 or  单码率适配流
// 主列表（Master Playlist） -> 媒体播放列表（Media Playlist）
.m3u8 -> .m3u8 -> .ts/.mp4/.aac,但.ts 是标准
```

```
// 没有适配
// 媒体播放列表（Media Playlist）
.m3u8 -> .ts/.mp4/.aac,但.ts 是标准
```

[HTTP Live Streaming](https://developer.apple.com/documentation/http_live_streaming)  
[Adding Alternate Media to a Playlist](https://developer.apple.com/documentation/http_live_streaming/example_playlists_for_http_live_streaming/adding_alternate_media_to_a_playlist)  
[Creating a Master Playlist]（https://developer.apple.com/documentation/http_live_streaming/example_playlists_for_http_live_streaming/creating_a_master_playlist）

- m3u8 的标准定义  
  国际标准组织定义 m3u8 的 rfc doc：https://tools.ietf.org/html/draft-pantos-http-live-streaming-06

## m3u8 的结构

- `#EXT-X-TARGETDURATION`：  
  定义每个 TS 的最大的 duration
- `#EXT-X-MEDIA-SEQUENCE`：  
  定义当前 m3u8 文件中第一个文件的序列号，每个 ts 文件在 m3u8 文件中都有固定唯一的序列号，该序列号用于在 MBR 时切换码率进行对齐
- `#EXT-X-KEY`:媒体片段可以进行加密，而该标签可以指定解密方法。  
   可选。定义加密方式和 key 文件的 url，表示怎么对 media segments 进行解码，用于取得 16bytes 的 key 文件解码 ts 文件。

  ```
  #EXT-X-KEY:METHOD=AES-128,URI="https://nhkmovs-i.akamaihd.net/i/design-ah/mov/vol130.mp4/crypt.key?e=6889c9ad1087852b"
  ```

  若 m3u8 文件只有一个 EXT-X-KEY 标签，则其作用于所有媒体片段  
  多个 EXT-X-KEY 标签如果最终生成的是同样的秘钥，则他们都可作用于同一个媒体片段

  METHOD = NONE/AES-128/SAMPLE-AES  
  NONE：表示切片未进行加密  
  AES-128：表示表示使用 AES-128 进行加密。  
  SAMPLE-AES：意味着媒体片段当中包含样本媒体，比如音频或视频，它们使用 AES-128 进行加密。这种情况下 IV 属性可以出现也可以不出现。

URI：指定密钥路径。  
该密钥是一个 16 字节的数据.用于取得 16bytes 的 key 文件解码 ts 文件。  
该键是必须参数，除非 METHOD 为 NONE。

- `#EXT-X-PROGRAM-DATE-TIME`:  
  第一个文件的绝对时间. 可选。
- `#EXT-X-ALLOW-CACHE:YES`：  
   是否允许 cache.  
  在 PlayList 文件中任意地方出现，并且最多出现一次，作
  用效果是所有的媒体段
  默认 = YES。
- `#EXT-X-ENDLIST`:  
  表明 m3u8 文件的结束。  
  它可以在 PlayList 中任意位置出现，但是只能出现一个 。  
  live m3u8 没有该 tag。
- `#EXT-X-STREAM-INF:`
  BANDWIDTH :指定码率.指每秒传输的比特数，也即带宽。代表该备份流的巅峰速率  
   PROGRAM-ID :唯一 ID. 可省略  
   CODECS :指定流的编码类型。可省略  
   RESOLUTION：该属性描述备份流视屏源的最佳像素方案。 该属性为可选参数，但对于包含视屏源的备份流建议增加该属性设置。  
   AVERAGE-BANDWIDTH：该属性为备份流的平均切片传输速率。 可选  
   FRAME-RATE：描述备份流所有视屏最大帧率。 对于备份流中任意视屏源帧数超过每秒 30 帧的，应当增加该属性设置。该属性为可选参数，但对于包含视屏源的备份流建议增加该属性设置。  
   AUDIO：其值必须与定义在主播放列表某处的设置了 TYPE 属性值为 AUDIO 的 EXT-X-MEDIA 标签的 GROUP-ID 属性值相匹配。该属性为可选参数。  
   VIDEO:其值必须与定义在主播放列表某处的设置了 TYPE 属性值为 VIDEO 的 EXT-X-MEDIA 标签的 GROUP-ID 属性值相匹配。该属性为可选参数。
  SUBTITLES：其值必须与定义在主播放列表某处的设置了 TYPE 属性值为 SUBTITLES 的 EXT-X-MEDIA 标签的 GROUP-ID 属性值相匹配。该属性为可选参数。

```
#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=1269000,RESOLUTION=822x462,CODECS="avc1.66.30, mp4a.40.2"

#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=563114,BANDWIDTH=620286,FRAME-RATE=23.976,CODECS="avc1.4D401F,mp4a.40.2",RESOLUTION=512x288

#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=800000,RESOLUTION=1080x608

#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=6307144,BANDWIDTH=6453202,CODECS="avc1.64002a,mp4a.40.2",RESOLUTION=1920x1080,FRAME-RATE=60.000,CLOSED-CAPTIONS="cc1",AUDIO="aud1",SUBTITLES="sub1"
v8/prog_index.m3u8

v9/prog_index.m3u8
#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=6528521,BANDWIDTH=6674579,CODECS="avc1.64002a,ac-3",RESOLUTION=1920x1080,FRAME-RATE=60.000,CLOSED-CAPTIONS="cc1",AUDIO="aud2",SUBTITLES="sub1"

#EXT-X-STREAM-INF:AVERAGE-BANDWIDTH=6336521,BANDWIDTH=6482579,CODECS="avc1.64002a,ec-3",RESOLUTION=1920x1080,FRAME-RATE=60.000,CLOSED-CAPTIONS="cc1",AUDIO="aud3",SUBTITLES="sub1"
v8/prog_index.m3u8
```

- `#EXT-X-MEDIA`  
  用于指定相同内容的可替换的多语言翻译播放媒体列表资源。

```
#EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID="aud1",LANGUAGE="en",NAME="English",AUTOSELECT=YES,DEFAULT=YES,CHANNELS="2",URI="a1/prog_index.m3u8"
#EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID="aud2",LANGUAGE="en",NAME="English",AUTOSELECT=YES,DEFAULT=YES,CHANNELS="6",URI="a2/prog_index.m3u8"
#EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID="aud3",LANGUAGE="en",NAME="English",AUTOSELECT=YES,DEFAULT=YES,CHANNELS="6",URI="a3/prog_index.m3u8"
#EXT-X-MEDIA:TYPE=SUBTITLES,GROUP-ID="sub1",LANGUAGE="en",NAME="English",AUTOSELECT=YES,DEFAULT=YES,FORCED=NO,URI="s1/en/prog_index.m3u8"
```

TYPE:AUDIO，VIDEO，SUBTITLES，CLOSED-CAPTIONS。

URI：双引号包裹的媒体资源播放列表路径。  
如果 TYPE 属性值为 CLOSED-CAPTIONS，那么则不能提供 URI。

GROUP-ID：表示多语言翻译流所属组。

LANGUAGE：用于指定流主要使用的语言。可选。

NAME:为翻译流提供可读的描述信息

ASSOC-LANGUAGE：其内包含一个语言标签，用于提供多语言流的其中一种语言版本。

AUTOSELECT：YES/NO(默认).可选。客户端在用户没有显示进行设置时，可以选择播放该翻译流

DEFAULT:YES/NO(默认).可选。YES，那么客户端在缺乏其他可选信息时应当播放该翻译流。

FORCED：YES/NO(默认).可选。YES，那么客户端在缺乏其他可选信息时应当播放该翻译流。  
只有在设置了 TYPE 为 SUBTITLES 时，才可以设置该属性。  
YES：暗示该翻译流包含重要内容。当设置了该属性，客户端应当选择播放匹配当前播放环境最佳的翻译流。  
NO：表示该翻译流内容意图用于回复用户显示进行请求。

NSTREAM-ID："CC1", "CC2", "CC3", "CC4" 和 "SERVICEn"（n 的值为 1~63）。用于指示切片的语言（Rendition）版本。只有当 TYPE 设为 CLOSED-CAPTIONS 时，才可以设置该属性。

CHANNELS：所有音频 EXT-X-MEDIA 标签应当都设置 CHANNELS 属性。如果主播放列表包含两个相同编码但是具有不同数目 channed 的翻译流，则必须设置 CHANNELS 属性；否则，CHANNELS 属性为可选参数。

- `#EXT-X-DISCONTINUITY`  
   当遇到该 tag 的时候说明以下属性发生了变化:让播放器重新初始化
  file format  
  number and type of tracks  
  identifiers of tracks  
  编码参数 encoding parameters  
  编码序列 encoding sequence  
  时间戳序列 timestamp sequence

  应用场景：
  1）轮播不用的影片。
  2）插入广告
  https://blog.csdn.net/haima1998/article/details/46791007

EXT-X-DISCONTINUITY 的一个经典使用场景就是在视屏流中插入广告，由于视屏流与广告视屏流不是同一份资源，因此在这两种流切换时使用 EXT-X-DISCONTINUITY 进行指明，客户端看到该标签后，就会处理这种切换中断问题，让体验更佳。

```
#EXTM3U
#EXT-X-TARGETDURATION:10
#EXT-X-MEDIA-SEQUENCE:0
#EXTINF:10,
400-clipA-0.ts
#EXTINF:10,
400-clipA-1.ts
#EXTINF:5,
400-clipA-2.ts


#EXT-X-DISCONTINUITY
#EXTINF:10,
400-advert0.ts
#EXTINF:3,
400-advert1.ts


#EXT-X-DISCONTINUITY
#EXTINF:10,
400-clipB-0.ts
#EXTINF:10,
400-clipB-1.ts
#EXTINF:5,
400-clipB-2.ts
```

- `#EXTINF`  
  duration 指定每个媒体段(ts)的持续时间（秒）

```
#EXTINF:4.200000,
1b48da125fc000000.ts
```

- `#EXT-X-PLAYLIST-TYPE`  
  提供关于 PlayList 的可变性的信息，这个对整个 PlayList 文件有效.  
  可选的.

```
// 服务器不能改变 PlayList 文件
#EXT-X-PLAYLIST-TYPE:VOD

// 服务器不能改变或是删除 PlayList 文件中的任何部分，但是可以向该文件中增加新的一行内容
#EXT-X-PLAYLIST-TYPE:EVENT
```

- `#EXT-X-TARGETDURATION`
  指定最大的媒体段时间长（秒）。  
  #EXTINF 中指定的时间长度必须小于或是等于这个最大值。这个 tag 在整个 PlayList 文件中只能出现一 次。  
  在嵌套的情况下，一般有真正 播放的 url 的 m3u8 才会出现该 tag

```
#EXT-X-TARGETDURATION:6
```

- `#EXT-X-MEDIA-SEQUENCE`
  每一个 media URI 在 PlayList 中只有唯一的序号，相邻之间序号+1,  
  可选。默认为 0.

```
#EXT-X-MEDIA-SEQUENCE:0
```

- `EXT-X-VERSION`
  表示 HLS 的协议版本号，该标签与流媒体的兼容性相关。该标签为全局作用域，使能整个 m3u8 文件；  
  每个 m3u8 文件内最多只能出现一个该标签定义。  
  如果 m3u8 文件不包含该标签，则默认为协议的第一个版本。

```
#EXT-X-VERSION:7
```

- `#EXT-X-BYTERANGE`
  接下来的切片资源是其后 URI 指定的媒体片段资源的局部范围（即截取 URI 媒体资源部分内容作为下一个切片）。该标签只对其后一个 URI 起作用。  
   使用 EXT-X-BYTERANGE 标签要求兼容版本号 EXT-X-VERSION 大于等于 4。

```
  #EXTINF:9.9433,
  #EXT-X-BYTERANGE:668340@3361440

  main.ts
```

`#EXT-X-BYTERANGE:<n>[@<o>]`  
n 是一个十进制整型，表示截取片段大小（单位：字节）。  
可选参数 o 也是一个十进制整型，指示截取起始位置（以字节表示，在 URI 指定的资源开头移动该字节位置后进行截取）。  
如果 o 未指定，则截取起始位置从上一个该标签截取完成的下一个字节（即上一个 n+o+1）开始截取。  
如果没有指定该标签，则表明的切分范围为整个 URI 资源片段。

# Refs

- 下载在线 m3u8 文件到本地成为一个 mp4 https://zhuanlan.51cto.com/art/201711/558658.htm
- macOS 下载 HLS https://blog.csdn.net/DRL101/article/details/100168256
- https://www.crifan.com/crawl_nickjr_com_cartoon_related_video_subtitle_data/
- https://www.crifan.com/mac_how_download_m3u8_single_video_file/
- m3u8 文件格式 https://www.jianshu.com/p/e97f6555a070
