#包结构#
    ├─constant        一些常量和静态方法
    ├─dao            dao层的接口（Spring的@Repository层）ModelDao命名
    │  └─impl		   DAO实现层ModelDaoImpl命名
    ├─model          业务Model类的定义（Model命名首字母大写）
    ├─service          Service层的接口(Spring的@Service层) ModelService命名
    │  ├─impl        Service层的实现ModelServiceImple
    │  └─interceptor   Service的Filter(Spring的AOP)operationInterceptor命名
    ├─struts           控制层的接口(Spring的@Controller) 
    │  ├─Action      控制层是实现及action实现operationAction命名
    │  └─interceptor  控制层的Filter（Struts2的拦截器）operationInterceptor命名
    └─util             工具类
#业务实体Model#
所谓的Model也就是业务实体类，命名规范以简洁明了英文命名方式，首字大写其余小写，每个单词之间链接处以第二个单词大写为表示(这要做，而不使用驼峰命名是因为这样可以快速的生成代码)。
例如：
public class Role {
	//角色ID
	@Getter @Setter private Integer RoleId;
	//角色名称
	@Getter @Setter private String RoleName;
	//角色昵称
	@Getter @Setter private String NickName;
	//角色值
	@Getter @Setter private String RoleValue;
	//角色描述
	@Getter @Setter private String RoleDesc;
	//角色拥有的权限
	@Getter @Setter private Set<Right> rights = new HashSet<Right>();
}

#DAO层的方法定义#
Dao层通过Hibernate实现ORM映射，完成数据库到JAVA对象的映射，数据库链接又通过Spring管理。同事事务在Service层又Spring以AOP的思想实现同一的控制.
DAO层的方法应以如下规则命名:
命名规则: CURDEntry() 	注CURD代表操作方式（通过Entry表明該操作属于DAO层）
	//保存实体
	public void saveEntry(T t);
	
	//更新实体
	public void updateEntry(T t);
	
	//保存或更新
	public void saveOrUpdateEntry(T t);
	
	//删除实体
	public void deleteEntry(T t);
	
	//加载实体
	public T loadEntry(String id);
	
	//加载实体
	public T getEntry(String id);
	
	//以HQL方式批量操作
	public int batchEntry (String hql,Object ...objects);
	
	//通过HQL获得实体列表
	public List<T> getEntryListByHQL(String hql,Object ...objects);
	
	//通过SQL获得提示列表
	public List<T> getEntryListBySQL(String sql,Object ...objects);

	public List<T> findEntryByHQL(String hql, Object[] objects);
	
	//以分页的方式获得实体
	public EntryPage queryPageEntry(final String hql, int page, final int size);
##DAO层的设计原则##
DAO层顾名思义就是用来操作业务实体的CURD操作，因此不应该将业务逻辑添加到DAO层,业务层应到设计简单明了，通用。
#Service层的方法定义#
Service是J2EE软件的核心部分，主要涉及到软件的业务功能，它通过控制层将客户端请求分装传递到业务层，业务层根据具体的情况做成相应的操作，例如调用DAO层的方式实现对业务实体的控制.
一般的设计原则是在业务层提供一套和DAO层统一的API接口。但为了明显的区分該方法调用是Service层的而不是DAO层的，对Service层的方法做如下规定:
命名规则: CURD() 	注CURD代表操作方式（通非Entry表明該操作属于Service层）
	//保存实体
	public void save (T t);
	//更新实体
	public void update (T t);	
	//保存或更新
	public void saveOrUpdate (T t);
	//删除实体
	public void delete (T t);
	//加载实体
	public T load(String id);
	//加载实体
	public T get(String id);
	//以HQL方式批量操作
	public int batch(String hql,Object ...objects);
	//通过HQL获得实体列表
	public List<T> getListByHQL(String hql,Object ...objects);
	//通过SQL获得提示列表
	public List<T> getListBySQL(String sql,Object ...objects);
	public List<T> findByHQL(String hql, Object[] objects);
	//以分页的方式获得实体
	public EntryPage queryPage(final String hql, int page, final int size);
##Service层的设计原则##
Service是一个系统的核心，所有的操作及判断都应当有该层完成，另外由于Spring在初始化的时候以单利的方式进行初始化，因此可以将一些常用的费时的操作放在该层，避免其他层因创建对象而带来的性能问题。另外该层也应该保证业务完整性，及数据库事务应该有该层控制并完成。
#控制层Action的方法定义#
该层有struts2实现，在此处层有一个简单的设计原则，及所以的非静态资源都应该有Action进行跳转，及链接中要直接出现*.JSP等。
方法定义:
	对应Action的action的方法，应当有统一的前缀或后缀，此处使用前缀及:
	execActionName()
