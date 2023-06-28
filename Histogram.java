/*File Histogram.java.java
Copyright 2004, R.G.Baldwin

Illustrates contrast and brightness control.

This program is designed to be driven by the
program named Framework01.  Enter the following at
the command line to run this program.

java Framework01 Histogram imageFileName

The purpose of this program is to illustrate how
to modify the contrast and the brightness of an
image by modifying the distribution of color
values.

The contrast of an image is determined by the
width of the distribution of the color values
belonging to the image.  If all color values are
grouped together in a very tight distribution,
the details in the image will tend to be washed
out and the overall appearance of the image will
tend toward some shade of gray.  The shade will
depend on the location of the grouping between
the extremes of 0 and 255.

At the extremes, if the color values are closely
grouped near zero, the colors will range from
black to dark gray.  If the color values are
grouped near 255, the colors will range from very
light gray to white.

The contrast of the image can be increased by
increasing the width of the distribution of the
color values.  The contrast can be decreased by
decreasing the width of the distribution.

The overall brightness of an image is determined
by the location of the grouping of color values.
If the grouping tends to be near the upper limit
of 255, the image will tend to be very bright.
If the grouping tends to be near the lower limit
of 0, the image will tend to be very dark.

The brightness of an image can be increased by
moving the grouping toward 255, and can be
decreased by moving the grouping toward 0.

A straightforward way to change the width of the
distribution with, or without changing the
general location of the distribution consists of
the following steps:
* Calculate the mean or average value of all the
color values.
* Subtract the mean from every color value,
causing the mean value to be shifted to zero.
* Multiply every color value by the same scale
factor.  If the scale factor is greater than 1.0,
the width of the distribution will be increased.
If the scale factor is less than 1.0, the width
of the distribution will be decreased.
* Add the original mean value, or a new mean
value to every color value to restore the
distribution to its original location or to move
it to a new location.

The above steps will change the contrast of the
image, and may, or may not change its
brightness depending on the value that is
added in the final step.

A straightforward way to change the location of
the distribution is to add (or subtract) the same
constant to every color value.  This will change
the brightness without changing the contrast.

After performing these operations, it is
important to make certain that all color values
fall within the range of an unsigned byte.  This
requires eliminating all color values that are
greater than 255 and all color values that are
less than 0.  A simple way to do this is to
simply clip the color values at those two limits
if they fall outside the limits.

Remember, however, that clipping can change the
width, the shape, and the mean value of the
distribution if the location of the distribution
is near the lower or upper limit.  Clipping
values to the limits will tend to narrow the
distribution and to create a spike in the
distribution at the value of the limit.

There are several ways to measure the width of a
distribution.  One way is to to measure the
distance between the minimum value and the
maximum value.  This is not a very good way
because a couple of outliers can lead to
erroneous conclusions regarding the width of the
distribution.

A better way is to compute the root mean square
(rms) value of all the color values in the
distribution.  This approach is less sensitive to
outliers and produces results that are more
representative of the bulk of the distribution.
For distributions of known shapes, it is possible
to say what percentage of the color values fall
within a range bounded by plus and minus the rms
value.  For example, if the distribution is
uniform (which it probably isn't), approximately
60-percent of all the color values will fall
within the range bounded by the rms value on
either side of the mean.

If the shape of the distribution is not
known, we can only say that for an image
containing a large number of different color
values, a large percentage of those color values
will lie within the range bounded by plus and
minus the rms value.

The rms value is computed and displayed in this
program solely to provide information to the
user.  The rms value is not used in the
computations that control the contrast and
brightness of the image.  For most images, the
user should be able to see a direct
correspondence between the rms value and the
contrast.  For small rms values, the contrast in
the image should appear to be washed out and the
overall color of the image should tend towards
gray.

The program provides a GUI with two text fields
and two histograms.

The text fields make it possible for the user
to modify the contrast and brightness of the
processed image by entering new values for
Contrast and Brightness and pressing the Replot
button on the main display.

The values entered into the text fields are
multiplicative factors.  The initial value in
each text field is 1.0.  For these values, the
processed image should be identical to the
new image.

To increase the contrast or the brightness, type
a value greater than 1.0 into the coresponding
text field and press the Replot button.

To decrease the contrast or the brightness, type
a value less than 1.0 into the text field and
press the Replot button.

It isn't necessary to press the Enter key to type
the value into the text field, but doing so won't
cause any harm.

Entering a value that cannot be converted to a
value of type double will cause the program to
throw an exception.

The top histogram shows the distribution of
color values for the original image.  The bottom
histogram shows the distribution of color values
for the modified image.

The histogram values are normalized to a peak
value of 100, exclusive of the values at 0 and
255.  These two values can, and often will
exceed 100 and go out of plotting range on the
histograms.

Tested using SDK 1.4.2 and WinXP
************************************************/
import java.awt.*;
import javax.swing.Box;
import javax.swing.BoxLayout;

