package com.car.l

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.Application
import com.car.l.screens.LevelTestScreen
import com.car.l.screens.AbstractScreen
import com.car.l.screens.SplashScreen
import com.car.l.screens.TransitionScreen
import com.car.l.screens.LevelEndScreen
import com.car.l.screens.GameOverScreen
import com.car.l.screens.MenuScreen

class LudumGame extends Game {
  lazy val testScreen = new LevelTestScreen(this)
  lazy val transitionScreen = new TransitionScreen(this)
  lazy val levelEndScreen = new LevelEndScreen(this, testScreen)
  lazy val gameOverScreen = new GameOverScreen(this)
  lazy val mainMenuScreen = new MenuScreen(this)

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
    currentScreen = transitionScreen.next
    Gdx.input.setInputProcessor(currentScreen.get.inputProcessor)
    currentScreen.get.justDraw = false
    setScreen(currentScreen.get)
  }
}
