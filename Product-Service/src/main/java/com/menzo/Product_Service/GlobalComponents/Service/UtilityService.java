package com.menzo.Product_Service.GlobalComponents.Service;

import com.menzo.Product_Service.Category.Repo.CategoriesRepo;
import com.menzo.Product_Service.Variation.Repo.ColorCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UtilityService {

    private static final Logger logger = LoggerFactory.getLogger(UtilityService.class);

    @Autowired
    private ColorCodeRepository colorCodeRepo;

    @Autowired
    private CategoriesRepo categoriesRepo;

    /*
     *
     * Generate Abbreviation for String
     * Used to create Abbreviated unique identity code - for SKUs
     * For Details like - color, sub-category, etc
     *
     * Abbreviation built with 1st character of all words - for 'sub-category'
     *
     * Abbreviation built with 1st & last character of Last word - for 'Colors',
     * concated with the first character of all the pre-last words
     *
     * TESTED
     */
    public String generateAbbreviation(String field, String content) {
        StringBuilder abbreviation = new StringBuilder();
        List<Character> vowels = new ArrayList<>(List.of('A', 'E', 'I', 'O', 'U'));

        // Splitting uppercased words in the given String 'content'
        String cont = content.toUpperCase();
        String[] contArray = cont.split(" ");
        char lastChar = 'a';

        // Building abbreviation
        if (field.equals("Colors")) {
            logger.info("Abbreviating the new color: " + content);
            for (int i = contArray[contArray.length - 1].length() - 1; i >= 0; i--) {
                if (vowels.contains(contArray[contArray.length - 1].charAt(i))) continue;
                lastChar = contArray[contArray.length - 1].charAt(i);
                break;
            }
            for (String s : contArray) abbreviation.append(s.charAt(0));
            abbreviation.append(lastChar);

            return ensureUniqueAbbreviation("Colors", abbreviation);
        } else if (field.equals("sub-category")) {
            logger.info("Abbreviating the new sub-category: " + content);
            for (String s : contArray) abbreviation.append(s.charAt(0));
            return ensureUniqueAbbreviation("sub-category", abbreviation);
        } else {
            return null;
        }
    }

    // ensure the abbreviation is unique - TESTED
    private String ensureUniqueAbbreviation(String field, StringBuilder abbreviation) {
        boolean abbreviationExists = false;
        if (field.equals("Colors")) {
            try{
                abbreviationExists = colorCodeRepo.existsByColorAbbreviation(abbreviation.toString());
            } catch (RuntimeException e) {
                throw new RuntimeException("Colors - abbreviation exists check error.", e);
            }
        }
        else if (field.equals("sub-category")) {
            try{
                abbreviationExists = categoriesRepo.existsByAbbreviation(abbreviation.toString());
            } catch (RuntimeException e) {
                throw new RuntimeException("Sub-category - abbreviation exists check error.", e);
            }
        }
//        System.out.println("Exists: " + abbreviationExists + " : " + abbreviation.toString());
        if (!abbreviationExists) return abbreviation.toString();
        else {
            char[] charArray = abbreviation.toString().toCharArray();
            int digitCount = 0;
            for (int i = charArray.length - 1; i >= 0; i--) {
                if (Character.isDigit(charArray[i])) digitCount++;
                else break;
            }
            if (digitCount == 0) abbreviation.append(1);
            else {
                int suffixInteger = Integer.valueOf(abbreviation.substring(abbreviation.length() - digitCount));
                abbreviation.replace(
                        abbreviation.length() - digitCount,
                        abbreviation.length(),
                        String.valueOf(++suffixInteger)
                );
            }
//            System.out.println("RECURSION WHILE ABB CHECK");
            return ensureUniqueAbbreviation(field, abbreviation);
        }
    }

}
