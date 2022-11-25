# Logger
一款小巧的个性化日志库，支持原生 Android 控制台日志打印、磁盘写入日志并支持配置日志的输出策略、磁盘写入策略、日志拦截等功能


## 项目集成
1. Add it in your root build.gradle at the end of repositories(jitpack):

```gradle
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
   }
}
```

2. Add dependency to your project(jcenter):

```gradle
dependencies {
        implementation 'com.github.fmtjava:Logger:1.0.0'
}
```

## 基本使用

```kotlin
Logger.init(LogConfiguration.Builder()
        .logLevel(LogLevel.ALL)                                     //指定日志输出级别
        .addAdapter(AndroidLogAdapter(isLoggable = true))           // 添加控制台日志输出策略
        .addAdapter(DiskLogAdapter(DiskLogStrategy.Builder()        // 添加磁盘日志输出策略
            .diskDirPath(getExternalFilesDir(null)?.absolutePath + "/log") //指定日志输出目录，注意必须改参数必须指定，sdk内部不会创建默认日志输出目录
            .maxDiskDirSize(500, DiskSizeUnit.MB)                   // 指定日志文件夹的最大存储大小
            .maxDiskFileSize(2, DiskSizeUnit.MB)                    // 指定单个文件的最大存储大小
            .writerStrategy(WriterStrategy.BIO)                     // 写入策略，分为 BIO、NIO、Native(C mmap 方式)
            .build(), isLoggable = true))                           // 日志策略是否可用   
        .build())
```

### NIO 写入策略使用注意
NIO 写入策略使用了mmap方法写入文件，但是Java没有公开unmap方法,所以采用了反射调用，但是这个方法被标记为私有了，所以必须借助以下方案绕过私有API限制
1. Add it in your root build.gradle at the end of repositories(jitpack):

```gradle
allprojects {
    repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

2. Add dependency to your project(jcenter):

```gradle
implementation 'com.github.tiann:FreeReflection:3.1.0'
```

3. Add one line to your `Application.attachBaseContext` :

```java
@Override
protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    Reflection.unseal(base);
}
```
如需使用 mmap 写入方式可使用 Native 方式，该方式使用了 JNI 调用 C 的 mmap 方法进行写入


