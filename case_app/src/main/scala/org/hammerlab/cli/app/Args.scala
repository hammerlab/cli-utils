package org.hammerlab.cli.app

case class Args[Opts](opts: Opts, args: Seq[String]) {
  def apply(i: Int) = args(i)
}
object Args {
  implicit def unwrap(args: Args[_]): Seq[String] = args.args
  implicit def unwrap[Opts](args: Args[Opts]): Opts = args.opts
}
