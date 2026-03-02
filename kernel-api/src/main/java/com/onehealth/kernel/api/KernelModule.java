package com.onehealth.kernel.api;

public interface KernelModule {

    String getModuleId();

    String getModuleName();

    String getVersion();

    void initialize();
}
