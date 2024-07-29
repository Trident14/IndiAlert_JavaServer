package com.inesh.IndigoIndiAlert.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.inesh.IndigoIndiAlert.Model.FlightModel;
import com.inesh.IndigoIndiAlert.Service.FlightService;




@RestController
@CrossOrigin
public class FlightController {

    @Autowired 
    private FlightService flightService;

   

    @GetMapping("/")
    public String hello() {
        System.out.println("helloooo");
        return "Hello from server";
    }

    @GetMapping("/allFlights")
    public ResponseEntity<List<FlightModel>> getAllFlights() {
        List<FlightModel> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<FlightModel> getFlightByNumber(@PathVariable String flightNumber) {
        FlightModel flight = flightService.getFlightByNumber(flightNumber);
        if (flight != null) {
            return ResponseEntity.ok(flight);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // @PutMapping("/{flightNumber}")
    // public ResponseEntity<FlightModel> updateFlightStatus(@PathVariable String flightNumber, @RequestBody FlightModel flightDetails) {
    //     FlightModel updatedFlight = flightService.updateFlightStatus(flightNumber, flightDetails);
    //     if (updatedFlight != null) {
    //         return ResponseEntity.ok(updatedFlight);
    //     } else {
    //         return ResponseEntity.notFound().build();
    //     }
    // }

    @PostMapping("/addFlight")
    public ResponseEntity<String> addFlight(@RequestBody FlightModel flightDetails) {
        boolean isAdded = flightService.addFlight(flightDetails);
        if (isAdded) {
            return ResponseEntity.ok("Flight added successfully");
        } else {
            return ResponseEntity.status(409).body("Flight already exists");
        }
    }

    


    @PutMapping("/updateFlight")
    public ResponseEntity<String> updateFlight(@RequestBody FlightModel flightDetails) {
       boolean isUpdated = flightService.updateDetails(flightDetails);
        
       if (isUpdated) {
            return ResponseEntity.ok("Flight updated successfully");
        } else {
            return ResponseEntity.status(409).body("Flight details do not exist");
        }
    }
}
