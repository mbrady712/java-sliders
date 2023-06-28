import java.awt.*;
import javax.swing.*;
class Proj12 extends Frame implements Framework01Intfc{

    //Create slider objects
    JSlider redSlider;
    JSlider greenSlider;
    JSlider blueSlider;
    JSlider alphaSlider;

    //Create objects and arrays for histograms
    TextField contrastField;
    TextField brightField;
    Panel input;
    OrigHistogramPanel origHistPanel;
    NewHistogramPanel newHistPanel;
    int[] origHistogram = new int[256];
    int[] newHistogram = new int[256];

    Proj12(){
        System.out.println(
        "I certify that this program is my own work \n" +
        "and is not the work of others. I agree not \n" +
        "to share my solution with others.\n" +
        "Michael Brady\n" +
        "\n" +
        "Slider pointers must snap to tick marks.");

        setLayout(new GridLayout(6, 2));

        //Create rgba section of GUI
    
        redSlider = new JSlider(0,100,20);
        add(redSlider);
        add(new Label("Red"));

        greenSlider = new JSlider(0,100,40);
        add(greenSlider);
        add(new Label("Green"));

        blueSlider = new JSlider(0,100,60);
        add(blueSlider);
        add(new Label("Blue"));

        alphaSlider = new JSlider(0,100,80);
        add(alphaSlider);
        add(new Label("Alpha"));

        redSlider.setMajorTickSpacing(20);
        redSlider.setPaintTicks(true);
        redSlider.setPaintLabels(true);
        redSlider.setSnapToTicks(true);
        
        greenSlider.setMajorTickSpacing(20);
        greenSlider.setPaintTicks(true);
        greenSlider.setPaintLabels(true);
        greenSlider.setSnapToTicks(true);
        
        blueSlider.setMajorTickSpacing(20);
        blueSlider.setPaintTicks(true);
        blueSlider.setPaintLabels(true);
        blueSlider.setSnapToTicks(true);

        alphaSlider.setMajorTickSpacing(20);
        alphaSlider.setPaintTicks(true);
        alphaSlider.setPaintLabels(true);
        alphaSlider.setSnapToTicks(true);

        //Add histogram GUI

        //Contrast and brightness panel

        input = new Panel();

        Panel contrastPanel = new Panel();
        contrastPanel.add(new Label("Contrast"));
        contrastField = new TextField("2.0",5);
        contrastPanel.add(contrastField);
        input.add(contrastPanel);

        Panel brightnessPanel = new Panel();
        brightnessPanel.add(new Label("Brightness"));
        brightField = new TextField("2.0",5);
        brightnessPanel.add(brightField);
        input.add(brightnessPanel);

        input.add(new Label("Press Replot to change"));

        input.setBackground(Color.YELLOW);
        add(input);

        //Blank panel

        Panel blank = new Panel();
        add(blank);

        //Histogram panels

        origHistPanel = new OrigHistogramPanel();
        origHistPanel.setBackground(Color.GREEN);
        add(origHistPanel);

        newHistPanel = new NewHistogramPanel();
        newHistPanel.setBackground(Color.ORANGE);
        add(newHistPanel);

        setTitle("Michael Brady");
        setBounds(400,0,550,720);
        setVisible(true);
    }

    //Classes for original and new histograms
    class OrigHistogramPanel extends Panel{
        public void paint(Graphics g){
            final int flip = 110;
            final int shift = 5;
            //Draw the horizontal axis
            g.drawLine(0 + shift,flip, 255 + shift,flip);
            //Draw the histogram
            for(int cnt = 0;cnt < origHistogram.length; cnt++){
                g.drawLine(cnt + shift,flip - 0, cnt + shift, flip - origHistogram[cnt]);
            }
        }
    }
      
    class NewHistogramPanel extends Panel{
        public void paint(Graphics g){
            final int flip = 110;
            final int shift = 5;
            //Draw the horizontal axis
            g.drawLine(0 + shift,flip, 255 + shift,flip);
            //Draw the histogram
            for(int cnt = 0;cnt < newHistogram.length; cnt++){
                g.drawLine(cnt + shift,flip - 0, cnt + shift, flip - newHistogram[cnt]);
            }
        }
    }

