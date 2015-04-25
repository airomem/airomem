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

    public static <T> RoyalFoodTester<T> of(T val) {
        return new RoyalFoodTester(val);
    }

    public static <T> RoyalFoodTester<T> absent() {
        return new RoyalFoodTester();
    }

    private RoyalFoodTester() {
        this.safeCopy = Optional.absent();
        this.foodTester = Optional.absent();
    }

    private RoyalFoodTester(T val) {
        this.safeCopy = Optional.of(val);
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

    public T getSafeCopy() {
        return this.safeCopy.get();
    }

    public final void restore() {
        this.foodTester = (Optional<T>) DeepCopier.deepCopy(this.safeCopy);
    }

}
