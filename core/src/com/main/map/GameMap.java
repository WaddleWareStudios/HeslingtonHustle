package com.main.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Represents the game map, handling rendering and toggling layer visibility.
 * It extends TiledMap to use the functionalities provided by libGDX for tile maps.
 */
public class GameMap extends TiledMap {
    private final int width, height;
    private final TiledMap gameMap;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;
    private final OrthographicCamera camera;
    final int tileSize = 16; // Added code
    float layerToggleTime;

    /**
     * Constructs a GameMap with an orthographic camera.
     *
     * @param camera The camera used to view the map.
     */
    public GameMap(OrthographicCamera camera) {
        // Load the .tmx with the MainMap for game
        gameMap = new TmxMapLoader().load("map/MainMap.tmx");
        MapProperties properties = gameMap.getProperties();
        height = properties.get("tileheight", Integer.class) * properties.get("height", Integer.class);
        width = properties.get("tilewidth", Integer.class) * properties.get("width", Integer.class);

        // Render the MainMap
        tiledMapRenderer = new OrthogonalTiledMapRenderer(gameMap);

        this.camera = camera;
    }

    /**
     * Renders the map by updating the camera and setting the renderer's view accordingly.
     */
    public void render() {
        // Update the camera and set the tiledMapRenderer's view based on that camera
        camera.update();
        tiledMapRenderer.setView(camera);

        // Render the map
        tiledMapRenderer.render();
    }

    /**
     * Updates the map, including toggling layer visibility based on a timer.
     *
     * @param delta The time in seconds since the last update.
     */
    public void update(float delta) {
        layerToggleTime += delta;
        if (layerToggleTime >= 0.75f) {
            toggleLayerVisibility("Water_2");
            //toggleLayerVisibility("Trees");
            layerToggleTime = 0;
        }
    }

    /**
     * Toggles the visibility of a specific layer within the map.
     *
     * @param layerName The name of the layer to toggle.
     */
    public void toggleLayerVisibility(String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer)gameMap.getLayers().get(layerName);
        if (layer != null) {
            layer.setVisible(!layer.isVisible());
        }
    }

    /**
     * Gets the tile size of the map.
     *
     * @return The size of the tiles in the map.
     */
    public int getTileSize(){
        return tileSize;
    }

    /**
     * Gets the width of the map in tiles.
     *
     * @return The width of the map.
     */
    public int getWidth(){
        return width;
    }

    /**
     * Gets the height of the map in tiles.
     *
     * @return The height of the map.
     */
    public int getHeight(){
        return height;
    }

    /**
     * Gets the TiledMap instance representing the game map.
     *
     * @return The TiledMap instance.
     */
    public TiledMap getMap(){
        return gameMap;
    }

    
    public void dispose() {
        gameMap.dispose();
        tiledMapRenderer.dispose();
    }
}
