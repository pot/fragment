package dev.nateweisz.fragment.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CharacterUtils {
    // the start of the Unicode private use area section
    private static int CURRENT_CHAR = 42000;

    public static char generateNextCharacter() {
        return Character.toChars(CURRENT_CHAR++)[0];
    }
}
