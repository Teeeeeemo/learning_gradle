#### 构建脚本概要

##### 构建块
    
    Gradle构建中的两个基本概念是项目(project)和任务(task)
    每个构建至少包含一个项目，项目中包含一个或多个任务。在多项目
    构建中，一个项目可以依赖于其他项目；类似的，任务可以形成一个依赖关系图
    来确保他们的执行顺序
    
##### 项目(project)
    
    一个项目代表一个正在构建的组件(比如一个jar文件)
    当构建启动后， Gradle会基于build.gradle实例化一个
    org.gradle.api.Project类，并且能够通过project使其隐式可用 即不需要在build.gradle中显式的写project.xxx
    
    项目的主要属性
    
        group, name, version
        group下的name不允许重名 通过版本号确定构建历史
        name对应ArtifactId
        
    项目的主要方法
    
        apply, dependencies, repositories, task
        apply: 应用一个插件
        dependencies: 声明依赖于哪些jar包或aar，war
        repositories: 仓库 去哪个仓库下找对应的dependencies的jar包
        task: 声明项目中的任务 需要自己实现
    
    属性的其他配置方式
        ext, gradle.properties
        ext: external 扩展 可以在这里定义属性 在项目中调用
        gradle.properties 使用键值对声明属性
        
    任务(task)
        
        任务对应org.gradle.api.Task.主要包括任务动作和任务依赖
        任务动作定义了一个最小的工作单元。可以定义依赖于其他任务,动作序列和执行条件
        即在任务中可以指定任务的执行顺序, 满足条件后再执行
        
        任务中的主要方法
            dependsOn 声明任务依赖
            doFirst 在动作列表前端添加一个动作
            doLast(简写方式为<<) 在动作列表末尾添加一个动作
            注: 在一个任务中可以执行多次doFirst 和 doLast
            
        在项目中 任务是非必须的 项目中不一定有任务
        在大多数情况下我们不需要自己编写任务 只需要应用插件提供的任务即可
        当没有插件给我们提供需要的功能时 我们可以自定义任务
            
##### 例
    
    在gradle-tasks中执行build->jar
    gradle命令行输出
    Run tasks
        :compileJava UP-TO-DATE
        :processResources UP-TO-DATE
        :classes UP-TO-DATE
        :jar
        
        说明 jar任务依赖于compileJava，processResources，classes, UP-TO-DATE表明在上一次构建后输入输出没有发生改变, 因此跳过
        
        
##### 自定义任务
    
在build.gradle下编写
```
// 声明一个闭包 这个闭包的作用是创建目录
def createDir = {
    path ->
        File dir = new File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
}
// 自定义task
task makeJavaDir() {
    def paths = ['src/main/java', 'src/main/resources', 'src/test/java', 'src/test/resources'];
    // 添加动作
    doFirst {
        // 遍历path 执行闭包创建目录操作
        paths.forEach(createDir)
    }

}
// 自定义task 创建Web工程目录 因为Web工程目录包含Java工程目录 因此可以进行task依赖
// Web工程只需要创建特有的工程目录就可以了
task makeWebDir() {
    // 声明依赖任务 注意 必须在函数中执行 动作执行阶段不允许依赖任务！
    dependsOn 'makeJavaDir'
    def paths = ['src/main/webapp', 'src/test/webapp']
    doLast {
        paths.forEach(createDir)
    }

}
```

执行task在IDEA右侧Gradle->Tasks->other->makeJavaDir 点击执行后可以看到目录创建了


##### 构建的生命周期

1. 初始化阶段: Gradle根据build.gradle构建脚本进行项目初始化, 在多项目构建中(多module), 会初始化所有
将要构建的项目

2. 配置阶段: 生成task的依赖顺序和执行顺序 就是初始化任务 生成task的依赖关系及执行图

    如: 
    ```
        task loadVersion{
            project.version = '1.0'
        }
    ```

3. 执行阶段: 执行task的动作代码 执行之后 整个项目的构建就完成了
    
    如:
    ```
        task loadVersion << {
            print 'success'
        } 

    ```

4. 生命周期的Hook方法 (一般不会用到)

    初始化阶段(Initialization phase)
    
    Hook 
        Before project evaluation(预估)
        
            gradle.beforeProject {
                project ->
                    ...
            }
        
    配置阶段(Configuration phase)
    
    Hook
        Task graph populated(居住于)
        
            gradle.taskGraph.whenReady {
                graph ->
                    ...
            }
        
    动作执行阶段(Execution phase)
    
    Hook
        Build Finished
        
            gradle.buildFinished {
                result ->
                    ...
            }
            
