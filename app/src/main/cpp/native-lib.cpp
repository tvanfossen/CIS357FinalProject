#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_td_warp_com_warptd_StartScreen_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
