import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class CipherScheme implements Serializable {
    private final Map<Character, Character> scheme;

    public CipherScheme(String letters, String counterparts) throws IllegalArgumentException {
        List<Character> letterList = parseString(letters);
        List<Character> counterpartList = parseString(counterparts);
        scheme = new TreeMap<>();
        if (letterList.size() != counterpartList.size()) {
            throw new IllegalArgumentException("String lengths don't match");
        }
        for (int i = 0; i < letterList.size(); i++) {
            if (scheme.containsKey(letterList.get(i))) {
                throw new IllegalArgumentException("Duplicated characters in the alphabet");
            }
            else {
                scheme.put(letterList.get(i), counterpartList.get(i));
            }
        }
    }

    public Character convertLetter(Character c) {
	char cNormalized = Character.toLowerCase(c);
        if (scheme.containsKey(cNormalized)) {
            return Character.isLowerCase(c) ?
		    scheme.get(cNormalized) :
		    Character.toUpperCase(scheme.get(cNormalized));
        } else return c;
    }

    private List<Character> parseString(String line) {
        return Arrays.stream(line.split("\\s")).map(s -> Character.toLowerCase(s.charAt(0))).collect(Collectors.toList());
    }
}
