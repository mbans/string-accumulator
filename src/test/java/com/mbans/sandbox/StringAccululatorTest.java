package com.mbans.sandbox;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by lumarmacy on 08/11/2018.
 */
public class StringAccululatorTest {

    private StringAccululator accululator = new StringAccululator();

    @Test
    public void emptyShouldReturnZero(){
        assertThat(accululator.add(""), is(0));
    }

    @Test
    public void shouldAddSimple() {
        assertThat(accululator.add("1,2"), is(3));
    }

    @Test
    public void canHandleNewLine() {
        assertThat(accululator.add("1\n2,3"), is(6));
    }

    @Test
    public void shouldSupportSingleDelimeterChange() {
        assertThat(accululator.add("//*\n1*2*3"), is(6));
        assertThat(accululator.add("//?123|&\n1?1232&3&1"), is(7));
    }

    @Test
    public void shouldSupportMultipleDelimeterChange() {
        assertThat(accululator.add("//*()|%xyz\n1*()2%xyz3"), is(6));
    }

    @Test(expected = RuntimeException.class)
    public void shouldExceptionWhenNegative() {
        assertThat(accululator.add("//*|%\n-1*2%3"), is(6));
    }

    @Test
    public void shouldIgnoreWhenLargerThan1000() {
        assertThat(accululator.add("//*|%\n1001*2%3"), is(5));
        assertThat(accululator.add("//*|%\n1000*2%3"), is(1005));
    }
}