另外如果实现了ModelDriven拦截器、Preparable拦截器、Validateable拦截器
其Action怎还用实现
如下俩个方法：
	getModel():提供Model对象
	prepareExecActionName ()：在调用execActionName方法前提供一个Model对象
	validateExecActionName():在调用execActionName前做输入校验

另外如果是实现Preparabel拦截器的话，就应当使用paramsPrepareParamsStack拦截器栈
Struts 2.0的设计上要求 modelDriven 在 params 之前调用，而业务中prepare要负责准备model，准备model又需要参数，这就需要在 prepare之前运行params拦截器设置相关参数，这个也就是创建paramsPrepareParamsStack的原因。 
##Action的设计原则##
SSH整合的核心思想中Action交有Spring控制，对于每个请求Spring会重新创建一个对象，故Action的设计原则应该将其体积最小化，方法最简化。故在Action中只做简单的数据封装和传递，已经一些校验工作。其余的所有部分都应当交由Service去控制。

#Spring整合Hibernate#
时至今日，Java EE通常都会面向对象的方式来操作关系数据库，都会采用ORM框架来完成这一功能，其中Hiernate以其灵巧，轻便的封装赢得了准多开发者的青睐。
Spring以良好的开放性，能与大部分发ORM框架良好整合。

Spring提供的DAO支持
	DAO模式是一种标准的Java EE设计模式，DAO模式的核心思想是：所有的数据库访问，都通过DAO组建完成，DAO组建封装了数据库的增、删、改等原子操作。
对于Java EE应用的架构，有非常多的选择，但不管细节如何变换。Java EE应用都大致可分为如下三层：
		表现层
		业务逻辑层
		数据持久层
轻量级Java EE框架以Spring Ioc容器为核心，承上启下，相上管理来自表现层的Action，下下管理业务逻辑层组建，同时负责业务逻辑层所需的DAO对象。
DAO组建是整个Java EE应用的持久层访问的重要组件，每个Java EE应用的底层实现都难以离开DAO组件的支持。Spring对实现DAO组件提供了许多工具类，系统的DAO组件可以继承这些工具类完成，从而可以更简单的实现DAO组件
Spring提供了一系列的抽象类，这些抽象类将被作为应用中DAO实现的父类，通过继承这些抽象类，spring简化DAO的开发不足，能以一致的方式使用数据库访问技术。不管底层采用JDBC、JDO还是Hibernate。就Hibernate的持久层访问技术而言，spring提供了如下三个工具类（或接口）来支持DAO组件的实现:
		HibernateDaoSupport
		HibernateTemplate
		HibernateCallback
管理Hibernate的SessionFactory
当通过Hibernate进行持久层访问时，必须先获得SessionFactory对象。它是单个数据库映射关系编译后的内存镜像。大部分情况下，一个JavaEE应用对应一个数据库，即对应一个SessionFactory对象
Spring的IoC容器正好提供了这种管理方式，它不仅能以声明式的方式配置SessionFactory实例，也可以为SessionFactory注入数据源。
例如：
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://www.springframework.org/schema/beans"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
	
	<!-- 配置Spring数据源 Oracle10g -->
	<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="oracle.jdbc.driver.OracleDriver"></property>
		<property name="url" value="jdbc:oracle:thin:@localhost:1521:orcl"></property>
		<property name="username" value="scott"></property>
		<property name="password" value="tiger"></property>
	</bean>
	
	<!-- 定义Hibernate的SessionFactory -->
    <bean id="sessionFactory" class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        
        <!-- mappingResources属性用来列出全部映射文件 -->
        <property name="mappingResources">
            <list>
                <!-- 以下用来列出所有的PO映射文件 -->
                <value>conf/hbm/User.hbm.xml</value>
            </list>
        </property>
         <!-- 定义Hibernate的SessionFactory属性 -->
        <property name="hibernateProperties">
             <props>
                <!-- 指定Hibernate的连接方言 -->
                <prop key="hibernate.dialect">
                	org.hibernate.dialect.Oracle9Dialect
                </prop>
                <!-- 配置启动应用时，是否根据Hibernate映射自动创建数据表 -->
                <prop key="hibernate.hbm2ddl.auto">update</prop>
           </props>
        </property>
    </bean>
