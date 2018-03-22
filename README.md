#第二章:创建和销毁对象
-   本章的主题是创建和销毁对象:何时以及如何创建对象,何时以及如何避免创建对象,
 如何确保它们能够适时地销毁,以及如何管理对象销毁之前必须进行的各种清理动作.
 
### 第1条:考虑用静态工厂方法代替构造器
 
    url:src/main/java/top/bfylu.service_provider_framework
-   静态工厂方法与构造器不同的第一大优势在于,它们有名称.

一个类只能有一个带有指定签名的构造器.编程人员通常知道如何避开这一限制:通过提
供两个构造器,它们的参数列表只在参数类型的顺序上有所不同.实际上这并还是一个好
主意.面对这样的API,用户永远也永不住该用哪个构造器,结果常常会调用错误的构造器.
并且,读到使用了这些构造器的代码时,如果没有参考类的文档,往往不知所云.

-   静态工厂方法与构造器不同的第三大优势在于,它们可以返回原返回类型的任何子
  类型的对象.
  
    静态工厂方法返回的对象所属的类,在编写包含该静态工厂方法的类时可以不必存在.
  这种灵活的静态工厂方法构成了服务提供者框架(Service Provider Framework)的
  基础,例如JDBC(java数据库连接,java Database Connectivity) API.服务提供
  者框架是指这样一个系统:多个服务提供者实现一个服务,系统为服务提供者的客户端提
  供多个实现,并把它们从多个实现中解耦出来.
  
  服务提供者框架中有三个重要的组件:服务接口(Service Interface),这是提供者实
  现的;提供者注册API(Provider Registration API),这是系统用来注册实现,让客
  户端访问它们的;服务访问API(Service Access API),是客户端用来获取服务的实例的.
  服务访问API是"灵活的静态工厂",它构成了服务提供者框架的基础.

### 第2条:遇到多个构造器参数时要考虑构建器
    url:/src/main/java/top/bfylu/builder_pattern
-   静态工厂和构造器有个共同的局限性:它们都不能很好地扩展到大量的可选参数.考虑用
    一个类表示包装食品外面显示的营养成份标签.这些标签中有几个域是必需的:每份的含量,
    每罐的含量以及每份的卡路里,还有超过20个可选域:总脂肪量,饱和脂肪量,转化脂肪,胆
    固醇,钠等等.大多数产品在某几个可选域中都会有非零的值.
    
    对于这样的类,应该用哪种构造器或者静态方法来编写呢?程序员一向习惯采用重叠构造器
    (telescoping constructor) 模式,在这种模式下,你提供第一个只有必要参数的构造
    器,第二个构造器有一个可选参数,第三个有两个可选参数,依此类推,最后一个构造器包含所
    有可选参数.  
    
    ```java
    /**
     * Telescoping constructor pattern - does not scale well!
     * Telescoping构造函数模式 - 不能很好地扩展！
     */
    public class NutritionFacts {
        private final int servingSize;      //(mL)              required
        private final int serings;          //(per container)   required
        private final int calories;         //                  optional
        private final int fat;              //(g)               optional
        private final int sodium;           //(mg)              optional
        private final int carbohydrate;     //(g)               optional
    
        public NutritionFacts(int servingSize, int serings) {
            this(servingSize, serings, 0);
        }
    
        public NutritionFacts(int servingSize, int serings, int calories) {
            this(servingSize, serings, calories, 0);
        }
    
        public NutritionFacts(int servingSize, int serings, int calories, int fat) {
            this(servingSize, serings, calories, fat, 0);
        }
    
        public NutritionFacts(int servingSize, int serings, int calories, int fat, int sodium) {
            this(servingSize, serings, calories, fat, sodium, 0);
        }
    
        public NutritionFacts(int servingSize, int serings, int calories, int fat, int sodium, int carbohydrate){
            this.servingSize    = servingSize;
            this.serings        = serings;
            this.calories       = calories;
            this.fat            = fat;
            this.sodium         = sodium;
            this.carbohydrate   = carbohydrate;
        }
    }
    
-   当你想要创建实例的时候,就利用参数列表最短的构造器,但该列表中包含了要设置的所有参数:
    
    ```NutritionFacts cocaCola = new NutritionFacts(240, 8, 100, 0, 35, 27)```
   
    这个构造器调用通常需要许多你本不想设置的参数,但还是不得不为它们传递值.在这个例子中,我们
    给fat传递了一个值为0.如果"仅仅:是这6个参数,看起来还不算太糟,问题是随参数数目的增加,它
    很快就推动失去了控制.
    
    一句话:重叠构造器模式可行,但是当有许多参数的时候,客户端代码会很难写,并且仍然较难以阅读.
    如果读者相知道那些值是什么意思,必须很仔细地数着这些参数来探个究竟.一长串类型相同的参数
    会导致一些微妙的错误.如果客户端不小心颠倒了其中两个参数的顺序,编译器也不会出错,但是程序
    在运行时会出现错误的行为.
    
    遇到许多构造器参数的时候,还有第二种代替办法,即JavaBeans模式,在这种模式下,调用一个无参
    构造器来创建对象,然后调用setter方法来设置每个必要的参数,以及每个相关的可选参数.
        
    ```java
    /**
        * JavaBeans Pattern - allows inconsistency, mandates mutability
        * JavaBean模式 - 允许不一致，强制可变性
        *
        */
       public class NutritionFactsTwo {
           //Parameters initialized to default values (if any)
           private int servingSize  = -1;   //Required; no default value
           private int servings     = -1;   //  "   "   "   "
           private int calories     = 0;
           private int fat          = 0;
           private int sodium       = 0;
           private int carbohydrate = 0;
       
           public NutritionFactsTwo(){}
           //Setters
           public void SetServingSize(int val)     { servingSize = val; }
           public void SetServigs(int val)         { servings = val; }
           public void SetCalories(int val)        { calories = val; }
           public void SetFat(int val)             { fat = val; }
           public void SetSodium(int val)          { sodium = val; }
           private void SetCarbohydrate(int val)   { carbohydrate = val;}
           
       }

   



