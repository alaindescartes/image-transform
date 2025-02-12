
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.imageio.*;


// This class loads two images and allows grayscale and sepia colouring of the images
// Eventually more convolutions will be added
public class convolutionStart extends JComponent implements KeyListener
{
	public BufferedImage imageKings;
	public BufferedImage imageChristmas;
	public BufferedImage imageOutside;
	public BufferedImage image;

	// Construct the frame and make it exit when the x button is clicked
	// 
	public static void main(String[] args)
	{
		JFrame f = new JFrame ("CMPT 450 - Assignment ");

		// Make the window closed when the x button is clicked
		// This makes a new instance that overrides the windowClosing function 
		// Hey look - it's an anonymous inner class - you did learn something from CMPT 305 ?
		f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		convolutionStart convolution = new convolutionStart();
		
		f.add(convolution);  // put the panel with the image in the frame
		f.pack();			// layout the frame
		f.setVisible(true);	// show the frame
		f.addKeyListener(convolution); // make the window respond to keyboard button presses
	}

	// Constructor 
	// Load the images that the program will work with
	public convolutionStart()
	{
		try
		{
			// Load the image, plus keep a copy of each image so that image can
			// be easily reset
	                // Images are hard coded - should be fine for purposes of this assignment
			imageKings = ImageIO.read(new File("2011-01-26_13_31_24.jpg"));
			imageChristmas = ImageIO.read(new File("2011-01-26_14_18_03.jpg"));
			imageOutside = ImageIO.read(new File("IMG_2145.jpg"));
			image = ImageIO.read(new File("2011-01-26_13_31_24.jpg"));

			System.out.println("The image has been loaded and is of size "+image.getWidth(null)+" by "+image.getHeight(null));

			// Example code for playing with the first pixel
			//System.out.println("The first pixel is: "+image.getRGB(0,0));
			//System.out.println("Alpha = "+getAlpha(image.getRGB(0,0)));
			//System.out.println("Red = "+getRed(image.getRGB(0,0)));
			//System.out.println("Green = "+getGreen(image.getRGB(0,0)));
			//System.out.println("Blue = "+getBlue(image.getRGB(0,0)));

		}
		catch (Exception e) // Generic Exception handler with information on what happened
		{
			System.out.println("There was a problem loading the image\n"+e);
		}

	}

	// Grayscale the image using 0.3R+0.59G+0.11B.  
	// This follows that our eye is most sensative to green and least sensative to blue
	public void grayScale()
	{
		//System.out.println("Gray scaling the image");

		// For each row
		for(int j=0; j<image.getHeight(); j++)
		{
			// For each column
			for(int i=0; i<image.getWidth(); i++)
			{
				// using Y = 0.3*R + 0.59*G + 0.11*B to grayscale
				int gs = (int)(0.3*(double)getRed(image.getRGB(i,j)) + 0.59*(double)getGreen(image.getRGB(i,j)) + 0.11*(double)getBlue(image.getRGB(i,j)));
				image.setRGB(i, j, makeColour(gs, gs, gs) );
			}
		}
		repaint(); // request the image be redrawn
	}

	// Loop through the image and change the red, green, and blue component to sepia 
	// This assumes that the black value is (R,G,B) = (112,66,20) and white is (255, 255, 255)
	public void sepia()
	{
           //System.out.println("Applying Sepia");
		// For each row
		for(int j=0; j<image.getHeight(); j++)
		{
			// For each column
			for(int i=0; i<image.getWidth(); i++)
			{
	 	                // Linearly interpolate between the sepia colour and white
				double sepia = (0.33*(double)getRed(image.getRGB(i,j)) + 0.33*(double)getGreen(image.getRGB(i,j)) + 0.33*(double)getBlue(image.getRGB(i,j)))/255.0;
				image.setRGB(i, j, makeColour( (int)((1-sepia)*112.0+sepia*255.0),
												(int)((1-sepia)*66.0+sepia*255.0),
												(int)((1-sepia)*20.0+sepia*255.0)) );

			}
		}
		repaint(); // request the image be redrawn
	}