</beans>
一旦在Spring的IoC容器中配置了SessionFactory Bean，它将随应用的启动而加载，并可以充分利用IoC容器的功能，将SessionFactory Bean注入任何bean。比如DAO组件，一旦DAO组件获得了SessionFactory Bean的引用，就可以完成实际的数据库访问。
使用HibernateTemplate
HibernateTemplate提供持久层访问模板化，它需要提供一个SessionFactory的引用，就可执行持久化操作。SessionFactory对象既可通过构造参数传入，也可以通过设值方式传入，HibernateTemplate提供如下三个构造函数：
	HibernateTemplate():构造一个默认的HibernateTemplate实例，因此创建了HibernateTemplate实例之后，还必须使用setSessionFactory(SessionFactory sf)为HibernateTemplate注入SessionFactory对象，然后才可以进行持久化操作。
	HibernateTemplate(org.hibernate.SessionFactory sessionFactory)在构造时已经传入SessionFactory对象，创建后立即可以执行持久化操作。
	HibernateTemplate(org.hibernate.SessionFactory sessionFactory,Boolean allowCreate):allowCreate参数表明，如果当前线程没有找到一个事务性的Session，是否需要创建一个非事务的Session
例如：
package spring3.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;

import hibernate3.POJO.VO_user;
import spring3.dao.PersonDao;

public class PersonDaoImpl implements PersonDao{

	//使用Spring注入HibernateTemplate的一个实例
	private HibernateTemplate hibernateTemplate;
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public void save(VO_user vo) {
		this.hibernateTemplate.save(vo);
	}

	@Override
	public void delete(VO_user vo) {
		this.hibernateTemplate.delete(vo);
	}

	@Override
	public VO_user getUserByID(long id) {
		return this.hibernateTemplate.get(VO_user.class, id);
	}
}
利用spring进行注入
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://www.springframework.org/schema/beans"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
	
	<!-- 配置Spring数据源 Oracle10g -->
	<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="oracle.jdbc.driver.OracleDriver"></property>
		<property name="url" value="jdbc:oracle:thin:@localhost:1521:orcl"></property>
		<property name="username" value="scott"></property>
		<property name="password" value="tiger"></property>
	</bean>
	
	<!-- 定义Hibernate的SessionFactory -->
    <bean id="sessionFactory" class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        
        <!-- mappingResources属性用来列出全部映射文件 -->
        <property name="mappingResources">
            <list>
                <!-- 以下用来列出所有的PO映射文件 -->
                <value>conf/hbm/User.hbm.xml</value>
            </list>
        </property>
         <!-- 定义Hibernate的SessionFactory属性 -->
        <property name="hibernateProperties">
             <props>
                <!-- 指定Hibernate的连接方言 -->
                <prop key="hibernate.dialect">
                	org.hibernate.dialect.Oracle9Dialect
                </prop>
                <!-- 配置启动应用时，是否根据Hibernate映射自动创建数据表 -->
                <prop key="hibernate.hbm2ddl.auto">update</prop>
           </props>
        </property>
    </bean>
	
	<bean id="hibernateTemplate" class="org.springframework.orm.hibernate3.HibernateTemplate">
  		<property name="sessionFactory" ref="sessionFactory"></property>
 	</bean>
	
 	<!-- 配置一个Person操作 -->
 	<bean id="person" class="spring3.impl.PersonDaoImpl">
 		<property name="hibernateTemplate" ref="hibernateTemplate"></property>
 	</bean>
 	
</beans>
HibernateTemplate提供很多实用的方法来完成基本的操作，比如增加、删除、修改、查询等操作。大部分情况下，通过HibernateTemplate如下的方法就可以完成大多数DAO对象的CRUD操作。
	void delete(Object entity):  删除指定持久化实例
	deleteAll(Collection entitles)：删除集合内全部持久化类实例
	find(String queryString)：根据HQL查询字符串来返回实例集合的系列重载方法
	findByNameQuery(String queryName)：根据命名查询返回实例集合的系列重载方法
	get(Class entityClass,Serializable id)：根据主键加载特定持久化类的实例
	save(Object entity)：保存新的实例
	saveOrUpdate(Object entity)：根据实例状态，选择保存或更新
	update(object entity)：更新实例的状态，要钱entity是持久状态
	setMaxResults(int maxResults)：设置分页的大小
使用HibernateCallback
使用HibernateTemplate进行数据库访问十分方便，但不是什么时候都未必好用。为了避免HibernateTemplate灵活性不足的缺陷，HibernateTemplate还提供一种更加灵活的方式来操作数据，通过这种方式可以完全使用Hibernate的操作方式。HibernateTemplate的灵活访问方式通过如下俩个方法来完成。
	Object execute(HibernateCallback action)
	List executeFind(HibernateCallback action)
