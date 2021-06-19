package cloud.rab.bit.mystrom.exporter.properties;

import cloud.rab.bit.mystrom.exporter.mystrom.MyStromTarget;
import cloud.rab.bit.mystrom.exporter.services.entities.TariffPeriod;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties("mystrom")
public class MyStromProperties {

    private List<MyStromTarget> targets = new ArrayList<>();
    private String timezone;
    private DayOfWeek offpeakDay;
    private Integer offpeakStartHour;
    private Integer offpeakStartMinute;
    private Integer offpeakEndHour;
    private Integer offpeakEndMinute;


    public TariffPeriod getCurrentTariffPeriod(Instant atInstant) {

        ZoneId zone;
        if (timezone != null) {
            zone = ZoneId.of(timezone);
        } else {
            zone = ZoneId.systemDefault();
        }
        ZonedDateTime dateTime = atInstant.atZone(zone);

        if (dateTime.getDayOfWeek().equals(offpeakDay)) {
            return TariffPeriod.offPeak;
        }

        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();

        if (offpeakStartHour != null) {
            if (offpeakStartMinute != null) {
                if (hour > offpeakStartHour || (hour == offpeakStartHour && minute > offpeakStartMinute)) {
                    return TariffPeriod.offPeak;
                }
            } else if (hour >= offpeakStartHour) {
                return TariffPeriod.offPeak;
            }
        }

        if (offpeakEndHour != null) {
            if (offpeakEndMinute != null) {
                if (hour < offpeakEndHour || (hour == offpeakEndHour && minute < offpeakEndMinute)) {
                    return TariffPeriod.offPeak;
                }
            } else if (hour < offpeakEndHour) {
                return TariffPeriod.offPeak;
            }
        }

        return TariffPeriod.peak;
    }

}
