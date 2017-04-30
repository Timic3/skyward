package com.timic.skywardgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.timic.skywardgame.accounts.Accounts;
import com.timic.skywardgame.accounts.Accounts.Status;

public class OptionsScreen extends ScreenBase {
	private Stage stage;

	public OptionsScreen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
		Table table = new Table();
		table.setFillParent(true);
		table.align(Align.top);
		//table.setDebug(true);
		table.setRound(false);
		stage.addActor(table);
		
		final Label lNewPassword = new Label("New Password", this.skin);
		final Label lConfirmNewPassword = new Label("Confirm New Password", this.skin);
		final Label lNewUsername = new Label("New Username", this.skin);
		//final Label lNote = new Label("Leave blank if you don't want to change it", this.skin);
		final Label lMovement = new Label("Movement", this.skin);
		final TextField tfNewPassword = new TextField("", this.skin);
		final TextField tfConfirmNewPassword = new TextField("", this.skin);
		final TextField tfNewUsername = new TextField("", this.skin);
		final CheckBox cbMouse = new CheckBox("Keys", this.skin);
		final CheckBox cbKeys = new CheckBox("Mouse", this.skin);
		final TextButton tbBack = new TextButton("Back", this.skin);
		final TextButton tbSave = new TextButton("Save", this.skin);
		final ButtonGroup<CheckBox> bgMovement = new ButtonGroup<CheckBox>(cbMouse, cbKeys);
		tfNewPassword.setAlignment(Align.center);
		tfNewPassword.setPasswordCharacter('\u2022');
		tfNewPassword.setPasswordMode(true);
		tfConfirmNewPassword.setAlignment(Align.center);
		tfConfirmNewPassword.setPasswordCharacter('\u2022');
		tfConfirmNewPassword.setPasswordMode(true);
		tfNewUsername.setAlignment(Align.center);
		bgMovement.setMaxCheckCount(1);
		bgMovement.setMinCheckCount(1);
		if(Accounts.movement == 0)
			bgMovement.setChecked("Keys");
		else
			bgMovement.setChecked("Mouse");
		
		table.add(lNewPassword).colspan(2).padTop(60);
		table.row();
		table.add(tfNewPassword).colspan(2).width(300);
		table.row();
		table.add(lConfirmNewPassword).colspan(2).padTop(10);
		table.row();
		table.add(tfConfirmNewPassword).colspan(2).width(300);
		table.row();
		table.add(lNewUsername).colspan(2).padTop(10);
		table.row();
		table.add(tfNewUsername).colspan(2).width(300);
		table.row();
		table.add(lMovement).colspan(2).padTop(10);
		table.row().padTop(10);
		table.add(cbMouse);
		table.add(cbKeys);
		table.row().padTop(20);
		table.add(tbBack).align(Align.right).padRight(10);
		table.add(tbSave).align(Align.left).padLeft(10);
		//table.row().padTop(20);
		//table.add(lNote).colspan(2);
		
		tbBack.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});
		
		tbSave.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if(tfNewPassword.getText().equals(tfConfirmNewPassword.getText())) {
					Status status = Accounts.updateProfile(tfNewUsername.getText(), tfNewPassword.getText(), bgMovement.getCheckedIndex());
					if(status == Accounts.Status.SUCCESS)
						game.setScreen(new MenuScreen(game));
					else
						Accounts.friendlyError(stage, status);
				} else {
					Accounts.friendlyError(stage, Accounts.Status.PASSWORDS_NOT_SAME);
				}
			}
		});
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1.0f, 0.33f, 0.33f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}
	
	@Override
	public void hide() {
		Gdx.app.debug("Skyward", "Disposing of options screen");
		stage.dispose();
	}
	
}
