package com.car.l.screens

import com.badlogic.gdx.Game
import com.car.l.LudumGame
import com.car.game.Player
import com.badlogic.gdx.graphics.g2d.Animation
import com.car.l.Assets.assets

class LevelTestScreen(game: LudumGame) extends AbstractScreen(game: LudumGame) {
  stage.addActor(new Player(Map("idle" -> new Animation(0.25f, assets.creatureAtlas.createSprites("main"), Animation.LOOP))))
  
}