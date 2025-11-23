package org.uk_energy.ExternalApiController;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalApiData {
    @JsonProperty("data")
    private List < IntervalInfo > listInterval;
    public List < IntervalInfo >  getData() {
        return listInterval;
    }
    public void setData(List < IntervalInfo > listInterval) {
        this.listInterval = listInterval;
    }
    public Map<String, Object> getSummary(ZonedDateTime startDate, long numbHours) {
        Map<String, Double> intervalSummary = new HashMap<>();
        ZonedDateTime endDate = startDate.plusHours(numbHours);
        for (IntervalInfo intervalInfo : listInterval) {
            if (intervalInfo.getListFuel() != null &&
                    (intervalInfo.getFrom().isAfter(startDate) || intervalInfo.getFrom().isEqual(startDate)) &&
                    (intervalInfo.getTo().isBefore(endDate) || intervalInfo.getTo().isEqual(endDate))) {
                for (FuelInfo info : intervalInfo.getListFuel()) {
                    if(intervalSummary.containsKey(info.getFuel())) {
                        intervalSummary.put(info.getFuel(), intervalSummary.get(info.getFuel()) + info.getPerc());
                    } else {
                        intervalSummary.put(info.getFuel(), info.getPerc());
                    }
                }
            }
        }
        for (Map.Entry<String, Double> entry : intervalSummary.entrySet()) {
            entry.setValue(entry.getValue() / listInterval.size());
        }
        double clean = 0.0, total = 0.0;
        clean = intervalSummary.get("wind")
                + intervalSummary.get("solar")
                + intervalSummary.get("hydro")
                + intervalSummary.get("biomass")
                + intervalSummary.get("nuclear");
        for (Map.Entry<String, Double> entry : intervalSummary.entrySet()) {
            total += entry.getValue();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("from", startDate);
        result.put("to", startDate.plusHours(numbHours));
        result.put("data", intervalSummary);
        result.put("clean energy percent", clean*100/total);
        return result;
    }
}

class IntervalInfo {
    private ZonedDateTime from;
    private ZonedDateTime to;
    @JsonProperty("generationmix")
    private List < FuelInfo > listFuel;
    public ZonedDateTime getFrom() {
        return from;
    }
    public void setFrom(ZonedDateTime from) {
        this.from = from;
    }
    public ZonedDateTime getTo() {
        return to;
    }
    public void setTo(ZonedDateTime to) {
        this.to = to;
    }
    public List < FuelInfo > getListFuel() {
        return listFuel;
    }
    public void setListFuel(List < FuelInfo > listFuel) {
        this.listFuel = listFuel;
    }
}

class FuelInfo {
    private String fuel;
    private Double perc;
    public String getFuel() {
        return fuel;
    }
    public void setFuel(String fuel) {
        this.fuel = fuel;
    }
    public Double getPerc() {
        return perc;
    }
    public void setPerc(Double perc) {
        this.perc = perc;
    }
}