package fi.tkgwf.ruuvi.db;

import fi.tkgwf.ruuvi.bean.RuuviMeasurement;
import fi.tkgwf.ruuvi.config.Config;
import fi.tkgwf.ruuvi.utils.InfluxDBConverter;
import java.util.concurrent.TimeUnit;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

public class InfluxDBConnection implements DBConnection {

    private final InfluxDB influxDB;

    public InfluxDBConnection() {
        influxDB = InfluxDBFactory.connect(Config.getInfluxUrl(), Config.getInfluxUser(), Config.getInfluxPassword());
        influxDB.setDatabase(Config.getInfluxDatabase());
        influxDB.enableGzip();
//        influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS); // TODO: make these configurable
    }

    @Override
    public void save(RuuviMeasurement measurement) {
        Point point = InfluxDBConverter.toInflux(measurement);
        influxDB.write(point);
    }

    @Override
    public void close() {
        influxDB.close();
    }
}
