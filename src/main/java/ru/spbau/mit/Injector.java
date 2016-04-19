package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;


public final class Injector {
    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        Class aClass = Class.forName(rootClassName);

        Constructor[] declaredConstructors = aClass.getDeclaredConstructors();
        if (declaredConstructors.length > 1) {
            throw new AmbiguousImplementationException();
        }

        Constructor constructor = declaredConstructors[0];
        Parameter[] parameters = constructor.getParameters();
        if (parameters.length != implementationClassNames.size()) {
            throw new RuntimeException();
        }

        Object[] values = new Object[parameters.length];
        for (int i1 = 0; i1 < parameters.length; i1++) {
            Parameter parameter = parameters[i1];
            String name = parameter.getType().getName();


            Class cClass = null;
            for (String implementationClassName : implementationClassNames) {
                Class bClass = Class.forName(implementationClassName);
                if (parameter.getType().isAssignableFrom(bClass)) {
                    cClass = bClass;
                    break;
                }
            }


            List<String> loadedClasses = new ArrayList<>();
            loadedClasses.add(cClass.getName());
            values[i1] = initializePrivate(cClass.getName(), loadedClasses);
        }

        return constructor.newInstance(values);
    }


    private static Object initializePrivate(String rootClassName, List<String> loadedClasses) throws Exception {
        Class aClass;
        try {
            aClass = Class.forName(rootClassName);
        } catch (ClassNotFoundException e) {
            throw new ImplementationNotFoundException();
        }

        Constructor[] declaredConstructors = aClass.getDeclaredConstructors();
        if (declaredConstructors.length > 1) {
            throw new AmbiguousImplementationException();
        }

        Constructor constructor = declaredConstructors[0];

        Parameter[] parameters = constructor.getParameters();
        Object[] values = new Object[parameters.length];
        for (int i1 = 0; i1 < parameters.length; i1++) {
            Parameter parameter = parameters[i1];
            String name = parameter.getType().getName();
            if (loadedClasses.indexOf(name) != -1) {
                throw new InjectionCycleException();
            }
            loadedClasses.add(name);
            values[i1] = initializePrivate(name, loadedClasses);
        }

        return constructor.newInstance(values);
    }
}
