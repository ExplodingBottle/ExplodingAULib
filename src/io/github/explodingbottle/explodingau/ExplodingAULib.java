/*
 *   ExplodingAU - The automatic update system for ExplodingBottle projects.
 *   Copyright (C) 2023  ExplodingBottle
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.explodingbottle.explodingau;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class contains utilities to register your application in the update
 * candidates list.
 * 
 * @author ExplodingBottle
 *
 */
public class ExplodingAULib {

	/**
	 * Preventing to construct this class.
	 */
	private ExplodingAULib() {
		throw new IllegalAccessError("Cannot construct this class.");
	}

	private static final String EXPLODING_AU_VERSION = "1.0.0.0_OR";

	/**
	 * Computes the hash of a file.
	 * 
	 * @param toHash The file to compute the hash.
	 * @return The hash or {@code null} if it failed.
	 */
	public static String hashFile(File toHash) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			FileInputStream toRead = new FileInputStream(toHash);
			byte[] buffer = new byte[4096];
			int read = toRead.read(buffer, 0, buffer.length);
			while (read != -1) {
				md.update(buffer, 0, read);
				read = toRead.read(buffer, 0, buffer.length);
			}
			toRead.close();
			return new BigInteger(md.digest()).toString(32);

		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * Returns the AU folder root.
	 * 
	 * @return The folder or null if error.
	 */
	public static File seekAUFolder() {
		File fold = new File(System.getenv("APPDATA"), "OverRender OverSuite");
		if (!fold.exists()) {
			if (fold.mkdir()) {
				return fold;
			}
		} else {
			return fold;
		}
		return null;
	}

	/**
	 * Returns the loaded properties stored in the AU folder.
	 * 
	 * @param auFolder A reference to the AU folder.
	 * @return Returns the properties or null if failed.
	 */
	public static Properties loadPropsFromAUFolder(File auFolder) {
		Properties toRet = null;
		try {
			File auprops = new File(auFolder, "reglist.txt");
			if (auprops.exists()) {
				Properties okProps = new Properties();
				FileInputStream fis = new FileInputStream(auprops);
				okProps.loadFromXML(fis);
				fis.close();
				toRet = okProps;
			}
		} catch (Exception e) {

		}
		return toRet;
	}

	/**
	 * Saves the properties.
	 * 
	 * @param auFolder Where is the AU folder.
	 * @param props    What are the properties to save.
	 * @return If we managed to save or not.
	 */
	public static boolean storePropsToAUFolder(File auFolder, Properties props) {
		try {
			File auprops = new File(auFolder, "reglist.txt");
			FileOutputStream fos = new FileOutputStream(auprops);
			props.storeToXML(fos, "ExplodingAU Version " + EXPLODING_AU_VERSION + " - Managed file");
			fos.close();
			return true;
		} catch (Exception e) {

		}
		return false;
	}

	public static void standardProgramRoutine(String programName) {
		File auFolder = seekAUFolder();
		if (auFolder != null) {
			Properties lk = loadPropsFromAUFolder(auFolder);
			if (lk == null) {
				lk = new Properties();
			}
			fileCfgRoutine(programName, lk);
			storePropsToAUFolder(auFolder, lk);
		}
	}

	public static void standardProgramRoutine(String programName, File overrideFile) {
		File auFolder = seekAUFolder();
		if (auFolder != null) {
			Properties lk = loadPropsFromAUFolder(auFolder);
			if (lk == null) {
				lk = new Properties();
			}
			fileCfgRoutine(programName, lk, overrideFile);
			storePropsToAUFolder(auFolder, lk);
		}
	}

	/**
	 * Starts the routine to update the list.
	 * 
	 * @param programName The program identifier.
	 * @param props       The loaded properties
	 * @return If we managed to do the update.
	 */
	public static void fileCfgRoutine(String programName, Properties props) {
		fileCfgRoutine(programName, props, null);
	}

	/**
	 * Starts the routine to update the list.
	 * 
	 * @param programName  The program identifier.
	 * @param props        The loaded properties
	 * @param overridePath Where to find the file to hash.
	 * @return If we managed to do the update.
	 */
	public static void fileCfgRoutine(String programName, Properties props, File overridePath) {

		final Properties loaded = props;
		ArrayList<String> toRemove = new ArrayList<String>();
		loaded.forEach((k, v) -> {
			String key = (String) k;
			String val = (String) v;
			File loc = new File(key);
			if (!loc.exists() || loc.isDirectory()) {
				toRemove.add(key);
			} else {
				String[] split = val.split(";");
				if (split.length != 2) {
					toRemove.add(key);
				} else {
					String hash = split[1];
					String hashToCompare = hashFile(loc);
					if (hashToCompare != null) {
						if (!hash.equalsIgnoreCase(hashToCompare)) {
							toRemove.add(key);
						}
					}
				}
			}
		});
		if (programName != null) {
			try {
				File location = new File(
						ExplodingAULib.class.getProtectionDomain().getCodeSource().getLocation().toURI());
				if (location != null && !location.isDirectory()) {
					String fHash = hashFile(location);
					loaded.put(location.getAbsolutePath(), programName + ";" + fHash);
				}
			} catch (Exception e) {
			}
		}
		toRemove.forEach(trm -> {
			loaded.remove(trm);
		});

	}

}
