package top.bfylu.singleton;

import org.junit.Test;

/**
 * 单例模式测试
 */
public class SingletonTest {

    @Test
    public void SingletonOneTest() {
        SingletonOne singletonOne = SingletonOne.INSTANCE;
        singletonOne.leaveTheBuilding();
    }

    @Test
    public void SingletonTwoTest() {
        SingletonTwo singletonTwo = SingletonTwo.getInstance();
        singletonTwo.leaveTheBuilding();
    }

    @Test
    public void SingletonThreeTest() {
        SingletonThree singletonThree = SingletonThree.INSTANCE;
        singletonThree.leaveTheBuilding();
    }
}
