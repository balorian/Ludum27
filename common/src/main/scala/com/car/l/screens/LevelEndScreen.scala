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
import com.badlogic.gdx.scenes.scene2d.actions.Actions._
import com.car.l.LevelLoader

class LevelEndScreen(game: LudumGame, screen: LevelTestScreen) extends AbstractScreen(game) {
  val label = new Label("Delving deeper into the dungeon", assets.skin)
  label.setPosition(graphics.getWidth() / 2 - label.getWidth() / 2, 300)

  stage.addActor(label)

  def reset() {
    stage.addAction(sequence(delay(1f), run(new Runnable {
      def run {
        game.testScreen.setLevel(LevelLoader.nextLevelFrom(game.testScreen.level.get.name))
        game.transitionToScreen(game.testScreen)
      }
    })))
  }

  override def into() {
    reset()
  }
}