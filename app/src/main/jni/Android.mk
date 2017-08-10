LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := refer_express_library
LOCAL_SRC_FILES := refer_express_library.cpp

include $(BUILD_SHARED_LIBRARY)
