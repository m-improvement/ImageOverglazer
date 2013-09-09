package mimp.gui

import scala.swing._
import java.awt.Color

class ComponentTest extends Component {
	
  peer.setPreferredSize(new Dimension(200, 200))
  
  override protected def paintComponent(g: Graphics2D) = {
    g.setColor(Color.RED)
    (1 to 10).foreach { v =>
      	val r = v * 10 * 2;
      	val x = 100 - v*10
      	val y = 100 - v*10
    	g.drawOval(x, y, r, r)
    }
  }
}