这俩个方法都需要一个HibernateCallback实例，HibernateCallback，实例可在任何有效的Hibernate数据库访问中使用。程序开发者通过HibernateCallback可以完全使用Hibernate灵活的方式来访问数据库，解决Spring封装Hibernate后灵活性不足的缺陷


Hibernate
映射对象标识符
Hibernate使用对象标识符（OID）来建立内存中的对象和数据库表中记录的对应关系。对象的OID和数据表的主键对应，Hibernate通过标识符生成器来为主键赋值。
Hibernate推荐在数据表中使用代理主键，既不具备业务含义的字段，代理主键通常为整数类型，因为整数类型比字符串类型要节省更多的数据库空间
在对象-关系映射文件中id元素用来设置对象标识符。Generator子元素用来设定标识符生成器。
Hibernate提供了标识符生成器接口:identifierGenerator并提供了各种内置对象 
主键生成策略
Id
Id设定持久化类的OID和表的主键的映射
	-name：标识持久化类OID的属性名
	-column：设置标识属性所映射的数据库表的列名（主键字段的名称）
	-unsaved-value：若设定了改属性，Hibernate会通过比较持久化类的OID值和改属性值来分区当前持久化类的对象是否为临时对象
-type:指定Hibernate映射类型，hibernate映射类型是Java类型的SQL类型的桥接，如果没有为某个属性显示设定映射类型，Hibernate会运用反射机制先识别出持久化类的特定属性的Java类型，然后自动使用与之对应的默认的Hibernate映射类型
-java的基本数据类型和包装对应相同的Hibernate映射类型。基本数据类型无法表达null。所以对于持久化类的OID推荐使用包装类型。
Generator
Generator是id的子类，用来设定持久化类设定标识符生气
	-class：指定使用的标识符生成器全限定的类名或其缩写名

Hibernate提供了内置的生成器：
标识符生成器	描述
Increment	代理主键。有hibernate自动以递增方式生成
Identity	代理主键。有底层数据库生成标识符
Sequence	代理主键。Hibernate根据底层数据库的序列生成标识符，这些要求底层数据库支持序列
Hilo	代理主键。Hibernate分局high/low算法生成标识符
Seqhilo	代理主键。使用一个高/低位算法来高校的生成long/short类型的标识符
Native	代理主键。根据底层数据库对自动生成标识符的方式，自动选用identity
Sequence或hilo
Uuid.hex	代理主键。Hibernate采用128位的UUID算法生成标识符
Uuid.string	适用于代理主键。UUID被选编码成一个16字符长的字符串
Assigned	自然主键。有Java应用程序负责生成标识符
Foreign	代理主键。使用另外一个相关的对象的标识符
Property
Property元素用于指定类型的属性和表的字段映射
-name：指定改持久化类的属性名字
-column：指定与类的属性映射的表的字段名，如果没有设置啊属性，hibernate将值将是哟年类的属性名做为字段
-type：指定hibernate映射类型。Hibernate映射类型是Java类型与SQL类型的桥梁，如果没有映射没一个属性，hibernate会用反射机制为持久化类的特定属性的Java类型，然后自动使用与质对应的默认的hibernate映射类型
-not-null：若改属性值为true，表明不允许为null。偶人为falshe
-access：指定hibernate的默认的属性访问策略。默认值为property既使用getter/setter方法来访问属性。若指定field，则hibernate会忽略getter/setter方法，而通过反射访问成员变量
-unique：设置是否为该属性所映射的数据列添加唯一约束
-index：指定一个字符串的索引名称，当系统需要hibernate自动建表时，为该属性映射的数据列表创建索引，从而该加快该数据列的查询
-length：指定该属性所映射数据列的字段的长度
-scale：指定该属性所映射数据列的小数位，对double，float，decimal等类型的数据列有效
-formula：设置一个SQL表达式，hibernate将根据它来计算派生属性的值

