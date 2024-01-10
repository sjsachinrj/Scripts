import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public class MyApiClient {

    private final WebClient webClient;

    public MyApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.example.com").build();
    }

    public Mono<String> makeThreeApiCalls() {
        // First API call
        Mono<String> responseA = webClient.get().uri("/api/endpointA").retrieve().bodyToMono(String.class);

        // Second API call using responseA
        Mono<Set<String>> responseBSet = responseA.flatMapMany(this::extractValuesAndMakeMultipleCalls)
                .collectSet();

        // Third parallel API calls based on values from responseBSet
        Flux<String> parallelApiCalls = responseBSet.flatMapMany(value -> makeParallelApiCalls(value, responseA));

        // You can further process the results or return the Flux directly
        return parallelApiCalls.collectList().map(list -> "Final result: " + list);
    }

    private Flux<String> extractValuesAndMakeMultipleCalls(String responseA) {
        // Extract values and make multiple API calls
        // Assuming you have a method that returns a Flux<String> for multiple calls
        return webClient.get().uri("/api/endpointB/{value}", responseA)
                .retrieve()
                .bodyToFlux(String.class);
    }

    private Flux<String> makeParallelApiCalls(String valueFromResponseB, Mono<String> responseA) {
        // Make parallel API calls based on the values
        // You can add the valueFromResponseB to the header of the request
        return webClient.get().uri("/api/endpointC")
                .header(HttpHeaders.AUTHORIZATION, valueFromResponseB) // Adding value to header
                .retrieve()
                .bodyToFlux(String.class);
    }
}
