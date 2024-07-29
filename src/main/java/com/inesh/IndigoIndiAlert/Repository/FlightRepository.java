package com.inesh.IndigoIndiAlert.Repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.inesh.IndigoIndiAlert.Model.FlightModel;

@Repository
public interface FlightRepository extends MongoRepository<FlightModel, String> {
    FlightModel findByFlightNumber(String flightNumber);
}