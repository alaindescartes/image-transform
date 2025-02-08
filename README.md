

# Image Processing Project  

This project is a starting point for experimenting with image processing in Java. It provides functionality for loading images and applying various transformations and filters, such as grayscale conversion, sepia toning, thresholding, and morphological operations. The interface is built using `Swing` and allows interaction through keyboard inputs.

---

## Features  

### Filters and Effects  
- **Grayscale**: Converts the image to grayscale using the formula `0.3*R + 0.59*G + 0.11*B`.  
- **Sepia**: Applies a sepia tone effect to the image.  
- **Inverse Colors**: Inverts the RGB components of each pixel.  
- **Thresholding**: Converts the image to black and white based on a threshold value.  
- **Adaptive Thresholding with Histogram**: Automatically determines the threshold using the image's histogram.

### Morphological Operations  
- **Erosion**: Removes noise by shrinking object boundaries based on neighboring pixels.  
- **Dilation**: Expands object boundaries to fill gaps in an image.  

### Histogram Visualization  
- Visualize the intensity distribution of the red, green, or blue channels in the image.

### Brightness Adjustment  
- Adjust the image brightness using gamma correction.

### Error Diffusion  
- Applies Floyd-Steinberg error diffusion for dithering effects.

---

## Installation and Usage  

### Prerequisites  
- Java 8 or higher  
- Java Swing library (built-in with Java)  

### Running the Project  
1. Clone the repository:  
   ```bash
   git clone <repository-url>
   ```  
2. Compile and run the `convolutionStart.java` file:  
   ```bash
   javac convolutionStart.java  
   java convolutionStart  
   ```  

### Keyboard Controls  
- **`g`**: Apply grayscale filter  
- **`G`**: Apply sepia filter  
- **`i`**: Invert colors  
- **`t`**: Apply basic thresholding  
- **`T`**: Apply adaptive thresholding using the histogram  
- **`h`, `H`, `j`**: Show histogram for red, green, or blue channels  
- **`I`**: Apply error diffusion  
- **`b`**: Brighten the image (gamma correction)  
- **`d`**: Darken the image (gamma correction)  
- **`0-8`**: Perform erosion with different levels of neighbor similarity  
- **`) - *`**: Perform dilation with different levels of neighbor similarity  
- **`ESC`**: Exit the application  

---

## Future Enhancements  
- Adding more convolution filters (e.g., edge detection, blur, sharpen).  
- Real-time performance improvements.  
- Save and export transformed images.  
