#include <jni.h>
#include <string>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include "logging.h"

// 全局变量
extern "C" {
char *ptr = nullptr;
long c_max_disk_file_size = 0;
}

void throwRunException(JNIEnv *env, const char *message) {
    jclass clazz = env->FindClass("java/lang/RuntimeException");
    env->ThrowNew(clazz, message);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_fmt_core_log_LogJNI_mMapOpen(JNIEnv *env, jobject thiz, jstring file_path,
                                      jlong max_disk_file_size) {
    const char *c_file_path = env->GetStringUTFChars(file_path, nullptr);
    int fd = open(c_file_path, O_RDWR | O_CREAT, 00777);
    if (fd == -1) {
        throwRunException(env, "open file fail");
        return;
    }
    env->ReleaseStringUTFChars(file_path, c_file_path);
    c_max_disk_file_size = (long) max_disk_file_size;
    //指定内存映射的大小
    ftruncate(fd, c_max_disk_file_size);
    ptr = static_cast<char *>(mmap(nullptr, c_max_disk_file_size, PROT_READ | PROT_WRITE,
                                   MAP_SHARED, fd,
                                   0));
    if (ptr == MAP_FAILED) {
        throwRunException(env, "mmap fail");
        return;
    }
    //关闭文件句柄
    close(fd);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_fmt_core_log_LogJNI_mMapClose(JNIEnv *env, jobject thiz) {
    if (ptr != nullptr) {
        // 释放内存映射区
        int ret = munmap(ptr, c_max_disk_file_size);
        if (ret == -1) {
            throwRunException(env, "munmap fail");
        } else {
            ptr = nullptr;
        }
    }
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_fmt_core_log_LogJNI_mMapWrite(JNIEnv *env, jobject thiz, jstring content) {
    if (ptr != nullptr) {
        const char *c_content = env->GetStringUTFChars(content, nullptr);
        // 修改映射区数据
        strcat(ptr, c_content);
        env->ReleaseStringUTFChars(content, c_content);
        size_t size = strlen(ptr);
        return size;
    }
    return 0;
}