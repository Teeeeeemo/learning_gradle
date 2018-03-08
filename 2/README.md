#### 第一个Gradle项目

##### 示例介绍

* ToDo应用程序，只实现添加待办事项
* Java应用程序版
* Web版本


##### 目录结构

src
    main
        java
        resources
        webapp
    test
        java
        resources
        
        
##### 给Java应用程序打包
* 选择idea右侧gradle-Tasks-jar或build即可给源文件打成jar包
* 生成的jar包在build/libs下
* 一般第三方lib库都是这么打包的 如果存在main方法 那么jar包可以直接执行
* 执行jar包

    ```
    java -classpath build/libs/2-1.0-SNAPSHOT.jar me.khrystal.gradle.todo.App
    ```
    
##### 给Web应用程序打包

* 在`build.gradle`文件中添加`apply plugin: 'war' ` 在gradle-Tasks-war就会生成war包
* 生成的war包在build/libs下
* 将生成的war包防止到tomcat下可以直接执行

##### 意义
使用构建工具(gradle) 可以免去命令行输入复杂命令的繁琐操作
由于gradle提供了插件 可以保证在每台电脑上使用相同的代码都可以打出相同的包，出错的几率就大大降低了
