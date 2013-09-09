package mimp.gui

import scala.swing.SimpleSwingApplication
import scala.swing.Dimension
import scala.swing.Dialog
import scala.swing.event.WindowClosing

object MyFrame extends SimpleSwingApplication {
  def top = new scala.swing.Frame {
    title = "Sample"
    minimumSize = new Dimension(300, 200)
    
    peer.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE)
    override def closeOperation() = {
      val res = Dialog.showConfirmation(title = "Confirm", message = "May I close this Application?")
      res match {
        case Dialog.Result.Yes => dispose()
        case _ => ()
      }
    }

    contents = new ComponentTest

  }
}

object Runner {
  def run(fun: => Unit): Unit = fun
  def run(label: String = "", fun: => Unit): Unit = fun
}
