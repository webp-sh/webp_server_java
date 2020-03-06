# webp-server-java


![dev](https://github.com/webp-sh/webp_server_java/workflows/dev/badge.svg?branch=dev)
![release](https://github.com/webp-sh/webp_server_java/workflows/release/badge.svg?branch=master)
![license](https://img.shields.io/github/license/webp-sh/webp_server_java)
[![Maven Central](https://img.shields.io/maven-central/v/moe.keshane/webp-server-java.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22moe.keshane%22%20AND%20a:%22webp-server-java%22)

This is a Server based on Java, which allows you to serve WebP images on the fly.

And you can easily integrate into your project. 
 
> e.g When you visit `https://a.com/1.jpg`，it will serve as `image/webp` without changing the URL.
>
> For Safari and Opera users, the original image will be used.

## Modified from [n0vad3v/webp_server](https://github.com/n0vad3v/webp_server)
* Add a simple feature that you can config more image directory

* And you can easily integrate into your Java project. 

## Integrate into your project
You need newer version than 0.3  

### 1.Import this package by gradle or maven

#### if you use gradle
add to `build.gralde`
```
implementation 'moe.keshane:webp-server-java:0.3.1'
```
#### if you use maven
add to `pom.xml`
```
<dependency>
  <groupId>moe.keshane</groupId>
  <artifactId>webp-server-java</artifactId>
  <version>0.3.1</version>
</dependency>
```
### 2. Initializing webp-server-java
To initial a server you need to create a WebpServerConfig object.

Two params of WebpServerConfig are `Map<String,String> imgMap,List<String> allowedTypes`.

`imaMap` is a map of request uri and image file path like
```
Map<String,String> imgMap = new HashMap<>();
map.put("/i","/home/ubuntu/pic");
map.put("/img","/home/ubuntu/pic2");
map.put("/","/home/ubuntu/pic1");
```
`allowedTypes` is a list of allowed image extension name like
```
List<String> allowedTypes = Arrays.asList("jpg","png","jpeg","webp");
```
and initial the server
```
WebpServerConfig webpConfig = new WebpServerConfig(imgMap,allowedTypes);
WebpServer server = WebpServer.init(webpConfig);
``` 

### 3. Get webp file or origin file
Server would return a webp image file if is not safari or origin image file if is safari.

The param is `HttpServletRequest` object.
``` 
File file = server.request(request);
```

## Run as server
Create a 'config.json' file

If you are serving images at `https://example.com/pics/tsuki.jpg` and 
your files are at `/var/www/image/pics/tsuki.jpg`, then `imgMap` shall be `{"/pics":"/var/www/image/pics/tsuki.jpg"}`.

### 1. Download or build the jar package
Download the `webp-server` from [release](https://github.com/webp-sh/webp_server_java/releases/) page.

### 2. config file
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

### 3. Run
Run this Jar package like 
```
java -jar webp-server-java.jar /path/to/your/config.json
```

#### screen or tmux
Use `screen` or `tmux` to avoid being terminated. Let's take `screen` for example
```
screen -S webp
java -jar webp-server-java.jar /path/to/your/config.json
```
(Use Ctrl-A-D to detach the `screen` with `webp-server` running.)

### 4. Nginx proxy_pass
Let Nginx to `proxy_pass http://localhost:3333/;`, and your webp-server is on-the-fly
#### WordPress example
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


## Using project
* [qwong/j-webp](https://github.com/qwong/j-webp)

* [biezhi/webp-io](https://github.com/biezhi/webp-io)

## License
[Apache License 2.0](./LICENSE)


