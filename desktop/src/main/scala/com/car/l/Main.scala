package com.car.l

import com.badlogic.gdx.backends.lwjgl._
import com.badlogic.gdx.Files

object Main {
  def main(args: Array[String]) {
    val cfg = new LwjglApplicationConfiguration()
    cfg.addIcon("images/icon_32.png", Files.FileType.Classpath)
    cfg.addIcon("images/icon_16.png", Files.FileType.Classpath)
    cfg.title = "Soul Reaver"
    cfg.height = 480
    cfg.width = 640
    cfg.useGL20 = true
    cfg.forceExit = false
    cfg.resizable = false
    new LwjglApplication(new LudumGame(), cfg)
  }
}

class Main {
  
}
