package top.bfylu.service_provider_framework;

/**
 * service provider interface
 * 服务提供商接口
 * 提供者注册API(Provider Registration API),这是系统用来注册实现,让客户端访问它们的
 */
public interface Provider {
    Service newService();
}
