# BYOW: Build your own World 🌎

Updated: 9/6/26
Contributors: Sean Lin (now Stanford MS), Dayne Tran (now at Applied Intuition)

## Overview
Build your own World is a simple offline game that you can play in single player or PvP mode. It was the iconic project from UC Berkeley's CS61B course specifically taught by Josh Hug (Fall '20).  Map generation, connection, and player data/metadata are coded in data structures built from the ground up without generative AI, serving as a good exercise in data structures and algorithms. 

## Setup

Requirement: Java 8+ 

Windows workflow
1. clone the repo
2. From the top-level folder BYOW, compile the contents of ```byow\Core``` and ```byow\TileEngine``` via the following command in your terminal. 

```javac -cp ".;.\library-fa20\javalib\*" byow\Core\*.java byow\TileEngine\*.java```

3. Start playing with ```java -cp ".;.\library-fa20\javalib\*" byow\Core\Main.java```. Follow the instructions. 

## How to Play

Player left controls with WASD.  Player right controls with IJKL. Get more flowers than your adversary to win!