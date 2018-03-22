package top.bfylu.create_fewer_objects;

/**
 * 自动装箱(autoboxing):一种创建多余对象的新方法.
 *
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
