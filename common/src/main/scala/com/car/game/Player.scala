package com.car.game

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Pixmap.Format
import com.car.l.screens.LevelTestScreen
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.math.MathUtils
import com.car.l.Assets.assets
import com.car.l.screens.GameOverScreen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.car.l.screens.LevelTestScreen

class PlayerProcessor(player: Player) extends InputProcessor {
  val moveKeys = Map(Keys.W -> 0, Keys.D -> 1, Keys.S -> 2, Keys.A -> 3)
  val shootKeys = Map(Keys.UP -> 1, Keys.RIGHT -> 2, Keys.DOWN -> 4, Keys.LEFT -> 8)

  override def keyDown(keycode: Int): Boolean = {
    if (moveKeys.contains(keycode)) player.movement(moveKeys(keycode)) = true
    if (shootKeys.contains(keycode)) {
      player.shootDir += shootKeys(keycode)

    }
    true
  }

  override def keyUp(keycode: Int): Boolean = {
    if (moveKeys.contains(keycode)) player.movement(moveKeys(keycode)) = false
    if (shootKeys.contains(keycode)) {
      player.shootDir -= shootKeys(keycode)
    }
    if (Keys.SPACE == keycode) {
      player.usePotion
    }
    true
  }

  override def keyTyped(char: Char) = false
  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
  override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
  override def touchDragged(screenX: Int, screenY: Int, pointer: Int) = false
  override def mouseMoved(screenX: Int, screenY: Int) = false
  override def scrolled(amount: Int) = false

}

class Player(animations: Map[String, Animation], var screen: LevelTestScreen) extends Entity(animations, 48, 7) {
  val SPEED = 5
  val WEAPON_COOLDOWN = 0.2f
  var movement = Array(false, false, false, false)
  var shootDir = 0
  var shootCooldown = 0f
  
  var superSayan = false
  
  val maxHealth = 100
  var currentHealth = maxHealth

  val maxSpirit = 10f
  var currentSpirit: Float = maxSpirit

  var score = 0
  var keys = 0
  var potions = 0

  setPosition(4 * 48, 4 * 48)

  def modSpirit(delta: Float) {
    currentSpirit = MathUtils.clamp(currentSpirit + delta, 0, maxSpirit)
  }

  def modHealth(delta: Int) {
    currentHealth = MathUtils.clamp(currentHealth + delta, 0, maxHealth)
    if (delta < 0) assets.playSound("hurt")
  }

  def newLevel(level: Level) {
    setPosition(level.startCoord._1, level.startCoord._2)
    modSpirit(4f)
    shootDir = 0
    shootCooldown = 0f
    movement = Array(false, false, false, false)
  }

  def reset() {
    currentSpirit = maxSpirit
    currentHealth = maxHealth
    keys = 0
    shootDir = 0
    shootCooldown = 0f
    movement = Array(false, false, false, false)
    score = 0
    potions = 0
  }

  def usePotion() {
    if (potions > 0) {
      screen.enemySet.foreach(enemy => if (scala.math.max(getX - enemy.getX, getY - enemy.getY) < 300) enemy.health -= 20)
      screen.stage.addActor(new Fader(screen))
      assets.playSound("explosion")
      potions -= 1
    }
  }

  override def act(delta: Float) {
    if (!(currentAnimation == "throw" && !animations(currentAnimation).isAnimationFinished(animationTimer))) {
      if (movement(0) || movement(1) || movement(2) || movement(3)) swapAnimation("walk")
      else swapAnimation("idle")
    }

    shootCooldown += delta

    super.act(delta)

    modSpirit(-delta)

    var deltaV = new Vector2(0, 0)
    if (movement(0)) { deltaV.add(0, 1); setRotation(0) }
    if (movement(2)) { deltaV.add(0, -1); setRotation(180) }
    if (movement(1)) { deltaV.add(1, 0); if (movement(0)) setRotation(315) else if (movement(2)) setRotation(225) else setRotation(270) }
    if (movement(3)) { deltaV.add(-1, 0); if (movement(0)) setRotation(45) else if (movement(2)) setRotation(135) else setRotation(90) }
    deltaV.nor().scl(SPEED)

    scan(0.05f, new Vector2(deltaV.x, 0).nor, deltaV.len)
    scan(0.05f, new Vector2(0, deltaV.y).nor, deltaV.len)

    if (screen.level.get.collidesWith(boundingBox, Tile.STAIRS_DOWN)) {
      assets.playSound("steps")
      screen.game.transitionToScreen(screen.game.levelEndScreen)
    }

    if (currentSpirit == 0 || currentHealth == 0) {
      screen.game.transitionToScreen(screen.game.gameOverScreen)
      assets.playSound("death")
    }

    screen.enemySet.foreach(enemy =>
      if (enemy.collidesWith(this)) {
        EnemyPool.returnEnemy(screen.enemySet, enemy)
        modHealth(-enemy.damage)
      })

    def spawnShot(dir: Int) {
      swapAnimation("throw")
      assets.playSound("throw")
      val shot = ShotPool.getShot(screen)
      var shotV: Vector2 = new Vector2(0, 0)
      if ((1 & shootDir) > 0) shotV.add(0, 1)
      if ((2 & shootDir) > 0) shotV.add(1, 0)
      if ((4 & shootDir) > 0) shotV.add(0, -1)
      if ((8 & shootDir) > 0) shotV.add(-1, 0)
      val shotPos = new Vector2(getX + 48 / 2f, getY + 48 / 2f).add(shotV.scl(27f))
      shot.setPosition(shotPos.x, shotPos.y)
      shot.deltaV = shotV.nor.scl(SPEED * 3)
    }

    if (shootDir > 0 && shootCooldown > WEAPON_COOLDOWN && !(shootDir == 5 || shootDir == 10)) {
      spawnShot(shootDir)
      shootCooldown = 0
    }
  }

  override def collides(): Boolean = {
    val spawnCol = !(screen.blockSet.forall(block => !(block.collidesWith(this)))) || !(screen.spawnSet.forall(spawn => !(spawn.collidesWith(this))))
    (screen.level.get.collidesWith(boundingBox, Tile.WALL) || screen.level.get.collidesWith(boundingBox, Tile.WATER) || screen.collidesWithBlock(this)) || spawnCol
  }
}

class Fader(var screen: LevelTestScreen) extends Actor {
  val FADE_TIME = 0.5f
  var fadeTimer = 0f
  val whiteTex = {
    val pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888)
    pixmap.setColor(1, 1, 1, 1f)
    pixmap.fill()
    new Texture(pixmap)
  }  
  
  override def act(delta: Float){
    fadeTimer += delta
    if(fadeTimer > FADE_TIME){
      remove
    }
  }
  
  override def draw(batch: SpriteBatch, parentAlpha: Float){
    batch.setColor(1, 1, 1, 1-fadeTimer*2)
    batch.draw(whiteTex, 0, 0, screen.player.getX + 400, screen.player.getY + 400)
    batch.setColor(Color.WHITE)
  }
  
}