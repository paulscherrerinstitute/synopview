package ch.psi.synopview;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import ch.psi.synopview.svp.devices.DevicesData;
import ch.psi.synopview.svp.devices.GroupData;
import ch.psi.synopview.svp.devices.MachinesData;
import ch.psi.synopview.svp.devices.SimpleDevice;
import ch.psi.synopview.svp.devices.SystemsData;
import ch.psi.synopview.svp.visual.DeviceColorModel;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

/**
 * Parser providing functions to parse the various input files
 */
public class ConfigurationParser {

	public static DevicesData parse(String filename, DeviceColorModel colorModel) {
		filename = expandFilename(filename);
		
		DevicesData data = new DevicesData();

		Consumer<String[]> c = new Consumer<String[]>() {
			@Override
			public void accept(String[] tokens) {
				// device-name
				String name = tokens[0];
				// device-parent
				String parent = tokens[1];
				// command
				String command = tokens[2];
				command = command.substring(1, command.length()-2); // remove quotes
				if(command.trim().isEmpty()){
					command = null;
				}
				
				// machine-index
				int machine = Integer.parseInt(tokens[3]);
				// system-index
				int system = Integer.parseInt(tokens[4]);
				// color-index
				int color = Integer.parseInt(tokens[5]);
				
				// coordinates
				int tokenIndex = 6;
				int i = 0;
				List<Integer> x = new ArrayList<>();
				List<Integer> y = new ArrayList<>();
				
				while ((tokenIndex+1) < tokens.length) { // always read 2 tokens per round
					int newX = (int) (Double.parseDouble(tokens[tokenIndex]) * 1000);
					int newY = (int) (- Double.parseDouble(tokens[tokenIndex+1]) * 1000);
					tokenIndex += 2;
					
					x.add(newX);
					y.add(newY);
					
					if (newX < data.minX)
						data.minX = newX;
					if (newX > data.maxX)
						data.maxX = newX;

					if (newY < data.minY)
						data.minY = newY;
					if (newY > data.maxY)
						data.maxY = newY;

					i++;
				}

				data.addDevice(new SimpleDevice(name, parent, command, machine, system, color, new java.awt.Polygon(toIntArray(x), toIntArray(y), i), colorModel));
			}
		};
		
		read(filename, c);
		return data;
	}
	
	public static DeviceColorModel parseColors(String filename) {
		filename = expandFilename(filename);

		DeviceColorModel model = new DeviceColorModel();
		
		// id red green blue
		read(filename, tokens->model.addColor(Integer.parseInt(tokens[0]), new Color(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]))));
		return model;
	}

	
	public static Hashtable<String, GroupData> parseGroups(String filename) {
		filename = expandFilename(filename);

		Hashtable<String, GroupData> groups = new Hashtable<String, GroupData>();
		
		// group parent
		read(filename, tokens->groups.put(tokens[0], new GroupData(tokens[0], tokens[1])));
		return groups;
	}

	public static MachinesData parseMachines(String filename) {
		filename = expandFilename(filename);
		
		MachinesData machines = new MachinesData();
		
		// index name
		read(filename, tokens->machines.addMachine(Integer.parseInt(tokens[0]), tokens[1]));
		return machines;
	}

	public static SystemsData parseSystems(String filename) {
		filename = expandFilename(filename);
		
		SystemsData systems = new SystemsData();
		
		// index name
		read(filename, tokens->systems.addSystem(Integer.parseInt(tokens[0]), tokens[1]));
		return systems;
	}

	/**
	 * Read csv files
	 * @param fileName	File to read
	 * @param consumer	Consumer consuming the tokenized line
	 */
	private static void read(String fileName, Consumer<String[]> consumer){
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.map(String::trim) // remove trailing/leading spaces/tabs/...
				  .filter(line->!line.startsWith("#")) // filter comments
				  .filter(line->!line.isEmpty()) // filter empty lines
//				  .map(line->line.split("\\s+")) // split string
				  // Using a more sophisitcated splitting as we have sometimes " " inside the config files
				  // Taken from: http://stackoverflow.com/questions/366202/regex-for-splitting-a-string-using-space-when-not-surrounded-by-single-or-double
				  .map(new Function<String, String[]>(){ 
					@Override
					public String[] apply(String line) {
						List<String> matchList = new ArrayList<String>();
						Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
						Matcher regexMatcher = regex.matcher(line);
						while (regexMatcher.find()) {
						    matchList.add(regexMatcher.group());
						} 
						return matchList.toArray(new String[matchList.size()]);
					}
					  
				  })
				  .forEach(consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int[] toIntArray(List<Integer> integerList) {
	    int[] intArray = new int[integerList.size()];
	    for (int i = 0; i < integerList.size(); i++) {
	        intArray[i] = integerList.get(i);
	    }
	    return intArray;
	}
	
	private static String expandFilename(String filename){
		String path = System.getProperty("SV_DIR");
		if(path!=null){
			filename = path+File.pathSeparator+filename;
		}
		return filename;
	}

}
