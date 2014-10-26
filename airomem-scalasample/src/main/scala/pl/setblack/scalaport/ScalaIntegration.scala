package pl.setblack.scalaport

import pl.setblack.airomem.core._;

/**
 * Some useful conversions for Commands and Queries.
 */
object ScalaIntegration {
 
  implicit def query[Q,R]( f: (Q) => R):Query[Q,R] = new Query[Q,R] {
    def evaluate(q: Q) : R = {
      return f(q);
    }
  }
  
  implicit def cmd[S]( f: (S) => Unit):VoidCommand[S] = new VoidCommand[S] {
    def executeVoid(s: S)  = {
        f(s);
    }
  }
  
}
