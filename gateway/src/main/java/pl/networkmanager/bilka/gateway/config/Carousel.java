package pl.networkmanager.bilka.gateway.config;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class Carousel {

    private final EurekaClient eurekaClient;
    List<InstanceInfo> instances = new ArrayList<>();
    int currentIndex = 0;

    public Carousel(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        try {
            initAuthCarousel();
        } catch (NullPointerException e) {
            log.warn("Can't find active instances of AUTH-SERVICE");
        }
        events();
    }

    public String getUriAuth() {
        StringBuilder stringBuilder = new StringBuilder();
        InstanceInfo instance = instances.get(currentIndex);
        stringBuilder.append(instance.getIPAddr()).append(":").append(instance.getPort());
        if (instances.size() - 1 == currentIndex) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        return stringBuilder.toString();
    }

    private void events() {
        eurekaClient.registerEventListener(eurekaEvent -> {
            log.info("--START initAuthCarousel-registerEventListener--");
            initAuthCarousel();
            log.info("--END initAuthCarousel-registerEventListener--");
        });
        eurekaClient.unregisterEventListener(eurekaEvent -> {
            try {
                log.info("--START initAuthCarousel-unregisterEventListener--");
                initAuthCarousel();
            } catch (NullPointerException e) {
                log.warn("Can't find active instances of AUTH-SERVICE");
            }
            log.info("--END initAuthCarousel-unregisterEventListener--");
        });
    }

    private void initAuthCarousel() throws NullPointerException {
        log.info("--START initAuthCarousel--");
        instances = eurekaClient.getApplication("AUTH-SERVICE").getInstances();
        log.info("--END initAuthCarousel--");
    }
}
