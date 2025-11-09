package ch.ethz.dalab.web2text

import scala.io.Source
import breeze.linalg.csvwrite
import ch.ethz.dalab.web2text.cdom.CDOM
import ch.ethz.dalab.web2text.features.extractor._
import ch.ethz.dalab.web2text.features.FeatureExtractor
import ch.ethz.dalab.web2text.utilities.Util
import ch.ethz.dalab.web2text.output.CleanTextOutput
import ch.ethz.dalab.web2text.output.LabeledTextOutput


/**
 * This is the second step in classifying boilerplate content in a webpage.
 * It takes:
 * (1) input html file
 * (2) csv file with block labels, produced by [python main.py classify] after running ExtractPageFeatures
 * (3) output html file
 */
object ApplyLabelsToPage {

  def main(args: Array[String]): Unit = {
    // Command line argument: path to HTML file
    if (args.length < 3) {
      throw new IllegalArgumentException("Expecting arguments: (1) input html file, (2) labels in csv, (3) output file, (4) optional, clean / labeled output format")
    }

    val isClean =
      if (args.length >= 4) {
        args(3).trim.toLowerCase match {
          case "true"  => true
          case "false" => false
          case _       => throw new IllegalArgumentException("Fourth argument must be 'true' or 'false' indicating whether to output clean text or labeled text.")
        }
      }
      else true

    val labels = Util.loadFile(args(1)).split(",").map(_.toInt)
    applyLabelsToPage(args(0), labels, args(2), isClean)
  }

  def applyLabelsToPage(filename: String, labels: Seq[Int], output: String, isClean: Boolean = true) = {
    val featureExtractor = FeatureExtractor(
      DuplicateCountsExtractor
      + LeafBlockExtractor
      + AncestorExtractor(NodeBlockExtractor + TagExtractor(mode="node"), 1)
      + AncestorExtractor(NodeBlockExtractor, 2)
      + RootExtractor(NodeBlockExtractor)
      + TagExtractor(mode="leaf"),
      TreeDistanceExtractor + BlockBreakExtractor + CommonAncestorExtractor(NodeBlockExtractor)
    )

    val source = Source.fromFile(filename).getLines.mkString
    val cdom = CDOM(source)
    if (isClean) {
      Util.save(output, CleanTextOutput.apply(cdom, labels))
    } else {
      Util.save(output, LabeledTextOutput.apply(cdom, labels))
    }
  }
}
