package com.car.game

import com.badlogic.gdx.graphics.g2d.Animation
import com.car.l.screens.LevelTestScreen
import com.car.l.Assets
import com.badlogic.gdx.math.MathUtils
import scala.collection.mutable.HashSet

abstract class Collectable(
  animations: Map[String, Animation],
  size: Int, offset: Int,
  screen: LevelTestScreen,
  set: HashSet[Entity],
  val sound: String) extends Entity(animations, size, offset) {
  override def act(delta: Float) {
    super.act(delta)
    if (screen.player.collidesWith(this) || collisionPartner.nonEmpty) pickupWithSound(screen.player)
  }

  def pickupWithSound(player: Player) {
    Assets.assets.playSound(sound)
    pickup(player)
    set.remove(this)
    remove
  }

  def pickup(player: Player) = {
  }
}

class Key(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 48, 5, screen, screen.collectablesSet, "key") {
  override def pickup(player: Player) = player.keys += 1
}

class SoulShard(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 48, 5, screen, screen.collectablesSet, "soul") {
  override def pickup(player: Player) = player.currentSpirit = MathUtils.clamp(player.currentSpirit + 1, 0, player.maxSpirit)
}

class Meat(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 48, 5, screen, screen.collectablesSet, "meat") {
  override def pickup(player: Player) = player.currentHealth = MathUtils.clamp(player.currentHealth + 10, 0, player.maxHealth)
}

class Door(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 48, 0, screen, screen.blocksSet, "door") {
  override def pickupWithSound(player: Player) {
    if (player.keys > 0) { player.keys -= 1; super.pickupWithSound(player) }
    collisionPartner = None
  }
}