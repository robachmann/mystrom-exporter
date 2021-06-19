package cloud.rab.bit.mystrom.exporter.properties;

import cloud.rab.bit.mystrom.exporter.services.entities.TariffPeriod;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyStromPropertiesTest {

    @Test
    void getCurrentTariffPeriod() {

        MyStromProperties myStromProperties = new MyStromProperties();
        myStromProperties.setOffpeakDay(DayOfWeek.SUNDAY);
        myStromProperties.setOffpeakStartHour(22);
        myStromProperties.setOffpeakStartMinute(0);
        myStromProperties.setOffpeakEndHour(7);
        myStromProperties.setOffpeakEndMinute(0);
        myStromProperties.setTimezone("Europe/Zurich");

        checkTariffPeriod(LocalDateTime.of(2021, 6, 1, 6, 45, 0), TariffPeriod.offPeak, myStromProperties);
        checkTariffPeriod(LocalDateTime.of(2021, 6, 1, 6, 59, 0), TariffPeriod.offPeak, myStromProperties);
        checkTariffPeriod(LocalDateTime.of(2021, 6, 1, 7, 0, 0), TariffPeriod.peak, myStromProperties);
        checkTariffPeriod(LocalDateTime.of(2021, 6, 1, 7, 1, 0), TariffPeriod.peak, myStromProperties);
        checkTariffPeriod(LocalDateTime.of(2021, 6, 1, 7, 6, 0), TariffPeriod.peak, myStromProperties);
        checkTariffPeriod(LocalDateTime.of(2021, 6, 1, 7, 11, 0), TariffPeriod.peak, myStromProperties);
        checkTariffPeriod(LocalDateTime.of(2021, 6, 1, 7, 16, 0), TariffPeriod.peak, myStromProperties);

    }

    private void checkTariffPeriod(LocalDateTime localDateTime, TariffPeriod expectedTariffPeriod, MyStromProperties myStromProperties) {
        assertEquals(expectedTariffPeriod, myStromProperties.getCurrentTariffPeriod(localDateTime.atZone(ZoneId.of(myStromProperties.getTimezone())).toInstant()), localDateTime.toString());
    }
}