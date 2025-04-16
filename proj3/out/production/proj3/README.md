# Build Your Own World Design Document

**Partner 1:** Dayne Tran

**Partner 2:** Sean Lin

## Classes and Data Structures

## Algorithms

## Persistence

##Brainstorming (to delete later)
For Room expansion, use two randomized probabilities that the original space will 
continue to extend North/South or East/West.  Stop cases for expansion on lat or lon axis are:
1. Bad probability for axis is reached
2. Room can no longer expand in said axis

Sean: Holistic ideas on modularity

Point class contains coordinates and instance variable for type (Wall, RoomSpace, Nothing)

Room contains instance variables for bounds as well as methods for expansion!
Room also contains list of points contained in said room (including walls)

Create new class called Hallways: instance variables and methods TBD