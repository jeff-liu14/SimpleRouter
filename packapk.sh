#!/bin/sh

#### 删除创建的路由文件
clean_auto_inject(){
  find . -type f -name "*.sr" | xargs rm -rf
}

#### 测试渠道
pack_debug_fun() {
    ./gradlew clean
    cleanAutoInject
    ./gradlew buildRelease
    ./gradlew assembleDebug
}

### 正式渠道
pack_release_fun() {
    ./gradlew clean
    cleanAutoInject
    ./gradlew buildRelease
    ./gradlew assembleRelease --stacktrace
}

### debug渠道
pack_install_debug_fun() {
    adb install app/build/outputs/apk/debug/Stah_V1.0_debug.apk
    adb shell am start -n  cn.laydown.stah/cn.laydown.stah.MainActivity
}

### release渠道
pack_install_release_fun() {
    adb install app/build/outputs/apk/release/Stah_V1.0_release.apk
    adb shell am start -n  cn.laydown.stah/cn.laydown.stah.MainActivity
}

#### 帮助
if [[ "$1" == "-h" ]] || [[ "$1" == "-help" ]]
then
  echo "-d,-debug : 打测试包"
  echo "-r,-release : 打正式包"
  echo "-id,-installDebug : 安装debug渠道包"
  echo "-ir,-installRelease : 安装release渠道包"
  echo "-c,-cleanAutoInject : 删除自动生成的路由文件"
  exit 0
fi

#### 打debug渠道包
if [[ "$1" == "-d" ]] || [[ "$1" == "-debug" ]]
then
  pack_debug_fun
  ### 为了测试方便直接调用安装
  pack_install_debug_fun
  exit 0
fi

#### 打release渠道包
if [[ "$1" == "-r" ]] || [[ "$1" == "-release" ]]
then
  pack_release_fun
  ### 为了测试方便直接调用安装
  pack_install_release_fun
  exit 0
fi

#### 安装debug渠道包
if [[ "$1" == "-id" ]] || [[ "$1" == "-installDebug" ]]
then
  pack_install_debug_fun
  exit 0
fi

#### 安装release渠道包
if [[ "$1" == "-ir" ]] || [[ "$1" == "-installRelease" ]]
then
  pack_install_release_fun
  exit 0
fi

#### 删除自动生成的路由文件
if [[ "$1" == "-c" ]] || [[ "$1" == "-cleanAutoInject" ]]
then
  clean_auto_inject
  exit 0
fi

echo "请加上参数-h/-help查看命令信息"