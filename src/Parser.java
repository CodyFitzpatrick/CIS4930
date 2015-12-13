import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Parser {
	
		public static void main(String[] args) throws FileNotFoundException, IOException {
			//outputRandomMoviesToTxt(); //output to random.txt
			//read the random.txt to get movie object array
			//title and year
			Map<String, Integer> map = new HashMap<String, Integer>();
			Movie[] movies = populateRandomMovies(map);
			System.out.println(movies.length);
			System.out.println(movies[0].title);
			System.out.println(movies[0].year);
			
			Map<String, Integer> movieYearMap = new HashMap<String, Integer>();
			for(int i = 0; i < 10000; i++) {
				movieYearMap.put(movies[i].title, i);
			}
			//map with generic titles and arraylist of actual titles it corresponds to
			Map<String, List<String>> actualTitlesMap = new HashMap<String, List<String>>();
			//populate just year and title by looking at last closing peren
			for(int i = 0; i < 10000; i++) {
				String title = movies[i].title;
				int j = title.indexOf(')');
				String strToAdd = title.substring(0, j + 1);
				if(!actualTitlesMap.containsKey(strToAdd)) {
					List<String> list = new ArrayList<String>();
					list.add(title);
					actualTitlesMap.put(strToAdd, list);
				} else {
					List<String> list = actualTitlesMap.get(strToAdd);
					list.add(title);
					actualTitlesMap.put(strToAdd, list);
				}
				
			}
			
			populateGenres(movies, map, actualTitlesMap);
			System.out.println(movies[0].genre);
			System.out.println(movies[9999].title);
			System.out.println(movies[9999].genre);
			
			//if no genre, it is populated with null, so when output output "N/A"
			
			//name then tabs then movies with newlines until next name
			//name has comma in it, first char isnt whitespace for the line
			
			populateDirectors(movies, map);
			System.out.println(movies[0].director);
			System.out.println(movies[9999].director);
			
			populateProducers(movies, map);
			System.out.println(movies[0].producer);
			System.out.println(movies[9999].producer);

			
			populateActors(movies, map);
			System.out.println(movies[0].actor);
			System.out.println(movies[9999].actor);
			
			populateActresses(movies, map);
			System.out.println(movies[0].actress);
			System.out.println(movies[300].title);
			System.out.println(movies[300].actress);
			
			//get ratings
			
			populateRatings(movies, movieYearMap);
			System.out.println(movies[10].title);
			System.out.println(movies[10].rating);
			System.out.println(movies[300].title);
			System.out.println(movies[300].rating);
			
			//get country
			
			populateCountries(movies, movieYearMap);
			System.out.println(movies[0].title);
			System.out.println(movies[0].country);
			System.out.println(movies[5006].title);
			System.out.println(movies[5006].country);
			
			//Now, output all attributes to a file tab separated
			//have the first row be the label, so first output:
			//Title, Year, Genre, Director, Executive Producer, Main Actor, Main Actress, Rating, Country
			outputTabSeparated(movies);
		}
		
		public static void outputTabSeparated(Movie[] movies) throws FileNotFoundException, IOException {
			PrintWriter writer = new PrintWriter("output2.txt");

			writer.println("Title\tYear\tGenre\tDirector\tExecutive Producer\tMain Actor\tMain Actress\tRating\tCountry");
			
			for(int i = 0; i < movies.length ; i++) {
				writer.print(movies[i].title + "\t");
				writer.print(movies[i].year + "\t");
				writer.print(movies[i].genre + "\t");
				writer.print(movies[i].director + "\t");
				writer.print(movies[i].producer + "\t");
				writer.print(movies[i].actor + "\t");
				writer.print(movies[i].actress + "\t");
				writer.print(movies[i].rating + "\t");
				writer.println(movies[i].country);
			}
			writer.close();
		}
		
		public static void populateCountries(Movie[] movies, Map<String, Integer> map) throws IOException {
			File file = new File("countries.txt");
			int lineNum = 1;
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			    for(String line; (line = br.readLine()) != null; lineNum++) {
			    	if(lineNum < 15) {
			    		continue;
			    	}
			    	
			    	//first, check if beginning to first tab is in map
			    	int j = line.indexOf('\t');
			    	if(j <= 0) {
			    		continue;
			    	}
			    	String movieWithYear = line.substring(0, j);
			    	if(map.containsKey(movieWithYear)) {
			    		//if it is, get last index of tab + 1 to end is country
			    		j = line.lastIndexOf('\t');
			    		if(j <= 0) {
				    		continue;
				    	}
			    		String country = line.substring(j + 1, line.length());
			    		movies[map.get(movieWithYear)].country = country;
			    	}
			    	
			    }
			}
		}
		
		public static void populateRatings(Movie[] movies, Map<String, Integer> map) throws IOException {
			File file = new File("ratings.txt");
			int lineNum = 1;
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			    for(String line; (line = br.readLine()) != null; lineNum++) {
			    	if(lineNum < 29) {
			    		continue;
			    	}
			    	//split if 2 or more spaces
			    	String[] words = line.split("\\s{2,}");
			    	//index 3 has rating and index 4 is the movie w/ year
			    	
			    	//check if index 4 is in the map, and if so then assign it the rating
			    	if(words.length >= 5 && map.containsKey(words[4])) {
			    		String rating = words[3];
			    		movies[map.get(words[4])].rating = rating;
			    	}
			    }
			}
		}
		
		public static String[] getMoviesWithYear() throws FileNotFoundException, IOException {
			//title with year already in title, so iterate over the movies
				File file = new File("random2.txt");
				String[] movies = new String[10000];
				int index = 0;
				try(BufferedReader br = new BufferedReader(new FileReader(file))) {
				    for(String line; (line = br.readLine()) != null; index++) {
				        //add title and year to Movie objects
				    	movies[index] = line;
				    }
				}
			return movies;
		}
		
		//IMPORTANT: actor and actress have billing number at very end of line <??>
		//find the actor and actress for each movie with the number closest to 1?
		//search for executive producer for producer?
		//directors has no info.
		
		public static void populateActresses(Movie[] movies, Map<String, Integer> map) throws IOException {
			File file = new File("actresses.txt");
			int lineNum = 1;
			String actress = "";
			String title = "";
			String[] words = new String[2];
			
			boolean justUpdated = false;
			
			Map<String, Integer> billingMap = new HashMap<String, Integer>();
			
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			    for(String line; (line = br.readLine()) != null; lineNum++) {
			    	if(lineNum >= 242) {
			    		if(line.length() > 0) {
				    		if(line.charAt(0) != '\t') {
				    			//update director name
				    			words = line.split("\t");
				    			actress = words[0];
				    			justUpdated = true;
				    			//words[1] is a movie,
				    		} 
				    		//is a movie on this line, compare movie title
				    		if(justUpdated) { //check if just updated and use words[1] as movie title
				    			if(words.length > 1) {
				    				title = words[1];
				    				int j = title.indexOf(')');
				    				int otherJ = title.lastIndexOf('}');
							    	if(j < otherJ) {
							    		j = otherJ;
							    	}
				    				if(j > 0) {
				    					title = title.substring(0, j + 1);
				    				}
				    			} else {
				    				title = "";
				    			}
				    		} else {
				    			int j = line.indexOf(')');
				    			int otherJ = line.lastIndexOf('}');
						    	if(j < otherJ) {
						    		j = otherJ;
						    	}
				    			if(j > 0) {
				    				title = line.substring(0, j + 1);
					    			//remove leading whitespace
					    			title = title.replaceAll("^\\s+",""); 
				    			} else {
				    				title = "";
				    			}
				    		}
				    		justUpdated = false;
				    		if(!map.containsKey(title)) {
					    		continue;
					    	}
				    		//get rank by checking <??>, which should be last
				    		int billingNum = Integer.MAX_VALUE;
				    		int j = line.lastIndexOf('<');
				    		if(j > 0) {
				    			//number exists, so get the number
				    			billingNum = Integer.parseInt(line.substring(j + 1, line.length() - 1));
				    		}
				    		//check if billing map has the entry or not, if it doesn't then add it
				    		//if it has it then the num needs to be less than the entry
				    		if(!billingMap.containsKey(title) || billingNum < billingMap.get(title)) {
				    			billingMap.put(title, billingNum);
				    			//now, set index of title movie.director to the director
					    		movies[map.get(title)].actress = actress;
				    		}
				    		
			    		}
			    	}
			    }
			}
		}
		
		public static void populateActors(Movie[] movies, Map<String, Integer> map) throws IOException {
			File file = new File("actors.txt");
			int lineNum = 1;
			String actor = "";
			String title = "";
			String[] words = new String[2];
			
			boolean justUpdated = false;
			
			Map<String, Integer> billingMap = new HashMap<String, Integer>();
			
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			    for(String line; (line = br.readLine()) != null; lineNum++) {
			    	if(lineNum >= 240) {
			    		if(line.length() > 0) {
				    		if(line.charAt(0) != '\t') {
				    			//update director name
				    			words = line.split("\t");
				    			actor = words[0];
				    			justUpdated = true;
				    			//words[1] is a movie,
				    		} 
				    		//is a movie on this line, compare movie title
				    		if(justUpdated) { //check if just updated and use words[1] as movie title
				    			if(words.length > 1) {
				    				title = words[1];
				    				int j = title.lastIndexOf(')');
				    				int otherJ = title.lastIndexOf('}');
							    	if(j < otherJ) {
							    		j = otherJ;
							    	}
				    				if(j > 0) {
				    					title = title.substring(0, j + 1);
				    				}
				    			} else {
				    				title = "";
				    			}
				    		} else {
				    			int j = line.indexOf(')');
				    			int otherJ = line.lastIndexOf('}');
						    	if(j < otherJ) {
						    		j = otherJ;
						    	}
				    			if(j > 0) {
				    				title = line.substring(0, j + 1);
					    			//remove leading whitespace
					    			title = title.replaceAll("^\\s+",""); 
				    			} else {
				    				title = "";
				    			}
				    		}
				    		justUpdated = false;
				    		if(!map.containsKey(title)) {
					    		continue;
					    	}
				    		//get rank by checking <??>, which should be last
				    		int billingNum = Integer.MAX_VALUE;
				    		int j = line.lastIndexOf('<');
				    		if(j > 0) {
				    			//number exists, so get the number
				    			billingNum = Integer.parseInt(line.substring(j + 1, line.length() - 1));
				    		}
				    		//check if billing map has the entry or not, if it doesn't then add it
				    		//if it has it then the num needs to be less than the entry
				    		if(!billingMap.containsKey(title) || billingNum < billingMap.get(title)) {
				    			billingMap.put(title, billingNum);
				    			//now, set index of title movie.director to the director
					    		movies[map.get(title)].actor = actor;
				    		}
			    		}
			    	}
			    }
			}
		}
		
		public static void populateProducers(Movie[] movies, Map<String, Integer> map) throws IOException {
			File file = new File("producers.txt");
			int lineNum = 1;
			String producer = "";
			String title = "";
			String[] words = new String[2];
			
			boolean justUpdated = false;
			
			Map<String, Boolean> execMap = new HashMap<String, Boolean>();
			
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			    for(String line; (line = br.readLine()) != null; lineNum++) {
			    	if(lineNum >= 220) {
			    		if(line.length() > 0) {
				    		if(line.charAt(0) != '\t') {
				    			//update director name
				    			words = line.split("\t");
				    			producer = words[0];
				    			justUpdated = true;
				    			//words[1] is a movie,
				    		} 
				    		//is a movie on this line, compare movie title
				    		if(justUpdated) { //check if just updated and use words[1] as movie title
				    			if(words.length > 1) {
				    				title = words[1];
				    				int j = title.lastIndexOf(')');
				    				int otherJ = title.lastIndexOf('}');
							    	if(j < otherJ) {
							    		j = otherJ;
							    	}
				    				if(j > 0) {
				    					title = title.substring(0, j + 1);
				    				}
				    			} else {
				    				title = "";
				    			}
				    		} else {
				    			int j = line.indexOf(')');
				    			int otherJ = line.lastIndexOf('}');
						    	if(j < otherJ) {
						    		j = otherJ;
						    	}
				    			if(j > 0) {
				    				title = line.substring(0, j + 1);
					    			//remove leading whitespace
					    			title = title.replaceAll("^\\s+",""); 
				    			} else {
				    				title = "";
				    			}
				    		}
				    		justUpdated = false;
				    		if(!map.containsKey(title)) {
					    		continue;
					    	}
				    		if(!execMap.containsKey(title)) {
				    			//now, set index of title movie.director to the director
					    		movies[map.get(title)].producer = producer;
					    		if(line.endsWith("(executive producer)")) {
					    			//it's exec, add true to map 
					    			execMap.put(title, Boolean.TRUE);
					    		} 
				    		}
			    		}
			    	}
			    }
			}
		}
		
		public static void populateDirectors(Movie[] movies, Map<String, Integer> map) throws IOException {
			File file = new File("directors.txt");
			int lineNum = 1;
			String director = "";
			String title = "";
			String[] words = new String[2];
			
			boolean justUpdated = false;
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			    for(String line; (line = br.readLine()) != null; lineNum++) {
			    	if(lineNum >= 236) {
			    		if(line.length() > 0) {
				    		if(line.charAt(0) != '\t') {
				    			//update director name
				    			words = line.split("\t");
				    			director = words[0];
				    			justUpdated = true;
				    			//words[1] is a movie,
				    		} 
				    		//is a movie on this line, compare movie title
				    		if(justUpdated) { //check if just updated and use words[1] as movie title
				    			if(words.length > 1) {
				    				title = words[1];
				    				int j = title.lastIndexOf(')');
				    				int otherJ = title.lastIndexOf('}');
							    	if(j < otherJ) {
							    		j = otherJ;
							    	}
				    				if(j > 0) {
				    					title = title.substring(0, j + 1);
				    				}
				    			} else {
				    				title = "";
				    			}
				    		} else {
				    			int j = line.lastIndexOf(')');
				    			int otherJ = line.lastIndexOf('}');
						    	if(j < otherJ) {
						    		j = otherJ;
						    	}
				    			if(j > 0) {
				    				title = line.substring(0, j + 1);
					    			//remove leading whitespace
					    			title = title.replaceAll("^\\s+",""); 
				    			} else {
				    				title = "";
				    			}
				    		}
				    		justUpdated = false;
				    		if(!map.containsKey(title)) {
					    		continue;
					    	}
				    		//now, set index of title movie.director to the director
				    		movies[map.get(title)].director = director;
			    		}
			    	}
			    }
			}
		}
		
		//for genre, only general title is labeled as a genre
		//need to populate genre with generic title
		//if the generic title is a substring of the saved title, then add that genre
		
		//scan line by line, get generic title of the line
		public static void populateGenres(Movie[] movies, Map<String, Integer> map, Map<String, List<String>> actualMoviesMap) throws IOException {
			//get genre, right of closing bracket and trim whitespace
			//read genres.txt
			File file = new File("genres.txt");
			int lineNum = 1;
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			    for(String line; (line = br.readLine()) != null; lineNum++) {
			    	//iterate over each movie, if the title is in the map of all of the titles
			    	//then add the genre to the movie
			    	if(lineNum < 381) {
			    		continue;
			    	}
			    	//null if no genre
			    	//add to index of the title, so need hashmap with title,index
			    	int j = line.lastIndexOf(')');
			    	int otherJ = line.lastIndexOf('}');
			    	if(j < otherJ) {
			    		j = otherJ;
			    	}
			    	String title = line.substring(0, j + 1);
			    	//have title in file, now check if it is contained in generic title map
			    	if(!actualMoviesMap.containsKey(title)) {
			    		continue;
			    	}
			        //add genre to Movie objects
			    	String genre = line.substring(j + 1, line.length());
			    	//remove all spaces
			    	genre = genre.replaceAll("\\s+",""); 
			    	//get entire list of the actual movies names corresponding to generic title
			    	List<String> list = actualMoviesMap.get(title);
			    	for(int i = 0; i < list.size(); i++) {
			    		//get index of actual title map with movies, set genre for each in list
			    		movies[map.get(list.get(i))].genre = genre;
			    	}
			    }
			}
		}
		
		public static Movie[] populateRandomMovies(Map<String, Integer> map) throws FileNotFoundException, IOException {
			File file = new File("random2.txt");
			Movie[] movies = new Movie[10000];
			int index = 0;
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			    for(String line; (line = br.readLine()) != null; index++) {
			        //add title and year to Movie objects
			    	int j = line.indexOf('\t');
		        	String title = line.substring(0, j);
		        	j = line.lastIndexOf('(');
		        	String year = line.substring(j + 1, j + 5);
			    	movies[index] = new Movie(title);
			    	movies[index].year = year;
			    	map.put(title, index);
			    }
			}
			return movies;
		}
		
		public static void outputRandomMoviesToTxt() throws FileNotFoundException, IOException {
			File file = new File("movies.txt");
			//generate 10,000 random numbers between 16 and 3570077
			Set<Integer> set = new HashSet<Integer>();
			int count = 0;
			int low = 16; //16 to 3570077
			int high = 3570078;
			
			while(count < 10000) {
				Random r = new Random();
				int result = r.nextInt(high-low) + low;
				if(!set.contains(result)) {
					count++;
					set.add(result);
				}
			}
			
			//keep count of current line, if line number matches then parse for movie 
			//and output movie string with new line to random.txt
			PrintWriter writer = new PrintWriter("random2.txt");

			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
				int lineNum = 1;
			    for(String line; (line = br.readLine()) != null; lineNum++) {
			        if(set.contains(lineNum)) {
			        	//parse the line String, output to random.txt
			        	int index = line.indexOf('\t');
			        	String strToOutput = line.substring(0, index);
			        	index = line.lastIndexOf('\t');
			        	writer.println(strToOutput + '\t' + line.substring(index + 1, line.length()));
			        }
			    }
			    writer.close();
			}
			
		}
}
