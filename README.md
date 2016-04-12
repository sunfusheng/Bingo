
### Bingo介绍

* Bingo是一款IT阅读学习类的开源软件，在不久的将来会拥有丰富的学习内容，推荐您也来用用来看看，在这里您也可以发布好的干货链接文章。

* 该APP是通过新浪微博登录的，为防止大家clone下来后可能无法编译或不能正常使用微博登录，现将证书上传，如果你还是不能编译，请与我联系。

### 该项目使用动态代理AOP编程框架，使开发起来更简洁、更高效

该动态代理框架的核心是通过dexmaker和Spring的拦截器实现AOP编程；dexmaker是运行在Android DVM上，
利用Java编写，来动态生成DEX字节码的API。如果了解Spring AOP编程的话，应该听说过cglib or ASM，
但这两个工具生成都是Java字节码，而DVM加载的必须是DEX字节码。所以，想要在Android上进行AOP编程，
Google 的dexmaker可以说是一个非常好的选择。

辅助的还有注解和反射，使用注解来标注同步、异步、加载框和加载显示的文字；反射回调继承以下Base类子类的方法：

    BaseAsyncActivity
    BaseAsyncFragment
    BaseAsyncListAdapter
    BaseAsyncObject

同时着重使用系统的Handler并封装为MessageProxy进行消息的分发与处理。

封装映射Map为ModelMap，方便回调时的数据传递。

### [APP下载地址](https://fir.im/Bingo)

	欢迎您的加入，共同收集好的技术文章，一起学习！共同进步！

### [GitHub开源地址](https://github.com/sfsheng0322/Bingo)

	欢迎各路大神Star、Fork、Pull requests，您的关注是我不断进步的动力。

### ScreenShots

![](/screenshots/icon_bingo_1.png) (图一)
![](/screenshots/icon_bingo_2.png) (图二)

![](/screenshots/icon_bingo_3.png) (图三)
![](/screenshots/icon_bingo_4.png) (图四)

### 用到的开源库，感谢

* [OkHttp3](https://github.com/square/okhttp)
* [Glide](https://github.com/bumptech/glide)
* [esperandro](https://github.com/dkunzler/esperandro)
* [SystemBarTint](https://github.com/jgilfelt/SystemBarTint)
* [Material Dialog](https://github.com/afollestad/material-dialogs)
* [logger](https://github.com/orhanobut/logger)
* [PhotoView](https://github.com/chrisbanes/PhotoView)
* [EventBus](https://github.com/greenrobot/EventBus)


#### Version 1.4

* 去掉短信注册登录功能
* 增加新浪微博账号一键快速登录
* 增加新浪微博SDK分享功能
* 增加剪切版粘贴去添加新Bingo的功能

#### Version 1.3

* 新增上拉加载更多功能
* 重构代码使用MVP框架
* 修改加载个别web页面崩溃问题
* 增加关于软件和关于作者界面
* 使用EventBus
* 增加删除功能

#### Version 1.2

* 使用PhotoView增加图片查看功能
* 修改了几个Bug
* 使用正式版的证书

#### Version 1.1

* 用户登录后可以收藏自己的技术链接文章
* 使用WebView打开链接文章
* 使用fir.im平台增加在线升级功能
* 增加主题设置功能和分享功能

#### Version 1.0

* 使用Bmob后端云服务
* 采用Android MD设计风格
* 目标只有一个收集好的技术文章
* 大量使用Github开源库

### 关于我

个人邮箱：sfsheng0322@126.com

[GitHub主页](https://github.com/sfsheng0322)

[简书主页](http://www.jianshu.com/users/88509e7e2ed1/latest_articles)

[个人博客](http://sunfusheng.com/)

[新浪微博](http://weibo.com/u/3852192525)




