package com.car.l.screens

import com.car.l.LudumGame
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.Gdx.graphics
import com.car.l.Assets.assets
import com.badlogic.gdx.scenes.scene2d.actions.Actions._
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent
import com.badlogic.gdx.scenes.scene2d.Actor

class MenuScreen(game: LudumGame) extends AbstractScreen(game) {
  val label = new Label("SOUL REAVER", assets.skin)
  label.setPosition(graphics.getWidth() / 2 - label.getWidth() / 2, 300)
  
  val button = new TextButton("START", assets.skin)
  button.setPosition(graphics.getWidth() / 2 - button.getWidth() / 2, 200)
  
  button.addListener(new ChangeListener {
    override def changed(event: ChangeEvent, actor: Actor) {
      game.transitionToScreen(game.testScreen)
    }
  })

  stage.addActor(label)
  stage.addActor(button)

  def reset() {
//    stage.addAction(sequence(delay(1f), run(new Runnable {
//      def run {
//        game.testScreen.setLevel(LevelLoader.nextLevelFrom(game.testScreen.level.get.name))
//        game.transitionToScreen(game.testScreen)
//      }
//    })))
  }

  override def into() {
    reset()
  }
}