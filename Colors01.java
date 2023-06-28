//34567890123456789012345678901234567890123456789
//Note: this is wide format for small fonts.
//=============================================//

/*File Colors01.java
Copyright 2005, R.G.Baldwin

This program illustrates color intensity cotrol,
color filtering, and color inversion.

The program is designed to be driven by the
program named Framework01.  Enter the following at
the command line to run this program.

java Framework01 Colors01 gifFileName

The program places two GUIs on the screen. One
GUI displays the "before" and "after" images of 
an image that is subjected to color intensity 
cotrol, color filtering, and color inversion. The
image at the top is the "before" image.  The 
image at the bottom is the "after" image.

The other GUI provides instructions and 
components by which the user can control the
processing of the image.  A check box appears
near the top of this GUI.  If the user checks
the check box, color filtering is performed
before color inversion takes place.  If the
check box is not checked, no color inversion is
performed.

This GUI also provides three sliders that make
it possible for the user to control color 
intensity and filtering.  Each slider controls
the intensity of a single color.  The intensity 
control ranges from 0% to 100% of the original
intensity value for each pixel.

If all three sliders are adjusted to the same
percentage value and the replot button is 
pressed, the overall intensity of the image is
modified with no change in the relative
contribution of each color. This makes it 
possible to control the overall intensity of the
image from very dark (black) to the maximum
intensity supported by the image.

If the three sliders are adjusted to different
percentage values and the replot button is
pressed, color filtering occurs. By this, I mean
that the intensity of one color is changed
relative to the intensity of the other colors.
This makes it possible, for example to adjust the
"warmth" of the image by emphasizing red over
blue, or to make the image "cooler" by
emphasizing blue over red.  It is also possible
to totally isolate and view the contributions of
red, green, and blue to the overall image.

As written, the program applies color filtering
before color inversion.  Sample code is also 
provided that can be used to modify the program 
to cause it to provide color inversion before 
color filtering.  There is a significant 
difference in the results produced by these two
different approaches.

This program illustrates the modification of
the pixels in an image.  It works best with
an image file that contains no transparent areas.

The pixel modifications performed in this program
have no impact on transparent pixels.  If you
don't see what you expect, it may be because your
image contains transparent pixels.

Tested using JDK 17.0.1 and Windowqs 7
************************************************/
import java.awt.*;
import javax.swing.*;

class Colors01 extends Frame
                     implements Framework01Intfc{

  //GUI components used to control color
  // filtering and color inversion.
  JSlider redSlider;
  JSlider greenSlider;
  JSlider blueSlider;
  JCheckBox checkBox;

  //Constructor must take no parameters
  Colors01(){
    //Create and display the user-input GUI.
    setLayout(new GridLayout(0,1));
    //Provide user instructions at the top of the
    // GUI.
    add(new Label(
            "Adjust Color Sliders and Replot."));

    
    //Provide a check box that is used to request
    // color inversion.
    checkBox = new JCheckBox();
    add(new Label("Check the box to Invert Colors"));
    add(checkBox);
    
    //Create three sliders each with a range of
    // 0 to 100 and an initial value of 100.
    redSlider = new JSlider(0,100,100);
    //Put a label next to the slider.
    add(redSlider);
    add(new Label("Red Slider"));


    greenSlider = new JSlider(0,100,100);
    add(greenSlider);
    add(new Label("Green Slider"));


    blueSlider = new JSlider(0,100,100);
    add(blueSlider);
    add(new Label("Blue Slider"));

    
    //Put numeric labels and tick marks on the
    // sliders.
    redSlider.setMajorTickSpacing(20);
    redSlider.setMinorTickSpacing(5);
    redSlider.setPaintTicks(true);
    redSlider.setPaintLabels(true);
    redSlider.setSnapToTicks(true);
    
    greenSlider.setMajorTickSpacing(20);
    greenSlider.setMinorTickSpacing(5);
    greenSlider.setPaintTicks(true);
    greenSlider.setPaintLabels(true);
    greenSlider.setSnapToTicks(true);
    
    blueSlider.setMajorTickSpacing(20);
    blueSlider.setMinorTickSpacing(5);
    blueSlider.setPaintTicks(true);
    blueSlider.setPaintLabels(true);
    blueSlider.setSnapToTicks(true);

    setTitle("Copyright Baldwin");
    setBounds(400,0,175,400);
    setVisible(true);
  }//end constructor

  //The following method must be defined to
  // implement the ImgIntfc02 interface.
  public int[][][] processImg(
                             int[][][] threeDPix,
                             int imgRows,
                             int imgCols){

    //Make a working copy of the 3D array to
    // avoid making permanent changes to the
    // raw image data.
    int[][][] temp3D =
                    new int[imgRows][imgCols][4];
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        temp3D[row][col][0] =
                          threeDPix[row][col][0];
        temp3D[row][col][1] =
                          threeDPix[row][col][1];
        temp3D[row][col][2] =
                          threeDPix[row][col][2];
        temp3D[row][col][3] =
                          threeDPix[row][col][3];
      }//end inner loop
    }//end outer loop

    //Get the current values of the three
    // sliders.  This information will be used to
    // scale the red, green, and blue pixel
    // values to new values ranging from 0% to
    // 100% of the original values in order to 
    // implement color filtering.
    int redScale = redSlider.getValue();
    int greenScale = greenSlider.getValue();
    int blueScale = blueSlider.getValue();

    //Process each pixel value to apply color
    // filtering either with or without color
    // inversion depending on the state of the
    // check box.
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        if(!checkBox.isSelected()){
          //Apply color filtering but no color
          // inversion
          temp3D[row][col][1] = 
              temp3D[row][col][1] * redScale/100;
          temp3D[row][col][2] = 
            temp3D[row][col][2] * greenScale/100;
          temp3D[row][col][3] = 
             temp3D[row][col][3] * blueScale/100;
        }else{
          //Apply color filtering with inversion.
          // Compile the following block of code
          // to filter before inverting.
          temp3D[row][col][1] = 255 - 
              temp3D[row][col][1] * redScale/100;
          temp3D[row][col][2] = 255 - 
            temp3D[row][col][2] * greenScale/100;
          temp3D[row][col][3] = 255 - 
             temp3D[row][col][3] * blueScale/100;
             
          /*Compile the following block of code
             instead to invert before filtering.
          temp3D[row][col][1] = (255 - 
             temp3D[row][col][1]) * redScale/100;
          temp3D[row][col][2] = (255 - 
           temp3D[row][col][2]) * greenScale/100;
          temp3D[row][col][3] = (255 - 
            temp3D[row][col][3]) * blueScale/100;
          */
        }//end else
      }//end inner loop
    }//end outer loop
    //Return the modified array of image data.
    return temp3D;
  }//end processImg
}//end class Colors01