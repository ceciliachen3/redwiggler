package com.nike.redwiggler.core.models

case class RedwigglerReport(verbPath: VerbPath, results : Map[Int, Seq[ValidationStatus]]) {

  def title: String = verbPath.verb + " " + verbPath.path

  def passed : Int = results.values.flatten.collect {
    case ValidationPassed(_, _) => 1
  }.sum

  def total : Int = results.values.flatten.size

  def badge: String = s"$passed/$total"
}

object RedwigglerReport {
  def apply(validationResults: Seq[ValidationStatus]) : Seq[RedwigglerReport] = validationResults
    .groupBy(_.verbPath)
    .map(x => apply(verbPath = x._1, results = x._2))
    .toSeq

  def apply(verbPath: VerbPath, results : Seq[ValidationStatus]) : RedwigglerReport = {
    RedwigglerReport(verbPath, results.groupBy(_.code))
  }
}

case class VerbPath(verb : HttpVerb, path : String)
