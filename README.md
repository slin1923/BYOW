# BYOW: Build your own World 🌎

Updated: 9/6/26

Contributors: Sean Lin (now Stanford MS), Dayne Tran (now at Applied Intuition)

## Overview
Build your own World is a simple offline game that you can play in single player or PvP mode. It was the iconic project from UC Berkeley's CS61B course specifically taught by Josh Hug (Fall '20).  Map generation, connection, and player data/metadata are coded in data structures built from the ground up without generative AI, serving as a good exercise in data structures and algorithms. 

## Setup

Requirement: Java 8+ 

Windows workflow
1. clone the repo, then in terminal from the top level directory...
2. initialize the github embedded submodule ```git submodule update --init --recursive```.  (BYOW depends on a 3rd-party library from 61B). 
3. Compile via ```javac -cp ".;.\library-fa20\javalib\*" byow\Core\*.java byow\TileEngine\*.java```.  This compiles the contents of ```byow\Core``` and ```byow\TileEngine``` and you should see ```.class``` files appear. 
4. Start playing with ```java -cp ".;.\library-fa20\javalib\*" byow\Core\Main.java```!s

## How to Play

Player left controls with **WASD**.  Player right controls with **IJKL**. Get more flowers than your adversary to win!