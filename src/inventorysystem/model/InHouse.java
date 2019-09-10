/*
 * Program: InHouse.java
 * Author: Davis Nguyen
 * Date: 8/24/2019 created
 *
 */
package inventorysystem.model;

import javafx.beans.property.SimpleIntegerProperty;

public class InHouse extends Part {

    //instantiating variable
    private SimpleIntegerProperty machineId;

    //default constructor
    public InHouse() {
    }

    //constructor with parameters
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = new SimpleIntegerProperty(machineId);
    }

    //setters and getters
    public void setmachineId(int machineId) {
        this.machineId = new SimpleIntegerProperty(machineId);
    }

    public int getmachineId() {
        return machineId.get();
    }
}
