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

object LevelLoader {
  val LOG_TAG = "LevelLoader"

  val MAPS_DIR = "maps/"
  val MAP_SUFFIX = ".png"

  val KEY = 0xFFD800FF
  val DOOR = 0x7D7D00FF
  val SPAWN_1 = 0x4CFF00FF
  val SPAWN_2 = 0x287D00FF
  val BREAKABLE_WALL = 0xD67FFFFF

  def load(key: String, screen: LevelTestScreen): Level = {
    def createSpawn1(x: Int, y: Int): SpawnPoint = {
      val sp = new SpawnPoint(Map("idle" -> new Animation(10f, assets.creatureAtlas.createSprites("spawn"), Animation.LOOP),
        "damaged" -> new Animation(10f, assets.creatureAtlas.createSprites("spawn_damaged"), Animation.LOOP)),
        screen, 20)
      sp.setPosition(x, y)
      sp
    }

    def createSpawn2(x: Int, y: Int): SpawnPoint = {
      val sp = new SpawnPoint(Map("idle" -> new Animation(10f, assets.creatureAtlas.createSprites("spawn2"), Animation.LOOP),
        "damaged" -> new Animation(10f, assets.creatureAtlas.createSprites("spawn2_damaged"), Animation.LOOP)),
        screen, 40)
      sp.setPosition(x, y)
      sp
    }

    def createKey(x: Int, y: Int): Key = {
      val key = new Key(Map("idle" -> new Animation(0.10f, assets.creatureAtlas.createSprites("key"), Animation.LOOP)), screen)
      key.setPosition(x, y)
      key
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
    
    def spawnKey(i: Int, j: Int) = screen.collectablesSet.add(createKey(i, j))
    def spawnDoor(i: Int, j: Int) = screen.doorSet.add(createDoor(i, j))
    def spawnPoint1(i: Int, j: Int) = screen.spawnSet.add(createSpawn1(i, j))
    def spawnPoint2(i: Int, j: Int) = screen.spawnSet.add(createSpawn2(i, j))
    def spawnBreakable(i: Int, j: Int) = screen.blockSet.add(createBlock(i, j))

    val levelData = new Pixmap(Gdx.files.classpath(MAPS_DIR + key + MAP_SUFFIX));
    val width = levelData.getWidth()
    val height = levelData.getHeight()
    val tilemap = new Array[Int](width * height)

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
        case BREAKABLE_WALL =>
          tilemap(index) = Tile.GROUND; spawnBreakable(x, y)
        case _ => tilemap(index) = pixel
      }
    }

    new Level(width, height, tilemap)
  }
}

class LevelLoader {

}