class Histogram extends Frame
                           implements Framework01Intfc{

  TextField contrastField;
  TextField brightField;
  Panel input;
  OrigHistogramPanel origHistPanel;
  NewHistogramPanel newHistPanel;
  int[] origHistogram = new int[256];
  int[] newHistogram = new int[256];


  //Constructor must take no parameters
  Histogram(){
    //Create a Box container with a vertical
    // layout and place it in the center of
    // the Frame.
    Box aBox = new Box(BoxLayout.Y_AXIS);
    this.add(aBox,BorderLayout.CENTER);

    //Create and place the user input panel at
    // the top of the vertical stack.  Make it
    // yellow
    input = new Panel();

    //Create panels to group the labels with the
    // text fields and add them to the input
    // panel under FlowLayout.
    Panel contrastPanel = new Panel();
    contrastPanel.add(new Label("Contrast"));
    contrastField = new TextField("1.0",5);
    contrastPanel.add(contrastField);
    input.add(contrastPanel);

    Panel brightnessPanel = new Panel();
    brightnessPanel.add(new Label("Brightness"));
    brightField = new TextField("1.0",5);
    brightnessPanel.add(brightField);
    input.add(brightnessPanel);

    input.add(new Label(
                      "Press Replot to change"));

    input.setBackground(Color.YELLOW);
    aBox.add(input);

    //Create and place the panel for the
    // original histogram in the middle of the
    // stack.  Make it green.
    origHistPanel = new OrigHistogramPanel();
    origHistPanel.setBackground(Color.GREEN);
    aBox.add(origHistPanel);

    //Create and place the panel for the new
    // histogram at bottom of the stack.  Make
    // it orange.
    newHistPanel = new NewHistogramPanel();
    newHistPanel.setBackground(Color.ORANGE);
    aBox.add(newHistPanel);

    //Set miscellaneous properties.
    setTitle("Copyright 2004, Baldwin");
    setBounds(400,0,275,400);
    setVisible(true);
  }//end constructor
  //-------------------------------------------//

  //An inner class for the original histogram
  // panel.  This is necessary to make it
  // possible to override the paint method.
  class OrigHistogramPanel extends Panel{
    public void paint(Graphics g){
      //Following constant corrects for positive
      // direction on the y-axis.
      final int flip = 110;
      //Following constant is used to shift the
      // histogram plot 5 pixels to the right.
      final int shift = 5;
      //Draw the horizontal axis
      g.drawLine(0 + shift,flip,
                               255 + shift,flip);
      //Draw the histogram
      for(int cnt = 0;cnt < origHistogram.length;
                                          cnt++){
        g.drawLine(cnt + shift,flip - 0,
                    cnt + shift,
                      flip - origHistogram[cnt]);
      }//end for loop
    }//end paint
  }//end class OrigHistogramPanel
  //-------------------------------------------//

  //An inner class for the new histogram
  // panel.  This is necessary to make it
  // possible to override the paint method.
  class NewHistogramPanel extends Panel{
    public void paint(Graphics g){
      //Following constant corrects for positive
      // direction on the y-axis.
      final int flip = 110;
      //Following constant is used to shift the
      // histogram plot 5 pixels to the right.
      final int shift = 5;
      //Draw the horizontal axis
      g.drawLine(0 + shift,flip,
                               255 + shift,flip);
      //Draw the histogram
      for(int cnt = 0;cnt < newHistogram.length;
                                          cnt++){
        g.drawLine(cnt + shift,flip - 0,
                    cnt + shift,
                      flip - newHistogram[cnt]);
      }//end for loop
    }//end paint
  }//end class NewHistogramPanel
  //-------------------------------------------//

  //This method is required by Framework01Intfc.
  public int[][][] processImg(
                            int[][][] threeDPix,
                            int imgRows,
                            int imgCols){

    System.out.println("Width = " + imgCols);
    System.out.println("Height = " + imgRows);

    //Get user input values for contrast and
    // brightness.  These values will be used as
    // multipliers to change the contrast and
    // the brightness.
    double contrast = Double.parseDouble(
                        contrastField.getText());
    double brightness = Double.parseDouble(
                          brightField.getText());

    //Make a working copy of the 3D array to
    // avoid making permanent changes to the
    // original image data.
    int[][][] output3D =
                    new int[imgRows][imgCols][4];
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        output3D[row][col][0] =
                          threeDPix[row][col][0];
        output3D[row][col][1] =
                          threeDPix[row][col][1];
        output3D[row][col][2] =
                          threeDPix[row][col][2];
        output3D[row][col][3] =
                          threeDPix[row][col][3];
      }//end inner loop
    }//end outer loop


    //Get, save, display, and remove the mean.
    int mean = getMean(output3D,imgRows,imgCols);
    System.out.println("Original mean: " + mean);
    removeMean(output3D,imgRows,imgCols,mean);

    //Get and display the rms value.  The rms
    // value is for user information only.  It
    // is not actually used by the program.
    int rms = getRms(output3D,imgRows,imgCols);
    System.out.println("Original rms: " + rms);

    //Scale each color value by the contrast
    // multiplier.  This will either expand or
    // compress the distribution.
    scale(output3D,imgRows,imgCols,contrast);
    System.out.println("New rms"
             + getRms(output3D,imgRows,imgCols));

    //Restore the mean to a non-zero value by
    // adding the same value to each color value.
    // The value added is the product of the
    // new mean and the brightness multiplier.
    shiftMean(output3D,imgRows,imgCols,
                         (int)(brightness*mean));
    System.out.println("New mean : "
            + getMean(output3D,imgRows,imgCols));

    //Clip all color values at 0 and 255 to make
    // certain that no color value is out of the
    // range of an unsigned byte.
    clip(output3D,imgRows,imgCols);
    System.out.println();

    //Create and draw the two histograms
    origHistogram = getHistogram(threeDPix,
                                imgRows,imgCols);
    origHistPanel.repaint();

    newHistogram = getHistogram(output3D,
                                imgRows,imgCols);
    newHistPanel.repaint();

    //Return the modified 3D array of pixel data.
    return output3D;

  }//end processImg
  //-------------------------------------------//

  //Method to create a histogram and return it in
  // an array of type int.  The histogram is
  // normalized to a peak value of 100 exclusive
  // of the values at 0 and 255.  Those values
  // can, and often do exceed 100.
  int[] getHistogram(int[][][] data3D,
                        int imgRows,int imgCols){
    int[] hist = new int[256];
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        hist[data3D[row][col][1]]++;
        hist[data3D[row][col][2]]++;
        hist[data3D[row][col][3]]++;
      }//end inner for loop
    }//end outer for loop
    //Get the maximum value, exclusive of the
    // values at 0 and 255
    int max = 0;
    for(int cnt = 1;cnt < hist.length - 1;cnt++){
      if(hist[cnt] > max){
        max = hist[cnt];
      }//end if
    }//end for loop

    //Normalize histogram to a peak value of 100
    // based on the max value, exclusive of the
    // values at 0 and 255
    for(int cnt = 0;cnt < hist.length;cnt++){
      hist[cnt] = 100 * hist[cnt]/max;
    }//end for loop
    return hist;
  }//end getHistogram
  //-------------------------------------------//

  //Method to calculate and return the mean
  // of all the color values
  int getMean(int[][][] data3D,int imgRows,
                                    int imgCols){

    int pixelCntr = 0;
    long accum = 0;
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        accum += data3D[row][col][1];
        accum += data3D[row][col][2];
        accum += data3D[row][col][3];
        pixelCntr += 3;
      }//end inner for loop
    }//end outer for loop

    return (int)(accum/pixelCntr);

  }//end getMean
  //-------------------------------------------//

  //Method to remove the mean causing the new
  // mean value to be 0
  void removeMean(int[][][] data3D,int imgRows,
                           int imgCols,int mean){
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        data3D[row][col][1] -= mean;
        data3D[row][col][2] -= mean;
        data3D[row][col][3] -= mean;
      }//end inner for loop
    }//end outer for loop
  }//end removeMean
  //-------------------------------------------//

  //Method to calculate the root mean square
  // value of all the color values.
  int getRms(int[][][] data3D,int imgRows,
                                    int imgCols){
    int pixelCntr = 0;
    long accum = 0;
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        accum += data3D[row][col][1] *
                             data3D[row][col][1];
        accum += data3D[row][col][2] *
                             data3D[row][col][2];
        accum += data3D[row][col][3] *
                             data3D[row][col][3];
        pixelCntr += 3;
      }//end inner for loop
    }//end outer for loop
    int meanSquare = (int)(accum/pixelCntr);
    int rms = (int)(Math.sqrt(meanSquare));
    return rms;
  }//end getRms
  //-------------------------------------------//

  //Method to scale the data and expand or
  // compress the distribution
  void scale(int[][][] data3D,int imgRows,
                       int imgCols,double scale){
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        data3D[row][col][1] *= scale;
        data3D[row][col][2] *= scale;
        data3D[row][col][3] *= scale;
      }//end inner for loop
    }//end outer for loop
  }//end scale
  //-------------------------------------------//

  //Method to shift the mean to a new value.
  void shiftMean(int[][][] data3D,int imgRows,
                        int imgCols,int newMean){
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        data3D[row][col][1] += newMean;
        data3D[row][col][2] += newMean;
        data3D[row][col][3] += newMean;
      }//end inner for loop
    }//end outer for loop
  }//end shiftMean
  //-------------------------------------------//

  //Method to clip the color data at 0 and 255
  void clip(int[][][] data3D,int imgRows,
                                    int imgCols){
    for(int row = 0;row < imgRows;row++){
      for(int col = 0;col < imgCols;col++){
        if(data3D[row][col][1] < 0)
          data3D[row][col][1] = 0;
        if(data3D[row][col][1] > 255)
          data3D[row][col][1] = 255;

        if(data3D[row][col][2] < 0)
          data3D[row][col][2] = 0;
        if(data3D[row][col][2] > 255)
          data3D[row][col][2] = 255;

        if(data3D[row][col][3] < 0)
          data3D[row][col][3] = 0;
        if(data3D[row][col][3] > 255)
          data3D[row][col][3] = 255;

      }//end inner for loop
    }//end outer for loop
  }//end clip
  //-------------------------------------------//

}//end class Histogram