/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.impl;

import com.google.common.base.Optional;

import java.io.IOException;
import java.io.Serializable;
import org.prevayler.foundation.DeepCopier;

/**
 * Consistency wrapper for a persistent object.
 *
 * This container holds two instances of persistence object.
 * FoodTester  -  object on which transactions are first applied.
 * SafeCopy - is the real state of a system  - transactions are applied to it
 * only if they go correctly on FoodTester.
 * Operation restore uses Deep Cloning to Re create Food Tester usinf SafeCopy.
 */
public class RoyalFoodTester<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Optional<T> foodTester;

    private final Optional<T> safeCopy;

    private final boolean safe;

    public static <T> RoyalFoodTester<T> of(T val) {
        return RoyalFoodTester.of(val, true);
    }


    public static <T> RoyalFoodTester<T> of(T val, boolean safe) {
        return new RoyalFoodTester(val, safe);
    }

    public static <T> RoyalFoodTester<T> absent(boolean safe) {
        return new RoyalFoodTester(safe);
    }

    private RoyalFoodTester(boolean safe) {
        this.safeCopy = Optional.absent();
        this.foodTester = Optional.absent();
        this.safe = safe;
    }

    private RoyalFoodTester(T val, boolean safe) {
        this.safeCopy = Optional.of(val);
        this.safe = safe;
        restore();
    }


    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        restore();
    }

    public T getFoodTester() {
        return this.foodTester.get();
    }

    public T getWorkObject() {
            return this.getFoodTester();
    }

    public T getSafeCopy() {
        return this.safeCopy.get();
    }

    public final void restore() {
        if ( isSafe()) {
            this.foodTester = (Optional<T>) DeepCopier.deepCopy(this.safeCopy);
        } else {
            this.foodTester = this.safeCopy;
        }
    }

    public boolean isSafe() {
        return safe;
    }
}
