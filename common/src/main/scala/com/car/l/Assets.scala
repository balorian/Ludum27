package com.car.l

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Label

object Assets {
  val assets = new Assets
}

class Assets {
  val manager = new AssetManager

  lazy val uiAtlas = manager.get("images/ui.pack", classOf[TextureAtlas])
  lazy val creatureAtlas = manager.get("images/creatures.pack", classOf[TextureAtlas])
  val font: BitmapFont = new BitmapFont(Gdx.files.classpath("images/ronda32_bold.fnt"), false)

  val skin: Skin = {
    val skin = new Skin()
    val ls = new Label.LabelStyle()
    ls.font = font
    skin.add("default", ls)
    skin
  }

  def loadAll() = {
    manager.load("images/ui.pack", classOf[TextureAtlas])
    manager.load("images/creatures.pack", classOf[TextureAtlas])
  }
}