	// This function inverts the colors of an image by subtracting each RGB component from 255.
	public void inverse(){
		for(int j=0; j<image.getHeight(); j++)
		{
			// For each column
			for(int i=0; i<image.getWidth(); i++)
			{
				int red = getRed(image.getRGB(i,j));
				int green = getGreen(image.getRGB(i,j));
				int blue = getBlue(image.getRGB(i,j));
				image.setRGB(i, j, makeColour(255-red,255- green, 255-blue) );
				
			}
		}
		repaint();
	}
	/**
	 * Applies a threshold filter to convert the image into black and white.
	 * Each pixel is converted to grayscale and then compared to a threshold (127).
	 * Pixels above the threshold are set to white, while those below are set to black.
	 */
	public void threshold(){
		int midpoint = 127;
		for(int j=0; j<image.getHeight(); j++)
		{
			// For each column
			for(int i=0; i<image.getWidth(); i++)
			{
				int gs = (int)(0.3*(double)getRed(image.getRGB(i,j)) + 0.59*(double)getGreen(image.getRGB(i,j)) + 0.11*(double)getBlue(image.getRGB(i,j)));
				//threshold each pixel to all black or white
				if (gs>midpoint){
					gs = 255;
				}else{
					gs = 0;
				}

				image.setRGB(i, j, makeColour(gs, gs, gs) );
			}
		}
		repaint();
	}

	public void threshold_with_histogram() {
		int[] bin = new int[256]; // Histogram array
		int total_pixels = image.getWidth() * image.getHeight(); // Total pixels
		int threshold = 0; // Placeholder for the threshold


		for (int j = 0; j < image.getHeight(); j++) {
			for (int i = 0; i < image.getWidth(); i++) {
				int gs = (int) (0.3 * getRed(image.getRGB(i, j)) +
						0.59 * getGreen(image.getRGB(i, j)) +
						0.11 * getBlue(image.getRGB(i, j)));
				bin[gs]++;
			}
		}


		int midway_count = total_pixels / 2; // Halfway point
		int sum = 0;
		for (int i = 0; i < 256; i++) {
			sum += bin[i];
			if (sum >= midway_count) {
				threshold = i;
				break;
			}
		}


		for (int j = 0; j < image.getHeight(); j++) {
			for (int i = 0; i < image.getWidth(); i++) {
				int gs = (int) (0.3 * getRed(image.getRGB(i, j)) +
						0.59 * getGreen(image.getRGB(i, j)) +
						0.11 * getBlue(image.getRGB(i, j)));

				gs = (gs > threshold) ? 255 : 0;
				image.setRGB(i, j, makeColour(gs, gs, gs));
			}
		}

		repaint();
	}

	public void draw_histogram(char colorChannel) {
		int[] bin = new int[256];

		for (int j = 0; j < image.getHeight(); j++) {
			for (int i = 0; i < image.getWidth(); i++) {
				int rgb = image.getRGB(i, j);
				int value = 0;
				if (colorChannel == 'h') value = getRed(rgb);
				else if (colorChannel == 'H') value = getGreen(rgb);
				else if (colorChannel == 'j') value = getBlue(rgb);

				bin[value]++;
			}
		}

		Graphics g = getGraphics();
		int panelWidth = image.getWidth();
		int panelHeight = image.getHeight();
		int barWidth = panelWidth / 256;

		int maxValue = 0;
		for (int value : bin) {
			if (value > maxValue) {
				maxValue = value;
			}
		}

		Color barColor = Color.BLACK;
		if (colorChannel == 'h') barColor = Color.RED;
		else if (colorChannel == 'H') barColor = Color.GREEN;
		else if (colorChannel == 'j') barColor = Color.BLUE;

		g.setColor(barColor);
		for (int i = 0; i < bin.length; i++) {
			int barHeight = (int) (((double) bin[i] / maxValue) * (panelHeight - 40));
			g.fillRect(i * barWidth, panelHeight - barHeight - 20, barWidth, barHeight);
		}

		g.setColor(Color.BLACK);
		g.drawLine(0, panelHeight - 20, panelWidth, panelHeight - 20);
		g.drawLine(0, 0, 0, panelHeight - 20);
		g.drawString("Intensity", panelWidth / 2 - 30, panelHeight - 5);
		g.drawString("Frequency", 5, 15);
	}

