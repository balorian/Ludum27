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

class LevelTestScreen(game: LudumGame) extends AbstractScreen(game: LudumGame) {
  var level = new Level()
  val player = new Player(Map("idle" -> new Animation(0.25f, assets.creatureAtlas.createSprites("main"), Animation.LOOP)), level)
  
  stage.addActor(level)
  stage.addActor(player)
  
  override def inputProcessor = {
    val plex = new InputMultiplexer()
    plex.addProcessor(stage)
    plex.addProcessor(new PlayerProcessor(player))
    plex
  }
}