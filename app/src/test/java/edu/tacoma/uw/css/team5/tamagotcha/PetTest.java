package edu.tacoma.uw.css.team5.tamagotcha;

import org.junit.*;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

/**
 * Testing class which tests the getters and setters
 * of our Pet class.
 *
 * @author Ryan Hansen
 */
public class PetTest {

    Pet testPet;

    @Before
    public void setUp() {

        HashMap<String, String> params = new HashMap();

        params.put("hunger", "10");
        params.put("happiness", "20");
        params.put("fitness", "30");
        params.put("status", "0");

        params.put("return_time", "null");

        params.put("next_hunger_tick", "1000");
        params.put("next_happiness_tick", "2000");
        params.put("next_fitness_tick", "3000");
        params.put("last_login", "1999");
        params.put("pet_name", "George");

        testPet = new Pet(params);
    }

    @Test
    public void testGetHunger() {
        assertEquals(10, testPet.getHunger());
    }

    @Test
    public void testSetHunger() {
        testPet.setHunger(50);
        assertEquals(60, testPet.getHunger());
    }

    @Test
    public void testGetHappiness() {
        assertEquals(20, testPet.getHappiness());
    }

    @Test
    public void testSetHappiness() {
        testPet.setHappiness(50);
        assertEquals(70, testPet.getHappiness());
    }

    @Test
    public void testGetFitness() {
        assertEquals(30, testPet.getFitness());
    }

    @Test
    public void testSetFitness() {
        testPet.setFitness(50);
        assertEquals(80, testPet.getFitness());
    }

    @Test
    public void testGetStatus() {
        assertEquals(0, testPet.getStatus());
    }

    @Test
    public void tesSetStatus() {
        testPet.setStatus(3);
        assertEquals(3, testPet.getStatus());
    }

    @Test
    public void testReturnTime() {
        assertEquals(0, testPet.getReturnTime());
    }

    @Test
    public void testSetReturnTime() {
        testPet.setReturnTime(2400);
        assertEquals(2400, testPet.getReturnTime());
    }

    @Test
    public void testGetHungerTick() {
        assertEquals(1000, testPet.getmNextHungerTick());
    }

    @Test
    public void testSetHungerTick() {
        testPet.setmNextHungerTick(3000);
        assertEquals(3000, testPet.getmNextHungerTick());
    }

    @Test
    public void testGetHappinessTick() {
        assertEquals(2000, testPet.getmNextHappinessTick());
    }

    @Test
    public void testSetHappinessTick() {
        testPet.setmNextHappinessTick(3000);
        assertEquals(3000, testPet.getmNextHappinessTick());
    }

    @Test
    public void testGetFitnessTick() {
        assertEquals(3000, testPet.getmNextFitnessTick());
    }

    @Test
    public void testSetFitnessTick() {
        testPet.setmNextFitnessTick(5000);
        assertEquals(5000, testPet.getmNextFitnessTick());
    }

    @Test
    public void testSetLastLogin() {
        testPet.setmLoginTime(20000);
        assertEquals(20000, testPet.getmLoginTime());
    }

    @Test
    public void testGetPetName() {
        assertEquals("George", testPet.getPetName());
    }

    @Test
    public void testSetPetName() {
        testPet.setPetName("Paul");
        assertEquals("Paul", testPet.getPetName());
    }

}