Hibernate映射类型	Java类型	标准SQL类型
integer	java.lang.Integer	INTEGER
long	java.lang.Long	BIGINT
short	java.lang.Short	SMALLINT
float	java.lang.Float	FLOAT
double	java.lang.Double	DOUBLE
big_decimal	java.math.BigDecimal	NUMERIC
character	java.lang.String	CHAR(1)
string	java.lang.String	VARCHAR
byte	byte或java.lang.Byte	TINYINT
boolean	boolean或java.lang.Boolean	BIT
yes_no	boolean或java.lang.Boolean	CHAR(1)('Y'或'N')
true_false	boolean或java.lang.Boolean	CHAR(1)('Y'或'N')
date	java.util.Date或java.sql.Date	DATE
time	java.util.Date或java.sql.Time	TIME
timestamp	java.util.Date或java.sql.Timestamp	TIMESTAMP
calendar	java.util.Calendar	TIMESTAMP
calendar_date	java.util.Calendar	DATE
binary	byte[]	VARBINARY或BLOB
text	java.lang.String	CLOB
serializable	java.io.Serializable实例	VARBINARY或BLOB
clob	java.sql.Clob	CLOB
blob	java.sql.Blob	BLOB
class	java.lang.Class	VARCHAR
locale	java.util.Locale	VARCHAR
timezone	java.util.TimeZone	VARCHAR
currency	java.util.Currency	VARCHAR
Java大对象类型的Hibernate映射
在Java中，java.lang.String可用于表示长字符串(长度超过255)，字符数组byte[]可以用于存放图片或二进制数据，此外，在JDBC API中还提供了Java.sql.Clob和java.sql.Blob类型，它们分别和标准SQL中的CLOB和BLOB类型对应。CLOB表示字符串大对象。BLOB表示二进制大对象
 
实际上在Java应用程序中处理长度超过255的字符串，使用java.lang.String比java.sql.Clob更方便。
Java时间和日期类型Hibernate映射
在java中，代表时间和日期的类型包括：java.util.Date和java.util.Calendar。此外JDBC按批中还提供3个扩展了Java.util.Date类的子类：java.sql.Date。java.sql.Time和java.sql.Timestamp
这三个类分别和标准的SQL类型中的DATE，time和timestamp类型对应
 
使用Hibernate内置映射类型
以下情况必须显示指定hibernate映射类型
	一个java类型可能对应多个hibernate映射类型。例如：如果持久化类的属性为：java.Util.Date，对应的hibernate映射类型可以是data，time或timestamp，此时必须根据对应的数据表的字段的SQL类型，来确定hibernate映射类型，如果字段为date类型，那么hibernate映射类型为date；如果字段为time类型，那么hibernate映射类型为time，如果字段为TIMESTATMP类型，那么hibernate映射类型为timestamp
例如：
================================POJO========================================
package hibernate3.POJO;

import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
public class CustomerVO {

	private long id;
	
	private String name;
	private char sex;
	private int  phoneNum;
	private boolean married;
	
	private String descript;
	private Blob image;
	
	private Date birthday;
	private Timestamp regeditTime;
    //getter/setter
}
================================映射文件=================================
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
	"-//Hibernate/Hibernate Mapping DTD 3.0//EN"
	"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">

<hibernate-mapping package="hibernate3.POJO">

	<class name="CustomerVO" table="hbm_tbl_customer">

		<id name="id" column="recordid">
			<generator class="native" />
		</id>

		<property name="name" column="name" type="java.lang.String"></property>

		<property name="sex" column="sex" type="character"></property>

		<property name="phoneNum" column="phone_num" type="java.lang.Integer"></property>

		<property name="married" column="married" type="java.lang.Boolean"></property>

		<property name="descript" column="descript" type="java.lang.String"></property>

		<property name="image" column="image" type="java.lang.String"></property>

		<property name="birthday" column="birthday" type="java.sql.Date"></property>

		<property name="regeditTime" column="regidt_time" type="java.sql.Timestamp"></property>
	</class>

</hibernate-mapping>
=====================测试========================
package hibernate3.CoreTest;



import java.sql.Date;
import java.sql.Timestamp;

import hibernate3.POJO.CustomerVO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConfigFileTest {
	
	Configuration configuration;
	SessionFactory sessionFactory;
	Session session;
	Transaction tx;
	@Before
	public void before(){
		configuration = new Configuration().configure();
		sessionFactory = configuration.buildSessionFactory();
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
	}
	@After
	public void after(){
		tx.commit();
		session.close();
		sessionFactory.close();
	}
	@Test
	public void testConfigFile() {
		CustomerVO obj = new CustomerVO();
		obj.setName("hibernate3");
		obj.setSex("男".charAt(0));
		obj.setMarried(false);
		obj.setPhoneNum(2212852);
		obj.setBirthday(Date.valueOf("2000-01-01"));
		obj.setRegeditTime(new Timestamp(new java.util.Date().getTime()));
		session.save(obj);
	}
}
映射组成关系

