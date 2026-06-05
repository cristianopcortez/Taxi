# Integração da Feature: Tela de Rota no Mapa (RouteMapScreen)

## Contexto

A estrutura da feature de mapa de rota já existe no projeto (`RouteMapScreen`, `InternalRouteMapFeatureApi`), mas nunca foi registrada no fluxo de navegação do app. Este documento descreve o que precisa ser corrigido e implementado para torná-la funcional.

O fluxo esperado após a integração:

```
TravelRequestScreen → RiderOptionsScreen → RouteMapScreen (tela cheia)
```

A `RouteMapScreen` deve exibir o percurso completo entre origem e destino em tela cheia, com polylines, marcadores de início/fim e câmera centralizada na rota — complementando o mini-mapa já presente na `RiderOptionsScreen`.

---

## O que já existe

| Arquivo | Status |
|---|---|
| `RouteMapScreen.kt` | Existe, mas tem bugs (detalhes abaixo) |
| `InternalRouteMapFeatureApi.kt` | Existe, mas não está registrada |
| `RouteMapFeature` (constantes de rota) | Definido em `NavigationConstants.kt` |
| `decodePolyline()` | Implementada e funcional em `RouteMapScreen.kt` |

---

## Problemas a corrigir

### 1. Rota do nav graph conflita com outra feature

**Arquivo:** `InternalRouteMapFeatureApi.kt` — linha 22

O grafo usa `AvailableDriversFeature.nestedRoute` como `route`, que já é utilizado pela feature de `availableRidersApi`. Isso causaria crash na navegação.

```kotlin
// ERRADO — conflito com AvailableRidersApi
navGraphBuilder.navigation(
    startDestination = RouteMapFeature.taxiTravelOptionsScreenRoute,
    route = AvailableDriversFeature.nestedRoute  // ← deve ser RouteMapFeature.nestedRoute
)
```

**Correção:** trocar para `RouteMapFeature.nestedRoute`.

---

### 2. `RouteMapScreen` ignora os parâmetros recebidos e usa endereços hardcoded

**Arquivo:** `RouteMapScreen.kt` — linhas 25–28

```kotlin
// REMOVER — endereços fixos de São Paulo
viewModel.setQueryUserId("1")
viewModel.setQueryOriginAddress("Av. Brasil, 2033 - Jardim America, São Paulo - SP, 01431-001")
viewModel.setQueryDestinyAddress("Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200")
viewModel.setQuery("abcd")
```

**Correção:** a tela deve receber `userId`, `originAddress` e `destinyAddress` via argumentos de navegação (igual ao padrão já usado em `RiderOptionsScreen`) e passá-los ao ViewModel.

---

### 3. `RouteMapScreen` lê o state holder errado

**Arquivo:** `RouteMapScreen.kt` — linha 23

```kotlin
// ERRADO — routeResponse nunca é populado
val resultRiderRoutes = viewModel.routeResponse.value
```

O ViewModel popula `_combinedResponse` (via `getCombinedData`), não `_routeResponse`. A tela deve ler `viewModel.combinedResponse.value` e acessar `.data?.routeResponse`.

**Correção:**
```kotlin
val combinedResult = viewModel.combinedResponse.value
// usar combinedResult.data?.routeResponse
```

---

### 4. A rota de navegação não aceita os argumentos necessários

**Arquivo:** `NavigationConstants.kt`

A rota atual de `RouteMapFeature` é:
```kotlin
const val taxiTravelOptionsScreenRoute = "route_map/{id}"
```

Isso só aceita um argumento (`id`), mas precisamos de `userId`, `originAddress` e `destinyAddress` — seguindo o mesmo padrão de `RiderOptionsFeature`:

```kotlin
// Padrão atual de RiderOptionsFeature (referência)
const val taxiTravelOptionsScreenRoute = "rider_options/{userId}/{originAddress}/{destinyAddress}"
```

**Correção:** atualizar `RouteMapFeature.taxiTravelOptionsScreenRoute` para:
```kotlin
const val taxiTravelOptionsScreenRoute = "route_map/{userId}/{originAddress}/{destinyAddress}"
```

---

## Tarefas de implementação

### Passo 1 — Corrigir `NavigationConstants.kt`

Atualizar `RouteMapFeature` com os argumentos corretos:

```kotlin
object RouteMapFeature {
    const val nestedRoute = "route_map_nested_route"
    const val taxiTravelOptionsScreenRoute = "route_map/{userId}/{originAddress}/{destinyAddress}"
    const val deepLinkRoute = "taxi://route_map/{userId}/{originAddress}/{destinyAddress}"
}
```

---

### Passo 2 — Corrigir `InternalRouteMapFeatureApi.kt`

- Trocar `AvailableDriversFeature.nestedRoute` → `RouteMapFeature.nestedRoute`
- Extrair os 3 argumentos de navegação e passá-los à tela

