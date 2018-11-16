package uk.ac.qub.eeecs.gage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.CollisionDetector;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.game.platformDemo.Platform;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlatformTest {

//Testing User Story 18
// "...method will return true if a given Platform is overlapping with any other Platform instance"

    @Mock
    Platform platform1, platform2;
    private ArrayList<Platform> mPlatforms;

    public boolean checkIfCollision() {
        boolean isOverlapping = false;

        if (mPlatforms.size() == 0) {
            return false;
        }

        if (mPlatforms.size() == 1) {
            return false;
        }

        for (int i = 0; i < (mPlatforms.size()-1); i++) {
            isOverlapping = false;
            if (CollisionDetector.isCollision(mPlatforms.get(i).getBound(), mPlatforms.get(mPlatforms.size() - 1).getBound())) {
                isOverlapping = true;
            }
        } return isOverlapping;
    }

    //Test 1: Produce two objects that overlap, check that they return true
    @Test
    public void checkOverLapping_True() {
        mPlatforms = new ArrayList<>();
        mPlatforms.add(platform1);
        mPlatforms.add(platform2);

        BoundingBox bound1 = new BoundingBox(100, 100, 50, 50);
        Vector2 position1 = new Vector2(100,100);
        when(platform1.getBound()).thenReturn(bound1);
        platform1.position = position1;

        BoundingBox bound2 = new BoundingBox(120, 80, 50, 50);
        Vector2 position2 = new Vector2(120,80);
        when(platform2.getBound()).thenReturn(bound2);
        platform2.position = position2;

        checkIfCollision();

        assert(checkIfCollision());
    }

    //Test 2: Produce an empty ArrayList and check that it returns false
    @Test
    public void checkOverLapping_EmptyArrayList() {
        mPlatforms = new ArrayList<>();

        checkIfCollision();

        assert(!checkIfCollision());
    }

    //Test 3: Produce an ArrayList of one Platform and check that it returns false
    @Test
    public void checkOverLapping_OnePlatform() {
        mPlatforms = new ArrayList<>();

        BoundingBox bound1 = new BoundingBox(100, 100, 50, 50);
        Vector2 position1 = new Vector2(100,100);
        when(platform1.getBound()).thenReturn(bound1);
        platform1.position = position1;
        mPlatforms.add(platform1);

        checkIfCollision();

        assert (!checkIfCollision());
    }
    
}
