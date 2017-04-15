package com.timic.skywardgame.accounts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	
	public enum Status {
		SUCCESS,
		USER_NOT_FOUND,
		PASSWORD_INCORRECT,
		USERNAME_EXISTS,
		RUNTIME_ERROR,
		IOEXCEPTION_ERROR,
		UNKNOWN_ERROR
	}
	/*public final static int SUCCESS = 0;
	public final static int RUNTIME_ERROR = 1;
	public final static int IOEXCEPTION_ERROR = 2;
	public final static int USER_NOT_FOUND = 3;
	public final static int PASSWORD_INCORRECT = 4;
	public final static int UNKNOWN_ERROR = 666;*/
	
	public static Status addAccount(String username, String password) {
		checkDatabaseFile();
		if(authenticate(username, "", true) != Status.SUCCESS) {
			try {
				PrintWriter pw = new PrintWriter(database.writer(true));
				pw.print(username);
				pw.print(SEPARATOR);
				pw.print(md5crypt(password));
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
	}
	
	public static Status authenticate(String username, String password) {
		return authenticate(username, password, false);
	}
	
	public static Status authenticate(String username, String password, boolean skipPasswordCheck) {
		checkDatabaseFile();
		try {
			BufferedReader br = new BufferedReader(database.reader());
			try {
				while(br.ready()) {
					String[] databaseRow = br.readLine().split(Character.toString(SEPARATOR));
					if(databaseRow[0].equals(username)) {
						if(skipPasswordCheck)
							return Status.SUCCESS;
						if(databaseRow[1].equals(md5crypt(password))) {
							br.close();
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
			}
		} catch(GdxRuntimeException e) {
			Gdx.app.error("Skyward", e.getMessage());
			return Status.RUNTIME_ERROR;
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
	public static String md5crypt(String message) {
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
