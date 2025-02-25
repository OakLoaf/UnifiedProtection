package org.lushplugins.unifiedprotection.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class ObjectUtils {

    public static boolean isObjectInList(Object mainObject, Object... objects) {
        return isObjectInStream(mainObject, Arrays.stream(objects));
    }

    public static boolean isObjectInCollection(Object mainObject, Collection<Object> objects) {
        return isObjectInStream(mainObject, objects.stream());
    }

    public static boolean isObjectInStream(Object mainObject, Stream<Object> stream) {
        return stream.anyMatch((obj) -> obj == mainObject);
    }
}
