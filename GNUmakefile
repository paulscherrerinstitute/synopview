#  $id$

DEVICE = svp/devices/
PARSER = svp/parser/
VISUAL = svp/visual/

T_A=linux
vpath %.cc .
vpath %.java .
vpath %.java $(DEVICE)
vpath %.java $(PARSER)
vpath %.java $(VISUAL)
vpath %.java $(VISUAL)/tree/


ALL = JavaCdev.class \
	Device.class DevicesData.class GroupData.class MenuData.class SimpleDevice.class \
	MachinesData.java SystemsData.java \
	SV.class ResultPanel.class SVControlSystem.class \
	DefineMenu.class DrawingSurface.class \
	ExecProgram.class FolderItem.class Time.class Item.class StatusBar.class \
	Synoptic.class TreeControl.class SaveOutput.java SelectDialog.java \
	SVParseException.class SVParser.class \
	Constants.class HistoryViewData.class ViewHistory.class ViewProperties.class \
	ItemAction.class ScrPane.class \
	sv.jar
##########################################################

all : $(ALL)

%.class : %.java
	javac -classpath ${CLASSPATH}:${SLSBASE}/sls/java/lib/cdev.jar $<

sv.jar : 
	jar cvf sv.jar *.class svp/devices/*.class svp/parser/*.class svp/visual/*.class svp/visual/tree/*.class
	@echo "builded sv.jar"

clean :
	rm -f *.class
	rm -f sv.jar
	rm -f $(DEVICE)*.class
	rm -f $(PARSER)*.class
	rm -f $(VISUAL)*.class
	rm -f $(VISUAL)/tree/*.class
