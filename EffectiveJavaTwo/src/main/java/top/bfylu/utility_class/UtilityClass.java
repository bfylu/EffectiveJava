package top.bfylu.utility_class;

/**
 * Noninstantiable utility class
 *
 * @author bfy
 * @date 2018.3.14
 */
public class UtilityClass {
    // Suppress default constructor for noninstantiability

    private UtilityClass() {
        throw new AssertionError();
    }

    //  Remainder omitted(剩余部分省略)

    public static UtilityClass getInstance() {
        return new UtilityClass();
    }
}
