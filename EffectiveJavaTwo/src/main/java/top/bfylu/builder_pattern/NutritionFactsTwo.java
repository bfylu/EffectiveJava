package top.bfylu.builder_pattern;

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

    public void setServingSize(int val)     { servingSize = val; }
    public void setServings(int val)         { servings = val; }
    public void setCalories(int val)        { calories = val; }
    public void setFat(int val)             { fat = val; }
    public void setSodium(int val)          { sodium = val; }
    public void setCarbohydrate(int val)   { carbohydrate = val;}

}


