package com.car.l.screens

import com.car.l.LudumGame
import com.car.game.Player
import com.car.game.Level
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.car.l.Assets.assets
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent
import com.badlogic.gdx.scenes.scene2d.Actor

class LevelEndScreen(game: LudumGame, screen: LevelTestScreen) extends AbstractScreen(game) {
  val button = new TextButton("Start next level", assets.skin)
  button.setPosition(graphics.getWidth() / 2 - button.getWidth() / 2, 200)
  
  val label = new Label("Delving deeper into the dungeon", assets.skin)
  label.setPosition(graphics.getWidth() / 2 - label.getWidth() / 2, 300)
  
  button.addListener(new ChangeListener {
    def changed(event: ChangeEvent, actor: Actor) {
      println("Start new level")
      game.testScreen.setLevel("level1")
      game.transitionToScreen(game.testScreen)
    }
  })
  stage.addActor(label)
  stage.addActor(button)

  def reset(player: Player, level: Level) {

  }

  override def into() {
    
  }
}