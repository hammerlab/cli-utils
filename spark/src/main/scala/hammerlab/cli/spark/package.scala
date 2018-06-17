package hammerlab.cli

import org.hammerlab.cli.{spark ⇒ s}
package object spark {
  type App[Opts] = s.App[Opts]
  type PathApp[Opts] = s.PathApp[Opts]
  type Registrar = s.Registrar
}
