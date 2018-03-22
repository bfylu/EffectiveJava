package top.bfylu.builder_pattern;

/**
 * Telescoping constructor pattern - does not scale well!
 * Telescoping构造函数模式 - 不能很好地扩展！
 */
public class NutritionFacts {
    private final int servingSize;      //(mL)              required
    private final int serings;          //(per container)   required
    private final int calories;         //                  optional
    private final int fat;              //(g)               optional
    private final int sodium;           //(mg)              optional
    private final int carbohydrate;     //(g)               optional

    public NutritionFacts(int servingSize, int serings) {
        this(servingSize, serings, 0);
    }

    public NutritionFacts(int servingSize, int serings, int calories) {
        this(servingSize, serings, calories, 0);
    }

    public NutritionFacts(int servingSize, int serings, int calories, int fat) {
        this(servingSize, serings, calories, fat, 0);
    }

    public NutritionFacts(int servingSize, int serings, int calories, int fat, int sodium) {
        this(servingSize, serings, calories, fat, sodium, 0);
    }

    public NutritionFacts(int servingSize, int serings, int calories, int fat, int sodium, int carbohydrate){
        this.servingSize    = servingSize;
        this.serings        = serings;
        this.calories       = calories;
        this.fat            = fat;
        this.sodium         = sodium;
        this.carbohydrate   = carbohydrate;
    }
}