	private int getGrayScale(int x, int y) {
		int rgb = image.getRGB(x, y);
		return (int)(0.3 * getRed(rgb) + 0.59 * getGreen(rgb) + 0.11 * getBlue(rgb));
	}
	@SuppressWarnings("DuplicatedCode")
    public void error_diffusion(){
		int midpoint = 128;
		// Floyd-Steinberg Error Diffusion Fractions
		final float RIGHT_NEIGHBOR = 7.0f / 16.0f;
		final float BOTTOM_LEFT_NEIGHBOR = 3.0f / 16.0f;
		final float BOTTOM_NEIGHBOR = 5.0f / 16.0f;
		final float BOTTOM_RIGHT_NEIGHBOR = 1.0f / 16.0f;
		int newValue = 0;


		for(int j=0; j<image.getHeight(); j++)
		{
			// For each column
			for(int i=0; i<image.getWidth(); i++)
			{
				int gs = (int)(0.3*(double)getRed(image.getRGB(i,j)) + 0.59*(double)getGreen(image.getRGB(i,j)) + 0.11*(double)getBlue(image.getRGB(i,j)));
				newValue = (gs > midpoint) ? 255 : 0;
				int error = gs - newValue;
				image.setRGB(i, j, makeColour(newValue, newValue, newValue) );
				// Distribute the error to neighboring pixels
				if (i + 1 < image.getWidth()) { // Right neighbor
					int neighbor = (int)(getGrayScale(i + 1, j) + error * RIGHT_NEIGHBOR);
					image.setRGB(i + 1, j, makeColour(neighbor, neighbor, neighbor));
				}

				if (i - 1 >= 0 && j + 1 < image.getHeight()) { // Bottom-left neighbor
					int neighbor = (int)(getGrayScale(i - 1, j + 1) + error * BOTTOM_LEFT_NEIGHBOR);
					image.setRGB(i - 1, j + 1, makeColour(neighbor, neighbor, neighbor));
				}

				if (j + 1 < image.getHeight()) { // Bottom neighbor
					int neighbor = (int)(getGrayScale(i, j + 1) + error * BOTTOM_NEIGHBOR);
					image.setRGB(i, j + 1, makeColour(neighbor, neighbor, neighbor));
				}

				if (i + 1 < image.getWidth() && j + 1 < image.getHeight()) { // Bottom-right neighbor
					int neighbor = (int)(getGrayScale(i + 1, j + 1) + error * BOTTOM_RIGHT_NEIGHBOR);
					image.setRGB(i + 1, j + 1, makeColour(neighbor, neighbor, neighbor));
				}
			}
		}
		repaint();
	}

	public void erosion(int minSimilarNeighbors) {
		BufferedImage tempImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int j = 0; j < image.getHeight(); j++) {
			for (int i = 0; i < image.getWidth(); i++) {
				int similarNeighborCount = 0;
				int currentPixel = image.getRGB(i, j);

				for (int ki = -1; ki <= 1; ki++) {
					for (int kj = -1; kj <= 1; kj++) {
						if (ki == 0 && kj == 0) continue;

						int ni = i + ki; // Neighbor column
						int nj = j + kj; // Neighbor row

						// Check if the neighbor is within image bounds
						if (ni >= 0 && ni < image.getWidth() && nj >= 0 && nj < image.getHeight()) {
							int neighborPixel = image.getRGB(ni, nj);

							if (colorSimilarity(currentPixel, neighborPixel) < 30) {
								similarNeighborCount++;
							}
						}
					}
				}

				if (similarNeighborCount >= minSimilarNeighbors) {
					tempImage.setRGB(i, j, currentPixel);
				} else {
					tempImage.setRGB(i, j, makeColour(255, 255, 255));
				}
			}
		}

