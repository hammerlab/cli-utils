package org.hammerlab.cli.app.spark

import org.apache.spark.serializer.KryoRegistrator
import org.hammerlab.spark.confs
import org.hammerlab.spark.confs.Kryo

trait Registrar {
  def apply(container: confs.Kryo): Unit
}

object Registrar {
  val noop =
    new Registrar {
      override def apply(container: Kryo): Unit = {}
    }
  
  implicit def fromCtor[T <: KryoRegistrator](ctor: () ⇒ T): Registrar =
    new Registrar {
      override def apply(container: Kryo): Unit = container.registrar(ctor())
    }
}
