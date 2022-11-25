#ifndef FIRSTJNI_LOGGING_H
#define FIRSTJNI_LOGGING_H

#include <android/log.h>

// 定义debug信息
#define LOGD(TAG, ...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
// 定义info信息
#define LOGI(TAG, ...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
// 定义warn信息
#define LOGW(TAG, ...) __android_log_print(ANDROID_LOG_WARN,TAG,__VA_ARGS__)
// 定义error信息
#define LOGE(TAG, ...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

#endif //FIRSTJNI_LOGGING_H
