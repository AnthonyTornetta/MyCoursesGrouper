/**
 * Made by: Anthony Tornetta (act5373@rit.edu)
 * 3/31/2022
 */

package com.cornchipss.mover;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import net.lingala.zip4j.ZipFile;

public class Mover
{	
	/**
	 * join(["a", "b", "c"], 1, " ") => "b c"
	 * @param args The string array to join
	 * @param index Where to start joining
	 * @param on The String to join them by
	 * @return The joined string (see example)
	 */
	private static String join(String[] args, int index, String on)
	{
		StringBuilder sbuilder = new StringBuilder();
		
		sbuilder.append(args[index]);
		
		for(index++; index < args.length; index++)
		{
			sbuilder.append(on);
			sbuilder.append(args[index]);	
		}
		
		return sbuilder.toString();
	}
	
	/**
	 * How it works: 
	 * It asks what directory all the MyCourses stuff is downloaded from.
	 * This should be the extracted folder's directory.
	 * 
	 * Asks the output directory:
	 * This is where the nice organized result should go.  It is safe to make the input + output directory the same.
	 * 
	 * @param args ignored
	 * @throws IOException If something bad happens
	 */
	public static void main(String[] args) throws IOException
	{
		System.out.println("My Courses Grouper - By Anthony Tornetta (act5373@rit.edu)");
		System.out.println();
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Starting directory: ");
		String startingDir = scan.nextLine();
		startingDir += "/";
		
		System.out.print("Output directory: ");
		String outputDirInput = scan.nextLine();
		
		System.out.print("Unzip zips? [(y)/n]: ");
		boolean unzipZips = !scan.nextLine().toLowerCase().equals("n");
		
		scan.close();

		File outputDirFile = new File(outputDirInput);
		if(!outputDirFile.exists())
		{
			System.out.print("That output directory does not exist! Would you like to create it? [(y)/n]: ");
			
			String input = scan.nextLine();
			if(input.equals("n") || input.equals("N"))
				return;
			
			outputDirFile.mkdirs();
		}
		else if(!outputDirFile.isDirectory())
		{
			System.out.println("Not a valid output directory!");
			return;
		}
				
		String outputDirStr = outputDirFile.getAbsolutePath() + "/";
		
		for(File f : new File(startingDir).listFiles())
		{
			if(f.getName().equals("index.html"))
				continue;
			
			String[] split = f.getName().split("-");
			
			if(split.length < 3)
				continue;
			
			String username = split[2].trim();
			
			System.out.println("Processing " + username + "...");
			
			String type = f.getName().substring(f.getName().lastIndexOf(".") + 1);
			
			File outputDir = new File(outputDirStr + username);
			outputDir.mkdirs();
			
			switch(type)
			{
				case "zip":
				{
					if(unzipZips)
					{
						new ZipFile(f).extractAll(outputDirStr + outputDir.getName() + "/");
						break;
					}
				}
				default:
				{
					String name = join(split, 3, "-").trim();
					File output = new File(outputDirStr + outputDir.getName() + "/" + name);
					output.createNewFile();
					Files.copy(f.toPath(), new FileOutputStream(output));
					break;
				}
			}
		}
		
		System.out.println("Done!");
	}
}
