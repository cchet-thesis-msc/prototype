package com.gepardec.esb.prototype.integration.services.db.rest.impl;

import com.gepardec.esb.prototype.integration.services.db.annotation.Logging;
import com.gepardec.esb.prototype.integration.services.db.rest.api.MetricRestService;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Metric;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.Timer;
import org.eclipse.microprofile.metrics.annotation.Counted;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
@ApplicationScoped
@Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_API)
public class MetricRestServiceImpl implements MetricRestService {

    @Inject
    private MetricRegistry metricRegistry;

    @Override
    @Counted(name = "generated-metrics", monotonic = true)
    public SortedMap<String, SortedMap<String, String>> get() {
        final SortedMap<String, SortedMap<String, String>> result = new TreeMap<>();
        for (Map.Entry<String, Metric> map : metricRegistry.getMetrics().entrySet()) {
            if (!result.containsKey(map.getValue().getClass().getSimpleName())) {
                result.put(map.getKey(), new TreeMap<>());
            }
            result.get(map.getKey()).put(map.getKey(), extractValue(map.getValue()));
        }

        return result;
    }

    private static String extractValue(final Metric metric) {
        if (Counter.class.isAssignableFrom(metric.getClass())) {
            return String.valueOf(((Counter) metric).getCount());
        } else if (Timer.class.isAssignableFrom(metric.getClass())) {
            return ((Timer) metric).getSnapshot().getMean() + " millis";
        } else {
            return "unsupported";
        }
    }
}
