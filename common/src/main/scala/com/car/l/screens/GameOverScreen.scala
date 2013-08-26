package com.car.l.screens

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.car.l.LudumGame
import com.car.l.Assets.assets
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent
import com.badlogic.gdx.scenes.scene2d.Actor
import com.car.l.LevelLoader

class GameOverScreen(game: LudumGame) extends AbstractScreen(game) {
  val button = new TextButton("RESTART", assets.skin)
  button.setPosition(graphics.getWidth() / 2 - button.getWidth() / 2, 200)

  button.addListener(new ChangeListener {
    override def changed(event: ChangeEvent, actor: Actor) {
      game.testScreen.setLevel(LevelLoader.levels.head)
      game.transitionToScreen(game.testScreen)
      assets.playSound("button")
    }
  })

  override def into() {
    game.testScreen.player.reset()
  }

  val label = new Label("You have perished", assets.skin)
  label.setPosition(graphics.getWidth() / 2 - label.getWidth() / 2, 300)

  stage.addActor(button)
  stage.addActor(label)
}