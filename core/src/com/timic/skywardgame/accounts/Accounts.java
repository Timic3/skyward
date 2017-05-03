package com.timic.skywardgame.accounts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.timic.skywardgame.SkywardGame;

public class Accounts {
	private static FileHandle database;
	private final static char SEPARATOR = ((char)007); // ASCII beep sound
	
	public static String loggedInUsername;
	public static int movement; // 0 = keys, 1 = mouse
	public static int score;
	
	public enum Status {
		SUCCESS,
		USER_NOT_FOUND,
		PASSWORD_INCORRECT,
		USERNAME_EXISTS,
		FIELDS_EMPTY,
		PASSWORDS_NOT_SAME,
		RUNTIME_ERROR,
		IOEXCEPTION_ERROR,
		UNKNOWN_ERROR
	}
	
	public static Status addAccount(String username, String password) {
		checkDatabaseFile();
		if(username.matches(".*\\w.*") && password.matches(".*\\w.*")) {
			if(authenticate(username, "", true) != Status.SUCCESS) {
				try {
					PrintWriter pw = new PrintWriter(database.writer(true));
					pw.print(username);
					pw.print(SEPARATOR);
					pw.print(md5crypt(password));
					pw.print(SEPARATOR);
					pw.print("0");
					pw.print(SEPARATOR);
					pw.print("0");
					pw.println();
					pw.flush();
					pw.close();
					return Status.SUCCESS;
				} catch(GdxRuntimeException e) {
					Gdx.app.error("Skyward", e.getMessage());
					return Status.RUNTIME_ERROR;
				}
			} else {
				return Status.USERNAME_EXISTS;
			}
		} else {
			return Status.FIELDS_EMPTY;
		}
	}
	
	public static Status authenticate(String username, String password) {
		return authenticate(username, password, false);
	}
	
	public static Status authenticate(String username, String password, boolean skipPasswordCheck) {
		checkDatabaseFile();
		try {
			BufferedReader br = new BufferedReader(database.reader());
			while(br.ready()) {
				String[] databaseRow = br.readLine().split(Character.toString(SEPARATOR));
				if(databaseRow[0].equals(username)) {
					if(skipPasswordCheck) {
						br.close();
						return Status.SUCCESS;
					}
					if(databaseRow[1].equals(md5crypt(password))) {
						br.close();
						loggedInUsername = databaseRow[0];
						movement = Integer.parseInt(databaseRow[2]);
						score = Integer.parseInt(databaseRow[3]);
						return Status.SUCCESS;
					} else {
						br.close();
						return Status.PASSWORD_INCORRECT;
					}
				}
			}
			br.close();
			return Status.USER_NOT_FOUND;
		} catch(IOException e) {
			Gdx.app.error("Skyward", e.getMessage());
			return Status.IOEXCEPTION_ERROR;
		} catch(GdxRuntimeException e) {
			Gdx.app.error("Skyward", e.getMessage());
			return Status.RUNTIME_ERROR;
		}
	}
	
	public static Status updateScore(int newScore) {
		return updateProfile("", "", movement, newScore);
	}
	
	public static Status updateProfile(String newUsername, String newPassword, int newMovement) {
		return updateProfile(newUsername, newPassword, newMovement, 0);
	}
	
