package org.hammerlab.cli.app

import org.apache.spark.serializer.KryoRegistrator
import org.hammerlab.cli.app.spark.SparkPathApp
import org.hammerlab.spark.confs
import org.hammerlab.spark.confs.Kryo

import scala.reflect.ClassTag

/**
 * Type-class representing types that can be passed as the `Reg` type-parameter to [[SparkPathApp]]: all
 * [[KryoRegistrator]] subclasses as well as a default/no-op [[Nothing]] implementation.
 */
trait IsRegistrar[T] {
  def apply(container: confs.Kryo): Unit
}

object IsRegistrar {
  // Default/No-op
  implicit val nothing: IsRegistrar[Nothing] =
    new IsRegistrar[Nothing] {
      override def apply(container: Kryo): Unit = {}
    }

  /**
   * Wrap any [[KryoRegistrator]] for use with [[SparkPathApp]]
   */
  implicit def registrator[T <: KryoRegistrator: ClassTag]: IsRegistrar[T] =
    new IsRegistrar[T] {
      override def apply(container: Kryo): Unit =
        container.registrar(implicitly[ClassTag[T]])
    }
}
