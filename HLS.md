# HLS

- HLS(HTTP LIVE Streaming) 是什么？  
  苹果公司定义的用于直播的流媒体传输协议。

- m3u8 是什么？  
  m3u8 是 HLS 的视频文件索引。  
  m3u8 只是视频的索引文件，因此只有 m3u8 是不完整的。还要有实体视频文件。  
  直接打开 ts 文件，可以直接播放该视频片段。

  模式 1: 本地电脑包含 m3u8 视频索引文件+ts 格式的视频切片文件  
  模式 2:本地只有 m3u8 文件，m3U8 文件将索引指向了远程的服务器中的 ts 文件
  ![m3u8_ts](https://yingvickycao.github.io/img/m3u8_ts.png)
- m3u8

- 那如何打开 m3u8 的文件呢？  
  能文本文档的 app

- m3u8 的好处：  
  做多码率的适配：根据网络带宽，客户端会选择一个适合自己码率的文件进行播放，保证视频流的流畅。

  分为：  
  多码率适配流  
  单码率适配流

- m3u8 的标准定义  
  国际标准组织定义 m3u8 的 rfc doc：https://tools.ietf.org/html/draft-pantos-http-live-streaming-06

## m3u8 的结构

- `#EXT-X-TARGETDURATION`：  
  定义每个 TS 的最大的 duration
- `#EXT-X-MEDIA-SEQUENCE`：  
  定义当前 m3u8 文件中第一个文件的序列号，每个 ts 文件在 m3u8 文件中都有固定唯一的序列号，该序列号用于在 MBR 时切换码率进行对齐
- `#EXT-X-KEY:METHOD=AES-128,URI="https://nhkmovs-i.akamaihd.net/i/design-ah/mov/vol130.mp4/crypt.key?e=6889c9ad1087852b"`：  
   可选。定义加密方式和 key 文件的 url，用于取得 16bytes 的 key 文件解码 ts 文件。
- `#EXT-X-PROGRAM-DATE-TIME`:  
  第一个文件的绝对时间. 可选。

# Refs

- 下载在线 m3u8 文件到本地成为一个 mp4 https://zhuanlan.51cto.com/art/201711/558658.htm
- macOS 下载 HLS https://blog.csdn.net/DRL101/article/details/100168256
- https://www.crifan.com/crawl_nickjr_com_cartoon_related_video_subtitle_data/
- https://www.crifan.com/mac_how_download_m3u8_single_video_file/
