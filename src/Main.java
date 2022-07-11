import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

  //Width and height must be divisible by 3
  public static final int width = 60;
  public static final int height = 60;

  public static void main(String[] args) {
    GUI gui = new GUI();
    JFileChooser fc = new JFileChooser();
    fc.showOpenDialog(gui.getFrame());
    File inputFile = fc.getSelectedFile();
    String inputFilePath = null;
    try {
      inputFilePath = "input_images/" + inputFile.getName();
    } catch (NullPointerException e) {
      System.exit(1);
    }

    BufferedImage inputImage = null;
    boolean successfulOutput = true;
    try {
      inputImage = ImageIO.read(inputFile);
    } catch (IOException e) {
      successfulOutput = false;
    }

    BufferedImage[] bufferedImages = Pixelator.pixelate(gui, inputImage, width, height);
    BufferedImage outputImage = bufferedImages[0];
    BufferedImage instructionImage = bufferedImages[1];
    String outputFileName = getTruncatedFileName(inputFilePath) + "_" +
        width + "x" + height + ".png";
    String instructionFileName = getTruncatedFileName(inputFilePath) + "_instructions_" +
        width + "x" + height + ".png";
    try {
      File outputFile2 = new File("output_images/" + outputFileName);
      ImageIO.write(outputImage, "png", outputFile2);
      File instructionFile2 = new File("output_images/" + instructionFileName);
      ImageIO.write(instructionImage, "png", instructionFile2);
    } catch (IOException e) {
      successfulOutput = false;
    }

    if (successfulOutput) {
      System.out.println("File Output of " + outputFileName + " and " + instructionFileName + " Successful");
    }
  }

  public static String getTruncatedFileName(String filePath) {
    int startIndex = filePath.lastIndexOf('/') + 1;
    int endIndex = filePath.lastIndexOf('.');

    return filePath.substring(startIndex, endIndex);
  }

}
