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
        

        