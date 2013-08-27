package com.car.l

import com.car.l.Assets.assets
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.Gdx
import scala.collection.convert.Wrappers._
import com.car.game.Level
import com.car.game.Tile
import com.car.l.screens.LevelTestScreen
import com.car.game.SpawnPoint
import com.badlogic.gdx.graphics.g2d.Animation
import com.car.game.Key
import com.car.game.Door
import com.car.game.BreakBlock
import com.car.game.BreakBlock
import com.car.game.Treasure
import com.car.game.Meat
import com.car.game.Potion
import com.car.game.PowerPotion
import com.car.game.PowerPotion
import com.car.game.SayanOrb


object LevelLoader {
  val LOG_TAG = "LevelLoader"

  val MAPS_DIR = "maps/"
  val MAP_SUFFIX = ".png"

  val KEY = 0xFFD800FF
  val DOOR = 0x7D7D00FF
  val SPAWN_1 = 0x4CFF00FF
  val SPAWN_2 = 0x287D00FF
  val SPAWN_3 = 0x194C00FF
  val BREAKABLE_WALL = 0xD67FFFFF
  val TREASURE = 0x0026FFFF
  val MEAT = 0xFFBFECFF
  val POTION = 0xFF6A00FF
  val POWER_POTION = 0x7F3300FF

  val levels = List("level0", "level1", "level10", "level2", "level3", "level4", "level5", "level6", "level7", "level8", "level9", "level11", "level12")
  
  var depth = 1
  
  def nextLevelFrom(current: String): String = {
    val l = levels.takeRight(levels.length - depth).head
    depth += 1
    l
  }

  def load(key: String, screen: LevelTestScreen): Level = {
    def createSpawn1(x: Int, y: Int): SpawnPoint = {
      val sp = new SpawnPoint(Map("idle" -> new Animation(10f, assets.creatureAtlas.createSprites("spawn3"), Animation.LOOP),
        "damaged" -> new Animation(10f, assets.creatureAtlas.createSprites("spawn3_damaged"), Animation.LOOP)),
        screen, 20, 'skeleton)
      sp.setPosition(x, y)
      sp
    }

    def createSpawn2(x: Int, y: Int): SpawnPoint = {
      val sp = new SpawnPoint(Map("idle" -> new Animation(10f, assets.creatureAtlas.createSprites("spawn"), Animation.LOOP),
        "damaged" -> new Animation(10f, assets.creatureAtlas.createSprites("spawn_damaged"), Animation.LOOP)),
        screen, 35, 'zombie)
      sp.setPosition(x, y)
      sp
    }

    def createSpawn3(x: Int, y: Int): SpawnPoint = {
      val sp = new SpawnPoint(Map("idle" -> new Animation(10f, assets.creatureAtlas.createSprites("spawn2"), Animation.LOOP),
        "damaged" -> new Animation(10f, assets.creatureAtlas.createSprites("spawn2_damaged"), Animation.LOOP)),
        screen, 45, 'ghost)
      sp.setPosition(x, y)
      sp
    }
    
    def createKey(x: Int, y: Int): Key = {
      val key = new Key(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprites("key"), Animation.LOOP_PINGPONG)), screen)
      key.setPosition(x, y)
      key
    }

    def createTreasure(x: Int, y: Int): Treasure = {
      val t = new Treasure(Map("idle" -> new Animation(10f, assets.creatureAtlas.createSprites("treasure"), Animation.LOOP)), screen)
      t.setPosition(x, y)
      t
    }

    def createSayanOrb(x: Int, y: Int): SayanOrb = {
      val t = new SayanOrb(Map("idle" -> new Animation(0.2f, assets.creatureAtlas.createSprites("soul_orb"), Animation.LOOP_PINGPONG)), screen)
      t.setPosition(x - 8, y + 16)
      t
    }
    
    def createDoor(x: Int, y: Int): Door = {
      val key = new Door(Map("idle" -> new Animation(10f, assets.creatureAtlas.createSprites("door"), Animation.LOOP)), screen)
      key.setPosition(x, y)
      key
    }

    def createBlock(x: Int, y: Int): BreakBlock = {
      val block = new BreakBlock(screen, 14)
      block.setPosition(x, y)
      block
    }

    def createMeat(x: Int, y: Int): Meat = {
      val meat = new Meat(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprite("meat"))), screen)
      meat.setPosition(x, y)
      meat
    }
    
