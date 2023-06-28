//The ImageProcessor01 class

//The purpose of this class is to provide a
// simple example of an image processing class
// that is compatible with the program named
// Framework01.

//The constructor for the class displays a small
// frame on the screen with a single textfield.
// The purpose of the text field is to allow the
// user to enter a value that represents the
// slope of a line.  In operation, the user
// types a value into the text field and then
// clicks the Replot button on the main image
// display frame.  The user is not required to
// press the Enter key after typing the new
// value, but it doesn't do any harm to do so.

//The method named processImage receives a 3D
// array containing alpha, red, green, and blue
// values for an image.  The values are received
// as type int (not type byte).

// The threeDPix array that is received is
// modified to cause a white diagonal line to be
// drawn down and to the right from the upper
// left-most corner of the image.  The slope of
// the line is controlled by the value that is
// typed into the text field.  Initially, this
// value is 1.0.  The image is not modified in
// any other way.

//To cause a new line to be drawn, type a slope
// value into the text field and click the Replot
// button at the bottom of the image display
// frame. Negative slope values are not allowed.

//This class extends Frame.  However, a
// compatible class is not required to extend the
// Frame class. This example extends Frame
// because it provides a GUI for user data input.

//A compatible class is required to implement the
// interface named Framework01Intfc.

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

class ImageProcessor01 extends Frame
                  implements Framework01Intfc{

  double slope;//Controls the slope of the line
  String inputData;//Obtained via the TextField
  TextField inputField;//Reference to TextField

  //Constructor must take no parameters
  ImageProcessor01(){
    //Create and display the user-input GUI.
    setLayout(new FlowLayout());

    Label instructions = new Label(
               "Type a slope value and Replot.");
    add(instructions);

    inputField = new TextField("1.0",5);
    add(inputField);

    setTitle("Copyright 2004, Baldwin");
    setBounds(400,0,200,100);
    setVisible(true);
  }//end constructor

  //The following method must be defined to
  // implement the Framework01Intfc interface.
  public int[][][] processImg(
                             int[][][] threeDPix,
                             int imgRows,
                             int imgCols){

    //Display some interesting information
    System.out.println("Program test");
    System.out.println("Width = " + imgCols);
    System.out.println("Height = " + imgRows);

    //Make a working copy of the 3D array to
    // avoid making permanent changes to the
    // image data.
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

    //Get slope value from the TextField
    slope = Double.parseDouble(
                           inputField.getText());

    /*
    The equation for a straight line
    You may recall from your math class that the 
    equation for a straight line in Cartesian 
    coordinates is:
    
    y = m * x + b
    
    where:
    m is the slope of the line
    b is the intersection of the line with the y-axis
    
    In our case, the intersection value is 0 and the 
    slope is the value obtained from the text field.
    You may also remember that when drawing graphics 
    on a computer screen using Java, the positive 
    direction for the y-axis is down the screen. 
    Thus, a line with a positive slope will go down 
    and to the right.
    */


    //Draw a white diagonal line on the image
    for(int col = 0;col < imgCols;col++){
      int row = (int)(slope*col);
      if(row > imgRows -1)break;
      //Set values for alpha, red, green, and
      // blue colors.
      temp3D[row][col][0] = (byte)0xff;
      temp3D[row][col][1] = (byte)0xff;
      temp3D[row][col][2] = (byte)0xff;
      temp3D[row][col][3] = (byte)0xff;
    }//end for loop
    //Return the modified array of image data.
    return temp3D;
  }//end processImg
}//end class ImageProcessor01