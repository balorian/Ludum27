package com.car.l

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.Application
import com.car.l.screens.MovementTestScreen
import com.car.l.screens.AbstractScreen
import com.car.l.screens.SplashScreen
import com.car.l.screens.TransitionScreen

class LudumGame extends Game {
  lazy val testScreen = new MovementTestScreen(this)
  lazy val transitionScreen = new TransitionScreen(this)
  var currentScreen: Option[AbstractScreen] = None

  override def create() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG)
    val splashScreen = new SplashScreen(this)
    splashScreen.create
    splashScreen.resize(Gdx.graphics.getWidth(),
      Gdx.graphics.getHeight())
    currentScreen = Some(splashScreen)
    this.setScreen(splashScreen)
  }

  def transitionToScreen(screen: AbstractScreen) {
    Gdx.input.setInputProcessor(transitionScreen.inputProcessor)

    transitionScreen.current = currentScreen
    transitionScreen.next = Some(screen)
    transitionScreen.reset

    setScreen(transitionScreen)
  }

  def transitionComplete() {
    Gdx.input.setInputProcessor(transitionScreen.next.get.inputProcessor)
    currentScreen = transitionScreen.next
    setScreen(transitionScreen.next.get)
  }
}
