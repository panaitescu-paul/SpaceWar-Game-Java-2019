# SpaceWar-Game-2019

***Space War*** is a **Shoot'em up type of game**. The player controls a **ship** that can **shoot enemies** and **get items** to improve it along the way. In order to advance in the game, you need to **survive the waves of enemies** by destroying them, or avoiding them. The **game will end** if the ship loses all its lives, otherwise will run indefinitely and will **increase the score** based on time passed, enemies destroyed, and items picked up.

## Initially, the **ship** has: 
- 1 live
- 1 bullet in a set
- No shield

## There are 3 types of **items**:
- **Health item** - to increase ship health up to 3
- **Multiple Bullets item** - to increase the ship bullets up to 5
- **Shield item** - to get a ship shield

## There are 3 types of **enemies**:
- **Basic enemy** - with 3 live
- **Shooting enemy** - with 3 lives, and the ability to shoot
- **Shielded enemy** - with 1 live, but it cannot be destroyed by ship bullets

## Interesting problems solved

### 1. Advancing in Game Levels
- Enemy Waves - customizable
- Item Waves - customizable

### 2. Enemy - shooting bullets
- Enemy Types
- Enemy Bullet Spawn
- Enemy Bullet Movement

### 3. Spaceship - shooting bullets
- Detect collision between spaceship bullet and enemy
- Bullet disappears off screen
- Customizable pace shooting (on units / off units)
- Multiple Bullets power up

### 4. Individual enemy movement

### 5. Parallax Background

### 6. Scoring System
The score will increase when you:
- Advance in the game (6 points every second)
- Shoot a Basic enemy (10 points)
- Shoot a Shooting enemy (25 points)
- Shoot a Shielded enemy (1 point)
- Collect a Health item (10 points)
- Collect a Multiple Bullets item (10 points)
- Collect a Shield item (10 points)
