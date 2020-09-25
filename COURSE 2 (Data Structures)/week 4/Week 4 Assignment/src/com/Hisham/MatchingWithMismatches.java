package com.Hisham;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class MatchingWithMismatches {
    private static class Solver {
        private static int MULTIPLIER = 31;
        private static long FIRST_PRIME = 1000000007;
        private static long SECOND_PRIME = 1000000009;

        private long[] hashesOfTextFromFirstFunction;
        private long[] hashesOfTextFromSecondFunction;
        private long[] hashesOfPatternFromFirstFunction;
        private long[] hashesOfPatternFromSecondFunction;

        private int threshold;
        private String text;
        private String pattern;

        Solver(int threshold, String text, String pattern) {
            this.threshold = threshold;
            this.text = text;
            this.pattern = pattern;
            hashesOfTextFromFirstFunction = computeHashes(text, FIRST_PRIME);
            hashesOfTextFromSecondFunction = computeHashes(text, SECOND_PRIME);
            hashesOfPatternFromFirstFunction = computeHashes(pattern, FIRST_PRIME);
            hashesOfPatternFromSecondFunction = computeHashes(pattern, SECOND_PRIME);
        }

        private List<Integer> solve() {
            ArrayList<Integer> indices = new ArrayList<>();
            for (int i = 0; i <= text.length() - pattern.length(); i++) {
                int numberOfMismatches = getNumberOfMismatches(i, pattern.length(), i, 0);

                if (numberOfMismatches <= threshold)
                    indices.add(i);
            }

            return indices;
        }

        private long[] computeHashes(String string, long prime) {
            long[] hashes = new long[string.length() + 1];
            for (int i = 1; i < hashes.length; i++) {
                hashes[i] = (((MULTIPLIER * hashes[i - 1]) % prime) + prime + string.charAt(i - 1)) % prime;
            }
            return hashes;
        }

        private int getNumberOfMismatches(int index, int length, int iteration, int numberOfMismatches) {
            if (numberOfMismatches > threshold) return numberOfMismatches;

            if (length == 1 && notEqual(index, index - iteration, length)) {
                return numberOfMismatches + 1;
            } else {
                int mid = length / 2;
                if (notEqual(index, index - iteration, mid)) {
                    numberOfMismatches = getNumberOfMismatches(index, mid, iteration, numberOfMismatches);
                }

                int textIndex = index + mid;
                int newLength = length - mid;
                if (numberOfMismatches <= threshold && notEqual(textIndex, textIndex - iteration, newLength)) {
                    return getNumberOfMismatches(textIndex, newLength, iteration, numberOfMismatches);
                } else {
                    return numberOfMismatches;
                }
            }
        }

        private boolean notEqual(int textIndex, int patternIndex, int length) {
            long multiplierInPowerModFirstPrime = getMultiplierInPowerModPrime(length, FIRST_PRIME);
            long textSubStringFirstHash = getSubstringHash(textIndex, length,
                    hashesOfTextFromFirstFunction, FIRST_PRIME, multiplierInPowerModFirstPrime);
            long patternSubstringFirstHash = getSubstringHash(patternIndex, length,
                    hashesOfPatternFromFirstFunction, FIRST_PRIME, multiplierInPowerModFirstPrime);

            if (textSubStringFirstHash == patternSubstringFirstHash) {
                long multiplierInPowerModSecondPrime = getMultiplierInPowerModPrime(length, SECOND_PRIME);
                long textSubStringSecondHash = getSubstringHash(textIndex, length,
                        hashesOfTextFromSecondFunction, SECOND_PRIME, multiplierInPowerModSecondPrime);
                long patternSubstringSecondHash = getSubstringHash(patternIndex, length,
                        hashesOfPatternFromSecondFunction, SECOND_PRIME, multiplierInPowerModSecondPrime);

                return textSubStringSecondHash != patternSubstringSecondHash;
            }
            return true;
        }

        private long getSubstringHash(int index, int length, long[] hashes, long prime, long multiplierInPowerLengthModPrime) {
            long result = hashes[index + length] - ((multiplierInPowerLengthModPrime * hashes[index]) % prime);
            if (result < 0) result += prime;
            return result;
        }

        private long getMultiplierInPowerModPrime(int exponent, long prime) {
            long result = 1;
            long base = MULTIPLIER;
            while (exponent > 0) {
                if (exponent % 2 == 1) {
                    result = (result * base) % prime; // multiplying with base
                }
                base = (base * base) % prime; // squaring the base
                exponent /= 2;
            }
            return result % prime;
        }
    }

    private void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        in.lines().forEach(line -> {
            if (line != null && !line.isEmpty()) {
                StringTokenizer tokenizer = new StringTokenizer(line);
                int k = Integer.parseInt(tokenizer.nextToken());
                String string = tokenizer.nextToken();
                String pattern = tokenizer.nextToken();
                List<Integer> answer = new Solver(k, string, pattern).solve();
                out.format("%d ", answer.size());
                out.println(answer.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(" "))
                );
            }
        });
        out.close();
    }

    static public void main(String[] args) {
        new MatchingWithMismatches().run();
    }
}
