package org.uk_energy.Service;

import org.springframework.stereotype.Service;
import org.uk_energy.ExternalApiController.ExternalApiData;
import org.uk_energy.ExternalApiController.ExternalApiService;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final ExternalApiService externalApiService;

    public UserServiceImpl(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @Override
    public Object getAvgEnergyMix() {
        //Data
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime threeDaysLaterUtc = nowUtc.plusDays(3);
        DateTimeFormatter frmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:01'Z'");
        String from = frmt.format(nowUtc);
        String to = frmt.format(threeDaysLaterUtc);

        //Request
        ExternalApiData data = (ExternalApiData) externalApiService.getData(from, to);

        //Computing result
        List<Map<String, Object>> res = new ArrayList<>();
        ZonedDateTime date = nowUtc.withHour(0).withMinute(0).withSecond(0).minusNanos(0).withNano(0);
        for (int i = 0; i < 3; ++i) {
            res.add(data.getSummary(date, 24));
            date = date.plusDays(1);
        }
        return res;
    }

    @Override
    public Object getBestInterval(long duration) {
        if (duration <= 0 || duration > 6) throw new IllegalArgumentException("Invalid interval duration!");

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        nowUtc = nowUtc.withMinute(0).withSecond(0).minusNanos(0).withNano(0);
        ZonedDateTime twoDaysLaterUtc = nowUtc.plusDays(2);
        DateTimeFormatter frmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:01'Z'");
        String from = frmt.format(nowUtc);
        String to = frmt.format(twoDaysLaterUtc);

        ExternalApiData data = (ExternalApiData) externalApiService.getData(from, to);

        Map<String, Object> bestInterval = new HashMap<>();
        double maximum = 0.0;
        ZonedDateTime date = nowUtc.withMinute(0).withSecond(0).minusNanos(0).withNano(0);
        for (int i = 0; i < 96 - 2 * duration + 1; ++i) {
            Map<String, Object> result = data.getSummary(date, duration);
            if (!result.containsKey("clean energy percent")) continue;
            if (bestInterval.isEmpty() ||
                    (Double) result.get("clean energy percent") > maximum) {
                maximum = (Double) result.get("clean energy percent");
                bestInterval.put("from", result.get("from"));
                bestInterval.put("to", result.get("to"));
                bestInterval.put("clean energy percent", result.get("clean energy percent"));
            }
            date = date.plusMinutes(30);
        }
        return bestInterval;
    }
}
