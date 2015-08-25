package net.ynotapps.skrypt.utils;

import java.util.Arrays;
import java.util.List;

public class PromptUtils {

    private List<String> promptList = Arrays.asList("pony", "train", "plane", "swimming", "island");
    private int index = 0;

    public String getNextPrompt(String skrypt) {
        boolean endOfList = index >= promptList.size();

        if (endOfList) {
            return "";
        }

        String prompt = promptList.get(index);
        boolean alreadyUsed = skryptContains(skrypt, prompt);
        String result = alreadyUsed ? "" : promptList.get(index);
        index++;
        return result;

    }

    // Checks whether prompt is in skrypt
    public boolean skryptContains(String skrypt, String prompt) {
        return skrypt.contains(prompt);
    }

    public void reset() {
        index = 0;
    }
}
