# Overview
synopview is a synoptic viewer of the SLS machine that is configured by text files that are generated out of the 'holy list'.

Right now it is not fully functional as it depends on cdev which is not supported and working any more.


# Usage

To run SynopView use:

```bash
java -jar synopview.jar
```							 

# Known Problems
* When reducing the frame size, there are problems when the frame is small...

# Ideas for Improvements
* Table to select what is drawn (Bends, Quads,Wigglers and Ring, Booster...)
* When a device is selected in the tree, this device should be highligthed in the drawing
  and/or be the viewport be set to this device...
* Possibility to select devices from sectors
* Start panel for more than one device  (from the select panel?)
* Cdev Java when 1 property is null the rest is not avaible... find a way aroun


# Development
To build SynopView and to create an all in one Jar use:

```bash
./gradlew fatJar
```

Afterwards the build library is available in `build/libs`.

# Notes
* Eventually Andreas Streun knows more about the generation of the required input files.