-   这种模式弥补了重叠构造器模式的不足,说得明白一点,就是创建实例很容易,这样产生的代码读起
    来也很容易.
    
    ```
    NutritionFactsTwo cocaCola = new NutritionFactsTwo();
    cocaCola.setServingSize(240);
    cocaCola.setServings(8);
    cocaCola.setCalories(100); 
    cocaCola.setSodium(35);
    cocaCola.setCarbohydrate(27);
    ```
    
-   遗憾的是,JavaBeans模式自身有着很严重的缺点.因为构造过程被分到了几个调用中,在构造过程
  中JavaBean可能处于不一致的状态.类无法仅仅通过检验构造器参数的有效性来保证一致性.试图使
  用处于不一致状态的对象,将会导致失败,这种指失败与包含错误的代码大相径庭,因此它调试起来十分
  困难.与此相关的另一点不足在于,JavaBeans模式阻止了把类做成不可变的可能,这就需要程序员付出
  额外的努力来确保它的线程安全.
  
  幸运的是,还有第三种替代方法,既能保证像重叠构造器模式那样的安全性,也能保证像JavaBeans模式
  那么好的可读性.这就是Builder模式的一种形式.不直接生成想要的对象,而是让客户端利用所有必要
  的参数调用构造器(或者静态工厂),得到一个builder对象.然后客户端在build对象上调用类似于
  setter的方法,来设置每个相关的可选参数.最后,客户端调用无参的build方法来生成不可变的对象.
  这个builder是它构建的类的静态成员类
 ```java
    /**
     * Builder Pattern
     * @author bfy
     * @date 2018.3.13
     */
    public class NutritionFactsThree {
    
        private final int servingSize;
        private final int servings;
        private final int calorier;
        private final int fat;
        private final int sodium;
        private final int carbohydrate;
    
    
        public static class Builder {
            //Required parameters
            //必需的参数
            private final int servingSize;
            private final int servings;
    
            //Optional parameters - initialized to default values
            //可选参数 - 初始化为默认值
            private int calories        = 0;
            private int fat             = 0;
            private int carbohydrate    = 0;
            private int sodium          = 0;
    
            public Builder(int servingSize, int servings) {
                this.servingSize = servingSize;
                this.servings    = servings;
            }
    
            public Builder calories(int val) {
                calories = val;
                return this;
            }
            public Builder fat(int val) {
                fat = val;
                return this;
            }
            public Builder carbohydrate(int var) {
                carbohydrate = var;
                return this;
            }
            public Builder sodium(int var) {
                sodium = var;
                return this;
            }
    
            public NutritionFactsThree build() {
                return  new NutritionFactsThree(this);
            }
        }
    
        public NutritionFactsThree(Builder builder) {
            servingSize     = builder.servingSize;
            servings        = builder.servings;
            calorier        = builder.calories;
            fat             = builder.fat;
            sodium          = builder.sodium;
            carbohydrate    = builder.carbohydrate;
        }
    }
```   
-   注意NutritionFactsThree是不可变的,所有的默认参数值都意单独放在一个真地方.builder的
    setter方法返回builder本身,以便可以把调用链接起来.下面就是客户端代码:
    ```java
    public class NutritionFactsThreeTest{
    
        NutritionFactsThree cocaCola = new NutritionFactsThree.Builder(240, 8).
        calories(100).sodium(35).carbohydrate(27).build();
    }

-   这样的客户端代码很容易编写,更为重要的是,易于阅读.builder模式模拟了具名的可选参数,就像Ada和
    Python中的一样.
    
    builder像个构造器一样,可以对其参数强加约束条件.build方法可以检验这些约束条件.将参数从
    builder拷贝到对象中之后,并在对象域而不是builder域中对它们进行检验,这一点很重要.如果违反
    了任何约束条件,build方法就应该抛出IllegalStateException.
    
-   对多个参数强加约束条件的另一种方法是,用多个setter方法对某个约束条件必须持有的所有参数进行
    检查.如果该约束条件没有得到满足,setter方法就会抛出IllegalArgumentExecption.这有个好处,
    就是一旦传递了无效的参数,立即就会发现约束条件失败,而不是等着调用build方法.
    
    
    
### 第3条:用私有构造器或者枚举类型强化Singleton属性
    url:/src/main/java/top/bfylu/singleton
-   Singleton(单例模式)指仅仅被实例化一次的类.Singleton通常用来代表那些本质上唯一的系统组件,比如窗口管理
    或者文件系统.使类成为Singleton会使它的客户端测试变得十分困难,因为无法给Singleton替换模拟实
    现,除非它实现一个充当其类型的接口.
    
    在Java 1.5发行版本之前,实现Singleton有两种方法.这两种方法都要把构造器保持为私有的,并导出公有的静态成
    员,以便允许客户端能够访问该类的唯一实例.在第一种方法中,公有静态成员是个final域:
        
    ```java
    /**
     * Singleton with public final field
     *单例模式
     * @author bfy
     *
     */
    public class SingletonOne {
    
        public static final SingletonOne INSTANCE = new SingletonOne();
        
        private SingletonOne() {
    
        }
        
        public void leaveTheBuilding() {
            //TODO
        }
    
    }