		image = tempImage;
		repaint();
	}

	// Helper method to calculate color similarity
	private int colorSimilarity(int color1, int color2) {
		int r1 = (color1 >> 16) & 0xFF;
		int g1 = (color1 >> 8) & 0xFF;
		int b1 = color1 & 0xFF;

		int r2 = (color2 >> 16) & 0xFF;
		int g2 = (color2 >> 8) & 0xFF;
		int b2 = color2 & 0xFF;

		return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
	}
	public void dilation(int minSimilarNeighbors) {
		BufferedImage tempImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int j = 0; j < image.getHeight(); j++) {
			for (int i = 0; i < image.getWidth(); i++) {
				int similarNeighborCount = 0;
				int currentPixel = image.getRGB(i, j);
				int maxSimilarPixel = currentPixel;

				// Loop through the 3×3 kernel centered at (i, j)
				for (int ki = -1; ki <= 1; ki++) {
					for (int kj = -1; kj <= 1; kj++) {
						if (ki == 0 && kj == 0) continue; // Skip the center pixel itself

						int ni = i + ki; // Neighbor column
						int nj = j + kj; // Neighbor row

						// Check if the neighbor is within image bounds
						if (ni >= 0 && ni < image.getWidth() && nj >= 0 && nj < image.getHeight()) {
							int neighborPixel = image.getRGB(ni, nj);

							// Count neighbors with similar color (tolerance threshold can be adjusted)
							if (colorSimilarity(currentPixel, neighborPixel) < 30) { // 30 is the tolerance threshold
								similarNeighborCount++;
								if (colorSimilarity(maxSimilarPixel, neighborPixel) > colorSimilarity(maxSimilarPixel, currentPixel)) {
									maxSimilarPixel = neighborPixel;
								}
							}
						}
					}
				}

				// Apply dilation based on the number of similar neighbors
				if (similarNeighborCount >= minSimilarNeighbors) {
					tempImage.setRGB(i, j, maxSimilarPixel); // Expand to the most similar neighbor
				} else {
					tempImage.setRGB(i, j, currentPixel); // Keep original color
				}
			}
		}

		image = tempImage; // Update the original image with the dilated version
		repaint();
	}

	//this function brightens an image by using gamma correction
	public void brightenImage(double gamma){
		final double brightGamma = 0.5;
		final double darkGamma = 1.6;
		for(int j=0; j<image.getHeight(); j++)
		{
			// For each column
			for(int i=0; i<image.getWidth(); i++)
			{
				int red = (int)(Math.pow(getRed(image.getRGB(i, j)) / 255.0,gamma) * 255);
				int green = (int)(Math.pow(getGreen(image.getRGB(i, j)) / 255.0,gamma) * 255);
				int blue = (int)(Math.pow(getBlue(image.getRGB(i, j)) / 255.0,gamma) * 255);
				image.setRGB(i, j, makeColour(red,green, blue) );
			}
		}
		repaint();
	}

	// This function applies a box filter (mean filter) to an image, producing a smoothing effect.
	// The filter can be a 3x3 for light smoothing or a 7x7 for stronger smoothing.
	// Currently, it applies the 7x7 filter. Uncomment the 3x3 filter line to switch.
	public void smoothImage() {
		int[][] filter1 = {
				{1, 1, 1},
				{1, 1, 1},
				{1, 1, 1}
		};
		int[][] filter2 = {
				{1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1}
		};

		applyKernel(3, filter2);
		//applyKernel(1, filter1);
	}


	// This method applies a Gaussian blur to the image, providing a smoothing effect.
	// It uses either a 3x3 Gaussian filter for light blurring or a 7x7 Gaussian filter for stronger blurring.
	// Currently, it applies the 7x7 filter. Uncomment the 3x3 filter line to switch.
	public void blurImage() {
		int[][] gaussian3x3 = {
				{1, 2, 1},
				{2, 4, 2},
				{1, 2, 1}
		};

		int[][] gaussian7x7 = {
				{1, 1, 2, 2, 2, 1, 1},
				{1, 2, 2, 4, 2, 2, 1},
				{2, 2, 4, 8, 4, 2, 2},
				{2, 4, 8, 16, 8, 4, 2},
				{2, 2, 4, 8, 4, 2, 2},
				{1, 2, 2, 4, 2, 2, 1},
				{1, 1, 2, 2, 2, 1, 1}
		};

		//applyKernel(1, gaussian3x3);
		applyKernel(3, gaussian7x7);
	}


	// This method applies edge detection to an image using either a 3x3 Sobel kernel or a 7x7 extended Sobel kernel.
	// It calculates gradients in the x (horizontal) and y (vertical) directions and combines them to find the edge magnitude.
	public void detectEdges() {
		int[][] filterx = {
				{ -1,  0, +1 },
				{ -2,  0, +2 },
				{ -1,  0, +1 }
		};
		int[][] filterY = {
				{ -1, -2, -1 },
				{  0,  0,  0 },
				{ +1, +2, +1 }
		};

//		int[][] filterx = {
//				{ -1,   -4,   -5,    0,   +5,   +4,   +1 },
//				{ -6,  -24,  -30,    0,  +30,  +24,   +6 },
//				{ -15, -60,  -75,    0,  +75,  +60,  +15 },
//				{ -20, -80, -100,    0, +100,  +80,  +20 },
//				{ -15, -60,  -75,    0,  +75,  +60,  +15 },
//				{ -6,  -24,  -30,    0,  +30,  +24,   +6 },
//				{ -1,   -4,   -5,    0,   +5,   +4,   +1 }
//		};
//
//		int[][] filterY = {
//				{ -1,   -6,   -15,  -20,  -15,   -6,   -1 },
//				{ -4,  -24,   -60,  -80,  -60,  -24,   -4 },
//				{ -5,  -30,   -75, -100,  -75,  -30,   -5 },
//				{  0,    0,     0,    0,    0,    0,    0 },
//				{ +5,  +30,   +75, +100,  +75,  +30,   +5 },
//				{ +4,  +24,   +60,  +80,  +60,  +24,   +4 },
//				{ +1,   +6,   +15,  +20,  +15,   +6,   +1 }
//		};



		int width = image.getWidth();
		int height = image.getHeight();
		int[][] temp = new int[height][width];
		int offset = 1;  // for a 3x3 kernel
		//int offset = 3;  // for a 7x7 kernel

		for (int y = offset; y < height - offset; y++) {
			for (int x = offset; x < width - offset; x++) {

				int gX = 0, gY = 0;
				for (int dy = -offset; dy <= offset; dy++) {
					for (int dx = -offset; dx <= offset; dx++) {
						int neighborPixel = getGrayScale(x + dx, y + dy);

						gX += neighborPixel * filterx[dy + offset][dx + offset];
						gY += neighborPixel * filterY[dy + offset][dx + offset];
					}
				}

				int magnitude = (int) Math.sqrt(gX * gX + gY * gY);
				magnitude = Math.min(255, Math.max(0, magnitude));
				temp[y][x] = (magnitude > 80) ? 0xFFFFFFFF : 0xFF000000;
			}
		}
		for (int y = offset; y < height - offset; y++) {
			for (int x = offset; x < width - offset; x++) {
				image.setRGB(x, y, temp[y][x]);
			}
		}
		repaint();
	}

	// This method applies a 3x3 median filter to the image to reduce noise.
	//It works by replacing each pixel's intensity with the median intensity of its 3x3 neighborhood.
	public void applyMedianFilter() {
		int offset = 1;
		int width = image.getWidth();
		int height = image.getHeight();
		for (int y = offset; y < height - offset; y++) {
			for (int x = offset; x < width - offset; x++) {

				int [][] temp = new int[3][3];
				for (int dy = -offset; dy <= offset; dy++) {
					for (int dx = -offset; dx <= offset; dx++) {
						temp[dy + offset][dx + offset] = getGrayScale(x + dx, y + dy);

					}
				}
				int[] flattenedArray = Arrays.stream(temp)
						.flatMapToInt(Arrays::stream)
						.sorted()
						.toArray();
				int median = flattenedArray[4];
				image.setRGB(x, y, (median << 16) | (median << 8) | median);
			}
		}
		repaint();
	}


	// This method sharpens the image by applying a Gaussian blur to create a blurred version,
	// then subtracting a scaled version of the blurred image from the original image.
	public void sharpenImage() {
		int offset = 1;
		BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		copyImage(image, temp);
//		int[][] gaussian = {
//				{1, 2, 1},
//				{2, 4, 2},
//				{1, 2, 1}
//		};

		int[][] gaussian = {
				{1, 1, 2, 2, 2, 1, 1},
				{1, 2, 2, 4, 2, 2, 1},
				{2, 2, 4, 8, 4, 2, 2},
				{2, 4, 8, 16, 8, 4, 2},
				{2, 2, 4, 8, 4, 2, 2},
				{1, 2, 2, 4, 2, 2, 1},
				{1, 1, 2, 2, 2, 1, 1}
		};

		var blurredImage = applyKernel(offset, gaussian, temp);
		for(int j=0; j<image.getHeight(); j++)
		{
			for(int i=0; i<image.getWidth(); i++)
			{
				int originalPixel = image.getRGB(i, j);
				int blurredPixel = blurredImage.getRGB(i, j);

				int newRed = (int) (1.6 * getRed(originalPixel) - 0.6 * getRed(blurredPixel));
				int newGreen = (int) (1.6 * getGreen(originalPixel) - 0.6 * getGreen(blurredPixel));
				int newBlue = (int) (1.6 * getBlue(originalPixel) - 0.6 * getBlue(blurredPixel));

				// Clamp values to [0, 255] to prevent overflow or underflow
				newRed = Math.min(255, Math.max(0, newRed));
				newGreen = Math.min(255, Math.max(0, newGreen));
				newBlue = Math.min(255, Math.max(0, newBlue));
				int newPixel = (newRed << 16) | (newGreen << 8) | newBlue;
				image.setRGB(i, j, newPixel);

			}
		}
		repaint();
	}


	// This method applies a custom kernel to an image, allowing for effects like smoothing, blurring, sharpening, etc.
	// The kernel size and weights are determined by the passed 2D filter array.
	//the offset defines how far the kernel extends form the center pixel
	public void applyKernel(int offset, int[][] filter) {
		int totalWeight = 0;

        for (int[] ints : filter) {
            for (int y = 0; y < filter.length; y++) {
                totalWeight += ints[y];
            }
        }
		if (totalWeight == 0) totalWeight = 1;
		for(int j=offset; j<image.getHeight() - offset; j++)
		{
			for(int i=offset; i<image.getWidth() - offset; i++)
			{
				int sumRed = 0, sumGreen = 0, sumBlue = 0;

				for (int col = -offset; col <= offset; col++) {
					for (int row = -offset; row <= offset; row++) {
						int neighborPixel = image.getRGB(i + row, j + col);
						int redColor = (neighborPixel >> 16) & 0xFF;
						int greenColor = (neighborPixel >> 8) & 0xFF;
						int blueColor = neighborPixel & 0xFF;

						int weight = filter[row + offset][col + offset];
						sumRed += redColor * weight;
						sumGreen += greenColor * weight;
						sumBlue += blueColor * weight;

					}
				}

				int avgRed, avgGreen, avgBlue;


				     avgRed = Math.min(255, Math.max(0, sumRed / totalWeight));
					 avgGreen = Math.min(255, Math.max(0, sumGreen / totalWeight));
					 avgBlue = Math.min(255, Math.max(0, sumBlue / totalWeight));



				int newPixel = (avgRed << 16) | (avgGreen << 8) | avgBlue;
				image.setRGB(i, j, newPixel);
			}
		}
		repaint();
	}
	// This method applies a custom kernel to an image, allowing for effects like smoothing, blurring, sharpening, etc.
	// The kernel size and weights are determined by the passed 2D filter array.
	//the offset defines how far the kernel extends form the center pixel
	public BufferedImage applyKernel(int offset, int[][] filter, BufferedImage imageCopy) {
		int totalWeight = 0;

		for (int[] ints : filter) {
			for (int y = 0; y < filter.length; y++) {
				totalWeight += ints[y];
			}
		}
		if (totalWeight == 0) totalWeight = 1;
		for(int j=offset; j<imageCopy.getHeight() - offset; j++)
		{
			for(int i=offset; i<imageCopy.getWidth() - offset; i++)
			{
				int sumRed = 0, sumGreen = 0, sumBlue = 0;

				for (int col = -offset; col <= offset; col++) {
					for (int row = -offset; row <= offset; row++) {
						int neighborPixel = image.getRGB(i + row, j + col);
						int redColor = (neighborPixel >> 16) & 0xFF;
						int greenColor = (neighborPixel >> 8) & 0xFF;
						int blueColor = neighborPixel & 0xFF;

						int weight = filter[row + offset][col + offset];
						sumRed += redColor * weight;
						sumGreen += greenColor * weight;
						sumBlue += blueColor * weight;
					}
				}

				int avgRed, avgGreen, avgBlue;

				avgRed = Math.min(255, Math.max(0, sumRed / totalWeight));
				avgGreen = Math.min(255, Math.max(0, sumGreen / totalWeight));
				avgBlue = Math.min(255, Math.max(0, sumBlue / totalWeight));

				int newPixel = (avgRed << 16) | (avgGreen << 8) | avgBlue;
				imageCopy.setRGB(i, j, newPixel);
			}
		}
		repaint();
		return imageCopy;
	}


	// These function definitions must be included to satisfy the KeyListener interface 
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	// Respond to key pressed
	public void keyTyped(KeyEvent e) 
	{
		if(e.getKeyChar() == KeyEvent.VK_ESCAPE)  System.exit(0); 	// exit when escape is pressed
		else if(e.getKeyChar() == 'g') grayScale();
		else if(e.getKeyChar() == 'G') sepia();
		else if(e.getKeyChar() == 'k') copyImage(imageKings, image); // reload the building picture
		else if(e.getKeyChar() == 'c') copyImage(imageChristmas, image); // reload the class picture
		else if(e.getKeyChar() == 'o') copyImage(imageOutside, image); // reload the class picture
		else if (e.getKeyChar() == 'i') inverse();
		else if(e.getKeyChar() == 't') threshold();
		else if(e.getKeyChar() == 'T') threshold_with_histogram();
		else if (e.getKeyChar() == 'h') draw_histogram('h');
		else if (e.getKeyChar() == 'H') draw_histogram('H');
		else if (e.getKeyChar() == 'j') draw_histogram('j');
		else if (e.getKeyChar() == 'I') error_diffusion();
		else if(e.getKeyChar() >= '0' && e.getKeyChar() <= '8') erosion(Character.getNumericValue(e.getKeyChar()));
		else if (e.getKeyChar() >= ')' && e.getKeyChar() <= '*') dilation(e.getKeyChar() - ')');
		else if (e.getKeyChar() == 'b') brightenImage(0.5);
		else if (e.getKeyChar() == 'd') brightenImage(1.6);
		else if (e.getKeyChar() == 'S' ||e.getKeyChar() == 's') smoothImage();
		else if (e.getKeyChar() == 'F' ||e.getKeyChar() == 'f') blurImage();
		else if (e.getKeyChar() == 'E' ||e.getKeyChar() == 'e') detectEdges();
		else if (e.getKeyChar() == 'Z' ||e.getKeyChar() == 'z') sharpenImage();
		else if (e.getKeyChar() == 'm' ) applyMedianFilter();

	}

	// Return the size this component should be - usually the size of the image,
	// or 100 x 100 if the image hasn't been loaded for some reason
	public Dimension getPreferredSize()
	{
		if(image == null) return new Dimension(100,100);
		else return new Dimension(image.getWidth(null), image.getHeight(null));
	}

	// When redrawing just paint the image on the component
	public void paint(Graphics g) { g.drawImage(image, 0, 0, null); }

	// This function copies each pixel from the source image to the destination image
	// This function assumes that the images are the same size
	public void copyImage(BufferedImage src, BufferedImage dst)
	{
		for(int j=0; j<src.getHeight(); j++)
		  for(int i=0; i<src.getWidth(); i++)
		    dst.setRGB(i, j, src.getRGB(i,j));

		repaint(); // request the image be redrawn
	}

	// Some functions for getting the alpha, red, green, or blue values from an integer that
	// represents a colour
	public static int getAlpha(int pixelColour) { return (0xFF000000 & pixelColour)>>>24;}
	public static int getRed(int pixelColour) { return   (0x00FF0000 & pixelColour)>>>16;}
	public static int getGreen(int pixelColour) { return (0x0000FF00 & pixelColour)>>>8;}
	public static int getBlue(int pixelColour) { return  (0x000000FF & pixelColour);}

	// Given the red, green, and blue values make the colour as an integer assuming pixel is opaque
	public static int makeColour(int red, int green, int blue) {return (255<<24 | red<<16 | green << 8 | blue);}
	
}