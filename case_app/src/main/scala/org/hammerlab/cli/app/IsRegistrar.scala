package org.hammerlab.cli.app

import org.apache.spark.serializer.KryoRegistrator
import org.hammerlab.spark.confs
import org.hammerlab.spark.confs.Kryo

import scala.reflect.ClassTag

/**
 * Type-class representing types that can be passed as the `Reg` type-parameter to [[PathApp]]: all
 * [[KryoRegistrator]] subclasses as well as a default/no-op [[Nothing]] implementation.
 */
trait IsRegistrar {
  def apply(container: confs.Kryo): Unit
}

object IsRegistrar {
  // Default/No-op
  val nothing: IsRegistrar =
    new IsRegistrar {
      override def apply(container: Kryo): Unit = {}
    }

  /**
   * Wrap any [[KryoRegistrator]] for use with [[PathApp]]
   */
  implicit def registrator[T <: KryoRegistrator: ClassTag]: IsRegistrar =
    new IsRegistrar {
      override def apply(container: Kryo): Unit =
        container.registrar(implicitly[ClassTag[T]])
    }

  implicit def fromRegistrator(r: KryoRegistrator): IsRegistrar =
    new IsRegistrar {
      override def apply(container: Kryo): Unit =
        container.registrar(r)
    }
}
