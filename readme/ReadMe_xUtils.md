
## xUtils简介
* xUtils 包含了很多实用的android工具。
* xUtils 支持大文件上传，更全面的http请求协议支持(10种谓词)，拥有更加灵活的ORM，更多的事件注解支持且不受混淆影响...
* xUitls 最低兼容android 2.2 (api level 8)

## 目前xUtils主要有四大模块：

* DbUtils模块：
  > * android中的orm框架，一行代码就可以进行增删改查；
  > * 支持事务，默认关闭；
  > * 可通过注解自定义表名，列名，外键，唯一性约束，NOT NULL约束，CHECK约束等（需要混淆的时候请注解表名和列名）；
  > * 支持绑定外键，保存实体时外键关联实体自动保存或更新；
  > * 自动加载外键关联实体，支持延时加载；
  > * 支持链式表达查询，更直观的查询语义，参考下面的介绍或sample中的例子。

* ViewUtils模块：
  > * android中的ioc框架，完全注解方式就可以进行UI，资源和事件绑定；
  > * 新的事件绑定方式，使用混淆工具混淆后仍可正常工作；
  > * 目前支持常用的20种事件绑定，参见ViewCommonEventListener类和包com.lidroid.xutils.view.annotation.event。

* HttpUtils模块：
  > * 支持同步，异步方式的请求；
  > * 支持大文件上传，上传大文件不会oom；
  > * 支持GET，POST，PUT，MOVE，COPY，DELETE，HEAD，OPTIONS，TRACE，CONNECT请求；
  > * 下载支持301/302重定向，支持设置是否根据Content-Disposition重命名下载的文件；
  > * 返回文本内容的请求(默认只启用了GET请求)支持缓存，可设置默认过期时间和针对当前请求的过期时间。

* BitmapUtils模块：
  > * 加载bitmap的时候无需考虑bitmap加载过程中出现的oom和android容器快速滑动时候出现的图片错位等现象；
  > * 支持加载网络图片和本地图片；
  > * 内存管理使用lru算法，更好的管理bitmap内存；
  > * 可配置线程加载线程数量，缓存大小，缓存路径，加载显示动画等...


----
## 使用xUtils快速开发框架需要有以下权限：

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

----
## 混淆时注意事项：

 * 添加Android默认混淆配置${sdk.dir}/tools/proguard/proguard-android.txt
 * 不要混淆xUtils中的注解类型，添加混淆配置：-keep class * extends java.lang.annotation.Annotation { *; }
 * 对使用DbUtils模块持久化的实体类不要混淆，或者注解所有表和列名称@Table(name="xxx")，@Id(column="xxx")，@Column(column="xxx"),@Foreign(column="xxx",foreign="xxx")；

----
## DbUtils使用方法：

* 创建数据库

    DaoConfig config = new DaoConfig(context);
    config.setDbName("xUtils-demo"); //db名
    config.setDbVersion(1);  //db版本
    DbUtils db = DbUtils.create(config);//db还有其他的一些构造方法，比如含有更新表版本的监听器的

* 创建表

  db.createTableIfNotExist(User.class); //创建一个表User
  db.save(user);//在表中保存一个user对象。最初执行保存动作时，也会创建User表


* 删除表

  db.dropTable(User.class);

* 开启事务

  db.configAllowTransaction(true);

* db相关Annotation

    @Check    check约束
    @Column   列名
    @Finder   一对多、多对一、多对多关系(见sample的Parent、Child中的使用)
    @Foreign  外键
    @Id       主键，当为int类型时，默认自增。 非自增时，需要设置id的值
    @NoAutoIncrement  不自增
    @NotNull  不为空
    @Table    表名
    @Transient  不写入数据库表结构
    @Unique   唯一约束

    DbUtils db = DbUtils.create(this);
    User user = new User(); //这里需要注意的是User对象必须有id属性，或者有通过@ID注解的属性
    user.setEmail("wyouflf@qq.com");
    user.setName("wyouflf");
    db.save(user); // 使用saveBindingId保存实体时会为实体的id赋值

    ...
    //查找
    Parent entity = db.findById(Parent.class, parent.getId());
    List<Parent> list = db.findAll(Parent.class);//通过类型查找

    Parent Parent = db.findFirst(Selector.from(Parent.class).where("name","=","test"));

    // IS NULL
    Parent Parent = db.findFirst(Selector.from(Parent.class).where("name","=", null));
    // IS NOT NULL
    Parent Parent = db.findFirst(Selector.from(Parent.class).where("name","!=", null));

    // WHERE id<54 AND (age>20 OR age<30) ORDER BY id LIMIT pageSize OFFSET pageOffset
    List<Parent> list = db.findAll(Selector.from(Parent.class)
                                       .where("id" ,"<", 54)
                                       .and(WhereBuilder.b("age", ">", 20).or("age", " < ", 30))
                                       .orderBy("id")
                                       .limit(pageSize)
                                       .offset(pageSize * pageIndex));

    // op为"in"时，最后一个参数必须是数组或Iterable的实现类(例如List等)
    Parent test = db.findFirst(Selector.from(Parent.class).where("id", "in", new int[]{1, 2, 3}));
    // op为"between"时，最后一个参数必须是数组或Iterable的实现类(例如List等)
    Parent test = db.findFirst(Selector.from(Parent.class).where("id", "between", new String[]{"1", "5"}));

    DbModel dbModel = db.findDbModelAll(Selector.from(Parent.class).select("name"));//select("name")只取出name列
    List<DbModel> dbModels = db.findDbModelAll(Selector.from(Parent.class).groupBy("name").select("name", "count(name)"));
    ...

    List<DbModel> dbModels = db.findDbModelAll(sql); // 自定义sql查询
    db.execNonQuery(sql) // 执行自定义sql
    ...

