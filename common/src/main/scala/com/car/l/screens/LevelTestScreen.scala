package com.car.l.screens

import com.badlogic.gdx.Game
import com.car.l.LudumGame
import com.car.game.Player
import com.badlogic.gdx.graphics.g2d.Animation
import com.car.l.Assets.assets
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.car.game.PlayerProcessor
import com.car.game.Level
import com.car.ui.GameUI
import com.car.l.LevelLoader
import com.badlogic.gdx.Gdx

class LevelTestScreen(game: LudumGame) extends AbstractScreen(game: LudumGame) {
  val LOG_TAG = "LevelTestScreen"
    
  lazy val ui = new GameUI

  var level: Option[Level] = None
  val player = new Player(Map("idle" -> new Animation(0.25f, assets.creatureAtlas.createSprites("main"), Animation.LOOP)))

  def setLevel(key: String) {
    level = Some(LevelLoader.load(key))
    stage.clear()
    stage.addActor(level.get)
    stage.addActor(player)
  }

  override def into() {
    Gdx.app.debug(LOG_TAG, "GOING TO LEVEL 1")
    setLevel("level1")
  }

  override def inputProcessor = {
    val plex = new InputMultiplexer()
    plex.addProcessor(stage)
    plex.addProcessor(new PlayerProcessor(player))
    plex
  }

  override def render(delta: Float) {
    super.render(delta)
    ui.render(delta)
  }
}