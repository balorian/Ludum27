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
  button.setPosition(graphics.getWidth() / 2 - button.getWidth() / 2, 100)

  button.addListener(new ChangeListener {
    override def changed(event: ChangeEvent, actor: Actor) {
      game.testScreen.setLevel(LevelLoader.levels.head)
      game.transitionToScreen(game.testScreen)
      assets.playSound("button")
    }
  })

  val labelMessage = new Label("You have perished", assets.skin)
  val labelTime = new Label(GAME_TIME, assets.skin)
  val labelScore = new Label(SCORE, assets.skin)

  stage.addActor(button)
  stage.addActor(labelMessage)
  stage.addActor(labelTime)
  stage.addActor(labelScore)

  override def into() {
    labelTime.setText(GAME_TIME + game.testScreen.time.toInt + "s")
    labelScore.setText(SCORE + game.testScreen.player.score)

    labelMessage.setPosition(100, 350)
    labelTime.setPosition(100, 290)
    labelScore.setPosition(100, 250)
  }

  override def outOf() {
    game.testScreen.player.reset()
    LevelLoader.depth = 1
    game.testScreen.time = 0
  }

  def setMessage(text: String) {
    labelMessage.setText(text)
  }
}