----
## ViewUtils使用方法
* 完全注解方式就可以进行UI绑定和事件绑定。
* 无需findViewById和setClickListener等。

    // xUtils的view注解要求必须提供id，以使代码混淆不受影响。
    @ViewInject(R.id.textView)
    TextView textView;

    //@ViewInject(vale=R.id.textView, parentId=R.id.parentView)
    //TextView textView;

    @ResInject(id = R.string.label, type = ResType.String)
    private String label;

    // 取消了之前使用方法名绑定事件的方式，使用id绑定不受混淆影响
    // 支持绑定多个id @OnClick({R.id.id1, R.id.id2, R.id.id3})
    // or @OnClick(value={R.id.id1, R.id.id2, R.id.id3}, parentId={R.id.pid1, R.id.pid2, R.id.pid3})
    // 更多事件支持参见ViewCommonEventListener类和包com.lidroid.xutils.view.annotation.event。

    @OnClick(R.id.test_button)
    public void testButtonClick(View v) { // 方法签名必须和接口中的要求一致
        ...
    }
    ...
    //在Activity中注入：
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ViewUtils.inject(this); //注入view和事件
        ...
        textView.setText("some text...");
        ...
    }
    //在Fragment中注入：
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bitmap_fragment, container, false); // 加载fragment布局
        ViewUtils.inject(this, view); //注入view和事件
        ...
    }
    //在PreferenceFragment中注入：
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewUtils.inject(this, getPreferenceScreen()); //注入view和事件
        ...
    }
    // 其他重载
    // inject(View view);
    // inject(Activity activity)
    // inject(PreferenceActivity preferenceActivity)
    // inject(Object handler, View view)
    // inject(Object handler, Activity activity)
    // inject(Object handler, PreferenceGroup preferenceGroup)
    // inject(Object handler, PreferenceActivity preferenceActivity)

----
## HttpUtils使用方法：
### 普通get方法

    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.GET,
    "http://www.lidroid.com",
    new RequestCallBack<String>(){
        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            testTextView.setText(current + "/" + total);
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            textView.setText(responseInfo.result);
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onFailure(HttpException error, String msg) {
        }
    });

----
### 使用HttpUtils上传文件 或者 提交数据 到服务器（post方法）

    RequestParams params = new RequestParams();
    params.addHeader("name", "value");
    params.addQueryStringParameter("name", "value");

    // 只包含字符串参数时默认使用BodyParamsEntity，
    // 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
    params.addBodyParameter("name", "value");

    // 加入文件参数后默认使用MultipartEntity（"multipart/form-data"），
    // 如需"multipart/related"，xUtils中提供的MultipartEntity支持设置subType为"related"。
    // 使用params.setBodyEntity(httpEntity)可设置更多类型的HttpEntity（如：
    // MultipartEntity,BodyParamsEntity,FileUploadEntity,InputStreamUploadEntity,StringEntity）。
    // 例如发送json参数：params.setBodyEntity(new StringEntity(jsonStr,charset));
    params.addBodyParameter("file", new File("path"));
    ...

    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST,
        "uploadUrl....",
        params,
        new RequestCallBack<String>() {

        @Override
        public void onStart() {
            testTextView.setText("conn...");
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            if (isUploading) {
                testTextView.setText("upload: " + current + "/" + total);
            } else {
                testTextView.setText("reply: " + current + "/" + total);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            testTextView.setText("reply: " + responseInfo.result);
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            testTextView.setText(error.getExceptionCode() + ":" + msg);
        }
    });

----
### 使用HttpUtils下载文件：
* 支持断点续传，随时停止下载任务，开始任务

    HttpUtils http = new HttpUtils();
    HttpHandler handler = http.download("http://apache.dataguru.cn/httpcomponents/httpclient/source/httpcomponents-client-4.2.5-src.zip",
        "/sdcard/httpcomponents-client-4.2.5-src.zip",
        true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
        true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
        new RequestCallBack<File>() {

            @Override
            public void onStart() {
                testTextView.setText("conn...");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                testTextView.setText(current + "/" + total);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                testTextView.setText("downloaded:" + responseInfo.result.getPath());
            }


            @Override
            public void onFailure(HttpException error, String msg) {
                testTextView.setText(msg);
            }
        });

    //调用cancel()方法停止下载
    handler.cancel();

----
## BitmapUtils 使用方法

    BitmapUtils bitmapUtils = new BitmapUtils(this);

    // 加载网络图片
    bitmapUtils.display(testImageView, "http://bbs.lidroid.com/static/image/common/logo.png");

    // 加载本地图片(路径以/开头， 绝对路径)
    bitmapUtils.display(testImageView, "/sdcard/test.jpg");

    // 加载assets中的图片(路径以assets开头)
    bitmapUtils.display(testImageView, "assets/img/wallpaper.jpg");

    // 使用ListView等容器展示图片时可通过PauseOnScrollListener控制滑动和快速滑动过程中时候暂停加载图片
    listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));
    listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true, customListener));

----
## 其他（***更多示例代码见sample文件夹中的代码***）
### 输出日志 LogUtils

    // 自动添加TAG，格式： className.methodName(L:lineNumber)
    // 可设置全局的LogUtils.allowD = false，LogUtils.allowI = false...，控制是否输出log。
    // 自定义log输出LogUtils.customLogger = new xxxLogger();
    LogUtils.d("wyouflf");

----
# 关于作者
* Email： <wyouflf@qq.com>, <wyouflf@gmail.com>
* 有任何建议或者使用中遇到问题都可以给我发邮件, 你也可以加入QQ群：330445659(已满), 275967695, 257323060，技术交流，idea分享 *_*