
package com.taobao.monitor.stable;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author xiaodu
 * @version 2011-6-17 ÉÏÎç10:12:56
 */
public class PackageClassLoader extends ClassLoader{
	
	private String packagePath;
	
	public PackageClassLoader(){
		
	}
	public PackageClassLoader(ClassLoader parent){
		super(parent);
	}
	
	
	public PackageClassLoader(String packagePath){
		this.packagePath = packagePath;
	}
	
	
	
	public List<Class<?>> findPackageClass(){
		
		List<Class<?>> listClasses = new ArrayList<Class<?>>();
		
		
		
		if(this.packagePath !=null){
			Set<String> classes = getPackageDirClasses(this.packagePath);
			for(String className:classes){
				try {
					listClasses.add(Thread.currentThread().getContextClassLoader().loadClass(className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return listClasses;
	}
	
	
	
	private Set<String> getPackageDirClasses(String packagePath){
		
		Set<String> set = new HashSet<String>();
		if(packagePath!=null){
			String packageDir = packagePath.replace('.', '/');
			try {
				 Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDir);
				 while(dirs.hasMoreElements()){
					 URL url = dirs.nextElement();
					 if("file".equals(url.getProtocol())){
						 String filePath = URLDecoder.decode(url.getFile(), "UTF-8");  
						 findAllClassesInPackageByFile(packagePath,filePath,set);
					 }
				 }
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return set;
	}
	

   
	private void findAllClassesInPackageByFile(String packageName,  
            String packagePath, Set<String> classes) {  
        File dir = new File(packagePath);  
        if (!dir.exists() || !dir.isDirectory()) {  
            return;  
        }  
        File[] dirfiles = dir.listFiles(new FileFilter() {  
            public boolean accept(File file) {  
                return (file.isDirectory())  
                        || (file.getName().endsWith(".class"));  
            }  
        });  
        for (File file : dirfiles) {  
            if (file.isDirectory()) {  
            	findAllClassesInPackageByFile(packageName + "."  
                        + file.getName(), file.getAbsolutePath(),   
                        classes);  
            } else {  
                String className = file.getName().substring(0,  
                        file.getName().length() - 6);  
                classes.add(packageName+"."+className);
            }  
        }  
    }  

}