-   私有构造器仅被调用一次,用来实例化公有的静态final域SingletonOne.INSTANCE.由于缺少公有的或受保护
    的构造器,所以保证了SingletonOne的全局唯一性:一旦SingletonOne被实例化,只会存在一个实例,不多不少.
    客户端的任何行为都不会改变这一点,但要提醒一点:享有特权的客户端可以借助AccessibleObject.setAccessible
    方法,通过反射机制调用私有构造器.如果需要抵御这种攻击,可以修改构造器,让它在被要求创建第二个实例的时候
    抛出异常.
    
-   在实现Singleton的第二种方法中,公有的成员是个静态工厂方法:

    ```java
    /**
     * Singleton的第二种方法中,公有的成员是个静态工厂方法
     * @author bfy
     * @date2018.3.13
     */
    public class SingletonTwo {
    
        private static final SingletonTwo INSTANCE = new SingletonTwo();
        private SingletonTwo() {
    
        }
    
        //静态工厂方法
        public static SingletonTwo getInstance() {
            return INSTANCE;
        }
    
        public void leaveTheBuilding() {
            //TODO
        }
    
    }
    
-   对于静态方法SingletonTwo.getInstance的所有调用,都会返回同一个对象引用,所以,永远
    不会创建其他的SingletonTwo实例(上述提醒依然适用);
    

-   公有域方法的主要好处在于,组成类的成员的声明很清楚的表明了这个类是一个Singleton:公有
    的静态域是final的,所以该域将总是包含相同的对象引用.公有域方法在性能上不再有任何优势:
    现代的JVM(java虚拟机,Java Virtual Machine)实现几乎都能将静态工厂方法的调用内联化.
    
-   工厂方法的优势之一在于,它提供了灵活性:在不改变其API的前提下,我们可以改变该类是否应该为
    Singleton的想法.工厂方法返回该类的唯一实例,但是,它可以很容易被修改,比如改成为每个调用
    该方法的线程返回一个唯一的实例.第二个优势与泛型有关.这些优势之间通常都不相关,public域
    (publci-field)的方法比较简单.
    
-   为了使利用这其中一种方法实现的Singleton类变成是可序列化的(Serializable),仅仅在声明
    所有实例域都是瞬时(transient)的,并提供一个readResolve方法.否则,每次反序列化一个序列
    化的实例时,都会创建一个新的实例比如说,在我们的例子中,会导致"假冒的SingletonOne~Two".为了防止
    这种情况,要在SingletonOne~Two类中加入下面这个readResolve方法:
    
    ```
        //readResolve method to preserve singleton property
    private Object readResolve() {
        // Return the one true SingletonOne~Two and let the garbage collector
        // take care of the SingletonOne~Two impersonator.
        return INSTANCE;
    }
           
