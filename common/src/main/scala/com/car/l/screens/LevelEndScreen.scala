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
import com.badlogic.gdx.math.MathUtils

class LevelEndScreen(game: LudumGame, screen: LevelTestScreen) extends AbstractScreen(game) {
  val label = new Label("You delve deeper into the dungeon", assets.skin)
  label.setPosition(graphics.getWidth() / 2 - label.getWidth() / 2, 300)

  val story = List(
    "The curse has left me without my soul, forcing me to scavenge the tattered remains of the undead",
    "I can feel my stregth slipping from me second by second",
    "The meat seems edible, giving me sustenance",
    "Some of my enemies drop curious potions. It might be prudent to ssave them for the lower levels",
    "I must be getting closer to my soul, I can sense it deep in the eart",
    "My weapon arm grows weaker, I hope it will not be much longer",
    "The screams of the undead haunt my ears",
    "What a horrible night to have a curse")

  val labelStory = new Label("", assets.skin)
  labelStory.setBounds(150, 50, 330, 300)
  labelStory.setWrap(true)

  stage.addActor(label)
  //stage.addActor(labelStory)

  def reset() {
    stage.addAction(sequence(delay(1.5f), run(new Runnable {
      def run {
        game.testScreen.setLevel(LevelLoader.nextLevelFrom(game.testScreen.level.get.name))
        game.transitionToScreen(game.testScreen)
      }
    })))
  }

  override def into() {
    reset()
    labelStory.setText(story(MathUtils.clamp(LevelLoader.depth - 1, 0, story.length - 1)))
  }
}