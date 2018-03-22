package top.bfylu.create_fewer_objects;

import org.junit.Test;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 避免创建不必要的对象测试
 *
 */
public class Person {

    @Test
    public void personOne() {

        String strDate ="1950-2-12 21:20:2" ;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(strDate, pos);
        PersonOne personOne = new PersonOne(date);
        for (int i = 0; i <= 10000000; i++) {
            personOne.isBabyBoomer();
            System.out.println("这个人是否出生于1946年至1964年期间:" + personOne.isBabyBoomer());
        }
        //调用一千万次,需要1m2s381ms
    }

    @Test
    public void personTwo() {
        String strDate ="1951-2-12 21:20:2" ;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(strDate, pos);
        PersonTwo personTwo = new PersonTwo(date);
        for (int i = 0; i <= 10000000; i++) {
            personTwo.isBabyBoomer();
            System.out.println("这个人是否出生于1946年至1964年期间:" + personTwo.isBabyBoomer());
        }
        //调用一千万次,需要
    }
}
