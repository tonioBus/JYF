package com.justyour.food.server;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import org.clapper.util.classutil.AbstractClassFilter;
import org.clapper.util.classutil.AndClassFilter;
import org.clapper.util.classutil.ClassFilter;
import org.clapper.util.classutil.ClassFinder;
import org.clapper.util.classutil.ClassInfo;
import org.clapper.util.classutil.ClassLoaderBuilder;
import org.clapper.util.classutil.InterfaceOnlyClassFilter;
import org.clapper.util.classutil.NotClassFilter;
import org.clapper.util.classutil.SubclassClassFilter;

import com.justyour.food.server.Parameters.DumperDeclaration;
import com.justyour.food.server.crawl.Dumper;

public class CrawlerClassLoader {
	static Logger logger = Logger.getLogger(CrawlerClassLoader.class.getName());

	HashMap<String, String> dumpers = new HashMap<>();

	class LogClassFilter implements ClassFilter {

		@Override
		public boolean accept(ClassInfo classInfo, ClassFinder arg1) {
			logger.info("accepting class: " + classInfo.toString());
			return true;
		}

	};

	public void loadDumpers(Parameters param) {
		if (Parameters.getDetectDumper() == true) {
			ClassFinder finder = new ClassFinder();
			String args[] = param.getJYFClassPath();
			ClassLoaderBuilder clb = new ClassLoaderBuilder();

			finder.addClassPath();
			clb.addClassPath();
			if (args != null) {
				for (String arg : args) {
					finder.add(new File(arg));
					clb.add(new File(arg));
				}
			}
			ClassLoader cl = clb.createClassLoader();
			ClassFilter filter = new AndClassFilter(

			// // dump the current visited class
			// new LogClassFilter(),

					// Must not be an interface
					new NotClassFilter(new InterfaceOnlyClassFilter()),

					// Must implement the ClassFilter interface
					new SubclassClassFilter(Dumper.class),

					// Must not be abstract
					new NotClassFilter(new AbstractClassFilter()));

			Collection<ClassInfo> foundClasses = new ArrayList<ClassInfo>();
			int ret = finder.findClasses(foundClasses, filter);
			logger.info("Result from finding Dumper class: " + ret);

			for (ClassInfo classInfo : foundClasses) {
				String clazz = classInfo.getClassName();
				try {
					@SuppressWarnings("unchecked")
					Class<Dumper> dumperClass = (Class<Dumper>) cl
							.loadClass(clazz);
					Method method = dumperClass
							.getDeclaredMethod("getDefaultDomain");
					try {
						String domain = (method.invoke(null)).toString();
						String previousUrl = dumpers.get(dumperClass.getName());
						if (previousUrl != null)
							dumpers.put(dumperClass.getName(), previousUrl
									+ ":" + domain);
						else
							dumpers.put(dumperClass.getName(), domain);
						logger.info("Found On Classpath [" + domain + "] -> ["
								+ clazz + "]");
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		loadParamDumpers(param);
	}

	protected void loadParamDumpers(Parameters param) {
		DumperDeclaration[] dumperDeclarations = param.getDumperDeclarations();
		for (DumperDeclaration dumperDeclaration : dumperDeclarations) {
			String domain = dumperDeclaration.urls.trim();
			String clazz = dumperDeclaration.className.trim();
			logger.info("Found On Parameters [" + domain + "] -> [" + clazz
					+ "]");
			if (dumpers.containsKey(clazz)) {
				logger.info("Replacing Dumper class [" + clazz + "]");
				dumpers.remove(clazz);
			}
			dumpers.put(clazz, domain);
		}
	}

	public HashMap<String, String> getDumpers() {
		return this.dumpers;
	}

}
