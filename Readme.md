# Overview
synopview is a synoptic viewer of the SLS machine that is configured by text files that are generated out of the 'holy list'.

Right now it is not fully functional as it depends on cdev which is not supported and working any more.


# Usage

Run Synoptic Viewer (as java application):

```bash
java [-DSV_DIR=path/] SV gatewayServerPC
```
[]                 options to specify where the resource files SV_*.txt are located
                   ex: -D=/devl/sls/lib/sv/

gatewayServerPC:   PC where the Cdev GatewayServer is running
                   ex: PC2294


```bash
java -classpath $CLASSPATH:/devl/lib/cdev.jar:/devl/sls/lib/sv/sv.jar SV pc2294
```

```bash									 
java -DSV_DIR=/devl/sls/lib/sv/ -classpath $CLASSPATH:/devl/lib/cdev.jar:/devl/sls/lib/sv/sv.jar SV pc2294
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
Create synoptic viewer archive:

```bash
jar cvf sv.jar *.class svp/devices/*.class svp/parser/*.class svp/visual/*.class svp/visual/tree/*.class sls.jpg
```
