package top.bfylu.singleton;

/**
 * Singleton with public final field
 * 显然单例模式的要点有三个；
 * 一是某个类只能有一个实例；
 * 二是它必须自行创建这个实例；
 * 三是它必须自行向整个系统提供这个实例。
 *单例模式
 * @author bfy
 *
 */
public class SingletonOne {

    public static final SingletonOne INSTANCE = new SingletonOne();

    private SingletonOne() {

    }

    //让Singleton变成可序列化的
    //为了让反序列化的时候只创建一个对象
    //readResolve method to preserve singleton property
    private Object readResolve() {
        // Return the one true SingletonOne~Two and let the garbage collector
        // take care of the SingletonOne~Two impersonator.
        return INSTANCE;
    }

    public void leaveTheBuilding() {
        System.out.println("SingletonOne");
    }

}
