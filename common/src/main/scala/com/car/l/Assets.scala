package com.car.l

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas

object Assets {
  val instance = new Assets

  def loadAll() = {
    instance.manager.load("images/ui.pack", classOf[TextureAtlas])
    instance.manager.load("images/creature.pack", classOf[TextureAtlas])
  }
}

class Assets {
  val manager = new AssetManager

  lazy val uiAtlas = manager.get("images/ui.pack", classOf[TextureAtlas])

  def loadAll() = {
    manager.load("images/ui.pack", classOf[TextureAtlas])
  }
}