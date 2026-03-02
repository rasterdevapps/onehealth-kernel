package com.onehealth.kernel.api;

import java.util.List;

public interface ExtensionRegistry {

    void registerExtension(String namespace, String name, ExtensionPoint ext);

    <T extends ExtensionPoint> T getExtension(String namespace, String name, Class<T> type);

    List<ExtensionPoint> getExtensionsByNamespace(String namespace);
}