建立域模型和关系数据模型有着不同的出发点：
	-域模型：由程序代码组成，通过细化持久化类的粒度可提供代码的可重用性，简化编程
	-关系数据模型：由关系数据组成，在存在数据冗余的情况下，需要把粗粒度的表表分成具有外键参照关系的几个细粒度的表，从而节省存储空间。另外一方面，在没有数据冗余的情况下，应该尽量减少表的数目，简化表之间的参照关系，以便提供数据的访问速度。

Hibernate把持久化类的属性分为俩中：
-值（value）类型：没有OID不能被单独持久化，生命周期依赖于所属的持久化类的对象的生命周期
-实体（entity）类型：有OID可以被单独持久化，有独立的生命周期

无法直接用propperty映射name属性。Hibernate使用<component>元素来映射成组的关系。
Component元素用来映射组成关系，常用的属性：
	-name
	-access
	-class:设定组成关系属性的类型
	-insert=”true”
	-lazy=”false”
	-node
	-optimistic-lock=”true”
	Parent元素指定组成属性所属的整体类
例如：
 
	<component name="pay" class="pay">
		<parent name="worker"/>
	
		<property name="monthlyPay" column="month_pay" type="integer"></property>
		<property name="vocationWithPay" column="vocation_with_pay" type="integer"></property>
		<property name="yearPay" column="year_pay" type="integer"></property>
	</component>