	public static Status updateProfile(String newUsername, String newPassword, int newMovement, int newScore) {
		checkDatabaseFile();
		if(newUsername.matches(".*\\w.*") || newPassword.matches(".*\\w.*") || newMovement != movement || newScore > 0) {
			// Ordered temporary account list
			ArrayList<Account> tempAccounts = new ArrayList<Account>();
			try {
				BufferedReader br = new BufferedReader(database.reader());
				while(br.ready()) {
					String[] databaseRow = br.readLine().split(Character.toString(SEPARATOR));
					if(databaseRow[0] != loggedInUsername && newUsername.matches(".*\\w.*") && databaseRow[0].equals(newUsername)) {
						br.close();
						tempAccounts.clear();
						return Status.USERNAME_EXISTS;
					}
					tempAccounts.add(new Account(databaseRow[0], databaseRow[1], 0, Integer.parseInt(databaseRow[3])));
				}
				br.close();
				database.writeBytes(new byte[0], false); // Delete content
				PrintWriter pw = new PrintWriter(database.writer(true));
				for(Account account : tempAccounts) {
					String username = account.getUsername();
					String password = account.getPassword();
					int currentScore = account.getScore();
					if(username.equals(loggedInUsername)) {
						if(newUsername.matches(".*\\w.*")) {
							username = newUsername;
							loggedInUsername = newUsername;
						}
						if(newPassword.matches(".*\\w.*"))
							password = md5crypt(newPassword);
						if(newScore > currentScore) {
							currentScore = newScore;
							score = newScore;
						}
						pw.print(username);
						pw.print(SEPARATOR);
						pw.print(password);
						pw.print(SEPARATOR);
						pw.print(newMovement);
						pw.print(SEPARATOR);
						pw.print(score);
						movement = newMovement;
					} else {
						pw.print(username);
						pw.print(SEPARATOR);
						pw.print(password);
						pw.print(SEPARATOR);
						pw.print(movement);
						pw.print(SEPARATOR);
						pw.print(currentScore);
					}
					pw.println();
				}
				pw.flush();
				pw.close();
				tempAccounts.clear();
				return Status.SUCCESS;
			} catch(IOException e) {
				Gdx.app.error("Skyward", e.getMessage());
				return Status.IOEXCEPTION_ERROR;
			} catch(GdxRuntimeException e) {
				Gdx.app.error("Skyward", e.getMessage());
				return Status.RUNTIME_ERROR;
			}
		} else {
			return Status.FIELDS_EMPTY;
		}
	}
	
	public static LinkedHashMap<String, Integer> getLeaderboard() {
		checkDatabaseFile();
		try {
			LinkedHashMap<String, Integer> leaderboard = new LinkedHashMap<String, Integer>();
			BufferedReader br = new BufferedReader(database.reader());
			while(br.ready()) {
				String[] databaseRow = br.readLine().split(Character.toString(SEPARATOR));
				leaderboard.put(databaseRow[0], Integer.parseInt(databaseRow[3]));
			}
			br.close();
			return leaderboard;
		} catch(IOException e) {
			Gdx.app.error("Skyward", e.getMessage());
			return null;
		} catch(GdxRuntimeException e) {
			Gdx.app.error("Skyward", e.getMessage());
			return null;
		}
	}
	
	public static void friendlyError(Stage stage, Status status) {
		if(status != Status.SUCCESS) {
			String friendlyError = null;
			if(status == Status.USER_NOT_FOUND)
				friendlyError = "Username not found!";
			else if(status == Status.PASSWORD_INCORRECT)
				friendlyError = "Password is incorrect!";
			else if(status == Status.USERNAME_EXISTS)
				friendlyError = "User with that name already exists!";
			else if(status == Status.FIELDS_EMPTY)
				friendlyError = "Required fields are empty!";
			else if(status == Status.PASSWORDS_NOT_SAME)
				friendlyError = "Password confirmation failed!";
			else
				friendlyError = "An unknown error has occured";
			new Dialog("Error!", SkywardGame.SKIN_VISION, "dialog") {
				protected void result(Object object) {
					Gdx.app.debug("Skyward", "Chosen: "+object);
				}
			}.text(friendlyError).button("Okay", true).key(Keys.ENTER, true).show(stage);
		}
	}
	
	private static void checkDatabaseFile() {
		FileHandle checkFile = Gdx.files.local("data/accounts.txt");
		if(checkFile.exists()) {
			Gdx.app.debug("Skyward", "Accounts database exists");
			Gdx.app.debug("Skyward", "Loaded: "+Gdx.files.getLocalStoragePath());
		} else {
			Gdx.app.debug("Skyward", "Accounts database does not exist. Creating a new one...");
			checkFile.writeString("", false);
		}
		if(checkFile instanceof FileHandle)
			database = checkFile;
	}
	
	// Safest way to get MD5 \o/
	// String is not secure
	private static String md5crypt(String message) {
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));
			
			// Convert byte array to hexadecimal string
			StringBuilder sb = new StringBuilder(2*hash.length);
			for(byte b : hash)
				sb.append(String.format("%02x", b&0xff));
			digest = sb.toString();
		} catch(UnsupportedEncodingException e) {
			Gdx.app.error("Skyward", e.getMessage());
		} catch(NoSuchAlgorithmException e) {
			Gdx.app.error("Skyward", e.getMessage());
		}
		return digest;
	}
	
}
