### 多项目构建

##### 项目模块化

        在企业项目中，包层次和类关系比较复杂，把代码拆分成模块通常是最佳实践
        这需要你清晰的划分功能边界，比如把业务逻辑和数据持久化拆分开来。
        项目符合高内聚低耦合时，模块化就变得很容易，这是一条非常好的软件开发实践
        
以模块化构建TODO应用程序
项目结构
![](http://ww2.sinaimg.cn/large/0060lm7Tly1fp6ovr6ozuj315s0iswfr.jpg)

* Model模块: 放置Java Bean , 数据库的元数据 不进行任何业务的处理
* Repository模块: 存储数据 需要使用Model中的元数据进行处理
* Web模块: 人机交互界面, 业务通过界面完成 需要依赖Model与Repository

根据依赖的传递性 Web只需要依赖Repository 就可以依赖Model了 不需要直接依赖Model

配置子项目要求
1. 所有项目应用Java插件
2. web子项目打包成war
3. 所有项目添加logback日志功能
4. 统一配置公用属性 (version/group等等)

项目范围构建(注 每个项目都有一个build.gradle与之对应)

![](http://ww1.sinaimg.cn/large/0060lm7Tly1fp6p3lsdtzj31a40lu75t.jpg)


模块之间的依赖使用 compile project(':模块名称')
在右侧 Gradle projects 中可以看到整个项目的所有子模块

总结: settings.gradle 用于管理项目与子项目的
每个模块下的build.gradle 用于管理当前模块的所有构建
根目录下的build.gradle 用于管理所有子项目的通用构建部分

当执行clear/jar/war 会根据依赖关系 从
`依赖 至 被依赖`执行 

当然也可以在子项目下配置 但这样就会出现重复配置

### 自动化测试

    一些开源的测试框架 比如JUnit, TestNG能够帮助你编写可复用的结构化测试
    为了运行这些测试，你需要先编译它们 就像编译源代码一样。
    测试代码的作用仅仅用于测试的情况, 不应该被发布到生产环境中, 需要把测试代码
    和源代码分开来
    
![](http://ww3.sinaimg.cn/large/0060lm7Tly1fpa45twopdj31g00pogog.jpg)

测试配置
```$xslt
dependencies{
    testCompile 'junit:junit:4.11'
}
``` 

编写测试代码后 可以进行测试
使用测试工具后 并没有新增其他步骤与构建工具进行集成
当把测试代码放到对应目录后 测试工具会自动发现这些测试代码并且在构建之前自动执行


测试任务的具体流程

![](http://ww2.sinaimg.cn/large/0060lm7Tly1fpa4ghlqnlj31c40nyq65.jpg)

* 第一排表示对java源代码进行处理
* 第二排表示对测试代码进行处理
* 当第二排测试代码通过之后再进行第三排的校验和构建 如果校验失败会抛出异常


###### 测试工具如何发现测试代码

* 任何继承自junit.framework.TestCase或groovy.util.GroovyTestCase的类
* 任何被@RunWith注解的类
* 任何至少包含一个被@Test注解的类


### 发布

> 发布的用意是把我们编写的项目打包成jar 供他人当作第三方库(轮子)去使用

发布到本地和远程仓库

![](http://ww3.sinaimg.cn/large/0060lm7Tly1fpa4uid708j31je0jmjuf.jpg)

```$xslt
 apply plugin: 'maven-publish'

    publishing {
        publications {
            // 声明一个发布函数
            myPublish(MavenPublication) {
                // 声明 发布的文件为java源代码
                from components.java
            }
        }

        repositories {
            // 设置发布到maven仓库的名称与地址
            maven {
                name "myRepo"
                url ""
            }
        }
    }

```
执行Gradle projects -> 4 -> publishing -> publishMyPublishPublicationToMavenLocal后
jar包与pom原数据会生成到指定目录下 如当前demo指定的路径为:
`~/.m2/repository/me/khrystal/groovy/4/1.0-SNAPSHOT`