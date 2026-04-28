package br.com.ccortez.feature.taxi_travel_available_riders.data.mapper

import br.com.ccortez.core.network.model.CustomerResponse
import br.com.ccortez.core.network.model.DriverResponse
import br.com.ccortez.feature.taxi_travel_available_riders.domain.model.Customer
import br.com.ccortez.feature.taxi_travel_available_riders.domain.model.Driver
import br.com.ccortez.feature.taxi_travel_available_riders.domain.model.Ride

fun CustomerResponse.toCustomer(): Customer {
    return Customer(
        customerId = customerId,
        rides = rides.map { rideResponse ->
            Ride(
                id = rideResponse.id,
                date = rideResponse.date,
                origin = rideResponse.origin,
                destination = rideResponse.destination,
                distance = rideResponse.distance,
                duration = rideResponse.duration,
                driver = rideResponse.driver.toDriver(),
                value = rideResponse.value
            )
        }
    )
}

fun DriverResponse.toDriver(): Driver {
    return Driver(
        id = id,
        name = name
    )
}