package android.esisa.projet.models;

import java.io.Serializable;
import java.util.List;

public class Contract implements Serializable {
    private String name;
    private String commercial_name;
    private List<String> cities;
    private String country_code;

    public Contract() {
    }

    public Contract(String name, String commercial_name, List<String> cities, String country_code) {
        this.name = name;
        this.commercial_name = commercial_name;
        this.cities = cities;
        this.country_code = country_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommercial_name() {
        return commercial_name;
    }

    public void setCommercial_name(String commercial_name) {
        this.commercial_name = commercial_name;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "name='" + name + '\'' +
                ", commercial_name='" + commercial_name + '\'' +
                ", cities=" + cities +
                ", country_code='" + country_code + '\'' +
                '}';
    }
}
