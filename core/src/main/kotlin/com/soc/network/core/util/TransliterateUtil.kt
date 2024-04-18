package com.soc.network.core.util

import org.springframework.stereotype.Component

@Component
class TransliterateUtil {
    companion object {
        @JvmStatic
        private val charMatches: Map<Char, String> = mapOf(
            'а' to "a",
            'б' to "b",
            'в' to "v",
            'г' to "g",
            'д' to "d",
            'е' to "e",
            'ё' to "e",
            'ж' to "zh",
            'з' to "z",
            'и' to "i",
            'й' to "y",
            'к' to "k",
            'л' to "l",
            'м' to "m",
            'н' to "n",
            'о' to "o",
            'п' to "p",
            'р' to "r",
            'с' to "s",
            'т' to "t",
            'у' to "u",
            'ф' to "f",
            'х' to "h",
            'ц' to "c",
            'ч' to "ch",
            'ш' to "sh",
            'щ' to "sch",
            'ъ' to "",
            'ы' to "i",
            'ь' to "",
            'э' to "e",
            'ю' to "yu",
            'я' to "ya")

        @JvmStatic
        fun transliterate(source: String): String {
            return source
                .filterNot { it.isWhitespace() }
                .lowercase()
                .map { charMatches[it] }
                .joinToString("")
        }
    }
}