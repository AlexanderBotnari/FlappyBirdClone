package com.alexander.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;

	Texture [] bird;
	int birdWingsFlag = 0;
	float flightHeight = 0;
	int fallingSpeed = 0;
	int gameStateFlag = 0;

	Texture bottomTube;
	Texture topTube;
	int spaceBetween = 500;
	Random random;
	int tubeSpeed = 5;
	int tubesNumber = 5;
	float[] tubeX = new float[tubesNumber];
	float[] tubeShift = new float[tubesNumber];
	float distanceBetweenTubes;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		bird = new Texture[2];
		bird[0] = new Texture("bird_wings_up.png");
		bird[1] = new Texture("bird_wings_down.png");
		flightHeight = Gdx.graphics.getHeight()/2f - bird[0].getHeight()/2f;
		bottomTube = new Texture("bottom_tube.png");
		topTube = new Texture("top_tube.png");
		random = new Random();

		distanceBetweenTubes = Gdx.graphics.getWidth() / 2f;
		for(int i=0; i<tubesNumber; i++){
			tubeX[i] = Gdx.graphics.getWidth()/2f - topTube.getWidth()/2f + i*distanceBetweenTubes;
			tubeShift[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - spaceBetween - 800);
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(Gdx.input.isTouched()){
			gameStateFlag = 1;
		}

		if (gameStateFlag == 1){
			if(Gdx.input.isTouched()){
				fallingSpeed = -20;
			}
			if (flightHeight > 0 || fallingSpeed < 0){
				fallingSpeed++;
				flightHeight -= fallingSpeed;
			}
		}else {
			if(Gdx.input.isTouched()){
				gameStateFlag = 1;
			}
		}

		for (int i=0; i<tubesNumber; i++){

			if (tubeX[i] < -topTube.getWidth()){
				tubeX[i] = tubesNumber * distanceBetweenTubes;
			}else{
				tubeX[i] -= tubeSpeed;
			}
			batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight()/2f + spaceBetween / 2f + tubeShift[i]);
			batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight()/2f - spaceBetween / 2f - bottomTube.getHeight() + tubeShift[i]);
		}

		if(birdWingsFlag == 0){
			birdWingsFlag = 1;
		}else {
			birdWingsFlag = 0;
		}

		batch.draw(bird[birdWingsFlag], Gdx.graphics.getWidth()/2f - bird[birdWingsFlag].getWidth()/2f, flightHeight);
		batch.end();
	}

}
