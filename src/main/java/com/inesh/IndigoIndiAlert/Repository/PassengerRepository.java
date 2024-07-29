package com.inesh.IndigoIndiAlert.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.inesh.IndigoIndiAlert.Model.PassengerModel;



public interface PassengerRepository extends MongoRepository<PassengerModel, String> {
    List<PassengerModel> findByFlightNumber(String flightNumber);
}