-   从Java 1.5发行版本起,实现Singleton还有第三种方法.只需编写一个包含单个元素的枚举类型:
    ```java
    public enum SingletonThree {
    
        INSTANCE;
    
        public void leaveTheBuilding() {
            //TODO
            System.out.println("SingletonThree");
        }
    }

-   这种方法在功能上与公有域方法相近,但是它更加简洁,无偿地提供了序列化机制,绝对防止多次实例化,
    即使是在面对复杂的序列化或者反射攻击的时候.虽然这种方法还没广泛采用,但是单元素的枚举类型已经
    成为实现Singleton的最佳方法.
    

### 第4条:通过私有构造器强化不可实例化的能力
    url:/src/main/java/top/bfylu/utility_class
        
-    有一些简单的习惯用法可以确保类不可被实例化.由于只有当类不包含显式的构造器时,编译器才会生成
    缺省的构造器,因此我们只要让这个类包含私有构造器,它就不能被实例化了:
    
   ```java
    //Noninstantiable utility class
    public class UtilityClass {
        //Suppress default constructor for noninstantiability
        private UtilityClass() {
            throw new AssertionError();
        }
        
        //Remainder omitted
        
    }
```

-   由于显式的构造器是私有的,所以不可以在该类的外部访问它.AssertionError不是必需的,但是它可以避
    免不小心在类的内部调用构造器,它保证该类在任何情况都不会被实例化.这种习惯用法有点违背直觉,好像构
    造器就是专门设计成不能被调用一样.因此,明智的做法就是在代码中增加一条注释,如上所示.
    
-   这种习惯用法也有副作用,它使得一个类不能被子类化.所有的构造器都必需显式或隐式地调用超类(superclass)
    构造器,在这种情况下,子类就没有可访问的超类构造器可调用了.
    
        
### 第5条:避免创建不必要的对象
    url:/src/main/java/top/bfylu/create_fewer_objects
-   一般来说,最好能重用对象而不是在每次需要的时候就创建一个相同功能的新对象.重用方式既快速,又流行.
    如果对象是不可变的(immutable),它就始终可以被重用.
    
    作为一个极端的反面例子,考虑下面的语句:
    String s = new String("stringette"); // DON'T DO THIS!
    
-   该语句每次被执行的时候都创建一个新的Stirng实例,但是这些创建对象的动作全都是不必要的.传递给String构造
    器的参数("stringette")本身就是一个String实例,功能方面等同于构造器创建的所有对象.如果这种用法是在一
    个循环中,或者是在一个被频繁调用的方法中,就会创建出成千上万不必要的String实例.
    
    改进后的版本如下所示:
    
    String s = "stringstte"
    
    这个版本只用了一个String实例,而不是每次执行的时候都创建一个新的实例.而且,它可以保证,对于所有在同
    一台虚拟机中运行的代码,只要它们包含相同的字符串字面常量,该对象就会被重用.
    
    对于同时提供了静态工厂方法和构造器的不可改变,通常可以使用静态工厂方法而不是构造器,以避免创建不必要的
    对象.例如,静态工厂方法Boolean.valueOf(String)几乎总是优先于构造器Boolean(String).构造器在每次
    被调用的时候都会创建一个新的对象,而静态工厂方法则从不要求这样做,实际上也不会这样做.
    
    除了重用不可变的对象之外,也可以重用那些已知不会被修改的可变对象.下面是一个比较微妙,也比较常见的反面例
    子,其中涉及可变欣Date对象,它们的值一旦计算出来之后就不再变化.这个类建立了一个模型:其中有一个人,并有一
    个isBabyBoomer方法,用来检验这个人是否为一个""baby boomer(生育高峰期出生的小孩)",换句话说,就是检验
    这个人是否出生于1946年至1964年期间.
    
       ```java
        /**
         *  避免创建不必要的对象反面例子
         *  isBabyBoomer每次调用的时候,都会新建一个Calendar,一个TimeZone和两个Date实例.
         * @author bfy
         * @date 2018.3.15
         */
        public class PersonOne {
            private final Date birthDate;
        
        
            public PersonOne(Date birthDate) {
                this.birthDate = birthDate;
            }
        
        
            //Other fields, methods, and constructor omitted
            // DON'T DO THIS!
            public boolean isBabyBoomer() {
                Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                gmtCal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
                Date boomStart = gmtCal.getTime();
                gmtCal.set(1965, Calendar.JANUARY, 1, 0, 0, 0);
                Date boomEnd = gmtCal.getTime();
                return birthDate.compareTo(boomStart) >= 0 && birthDate.compareTo(boomEnd) < 0;
            }
        }

    
    
