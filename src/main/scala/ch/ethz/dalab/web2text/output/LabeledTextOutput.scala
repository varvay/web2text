package ch.ethz.dalab.web2text.output

import ch.ethz.dalab.web2text.cdom.CDOM

object LabeledTextOutput {

  def apply(cdom: CDOM, labels: Seq[Int]): String = {
    var outstring = ""
    var breaked = true
    cdom.leaves.zip(labels).foreach {
      case (leaf, 1) => {
        breaked = false
        outstring += "[CONTENT]"
        outstring += leaf.text
        if(leaf.properties.blockBreakAfter) {
          outstring += "\n\n"
          breaked = true
        }
      }
      case (leaf, 0) => {
        breaked = false
        outstring += "[BOILERPLATE]"
        outstring += leaf.text
        if(leaf.properties.blockBreakAfter) {
          outstring += "\n\n"
          breaked = true
        }
      }
    }

    return outstring
  }

}
