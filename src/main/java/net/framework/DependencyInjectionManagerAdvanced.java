package net.framework;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DependencyInjectionManagerAdvanced extends DependencyInjectionManager {
    public DependencyInjectionManagerAdvanced(Properties applicationProperties, Map<Class<?>, Object> externalDependencies) {
        super(applicationProperties, externalDependencies);
    }

    @Override
    protected List<Class<?>> getAllClassesInPackage(String packagePath) throws IOException, ClassNotFoundException {
        return super.getAllClassesInPackage(packagePath);
    }
}
