package com.inesh.IndigoIndiAlert.Model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "flights")
public class FlightModel {

    @Id
    private String id;
    private String flightNumber;
    private String status;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String destinationCity;
    private String originCity;
    private String gate;
    private FlightState previousState; // Nested object

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDepartureTime() { // Correct spelling
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) { // Correct spelling
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public FlightState getPreviousState() {
        return previousState;
    }

    public void setPreviousState(FlightState previousState) {
        this.previousState = previousState;
    }

    // Nested class for previous state
    public static class FlightState {
        private String status;
        private LocalDateTime departureTime; // Correct spelling
        private LocalDateTime arrivalTime;
        private String destinationCity;
        private String originCity;
        private String gate;

        public FlightState() {}
        public FlightState(FlightModel flight) {
            this.status = flight.getStatus();
            this.departureTime = flight.getDepartureTime();
            this.arrivalTime = flight.getArrivalTime();
            this.destinationCity = flight.getDestinationCity();
            this.originCity = flight.getOriginCity();
            this.gate = flight.getGate();
        }


        // Getters and Setters
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getDepartureTime() { // Correct spelling
            return departureTime;
        }

        public void setDepartureTime(LocalDateTime departureTime) { // Correct spelling
            this.departureTime = departureTime;
        }

        public LocalDateTime getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(LocalDateTime arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public String getDestinationCity() {
            return destinationCity;
        }

        public void setDestinationCity(String destinationCity) {
            this.destinationCity = destinationCity;
        }

        public String getOriginCity() {
            return originCity;
        }

        public void setOriginCity(String originCity) {
            this.originCity = originCity;
        }

        public String getGate() {
            return gate;
        }

        public void setGate(String gate) {
            this.gate = gate;
        }
    }
}
