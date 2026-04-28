package br.com.ccortez.feature.taxi_travel_options.domain.model

data class RiderOption(
    val id: Int,
    val nome: String,
    val descricao: String,
    val veiculo: String,
    val availacao: Int,
    val valorDaViagem: Double
)
