package com.axibase.tsd.api.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AtsdText {

    private static final List<Character> SAFE_CHARACTERS = characterRanges(
            new int[][]{
                    {0x21},
                    {0x23, 0x3C},
                    {0x3E, 0x7E},
                    {0x80, 0xFF},
                    {0x100, 0x17F},
                    {0x370, 0x3FF},
                    {0x400, 0x4FF},
                    {0x500, 0x52F},
                    {0x4E00, 0x9FFF}
            }
    );

    private static final List<Character> NAME_CHARACTERS = uniteClasses(
            SAFE_CHARACTERS,
            individualCharacters(0x22, 0x3D)
    );

    private static List<Character> uniteClasses(List<Character>... classes) {
        List<Character> characters = new ArrayList<>();
        for (List<Character> charClass : classes) {
            characters.addAll(charClass);
        }
        return Collections.unmodifiableList(characters);
    }

    private static List<Character> individualCharacters(int... characters) {
        List<Character> characterList = new ArrayList<>();
        for (int character : characters) {
            characterList.add((char) character);
        }
        return Collections.unmodifiableList(characterList);
    }

    private static List<Character> characterRanges(int[][] ranges) {
        List<Character> characters = new ArrayList<>();
        for (int[] range : ranges) {
            if (range.length == 1) {
                characters.add((char) range[0]);
            } else {
                characters.addAll(characterRange(range[0], range[1]));
            }
        }
        return Collections.unmodifiableList(characters);
    }

    private static List<Character> characterRange(int left, int right) {
        List<Character> characters = new ArrayList<>();
        for (int c = left; c <= right; c++)
            characters.add((char) c);
        return Collections.unmodifiableList(characters);
    }

    private AtsdText() {
    }
}
