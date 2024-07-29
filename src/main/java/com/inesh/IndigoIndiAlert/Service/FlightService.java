package com.inesh.IndigoIndiAlert.Service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.inesh.IndigoIndiAlert.Model.FlightModel;
import com.inesh.IndigoIndiAlert.Model.PassengerModel;
import com.inesh.IndigoIndiAlert.Repository.FlightRepository;
import com.inesh.IndigoIndiAlert.Repository.PassengerRepository;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private EmailService emailService;

    private static final String TOPIC = "flightUpdate";

    public List<FlightModel> getAllFlights() {
        return flightRepository.findAll();
    }

    public FlightModel getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber);
    }

    public boolean addFlight(FlightModel flightDetails) {
        FlightModel existingFlight = flightRepository.findByFlightNumber(flightDetails.getFlightNumber());
        if (existingFlight != null) {
            return false; // Flight already exists
        } else {
            flightRepository.save(flightDetails);
            return true; // Flight added successfully
        }
    }

    public boolean checkIfFlightStatusChanged(FlightModel flight) {
        FlightModel.FlightState previous = flight.getPreviousState();
        if (previous == null) {
            return true; // If no previous state, assume it's a change
        }

        boolean hasChanged = !Objects.equals(flight.getStatus(), previous.getStatus())
            || !Objects.equals(flight.getDepartureTime(), previous.getDepartureTime())
            || !Objects.equals(flight.getArrivalTime(), previous.getArrivalTime())
            || !Objects.equals(flight.getDestinationCity(), previous.getDestinationCity())
            || !Objects.equals(flight.getOriginCity(), previous.getOriginCity())
            || !Objects.equals(flight.getGate(), previous.getGate());

        return hasChanged;
    }

    public void handleFlightUpdate(FlightModel flight) {
        // Ensure previous state is set or handle as a special case
        if (flight.getPreviousState() == null) {
            flight.setPreviousState(new FlightModel.FlightState(flight)); // Initialize previous state
        }

        boolean hasChanged = checkIfFlightStatusChanged(flight);

        if (hasChanged) {
            String message = buildMessage(flight);
            try {
                sendUpdateToKafka(flight, message);
                notifyPassengers(flight, message);
            } catch (Exception e) {
                System.err.println("Error notifying passengers: " + e.getMessage());
                e.printStackTrace();
            } finally {
                flight.setPreviousState(new FlightModel.FlightState(flight));
                flightRepository.save(flight);
            }
        }
    }

    public boolean updateDetails(FlightModel flightDetails) {
        FlightModel existingFlight = flightRepository.findByFlightNumber(flightDetails.getFlightNumber());
        if (existingFlight != null) {
            // Save the current state to previousState
            existingFlight.setPreviousState(new FlightModel.FlightState(existingFlight));

            // Update the flight with the new details
            existingFlight.setStatus(flightDetails.getStatus());
            existingFlight.setDepartureTime(flightDetails.getDepartureTime());
            existingFlight.setArrivalTime(flightDetails.getArrivalTime());
            existingFlight.setGate(flightDetails.getGate());
            existingFlight.setDestinationCity(flightDetails.getDestinationCity());
            existingFlight.setOriginCity(flightDetails.getOriginCity());
            flightRepository.save(existingFlight);
            String message = buildMessage(existingFlight);
            sendUpdateToKafka(existingFlight, message);

            return true;
        } else {
            return false; // Flight does not exist
        }
    }

    public void sendUpdateToKafka(FlightModel flight, String message) {
        try {
            if (!message.isEmpty()) {
                kafkaTemplate.send(TOPIC, message).get(); // Ensure message is sent before proceeding
            }
        } catch (Exception e) {
            System.err.println("Failed to send message to Kafka: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String buildMessage(FlightModel flight) {
        StringBuilder message = new StringBuilder();

        // Include flight number in the message
        String flightNumber = flight.getFlightNumber();
        message.append("Flight ").append(flightNumber).append(": ");

        // Check if the status has changed
        if (flight.getStatus() != null && !flight.getStatus().equals(flight.getPreviousState().getStatus())) {
            message.append("status changed to ").append(flight.getStatus()).append(". ");
        }

        // Check if the departure time has changed
        if (flight.getDepartureTime() != null && !flight.getDepartureTime().equals(flight.getPreviousState().getDepartureTime())) {
            message.append("departure time updated to ").append(flight.getDepartureTime()).append(". ");
        }

        // Check if the arrival time has changed
        if (flight.getArrivalTime() != null && !flight.getArrivalTime().equals(flight.getPreviousState().getArrivalTime())) {
            message.append("arrival time updated to ").append(flight.getArrivalTime()).append(". ");
        }

        // Check if the destination city has changed
        if (flight.getDestinationCity() != null && !flight.getDestinationCity().equals(flight.getPreviousState().getDestinationCity())) {
            message.append("destination city updated to ").append(flight.getDestinationCity()).append(". ");
        }

        // Check if the origin city has changed
        if (flight.getOriginCity() != null && !flight.getOriginCity().equals(flight.getPreviousState().getOriginCity())) {
            message.append("origin city updated to ").append(flight.getOriginCity()).append(". ");
        }

        // Check if the gate has changed
        if (flight.getGate() != null && !flight.getGate().equals(flight.getPreviousState().getGate())) {
            message.append("gate changed to ").append(flight.getGate()).append(". ");
        }

        return message.toString().trim();
    }

    public void notifyPassengers(FlightModel flight, String message) {
        List<PassengerModel> passengers = passengerRepository.findByFlightNumber(flight.getFlightNumber());
        for (PassengerModel passenger : passengers) {
            String subject = "Flight Update: " + flight.getFlightNumber();
            String recipientEmail = passenger.getEmail(); // Assuming PassengerModel has getEmail() method

            if (recipientEmail != null && !recipientEmail.isEmpty()) {
                emailService.sendEmail(recipientEmail, subject, message);
                System.out.println("Notifying passenger " + passenger.getName() + " about flight " + flight.getFlightNumber() + " via email.");
            } else {
                System.out.println("Passenger " + passenger.getName() + " has no email address. Cannot send notification.");
            }
        }
    }
}
