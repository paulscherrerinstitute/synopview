dec 2000

processInterrestData () is not yet implemented (just the reading of the file and the choiceButton in the
						ResultPanel
						JavaCdev Problems to get Data with sendNoBlock()... example..
						Werners ALIVA-VG-5 sucess getting results, but ARIVA-VG03-1 failed !!!
						

===================================================
old....

currently the Synoptic Viewer is located at:
/home/pss060/sls/grunder/work/sv
/home/pss060/sls/grunder/PROJECTS/synop_view/psi/
----------------------------------------------------------------------------------
currently a Java Cdev Test is placed at
/home/pss060/sls/grunder/work/cdev
cdevTest.java   -> main Test program that defines the number of devices to get
                   sand the chanel name...
JavaCdev.java   -> cdev methods to connect and to cdevGet() and CdevSendNoBlock()
--------------------------------------
/home/pss060/sls/grunder/work/cdevDirectory
java Test [device]
if no device are defined all are checked
==================================================================================

to use the viewer to access cdev the nameserver and the cdevGatewayserver must run
==================================================================================
  cdev nameserver: 
  rsvcServer &

  gatewayServer:
  cdevGateway

to run the SLS Synoptic Viewer you must define this variables
=============================================================
* needed to load the Java Nativ Interface library libgetQuery.so from the current 
  directory... this library will be somewhere else later...
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.

export CLASSPATH=.:/tools/bin:/usr/local/jdk117_v1a/lib/classes.zip:/home/pss060/sls/grunder/PACKAGES/cdev/jlabjava/cdev2.0

* to use java cdev...
export CDEV_NAME_SERVER=pc2294.psi.ch

* a ddl file must be defined to get cdev data from devices (used by jni cdevDirectoryService)
export CDEVDDL=/home/pss060/sls/grunder/DDL/sls.ddl


run Synoptic Viewer (as java application):
==========================================
java [-DSV_DIR=path/]SV gatewayServerPC

[]                 options to specify where the resource files SV_*.txt are located
                   ex: -D=/devl/sls/lib/sv/

gatewayServerPC:   PC where the Cdev GatewayServer is running 
                   ex: PC2294

known problems:
* when reducing the frame size, there are problems when the frame is small...

idees for improofments:
* table to select what is drawn (Bends, Quads,Wigglers and Ring, Booster...)
* when a device is selected in the tree, this device should be highligthed in the drawing
  and/or be the viewport be set to this device... 
* Possibility to select devices from sectors
* start panel for more than one device  (from the select panel?)
* cdev Java when 1 property is null the rest is not avaible... find a way aroun


p.s. to extract java cdev from cvs to your private home...:
===========================================================
cvs co PACKAGES/cdev/jlabjava/cdev2.0/cdev   

===================================================================================
create synoptic viewer archive:
-------------------------------
jar cvf sv.jar *.class svp/devices/*.class svp/parser/*.class svp/visual/*.class svp/visual/tree/*.class sls.jpg 

cp sv.jar /devl/sls/lib/sv/sv.jar   

---------------

create Java Cdev archive:
-------------------------
jar cvf cdev.jar cdev/*.class cdev/data/*.class cdev/clip/*.class cdev/dir/*.class cdev/io/*.class  

execute Synoptic Viewer jar archive:
------------------------------------
java -classpath $CLASSPATH:/devl/lib/cdev.jar:/devl/sls/lib/sv/sv.jar SV pc2294 

java -DSV_DIR=/devl/sls/lib/sv/ -classpath $CLASSPATH:/devl/lib/cdev.jar:/devl/sls/lib/sv/sv.jar SV pc2294 

