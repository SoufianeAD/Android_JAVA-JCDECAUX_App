package android.esisa.projet.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Station implements Serializable {
    public enum Status {
        OPEN,
        CLOSE,
        UNKNOWN,
    }

    private int number;
    private String contract_name;
    private String name;
    private String address;
    private Position position;
    private boolean banking;
    private boolean bonus;
    private Status status;
    private int bike_stands;
    private int available_bike_stands;
    private int available_bike;

    public Station() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getContract_name() {
        return contract_name;
    }

    public void setContract_name(String contract_name) {
        this.contract_name = contract_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isBanking() {
        return banking;
    }

    public void setBanking(boolean banking) {
        this.banking = banking;
    }

    public boolean isBonus() {
        return bonus;
    }

    public void setBonus(boolean bonus) {
        this.bonus = bonus;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getBike_stands() {
        return bike_stands;
    }

    public void setBike_stands(int bike_stands) {
        this.bike_stands = bike_stands;
    }

    public int getAvailable_bike_stands() {
        return available_bike_stands;
    }

    public void setAvailable_bike_stands(int available_bike_stands) {
        this.available_bike_stands = available_bike_stands;
    }

    public int getAvailable_bike() {
        return available_bike;
    }

    public void setAvailable_bike(int available_bike) {
        this.available_bike = available_bike;
    }

}