Spring整合Struts
整合Spring和Struts其实就是适用Spring的IOC容器管理Struts2的Action。
首先将相应的Jar文件复制到Web/lib目录下，记得复制struts2-spring-plugin-xxx.jar文件。
在web.xml文件中配置struts2
<filter>
<filter-name>struts2</filter-name> 
<filter-class>
org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter
</filter-class>
</filter>
<filter-mapping>
<filter-name>struts2</filter-name>
<url-pattern>/*</url-pattern>
</filter-mapping>
新建struts的配置文件struts.xml
<struts>
<constant name="struts.enable.DynamicMethodInvocation" value="false" />
<constant name="struts.devMode" value="true" />
<package name="default" namespace="/" extends="struts-default">
</package>
</struts>
在web.xml文件中配置spring
//配置 Spring 配置文件的位置
<context-param>
<param-name>contextConfigLocation</param-name>
<param-value>classpath:applicationContext.xml</param-value>
</context-param>
//配置启动 IOC 容器的 Listener
<listener>
<listener-class>
org.springframework.web.context.ContextLoaderListener
</listener-class>
</listener>
1.在 Spring 的配置文件中配置 Struts2 的 Action 实例，注意：需要
配置 scope 属性为 prototype，因为 Struts2 的 Action 不是单例的！
例如：
<bean id="userAction" 
class="com.atguigu.ss.action.UserAction" scope="prototype">
<property name="userService" ref="userService"></property>
</bean>
2.  在  Struts 配置文件中配置 action, 但其 class 属性不再指向该
Action 的实现类, 而是指向 Spring 容器中 Action 实例的 ID
例如：
<action name="helloworld" class="userAction">
<result>/success.jsp</result>
</action>
原理
Spring 插 件 是 通 过 覆 盖 Struts2 的
ObjectFactory 来增强核心框架对象的创建。当创建一个对象的时候，它会用
Struts2 配置文件中的 class 属性去和 Spring 配置文件中的 id 属性进行关联，
如果能找到， 则由 Spring 创建， 否则由 Struts 2 框架自身创建， 然后由 Spring
来装配。
通过Spring AOP配置业务层事务
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xsi:schemaLocation="http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd">


	<!-- 通过context指定外部引用的属性文件 -->
	<context:property-placeholder location="classpath:db.properties" />

	<!-- 配置Spring数据源 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="driverClass" value="${jdbc.driverClassName}" />
		<property name="jdbcUrl" value="${jdbc.url}" />
		<property name="user" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />

		<property name="maxPoolSize" value="${c3p0.pool.size.max}" />
		<property name="minPoolSize" value="${c3p0.pool.size.min}" />
		<property name="initialPoolSize" value="${c3p0.pool.size.ini}" />
		<property name="acquireIncrement" value="${c3p0.pool.size.increment}" />
	</bean>


	<!-- 定义Hibernate的SessionFactory -->
	<bean id="sessionFactory"
		class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="mappingLocations">
			<list>
				<value>classpath:hbm/*.hbm.xml</value>
			</list>
		</property>
		<property name="hibernateProperties">
			<props>
				<prop key="hibernate.dialect">org.hibernate.dialect.Oracle10gDialect</prop>
				<prop key="hibernate.show_sql">true</prop>
				<prop key="hibernate.format_sql">false</prop>
				<prop key="hibernate.hbm2ddl.auto">update</prop>
				<prop key="hibernate.current_session_context_class">thread</prop>
				<prop key="hibernate.use_sql_comments">false</prop>
				<prop key="hibernate.temp.use_jdbc_metadata_defaults">false</prop>
				<prop key="hibernate.current_session_context_class">org.springframework.orm.hibernate4.SpringSessionContext</prop>
				<prop key="javax.persistence.validation.mode">none</prop>
				<!-- <prop key="hibernate.cache.provider_class">net.sf.ehcache.hibernate.EhCacheProvider</prop> 
					<prop key="hibernate.cache.region.factory_class">org.hibernate.cache.ehcache.EhCacheRegionFactory</prop> -->
				<!-- <prop key="hibernate.cache.provider_class">net.sf.ehcache.hibernate.EhCacheProvider</prop> 
					<prop key="hibernate.cache.region.factory_class">org.hibernate.cache.ehcache.EhCacheRegionFactory</> -->
				<!-- 配置二级缓存 -->
				<!-- <prop key="hibernate.cache.use_second_level_cache">true</prop> <prop 
					key="hibernate.cache.provider_class">org.hibernate.cache.EhCacheProvider</prop> 
					<prop key="hibernate.cache.use_query_cache">true</prop> 这个一定要配上，不然可能会出错 <prop 
					key="hibernate.cache.provider_class">org.hibernate.cache.HashtableCacheProvider</prop> -->
			</props>
		</property>
	</bean>

	<!-- 配置hibernteTemplate -->
	<bean id="hibernateTemplate" class="org.springframework.orm.hibernate4.HibernateTemplate">
		<property name="sessionFactory" ref="sessionFactory"></property>
	</bean>

	<!-- 管理事务(JDBC局部事务管理策略) -->
	<bean id="transactionManager"
		class="org.springframework.orm.hibernate4.HibernateTransactionManager">
		<property name="sessionFactory" ref="sessionFactory" />
	</bean>

	<!-- 配置事务通知属性 -->
	<tx:advice id="txAdvice" transaction-manager="transactionManager">
		<!-- 定义事务传播属性 -->
		<tx:attributes>
			<tx:method name="get*" propagation="REQUIRED" read-only="true" />
			<tx:method name="find*" propagation="REQUIRED" read-only="true" />
			<tx:method name="load*" propagation="REQUIRED" read-only="true" />
			<tx:method name="*" propagation="REQUIRED" />
		</tx:attributes>
	</tx:advice>

	<!-- 配置事务切入点, 再把事务属性和事务切入点关联起来 -->
	<aop:config>
		<aop:pointcut expression="execution(* app.service.*.*(..))"
			id="txPointcut" />
		<aop:advisor advice-ref="txAdvice" pointcut-ref="txPointcut" />
	</aop:config>

	<!-- 业务层的日志记录器 -->
	<bean id="serviceAopLog" class="app.service.interceptor.ServiceIntercetor" />

	<aop:config>
		<!-- 事务切入 -->
		<aop:advisor advice-ref="txAdvice" pointcut="execution(* *..*ServiceImpl.*(..))" order="2" />

		<!-- 日志切入 -->
		<aop:aspect id="serviceAopLog" ref="serviceAopLog" order="1">
			<aop:around pointcut="execution(* *..*ServiceImpl.*(..)) and !bean(businessLogService)" method="logAop" />
		</aop:aspect>
	</aop:config>
</beans>
利用注解和AOP实现业务的日志记录
利用AOP思想在所有业务层的方法调用上加入业务日志。
首先建立业务日志实体
public class BusinessLog {

	@Getter @Setter private Long id;

	@Getter @Setter private String uri;

	@Getter @Setter private String userName;

	@Getter @Setter private String address;

	@Getter @Setter private Date startTime = new Date();
	
	@Getter @Setter private Date endTime = new Date();

	@Getter @Setter private String operationName;

	@Getter @Setter private String operationParam;
	
	@Getter @Setter private String type;

	@Getter @Setter private String result;

	@Getter @Setter private String msg;

	@Override
	public String toString() {
		return " ["+(endTime.getTime() - startTime.getTime())+" - "+ result+"] -> "+operationName+"("+operationParam+")";
	}
}
建立并定义AOP切入点
public class LogInterceptor implements Interceptor {

	private static final long serialVersionUID = 965894819250440297L;

	@Override
	public void destroy() {

	}

	@Override
	public void init() {

	}

	@Override
	public String intercept(ActionInvocation action) throws Exception {
		String methodName = action.getProxy().getMethod();
		Class clazz = action.getAction().getClass(); // 获取类对象
		Method currentMethod = clazz.getMethod(methodName);
		ActionContext context = action.getInvocationContext();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> actionParam = context.getParameters();
		String uri = request.getRequestURI();
		String temp = "";
		if (currentMethod.isAnnotationPresent(ActionMethod.class)) {
			ActionMethod annotation = currentMethod.getAnnotation(ActionMethod.class);
			temp = annotation.type() + "::" + annotation.name();
		} else {
			return uri+"方法无签名,请不会执行！";
		}
		Long startTime = System.currentTimeMillis();
		// Map<String, Object> session = context.getSession();
		String result = action.invoke();
		Long endTime = System.currentTimeMillis();
		StringBuffer paramters = new StringBuffer();
		Iterator<Entry<String, Object>> iterator = actionParam.entrySet().iterator();
		Entry<String, Object> entry;
		while (iterator.hasNext()) {
			entry = iterator.next();
			paramters.append("&");
			paramters.append(entry.getKey());
			paramters.append("=");
			paramters.append(StringUtil.arr2Str((String[]) entry.getValue()));
		}
		System.out.println(DateUtil.getDateTime(new Date()) + " struts[" + (endTime - startTime) + "]->" + temp + " > " + uri + "::" + paramters.toString());
		return result;
	}

}
定切入点
	<!-- 业务层的日志记录器 -->
	<bean id="serviceAopLog" class="app.service.interceptor.ServiceIntercetor" />

	<aop:config>
		<!-- 事务切入 -->
		<aop:advisor advice-ref="txAdvice" pointcut="execution(* *..*ServiceImpl.*(..))" order="2" />

		<!-- 日志切入 -->
		<aop:aspect id="serviceAopLog" ref="serviceAopLog" order="1">
			<aop:around pointcut="execution(* *..*ServiceImpl.*(..)) and !bean(businessLogService)" method="logAop" />
		</aop:aspect>
	</aop:config>
单元测试
	@Test
	public void getMenuList(){
		RightsService service = (RightsService) Context.getBean("rightsService");
		
		List<?> list = service.getMenuList();
		
		System.out.println(list.size());
	}
测试结果如下
 
利用Struts拦截器和Anontation记录和拦截请求层输入
编写Anontation
public enum ActionType {
	NONE,		//无
	SYSTEM,		//系统方法
	TOOLS,		//工具方法
	BUSINESS;	//业务方法
}

@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionMethod {
	public ActionType type() default ActionType.BUSINESS;
	public String name() default "业务请求";
}
编写Action的拦截器记录并拦截为签名的请求输入
public class LogInterceptor implements Interceptor {

	private static final long serialVersionUID = 965894819250440297L;

	@Override
	public void destroy() {

	}

	@Override
	public void init() {

	}

	@Override
	public String intercept(ActionInvocation action) throws Exception {
		String methodName = action.getProxy().getMethod();
		Class clazz = action.getAction().getClass(); // 获取类对象
		Method currentMethod = clazz.getMethod(methodName);
		ActionContext context = action.getInvocationContext();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> actionParam = context.getParameters();
		String uri = request.getRequestURI();
		String temp = "";
		if (currentMethod.isAnnotationPresent(ActionMethod.class)) {
			ActionMethod annotation = currentMethod.getAnnotation(ActionMethod.class);
			temp = annotation.type() + "::" + annotation.name();
		} else {
			return uri+"方法无签名,请不会执行！";
		}
		Long startTime = System.currentTimeMillis();
		// Map<String, Object> session = context.getSession();
		String result = action.invoke();
		Long endTime = System.currentTimeMillis();
		StringBuffer paramters = new StringBuffer();
		Iterator<Entry<String, Object>> iterator = actionParam.entrySet().iterator();
		Entry<String, Object> entry;
		while (iterator.hasNext()) {
			entry = iterator.next();
			paramters.append("&");
			paramters.append(entry.getKey());
			paramters.append("=");
			paramters.append(StringUtil.arr2Str((String[]) entry.getValue()));
		}
		System.out.println(DateUtil.getDateTime(new Date()) + " struts[" + (endTime - startTime) + "]->" + temp + " > " + uri + "::" + paramters.toString());
		return result;
	}
}
利用浏览器进行测试，后期会编写Python的测试脚本
 
