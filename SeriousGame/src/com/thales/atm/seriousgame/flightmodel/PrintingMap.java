package com.thales.atm.seriousgame.flightmodel;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
/**
	Print map for testing purpose
 */

public class PrintingMap<K, V> {
    private Map<K, V> map;

    public PrintingMap(Map<K, V> map) {
        this.map = map;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<K, V>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<K, V> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();

    }
}
