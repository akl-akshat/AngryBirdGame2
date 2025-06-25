package angry_bird.gamescreens;

import angry_bird.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static angry_bird.utils.Constants.UI.skin.BUTTON_SKIN;

public class Level3Screen implements Screen {
    private static final Logger logger = new Logger(Level1Screen.class.getName(), Logger.DEBUG);
    private static final float PPM = 100; // Pixels per meter
    private Main main;
    int load;
    private Stage stage;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Slingshot slingshot;
    private ProjectilePathSimulator projectilePathSimulator;
    private ShapeRenderer shapeRenderer;
    private Queue<Bird> birdQueue;
    //private Music backgroundMusic;
    private Sound slingshotStretchSound;
    private Sound birdLaunchSound;
    private Sound pigHitSound;
    private Sound victorySound;
    private Sound gameOverSound;

    // Game assets
    private Texture backgroundTexture;
    private Bird bird1, bird2, bird3;

    // Pause menu and state
    private boolean isPaused = false;
    private Table pauseMenuTable;

    private Block block1, block2, block3, block4, block5, block6, block7, block8, block9, block10, block11, block12, block13, block14, block15, block16, block17, block18, block19, block20, block21, block22, block23, block24, block25,block26;
    private Pig pig1, pig2, pig3;
    private boolean victoryTriggered = false;
    private boolean gameOverTriggered = false;
    private float transitionTimer = 0;
    public Level3Screen(Main main, int load) {
        this.main = main;
        this.load = load;
        //backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/background.mp3"));
        slingshotStretchSound = Gdx.audio.newSound(Gdx.files.internal("audio/slingshot_stretch.mp3"));
        birdLaunchSound = Gdx.audio.newSound(Gdx.files.internal("audio/bird_launch.mp3"));
        pigHitSound = Gdx.audio.newSound(Gdx.files.internal("audio/pig_hit.mp3"));
        victorySound = Gdx.audio.newSound(Gdx.files.internal("audio/victory.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_over.mp3"));

        // Start background music
        //backgroundMusic.setLooping(true);
        //backgroundMusic.play();
        try {
            setupStage();
            setupWorld();
            setupTextures();
            setupSlingshot();
            setupBirds();
            setupBirdQueue();
            setupBlocks();
            setupGroundAndWalls();
            setupPigs();
            setupButtons();
            projectilePathSimulator = new ProjectilePathSimulator();
            shapeRenderer = new ShapeRenderer();

            stage.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("\nClicked at: (" + x + ", " + y + ")\n");
                }
            });

        } catch (Exception e) {
            logger.error("Error initializing Level1Screen", e);
            Gdx.app.exit(); // Exit the application if initialization fails
        }
    }

    private void setupStage() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private boolean isStretching() {
        return slingshot != null && slingshot.isStretching();
    }


    private void setupTextures() {
        backgroundTexture = new Texture("screens/level_1bg/level1.png");
    }

    private void setupSlingshot() {
        Texture slingshotTexture = new Texture("slingshot/slingshotabgnew.png");
        Vector2 anchorPoint = new Vector2(450, 300); // Example anchor point in pixels
        //change the y coordinate  of slingshot
        anchorPoint.y = anchorPoint.y + 52;
        slingshot = new Slingshot(world, slingshotTexture, anchorPoint);

        // Scale the texture (increase size by 1.5x for example)
        slingshot.setScale(1.5f, 1.5f);

        // Attach the first bird to the slingshot
        if (bird1 != null) {
            slingshot.attachBird(bird1.getBody());
        }
    }

    private void setupBirds() {
        // Initialize bird textures
        Texture birdTexture1 = new Texture("birds/red_bird.png");
        Texture birdTexture2 = new Texture("birds/bomb.png");
        Texture birdTexture3 = new Texture("birds/yellow_bird.png");

        // Create Bird objects
        bird1 = new Bird(world, birdTexture1, new Vector2(430, 308));
        bird2 = new Bird(world, birdTexture2, new Vector2(380, 300));
        bird3 = new Bird(world, birdTexture3, new Vector2(330, 328));

        // Initialize the bird queue
        birdQueue = new LinkedList<>();
        birdQueue.add(bird1);
        birdQueue.add(bird2);
        birdQueue.add(bird3);
    }

    private void setupBirdQueue() {
        if (!birdQueue.isEmpty()) {
            Bird currentBird = birdQueue.peek();
            slingshot.attachBird(currentBird.getBody());
        }
    }

    private void setupBlocks() {
        Texture woodblockTexture = new Texture("blocks/woodblock.jpg");
        Texture glassblockTexture = new Texture("blocks/glassblock.jpg");
        Texture stoneblockTexture = new Texture("blocks/stoneblock.jpg");
        block1 = new Block(world, woodblockTexture, new Vector2(1100, 300), 50, 50);
        block2 = new Block(world, woodblockTexture, new Vector2(1100, 320), 50, 50);
        block3 = new Block(world, stoneblockTexture, new Vector2(1100, 340), 50, 50);
        block4 = new Block(world, stoneblockTexture, new Vector2(1100, 350),50,50);
        block5 = new Block(world, glassblockTexture, new Vector2(1100, 360),50,50);
        //new
        block6 = new Block(world, woodblockTexture, new Vector2(1100, 370), 50, 50);
        block7 = new Block(world, woodblockTexture, new Vector2(1150, 300), 50, 50);
        block8 = new Block(world, stoneblockTexture, new Vector2(1150, 320), 50, 50);
        block9 = new Block(world, stoneblockTexture, new Vector2(1150, 340),50,50);
        block10 = new Block(world, glassblockTexture, new Vector2(1150, 350),50,50);
        block11 = new Block(world, woodblockTexture, new Vector2(1200, 300), 50, 50);
        block12 = new Block(world, stoneblockTexture, new Vector2(1200, 320), 50, 50);
        block13= new Block(world, stoneblockTexture, new Vector2(1200, 340),50,50);
        block14 = new Block(world, glassblockTexture, new Vector2(1200, 350),50,50);
        block15 = new Block(world, woodblockTexture, new Vector2(1260, 300), 50, 50);
        block16 = new Block(world, stoneblockTexture, new Vector2(1260, 320), 50, 50);
        block17= new Block(world, stoneblockTexture, new Vector2(1260, 340),50,50);
        block18 = new Block(world, glassblockTexture, new Vector2(1260, 350),50,50);
        block19 = new Block(world, woodblockTexture, new Vector2(1260, 300), 50, 50);
        block20 = new Block(world, stoneblockTexture, new Vector2(1260, 320), 50, 50);
        block21= new Block(world, stoneblockTexture, new Vector2(1260, 340),50,50);
        block22 = new Block(world, glassblockTexture, new Vector2(1260, 350),50,50);

        block23 = new Block(world, woodblockTexture, new Vector2(1360, 300), 50, 50);
        block24 = new Block(world, stoneblockTexture, new Vector2(1360, 320), 50, 50);
        block25= new Block(world, stoneblockTexture, new Vector2(1360, 340),50,50);
        block26 = new Block(world, glassblockTexture, new Vector2(1360, 350),50,50);
    }

    private void setupPigs() {
        Texture ForemanpigTexture = new Texture("pigs/Foreman_Pig.png");
        Texture mediumpigTexture = new Texture("pigs/Piggy_medium.png");
        Texture scaredpigTexture = new Texture("pigs/Pig_scared_2.png");
        Texture KPpigTexture = new Texture("pigs/KPSprite.png");
        pig1 = new Pig(world, ForemanpigTexture, new Vector2(1250, 370));
        pig2 = new Pig(world, scaredpigTexture, new Vector2(1300, 300));
        pig3 = new Pig(world, KPpigTexture, new Vector2(1000, 300));
    }

    private void setupButtons() {
        Skin skin = new Skin(Gdx.files.internal(BUTTON_SKIN));
        createBackButton(skin);
        createPauseButton(skin);
        setupPauseMenu(skin);
    }

    private void createBackButton(Skin skin) {
        TextButton backButton = new TextButton("<", skin);
        backButton.setSize(70, 70);
        backButton.setPosition(50, 950);
        stage.addActor(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                try {
                    main.setScreen(new Map_of_Levels(main, 0));
                } catch (Exception e) {
                    logger.error("Error transitioning to Map_of_Levels screen", e);
                }
            }
        });
    }

    private void createPauseButton(Skin skin) {
        TextButton pauseButton = new TextButton("II", skin);
        pauseButton.setSize(70, 70);
        pauseButton.setPosition(Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 80);
        stage.addActor(pauseButton);

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                togglePauseMenu();
            }
        });
    }

    private void togglePauseMenu() {
        isPaused = !isPaused;
        pauseMenuTable.setVisible(isPaused);
    }

    private void setupGroundAndWalls() {
        // Example implementation for setting up ground and walls
        // Create ground
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(0, 230 / PPM)); // Increased y-coordinate
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(Gdx.graphics.getWidth() / PPM, 10 / PPM);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();

        // Create walls (left and right)
        BodyDef wallBodyDef = new BodyDef();
        wallBodyDef.position.set(new Vector2(0, Gdx.graphics.getHeight() / 2 / PPM));
        Body leftWallBody = world.createBody(wallBodyDef);
        PolygonShape wallBox = new PolygonShape();
        wallBox.setAsBox(10 / PPM, Gdx.graphics.getHeight() / 2 / PPM);
        leftWallBody.createFixture(wallBox, 0.0f);
        wallBox.dispose();

        wallBodyDef.position.set(new Vector2(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / 2 / PPM));
        Body rightWallBody = world.createBody(wallBodyDef);
        wallBox = new PolygonShape();
        wallBox.setAsBox(10 / PPM, Gdx.graphics.getHeight() / 2 / PPM);
        rightWallBody.createFixture(wallBox, 0.0f);
        wallBox.dispose();
    }
    private void checkForTransitions(float delta) {
        boolean allPigsDestroyed = pig1.isMarkedForRemoval()&& pig2.isMarkedForRemoval;
        boolean allBirdsUsed = birdQueue.isEmpty();

        if (allPigsDestroyed && !victoryTriggered) {
            // Trigger victory
            victoryTriggered = true;
            transitionTimer = 0;
            victorySound.play();
        } else if (allBirdsUsed && !allPigsDestroyed && !gameOverTriggered) {
            // Trigger game over
            gameOverTriggered = true;
            transitionTimer = 0;
            gameOverSound.play();
        }

        // Handle transitions
        if (victoryTriggered || gameOverTriggered) {
            transitionTimer += delta;
            if (transitionTimer >= 2.0f) {
                if (victoryTriggered) {
                    main.setScreen(new VictoryScreen(main, this));
                } else if (gameOverTriggered) {
                    main.setScreen(new GameOverScreen(main, this));
                }
            }
        }
    }


    private void setupPauseMenu(Skin skin) {
        pauseMenuTable = new Table();
        pauseMenuTable.setFillParent(true);
        pauseMenuTable.center();

        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton restartButton = new TextButton("Restart", skin);
        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        pauseMenuTable.add(resumeButton).pad(10).row();
        pauseMenuTable.add(restartButton).pad(10).row();
        pauseMenuTable.add(mainMenuButton).pad(10).row();
        pauseMenuTable.add(exitButton).pad(10).row();

        stage.addActor(pauseMenuTable);
        pauseMenuTable.setVisible(false);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                togglePauseMenu();
            }
        });

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                try {
                    main.setScreen(new Level3Screen(main, 0));
                } catch (Exception e) {
                    logger.error("Error restarting Level3Screen", e);
                }
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                try {
                    main.setScreen(new MainMenuScreen(main));
                } catch (Exception e) {
                    logger.error("Error transitioning to MainMenuScreen", e);
                }
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    private void setupWorld() {
        world = new World(new Vector2(0, -9.8f), true); // Gravity of -9.8
        debugRenderer = new Box2DDebugRenderer();

        // Register a contact listener for collision handling
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object userDataA = contact.getFixtureA().getBody().getUserData();
                Object userDataB = contact.getFixtureB().getBody().getUserData();

                if (userDataA instanceof Bird && userDataB instanceof Pig) {
                    handleBirdHitsPig((Bird) userDataA, (Pig) userDataB);
                } else if (userDataA instanceof Pig && userDataB instanceof Bird) {
                    handleBirdHitsPig((Bird) userDataB, (Pig) userDataA);
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    //    private void handleBirdHitsPig(Bird bird, Pig pig) {
//        // Calculate damage based on bird's velocity and power
//        Vector2 velocity = bird.getBody().getLinearVelocity();
//        float birdSpeed = velocity.len();
//        float damage = birdSpeed * bird.getPower();
//
//        pig.applyDamage(damage); // Apply damage to the pig
//    }
    private void handleBirdHitsPig(Bird bird, Pig pig) {
        Vector2 velocity = bird.getBody().getLinearVelocity();
        float birdSpeed = velocity.len();
        float damage = birdSpeed * bird.getPower();

        pig.applyDamage(damage);

        if (pig.isMarkedForRemoval()) {
            pigsToRemove.add(pig); // Mark pig for removal
            pigHitSound.play();    // Play pig hit sound
        }
    }


    @Override
    public void show() {
    }

    // Add a field to track the bird's launch time
    private float birdLaunchTime = 0;
    private boolean isBirdLaunched = false;
    private List<Pig> pigsToRemove = new ArrayList<>();

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePauseMenu();
        }

        if (!isPaused) {
            // Draw background
            main.spriteBatch.begin();
            main.spriteBatch.draw(backgroundTexture, 0, 0);

            // Draw blocks, pigs, and the current bird
            renderObstacles();
            if (!birdQueue.isEmpty()) {
                Bird currentBird = birdQueue.peek();
                currentBird.render(main.spriteBatch);
            }
            pig1.render(main.spriteBatch);
            pig2.render(main.spriteBatch);
            pig3.render(main.spriteBatch);
            // Draw slingshot
            slingshot.render(main.spriteBatch);
            main.spriteBatch.end();

            // Handle slingshot stretching and bird launch
            if (Gdx.input.isTouched()) {
                Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

                if (Gdx.input.justTouched()) {
                    slingshot.startStretching(touchPos); // Start stretching
                    slingshotStretchSound.play();
                } else {
                    slingshot.updateStretching(touchPos); // Update bird position dynamically
                }
            } else if (slingshot.isStretching() && !isBirdLaunched) {
                slingshot.release(); // Release slingshot and calculate projectile motion
                birdLaunchSound.play();
                birdLaunchTime = 0; // Reset the launch timer
                isBirdLaunched = true;
            }



            // Update the physics world
            world.step(1 / 60f, 6, 2);
            checkForTransitions(delta);
            if (!birdQueue.isEmpty()) {
                birdQueue.peek().update(); // Update the current bird
            }
            pig1.update();
            pig2.update();
            pig3.update();
            // Safely remove pigs marked for removal
            processRemovals();


            // Update the current bird
            if (!birdQueue.isEmpty()) {
                Bird currentBird = birdQueue.peek();
                currentBird.update();

                // Track the time since launch
                if (isBirdLaunched) {
                    birdLaunchTime += delta;
                    Vector2 velocity = currentBird.getBody().getLinearVelocity();
                    boolean isSlow = velocity.len2() < 0.04; // Velocity threshold for "stopping"

                    // Remove bird if it's been on-screen for enough time and is nearly stopped
                    if (birdLaunchTime >= 5.0f || isSlow) {
                        currentBird.dispose(); // Clean up the current bird
                        birdQueue.poll(); // Remove it from the queue
                        isBirdLaunched = false; // Reset the launch flag

                        // Attach the next bird, if any
                        if (!birdQueue.isEmpty()) {
                            Bird nextBird = birdQueue.peek();
                            slingshot.attachBird(nextBird.getBody());
                        }
                    }
                }
            }

            // Update pigs
            pig1.update();
            pig2.update();
            pig3.update();

            // Calculate and render the projectile path (if stretching)
            if (isStretching()) {
                Vector2 launchVelocity = slingshot.calculateLaunchVelocity(10.0f);
                projectilePathSimulator.calculatePath(new Vector2(450, 300), launchVelocity, -9.8f, 0.1f);
                projectilePathSimulator.renderPath(shapeRenderer);
            }
        }

        // Render the stage and debug information
        stage.act();
        stage.draw();
        debugRenderer.render(world, stage.getCamera().combined.scl(100));
    }

    private void processRemovals() {
        for (Pig pig : pigsToRemove) {
            pig.destroyBody();
        }
        pigsToRemove.clear();
    }
    private void renderObstacles() {
        block1.render(main.spriteBatch);
        block2.render(main.spriteBatch);
        block3.render(main.spriteBatch);
        block4.render(main.spriteBatch);
        block5.render(main.spriteBatch);
        block6.render(main.spriteBatch);
        block7.render(main.spriteBatch);
        block8.render(main.spriteBatch);
        block9.render(main.spriteBatch);
        block10.render(main.spriteBatch);
        block11.render(main.spriteBatch);
        block12.render(main.spriteBatch);
        block13.render(main.spriteBatch);
        block14.render(main.spriteBatch);

        block15.render(main.spriteBatch);
        block16.render(main.spriteBatch);
        block17.render(main.spriteBatch);
        block18.render(main.spriteBatch);

        block19.render(main.spriteBatch);
        block20.render(main.spriteBatch);
        block21.render(main.spriteBatch);
        block22.render(main.spriteBatch);

        block23.render(main.spriteBatch);
        block24.render(main.spriteBatch);
        block25.render(main.spriteBatch);
        block26.render(main.spriteBatch);

        pig1.render(main.spriteBatch);
        pig2.render(main.spriteBatch);// Render the pig
        pig3.render(main.spriteBatch);// Render the pig
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        debugRenderer.dispose();
        backgroundTexture.dispose();
        //backgroundMusic.dispose();
        slingshotStretchSound.dispose();
        birdLaunchSound.dispose();
        pigHitSound.dispose();
        victorySound.dispose();
        gameOverSound.dispose();
        for (Bird bird : birdQueue) {
            bird.dispose();
        }
    }

    private class Bird extends angry_bird.gamescreens.Bird {
        public Bird(World world, Texture birdTexture, Vector2 position) {
            super(world, birdTexture, position);
        }

        public Body getBody() {
            return body;
        }
    }

    public class Block {
        private static final float PPM = 100; // Pixels per meter for Box2D scaling
        private World world;
        private Body body;
        private Sprite sprite;

        /**
         * Constructor for the Block class.
         *
         * @param world    The Box2D world where the block's physics body will be created.
         * @param texture  The texture used to visually represent the block.
         * @param position The initial position of the block in world coordinates.
         * @param width    The width of the block in pixels.
         * @param height   The height of the block in pixels.
         */
        public Block(World world, Texture texture, Vector2 position, float width, float height) {
            this.sprite = new Sprite(texture);

            // Create the block's physics body
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(position.x / PPM, position.y / PPM);
            body = world.createBody(bodyDef);

            // Define the block's shape
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width / 2f / PPM, height / 2f / PPM); // Use provided width and height
            body.createFixture(shape, 0.0f);
            shape.dispose();

            // Set the sprite's size to match the block's dimensions
            sprite.setSize(width, height);
            sprite.setOriginCenter();
            sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        }

        /**
         * Renders the block's sprite using the provided SpriteBatch.
         *
         * @param spriteBatch The SpriteBatch used for rendering.
         */
        public void render(SpriteBatch spriteBatch) {
            // Update sprite position and draw
            sprite.setPosition(body.getPosition().x * PPM - sprite.getWidth() / 2,
                body.getPosition().y * PPM - sprite.getHeight() / 2);
            sprite.setRotation((float) Math.toDegrees(body.getAngle()));
            sprite.draw(spriteBatch);
        }

        /**
         * Disposes of resources related to the block.
         */
        public void dispose() {
            sprite.getTexture().dispose();
        }
    }


    public class Pig {
        private World world;
        public Body body; // The physics body of the pig
        private Sprite sprite; // The sprite for rendering the pig
        private static final float PPM = 100; // Pixels per meter for Box2D scaling
        private float health;
        private boolean isMarkedForRemoval = false;
        private boolean isDestroyed; // To track if the pig has been destroyed
        /**
         * Constructor for the Pig class.
         *
         * @param world    The Box2D world where the pig's physics body will be created.
         * @param texture  The texture used to visually represent the pig.
         * @param position The initial position of the pig in world coordinates.
         */
        public Pig(World world, Texture texture, Vector2 position) {
            this.health = 100f; // Set the pig's initial health
            this.isDestroyed = false;

            // Create the pig's physics body
            this.sprite = new Sprite(texture);
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(position.x / PPM, position.y / PPM);
            this.body = world.createBody(bodyDef);

            // Define the pig's shape
            CircleShape shape = new CircleShape();
            shape.setRadius(texture.getWidth() / 2f / PPM);

            // Define the pig's fixture
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 0.5f;
            fixtureDef.friction = 0.5f;
            fixtureDef.restitution = 0.3f;
            this.world = world;
            sprite = new Sprite(texture); // Create a sprite from the texture
            createBody(world, texture, position);
            this.sprite.setSize(texture.getWidth(), texture.getHeight()); // Adjust to the desired size
            sprite.setOriginCenter();
            sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
            this.sprite = new Sprite(texture);
            this.health = 50;
            body.setUserData(this);
        }
        public void applyDamage(float damage) {
            this.health -= damage;
            if (this.health <= 0 && !isMarkedForRemoval) {
                isMarkedForRemoval = true; // Mark for removal
            }
        }
        public boolean isMarkedForRemoval() {
            return isMarkedForRemoval;
        }
        /**
         * Creates the physics body for the pig.
         *
         * @param world    The Box2D world instance.
         * @param texture  The texture used to visually represent the pig.
         * @param position The initial position of the pig in world coordinates.
         */
        private void createBody(World world, Texture texture, Vector2 position) {
            // Define the body
            this.world = world;
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody; // Pigs are dynamic (moveable)
            bodyDef.position.set(position.x / PPM, position.y / PPM);
            body = world.createBody(bodyDef);

            // Define the shape (circle)
            CircleShape shape = new CircleShape();
            shape.setRadius(texture.getWidth() / 2f / PPM); // Use half the texture width as radius

            // Define the fixture
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f; // Mass-related property
            fixtureDef.friction = 0.5f; // Surface friction
            fixtureDef.restitution = 0.3f; // Bounciness

            // Attach the fixture to the body
            body.createFixture(fixtureDef);
            this.body.setUserData(this);
            this.body.setAwake(false);
            shape.dispose(); // Dispose the shape after use
        }

        public void destroyBody() {
            if (body != null) {
                world.destroyBody(body); // Safely destroy the body
                body = null;
            }
        }
        /**
         * Updates the pig's sprite position to match the physics body.
         */
        public void update() {
            if (body != null) {
                Vector2 bodyPosition = body.getPosition();
                sprite.setPosition(
                    bodyPosition.x * PPM - sprite.getWidth() / 2,
                    bodyPosition.y * PPM - sprite.getHeight() / 2
                );
                sprite.setRotation((float) Math.toDegrees(body.getAngle()));
            }
        }

        public void reduceHealth(float d) {
            health -= d;
            if (health <= 0) {
                // Remove pig from world
                if (body != null) {
                    world.destroyBody(body);
                    body = null; // Prevent further updates
                }
            }
        }

        /**
         * Renders the pig's sprite using the provided SpriteBatch.
         *
         * @param spriteBatch The SpriteBatch used for rendering.
         */
        public void render(SpriteBatch spriteBatch) {
            if (body != null) {
                sprite.draw(spriteBatch);
            }
        }

        private boolean isDestroyed() {
            return isDestroyed;
        }

        /**
         * Disposes of resources related to the pig.
         */
        public void dispose() {
            sprite.getTexture().dispose();
        }

        public float getHealth() {
            return health;
        }

        public void setHealth(int health) {
            this.health = health;
        }
    }
}
