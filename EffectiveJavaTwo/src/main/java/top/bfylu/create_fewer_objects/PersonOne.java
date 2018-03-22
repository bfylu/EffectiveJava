package top.bfylu.create_fewer_objects;


import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