-   isBabyBoomer每次调用的时候,都会新建一个Calendar,一个TimeZone和两个Date实例.这是不必要的.下面版
    本用一个静态的初始化器(initializer),避免了这种效率低下的情况:
    ```java
    /**
     * 用一个静态的初始化器(initializer),避免了重复创建对象效率低下的情况
     * @author bfy
     * @date 2018.3.15
     */
    public class PersonTwo {
        private final Date birthDate;
        //Other fields, methods, and constructor omitted
        //其他字段，方法和构造函数被省略
    
        public PersonTwo(Date val) {
            this.birthDate = val;
        }
    
        /**
         * The starting and ending dates of the baby boom.
         * 婴儿潮的起止日期
         */
        private static final Date BOOM_START;
        private static final Date BOOM_END;
    
        static {
            Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            gmtCal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
            BOOM_START = gmtCal.getTime();
            gmtCal.set(1965, Calendar.JANUARY, 1, 0, 0, 0);
            BOOM_END = gmtCal.getTime();
        }
    
    
        public boolean isBabyBoomer() {
            return birthDate.compareTo(BOOM_START) >= 0 && birthDate.compareTo(BOOM_END) <0;
        }
    }


-   改进后的Person类只在初始化的时候创建Calendar, TimeZone和Date实例一次,而不是在每次调用isBabyBoomer
    的时候都创建这些实例.如果isBabyBoomer方法被频繁地调用,这种方法将会显著地提高性能.除了提高性能之外,代码
    的含义也更加清晰了.把boomStart和boomEnd从局部变量改为final静态域,这些日期显然是被作为常量对待,从而使
    得代码更易于理解.但是,这种优化带来的效果并不总是那么明显,因为Calendar实例的创建代价特别昂贵.
    
-   如果改进后的Person类被初始化了,它的isBabyBoomer方法却永远不会被调用,那就没有必要初始化BOOM_START和
    BOOM_END域通过延迟初始化(lazily initializing),即把对这些域的初始化延迟到isBabyBoomer方法第一次被
    调用的时候进行,则有可能消除这些不必要的初始化工作,但是不建议这样做.正如延迟初始化中常见的情况一样,这样做
    会使方法的实现更加复杂,从而无法将性能显著提高到超过已经达到的水平.
    
-   在Java 1.5发行版本中,有一种创建多余对象的新方法,称作自动装箱(autoboxing),它允许程序员将基本类型和装箱
    基本类型(Boxed Primitive Type) 混用,按需要自动装箱和拆箱.自动装箱使得基本类型和装箱基本类型之间的差别
    变得模糊起来,但是并没有完全消除.它们在语义上还有着微妙匆差别,在性能上也有着比较明显的差别.考虑下面的程序,
    它计算所有int正值的总和.为此程序必须使用long算法,因为int不够大,无法容纳所有int正值的总和:
    
    
    
   ```java
    /**
     * 自动装箱(autoboxing):一种创建多余对象的新方法.
     * Hideously slow program! Can you spot the object creation?
     *  @author bfy
     *  @date 2018.3.15
     */
    public class Autoboxing {
    
        //Hideously slow program! Can you spot the object creation?
    
        public static void main(String[] args) {
            Long sum = 0L;
            for (long i = 0; i < Integer.MAX_VALUE; i++) {
                sum += i;
            }
            
            System.out.println(sum);
        }
    }
