package ro.calin.vr;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;

public class Utils {
	private Utils() {
	}
	
	public static URL[] getResInPackage(String pckgname, final String pattern) throws IOException {
	 	ClassLoader cld = Thread.currentThread().getContextClassLoader();
	 	if(cld == null)
	 		throw new IOException("Classloader error.");
	 	
	 	pckgname = pckgname.replace('.', '/');
	 	
	 	Enumeration<URL> resources = cld.getResources(pckgname);
	 	if(!resources.hasMoreElements())
	 		throw new IOException("No such package.");
	 	
	 	File location = new File(URLDecoder.decode(resources.nextElement().getPath(), "UTF-8"));
	 	
	 	if(location == null)
	 		throw new IOException("Error finding package.");;
	 		
	 	File[] files = location.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return pattern == null? true : name.matches(pattern);
			}
		});
	 	
	 	URL[] urls = new URL[files.length];
	 	
        for (int i = 0; i < files.length; i++) {
        	urls[i] = files[i].toURI().toURL();
        }
 
        return urls;
    }
}
