package io.HeslingtonHustle.tests;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testBoyAssetExists() {
        assertTrue("Asset for boy idle sprite exists", Gdx.files.internal("boy_idle.png").exists());
        //this is a bad test i was just checking to see if i would run a test, which i can :)
    }
}
