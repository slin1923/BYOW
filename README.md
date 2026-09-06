# BYOW: Build your own World 🌎

Updated: 9/6/26

Contributors: Sean Lin (now Stanford MS), Dayne Tran (now at Applied Intuition)

## Rules
Build your own World is a simple offline PvP game where you try to collect more flowers than your opponent in a randomly generated world of rooms and hallways.  Player 1 (L) uses **WASD** to move, and Player 2 (R) uses **IJKL** to move.  Optionally, you can toggle all the lights on with **B**. 

## Setup

Requirement: Java 8+ 

Windows workflow
1. clone the repo, then `cd BYOW`
2. Compile

>`javac -cp ".;.\javalib\*" byow\Core\*.java byow\TileEngine\*.java`.  

3. Start playing

>`java -cp ".;.\javalib\*" .\byow\Core\Main.java`

## Background

BYOW was the iconic project from UC Berkeley's CS61B course specifically taught by Josh Hug (Fall '20).  Map generation, connection, and player data/metadata are coded in data structures built from the ground up without generative AI, serving as a good exercise in data structures and algorithms. 
