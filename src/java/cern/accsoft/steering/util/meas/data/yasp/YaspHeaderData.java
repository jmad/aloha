package cern.accsoft.steering.util.meas.data.yasp;

public enum YaspHeaderData {
    OPTIC("TWISS", String.class), //
    CONTEXT("CONTEXT", String.class), //
    TIME_STAMP("DATE-CODE", Long.class), //
    ENERGY("P", Double.class), //
    FILL_NUMBER("FILLNUM", Long.class),
    BEAM_MODE("BMODE", String.class),
    MODEL_URI("MODEL-LINK", String.class),
    UNKNOWN("unknown", Object.class);

    private Class<?> dataTypeClazz;
    private String tagName;

    private YaspHeaderData(String tagName, Class<?> dataTypeClazz) {
        this.tagName = tagName;
        this.dataTypeClazz = dataTypeClazz;
    }

    public String getYaspTag() {
        return this.tagName;
    }

    public Class<?> getDataTypeClass() {
        return this.dataTypeClazz;
    }

    public static YaspHeaderData fromString(String tag) {
        String normalizedTag = tag.toUpperCase();
        for (YaspHeaderData data : YaspHeaderData.values()) {
            if (data.getYaspTag().equals(normalizedTag)) {
                return data;
            }
        }

        return YaspHeaderData.UNKNOWN;
    }
}
