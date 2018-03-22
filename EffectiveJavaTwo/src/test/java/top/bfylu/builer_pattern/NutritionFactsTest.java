package top.bfylu.builer_pattern;


import org.junit.Test;
import top.bfylu.builder_pattern.NutritionFacts;
import top.bfylu.builder_pattern.NutritionFactsThree;
import top.bfylu.builder_pattern.NutritionFactsTwo;

/**
 *
 * @author bfy
 *
 */
public class NutritionFactsTest {

    //重叠构造器
    @Test
    public void one(){
        NutritionFacts cocaCola = new NutritionFacts
                (240, 8, 100, 0, 35, 27);
    }

    //JavaBeans模式
    @Test
    public void Two(){
        NutritionFactsTwo cocaCola = new NutritionFactsTwo();
        cocaCola.setServingSize(240);
        cocaCola.setServings(8);
        cocaCola.setCalories(100);
        cocaCola.setSodium(35);
        cocaCola.setCarbohydrate(27);
    }

    //Builder模式的一种形式
    @Test
    public void Three(){
        NutritionFactsThree cocaCola = new NutritionFactsThree.Builder(240, 8)
                .calories(100)
                .sodium(35)
                .carbohydrate(27)
                .build();
    }
}