```

-   这段程序算出的答案是正确的,但是比实际情况要更慢一些,只因打错了一个字符.变量sum被声明成Long,意味着程序构
    造了大约2^31个多余的Long实例(大约每次往Long sum中增加long时构造一个实例).将sum声明从Long改成long,在
    我的机器上使运行时间从43秒减少到6.8秒.结论很明显:要优先使用基本类型而不是装箱基本类型,要当心无意识的自动
    装箱.
    
-   不要错误地认为本条目所介绍的内容暗示着"创建对象的代价非常昂贵,我们应该要尽可能地避免创建对象".相反,由于小
    对象的构造器只做很少量的显式工作,所以,小对象的创建和回收动作是非常廉价的,特别是在现代的JVM实现上更是如此.
    通过创建附加的对象,提升程序的清晰性,简洁性和功能性,这通常是件好事.
    
-   反之,通过维护自己的对象池(object pool) 来避免创建对象并不是一种好的做法,除非池中的对象是非常重量级的.
    真正正确使用对象池的典型对象示例就是数据库连接池.
    
             
    
### 第6条:消除过期的对象引用(Expired object reference)
    url:/src/main/java/top/bfylu/expired_object_reference

-   当你从手工管理内存的语言(比如C或C++)转换的具有垃圾回收功能的语言的时候,程序员的工作会变得更加容易,因为
    当你用完了对象之后,它们会被自动回收.当你第一次经历对象回收功能的时候,会觉得这简直不可思议.这很容易给你留
    下这样的印象,认为自己不再需要考虑内存管理的事情了.其实不然.
    
    考虑下面这个简单的栈实现的例子:
    ```java
        //Can you spot the "memory leak" ?
    /**
     * Can you spot the "memory leak"?
     * @author bfy
     * @date 2018.3.15
     */
    public class Stack {
        private Object[] elements;
        private int size = 0;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;
    
        public Stack() {
            elements = new Object[DEFAULT_INITIAL_CAPACITY];
        }
    
        public void push(Object e) {
            ensureCapacity();
            elements[size++] = e;
        }
    
        public Object pop() {
            if (size == 0)
                throw new EmptyStackException();
            return elements[--size];
        }
    
    
        /**
         * Ensure space for at least one more element, roughly
         * doubling the capacity each time the array needs to grow.
         */
        private void ensureCapacity() {
            if (elements.length == size)
                elements = Arrays.copyOf(elements, 2 * size + 1);
        }
        
        
    }


