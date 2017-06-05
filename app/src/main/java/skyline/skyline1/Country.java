package skyline.skyline1;

import java.util.Comparator;



public class Country implements java.io.Serializable{

    private String name;

    public Country(String name) {
        this.name = name;
    } //Construction

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static Comparator<Country> COMPARE_BY_ORIGIN = new Comparator<Country>() {
        public int compare(Country one, Country other) {
            return one.getName().compareTo(other.getName());
        }
    };}

