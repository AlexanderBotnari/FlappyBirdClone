package com.alexander.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
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
	int spaceBetween = 700;
	Random random;
	int tubeSpeed = 5;
	int tubesNumber = 5;
	float[] tubeX = new float[tubesNumber];
	float[] tubeShift = new float[tubesNumber];
	float distanceBetweenTubes;

	Circle birdCircle;
	Rectangle[] topTubesRectangles;
	Rectangle[] bottomTubesRectangles;
//	ShapeRenderer shapeRenderer;

	int gameScore = 0;
	int passedTubeIndex = 0;
	BitmapFont scoreFont;

	Texture gameOver;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		birdCircle = new Circle();
		topTubesRectangles = new Rectangle[tubesNumber];
		bottomTubesRectangles = new Rectangle[tubesNumber];
		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.CYAN);
		scoreFont.getData().scale(10);
//		shapeRenderer = new ShapeRenderer();
		gameOver = new Texture("game_over.png");

		bird = new Texture[2];
		bird[0] = new Texture("bird_wings_up.png");
		bird[1] = new Texture("bird_wings_down.png");
		bottomTube = new Texture("bottom_tube.png");
		topTube = new Texture("top_tube.png");
		random = new Random();

		distanceBetweenTubes = Gdx.graphics.getWidth() / 2f;
		initGame();
	}

	private void initGame(){
		flightHeight = Gdx.graphics.getHeight()/2f - bird[0].getHeight()/2f;
		for(int i=0; i<tubesNumber; i++){
			tubeX[i] = Gdx.graphics.getWidth()/2f - topTube.getWidth()/2f + Gdx.graphics.getWidth() + i*distanceBetweenTubes;
			tubeShift[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - spaceBetween - 1200);
			topTubesRectangles[i] = new Rectangle();
			bottomTubesRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameStateFlag == 1){
			if (tubeX[passedTubeIndex] < Gdx.graphics.getWidth() / 2f){
				gameScore++;
				if (passedTubeIndex < tubesNumber - 1){
					passedTubeIndex++;
				}else {
					passedTubeIndex = 0;
				}
			}

			if(Gdx.input.isTouched()){
				fallingSpeed = -15;
			}

			for (int i=0; i<tubesNumber; i++){

				if (tubeX[i] < -topTube.getWidth()){
					tubeX[i] = tubesNumber * distanceBetweenTubes;
				}else{
					tubeX[i] -= tubeSpeed;
				}
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight()/2f + spaceBetween / 2f + tubeShift[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight()/2f - spaceBetween / 2f - bottomTube.getHeight() + tubeShift[i]);

				topTubesRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2f + spaceBetween / 2f + tubeShift[i], topTube.getWidth(), topTube.getHeight());
				bottomTubesRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2f - spaceBetween / 2f - bottomTube.getHeight() + tubeShift[i], bottomTube.getWidth(), bottomTube.getHeight());

			}

			if (flightHeight > 0){
				fallingSpeed++;
				flightHeight -= fallingSpeed;
			}else {
				gameStateFlag = 2;
			}
		}else if(gameStateFlag == 0){
			if(Gdx.input.isTouched()){
				gameStateFlag = 1;
			}
		}else  if (gameStateFlag == 2){
			batch.draw(gameOver, Gdx.graphics.getWidth()/2f - gameOver.getWidth()/2f, Gdx.graphics.getHeight()/2f - gameOver.getHeight()/2f);
			if(Gdx.input.isTouched()){
				gameStateFlag = 1;
				initGame();
				gameScore = 0;
				passedTubeIndex = 0;
				fallingSpeed = 0;
			}
		}

		if(birdWingsFlag == 0){
			birdWingsFlag = 1;
		}else {
			birdWingsFlag = 0;
		}

		batch.draw(bird[birdWingsFlag], Gdx.graphics.getWidth()/2f - bird[birdWingsFlag].getWidth()/2f, flightHeight);
		scoreFont.draw(batch, String.valueOf(gameScore), 100, 200);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2f, flightHeight + bird[birdWingsFlag].getHeight()/2f, bird[birdWingsFlag].getWidth()/2f);
//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.CYAN);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
//		shapeRenderer.end();
		for (int i=0; i<tubesNumber; i++){
			if(Intersector.overlaps(birdCircle, topTubesRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubesRectangles[i])){
				gameStateFlag = 2;
			}
		}
	}

}
