import java.awt.*;
import java.awt.image.BufferedImage;

public class Pixelator {

  public static final Color red = new Color(255, 0, 0);
  public static final Color green = new Color(0, 255, 0);
  public static final Color blue = new Color(0, 0, 255);
  public static final Color orange = new Color(255, 165, 0);
  public static final Color yellow = new Color(255, 255, 0);
  public static final Color white = new Color(255, 255, 255);
  public static final Color[] colorArray = {red, green, blue, orange, yellow, white};
  public static Color[] pickedColorArray;
  public static boolean finished = false;

  public static BufferedImage[] pixelate(GUI gui, BufferedImage inputImage, int width, int height) {
    Image scaledImage = inputImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    Image displayImage = inputImage.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
    BufferedImage bufferedInputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    BufferedImage bufferedDisplayImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
    BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    BufferedImage instructionImage = new BufferedImage(width + (width / 3) - 1,
        height + (height / 3) - 1, BufferedImage.TYPE_INT_RGB);

    Graphics2D g2d = outputImage.createGraphics();
    g2d.drawImage(scaledImage, 0, 0, null);
    g2d = bufferedInputImage.createGraphics();
    g2d.drawImage(scaledImage, 0, 0, null);
    g2d = instructionImage.createGraphics();
    g2d.setColor(Color.BLACK);
    g2d.drawRect(0, 0, instructionImage.getWidth(), instructionImage.getHeight());
    g2d = bufferedDisplayImage.createGraphics();
    g2d.drawImage(displayImage, 0, 0, null);
    g2d.dispose();

    while (!finished) {
      pickedColorArray = gui.getPickedColorArray(bufferedDisplayImage);
      buildImages(bufferedInputImage, outputImage, instructionImage, gui, width, height);
    }

    gui.close();
    return new BufferedImage[]{outputImage, instructionImage};
  }

  private static void buildImages(BufferedImage bufferedInputImage, BufferedImage outputImage,
                                  BufferedImage instructionImage, GUI gui, int width, int height) {
    int instructionOffsetX;
    int instructionOffsetY = 0;
    for (int y = 0; y < height; y++) {
      instructionOffsetX = 0;
      if (y != 0 && y % 3 == 0) {
        instructionOffsetY++;
      }
      for (int x = 0; x < width; x++) {
        int closestColor = getClosestColor(bufferedInputImage.getRGB(x, y)).getRGB();
        if (x != 0 && x % 3 == 0) {
          instructionOffsetX++;
        }
        outputImage.setRGB(x, y, closestColor);
        instructionImage.setRGB(x + instructionOffsetX, y + instructionOffsetY, closestColor);
      }
    }
    Image outputDisplayImage = outputImage.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
    gui.drawPixeledImage(outputDisplayImage);
  }

  private static Color getClosestColor(int colorInt) {
    Color imageColor = new Color(colorInt);
    int r = imageColor.getRed();
    int g = imageColor.getGreen();
    int b = imageColor.getBlue();

    double closestDistance = 1000000;
    Color closestColor = null;

    for (int i = 0; i < 6; i++) {
      Color selectedColor = pickedColorArray[i];
      if (selectedColor == GUI.IGNORE_COLOR) {
        continue;
      }
      int r2 = selectedColor.getRed();
      int g2 = selectedColor.getGreen();
      int b2 = selectedColor.getBlue();

      double tempDistance = Math.sqrt(Math.pow((r-r2)*0.3,2) +
          Math.pow((g-g2)*0.59, 2) +
          Math.pow((b-b2)*0.11, 2));
      if (tempDistance < closestDistance) {
        closestDistance = tempDistance;
        closestColor = colorArray[i];
      }
    }

    return closestColor;
  }

}
