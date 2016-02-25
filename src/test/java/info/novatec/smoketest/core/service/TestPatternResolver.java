package info.novatec.smoketest.core.service;

import com.beust.jcommander.internal.Lists;
import org.testng.annotations.Test;

import java.util.Deque;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class TestPatternResolver {


    @Test
    public void testPattern() {

        String pattern = "test_{#first}_{#third.{#second}}_{#fourth}";

        Deque<Expression> expressions = Lists.newLinkedList();
        Deque<Integer> opens = Lists.newLinkedList();

        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '{') {
            } else if (pattern.charAt(i) == '}') {
            }
        }

    }


    private static class Expression {

        public String pattern;
        public Expression parent;

        public Expression(String pattern) {
            this(pattern, null);
        }

        public Expression(String pattern,
                          Expression parent) {
            this.pattern = pattern;
            this.parent = parent;
        }

        public String resolve() {
            return "";
        }
    }


    private static class Parser {

        public String parse(final String expression) {


            return null;
        }

    }

}
