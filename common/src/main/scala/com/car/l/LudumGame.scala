package com.car.l

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.Application
import com.car.l.screens.MovementTestScreen
import com.car.l.screens.AbstractScreen
import com.car.l.screens.SplashScreen

class LudumGame extends Game {
  lazy val testScreen = new MovementTestScreen(this)
  //  lazy val transitionScreen = new TransitionScreen
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
    currentScreen.get.outOf()
    screen.into()
    currentScreen = Some(screen)
    this.setScreen(screen)
    //    transitionScreen = new TransitionScreen(this);
    //    Gdx.input.setInputProcessor(transitionScreen.getInputProcessor());
    //
    //    transitionScreen.setCurrent(this.getScreen());
    //    transitionScreen.setNext(screen);
    //    transitionScreen.reset();
    //
    //    transitionScreen.resize(Gdx.graphics.getWidth(),
    //      Gdx.graphics.getHeight());
    //
    //    setScreen(transitionScreen);
  }
}
