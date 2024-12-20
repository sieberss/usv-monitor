package de.sieberss.backend.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class FallbackPageTest {

    @Test
    void expectRelativeResource_ifItExists() throws IOException {

        // GIVEN
        var resolver = new FallbackPage.ReactRoutingPathResourceResolver();
        var location = mock(Resource.class);
        var relativeLocation = mock(Resource.class);
        var resourcePath = "index.html";
        when(location.createRelative(resourcePath)).thenReturn(relativeLocation);
        when(relativeLocation.exists()).thenReturn(true);
        when(relativeLocation.isReadable()).thenReturn(true);

        // WHEN
        var actual = resolver.getResource(resourcePath, location);

        // THEN
        Assertions.assertEquals(relativeLocation, actual);
    }

    @Test
    void expectIndexHtml_ifRequestedResourceDoesNotExist() throws IOException {

        // GIVEN
        var resolver = new FallbackPage.ReactRoutingPathResourceResolver();
        var location = mock(Resource.class);
        var relativeLocation = mock(Resource.class);
        var resourcePath = "index.html";
        when(location.createRelative(resourcePath)).thenReturn(relativeLocation);
        when(relativeLocation.exists()).thenReturn(false);

        // WHEN
        var actual = resolver.getResource(resourcePath, location);

        // THEN
        ClassPathResource expected = new ClassPathResource("static/index.html");
        Assertions.assertEquals(expected, actual);
    }

}