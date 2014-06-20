/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

/**
 *
 * @author Kanapka
 */
public interface Query<READONLY, RESULT> {

    RESULT evaluate(READONLY object);

}
