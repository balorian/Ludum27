package com.car.game

import com.badlogic.gdx.graphics.g2d.Animation
import com.car.l.screens.LevelTestScreen
import com.car.l.Assets
import com.badlogic.gdx.math.MathUtils
import scala.collection.mutable.HashSet
import com.badlogic.gdx.math.Vector2

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

  override def collides() = false
}

class Key(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 48, 5, screen, screen.collectablesSet, "key") {
  override def pickup(player: Player) = { player.keys += 1; player.score += 100 }
}

class Potion(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 48, 5, screen, screen.collectablesSet, "potion") {
  override def pickup(player: Player) = { player.potions += 1; player.score += 100 }
}

class PowerPotion(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 48, 5, screen, screen.collectablesSet, "potion") {
  override def pickup(player: Player) = { player.powerTimer = 0}
}

class SoulShard(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 24, 2, screen, screen.collectablesSet, "soul") {
  override def pickup(player: Player) = { player.modSpirit(1f); player.score += 10 }

  val speed = 5f

  override def act(delta: Float) {
    super.act(delta)

    val pv = screen.player.center
    val ov = center

    if (math.sqrt(pv.dst2(ov)) <= 100) {
      val nv = ov.add(pv.sub(ov).nor().mul(speed))
      setPosition(nv.x, nv.y)
    }
  }
}

class Treasure(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 48, 5, screen, screen.collectablesSet, "big_pickup") {
  override def pickup(player: Player) = player.score += 2000
}

class Meat(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 48, 5, screen, screen.collectablesSet, "meat") {
  override def pickup(player: Player) = { player.modHealth(10); player.score += 50 }
}

class Door(animations: Map[String, Animation], screen: LevelTestScreen) extends Collectable(animations, 48, 0, screen, screen.doorSet, "door") {
  override def pickupWithSound(player: Player) {
    if (player.keys > 0) { player.keys -= 1; super.pickupWithSound(player) }
    collisionPartner = None
  }
}

