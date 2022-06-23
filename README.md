# ing-sw-2022-Liu-Nobile-Paloscia

<div style="text-align: center;">
  
The repository is dedicated to the final project of "Ingegneria del Software (2021/2022)", which requires a java implementation of the game [Eriantys](https://www.craniocreations.it/prodotto/eriantys/).
  
</div>


* [Features](#features)
* [Game Rules](#Game-Rules)

---

## Features

| Functionality | Status |
|:-----------------------|:------------------------------------:|
| Simplified Ruleset | 🟢 |
| Complete Ruleset | 🟢 |
| Socket |🟢 |
| CLI | 🟢 |
| GUI | 🟢 |
| All character cards | 🟢 |
| 4 Player Game | 🟢 |
| Multiple matches | 🟢 |
| Persistence | 🔴 |
| Resilience to disconnections | 🔴 |

## Game Rules

* The game is played over a number of rounds until 
  * one player has placed all of their towers (thus win the game);
  * there are no more students left;
  * only 3 groups of Islands remain on the table.
* Each round has two phases : 
  * Planning phase
    * Add new students to cloud tiles. 
    * In turn order, choose and play an assistant card. 
  * Action phase (starting with the player with the **lowest** value assistant card and proceeding in **ascending order**) 
    * Move **3 students** to either your Dining Room or a Island.
    * Move **Mother Nature** to an Island. At the end of the movement, there are 2 things will happen 
      * Controlling an Island if the Island does not have a tower on it, otherwise
      * Conquering an Island.
    * Unify adjacent Islands with same color of towers. Once unified, the Islands will be considered as a single Island.
    * Choose a cloud tile and take 3 new students. 