    //The following method must be defined to implement the ImgIntfc02 interface.
    public int[][][] processImg(int[][][] threeDPix, int imgRows, int imgCols){

        //Make a working copy of the 3D array
        int[][][] temp3D = new int[imgRows][imgCols][4];
        for(int row = 0;row < imgRows;row++){
            for(int col = 0;col < imgCols;col++){
                temp3D[row][col][0] = threeDPix[row][col][0];
                temp3D[row][col][1] = threeDPix[row][col][1];
                temp3D[row][col][2] = threeDPix[row][col][2];
                temp3D[row][col][3] = threeDPix[row][col][3];
            }
        }

        //Apply alpha and color changes
        //Get the current values of the sliders.  
        int redScale = redSlider.getValue();
        int greenScale = greenSlider.getValue();
        int blueScale = blueSlider.getValue();
        int alphaScale = alphaSlider.getValue();

        //Process each pixel value to apply alpha and color filtering
        for(int row = 0;row < imgRows;row++){
            for(int col = 0;col < imgCols;col++){
                temp3D[row][col][0] = temp3D[row][col][0] * alphaScale/100;
                temp3D[row][col][1] = temp3D[row][col][1] * redScale/100;
                temp3D[row][col][2] = temp3D[row][col][2] * greenScale/100;
                temp3D[row][col][3] = temp3D[row][col][3] * blueScale/100;
            }
        }

        //Apply contrast and brightness changes and draw histogram

        //Get contrast and brightness values
        double contrast = Double.parseDouble(contrastField.getText());
        double brightness = Double.parseDouble(brightField.getText());

        //Get, save, display, and remove the mean.
        int mean = getMean(temp3D,imgRows,imgCols);
        removeMean(temp3D,imgRows,imgCols,mean);

        //Scale each color value by the contrast
        //multiplier.  This will either expand or
        //compress the distribution.
        scale(temp3D,imgRows,imgCols,contrast);

        //Restore the mean to a non-zero value by
        //adding the same value to each color value.
        //The value added is the product of the
        //new mean and the brightness multiplier.
        shiftMean(temp3D,imgRows,imgCols,(int)(brightness*mean));

        //Clip all color values at 0 and 255 to make
        //certain that no color value is out of the
        //range of an unsigned byte.
        clip(temp3D,imgRows,imgCols);

        //Create and draw the two histograms
        origHistogram = getHistogram(threeDPix,imgRows,imgCols);
        origHistPanel.repaint();

        newHistogram = getHistogram(temp3D,imgRows,imgCols);
        newHistPanel.repaint();

        //Return the modified array of image data.
        return temp3D;
    }

    //Methods for creating histogram and altering contrast and brightness.

    int[] getHistogram(int[][][] data3D,int imgRows,int imgCols){

        int[] hist = new int[256];
        for(int row = 0;row < imgRows;row++){
            for(int col = 0;col < imgCols;col++){
                hist[data3D[row][col][1]]++;
                hist[data3D[row][col][2]]++;
                hist[data3D[row][col][3]]++;
            }
        }

        //Get the maximum value, exclusive of the values at 0 and 255
        int max = 0;
        for(int cnt = 1;cnt < hist.length - 1;cnt++){
            if(hist[cnt] > max){
                max = hist[cnt];
            }
        }

        //Normalize histogram to a peak value of 100 based on the max value, exclusive of the 
        //values at 0 and 255
        for(int cnt = 0;cnt < hist.length;cnt++){
            hist[cnt] = 100 * hist[cnt]/max;
        }
        return hist;
    }

    //Method to calculate and return the mean of all the color values
    int getMean(int[][][] data3D, int imgRows, int imgCols){
        int pixelCntr = 0;
        long accum = 0;
        for(int row = 0;row < imgRows;row++){
            for(int col = 0;col < imgCols;col++){
                accum += data3D[row][col][1];
                accum += data3D[row][col][2];
                accum += data3D[row][col][3];
                pixelCntr += 3;
            }
        }
        return (int)(accum/pixelCntr);
    }

    //Method to remove the mean causing the new mean value to be 0
    void removeMean(int[][][] data3D,int imgRows,int imgCols,int mean){
        for(int row = 0;row < imgRows;row++){
            for(int col = 0;col < imgCols;col++){
                data3D[row][col][1] -= mean;
                data3D[row][col][2] -= mean;
                data3D[row][col][3] -= mean;
            }
        }
    }

    //Method to scale the data and expand or compress the distribution
    void scale(int[][][] data3D,int imgRows,int imgCols,double scale){
        for(int row = 0;row < imgRows;row++){
            for(int col = 0;col < imgCols;col++){
                data3D[row][col][1] *= scale;
                data3D[row][col][2] *= scale;
                data3D[row][col][3] *= scale;
            }
        }
    }

    //Method to shift the mean to a new value.
    void shiftMean(int[][][] data3D,int imgRows,int imgCols,int newMean){
        for(int row = 0;row < imgRows;row++){
            for(int col = 0;col < imgCols;col++){
                data3D[row][col][1] += newMean;
                data3D[row][col][2] += newMean;
                data3D[row][col][3] += newMean;
            }
        }
    }

    //Method to clip the color data at 0 and 255
    void clip(int[][][] data3D,int imgRows,int imgCols){
        for(int row = 0;row < imgRows;row++){
            for(int col = 0;col < imgCols;col++){
                if(data3D[row][col][1] < 0){
                    data3D[row][col][1] = 0;
                }
                if(data3D[row][col][1] > 255){
                    data3D[row][col][1] = 255;
                }
                if(data3D[row][col][2] < 0){
                    data3D[row][col][2] = 0;
                }
                if(data3D[row][col][2] > 255){
                    data3D[row][col][2] = 255;
                }
                if(data3D[row][col][3] < 0){
                    data3D[row][col][3] = 0;
                }
                if(data3D[row][col][3] > 255){
                    data3D[row][col][3] = 255;
                }
            }
        }
    }
}
