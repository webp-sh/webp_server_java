<p align="center">
	<img src="./pics/webp_server.png"/>
</p>

![build](https://github.com/webp-sh/webp_server_java/workflows/release/badge.svg)

This is a Server based on Java, which allows you to serve WebP images on the fly. 
It will convert `jpg,jpeg,png` files by default, this can be customized by editing the `config.json`.. 

> e.g When you visit `https://a.com/1.jpg`，it will serve as `image/webp` without changing the URL.
>
> For Safari and Opera users, the original image will be used.

## Modified from [n0vad3v/webp_server](https://github.com/n0vad3v/webp_server)
Add a simple feature that you can config more image directory

## General Usage Steps
Create a 'config.json' file

If you are serving images at `https://example.com/pics/tsuki.jpg` and 
your files are at `/var/www/image/pics/tsuki.jpg`, then `imgMap` shall be {"/pics":"/var/www/image/pics/tsuki.jpg"}.

## 1. Download or build the binary
Download the `webp-server` from [release](https://github.com/webp-sh/webp_server_java/releases/) page.

## 2. config file
Create a `config.json` 
```json
{
  "host": "127.0.0.1",
  "port": 3333,
  "imgMap": {
    "/i": "/home/ubuntu/pic",
    "/img": "/home/ubuntu/pic2",
    "/": "/home/pic1"
  },
  "allowedTypes": ["jpg","png","jpeg","webp"]
}
```
**You must provide a config file to run this server**

## 3. Run
Run this Jar package like 
```
java -jar webp-server-java.jar /path/to/your/config.json
```

### screen or tmux
Use `screen` or `tmux` to avoid being terminated. Let's take `screen` for example
```
screen -S webp
java -jar webp-server-java.jar /path/to/your/config.json
```
(Use Ctrl-A-D to detach the `screen` with `webp-server` running.)

## 4. Nginx proxy_pass
Let Nginx to `proxy_pass http://localhost:3333/;`, and your webp-server is on-the-fly
### WordPress example
```
location ^~ /wp-content/uploads/ {
        proxy_pass http://127.0.0.1:3333;
}
```
## Build your own Jar package
Install jdk8 and clone the repo

then
```
./gradlew build
./gradlew bootJar
```


## Related Articles(In chronological order)

* [让站点图片加载速度更快——引入 WebP Server 无缝转换图片为 WebP](https://nova.moe/re-introduce-webp-server/)
* [记 Golang 下遇到的一回「毫无由头」的内存更改](https://await.moe/2020/02/note-about-encountered-memory-changes-for-no-reason-in-golang/)
* [WebP Server in Rust](https://await.moe/2020/02/webp-server-in-rust/)
* [个人网站无缝切换图片到 webp](https://www.bennythink.com/flying-webp.html)


