package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class GT4500Test {

  private GT4500 ship;

  private TorpedoStore mockPS, mockSS;

  @BeforeEach
  public void init(){
    mockPS = mock(TorpedoStore.class);
    mockSS = mock(TorpedoStore.class);

    ship = new GT4500(mockPS, mockSS);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPS.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPS.isEmpty()).thenReturn(false);
    when(mockSS.isEmpty()).thenReturn(false);
    when(mockPS.fire(1)).thenReturn(true);
    when(mockSS.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
  }

  @Test

  public void fireTorpedo_from_primary_first(){
    when(mockPS.fire(1)).thenReturn(true); 
    when(mockPS.isEmpty()).thenReturn(false);
    when(mockSS.isEmpty()).thenReturn(true); //make sure that don't fire at all

    boolean success = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, success);
  }

  @Test

  public void alternating_fire_test(){

    //Arrange
    when(mockPS.fire(1)).thenReturn(true);
    when(mockPS.isEmpty()).thenReturn(false);
    when(mockSS.fire(1)).thenReturn(true);
    when(mockSS.isEmpty()).thenReturn(false);


    //Act
    boolean success1 = ship.fireTorpedo(FiringMode.SINGLE);
    when(mockPS.fire(1)).thenReturn(false); // make sure this returns false
    boolean success2 = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    assertEquals(true, success1 & success2); 
  }
  @Test
  public void fire_from_secondary_if_primary_is_empty(){
      //Arrange
      when(mockPS.isEmpty()).thenReturn(true);
      when(mockSS.isEmpty()).thenReturn(false);
      when(mockSS.fire(1)).thenReturn(true);
      //Act      
      boolean success = ship.fireTorpedo(FiringMode.SINGLE);
      //Assert
      assertEquals(true, success);
  }
  @Test
  public void fire_after_failure(){
    //Arrange
    when(mockPS.fire(1)).thenReturn(false); //fire will fail
    when(mockSS.fire(1)).thenThrow(IllegalArgumentException.class); //if we fire another torpedo, we'll thow an exception

    //Act

    //Assert
    assertDoesNotThrow(() -> {ship.fireTorpedo(FiringMode.SINGLE);}); //if we won't get an exception, there's no call for fire after failure
  }

  @Test 
  public void fire_both(){
    //Arrange
    when(mockPS.isEmpty()).thenReturn(false);
    when(mockSS.isEmpty()).thenReturn(false);
    when(mockPS.fire(1)).thenReturn(true);
    when(mockSS.fire(1)).thenReturn(true);

    //Act
    boolean success = ship.fireTorpedo(FiringMode.ALL);

    //Assert
    assertEquals(true, success);
  }

  @Test
  public void fire_after_exception(){
    //Arrange
    when(mockPS.isEmpty()).thenReturn(true);
    when(mockSS.isEmpty()).thenReturn(true);

    //Act
    boolean success = ship.fireTorpedo(FiringMode.SINGLE);
    
    assertEquals(false, success);
  }

  @Test
  public void fire_primary_if_secondary_empty() {
    //Arrange
    when(mockSS.isEmpty()).thenReturn(true);
    when(mockPS.isEmpty()).thenReturn(false);
    when(mockPS.fire(1)).thenReturn(true);
    //Act
    boolean success1 = ship.fireTorpedo(FiringMode.SINGLE);
    when(mockPS.isEmpty()).thenReturn(false);
    boolean success2 = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    assertEquals(true, success1 & success2);
  }

}