```kotlin
internal object InternalRouteMapFeatureApi : FeatureApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation(
            startDestination = RouteMapFeature.taxiTravelOptionsScreenRoute,
            route = RouteMapFeature.nestedRoute  // corrigido
        ) {
            composable(
                RouteMapFeature.taxiTravelOptionsScreenRoute,
                deepLinks = listOf(navDeepLink { uriPattern = RouteMapFeature.deepLinkRoute })
            ) { backStackEntry ->
                val viewModel = hiltViewModel<RequestRideViewModel>()
                val userId = backStackEntry.arguments?.getString("userId").orEmpty()
                val originAddress = backStackEntry.arguments?.getString("originAddress").orEmpty()
                val destinyAddress = backStackEntry.arguments?.getString("destinyAddress").orEmpty()
                RouteMapScreen(userId, originAddress, destinyAddress, viewModel, navController)
            }
        }
    }
}
```

---

### Passo 3 — Reescrever `RouteMapScreen.kt`

- Receber `userId`, `originAddress`, `destinyAddress` como parâmetros
- Remover endereços hardcoded
- Chamar `viewModel.setQuery(userId, originAddress, destinyAddress)` com os valores recebidos
- Ler `viewModel.combinedResponse.value` em vez de `viewModel.routeResponse.value`
- Tratar os estados de loading e erro (seguir o padrão de `RiderOptionsScreen`)

Estrutura esperada da tela:

```
RouteMapScreen
├── Loading → CircularProgressIndicator centralizado
├── Error   → mensagem de erro + imagem
└── Success → MapView em tela cheia
               ├── Marcador de origem
               ├── Marcador de destino
               ├── Polylines do percurso (decodePolyline já implementado)
               └── Câmera centralizada nos bounds da rota
```

---

### Passo 4 — Criar `RouteMapApi` (interface pública da feature)

Seguindo o mesmo padrão de `RiderOptionsApi.kt`:

```kotlin
// navigation/RouteMapApi.kt
interface RouteMapApi : FeatureApi

class RouteMapApiImpl : RouteMapApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalRouteMapFeatureApi.registerGraph(navController, navGraphBuilder)
    }
}
```

---

### Passo 5 — Registrar em `UiModule.kt`

Adicionar o `@Provides` para `RouteMapApi`:

```kotlin
@Provides
fun provideRouteMapApi(): RouteMapApi {
    return RouteMapApiImpl()
}
```

---

### Passo 6 — Adicionar à `NavigationProvider`

```kotlin
data class NavigationProvider(
    val travelRequestFeatureApi: TravelRequestFeatureApi,
    val availableRidersApi: AvailableRidersApi,
    val riderOptionsApi: RiderOptionsApi,
    val tripHistoryApi: TripHistoryApi,
    val routeMapApi: RouteMapApi  // novo
)
```

Injetar também em `AppModule.kt`:

```kotlin
@Provides
fun provideNavigationProvider(
    travelRequestFeatureApi: TravelRequestFeatureApi,
    availableRidersApi: AvailableRidersApi,
    riderOptionsApi: RiderOptionsApi,
    tripHistoryApi: TripHistoryApi,
    routeMapApi: RouteMapApi  // novo
): NavigationProvider {
    return NavigationProvider(
        travelRequestFeatureApi,
        availableRidersApi,
        riderOptionsApi,
        tripHistoryApi,
        routeMapApi
    )
}
```

---

### Passo 7 — Registrar no `AppNavGraph.kt`

```kotlin
NavHost(...) {
    navigationProvider.travelRequestFeatureApi.registerGraph(navController, this)
    navigationProvider.availableRidersApi.registerGraph(navController, this)
    navigationProvider.riderOptionsApi.registerGraph(navController, this)
    navigationProvider.tripHistoryApi.registerGraph(navController, this)
    navigationProvider.routeMapApi.registerGraph(navController, this)  // novo
}
```

---

### Passo 8 — Adicionar botão de navegação na `RiderOptionsScreen`

Adicionar um botão "Ver Rota Completa" (ou similar) que navega para `RouteMapScreen` passando os endereços:

```kotlin
val route = RouteMapFeature.taxiTravelOptionsScreenRoute
    .replace("{userId}", userId)
    .replace("{originAddress}", originAddress)
    .replace("{destinyAddress}", destinyAddress)

navController.navigate(route)
```

> **Sugestão de UX:** posicionar o botão próximo ao mini-mapa já existente na `RiderOptionsScreen`.

---

## Observações finais

- O `decodePolyline()` em `RouteMapScreen.kt` está correto e pode ser mantido como está.
- Após a integração, os arquivos `BasicMapActivity.kt` e `StepGoogleMapsView.kt` (com as variáveis de coordenadas de Singapura) podem ser removidos ou isolados — eles são código de amostra do SDK que vazou para a produção.
- Considerar URL-encode/decode nos argumentos de endereço na navegação, pois endereços contêm espaços, vírgulas e caracteres especiais que podem quebrar a rota. Ver como `InternalRiderOptionsFeatureApi` lida com isso atualmente e seguir o mesmo padrão.
