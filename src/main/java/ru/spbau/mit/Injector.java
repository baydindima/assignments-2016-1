package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Injector {
    private static Map<String, Object> intancedClasses;
    private static List<String> loadedClasses;

    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        reset();

        loadedClasses.add(rootClassName);
        Class aClass = Class.forName(rootClassName);
        Constructor[] declaredConstructors = aClass.getDeclaredConstructors();
        Constructor constructor = declaredConstructors[0];
        Parameter[] parameters = constructor.getParameters();
        Object[] values = new Object[parameters.length];
        for (int i1 = 0; i1 < parameters.length; i1++) {
            Parameter parameter = parameters[i1];

            Class cClass = null;
            for (String implementationClassName : implementationClassNames) {
                Class bClass = Class.forName(implementationClassName);
                if (parameter.getType().isAssignableFrom(bClass)) {
                    if (cClass != null) {
                        throw new AmbiguousImplementationException();
                    }
                    cClass = bClass;
                }
            }
            if (cClass == null) {
                throw new ImplementationNotFoundException();
            }

            resetVisited();
            values[i1] = initializePrivate(cClass.getName());
        }

        return constructor.newInstance(values);
    }

    private static void reset() {
        intancedClasses = new HashMap<>();
        resetVisited();
    }

    private static void resetVisited() {
        loadedClasses = new ArrayList<>();
    }

    private static Object initializePrivate(String rootClassName) throws Exception {
        if (intancedClasses.containsKey(rootClassName)) {
            return intancedClasses.get(rootClassName);
        }

        if (loadedClasses.contains(rootClassName)) {
            throw new InjectionCycleException();
        }
        loadedClasses.add(rootClassName);

        Class aClass = Class.forName(rootClassName);

        Constructor[] declaredConstructors = aClass.getConstructors();
        Constructor constructor = declaredConstructors[0];
        Parameter[] parameters = constructor.getParameters();

        Object[] values = new Object[parameters.length];
        for (int i1 = 0; i1 < parameters.length; i1++) {
            Parameter parameter = parameters[i1];
            String name = parameter.getType().getName();
            values[i1] = initializePrivate(name);
        }

        Object newInstance = constructor.newInstance(values);
        intancedClasses.put(rootClassName, newInstance);

        return newInstance;
    }
}