    def createPotion(x: Int, y: Int): Potion = {
      val potion = new Potion(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprite("potion"))), screen)
      potion.setPosition(x, y)
      potion
    }
    
    def createPowerPotion(x: Int, y: Int): PowerPotion = {
      val potion = new PowerPotion(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprite("potion_red"))), screen)
      potion.setPosition(x, y)
      potion
    }
    
    def spawnKey(i: Int, j: Int) = screen.collectablesSet.add(createKey(i, j))
    def spawnTreasure(i: Int, j: Int) = screen.collectablesSet.add(createTreasure(i, j))
    def spawnSayanOrb(i: Int, j: Int) = screen.collectablesSet.add(createSayanOrb(i, j))
    def spawnDoor(i: Int, j: Int) = screen.doorSet.add(createDoor(i, j))
    def spawnPoint1(i: Int, j: Int) = screen.spawnSet.add(createSpawn1(i, j))
    def spawnPoint2(i: Int, j: Int) = screen.spawnSet.add(createSpawn2(i, j))
    def spawnPoint3(i: Int, j: Int) = screen.spawnSet.add(createSpawn3(i, j))
    def spawnBreakable(i: Int, j: Int) = screen.blockSet.add(createBlock(i, j))
    def spawnMeat(i: Int, j: Int) = screen.collectablesSet.add(createMeat(i, j))
    def spawnPotion(i: Int, j: Int) = screen.collectablesSet.add(createPotion(i, j))
    def spawnPowerPotion(i: Int, j: Int) = screen.collectablesSet.add(createPowerPotion(i, j))

    val levelData = new Pixmap(Gdx.files.classpath(MAPS_DIR + key + MAP_SUFFIX));
    val width = levelData.getWidth()
    val height = levelData.getHeight()
    val tilemap = new Array[Int](width * height)

    var sc: (Float, Float) = (0, 0)

    for (j <- 0 until height; i <- 0 until width) {
      val pixel = levelData.getPixel(i, j)

      def flip(j: Int) = height - (j + 1)

      val x = i * Tile.TILE_SIZE
      val y = flip(j) * Tile.TILE_SIZE
      val index = flip(j) * width + i

      pixel match {
        case KEY =>
          tilemap(index) = Tile.GROUND; spawnKey(x, y)
        case DOOR =>
          tilemap(index) = Tile.GROUND; spawnDoor(x, y)
        case SPAWN_1 =>
          tilemap(index) = Tile.GROUND; spawnPoint1(x, y)
        case SPAWN_2 =>
          tilemap(index) = Tile.GROUND; spawnPoint2(x, y)
        case SPAWN_3 =>
          tilemap(index) = Tile.GROUND; spawnPoint3(x, y)
        case BREAKABLE_WALL =>
          tilemap(index) = Tile.GROUND; spawnBreakable(x, y)
        case TREASURE =>
          tilemap(index) = Tile.GROUND; spawnTreasure(x, y)
        case MEAT =>
          tilemap(index) = Tile.GROUND; spawnMeat(x, y)
        case POTION =>
          tilemap(index) = Tile.GROUND; spawnPotion(x, y)
        case POWER_POTION =>
          tilemap(index) = Tile.GROUND; spawnPowerPotion(x, y)
        case Tile.STAIRS_UP =>
          tilemap(index) = pixel; sc = (x, y)
        case Tile.PEDESTAL =>
          tilemap(index) = pixel; spawnSayanOrb(x,y)
        case _ => tilemap(index) = pixel
      }
    }

    new Level(width, height, tilemap, sc, key)
  }
}

class LevelLoader {

}