package com.dantasmr.testerunner.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.dantasmr.testerunner.dto.CommandResult;

public class CommandUtil {

	public static CommandResult runCommand(String command) throws InterruptedException {

		CommandResult commandResult = new CommandResult();

		try {

			ProcessBuilder pb = new ProcessBuilder();
			if (isWindows()) {
				pb.command("cmd.exe", command);
			} else {
				pb.command("sh", command);
			}

			Process process = pb.start();
			int exitValue = process.waitFor();

			StringBuilder strbuilder = new StringBuilder();
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			if (exitValue != 0) {
				while (errReader.ready()) {
					strbuilder.append(errReader.readLine());
				}
			} else {
				while (inputReader.ready()) {
					strbuilder.append(inputReader.readLine());
				}
			}

			commandResult.setStatuscodigo(exitValue);
			commandResult.setMessage(strbuilder.toString());

		} catch (Exception error) {
			error.printStackTrace();
			commandResult.setStatuscodigo(9);
			commandResult.setMessage(error.getMessage());
		}
		
		return commandResult;
	}

	public static boolean isLinux() {
		String os = System.getProperty("os.name");
		return os.toLowerCase().indexOf("linux") >= 0;
	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name");
		return os.toLowerCase().indexOf("windows") >= 0;
	}

}
