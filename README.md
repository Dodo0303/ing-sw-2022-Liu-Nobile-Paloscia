# **Eriantys**

<div style="text-align: center;">

The repository is dedicated to the final project of "Ingegneria del Software (2021/2022)", which requires a java implementation of the game [Eriantys](https://www.craniocreations.it/prodotto/eriantys/).

</div>

* [Group Components](#Group-Components)
* [Features](#features)
* [Instructions](#instructions)


---
## Group Components

| Surname | Name | E-mail | Student number |
|:--------|:-----|:-------|:---------------|
| Liu | Yixin | yixin.liu@mail.polimi.it | 940625 |
| Nobile | Domenico | domenico.nobile@mail.polimi.it | 937096|
| Paloscia | Claudio | claudio.paloscia@mail.polimi.it | 932505|

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

## Instructions

Into *deliverables/jar* folder can be found two files .jar:
+ Eriantys.jar
+ Eriantys_M1.jar

The only requirement to run the game is to have installed JDK17.

The file "Eriantys.jar" can be executed in any OS different from MacOS with M1:
```sh
java -jar Eriantys.jar
```

The file "Eriantys_M1.jar" can be executed from MacOS with M1:
```sh
java -jar Eriantys_M1.jar
```

The game will begin and the user will be asked to select between server, CLI and GUI.
