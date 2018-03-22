package top.bfylu.singleton;

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

    /**让Singleton变成可序列化的
     * 为了让反序列化的时候只创建一个对象
     *readResolve method to preserve singleton property
     *readResolve方法保留单例属性
     * @return
     */
    private Object readResolve() {
        // Return the one true SingletonOne~Two and let the garbage collector
        //返回一个真正的SingletonOne〜Two并让垃圾收集器
        // take care of the SingletonOne~Two impersonator.
        //照顾SingletonOne〜Two模仿者
        return INSTANCE;
    }

    public void leaveTheBuilding() {
        //TODO
        System.out.println("SingletonTwo");
    }

}
