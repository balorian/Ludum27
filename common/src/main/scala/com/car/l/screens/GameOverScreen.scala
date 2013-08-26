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
  val GAME_TIME = "Game time: "
  val SCORE = "Score: "

  val button = new TextButton("RESTART", assets.skin)
  button.setPosition(graphics.getWidth() / 2 - button.getWidth() / 2, 150)

  button.addListener(new ChangeListener {
    override def changed(event: ChangeEvent, actor: Actor) {
      game.testScreen.setLevel(LevelLoader.levels.head)
      game.transitionToScreen(game.testScreen)
      assets.playSound("button")
    }
  })

  val labelMessage = new Label("You have perished", assets.skin)
  labelMessage.setPosition(graphics.getWidth() / 2 - labelMessage.getWidth() / 2, 330)

  val labelTime = new Label(GAME_TIME, assets.skin)
  labelTime.setPosition(graphics.getWidth() / 2 - labelMessage.getWidth() / 2, 290)

  val labelScore = new Label(SCORE, assets.skin)
  labelScore.setPosition(graphics.getWidth() / 2 - labelMessage.getWidth() / 2, 250)

  stage.addActor(button)
  stage.addActor(labelMessage)
  stage.addActor(labelTime)
  stage.addActor(labelScore)

  override def into() {
    labelTime.setText(GAME_TIME + game.testScreen.time)
    labelScore.setText(SCORE + game.testScreen.player.score)
  }

  override def outOf() {
    game.testScreen.player.reset()
    LevelLoader.depth = 1
    game.testScreen.time = 0
  }
}