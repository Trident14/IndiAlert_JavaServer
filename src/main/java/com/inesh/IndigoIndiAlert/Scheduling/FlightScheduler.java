package com.inesh.IndigoIndiAlert.Scheduling;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inesh.IndigoIndiAlert.Model.FlightModel;
import com.inesh.IndigoIndiAlert.Service.FlightService;

@Component
public class FlightScheduler {

    @Autowired
    private FlightService flightService;

    @Scheduled(fixedRate = 5000) // Fetch data every 30 seconds
    public void fetchAndCheckForUpdates() {
        List<FlightModel> flights = flightService.getAllFlights();
        
        for (FlightModel flight : flights) {
          flightService.handleFlightUpdate(flight);
        }
    }
}