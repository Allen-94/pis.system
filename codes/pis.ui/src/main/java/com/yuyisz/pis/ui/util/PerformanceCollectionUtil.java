package com.yuyisz.pis.ui.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yuyisz.pis.ui.module.performance.CollectionInfo;
import com.yuyisz.pis.ui.module.security.DevResources;
 
public class PerformanceCollectionUtil {
 
  private static final int DRIVE_TYPE_HARD = 3;
 
  private static final String CHARSET = "GBK";
 
  private static final String CAP_LOAD_PERCENTAGE = "LoadPercentage";
 
  private static final String CAP_CAPACITY = "Capacity";
  private static final String CAP_CAPTION = "Caption";
  private static final String CAP_DRIVE_LETTER = "DriveLetter";
  private static final String CAP_DRIVE_TYPE = "DriveType";
  private static final String CAP_FREE_SPACE = "FreeSpace";
 
  private static final List<String> CAPS_VOLUME = Arrays.asList(CAP_CAPACITY,
      CAP_CAPTION, CAP_DRIVE_LETTER, CAP_DRIVE_TYPE, CAP_FREE_SPACE);
 
  private static final String CMD_CPU = "wmic cpu get " + CAP_LOAD_PERCENTAGE;
  private static final String CMD_VOLUME = "wmic volume get " + CAP_CAPACITY
      + "," + CAP_CAPTION + "," + CAP_DRIVE_LETTER + "," + CAP_DRIVE_TYPE + ","
      + CAP_FREE_SPACE;
 
  public static void main(String[] args) throws IOException {
    printDiskUsages(getDiskUsages());
    printCpuUsage(getCpuLoadPercentage());
  }
 
  private static void printDiskUsages(List<DiskUsage> diskUsages) {
    for (DiskUsage diskUsage : diskUsages) {
      System.out.printf("%s占用率：%.2f%%\n", diskUsage.caption, diskUsage.usage);
    }
  }
 
  private static void printCpuUsage(double cpuLoadPercentage) {
    System.out.printf("CPU占用率：%.2f%%\n", cpuLoadPercentage);
  }
 
  /**
   * 取得 CPU 占用率。
   * 
   * @return
   * @throws IOException
   */
  public static double getCpuLoadPercentage() throws IOException {
    Process process = Runtime.getRuntime().exec(CMD_CPU);
    InputStream is = process.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(is, CHARSET));
    br.readLine(); // 舍弃标题行
    br.readLine(); // 舍弃标题行下空行
    String percentageLine = br.readLine();
    if (percentageLine == null) {
      return 0;
    }
    return Double.parseDouble(percentageLine.trim());
  }
 
  /**
   * 取得硬盘占用率。
   * 
   * @return
   * @throws IOException
   */
  private static List<DiskUsage> getDiskUsages() throws IOException {
    Process process = Runtime.getRuntime().exec(CMD_VOLUME);
    InputStream is = process.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(is, CHARSET));
    String captionLine = br.readLine(); // 舍弃标题行
    br.readLine(); // 舍弃标题行下空行
    Map<String, Integer> captionToIndex = parseVolumeCaptionLine(captionLine);
    String line;
    List<DiskUsage> result = new ArrayList<DiskUsage>();
    while ((line = br.readLine()) != null) {
      if (line.length() == 0) {
        // 舍弃空行
        continue;
      }
      DiskUsage diskUsage = parseVolumeLine(line, captionToIndex);
      if (diskUsage != null) {
        result.add(diskUsage);
      }
    }
    Collections.sort(result, new DiskUsageComparator());
    return result;
  }
 
  private static Map<String, Integer> parseVolumeCaptionLine(String captionLine) {
    Map<String, Integer> captionToIndex = new HashMap<String, Integer>();
    for (String caption : CAPS_VOLUME) {
      captionToIndex.put(caption, captionLine.indexOf(caption));
    }
    return captionToIndex;
  }
 
  private static DiskUsage parseVolumeLine(String line,
      Map<String, Integer> captionToIndex) {
    int driveType = parseVolumeDriveType(line, captionToIndex);
    if (driveType != DRIVE_TYPE_HARD) {
      return null;
    }
    String driveLetter = parseVolumeDriveLetter(line, captionToIndex);
    if (!isValidDriveLetter(driveLetter)) {
      return null;
    }
 
    String caption = parseVolumeCaption(line, captionToIndex);
    long capacity = parseVolumeCapacity(line, captionToIndex);
    long freeSpace = parseVolumeFreeSpace(line, captionToIndex);
    return new DiskUsage(caption,
        ((capacity - freeSpace) / (double) capacity) * 100);
  }
 
  private static boolean isValidDriveLetter(String driveLetter) {
    if (driveLetter.length() != 2) {
      return false;
    }
    return Character.isUpperCase(driveLetter.charAt(0));
  }
 
  private static int parseVolumeDriveType(String line,
      Map<String, Integer> captionToIndex) {
    String str = line.substring(captionToIndex.get(CAP_DRIVE_TYPE),
        captionToIndex.get(CAP_FREE_SPACE));
    return Integer.parseInt(str.trim());
  }
 
  private static String parseVolumeDriveLetter(String line,
      Map<String, Integer> captionToIndex) {
    String str = line.substring(captionToIndex.get(CAP_DRIVE_LETTER),
        captionToIndex.get(CAP_DRIVE_TYPE));
    return str.trim();
  }
 
  private static String parseVolumeCaption(String line,
      Map<String, Integer> captionToIndex) {
    String str = line.substring(captionToIndex.get(CAP_CAPTION),
        captionToIndex.get(CAP_DRIVE_LETTER));
    return str.trim();
  }
 
  private static long parseVolumeCapacity(String line,
      Map<String, Integer> captionToIndex) {
    String str = line.substring(captionToIndex.get(CAP_CAPACITY),
        captionToIndex.get(CAP_CAPTION));
    return Long.parseLong(str.trim());
  }
 
  private static long parseVolumeFreeSpace(String line,
      Map<String, Integer> captionToIndex) {
    String str = line.substring(captionToIndex.get(CAP_FREE_SPACE));
    return Long.parseLong(str.trim());
  }
 
  private static class DiskUsageComparator implements Comparator<DiskUsage> {

	public int compare(DiskUsage o1, DiskUsage o2) {
		// TODO Auto-generated method stub
		return o1.caption.compareTo(o2.caption);
	}
  }
 
  private static class DiskUsage {
    public String caption;
    public double usage;
 
    public DiskUsage(String caption, Double usage) {
      this.caption = caption;
      this.usage = usage;
    }
  }

  /**
   * 获取性能数据，假装的
   * 临时获取的不需要存入数据库
   * @return
   */
public static CollectionInfo getRatios(DevResources dev) {
	CollectionInfo info = new CollectionInfo();
	try {
		info.setCpuRatio(getCpuLoadPercentage());
		info.setDev(dev);
		info.setCreateTime(new Date());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return info;
}
}