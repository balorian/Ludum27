package com.car.l

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.audio.Sound

object Assets {
  val assets = new Assets
}

class Assets {
  val manager = new AssetManager

  lazy val uiAtlas = manager.get("images/ui.pack", classOf[TextureAtlas])
  lazy val creatureAtlas = manager.get("images/creatures.pack", classOf[TextureAtlas])
  lazy val tileAtlas = manager.get("images/tiles.pack", classOf[TextureAtlas])

  lazy val hit = manager.get("sounds/hit.wav", classOf[Sound])
  lazy val hurt = manager.get("sounds/hurt.wav", classOf[Sound])

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
    manager.load("images/tiles.pack", classOf[TextureAtlas])

    manager.load("sounds/hit.wav", classOf[Sound])
    manager.load("sounds/hurt.wav", classOf[Sound])
    manager.load("sounds/key.wav", classOf[Sound])
    manager.load("sounds/meat.wav", classOf[Sound])
    manager.load("sounds/soul.wav", classOf[Sound])
    manager.load("sounds/door.wav", classOf[Sound])
  }

  def playSound(key: String) {
    manager.get("sounds/" + key + ".wav", classOf[Sound]).play()
  }
}