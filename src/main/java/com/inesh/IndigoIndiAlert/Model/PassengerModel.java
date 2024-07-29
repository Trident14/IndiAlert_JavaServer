package com.inesh.IndigoIndiAlert.Model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "passengers")
public class PassengerModel {
    @Id
    private String id;
    private String name;
    private String flightNumber;
    private String Email;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getEmail() {
        return Email;
    }

    public String setEmail() {
        return Email;
    }


}