#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

// Dev Crittercism App Id
jstring Java_com_quickenloans_referexpress_misc_Environment_getCrittercismAppIdDev( JNIEnv* env, jobject javaThis )
{
    return (env)->NewStringUTF("53e3966a83fb7944df000002");
}

// Prod Crittercism App Id
jstring Java_com_quickenloans_referexpress_misc_Environment_getCrittercismAppIdProd( JNIEnv* env, jobject javaThis )
{
    return (env)->NewStringUTF("55ce00b2985ec40d0002c587");
}

// Dev Flurry App Id
jstring Java_com_quickenloans_referexpress_misc_Environment_getFlurryAppIdDev( JNIEnv* env, jobject javaThis )
{
    return (env)->NewStringUTF("QJWMZXFF8G99PQ9FG33N");
}

// Prod Flurry App Id
jstring Java_com_quickenloans_referexpress_misc_Environment_getFlurryAppIdProd( JNIEnv* env, jobject javaThis )
{
    return (env)->NewStringUTF("72M5FKJWGX3MDPC5WHKD");
}


// Auth Key
jstring Java_com_quickenloans_referexpress_misc_Environment_getAuthKey( JNIEnv* env, jobject javaThis )
{
    return (env)->NewStringUTF("9q0m5Gt2UT0ZM70q");
}

// URL Dev
jstring Java_com_quickenloans_referexpress_misc_Environment_getPostUrlDev( JNIEnv* env, jobject javaThis )
{
    return (env)->NewStringUTF("https://servicestest.quickenloans.com/leadsubmit/Submit.aspx?factory=0");
}

// URL Prod
jstring Java_com_quickenloans_referexpress_misc_Environment_getPostUrlProd( JNIEnv* env, jobject javaThis )
{
    return (env)->NewStringUTF("https://services.quickenloans.com/leadsubmit/Submit.aspx?factory=0");
}

#ifdef __cplusplus
}
#endif
