### Inject

当前最新版本号：[![](https://jitpack.io/v/cn.numeron/inject.svg)](https://jitpack.io/#cn.numeron/inject)

#### 介绍

* 一款基于`kotlin`语言开发的简单易用的依赖注入框架，拥有较强的扩展性，可以自定义注入器、拦截器。除此之外相比其它注入框架并没有什么优势，纯粹是作者的练习之作。

* `inject`默认通过构造方法来创建`Class`的实例，仅满足以下条件时可注入：
    - 为无参构造时
    - 构造方法中所有的参数均可通过`inject`获取时
    
* 提供`Retrofit`支持，可注入所有通过`Retrofit`创建的`Api`实例
* 提供`ROOM`支持，可注入所有在`RoomDatabase`中声明的`Dao`实例
* 可自定义`Producer.Factory`以支持注入更多类型的实例
* 可自定义`Interceptor`以拦截所有实例的创建过程
#### 安装方法

1. 在android工程的根目录下build.gradle文件的适当位置添加以下代码：

```groovy
buildscript {
    repositories {
        // 添加jitpack仓库
        maven { url 'https://jitpack.io' }
    }
}
```

2. 在app模块的build.gradle文件的适当位置添加以下代码：

```groovy
dependencies {
    // 核心库
    implementation 'cn.numeron:inject:latest_version'
    // ROOM支持库
    implementation 'cn.numeron:inject-room:latest_version'
    // Retrofit支持库
    implementation 'cn.numeron:inject-retrofit:latest_version'
}

```

#### 使用方法

* 初始化`inject`

```kotlin
val inject = Inject.Builder()
    .addComponent {
        // add producer factory
        // 如果使用Retrofit
        addApiProducerFactory(retrofit)
        // 如果使用ROOM
        addDaoProducerFactory(database)
    }
    .addInterceptor { chain ->
        // custom interceptor
    }
    .build()
```

* 注入实例

```kotlin
class LoginViewModel : ViewModel() {
    // 注入LoginRepository
    private val loginRepository: LoginRepository by inject
}

class LoginRepository {
    // 注入LoginApi的实例，需要在初始化时调用addApiProducerFactory方法
    private val loginApi: LoginApi by inject
    // 注入UserDao的实例需要在初始化时调用addDaoProducerFactory方法
    private val userDao: UserDao by inject
}

/** Retrofit Api接口 */
interface LoginApi {
    @POST("api/account/login")
    suspend fun login(username: String, password: String)
}

/** ROOM数据库 */
abstract class MyRoomDatabase: RoomDatabase() {
    abstract val userDao: UserDao
}
```