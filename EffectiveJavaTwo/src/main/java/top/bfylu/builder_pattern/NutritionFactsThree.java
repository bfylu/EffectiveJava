package top.bfylu.builder_pattern;


/**
 * Builder Pattern
 * @author bfy
 * @date 2018.3.13
 */
public class NutritionFactsThree {

    private final int servingSize;
    private final int servings;
    private final int calorier;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;


    public static class Builder {
        //Required parameters
        //必需的参数
        private final int servingSize;
        private final int servings;

        //Optional parameters - initialized to default values
        //可选参数 - 初始化为默认值
        private int calories        = 0;
        private int fat             = 0;
        private int carbohydrate    = 0;
        private int sodium          = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings    = servings;
        }

        public Builder calories(int val) {
            calories = val;
            return this;
        }
        public Builder fat(int val) {
            fat = val;
            return this;
        }
        public Builder carbohydrate(int var) {
            carbohydrate = var;
            return this;
        }
        public Builder sodium(int var) {
            sodium = var;
            return this;
        }

        public NutritionFactsThree build() {
            return  new NutritionFactsThree(this);
        }
    }

    public NutritionFactsThree(Builder builder) {
        servingSize     = builder.servingSize;
        servings        = builder.servings;
        calorier        = builder.calories;
        fat             = builder.fat;
        sodium          = builder.sodium;
        carbohydrate    = builder.carbohydrate;
    }
}
