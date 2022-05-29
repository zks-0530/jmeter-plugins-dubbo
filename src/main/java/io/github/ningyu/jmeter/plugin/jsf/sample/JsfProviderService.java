package io.github.ningyu.jmeter.plugin.jsf.sample;

import com.jd.jsf.gd.config.ConsumerConfig;
import com.jd.jsf.gd.config.RegistryConfig;
import io.github.ningyu.jmeter.plugin.dubbo.sample.DubboSample;
import io.github.ningyu.jmeter.plugin.dubbo.sample.ProviderService;
import io.github.ningyu.jmeter.plugin.dubbo.sample.RegistryServerSync;
import io.github.ningyu.jmeter.plugin.util.Constants;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: zengweipei
 * @CreateTime: 2021-10-19 15:16
 * @Description:
 */
public class JsfProviderService implements Serializable {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = -750353929981409079L;



    private static ConcurrentMap<String, JsfProviderService> cache = new ConcurrentHashMap<>();

    public static JsfProviderService get(String key) {
        JsfProviderService service = cache.get(key);
        if (service == null) {
            cache.putIfAbsent(key, new JsfProviderService());
            service = cache.get(key);
        }
        return service;
    }

    public List<String> getProviders(String protocol, String address, String group) throws RuntimeException {
        if (protocol.equals("index")){
            return executeRegistry(protocol, address, group);
//        } else if (protocol.equals("none")) {
//            return executeTelnet();
        } else {
            throw new RuntimeException("Registry Protocol please use index ");
        }
    }

    private List<String> executeTelnet() throws RuntimeException {
        throw new RuntimeException();
    }

    private List<String> executeRegistry(String protocol, String address, String group) throws RuntimeException {
        ConsumerConfig consumer = new ConsumerConfig();
        RegistryConfig registry = null;
        switch (protocol) {
            case Constants.REGISTRY_JSF:
                registry = new RegistryConfig();
                registry.setProtocol(Constants.REGISTRY_JSF);
                registry.setIndex(address);
                consumer.setRegistry(registry);
                break;
        }

        return null;
    }
}
