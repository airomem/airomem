/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

import java.io.Serializable;

/**
 *
 * @author Kanapka
 */
public interface Storable<READONLY> extends Serializable {

    READONLY getImmutable();
}
