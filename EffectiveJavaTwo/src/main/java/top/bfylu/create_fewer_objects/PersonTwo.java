package top.bfylu.create_fewer_objects;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
