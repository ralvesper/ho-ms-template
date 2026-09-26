package com.highonline.exampleapi.core.domain.model.greeting;

import io.hypersistence.tsid.TSID;

/** TSID: ordenável por tempo, exposto como string na API e como bigint no banco. */
public record GreetingId(long value) {

    public static GreetingId generate() {
        return new GreetingId(TSID.fast().toLong());
    }

    public static GreetingId from(String raw) {
        try {
            return new GreetingId(TSID.from(raw).toLong());
        } catch (IllegalArgumentException e) {
            throw new GreetingNotFoundException(raw);
        }
    }

    @Override
    public String toString() {
        return TSID.from(value).toString();
    }
}
