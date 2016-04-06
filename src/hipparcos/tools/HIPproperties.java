// Written by William O'Mullane for the 
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.tools;

import java.io.*;
import java.util.*;


/** Class to load properties from file and then be a singleton for thoose 
    properties. Has smart defaults though just in case the file is not 
	found */

public class HIPproperties {
	static Properties props=null;

	/** Static initializer ... */
	static void init (){
		props = new Properties();
		if (Browser.inApplet()) {
				props.put("hipurl","https://hipparcos-tools.cosmos.esa.int/cgi-bin/HIPcatalogueSearch.pl");
		} else {
		   String toolProps = System.getProperty("toolProps","hipparcos.properties");
			try {
				String fn = locateFile(toolProps);
				FileInputStream in = new FileInputStream(fn);	
				props.load(in);
				in.close();
			} catch (Exception e) {
				System.err.println(" Can not find "+toolProps +" use -DtoolProps=path to specify location if not in current dir");
				props.put("hipurl","http://astro.estec.esa.nl/hipparcos_scripts/HIPcatalogueSearch.pl");
				props.put("browser","netscape");
				System.exit(1);
			}
		}
	}

	public static String getProperty (String prop) {
		if (props==null) init();
		return props.getProperty(prop);
	}
	
	public static String getProperty (String prop,String def) {
		if (props==null) init();
		return props.getProperty(prop,def);
	}
	
	
	 /**
     * Locates a file across the local file system, given a set of directories.
     * <p>
     * If the file is absolute and it exists the directory set is ignored and that full filename is returned. If the
     * file is absolute but doesn't exist, then the short filename (ie without the path) is taken in the search below.
     * </p>
     * <p>
     * The directories are taken from the value of the property directoryProp. If this does not exist, the value of the
     * parameter defaultDirectories is used.
     * </p>
     * 

     * @param filename
     *            the file to search for.
     * 
     * @return String
	 * @throws Exception 
     */
    public static String locateFile(  String filename) throws Exception {

        String shortFileName = filename;

        // Check if file is absolute.
        // If so, use that name if it exists,
        // otherwise use the short file name (at the end of the path)
        // as the base file name to search against.
        File absFile = new File(filename);
        if (absFile.isAbsolute()) {
            if (absFile.exists()) {
                return filename;
            }
            shortFileName = absFile.getName();
        }

     

        final String[] propDirs ={".","./conf", "conf"};

        String EXTENSION = ".properties";
        List<File> fileNameList = new ArrayList<File>(propDirs.length);

        // Now search through directory set
        for (final String dir : propDirs) {

            final File f = new File(dir, shortFileName);

            if (f.exists()) {
                fileNameList.add(f);
            }
        }

        if (filename.trim().endsWith(EXTENSION) && fileNameList.size() > 1) {
            throw new Exception("Property file: " + shortFileName + " found in " + fileNameList.size() + " locations.");
        }
        
        if (!fileNameList.isEmpty()) {
            return fileNameList.get(0).getAbsolutePath();
        }

        return null;
    }


}
