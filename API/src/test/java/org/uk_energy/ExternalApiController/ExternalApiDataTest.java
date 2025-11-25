package org.uk_energy.ExternalApiController;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExternalApiDataTest {

    @Test
    void getSummary() {
        ExternalApiData data = new ExternalApiData();
        List<IntervalInfo> intervals = new ArrayList<>();

        ZonedDateTime now = ZonedDateTime.now();

        IntervalInfo info1 = new IntervalInfo();
        info1.setFrom(now);
        info1.setTo(now.plusMinutes(30));
        List<FuelInfo> fuels1 = new ArrayList<>();
        FuelInfo fuel1 = new FuelInfo();
        fuel1.setFuel("wind");
        fuel1.setPerc(50.0);
        fuels1.add(fuel1);
        FuelInfo fuel12 = new FuelInfo();
        fuel12.setFuel("solar");
        fuel12.setPerc(50.0);
        fuels1.add(fuel12);
        info1.setListFuel(fuels1);
        intervals.add(info1);

        IntervalInfo info2 = new IntervalInfo();
        info2.setFrom(now.plusMinutes(30));
        info2.setTo(now.plusMinutes(60));
        List<FuelInfo> fuels2 = new ArrayList<>();
        FuelInfo fuel2 = new FuelInfo();
        fuel2.setFuel("wind");
        fuel2.setPerc(100.0);
        fuels2.add(fuel2);
        info2.setListFuel(fuels2);
        intervals.add(info2);

        data.setData(intervals);

        Map<String, Object> result = data.getSummary(now, 1);

        assertNotNull(result);
        Map<String, Double> summary = (Map<String, Double>) result.get("data");
        assertEquals(75.0, summary.get("wind"));
        assertEquals(100.0, result.get("clean energy percent"));
    }
}