##### 依赖管理

    概述：几乎所有的基于JVM的软件项目都需要依赖外部类库来
    重用现有的功能。也就不需要重复的造轮子，自动化的依赖管理
    可以明确依赖版本，可以解决因传递性依赖带来的版本冲突。
    自动化依赖管理 可以帮我们明确标明依赖的版本 同时解决版本冲突
    
* 工件坐标
    
    工件: jar包
    
    坐标: group, name, version 通过这三个属性 可以明确规范一个唯一的jar包
    
* 仓库
    
    用来存放jar包的地方
    
    常用仓库有 mavenLocal/mavenCentral/jcenter/jitpack, 自定义maven仓库, 文件仓库
        
        mavenCentral与jcenter,jitpack 为公网上的公共仓库
        我们可以在上面上传jar包或依赖jar包
        
        
        mavenLocal 为本机使用过的jar包, 存放的本地仓库
        使用mavenLocal 的话 就不会从远程仓库加载 直接从本地加载
        
        自定义maven仓库: maven私服, 公司内部为了代码安全而使用的内部仓库
        
        文件仓库: 本机jar包的文件路径 不建议使用 因为协作开放时 路径不可使用 违反了一致性 构建工具不能到处使用
             
![](http://ww1.sinaimg.cn/large/0060lm7Tly1fp6jcvdc5nj31920m8770.jpg)

* 依赖配置
    
    源代码配置 src/main
        
        compile(编译阶段)
        
        runtime(运行时阶段)
        
    测试代码配置 src/compile
        
        testCompile(编译阶段)
        
        testRuntime(运行时阶段)
        
* 依赖配置关系
    
    可以在右侧Gradle SourceSet 查看依赖配置和配置阶段

![](http://ww3.sinaimg.cn/large/0060lm7Tly1fp6jozea28j31360kemys.jpg)

    运行时阶段都是继承于编译阶段的
    测试代码依赖配置都是继承于源代码依赖配置的
    在运行时阶段(runtime testRuntime)依赖的 在编译阶段(compile testCompile)不会依赖
    如 testCompile 依赖的jar包 在发版后并不会体现在线上版本的代码中
    
    gradle jar包规则 groupid:name:version
    
* 练习 

    在[http://search.maven.org/](http://search.maven.org/)
    上找到logback-classic库 添加到项目依赖中
    查看Gradle Project->SourceSet 观察main下的状态
    同时把App类中的`System.out.println`改为`log.info`
    
    ![](http://ww1.sinaimg.cn/large/0060lm7Tly1fp6mfezpd6j30l6078myl.jpg)
    
    可以看到 logback-classic还依赖于logback-core,slf4j-api,java-mail
    java.mail还依赖于activation 这就是传递性依赖 由于多个传递性依赖之间有时会导致版本冲突
    如依赖了另一个jar包 jar包中传递性依赖java-mail 且版本不同 就会造成依赖的版本冲突
    因此我们需要构建工具去解决版本冲突
    
##### 解决版本冲突

> 版本冲突示例

![](http://ww3.sinaimg.cn/large/0060lm7Tly1fp6mstv5f7j30yc0omgnp.jpg)
    
* 解决版本冲突步骤

    1. 查看依赖报告 确定哪些jar包造成了版本冲突 
        命令行执行 `gradle dependencies`
        或Gradle projects -> Tasks -> help -> dependencies
        注意 第二种方式Mac上不能显示 需要添加
        `classpath "io.spring.gradle:dependency-management-plugin:0.6.1.RELEASE"`
        参考 [https://hacpai.com/article/1487849072882](https://hacpai.com/article/1487849072882)
          
    2. 排除传递性依赖或强制指定一个版本
    
        * gradle默认会使用一个最高版本的jar包
        
        * 修改默认策略 我们不使用默认策略 配置下方代码 当出现版本冲突时让项目构建失败
        能够知道那些依赖出现了版本冲突
        
                configurations.all{
                    resolutionStrategy{
                        failOnVersionConflict()
                    }
                }
        
        *  排除传递性依赖有三种方式
            1. 使用transitive = false 排除所有传递性依赖(不推荐使用)
            2. 排除低版本依赖
             
                compile('org.hibernate:hibernate-core:3.6.3.Final') {
                    exclude group:"org.slf4j", module:"slf4j-api"
                    // 注: 这里的module就是jar包的`name`属性
                }
                
            3. 强制指定一个版本
                
                configurations.all{
                    resolutionStrategy{
                        force 'org.slf4j:slf4j-api:1.7.24'
                    }
                }    
    
    
        