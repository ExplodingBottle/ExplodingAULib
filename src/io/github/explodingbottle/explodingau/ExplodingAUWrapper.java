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
			File override = new File(args[0]);
			if (override.exists() && override.isFile()) {
				ExplodingAULib.standardProgramRoutine(args[1], override);
			}
		}
	}

}