-   这段程序中并没有很明显的错误.无论如何测试,它都会成功地通过每一项测试,但是这个程序中隐藏着一个问题.有一
    个"内存泄漏",随着垃圾回收器活动的增加,或者由于内存占用的不断增加,程序性能的降低会逐渐表现出来,在极端的
    情况下,这种内存泄漏会导致磁盘交换(Disk Paging),甚至导致程序失败(OutOfMemoryError错误),但是这种失
    败情形相对比较少见.
    
-   那么,程序中哪里发生了内存泄漏呢?如果一个栈先是增长,然后再收缩,那么,从栈中弹出来的对象将不会被当作垃圾
    回收,即使使用栈的程序不再引用这些对象,它们也不会被回收.这是因为,栈内部维护着对这些对象的过期引用
    (obsolete reference).所谓的过期引用,是指永远也不会再被解除的引用.
    
-   这类问题的修复方法很简单:一旦对象引用已经过期,只需清空这些引用即可.对于上述例子中的Stack类而言,只要一
    个单元被弹出栈,指向它的引用就过期了.pop方法的修订版本如下所示:
    
    ```
    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; //Eliminate obsolete reference 消除过期引用
        return result;
    }

-   清空过期引用的另一个好处是,如果它们以后又被错误地解除引用,程序就会立即抛出NullpointerException异常,
    而不是悄悄地错误运行下去.尽快地检测出程序中的错误总是有益的.
    
    
    
### 第7条:避免使用终结方法
    
    url:/src/main/java/top/bfylu/
    
-   终结方法(finalizer)通常是不可预测的,也是很危险换主,一般情况下是不必要的.使用终结方法会导致行为不稳定,
    降低性能,以及可移植性问题.当然,终结方法也有其可用之处,我们将在本条目的最后做介绍但是根据经验,应该避免使
    用终结方法.
    
-   在Java中,一般用try-finally块来完成类似的工作.终结方法的缺点在于不能保证会被及时地执行.从一个对象变得
    不可到达开始,到它的终结方法被执行,所花费的这段时间是任意长的.这意味着,注重时间(time-critical)的任务
    不应该由终结方法来完成.例如,用终结方法关闭已经打开的文件,这是严重错误,因为打开文件的描述符是一种很有限
    的资源.由于JVM会延迟执行终结方法,所以大量的文件会保留在打开状态,当一个程序再不能打开文件的时候,它可能
    会运行失败.
    
-   终结方法的缺点在于不能保证会被及时地执行.从一个对象变的不可达到开始,到它的终结方法被执行,所花费的这段时
    间是任意长的.这意味着,注重时间(time-critical)的任务不应该由终结方法来完成.
    
-   还有一点:使用终结方法有一个非常严重的(Server)性能损失.在我的机器上,创建和销毁一个简单对象的时间大约
    为5.6s.增加一个终结方法使时间增加到了2400ns.换句话说,用终结方法创建和销毁对象慢了大约430倍.
    
-   那么,如果类的对象中封装的资源(例如文件或者线程)确实需要终止,应该怎么做才能不用编写终结方法呢?只需提供一
    个的终止方法,并要求该类的客户端在每个实例不再有用的时候调用这个方法.值得提及的一个细节是,该实例必须记录下
    自己是否已经被终止了:显式的终止方法必须在一个私有域中记录下"该对象已经不再有效".如果这些方法是在对象已经
    终止之后被调用,其他的方法就必须检查这个域,并抛出IllegalStateException异常.
    
-   显式终止方法的典型例子是InputStream, OutputStream和java.sql.Connection上的close方法.另一个例子是
    java.util.Timer上的cancel方法,它执行必要的状态改变,使得与Timer实例相关联的该线程温和地终止自己.
    java.awt中的例子还包括Graphics.dispose和Window.dispose;这些方法通常由于性能不好而不被人们关注,一个
    相关的方法是Image.flush,它会释放所有与Image实例相关联的资源,但是该实例仍然处于可用的状态,如果有必要的话,
    会重新分配资源.
    
-   显式的终止方法通常与try-finally结构结合起来使用,以确保及时终止.在finally子句内部调用显式的终止方法,可以
    保证即使在使用对象的时候有异常抛出,该终止方法也会执行:
    
        
            
    
        
    
    
    
    

    
                                                                        
    
    
    
    


