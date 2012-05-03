package com.googlecode.cmakemavenproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * Mojo helper functions.
 *
 * @author Gili Tzabari
 */
public class Mojos
{
	/**
	 * Looks up a plugin configuration by its name.
	 *
	 * @param project the maven project
	 * @param key the plugin key (groupId:artifactId)
	 * @return null if the plugin was not found
	 */
	public static Xpp3Dom getPluginConfiguration(MavenProject project, String key)
	{
		@java.lang.SuppressWarnings("unchecked")
		List<Plugin> plugins = project.getBuildPlugins();

		for (Iterator<Plugin> i = plugins.iterator(); i.hasNext();)
		{
			Plugin plugin = i.next();
			if (key.equalsIgnoreCase(plugin.getKey()))
				return (Xpp3Dom) plugin.getConfiguration();
		}
		return null;
	}

	/**
	 * Launches and waits for a process to complete.
	 *
	 * @param processBuilder the process builder
	 * @return the process exit code
	 * @throws IOException if an I/O error occurs while running the process
	 * @throws InterruptedException if the thread was interrupted
	 */
	public static int waitFor(ProcessBuilder processBuilder)
		throws IOException, InterruptedException
	{
		Process process = processBuilder.redirectErrorStream(true).start();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream())))
		{
			while (true)
			{
				String line = in.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}
		}
		return process.waitFor();
	}
}
