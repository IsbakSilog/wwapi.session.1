package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String continueGame;

        do {
            // Fetch jokes from API
            String jokeJson = new RestTemplate().getForObject(
                    "https://official-joke-api.appspot.com/jokes/ten", String.class
            );

            JsonArray jokeResponse = JsonParser.parseString(jokeJson).getAsJsonArray();
            String setup = jokeResponse.get(5).getAsJsonObject().get("setup").getAsString();
            String punchline = jokeResponse.get(5).getAsJsonObject().get("punchline").getAsString();
            System.out.println(setup + ", " + punchline);

            // Randomly generate character IDs
            Random random = new Random();
            int char1ID = random.nextInt(1, 732);
            int char2ID = random.nextInt(1, 732);

            // Superhero API URLs
            String urlChar1 = "https://superheroapi.com/api/385577e53eb499e6f18e1e6a9d12a313/" + char1ID;
            String urlChar2 = "https://superheroapi.com/api/385577e53eb499e6f18e1e6a9d12a313/" + char2ID;

            // Fetch superhero data
            String char1Json = new RestTemplate().getForObject(urlChar1, String.class);
            String char2Json = new RestTemplate().getForObject(urlChar2, String.class);

            JsonObject char1Object = JsonParser.parseString(char1Json).getAsJsonObject();
            JsonObject char2Object = JsonParser.parseString(char2Json).getAsJsonObject();

            System.out.println(char1Object.get("name").getAsString() + " vs " + char2Object.get("name").getAsString());

            int winnerScore = 0;
            Set<String> powerstats = char1Object.get("powerstats").getAsJsonObject().keySet();

            // Compare powerstats
            for (String stat : powerstats) {
                String char1Stat = char1Object.get("powerstats").getAsJsonObject().get(stat).getAsString();
                String char2Stat = char2Object.get("powerstats").getAsJsonObject().get(stat).getAsString();

                if ("null".equals(char1Stat)) {
                    if (!"null".equals(char2Stat)) {
                        winnerScore -= 1;
                    }
                } else if ("null".equals(char2Stat)) {
                    winnerScore += 1;
                } else {
                    int char1Value = Integer.parseInt(char1Stat);
                    int char2Value = Integer.parseInt(char2Stat);

                    if (char1Value > char2Value) {
                        winnerScore += 1;
                    } else if (char1Value < char2Value) {
                        winnerScore -= 1;
                    }
                }
            }

            // Determine the winner
            if (winnerScore > 0) {
                System.out.println(char1Object.get("name").getAsString() + " wins!");
            } else if (winnerScore < 0) {
                System.out.println(char2Object.get("name").getAsString() + " wins!");
            } else {
                System.out.println("It's a tie!");
            }

            // Ask user if they want to continue
            System.out.print("Do you want to play again? (y/n): ");
            continueGame = scanner.nextLine();

        } while ("y".equalsIgnoreCase(continueGame));

        System.out.println("Thanks for playing!");
    }
}
