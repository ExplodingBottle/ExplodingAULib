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
