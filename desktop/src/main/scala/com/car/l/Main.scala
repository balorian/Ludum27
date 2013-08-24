package com.car.l

import com.badlogic.gdx.backends.lwjgl._

object Main {
  def main(args: Array[String]) {
    val cfg = new LwjglApplicationConfiguration()
    cfg.title = "ludum"
    cfg.height = 480
    cfg.width = 640
    cfg.useGL20 = true
    cfg.forceExit = false
    new LwjglApplication(new LudumGame(), cfg)
  }
}

class Main {
  
}
