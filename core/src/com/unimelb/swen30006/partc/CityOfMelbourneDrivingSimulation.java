package com.unimelb.swen30006.partc;

import java.awt.geom.Point2D;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.unimelb.swen30006.partc.core.World;

/**
 * The primary simulation driver for the Part C project for SWEN30006 Sem 1 2016
 * @author Mathew Blair
 * mathew.blair@unimelb.edu.au
 */
public class CityOfMelbourneDrivingSimulation extends ApplicationAdapter {
	
	// The width of the world in unitless dimensions
    static final int WORLD_WIDTH = 800;
    static final int WORLD_HEIGHT = 800;

    // Viewport state
    int VIEWPORT_WIDTH=200;
	float viewport_width;

	// Data for simluation, rendering and camera.
	private World world;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private boolean followCar = false;
	
	@Override
	public void resize(int width, int height) {
        camera.viewportWidth = viewport_width;
        camera.viewportHeight = viewport_width * (float)height/width;
        camera.update();
	}
	@Override
	public void create () {
		this.world = new World();
		
		// Setup our 2D Camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        viewport_width = VIEWPORT_WIDTH;
		camera = new OrthographicCamera(viewport_width, viewport_width * (h / w));
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		
		// Create our shape renderer
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {
		// Clear the graphics to white
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		// Update the simulation and camera
		handleInput();
		camera.update();
		world.update(Gdx.graphics.getDeltaTime());

		// Render the simulation
		shapeRenderer.setProjectionMatrix(camera.combined);

		// Render all filled shapes.
		shapeRenderer.begin(ShapeType.Filled);
		world.render(shapeRenderer);
		shapeRenderer.end();

		// Begin preparations to render text
		SpriteBatch batch = new SpriteBatch();
		batch.begin();

		// Render Header
		batch.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

	}
	
    private void handleInput() {
    	
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom += 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
        	camera.zoom -= 0.1;
        }

    	
    	if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
    		this.followCar = !this.followCar;
    	}
    	
    	if(!this.followCar){
            if (Gdx.input.isKeyPressed(Input.Keys.A)){
            	camera.translate(-3f, 0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            	camera.translate(3f, 0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            	camera.translate(0, -3f, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            	camera.translate(0, 3f, 0);
            }
    	} else {
    		Point2D.Double poi = this.world.getPointOfInterest();
    		camera.position.set((float)poi.x, (float)poi.y, 0f);
    	}
        
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, WORLD_WIDTH/camera.viewportWidth);
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, WORLD_WIDTH - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, WORLD_HEIGHT - effectiveViewportHeight / 2f);
    }

}
