
package org.tensorflow.demo.env;

import android.graphics.Bitmap;
import android.text.TextUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Size implements Comparable<Size>, Serializable {

  // 1.4 went out with this UID so we'll need to maintain it to preserve pending queries when
  // upgrading.
  public static final long serialVersionUID = 7689808733290872361L;

  public final int width;
  public final int height;

  public Size(final int width, final int height) {
    this.width = width;
    this.height = height;
  }

  public Size(final Bitmap bmp) {
    this.width = bmp.getWidth();
    this.height = bmp.getHeight();
  }


  public static Size getRotatedSize(final Size size, final int rotation) {
    if (rotation % 180 != 0) {
     
      return new Size(size.height, size.width);
    }
    return size;
  }

  public static Size parseFromString(String sizeString) {
    if (TextUtils.isEmpty(sizeString)) {
      return null;
    }

    sizeString = sizeString.trim();

   
    final String[] components = sizeString.split("x");
    if (components.length == 2) {
      try {
        final int width = Integer.parseInt(components[0]);
        final int height = Integer.parseInt(components[1]);
        return new Size(width, height);
      } catch (final NumberFormatException e) {
        return null;
      }
    } else {
      return null;
    }
  }

  public static List<Size> sizeStringToList(final String sizes) {
    final List<Size> sizeList = new ArrayList<Size>();
    if (sizes != null) {
      final String[] pairs = sizes.split(",");
      for (final String pair : pairs) {
        final Size size = Size.parseFromString(pair);
        if (size != null) {
          sizeList.add(size);
        }
      }
    }
    return sizeList;
  }

  public static String sizeListToString(final List<Size> sizes) {
    String sizesString = "";
    if (sizes != null && sizes.size() > 0) {
      sizesString = sizes.get(0).toString();
      for (int i = 1; i < sizes.size(); i++) {
        sizesString += "," + sizes.get(i).toString();
      }
    }
    return sizesString;
  }

  public final float aspectRatio() {
    return (float) width / (float) height;
  }

  @Override
  public int compareTo(final Size other) {
    return width * height - other.width * other.height;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null) {
      return false;
    }

    if (!(other instanceof Size)) {
      return false;
    }

    final Size otherSize = (Size) other;
    return (width == otherSize.width && height == otherSize.height);
  }

  @Override
  public int hashCode() {
    return width * 32713 + height;
  }

  @Override
  public String toString() {
    return dimensionsAsString(width, height);
  }

  public static final String dimensionsAsString(final int width, final int height) {
    return width + "x" + height;
  }
}
