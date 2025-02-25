package org.lushplugins.unifiedprotection.utils;

import java.util.Collection;
import java.util.concurrent.Callable;

public class ObjectMatcher {

    public static <T> T onFirstMatch(Object mainObject, Collection<ObjectRunner<T>> runners) {
        for (ObjectRunner<T> runner : runners) {
            for (Object object : runner.objects()) {
                if (object == mainObject) {
                    try {
                        return runner.callable().call();
                    } catch (Exception ignored) {}
                }
            }
        }

        return null;
    }

    public record ObjectRunner<T>(Callable<T> callable, Object... objects) {}
}
