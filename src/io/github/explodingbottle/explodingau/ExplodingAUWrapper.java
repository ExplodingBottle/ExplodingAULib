package io.github.explodingbottle.explodingau;

import java.io.File;

/**
 * This class wraps the library for launch by non-Java programs.
 * 
 * @author ExplodingBottle
 *
 */
public class ExplodingAUWrapper {

	public static void main(String[] args) {
		if (args.length == 2) {
			System.out.println("Two arguments: OK");
			File override = new File(args[0]);
			if (override.exists() && override.isFile()) {
				System.out.println("File exists: OK");
				ExplodingAULib.standardProgramRoutine(args[1], override);
			} else {
				System.out.println("File exists: FAIL");
			}
		} else {
			System.out.println("Two arguments: FAIL");
		}
	}

}
