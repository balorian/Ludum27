package com.car.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.graphics.Color
import com.car.l.Assets.assets
import com.badlogic.gdx.scenes.scene2d.ui.Label

object GameUI {
  val LEVEL = "Level - "
  val SCORE = "Score - "
}

class GameUI {
  lazy val stage: Stage = new Stage(graphics.getWidth, graphics.getHeight,
    false)

  val font = assets.font

  val spiritBar = new StateBar(10, 10, 64, graphics.getHeight() - 20, new Color(1f, 0xFF / 0x13, 0xFF / 0x7F, 1))
  val healthBar = new StateBar(graphics.getWidth() - 74, 10, 64, graphics.getHeight() - 20, new Color(0.75f, 0, 0, 1))

  val scoreLabel = new Label(GameUI.SCORE + "2000", assets.skin)
  scoreLabel.setPosition(100, graphics.getHeight() - 50)
  
  val levelLabel = new Label(GameUI.LEVEL + "1", assets.skin)
  levelLabel.setPosition(graphics.getWidth() - 100f - font.getBounds(levelLabel.getText()).width, graphics.getHeight() - 50)
  

  stage.addActor(spiritBar)
  stage.addActor(healthBar)
  stage.addActor(scoreLabel)
  stage.addActor(levelLabel)

  def update() {
    levelLabel.setText(GameUI.LEVEL + "1")
    scoreLabel.setText(GameUI.SCORE + "2000")
  }

  def render(delta: Float) {
    stage.act(delta)
    stage.draw()

  }
}