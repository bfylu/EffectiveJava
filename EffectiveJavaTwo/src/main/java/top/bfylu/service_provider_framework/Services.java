package top.bfylu.service_provider_framework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Noninstantiable class for service registration and access
 * 服务注册和访问不可证实的类
 *
 */
public class Services {
    public Services() { } //Prevents instantiation (Item 4)

    //Maps service names to services
    //将服务名称映射到服务
    private static final Map<String, Provider> providers = new ConcurrentHashMap<String, Provider>();
    public static final String DEFAULT_PROVIDER_NAME = "<def>";
    //Provider registration API
    //提供商注册API
    public static void registerDefaultProvider(Provider p){
        registerProvider(DEFAULT_PROVIDER_NAME, p);
    }
    public static void registerProvider(String name, Provider p) {
        providers.put(name, p);
    }

    //Service access API
    //服务访问API
    public static Service newInstance() {
        return newInstance(DEFAULT_PROVIDER_NAME);
    }

    public static Service newInstance(String name) {
        Provider p = providers.get(name);
        if (p == null)
            throw new IllegalArgumentException("没有提供者注册名称: " + name);
        return p.newService();
    }
}
