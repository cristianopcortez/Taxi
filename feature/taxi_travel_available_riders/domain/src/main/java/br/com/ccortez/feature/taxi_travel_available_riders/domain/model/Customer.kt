package br.com.ccortez.feature.taxi_travel_available_riders.domain.model

data class Customer(
    val customerId: String,
    val rides: List<Ride>
)