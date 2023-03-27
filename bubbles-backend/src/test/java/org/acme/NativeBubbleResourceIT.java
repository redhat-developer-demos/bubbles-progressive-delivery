package org.acme;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeBubbleResourceIT extends BubbleResourceTest {

    // Execute the same tests but in native mode.
}