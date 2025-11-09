package ch.ethz.dalab.web2text

import scala.io.Source
import breeze.linalg.csvwrite
import ch.ethz.dalab.web2text.cdom.CDOM
import ch.ethz.dalab.web2text.features.extractor._
import ch.ethz.dalab.web2text.features.FeatureExtractor
import ch.ethz.dalab.web2text.utilities.Util
import ch.ethz.dalab.web2text.output.CleanTextOutput


object VisualizeCDOM {

  def main(args: Array[String]): Unit = {
    // Command line argument: path to HTML file
    if (args.length < 1) {
      throw new IllegalArgumentException("Expecting arguments: (1) input html file")
    }
    visualizeCDOM(args(0))
  }

  def visualizeCDOM(filename: String) = {
    val source = Source.fromFile(filename).getLines.mkString
    val cdom = CDOM(source)
    cdom.saveHTML(filename + "_cdom.html")
  }
}
