package cloud.rab.bit.mystrom.exporter.services.entities;

public enum TariffPeriod {

    offPeak,
    peak;

    public String getLabel() {
        return switch (this) {
            case offPeak -> "off-peak";
            case peak -> "peak";
        };
    }

    public String getInverseLabel() {
        return switch (this) {
            case offPeak -> "peak";
            case peak -> "off-peak";
        };
    }

}
