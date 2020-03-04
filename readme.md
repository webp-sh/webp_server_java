# webp-server-java

![dev](https://github.com/webp-sh/webp_server_java/workflows/dev/badge.svg)
![release](https://github.com/webp-sh/webp_server_java/workflows/release/badge.svg)

This is a Server based on Java, which allows you to serve WebP images on the fly.

You can easily integrate into your project. 
 
It will convert `jpg,jpeg,png` files by default, this can be customized by editing the `config.json`.. 

> e.g When you visit `https://a.com/1.jpg`，it will serve as `image/webp` without changing the URL.
>
> For Safari and Opera users, the original image will be used.

## Modified from [n0vad3v/webp_server](https://github.com/n0vad3v/webp_server)
Add a simple feature that you can config more image directory

## Run as server
Create a 'config.json' file

If you are serving images at `https://example.com/pics/tsuki.jpg` and 
your files are at `/var/www/image/pics/tsuki.jpg`, then `imgMap` shall be {"/pics":"/var/www/image/pics/tsuki.jpg"}.

### 1. Download or build the binary
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
## Integrate into your project
You need newer version than 0.3  

### 1.Download the jar package and put it in your project
#### if you use gradle
you can put it into `src/main/resource/libs`, and edit config filebuild.gradle to add local dependencies

```
dependencies {
    compile fileTree(dir:'src/main/resources/libs',include:['*.jar'])
}
```
#### if you use maven
you can put it `${project.basedir}/libs`, and edit config file pom.xml to add local dependencies

```
<dependency>  
    <groupId>moe.keshane</groupId>  
    <artifactId>webp-server-java</artifactId>  
    <version>{versoin}</version>  
    <scope>system</scope>  
    <systemPath>${project.basedir}/libs/webp-server-java-{version}.jar</systemPath>  
</dependency>
```
### 2. Initializing webp-server-java
To initial a server you need to create a WebpServerConfig object.
Two params of WebpServerConfig are `Map<String,String> imgMap,List<String> allowedTypes`.
`imaMap` is a map of request uri and image file path like
```
{
    "/i": "/home/ubuntu/pic",
    "/img": "/home/ubuntu/pic2",
    "/": "/home/pic1"
}
```
`allowedTypes` is a list of allowed image extension name like
```
["jpg","png","jpeg","webp"]
```
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

## Build your own Jar package
Install jdk8 and clone the repo

then
```
./gradlew build
./gradlew bootJar
```


## Using project
[qwong/j-webp](https://github.com/qwong/j-webp)

## License
[Apache License 2.0](./LICENSE)


