package cloud.rab.bit.mystrom.exporter.services.entities;

public enum TariffPeriod {

    offPeak,
    peak;

    public String getLabel() {
        switch (this) {
            case offPeak:
                return "off-peak";
            case peak:
                return "peak";
        }
        return "invalid";
    }

    public String getInverseLabel() {
        switch (this) {
            case offPeak:
                return "peak";
            case peak:
                return "off-peak";
        }
        return "invalid";
    }

}
