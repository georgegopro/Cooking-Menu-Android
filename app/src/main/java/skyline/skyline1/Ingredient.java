package skyline.skyline1;

import java.util.Comparator;



public class Ingredient  implements java.io.Serializable{
    private String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static Comparator<Ingredient> COMPARE_BY_INGREDIENT = new Comparator<Ingredient>() {
        public int compare(Ingredient one, Ingredient other) {
            return one.getName().compareTo(other.getName());
        }
    };

}


