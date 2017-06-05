package skyline.skyline1;

import java.util.Comparator;


public class Category implements java.io.Serializable{
    private String name;

    public Category(String name) {
        this.name = name;
    } //Construction to set name

    public String getName() {
        return name;
    } //return names

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    } //to String Method

    public static Comparator<Category> COMPARE_BY_CATEGORY = new Comparator<Category>() {
        public int compare(Category one, Category other) {
            return one.getName().compareTo(other.getName());
        }
    };}

