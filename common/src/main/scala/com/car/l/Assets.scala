package com.car.l

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable

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

  lazy val skin: Skin = {
    val skin = new Skin()
    
    val ls = new Label.LabelStyle()
    ls.font = font
    skin.add("default", ls)
    
    val tbs = new TextButtonStyle()
    tbs.font = font
    tbs.down = new NinePatchDrawable(uiAtlas.createPatch("button_pressed"))
    tbs.up = new NinePatchDrawable(uiAtlas.createPatch("button_normal"))
    skin.add("default", tbs)
    
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
    manager.load("sounds/steps.wav", classOf[Sound])
    manager.load("sounds/enemy_hit.wav", classOf[Sound])
    manager.load("sounds/death.wav", classOf[Sound])
    manager.load("sounds/throw.wav", classOf[Sound])
    manager.load("sounds/big_pickup.wav", classOf[Sound])
    manager.load("sounds/potion.wav", classOf[Sound])
    manager.load("sounds/explosion.wav", classOf[Sound])
  }

  def playSound(key: String) {
    manager.get("sounds/" + key + ".wav", classOf[Sound]).play()
  }
}