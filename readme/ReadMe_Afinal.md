
## Android Afinal框架学习(一) FinalDb 数据库操作

框架地址：https://github.com/yangfuhai/afinal
对应源码：

    net.tsz.afinal.annotation.sqlite.*
    net.tsz.afinal.db.sqlite.*
    net.tsz.afinal.db.table.*
    net.tsz.afinal.utils.ClassUtils、net.tsz.afinal.utils.FieldUtils
    FinalDb

建库

    FinalDb db = FinalDb.create(context, "mytest.db", true);

有实体bean

    @Table(name = "user") //@Table 表示orm(对象关系映射)的表名
    public class User {
        private int id;
        private String name;
        private String email;
        private Date registerDate;
        private Double money;

        /////////////getter and setter 不能省略哦///////////////
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public Date getRegisterDate() {
            return registerDate;
        }
        public void setRegisterDate(Date registerDate) {
            this.registerDate = registerDate;
        }
        public Double getMoney() {
            return money;
        }
        public void setMoney(Double money) {
            this.money = money;
        }
    }

建表
    db.save(user);

    主键注解：
    必须有一个主键。默认列名为id，并自增。使用注解@Id(column="id")
    实际bean中没有id属性，使用@id(column="name") 使name成主键 ，非integer等整数类型，不会自增
    属性注解
    @Property(column=“uname") ,  将属性name映射成表中的uname字段
    取消orm的注解
    @Transient  表示不将某属性映射到表中
    一对多关系
    @OneToMany(manyColumn="parentid")
    多对一关系
    @ManyToOne(column="parentid")
    FinalDB OneToMany懒加载使用方法：

模型定义：

    public class Parent{
        private int id;
        @OneToMany(manyColumn = "parentId")
        private OneToManyLazyLoader<Parent ,Child> children;
        /*....*/
    }
    public class Child{
        private int id;
        private String text;
        @ManyToOne(column = "parentId")
        private  Parent  parent;
        /*....*/
    }

使用：

    List<Parent> all = db.findAll(Parent.class);
        for( Parent  item : all){
            if(item.getChildren ().getList().size()>0)
                Toast.makeText(this,item.getText() + item.getChildren().getList().get(0).getText(),Toast.LENGTH_LONG).show();
        }
