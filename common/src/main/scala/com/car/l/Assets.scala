package com.car.l

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas

object Assets {
  val assets = new Assets
}

class Assets {
  val manager = new AssetManager

  lazy val uiAtlas = manager.get("images/ui.pack", classOf[TextureAtlas])
  lazy val creatureAtlas = manager.get("images/creatures.pack", classOf[TextureAtlas])

  def loadAll() = {
    manager.load("images/ui.pack", classOf[TextureAtlas])
    manager.load("images/creatures.pack", classOf[TextureAtlas])
  }
}