package by.bsuir.wildboom.lab2;

import java.io.Serializable;

public class Item implements Serializable {
    public Integer length;
    public String imageName;
    public String name;

    public Item(String name, int length)
    {
        this.length = length;
        this.imageName = null;
        this.name = name;
    }
}
