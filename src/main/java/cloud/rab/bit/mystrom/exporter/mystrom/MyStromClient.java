package cloud.rab.bit.mystrom.exporter.mystrom;

import cloud.rab.bit.mystrom.exporter.mystrom.entities.MyStromResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
public class MyStromClient {

    private final WebClient webClient;

    public MyStromClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<MyStromResponse> getSwitchData(String switchAddress) {
        return webClient.get()
                .uri(URI.create("http://" + switchAddress + "/report"))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().isError()) {
                        log.warn("SwitchData: {}", clientResponse.statusCode());
                    } else {
                        log.info("SwitchData: {}", clientResponse.statusCode());
                    }
                    return clientResponse.bodyToMono(MyStromResponse.class);
                });
    }

}
