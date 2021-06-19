package cloud.rab.bit.mystrom.exporter.services;

import cloud.rab.bit.mystrom.exporter.mystrom.MyStromClient;
import cloud.rab.bit.mystrom.exporter.properties.MyStromProperties;
import cloud.rab.bit.mystrom.exporter.services.entities.Metric;
import cloud.rab.bit.mystrom.exporter.services.entities.TariffPeriod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyStromService {

    private final MyStromClient mystromClient;
    private final MyStromProperties myStromProperties;

    @EventListener(ApplicationStartedEvent.class)
    public void printConfiguration() {
        log.info("Picked up MyStrom configuration: {}", myStromProperties);
    }

    public Mono<List<Metric>> getCurrentValues() {

        TariffPeriod tariffPeriod = myStromProperties.getCurrentTariffPeriod(Instant.now());
        return Flux.fromIterable(myStromProperties.getTargets())
                .flatMap(target -> mystromClient.getSwitchData(target.getAddress())
                        .flatMapIterable(response -> {
                            Map<String, String> deviceLabels = Map.of("switchAddress", target.getAddress(), "switchName", target.getAlias());
                            Metric current = createMetric("mystrom_power_watt", response::getPower, deviceLabels, Map.of("tariffPeriod", tariffPeriod.getLabel()));
                            Metric inverse = createMetric("mystrom_power_watt", () -> 0, deviceLabels, Map.of("tariffPeriod", tariffPeriod.getInverseLabel()));
                            return List.of(current, inverse);
                        }))
                .collectList();
    }

    private Metric createMetric(String metricName, Supplier<Number> valueSupplier, Map<String, String> labels, Map<String, String> additionalLabels) {
        Map<String, String> metricLabels = new TreeMap<>();
        metricLabels.putAll(labels);
        metricLabels.putAll(additionalLabels);

        return Metric.builder()
                .name(metricName)
                .type("gauge")
                .value(valueSupplier.get())
                .labels(metricLabels)
                .build();
    }

}
