package com.gepardec.esb.prototype.services.app.rest.impl;

import com.gepardec.esb.prototype.services.app.annotation.Logging;
import com.gepardec.esb.prototype.services.app.rest.api.MetricRestService;
import org.eclipse.microprofile.metrics.MetricRegistry;
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
    public Map<String, Long> get() {
        final SortedMap<String, Long> result = new TreeMap<>();
        metricRegistry.getCounters().forEach((key, value) -> result.put(key, value.getCount()));

        return result;
    }
}
